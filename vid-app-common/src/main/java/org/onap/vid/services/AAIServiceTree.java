package org.onap.vid.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.util.AAITreeConverter;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.parser.ServiceModelInflator;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.ServiceInstance;
import org.onap.vid.utils.Tree;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.onap.vid.services.AAITreeNodeBuilder.*;

@Component
public class AAIServiceTree {

    private final AAITreeNodeBuilder aaiTreeNodeBuilder;

    private final AAITreeConverter aaiTreeConverter;

    private final AaiClientInterface aaiClient;

    private final VidService sdcService;

    private final ServiceModelInflator serviceModelInflator;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AAIServiceTree.class);

    public static final Tree<AaiRelationship> AAI_TREE_PATHS =
            new Tree<>(new AaiRelationship(SERVICE_INSTANCE));

    static {
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(GENERIC_VNF, VG));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NETWORK));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(GENERIC_VNF, NETWORK));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(INSTANCE_GROUP));
    }

    @Inject
    public AAIServiceTree(AaiClientInterface aaiClient, AAITreeNodeBuilder aaiTreeNodeBuilder,
                          AAITreeConverter aaiTreeConverter, VidService sdcService,
                          ServiceModelInflator serviceModelInflator) {
        this.aaiClient = aaiClient;
        this.aaiTreeNodeBuilder = aaiTreeNodeBuilder;
        this.aaiTreeConverter = aaiTreeConverter;
        this.sdcService = sdcService;
        this.serviceModelInflator = serviceModelInflator;
    }

    public List<AAITreeNode> buildAAITree(String getUrl, Tree<AaiRelationship> pathsToSearch) {

        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = createNodesAccumulator();

        List<AAITreeNode> aaiTreeNodes = fetchAAITree(getUrl, pathsToSearch, nodesAccumulator, true);

        enrichNodesWithModelVersionAndModelName(nodesAccumulator);

        return aaiTreeNodes;
    }

    public ServiceInstance getServiceInstanceTopology(String globalCustomerId, String serviceType, String serviceInstanceId) {

        String getURL = "business/customers/customer/" +
                globalCustomerId + "/service-subscriptions/service-subscription/" +
                serviceType + "/service-instances/service-instance/" + serviceInstanceId;

        //Used later to get the nodes UUID
        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = createNodesAccumulator();

        AAITreeNode aaiTree = fetchAAITree(getURL, AAI_TREE_PATHS, nodesAccumulator, false).get(0);

        //Populate nodes with model-name & model-version (from aai)
        enrichNodesWithModelVersionAndModelName(nodesAccumulator);

        final ServiceModel serviceModel = getServiceModel(aaiTree.getModelVersionId());

        //Populate nodes with model-customization-name (from sdc model)
        enrichNodesWithModelCustomizationName(nodesAccumulator, serviceModel);

        return aaiTreeConverter.convertTreeToUIModel(aaiTree, globalCustomerId, serviceType, getInstantiationType(serviceModel));
    }

    private List<AAITreeNode> fetchAAITree(String getUrl, Tree<AaiRelationship> pathsToSearch,
                                           ConcurrentSkipListSet<AAITreeNode> nodesAccumulator, boolean partialTreeOnTimeout) {
        ThreadPoolExecutor threadPool = getThreadPool();

        List<AAITreeNode> aaiTree =  aaiTreeNodeBuilder.buildNode(SERVICE_INSTANCE,
                getUrl, defaultIfNull(nodesAccumulator, createNodesAccumulator()),
                threadPool, new ConcurrentLinkedQueue<>(),
                new AtomicInteger(0), pathsToSearch);

        boolean timeoutOccurred = waitForTreeFetch(threadPool);

        if (timeoutOccurred) {
            if (!partialTreeOnTimeout) {
                throw new GenericUncheckedException("Timeout on fetchAAITree. Fetched " + nodesAccumulator.size() + " nodes for url: " + getUrl);
            }
            LOGGER.warn(EELFLoggerDelegate.errorLogger, "Timeout on fetchAAITree for url: " + getUrl);
        }

        return aaiTree;
    }

    private ConcurrentSkipListSet<AAITreeNode> createNodesAccumulator() {
        return new ConcurrentSkipListSet<>(comparing(AAITreeNode::getUniqueNodeKey));
    }

    private String getInstantiationType(ServiceModel serviceModel) {
        if (serviceModel.getService() != null && serviceModel.getService().getInstantiationType() != null) {
            return serviceModel.getService().getInstantiationType();
        } else {
            return null;
        }
    }

    private ServiceModel getServiceModel(String modelVersionId) {
        try {
            final ServiceModel serviceModel = sdcService.getService(modelVersionId);
            if (serviceModel == null) {
                throw new GenericUncheckedException("Model version '" + modelVersionId + "' not found");
            }
            return serviceModel;
        } catch (AsdcCatalogException e) {
            throw new GenericUncheckedException("Exception while loading model version '" + modelVersionId + "'", e);
        }

    }

    void enrichNodesWithModelCustomizationName(Collection<AAITreeNode> nodes, ServiceModel serviceModel) {
        final Map<String, ServiceModelInflator.Names> customizationNameByVersionId = serviceModelInflator.toNamesByVersionId(serviceModel);

        nodes.forEach(node -> {
            final ServiceModelInflator.Names names = customizationNameByVersionId.get(node.getModelVersionId());
            if (names != null) {
                node.setKeyInModel(names.getModelKey());
                node.setModelCustomizationName(names.getModelCustomizationName());
            }
        });
    }


    private void enrichNodesWithModelVersionAndModelName(Collection<AAITreeNode> nodes) {

        Collection<String> invariantIDs = getModelInvariantIds(nodes);

        Map<String, String> modelVersionByModelVersionId = new HashMap<>();
        Map<String, String> modelNameByModelVersionId = new HashMap<>();

        JsonNode models = getModels(aaiClient, invariantIDs);
        for (JsonNode model: models) {
            JsonNode modelVersions = model.get("model-vers").get("model-ver");
            for (JsonNode modelVersion: modelVersions) {
                final String modelVersionId = modelVersion.get("model-version-id").asText();
                modelVersionByModelVersionId.put(modelVersionId, modelVersion.get("model-version").asText());
                modelNameByModelVersionId.put(modelVersionId, modelVersion.get("model-name").asText());
            }
        }

        nodes.forEach(node -> {
            node.setModelVersion(modelVersionByModelVersionId.get(node.getModelVersionId()));
            node.setModelName(modelNameByModelVersionId.get(node.getModelVersionId()));
        });

    }

    private JsonNode getModels(AaiClientInterface aaiClient, Collection<String> invariantIDs) {
        Response response = aaiClient.getVersionByInvariantId(ImmutableList.copyOf(invariantIDs));
        try {
            JsonNode responseJson = mapper.readTree(response.readEntity(String.class));
            return responseJson.get("model");
        } catch (Exception e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to getVersionByInvariantId from A&AI", e);
        }
        return mapper.createObjectNode();
    }

    private Set<String> getModelInvariantIds(Collection<AAITreeNode> nodes) {
        return nodes.stream()
                .map(AAITreeNode::getModelInvariantId)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    private boolean waitForTreeFetch(ThreadPoolExecutor threadPool) {
        int timer = 60;
        try {
            //Stop fetching information if it takes more than 1 minute
            while (threadPool.getActiveCount() != 0 &&
                    timer > 0) {
                sleep(1000);
                timer--;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GenericUncheckedException(e);
        }
        threadPool.shutdown();
        return (timer == 0);
    }

    private ThreadPoolExecutor getThreadPool() {
        //Use at least one thread, and never more than 75% of the available thread.
        int cores = Math.max((int)(Runtime.getRuntime().availableProcessors() * 0.75), 1);
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        return new ThreadPoolExecutor(1, cores, 10, TimeUnit.SECONDS, queue);
    }

    public static class AaiRelationship {

        public final String type;

        public AaiRelationship(String type) {
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AaiRelationship)) return false;
            AaiRelationship that = (AaiRelationship) o;
            return Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }
}
