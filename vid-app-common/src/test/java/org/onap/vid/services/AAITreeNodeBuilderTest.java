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
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.onap.vid.services.AAIServiceTree.AAI_TREE_PATHS;
import static org.onap.vid.testUtils.TestUtils.initMockitoMocks;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.Tree;
import org.onap.vid.utils.Unchecked;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AAITreeNodeBuilderTest {

    private AAITreeNodeBuilder aaiTreeNodeBuilder;

    @Mock
    private AaiClientInterface aaiClientMock;

    private ExecutorService executorService;
    private Logging logging = new Logging();
    private static final Logger logger = LogManager.getLogger(AAITreeNodeBuilderTest.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeMethod
    public void initMocks() {
        initMockitoMocks(this);
        aaiTreeNodeBuilder = new AAITreeNodeBuilder(aaiClientMock, logging);
        executorService = MoreExecutors.newDirectExecutorService();
    }

    private void buildNodeAndAssert(JsonNode inputNode, AAITreeNode expectedNode, NodeType nodeType){
        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = new ConcurrentSkipListSet<>(comparing(AAITreeNode::getUniqueNodeKey));
        when(aaiClientMock.typedAaiRest(Unchecked.toURI("anyUrl"), JsonNode.class, null, HttpMethod.GET, false)).thenReturn(inputNode);
        AAITreeNode actualNode = null;
        try {
            actualNode = aaiTreeNodeBuilder.buildNode(
                nodeType,
                "anyUrl",
                null,
                HttpMethod.GET,
                nodesAccumulator,
                executorService,
                AAI_TREE_PATHS.getSubTree(new AAIServiceTree.AaiRelationship(nodeType))
            ).get(0);
        //print stack trace for more information in case of failure
        } catch (RuntimeException e) {
            logger.error("Failed to build node by aaiTreeNodeBuilder", e);
            throw e;
        }
        assertThat(actualNode, jsonEquals(expectedNode).when(IGNORING_ARRAY_ORDER, IGNORING_EXTRA_FIELDS).whenIgnoringPaths("relationshipList","children[0].relationshipList"));
    }

    @Test
    public void buildNode_buildGroupNode_NodeIsAsExpected() {
        buildNodeAndAssert(createGroupJson(), createExpectedGroupNode(), NodeType.INSTANCE_GROUP);
    }

    private AAITreeNode createExpectedGroupNode() {
        AAITreeNode expectedNode = new AAITreeNode();
        expectedNode.setId("c4fcf022-31a0-470a-b5b8-c18335b7af32");
        expectedNode.setType(NodeType.INSTANCE_GROUP);
        expectedNode.setName("Test vE-Flex");
        expectedNode.setModelVersionId("Test vE-Flex");
        expectedNode.setModelInvariantId("dd182d7d-6949-4b90-b3cc-5befe400742e");
        expectedNode.setInMaint(false);
        HashMap<String,Object> additionalProperties = new HashMap<>();
        additionalProperties.put("inMaint","false");
        additionalProperties.put("description","Test vE-Flex instance-group");
        additionalProperties.put("instance-group-type","ha");
        additionalProperties.put("instance-group-role","test-IG-role");
        additionalProperties.put("resource-version","1533315433086");
        additionalProperties.put("instance-group-function","vTSBC Customer Landing Network Collection");
        expectedNode.setAdditionalProperties(additionalProperties);
        return expectedNode;
    }

    private JsonNode createGroupJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode groupNode = null;
        try {
            groupNode = objectMapper.readTree("" +
                    "{" +
                    "      \"id\": \"c4fcf022-31a0-470a-b5b8-c18335b7af32\"," +
                    "      \"instance-group-role\": \"test-IG-role\"," +
                    "      \"description\": \"Test vE-Flex instance-group\"," +
                    "      \"instance-group-type\": \"ha\"," +
                    "      \"resource-version\": \"1533315433086\"," +
                    "      \"instance-group-name\": \"Test vE-Flex\"," +
                    "      \"model-invariant-id\": \"dd182d7d-6949-4b90-b3cc-5befe400742e\"," +
                    "      \"model-version-id\": \"Test vE-Flex\"," +
                    "      \"inMaint\": \"false\"," +
                    "      \"instance-group-function\": \"vTSBC Customer Landing Network Collection\"," +
                    "      \"relationship-list\": {" +
                    "      \"relationship\": []" +
                    "    }" +
                    "    }");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groupNode;
    }

    @Test
    public void whenReadNetworkNode_thenNodeIsAsExpected() throws IOException {
        JsonNode mockedAaiResponse = TestUtils.readJsonResourceFileAsObject("/getTopology/network.json", JsonNode.class);

        AAITreeNode expectedNetworkNode = new AAITreeNode();
        expectedNetworkNode.setId("94c86b39-bbbf-4027-8120-ff37c6d2493a");
        expectedNetworkNode.setName("AUK51a_oam_calea_net_1");
        expectedNetworkNode.setOrchestrationStatus("Assigned");
        expectedNetworkNode.setModelInvariantId("b9a9b549-0ee4-49fc-b4f2-5edc6701da68");
        expectedNetworkNode.setModelVersionId("77010093-df36-4dcb-8428-c3d02bf3f88d");
        expectedNetworkNode.setModelCustomizationId("e5f33853-f84c-4cdd-99f2-93846957aa18");
        expectedNetworkNode.setType(NodeType.NETWORK);
        expectedNetworkNode.setCloudConfiguration(new CloudConfiguration("auk51a", "b530fc990b6d4334bd45518bebca6a51", "att-nc"));

        buildNodeAndAssert(mockedAaiResponse, expectedNetworkNode, NodeType.NETWORK);
    }

    @Test
    public void whenCloudRegionMissing_otherPlacementFieldsReadAsExpected() throws IOException {

        AAITreeNode node = new AAITreeNode();
        Optional<Relationship> tenantRelationShip = Optional.of(
                JACKSON_OBJECT_MAPPER.readValue("{" +
                        "      \"related-to\": \"tenant\"," +
                        "      \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                        "      \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a/tenants/tenant/b530fc990b6d4334bd45518bebca6a51\"," +
                        "      \"relationship-data\": [{" +
                        "        \"relationship-key\": \"cloud-region.cloud-owner\"," +
                        "        \"relationship-value\": \"att-nc\"" +
                        "      }, {" +
                        "        \"relationship-key\": \"tenant.tenant-id\"," +
                        "        \"relationship-value\": \"b530fc990b6d4334bd45518bebca6a51\"" +
                        "      }" +
                        "      ]," +
                        "      \"related-to-property\": [{" +
                        "        \"property-key\": \"tenant.tenant-name\"," +
                        "        \"property-value\": \"ecomp_ispt\"" +
                        "      }" +
                        "      ]" +
                        "    }", Relationship.class)
        );
        aaiTreeNodeBuilder.enrichPlacementDataUsingTenantInfo(node, tenantRelationShip);
        assertEquals(new CloudConfiguration(null, "b530fc990b6d4334bd45518bebca6a51", "att-nc"), node.getCloudConfiguration());
    }

    @Test
    public void whenTenantMissing_otherPlacementFieldsReadAsExpected() throws IOException {

        AAITreeNode node = new AAITreeNode();
        Optional<Relationship> tenantRelationShip = Optional.of(
                JACKSON_OBJECT_MAPPER.readValue("{" +
                        "      \"related-to\": \"tenant\"," +
                        "      \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                        "      \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a/tenants/tenant/b530fc990b6d4334bd45518bebca6a51\"," +
                        "      \"relationship-data\": [{" +
                        "        \"relationship-key\": \"cloud-region.cloud-owner\"," +
                        "        \"relationship-value\": \"att-nc\"" +
                        "      }, {" +
                        "        \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                        "        \"relationship-value\": \"auk51a\"" +
                        "      }" +
                        "      ]," +
                        "      \"related-to-property\": [{" +
                        "        \"property-key\": \"tenant.tenant-name\"," +
                        "        \"property-value\": \"ecomp_ispt\"" +
                        "      }" +
                        "      ]" +
                        "    }", Relationship.class)
        );
        aaiTreeNodeBuilder.enrichPlacementDataUsingTenantInfo(node, tenantRelationShip);
        assertEquals(new CloudConfiguration("auk51a", null, "att-nc"), node.getCloudConfiguration());
    }

    @Test
    public void whenCloudOwnerMissing_otherPlacementFieldsReadAsExpected() throws IOException {

        AAITreeNode node = new AAITreeNode();
        Optional<Relationship> tenantRelationShip = Optional.of(
                JACKSON_OBJECT_MAPPER.readValue("{" +
                        "      \"related-to\": \"tenant\"," +
                        "      \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                        "      \"related-link\": \"/aai/v14/cloud-infrastructure/cloud-regions/cloud-region/att-nc/auk51a/tenants/tenant/b530fc990b6d4334bd45518bebca6a51\"," +
                        "      \"relationship-data\": [{" +
                        "        \"relationship-key\": \"tenant.tenant-id\"," +
                        "        \"relationship-value\": \"b530fc990b6d4334bd45518bebca6a51\"" +
                        "      }, {" +
                        "        \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                        "        \"relationship-value\": \"auk51a\"" +
                        "      }" +
                        "      ]," +
                        "      \"related-to-property\": [{" +
                        "        \"property-key\": \"tenant.tenant-name\"," +
                        "        \"property-value\": \"ecomp_ispt\"" +
                        "      }" +
                        "      ]" +
                        "    }", Relationship.class)
        );
        aaiTreeNodeBuilder.enrichPlacementDataUsingTenantInfo(node, tenantRelationShip);
        assertEquals(new CloudConfiguration("auk51a", "b530fc990b6d4334bd45518bebca6a51",  null), node.getCloudConfiguration());
    }

    @Test
    public void whenThereIsNoTenantRelationship_thenPlacementIsNull() throws IOException {
        AAITreeNode node = new AAITreeNode();
        aaiTreeNodeBuilder.enrichPlacementData(node);
        assertNull(node.getCloudConfiguration());
    }


    @Test
    public void whenReadVnfNodeWithVfModule_thenNodeIsAsExpected() throws IOException {
        JsonNode mockedAaiGetVnfResponse = TestUtils.readJsonResourceFileAsObject("/getTopology/vnf.json", JsonNode.class);

        //add mock for vfModule of the VNF
        JsonNode mockedAaiGetVfModuleResponse = TestUtils.readJsonResourceFileAsObject("/getTopology/vfModule.json", JsonNode.class);
        when(aaiClientMock.typedAaiGet(Unchecked.toURI("anyUrl/vf-modules"), JsonNode.class)).thenReturn(mockedAaiGetVfModuleResponse);

        CloudConfiguration expectedCloudConfiguration = new CloudConfiguration("dyh3b", "c8035f5ee95d4c62bbc8074c044122b9", "irma-aic");

        AAITreeNode expectedVnfNode = createExpectedVnfTreeNode(expectedCloudConfiguration);

        AAITreeNode expectedVfModule = new AAITreeNode();
        expectedVfModule.setId("2cb6d41e-2bef-4cb2-80ce-c7815bcdcf4e");
        expectedVfModule.setName("dyh3brarf8000v_base");
        expectedVfModule.setOrchestrationStatus("Active");
        expectedVfModule.setModelInvariantId("3ecca473-b0c0-46ae-b0b7-bd2969d8b79f");
        expectedVfModule.setModelVersionId("5c35b764-e266-4498-af87-a88c4ba92dc4");
        expectedVfModule.setModelCustomizationId("06b4ece0-f6f8-4003-b445-653418292101");
        expectedVfModule.setType(NodeType.VF_MODULE);
        expectedVfModule.setInMaint(false);
        expectedVfModule.setCloudConfiguration(expectedCloudConfiguration);

        expectedVnfNode.addChildren(ImmutableList.of(expectedVfModule));

        buildNodeAndAssert(mockedAaiGetVnfResponse, expectedVnfNode, NodeType.GENERIC_VNF);
    }

    @NotNull
    public static AAITreeNode createExpectedVnfTreeNode(CloudConfiguration expectedCloudConfiguration) {
        AAITreeNode expectedVnfNode = new AAITreeNode();
        expectedVnfNode.setId("9a7a4dc1-8e5f-43fe-a360-7734c5f51382");
        expectedVnfNode.setName("dyh3brarf8000v");
        expectedVnfNode.setOrchestrationStatus("Active");
        expectedVnfNode.setModelInvariantId("b711997f-36b3-4a9b-8b37-71a0fc2ebd6d");
        expectedVnfNode.setModelVersionId("7f23e4f7-e44c-44df-b066-4cedc6950bfe");
        expectedVnfNode.setModelCustomizationId("401350be-0f56-481c-86d8-f32d573fec26");
        expectedVnfNode.setType(NodeType.GENERIC_VNF);
        expectedVnfNode.setInMaint(true);
        expectedVnfNode.setProvStatus("PREPROV");
        expectedVnfNode.setCloudConfiguration(expectedCloudConfiguration);
        return expectedVnfNode;
    }

    @DataProvider
    public static Object[][] isArrayDataProvider() {
        return new Object[][] {
                {"Json Array", buildArrayJson(NodeType.GENERIC_VNF), true},
                {"Json Object", buildOneLevelJson(NodeType.GENERIC_VNF), false},
                {"Json Array with another node type", buildArrayJson(NodeType.SERVICE_INSTANCE), false},
                {"null json", null, false}
        };
    }

    @Test(dataProvider = "isArrayDataProvider")
    public void IsArrayType(String description, JsonNode jsonNode, boolean expectedResult) {
        boolean isArray = aaiTreeNodeBuilder.isArray(jsonNode, NodeType.GENERIC_VNF);
        assertEquals(expectedResult, isArray);
    }

    @Test
    public void jsonToAaiNodeTest() {
        NodeType nodeType = NodeType.SERVICE_INSTANCE;
        JsonNode node = buildOneLevelJson(nodeType);
        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = new ConcurrentSkipListSet<>(comparing(AAITreeNode::getUniqueNodeKey));

        AAITreeNode aaiTreeNode = aaiTreeNodeBuilder.createAaiNode(nodeType, node, nodesAccumulator);

        assertEquals("any-instance-id", aaiTreeNode.getId());
        assertEquals("any-instance-name", aaiTreeNode.getName());
        assertTrue(nodesAccumulator.contains(aaiTreeNode));
    }

    @Test
    public void getNextLevelInPathsTreeTest() {
        Tree<AAIServiceTree.AaiRelationship> firstLevelTree = getPathsTree();

        Tree<AAIServiceTree.AaiRelationship> secondLevelTree = aaiTreeNodeBuilder.getNextLevelInPathsTree(firstLevelTree, NodeType.GENERIC_VNF.getType());
        assertEquals(NodeType.GENERIC_VNF.getType(), secondLevelTree.getRootValue().type);

        Tree<AAIServiceTree.AaiRelationship> thirdLevelTree = aaiTreeNodeBuilder.getNextLevelInPathsTree(secondLevelTree, NodeType.INSTANCE_GROUP.getType());
        assertEquals(NodeType.INSTANCE_GROUP.getType(), thirdLevelTree.getRootValue().type);
    }

    @Test
    public void getNextLevelInPathsTreeTest_givenIrrelevantNode_expectedNull() {
        Tree<AAIServiceTree.AaiRelationship> pathsTree = getPathsTree();

        Tree<AAIServiceTree.AaiRelationship> subTree = aaiTreeNodeBuilder.getNextLevelInPathsTree(pathsTree, NodeType.INSTANCE_GROUP.getType());

        assertNull(subTree);
    }

    @Test
    public void getRelationships_given2Relationships_expect1filtered() {
        NodeType firstRelationship = NodeType.GENERIC_VNF;
        NodeType secondRelationship = NodeType.INSTANCE_GROUP;
        JsonNode jsonNode = buildOneLevelJson(NodeType.SERVICE_INSTANCE, firstRelationship, secondRelationship);

        List<Relationship> relationships = aaiTreeNodeBuilder.getFilteredRelationships(jsonNode, getPathsTree());

        assertEquals(1, relationships.size());
        assertEquals(firstRelationship.getType(), relationships.get(0).getRelatedTo());
    }

    @Test
    public void getRelationships_givenNoRelationships_expectedEmptyListTest() {
        JsonNode jsonNode = buildOneLevelJson(NodeType.SERVICE_INSTANCE);

        List<Relationship> relationships = aaiTreeNodeBuilder.getFilteredRelationships(jsonNode, getPathsTree());

        assertThat(relationships, is(empty()));
    }

    @Test
    public void getRelationships_given2RelationshipsNotExistInTreePaths_expectAllFiltered() {
        NodeType firstRelationship = NodeType.CONFIGURATION;
        NodeType secondRelationship = NodeType.INSTANCE_GROUP;
        JsonNode jsonNode = buildOneLevelJson(NodeType.SERVICE_INSTANCE, firstRelationship, secondRelationship);

        List<Relationship> relationships = aaiTreeNodeBuilder.getFilteredRelationships(jsonNode, getPathsTree());

        assertThat(relationships, is(empty()));
    }

    @Test
    public void aggregateAllOtherPropertiesTest() {
        NodeType nodeType = NodeType.SERVICE_INSTANCE;
        JsonNode jsonNode = buildOneLevelJson(nodeType, NodeType.GENERIC_VNF, NodeType.GENERIC_VNF);
        ((ObjectNode) jsonNode).put("nf-role", "any-value");

        Map<String, Object> additionalProps = aaiTreeNodeBuilder.aggregateAllOtherProperties(jsonNode, nodeType);
        assertThat(additionalProps, is(ImmutableMap.of(
                "nf-role", "any-value")));
    }

    @Test
    public void parseNodeAndFilterRelationshipsTest() {
        NodeType nodeType = NodeType.SERVICE_INSTANCE;
        JsonNode jsonNode = buildOneLevelJson(NodeType.SERVICE_INSTANCE, NodeType.GENERIC_VNF, NodeType.NETWORK, NodeType.VF_MODULE);
        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = new ConcurrentSkipListSet<>(comparing(AAITreeNode::getUniqueNodeKey));

        Pair<AAITreeNode, List<Relationship>> resultNode = aaiTreeNodeBuilder.parseNodeAndFilterRelationships(jsonNode, nodeType,
                nodesAccumulator, getPathsTree());

        assertEquals(nodeType, resultNode.getKey().getType());
        assertEquals(2, resultNode.getValue().size());
        assertEquals(NodeType.GENERIC_VNF.getType(), resultNode.getValue().get(0).getRelatedTo());
        assertEquals(NodeType.NETWORK.getType(), resultNode.getValue().get(1).getRelatedTo());
    }

    @Test(expectedExceptions = GenericUncheckedException.class ,expectedExceptionsMessageRegExp = "AAI node fetching failed.")
    public void fetchChildrenAsyncTest_given2children_expected1Ok1Timeout() {
        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = new ConcurrentSkipListSet<>(comparing(AAITreeNode::getUniqueNodeKey));
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        AAITreeNode rootNode = createExpectedGroupNode();
        JsonNode relationshipJson = getRelationships(NodeType.GENERIC_VNF, NodeType.NETWORK);
        List<Relationship> relationships = mapper.convertValue(relationshipJson, RelationshipList.class).getRelationship();

        when(aaiClientMock.typedAaiRest(Unchecked.toURI(relationships.get(0).getRelatedLink()), JsonNode.class, null, HttpMethod.GET, false))
                .thenReturn(buildOneLevelJson(NodeType.GENERIC_VNF));

        when(aaiClientMock.typedAaiRest(Unchecked.toURI(relationships.get(1).getRelatedLink()), JsonNode.class, null, HttpMethod.GET, false))
                .thenAnswer((Answer<JsonNode>) invocation -> {
                    Thread.sleep(2000);
                    return buildOneLevelJson(NodeType.NETWORK);
                });

        aaiTreeNodeBuilder.fetchChildrenAsync(threadPool, nodesAccumulator, rootNode, relationships, getPathsTree(), 1);

        assertEquals(2, rootNode.getChildren().size());
        assertEquals(NodeType.GENERIC_VNF, rootNode.getChildren().get(0).getType());
        assertEquals(NodeType.NETWORK, rootNode.getChildren().get(1).getType());
    }

    @DataProvider
    public Object[][] testIsListOfKeyResultsDataProvider() {
        return new Object[][]{
                {"Node has results with several values",
                        "{\"results\":[{\"l3-network\":{}},{\"l3-network\":{}},{\"l3-network\":{}}]}",
                        true},
                {"Node has results with no values",
                        "{\"results\":[]}",
                        true},
                {"Node has results, but it isn't an array",
                        "{\"results\":{\"some-field\":{}}}",
                        false},
                {"Node doesn't have results",
                        "{\"l3-network\":[{},{}]}",
                        false},
                {"Node is null",
                        "null",
                        false},
        };
    }

    @Test(dataProvider = "testIsListOfKeyResultsDataProvider")
    public void testIsListOfKeyResults(String testCase, String input, boolean expectedResult) throws IOException {
        assertEquals(testCase + ": " + input,
                expectedResult, aaiTreeNodeBuilder.isListOfKeyResults(new ObjectMapper().readTree(input)));
    }

    private Tree<AAIServiceTree.AaiRelationship> getPathsTree() {
        Tree<AAIServiceTree.AaiRelationship> pathsTree = new Tree<>(new AAIServiceTree.AaiRelationship(NodeType.SERVICE_INSTANCE));
        pathsTree.addPath(AAIServiceTree.toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.INSTANCE_GROUP));
        pathsTree.addPath(AAIServiceTree.toAaiRelationshipList(NodeType.NETWORK));

        return pathsTree;
    }

    private static JsonNode buildOneLevelJson(NodeType nodeType, NodeType...relationships) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put(nodeType.getId(), "any-instance-id");
        objectNode.put(nodeType.getName(), "any-instance-name");
        if (relationships.length > 0 ) {
            objectNode.putPOJO("relationship-list", getRelationships(relationships));
        }
        return objectNode;
    }

    private static JsonNode buildArrayJson(NodeType nodeType) {
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode arrayNode = objectNode.putArray(nodeType.getType());
        arrayNode.add(buildOneLevelJson(nodeType));
        arrayNode.add(buildOneLevelJson(nodeType));

        return objectNode;
    }

    private static JsonNode getRelationship(String nodeType) {
        ObjectNode relationship = mapper.createObjectNode();
        relationship.put("related-to", nodeType);
        relationship.put("relationship-label", "org.onap.relationships.inventory.ComposedOf");
        relationship.put("related-link", "/aai/v12/network/" + nodeType + "s/" + nodeType + "/cf6f60cd-808d-44e6-978b-c663e00dba8d");
        return relationship;
    }

    private static JsonNode getRelationships(NodeType...nodeTypes) {
        ObjectNode relationshipList = mapper.createObjectNode();
        ArrayNode relationships = relationshipList.putArray("relationship");

        for (NodeType nodeType: nodeTypes) {
            relationships.add(getRelationship(nodeType.getType()));
        }

        return relationshipList;
    }

}
