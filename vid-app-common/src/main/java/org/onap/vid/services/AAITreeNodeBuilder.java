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

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;
import static org.onap.vid.utils.Streams.not;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipData;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList;
import org.onap.vid.aai.util.AAITreeNodeUtils;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.FailureAAITreeNode;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.Streams;
import org.onap.vid.utils.Tree;
import org.onap.vid.utils.Unchecked;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;


@Component
public class AAITreeNodeBuilder {

    private static final String RESULTS = "results";
    private final AaiClientInterface aaiClient;
    private final Logging logging;

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AAITreeNodeBuilder.class);


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

    @Inject
    public AAITreeNodeBuilder(AaiClientInterface aaiClient, Logging logging) {
        this.aaiClient = aaiClient;
        this.logging = logging;
    }

    List<AAITreeNode> buildNode(NodeType nodeType,
                                String requestURL,
                                String payload,
                                HttpMethod method,
                                ConcurrentSkipListSet<AAITreeNode> nodesAccumulator,
                                ExecutorService threadPool,
                                Tree<AAIServiceTree.AaiRelationship> pathsTree) {

        JsonNode jsonNode = aaiClient.typedAaiRest(Unchecked.toURI(requestURL), JsonNode.class, payload, method, false);

        List<Pair<AAITreeNode, List<Relationship>>> nodes = getNodesWithRelationships(jsonNode, nodeType, nodesAccumulator, pathsTree);

        String timeout = SystemProperties.getProperty(VidProperties.VID_THREAD_TIMEOUT);
        long timeoutNum = Long.parseLong(StringUtils.defaultIfEmpty(timeout, "30"));

        for (Pair<AAITreeNode, List<Relationship>> entry : nodes) {
            fetchChildrenAsync(threadPool, nodesAccumulator, entry.getKey(), entry.getValue(), pathsTree, timeoutNum);

            if (getNextLevelInPathsTree(pathsTree, NodeType.VF_MODULE.getType()) != null) {
                getRelatedVfModules(threadPool, nodesAccumulator, requestURL, entry.getKey());
            }
        }

        return nodes.stream()
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }

    private List<Pair<AAITreeNode, List<Relationship>>> getNodesWithRelationships(JsonNode jsonNode, NodeType nodeType,
                                                                                  ConcurrentSkipListSet<AAITreeNode> nodesAccumulator,
                                                                                  Tree<AAIServiceTree.AaiRelationship> pathsTree) {
        if (isListOfKeyResults(jsonNode)) {
            return Streams.fromIterable(jsonNode.get(RESULTS))
                    .filter(item -> item.has(nodeType.getType()))
                    .map(item -> item.get(nodeType.getType()))
                    .map(item -> parseNodeAndFilterRelationships(item, nodeType, nodesAccumulator, pathsTree))
                    .collect(Collectors.toList());
        } else if (isArray(jsonNode, nodeType)) {
            return Streams.fromIterable(jsonNode.get(nodeType.getType()))
                    .map(item -> parseNodeAndFilterRelationships(item, nodeType, nodesAccumulator, pathsTree))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of(parseNodeAndFilterRelationships(jsonNode, nodeType, nodesAccumulator, pathsTree));
        }
    }

    Pair<AAITreeNode, List<Relationship>> parseNodeAndFilterRelationships(JsonNode jsonNode, NodeType nodeType,
                                                                          ConcurrentSkipListSet<AAITreeNode> nodesAccumulator,
                                                                          Tree<AAIServiceTree.AaiRelationship> pathsTree) {
        AAITreeNode node = createAaiNode(nodeType, jsonNode, nodesAccumulator);

        enrichPlacementData(node);

        List<Relationship> filteredRelationships = getFilteredRelationships(jsonNode, pathsTree);

        return ImmutablePair.of(node, filteredRelationships);
    }

    boolean isArray(JsonNode json, NodeType nodeType) {
        return json != null && json.has(nodeType.getType()) && json.get(nodeType.getType()).isArray();
    }

    boolean isListOfKeyResults(JsonNode jsonNode) {
        return jsonNode != null && jsonNode.has(RESULTS) && jsonNode.get(RESULTS).isArray();
    }

    AAITreeNode createAaiNode(NodeType nodeType, JsonNode jsonNode, ConcurrentSkipListSet<AAITreeNode> nodesAccumulator) {
        AAITreeNode node = jsonNodeToAaiNode(nodeType, jsonNode);

        nodesAccumulator.add(node);

        return node;
    }

    private void addChildren(AAITreeNode node, Future<List<AAITreeNode>> children) {
        try {
            node.addChildren(children.get());
        } catch (Exception e) {
            node.getChildren().add(createFailureNode(e));
        }
    }

    private Map<String,String> convertRelationshipDataToMap(List<RelationshipData> relationshipData) {
        return relationshipData.stream().collect(
                Collectors.toMap(RelationshipData::getKey, RelationshipData::getValue));
    }

    void enrichPlacementData(AAITreeNode node){
        Optional<Relationship> tenantRelationShip = AAITreeNodeUtils.findFirstRelationshipByRelatedTo(node.getRelationshipList(), "tenant");
        enrichPlacementDataUsingTenantInfo(node, tenantRelationShip);
    }

    void enrichPlacementDataUsingTenantInfo(AAITreeNode node, Optional<Relationship> tenantRelationShip) {
        //no tenant relationship in this node - so no placement data
        if (!tenantRelationShip.isPresent()) {
            return;
        }
        try {
            Map<String, String> relationshipsDataMap = convertRelationshipDataToMap(tenantRelationShip.get().getRelationDataList());
            node.setCloudConfiguration(new CloudConfiguration(
                    relationshipsDataMap.get("cloud-region.cloud-region-id"),
                    relationshipsDataMap.get("tenant.tenant-id"),
                    relationshipsDataMap.get("cloud-region.cloud-owner")));
        }
        catch (Exception exception) {
            LOGGER.error("Failed to extract placement form tenant relationship of {}:{}", node.getType(), node.getId(), exception);
        }
    }

    private void getRelatedVfModules(ExecutorService threadPool, ConcurrentSkipListSet<AAITreeNode> nodesAccumulator, String parentURL, AAITreeNode parentNode) {
        /*
        VNFs do not report their direct related-to vf-modules, so try
        directly fetching a resource URI.
         */

        Future<?> vfModulesTask = threadPool.submit(withCopyOfMDC(() -> {
            // the response is an array of vf-modules
            final JsonNode jsonNode;
            try {
                jsonNode = aaiClient.typedAaiGet(Unchecked.toURI(parentURL + "/vf-modules"), JsonNode.class);
            } catch (ExceptionWithRequestInfo e) {
                if (e.getHttpCode().equals(404)) {
                    // it's ok, as we're just optimistically fetching
                    // the /vf-modules uri; 404 says this time it was a bad guess
                    return true;
                } else {
                    throw e;
                }
            }

            if (isArray(jsonNode, NodeType.VF_MODULE)) {
                //create list of AAITreeNode represent the VfModules from AAI result
                List<AAITreeNode> vfModules = Streams.fromIterable(jsonNode.get(NodeType.VF_MODULE.getType()))
                    .map(vfModuleNode -> createAaiNode(NodeType.VF_MODULE, vfModuleNode, nodesAccumulator))
                    .collect(toList());
                //enrich each of the VfModule with placement info
                vfModules.forEach(vfModule -> enrichPlacementDataUsingTenantInfo(
                    vfModule,
                    AAITreeNodeUtils.findFirstRelationshipByRelatedTo(vfModule.getRelationshipList(), "vserver")
                ));
                //add all VfModules to children list of parent node
                parentNode.getChildren().addAll(vfModules);
            } else {
                LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to get vf-modules for vnf " + parentNode.getId());
            }

            return true; // the Callable<> contract requires a return value
        }));

        waitForCompletion(vfModulesTask);
    }

    private void waitForCompletion(Future<?> future) {
        try {
            future.get();
        } catch (Exception e) {
            throw new GenericUncheckedException(e);
        }
    }

    List<Relationship> getFilteredRelationships(JsonNode json, Tree<AAIServiceTree.AaiRelationship> pathsTree) {
        RelationshipList relationshipList = JACKSON_OBJECT_MAPPER.convertValue(json.get(AAIBaseProperties.RELATIONSHIP_LIST.getAaiKey()), RelationshipList.class);
        if (relationshipList != null) {
            return relationshipList.getRelationship().stream()
                    .filter(rel -> getNextLevelInPathsTree(pathsTree, rel.getRelatedTo()) != null)
                    .filter(rel -> !Objects.equals(rel.getRelatedTo(), NodeType.VF_MODULE.getType())) // vf-modules are handled separately
                    .collect(toList());
        }

        return Collections.emptyList();
    }

    void fetchChildrenAsync(ExecutorService threadPool, ConcurrentSkipListSet<AAITreeNode> nodesAccumulator,
                            AAITreeNode node, List<Relationship> relationships, Tree<AAIServiceTree.AaiRelationship> pathsTree, long timeout) {

        if (!relationships.isEmpty()) {
            List<Callable<List<AAITreeNode>>> tasks = relationships.stream()
                .map(relationship ->
                    withCopyOfMDC(() -> getChildNode(threadPool, nodesAccumulator, relationship.getRelatedTo(),
                            relationship.getRelatedLink(), pathsTree)))
                .collect(Collectors.toList());

            try {
                int depth = pathsTree.getChildrenDepth();
                threadPool.invokeAll(tasks, timeout * depth, TimeUnit.SECONDS)
                    .forEach(future ->
                        addChildren(node, future)
                    );
            } catch (Exception e) {
                throw new GenericUncheckedException(e);
            }
        }
    }

    private <V> Callable<V> withCopyOfMDC(Callable<V> callable) {
        return logging.withMDC(MDC.getCopyOfContextMap(), callable);
    }

    private List<AAITreeNode> getChildNode(ExecutorService threadPool, ConcurrentSkipListSet<AAITreeNode> nodesAccumulator,
                                           String childNodeType, String childNodeUrl,
                                           Tree<AAIServiceTree.AaiRelationship> pathsTree) {

        Tree<AAIServiceTree.AaiRelationship> subTree = getNextLevelInPathsTree(pathsTree, childNodeType);

        return buildNode(NodeType.fromString(childNodeType), childNodeUrl, null, HttpMethod.GET, nodesAccumulator, threadPool, subTree);
    }

    Tree<AAIServiceTree.AaiRelationship> getNextLevelInPathsTree(Tree<AAIServiceTree.AaiRelationship> pathsTree, String nodeType) {
        return pathsTree.getSubTree(new AAIServiceTree.AaiRelationship(nodeType));
    }

    //ADD TEST
    private AAITreeNode jsonNodeToAaiNode(NodeType nodeType, JsonNode jsonNode) {
        AAITreeNode node = new AAITreeNode();
        node.setType(nodeType);
        node.setOrchestrationStatus(getStringDataFromJsonIfExists(jsonNode, AAIBaseProperties.ORCHESTRATION_STATUS.getAaiKey()));
        node.setProvStatus(getStringDataFromJsonIfExists(jsonNode, AAIBaseProperties.PROV_STATUS.getAaiKey()));
        node.setInMaint(getBooleanDataFromJsonIfExists(jsonNode, AAIBaseProperties.IN_MAINT.getAaiKey()));
        node.setModelVersionId(getStringDataFromJsonIfExists(jsonNode, AAIBaseProperties.MODEL_VERSION_ID.getAaiKey()));
        node.setModelCustomizationId(getStringDataFromJsonIfExists(jsonNode, AAIBaseProperties.MODEL_CUSTOMIZATION_ID.getAaiKey()));
        node.setModelInvariantId(getStringDataFromJsonIfExists(jsonNode, AAIBaseProperties.MODEL_INVARIANT_ID.getAaiKey()));
        node.setId(getStringDataFromJsonIfExists(jsonNode, nodeType.getId()));
        node.setName(getStringDataFromJsonIfExists(jsonNode, nodeType.getName()));
        node.setAdditionalProperties(aggregateAllOtherProperties(jsonNode, nodeType));
        node.setRelationshipList(JACKSON_OBJECT_MAPPER.convertValue(jsonNode.get(AAIBaseProperties.RELATIONSHIP_LIST.getAaiKey()), RelationshipList.class));
        return node;
    }

    private AAITreeNode createFailureNode(Exception exception) {
        return FailureAAITreeNode.of(exception);
    }

    private String getStringDataFromJsonIfExists(JsonNode model, String key) {
        if (!NodeType.NONE.equals(key) && model.has(key)) {
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

    Map<String, Object> aggregateAllOtherProperties(JsonNode model, NodeType nodeType) {
        Set<String> ignoreProperties = Stream.of(AAIBaseProperties.values())
                .map(AAIBaseProperties::getAaiKey).collect(toSet());
        return Streams.fromIterator(model.fields())
                .filter(not(field -> StringUtils.equals(field.getKey(), nodeType.getId())))
                .filter(not(field -> StringUtils.equals(field.getKey(), nodeType.getName())))
                .filter(not(field -> ignoreProperties.contains(field.getKey())))
                .collect(toMap(Map.Entry::getKey, v -> ifTextualGetAsText(v.getValue())));
    }

    private Object ifTextualGetAsText(JsonNode jsonNode) {
        return jsonNode.isTextual() ? jsonNode.asText() : jsonNode;
    }
}