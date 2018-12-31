package org.onap.vid.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.FailureAAITreeNode;
import org.onap.vid.utils.Streams;
import org.onap.vid.utils.Tree;
import org.onap.vid.utils.Unchecked;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.onap.vid.utils.Streams.not;


@Component
public class AAITreeNodeBuilder {

    private AaiClientInterface aaiClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AAITreeNodeBuilder.class);

    //List of all the node types the tree should include
    public static final String SERVICE_INSTANCE = "service-instance";
    public static final String GENERIC_VNF = "generic-vnf";
    public static final String NETWORK = "l3-network";
    public static final String FAILURE = "failure_node";
    public static final String COLLECTION_RESOURCE = "collection";
    public static final String CONFIGURATION = "configuration";
    public static final String PNF = "pnf";
    public static final String VF_MODULE = "vf-module";
    public static final String INSTANCE_GROUP = "instance-group";
    public static final String PORT = "l-interface";
    public static final String VG = "volume-group";
    public static final String VLAN_TAG = "vlan-tag";

    //Hashmap that defines the node-type and the tag that should be used to find it's ID key in the JSON.
    private static HashMap<String, String> nodeTypeToIdKeyMap = generateTypeToIdKeyMap();

    //Hashmap that defines the node-type and the tag that should be used to find it's NAMR key in the JSON.
    private static HashMap<String, String> nodeTypeToNameKeyMap = generateTypeToNameKeyMap();

    public enum AAIBaseProperties {
        ORCHESTRATION_STATUS("orchestration-status"),
        PROV_STATUS("prov-status"),
        IN_MAINT("in-maint"),
        MODEL_VERSION_ID("model-version-id"),
        MODEL_CUSTOMIZATION_ID("model-customization-id"),
        MODEL_INVARIANT_ID("model-invariant-id"),
        RELATIONSHIP_LIST("relationship-list");

        private final String aaiKey;

        AAIBaseProperties(String aaiKey) {
            this.aaiKey = aaiKey;
        }

        public String getAaiKey() {
            return aaiKey;
        }
    }

    public static List<AAIServiceTree.AaiRelationship> toAaiRelationshipList(String... types) {
        return Stream.of(types).map(AAIServiceTree.AaiRelationship::new).collect(Collectors.toList());
    }

    @Inject
    public AAITreeNodeBuilder(AaiClientInterface aaiClient) {
        this.aaiClient = aaiClient;
    }

    public List<AAITreeNode> buildNode(String nodeType,
                                 String requestURL,
                                 ConcurrentSkipListSet<AAITreeNode> nodesAccumulator, ExecutorService threadPool,
                                 ConcurrentLinkedQueue<String> visitedNodes,
                                 AtomicInteger nodesCounter,
                                 Tree<AAIServiceTree.AaiRelationship> pathsTree) {

        JsonNode topLevelJson = aaiClient.typedAaiGet(Unchecked.toURI(requestURL), JsonNode.class);

        if (topLevelJson.has(nodeType) && topLevelJson.get(nodeType).isArray()) {
            return Streams.fromIterable(topLevelJson.get(nodeType))
                    .map(item -> parseNodeAndGetChildren(nodeType, requestURL, item,
                            nodesAccumulator, threadPool, visitedNodes, nodesCounter, pathsTree))
                    .collect(toList());
        } else {
            return ImmutableList.of(parseNodeAndGetChildren(nodeType, requestURL, topLevelJson,
                    nodesAccumulator, threadPool, visitedNodes, nodesCounter, pathsTree));
        }
    }

    private AAITreeNode parseNodeAndGetChildren(String nodeType,
                                                String requestURL,
                                                JsonNode topLevelJson,
                                                ConcurrentSkipListSet<AAITreeNode> nodesAccumulator, ExecutorService threadPool,
                                                ConcurrentLinkedQueue<String> visitedNodes,
                                                AtomicInteger nodesCounter,
                                                Tree<AAIServiceTree.AaiRelationship> pathsTree) {
        AAITreeNode node = jsonToAaiNode(nodeType, topLevelJson, nodesAccumulator, nodesCounter);

        RelationshipList relationships = mapper.convertValue(topLevelJson.get(AAIBaseProperties.RELATIONSHIP_LIST.getAaiKey()), RelationshipList.class);
        if (relationships != null) {
            getChildren(threadPool, nodesAccumulator, relationships.getRelationship(), visitedNodes, node, nodesCounter, pathsTree);
        }
        if (StringUtils.equals(node.getType(), GENERIC_VNF)) {
            getRelatedVfModules(threadPool, nodesAccumulator, requestURL, node, nodesCounter);
        }
        return node;
    }

    private AAITreeNode jsonToAaiNode(String nodeType, JsonNode topLevelJson, ConcurrentSkipListSet<AAITreeNode> nodesAccumulator, AtomicInteger nodesCounter) {
        AAITreeNode node = fillNodeMetaData(nodeType, topLevelJson, nodesCounter);

        nodesAccumulator.add(node);

        return node;
    }

    private void getRelatedVfModules(ExecutorService threadPool, ConcurrentSkipListSet<AAITreeNode> nodesAccumulator, String parentURL, AAITreeNode parentNode, AtomicInteger nodesCounter) {
        /*
        VNFs do not report their direct related-to vf-modules, so try
        directly fetching a resource URI.
         */

        threadPool.execute(() -> {
            // the response is an array of vf-modules
            final JsonNode topLevelJson;
            try {
                topLevelJson = aaiClient.typedAaiGet(Unchecked.toURI(parentURL + "/vf-modules"), JsonNode.class);
            } catch (ExceptionWithRequestInfo e) {
                if (e.getHttpCode().equals(404)) {
                    // it's ok, as we're just optimistically fetching
                    // the /vf-modules uri; 404 says this time it was
                    // a bad guess
                    return;
                } else {
                    throw e;
                }
            }

            if (topLevelJson != null) {
                parentNode.getChildren().addAll(
                        Streams.fromIterable(topLevelJson.get(VF_MODULE))
                                .map(vfModuleNode -> jsonToAaiNode(VF_MODULE, vfModuleNode, nodesAccumulator, nodesCounter))
                                .collect(toList())
                );
            } else {
                LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to get vf-modules for vnf " + parentNode.getId());
            }
        });
    }

    private void getChildren(ExecutorService threadPool, ConcurrentSkipListSet<AAITreeNode> nodesAccumulator,
                             List<Relationship> relationships, ConcurrentLinkedQueue<String> visitedNodes, AAITreeNode parent, AtomicInteger nodesCounter, Tree<AAIServiceTree.AaiRelationship> pathsTree) {
        for (Relationship relationship : relationships) {
            createChildNode(threadPool, nodesAccumulator, relationship, visitedNodes, parent, nodesCounter, pathsTree);
        }
    }

    private void createChildNode(ExecutorService threadPool, ConcurrentSkipListSet<AAITreeNode> nodesAccumulator,
                                 Relationship relationship, ConcurrentLinkedQueue<String> visitedNodes, AAITreeNode parent, AtomicInteger nodesCounter, Tree<AAIServiceTree.AaiRelationship> pathsTree) {
        String newNodeType = relationship.getRelatedTo();
        Tree<AAIServiceTree.AaiRelationship> subTree = pathsTree.getSubTree(new AAIServiceTree.AaiRelationship(newNodeType));
        if (subTree!=null) {
            String newNodeUrl = relationship.getRelatedLink();
            if (!visitedNodes.contains(newNodeUrl)) {
                visitedNodes.add(newNodeUrl);
                threadPool.execute(() -> {
                            try {
                                parent.addChildren(buildNode(newNodeType, newNodeUrl, nodesAccumulator, threadPool, visitedNodes, nodesCounter,  subTree));
                            } catch (Exception e) {
                                parent.getChildren().add(createFailureNode(e));
                            }
                        }
                );
            }
        }
    }

    private AAITreeNode fillNodeMetaData(String nodeType, JsonNode model, @NotNull AtomicInteger nodesCounter) {
        AAITreeNode node = new AAITreeNode();
        node.setType(nodeType);
        node.setUniqueNumber(nodesCounter.getAndIncrement());
        node.setOrchestrationStatus(getStringDataFromJsonIfExists(model, AAIBaseProperties.ORCHESTRATION_STATUS.getAaiKey()));
        node.setProvStatus(getStringDataFromJsonIfExists(model, AAIBaseProperties.PROV_STATUS.getAaiKey()));
        node.setInMaint(getBooleanDataFromJsonIfExists(model, AAIBaseProperties.IN_MAINT.getAaiKey()));
        node.setModelVersionId(getStringDataFromJsonIfExists(model, AAIBaseProperties.MODEL_VERSION_ID.getAaiKey()));
        node.setModelCustomizationId(getStringDataFromJsonIfExists(model, AAIBaseProperties.MODEL_CUSTOMIZATION_ID.getAaiKey()));
        node.setModelInvariantId(getStringDataFromJsonIfExists(model, AAIBaseProperties.MODEL_INVARIANT_ID.getAaiKey()));
        node.setId(getStringDataFromJsonIfExists(model, nodeTypeToIdKeyMap.get(nodeType)));
        node.setName(getStringDataFromJsonIfExists(model, nodeTypeToNameKeyMap.get(nodeType)));
        node.setAdditionalProperties(aggregateAllOtherProperties(model, nodeType));

        return node;
    }

    private AAITreeNode createFailureNode(Exception exception) {
        return FailureAAITreeNode.of(exception);
    }

    private String getStringDataFromJsonIfExists(JsonNode model, String key) {
        if (model.has(key)) {
            return model.get(key).asText();
        }
        return null;
    }

    private Boolean getBooleanDataFromJsonIfExists(JsonNode model, String key) {
        if (model.has(key)) {
            return model.get(key).asBoolean();
        }
        return false;
    }

    private Map<String, Object> aggregateAllOtherProperties(JsonNode model, String nodeType) {
        Set<String> ignoreProperties = Stream.of(AAIBaseProperties.values())
                .map(AAIBaseProperties::getAaiKey).collect(toSet());

        return Streams.fromIterator(model.fields())
                .filter(not(field -> StringUtils.equals(field.getKey(), nodeTypeToIdKeyMap.get(nodeType))))
                .filter(not(field -> StringUtils.equals(field.getKey(), nodeTypeToNameKeyMap.get(nodeType))))
                .filter(not(field -> ignoreProperties.contains(field.getKey())))
                .collect(toMap(Map.Entry::getKey, v -> v.getValue().asText()));
    }

    private static HashMap<String, String> generateTypeToIdKeyMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put(SERVICE_INSTANCE, "service-instance-id");
        result.put(GENERIC_VNF, "vnf-id");
        result.put(NETWORK, "network-id");
        result.put(COLLECTION_RESOURCE, "collection-id");
        result.put(CONFIGURATION, "configuration-id");
        result.put(PNF, "pnf-id");
        result.put(VF_MODULE, "vf-module-id");
        result.put(INSTANCE_GROUP, "id");
        result.put(PORT, "l-interface-id");
        result.put(VG, "volume-group-id");
        result.put(VLAN_TAG, "vlan-id");

        return result;
    }

    private static HashMap<String, String> generateTypeToNameKeyMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put(SERVICE_INSTANCE, "service-instance-name");
        result.put(GENERIC_VNF, "vnf-name");
        result.put(NETWORK, "network-name");
        result.put(COLLECTION_RESOURCE, "collection-name");
        result.put(CONFIGURATION, "configuration-name");
        result.put(PNF, "pnf-name");
        result.put(VF_MODULE, "vf-module-name");
        result.put(INSTANCE_GROUP, "instance-group-name");
        result.put(PORT, "l-interface-name");
        result.put(VG, "volume-group-name");
        result.put(VLAN_TAG, "vlan-name");

        return result;
    }
}