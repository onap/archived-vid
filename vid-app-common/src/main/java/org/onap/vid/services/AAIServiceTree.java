/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.services;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isAllEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.aai.util.AAITreeConverter;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.parser.ServiceModelInflator;
import org.onap.vid.asdc.parser.ServiceModelInflator.Names;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.model.aaiTree.ServiceInstance;
import org.onap.vid.properties.Features;
import org.onap.vid.utils.Tree;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.togglz.core.manager.FeatureManager;

@Component
public class AAIServiceTree {

    private final AAITreeNodeBuilder aaiTreeNodeBuilder;

    private final AAITreeConverter aaiTreeConverter;

    private final AaiClientInterface aaiClient;

    private final VidService sdcService;

    private final ServiceModelInflator serviceModelInflator;

    private final ExecutorService executorService;

    private final FeatureManager featureManager;

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AAIServiceTree.class);

    public static final Tree<AaiRelationship> AAI_TREE_PATHS =
            new Tree<>(new AaiRelationship(NodeType.SERVICE_INSTANCE));

    static {
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.VOLUME_GROUP));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.VF_MODULE));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.NETWORK, NodeType.VPN_BINDING));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.NETWORK, NodeType.VPN_BINDING));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.INSTANCE_GROUP, NodeType.GENERIC_VNF));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.COLLECTION_RESOURCE, NodeType.INSTANCE_GROUP));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.CONFIGURATION, NodeType.NETWORK, NodeType.VPN_BINDING));
        AAI_TREE_PATHS.addPath(toAaiRelationshipList(NodeType.CONFIGURATION, NodeType.VPN_BINDING));
    }

    public static List<AAIServiceTree.AaiRelationship> toAaiRelationshipList(NodeType... types) {
        return Stream.of(types).map(AAIServiceTree.AaiRelationship::new).collect(Collectors.toList());
    }

    @Inject
    public AAIServiceTree(AaiClientInterface aaiClient, AAITreeNodeBuilder aaiTreeNodeBuilder,
                          AAITreeConverter aaiTreeConverter, VidService sdcService,
                          FeatureManager featureManager,
                          ServiceModelInflator serviceModelInflator, ExecutorService executorService) {
        this.aaiClient = aaiClient;
        this.aaiTreeNodeBuilder = aaiTreeNodeBuilder;
        this.aaiTreeConverter = aaiTreeConverter;
        this.sdcService = sdcService;
        this.featureManager = featureManager;
        this.serviceModelInflator = serviceModelInflator;
        this.executorService = executorService;
    }

    List<AAITreeNode> buildAAITreeForUniqueResource(String getUrl, NodeType nodeType) {
        return buildAAITreeForUniqueResourceFromCustomQuery(getUrl, null, HttpMethod.GET, nodeType);
    }

    List<AAITreeNode> buildAAITreeForUniqueResourceFromCustomQuery(String url, String payload, HttpMethod method, NodeType nodeType) {
        Tree<AAIServiceTree.AaiRelationship> pathsToSearch = new Tree<>(new AAIServiceTree.AaiRelationship(nodeType));
        return buildAAITree(url, payload, method, pathsToSearch, false);
    }

    public List<AAITreeNode> buildAAITree(String url, String payload, HttpMethod method, Tree<AaiRelationship> pathsToSearch, boolean enrichWithModelVersion) {

        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = createNodesAccumulator();

        List<AAITreeNode> aaiTreeNodes = fetchAAITree(url, payload, method, pathsToSearch, nodesAccumulator);

        if (enrichWithModelVersion) {
            enrichNodesWithModelVersionAndModelName(nodesAccumulator);
        }

        return aaiTreeNodes;
    }

    public ServiceInstance getServiceInstanceTopology(String globalCustomerId, String serviceType, String serviceInstanceId) {

        String getURL = "business/customers/customer/" +
                globalCustomerId + "/service-subscriptions/service-subscription/" +
                serviceType + "/service-instances/service-instance/" + serviceInstanceId;

        //Used later to get the nodes UUID
        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = createNodesAccumulator();

        AAITreeNode aaiTree = fetchAAITree(getURL, null, HttpMethod.GET, AAI_TREE_PATHS, nodesAccumulator).get(0);

        //Populate nodes with model-name & model-version (from aai)
        enrichNodesWithModelVersionAndModelName(nodesAccumulator);

        final ServiceModel serviceModel = getServiceModel(aaiTree.getModelVersionId());

        //Populate nodes with model-customization-name (from sdc model)
        enrichNodesWithModelCustomizationName(nodesAccumulator, serviceModel);
        enrichVfModulesWithModelCustomizationNameFromOtherVersions(nodesAccumulator, aaiTree.getModelInvariantId());

        return aaiTreeConverter.convertTreeToUIModel(aaiTree, globalCustomerId, serviceType, getInstantiationType(serviceModel), getInstanceRole(serviceModel), getInstanceType(serviceModel));
    }

    private String getInstanceType(ServiceModel serviceModel){
        if (serviceModel != null && serviceModel.getService() != null) {
            return serviceModel.getService().getServiceType();
        }
        return "";
    }

    private String getInstanceRole(ServiceModel serviceModel) {
        if (serviceModel != null && serviceModel.getService() != null) {
            return serviceModel.getService().getServiceRole();
        }
        return "";
    }

    private List<AAITreeNode> fetchAAITree(String url, String payload, HttpMethod method, Tree<AaiRelationship> pathsToSearch,
                                           ConcurrentSkipListSet<AAITreeNode> nodesAccumulator) {
        return aaiTreeNodeBuilder.buildNode(NodeType.fromString(pathsToSearch.getRootValue().type),
                url, payload, method, defaultIfNull(nodesAccumulator, createNodesAccumulator()),
                executorService, pathsToSearch);
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

    void enrichVfModulesWithModelCustomizationNameFromOtherVersions(Collection<AAITreeNode> nodes, String modelInvariantId) {
        if (!featureManager.isActive(Features.FLAG_EXP_TOPOLOGY_TREE_VFMODULE_NAMES_FROM_OTHER_TOSCA_VERSIONS)) {
            return;
        }

        final Predicate<AAITreeNode> nodeWithMissingData =
            (AAITreeNode node) -> isAllEmpty(node.getKeyInModel(), node.getModelCustomizationId());

        final Predicate<AAITreeNode> vfModuleWithMissingData =
            nodeWithMissingData.and(node -> node.getType() == NodeType.VF_MODULE);

        if (nodes.stream().noneMatch(vfModuleWithMissingData)) {
            return;
        }

        List<ModelVer> allModelVersions = aaiClient.getSortedVersionsByInvariantId(modelInvariantId);

        if (allModelVersions == null || allModelVersions.isEmpty()) {
            return;
        }

        final ListIterator<ModelVer> modelVersionsIterator = allModelVersions.listIterator();
        final Map<String, ServiceModelInflator.Names> namesByCustomizationId = new HashMap<>();

        nodes.stream().filter(vfModuleWithMissingData).forEach(node -> {
            String modelCustomizationId = node.getModelCustomizationId();

            fetchCustomizationIdsFromToscaModels(namesByCustomizationId, modelVersionsIterator, modelCustomizationId);

            final ServiceModelInflator.Names names = namesByCustomizationId.get(modelCustomizationId);
            if (names != null) {
                node.setKeyInModel(defaultIfEmpty(node.getKeyInModel(), names.getModelKey()));
                node.setModelCustomizationName(defaultIfEmpty(node.getModelCustomizationName(), names.getModelCustomizationName()));
            }
        });
    }

    /**
     * Loads namesByCustomizationId with all customization IDs from the list of modelVersions. Will seize loading
     * if yieldCustomizationId presents in namesByCustomizationId.
     * @param namesByCustomizationId Mutable Map to fill-up
     * @param modelVersions Iterable of model-version-ids to load
     * @param yieldCustomizationId The key to stop loading on
     */
    private void fetchCustomizationIdsFromToscaModels(
        Map<String, Names> namesByCustomizationId, ListIterator<ModelVer> modelVersions, String yieldCustomizationId
    ) {
        while (modelVersions.hasNext() && !namesByCustomizationId.containsKey(yieldCustomizationId)) {
            final String nextModelVersionId = modelVersions.next().getModelVersionId();

            if (isNotEmpty(nextModelVersionId)) {
                namesByCustomizationId.putAll(serviceModelInflator.toNamesByCustomizationId(
                        getServiceModel(nextModelVersionId)
                    ));
            }
        }
    }


    private void enrichNodesWithModelVersionAndModelName(Collection<AAITreeNode> nodes) {

        Collection<String> invariantIDs = getModelInvariantIds(nodes);

        Map<String, String> modelVersionByModelVersionId = new HashMap<>();
        Map<String, String> modelNameByModelVersionId = new HashMap<>();

        JsonNode models = getModels(aaiClient, invariantIDs);
        if (models!=null) {
            for (JsonNode model : models) {
                JsonNode modelVersions = model.get("model-vers").get("model-ver");
                for (JsonNode modelVersion : modelVersions) {
                    final String modelVersionId = modelVersion.get("model-version-id").asText();
                    modelVersionByModelVersionId.put(modelVersionId, modelVersion.get("model-version").asText());
                    modelNameByModelVersionId.put(modelVersionId, modelVersion.get("model-name").asText());
                }
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
            JsonNode responseJson = JACKSON_OBJECT_MAPPER.readTree(response.readEntity(String.class));
            return responseJson.get("model");
        } catch (Exception e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to getVersionByInvariantId from A&AI", e);
        }
        return JACKSON_OBJECT_MAPPER.createObjectNode();
    }

    private Set<String> getModelInvariantIds(Collection<AAITreeNode> nodes) {
        return nodes.stream()
                .map(AAITreeNode::getModelInvariantId)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    public static class AaiRelationship {

        public final String type;

        public AaiRelationship(String type) {
            this.type = type;
        }

        public AaiRelationship(NodeType nodeType) {
            this.type = nodeType.getType();
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
