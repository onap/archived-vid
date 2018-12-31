package org.onap.vid.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.utils.Unchecked;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.comparing;
import static org.mockito.Mockito.when;
import static org.onap.vid.services.AAIServiceTree.AAI_TREE_PATHS;

public class AAITreeNodeBuilderTest {

    AAITreeNodeBuilder aaiTreeNodeBuilder;

    @Mock
    AaiClientInterface aaiClientMock;

    @Mock
    ThreadPoolExecutor threadPoolMock;


    @BeforeTest
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        aaiTreeNodeBuilder = new AAITreeNodeBuilder(aaiClientMock);
    }

    @Test
    public void buildNode_buildGroupNode_NodeIsAsExpected(){
        ConcurrentSkipListSet<AAITreeNode> nodesAccumulator = new ConcurrentSkipListSet<>(comparing(AAITreeNode::getUniqueNodeKey));
        ConcurrentLinkedQueue<String> visitedNodes = new ConcurrentLinkedQueue<>();
        when(aaiClientMock.typedAaiGet(Unchecked.toURI("anyUrl"), JsonNode.class)).thenReturn(createGroupJson());

        AAITreeNode groupNode = aaiTreeNodeBuilder.buildNode("instance-group",
                "anyUrl",
                nodesAccumulator,
                threadPoolMock,
                visitedNodes,
                new AtomicInteger(0),
                AAI_TREE_PATHS).get(0);

        AAITreeNode expectedGroupNode = createExpectedGroupNode();
        assertNodeIsAsExpected(expectedGroupNode,groupNode);
    }

    private void assertNodeIsAsExpected(AAITreeNode expectedGroupNode, AAITreeNode groupNode) {
        Assert.assertEquals(groupNode.getId(), expectedGroupNode.getId());
        Assert.assertEquals(groupNode.getType(), expectedGroupNode.getType());
        Assert.assertEquals(groupNode.getName(), expectedGroupNode.getName());
        Assert.assertEquals(groupNode.getModelVersionId(), expectedGroupNode.getModelVersionId());
        Assert.assertEquals(groupNode.getModelInvariantId(), expectedGroupNode.getModelInvariantId());
        Assert.assertEquals(groupNode.getInMaint(), expectedGroupNode.getInMaint());
        Assert.assertEquals(groupNode.getAdditionalProperties(), expectedGroupNode.getAdditionalProperties());
    }

    private AAITreeNode createExpectedGroupNode() {
        AAITreeNode expectedNode = new AAITreeNode();
        expectedNode.setId("c4fcf022-31a0-470a-b5b8-c18335b7af32");
        expectedNode.setType("instance-group");
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
                    "      \"relationship\": [{" +
                    "       \"related-to\": \"generic-vnf\"," +
                    "       \"relationship-label\": \"org.onap.relationships.inventory.MemberOf\" ," +
                    "       \"related-link\": \"/aai/v14/network/generic-vnfs/generic-vnf/8c54c369-2876-4423-9b33-80f783f29082\" ," +
                    "       \"relationship-data\": [{" +
                    "        \"relationship-key\": \"generic-vnf.vnf-id\"," +
                    "        \"relationship-value\": \"8c54c369-2876-4423-9b33-80f783f29082\"" +
                    "      }" +
                    "    ]," +
                    "      \"related-to-property\": [{" +
                    "      \"property-key\": \"generic-vnf.vnf-name\"," +
                    "      \"property-value\": \"zrdm5bffad01\"" +
                    "    }" +
                    "    ]" +
                    "    }" +
                    "    ]" +
                    "    }" +
                    "    }");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groupNode;
    }
}
