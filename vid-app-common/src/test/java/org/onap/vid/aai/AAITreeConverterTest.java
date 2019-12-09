/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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
package org.onap.vid.aai;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.onap.vid.asdc.parser.ToscaParserImpl2.Constants.A_LA_CARTE;
import static org.testng.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.onap.vid.aai.util.AAITreeConverter;
import org.onap.vid.model.Action;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.CollectionResource;
import org.onap.vid.model.aaiTree.Network;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.model.aaiTree.ServiceInstance;
import org.onap.vid.model.aaiTree.VfModule;
import org.onap.vid.model.aaiTree.Vnf;
import org.onap.vid.mso.model.CloudConfiguration;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AAITreeConverterTest {

    @Spy
    private ModelUtil modelUtil;

    @InjectMocks
    private AAITreeConverter aaiTreeConverter;

    @BeforeTest
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvertTreeToUIModel_NoChildren() throws Exception {

        AAITreeNode aaiTree = generateAaiTreeToConvert(0, 0);

        ServiceInstance result = aaiTreeConverter.convertTreeToUIModel(aaiTree, "global-customer-id", "service-type", A_LA_CARTE, "", "");

        assertService(result, 0, 0, true);
    }

    @Test
    public void testConvertTreeToUIModel_MultipleChildren() {

        AAITreeNode aaiTree = generateAaiTreeToConvert(2, 2);

        ServiceInstance serviceInstance = aaiTreeConverter.convertTreeToUIModel(aaiTree, "global-customer-id", "service-type", null, "", "");

        assertService(serviceInstance, 2, 2, false);

        int nodesCounter = 0;
        assertThat(serviceInstance.getVnfs().entrySet(), hasSize(2));
        assertVnf(serviceInstance.getVnfs().get("vnf-instance-id" + (nodesCounter++)), 0, 0);
        assertVnf(serviceInstance.getVnfs().get("vnf-instance-id" + (nodesCounter++)), 0, 0);

        assertThat(serviceInstance.getNetworks().entrySet(), hasSize(2));
        assertNetwork(serviceInstance.getNetworks().get("network-instance-id" + (nodesCounter++)), false);
        assertNetwork(serviceInstance.getNetworks().get("network-instance-id" + (nodesCounter++)), false);

        assertThat(serviceInstance.getVnfGroups().size(), equalTo(1));
        assertThat(serviceInstance.getVnfGroups().get("vnf-group-id" + (nodesCounter++)).getInstanceId(), startsWith("vnf-group-id"));

        assertThat(serviceInstance.getCollectionResources().size(), equalTo(1));
        CollectionResource cr = serviceInstance.getCollectionResources().get("cr-id" + (nodesCounter++));
        assertThat(cr.getInstanceId(), startsWith("cr-id"));

        assertThat(cr.getNcfs().size(), equalTo(1));
        assertThat(cr.getNcfs().get("ncf-id").getInstanceId(), startsWith("ncf-id"));

        assertThat(serviceInstance.getExistingVNFCounterMap().get("vnf-model-customization-id"), equalTo(2L));
        assertThat(serviceInstance.getExistingNetworksCounterMap().get("network-model-customization-id"), equalTo(2L));
    }

    @DataProvider
    public static Object[][] vnfWithChildren() {
        return new Object[][]{
                {ImmutableList.of(), ImmutableList.of()},
                {ImmutableList.of(true, false), ImmutableList.of()},
                {ImmutableList.of(), ImmutableList.of(false)}
        };
    }

    @Test(dataProvider = "vnfWithChildren")
    public void testComplexVnfConversion(List<Boolean> vfModules, List<Boolean> networks) {

        AAITreeNode vnfTreeNode = createVnf(1, false);
        int nodesCounter = 0;

        for (Boolean customizationName: vfModules) {
            vnfTreeNode.getChildren().add(createVfModule(customizationName));
        }

        for (Boolean customizationName: networks) {
            vnfTreeNode.getChildren().add(createNetwork(nodesCounter++, customizationName));
        }

        Vnf actualVnf = Vnf.from(vnfTreeNode);

        assertVnf(actualVnf, vfModules.size(), networks.size());

        nodesCounter = 0;
        for (Boolean customizationName: vfModules) {
            String key = customizationName ? "vfModule key in model" : "vfModule-model-version-id";

            assertThat(actualVnf.getVfModules(), hasKey(key));
            assertThat(actualVnf.getVfModules().get(key), hasKey("vfModule-instance-id"));
            VfModule actualVfModule = actualVnf.getVfModules().get(key).get("vfModule-instance-id");
            assertVfModule(actualVfModule, customizationName);
        }

        for (Boolean customizationName: networks) {
            assertThat(actualVnf.getNetworks(), hasKey("network-instance-id" + nodesCounter));
            Network actualNetwork = actualVnf.getNetworks().get("network-instance-id" + nodesCounter);
            assertNetwork(actualNetwork, customizationName);
            nodesCounter++;
        }
    }

    @Test
    public void testNetworkConversion() {
        AAITreeNode networkTreeNode = createNetwork(1, true);

        Network actualNetwork = Network.from(networkTreeNode);

        assertNetwork(actualNetwork, true);
    }

    private AAITreeNode createVnf(int uniqueNumber, boolean hasCustomizationName) {
        AAITreeNode vnfTreeNode = new AAITreeNode();
        vnfTreeNode.setId("vnf-instance-id" + uniqueNumber);
        vnfTreeNode.setName("vnf-instance-name");
        vnfTreeNode.setType(NodeType.GENERIC_VNF);
        vnfTreeNode.setModelVersionId("vnf-model-version-id");
        vnfTreeNode.setModelCustomizationId("vnf-model-customization-id");
        if (hasCustomizationName) {
            vnfTreeNode.setModelCustomizationName("vnf model customization name");
            vnfTreeNode.setKeyInModel("vnf key in model");
        }
        return vnfTreeNode;
    }

    private AAITreeNode createVnfGroup(int uniqueNumber) {
        AAITreeNode treeNode = new AAITreeNode();
        treeNode.setId("vnf-group-id" + uniqueNumber);
        treeNode.setType(NodeType.INSTANCE_GROUP);
        treeNode.getAdditionalProperties().put("instance-group-type", "vnfGroup-type");
        return treeNode;
    }

    private AAITreeNode createCollectionResource(int uniqueNumber) {
        AAITreeNode treeNode = new AAITreeNode();
        treeNode.setId("cr-id" + uniqueNumber);
        treeNode.setType(NodeType.COLLECTION_RESOURCE);
        treeNode.getChildren().add(createNCF());
        return treeNode;
    }

    private AAITreeNode createNCF() {
        AAITreeNode treeNode = new AAITreeNode();
        treeNode.setId("ncf-id");
        treeNode.setType(NodeType.INSTANCE_GROUP);
        treeNode.getAdditionalProperties().put("instance-group-type", "L3-NETWORK");
        return treeNode;
    }

    @Test
    public void givenPlacementIsNull_whenConvertToNetwork_relevantFieldsAreAlsoNull() {
        AAITreeNode aaiTreeNode = new AAITreeNode();
        aaiTreeNode.setType(NodeType.NETWORK);
        Network actualNetwork = Network.from(aaiTreeNode);
        assertNull(actualNetwork.getCloudOwner());
        assertNull(actualNetwork.getLcpCloudRegionId());
        assertNull(actualNetwork.getTenantId());
    }

    private AAITreeNode createVfModule(boolean hasCustomizationName) {
        AAITreeNode vfModuleTreeNode = new AAITreeNode();
        vfModuleTreeNode.setId("vfModule-instance-id");
        vfModuleTreeNode.setName("vfModule-instance-name");
        vfModuleTreeNode.setType(NodeType.VF_MODULE);
        vfModuleTreeNode.setModelVersionId("vfModule-model-version-id");
        vfModuleTreeNode.setModelCustomizationId("vfModule-model-customization-id");
        if (hasCustomizationName) {
            vfModuleTreeNode.setModelCustomizationName("vfModule model customization name");
            vfModuleTreeNode.setKeyInModel("vfModule key in model");
        }
        vfModuleTreeNode.setCloudConfiguration(new CloudConfiguration("lcpRegion2", "tenant3", "cloudOwner1"));
        return vfModuleTreeNode;
    }

    private AAITreeNode createNetwork(int uniqueNumber, boolean hasCustomizationName) {
        AAITreeNode networkTreeNode = new AAITreeNode();
        networkTreeNode.setId("network-instance-id" + uniqueNumber);
        networkTreeNode.setName("network-instance-name");
        networkTreeNode.setType(NodeType.NETWORK);
        networkTreeNode.setModelVersionId("network-model-version-id");
        networkTreeNode.setModelCustomizationId("network-model-customization-id");
        if (hasCustomizationName) {
            networkTreeNode.setModelCustomizationName("network model customization name");
            networkTreeNode.setKeyInModel("network key in model");
        }
        networkTreeNode.setCloudConfiguration(new CloudConfiguration("auk51a", "b530fc990b6d4334bd45518bebca6a51", "att-nc"));
        return networkTreeNode;
    }

    private void assertService(ServiceInstance serviceInstance, int expectedVnfs, int expectedNetworks, boolean isALaCarte) {
        assertThat(serviceInstance.getInstanceId(), is("service-instance-id"));
        assertThat(serviceInstance.getInstanceName(), is("service-instance-name"));
        assertThat(serviceInstance.getAction(), is(Action.None));
        assertThat(serviceInstance.getGlobalSubscriberId(), is("global-customer-id"));
        assertThat(serviceInstance.getSubscriptionServiceType(), is("service-type"));
        assertThat(serviceInstance.getModelInfo().getModelType(), is("service"));
        assertThat(serviceInstance.getVnfs().entrySet(), hasSize(expectedVnfs));
        assertThat(serviceInstance.getNetworks().entrySet(), hasSize(expectedNetworks));
        assertThat(serviceInstance.getIsALaCarte(), is(isALaCarte));
    }

    private void assertVnf(Vnf actualVnf, int expectedVfModules, int expectedNetworks) {
        assertThat(actualVnf.getInstanceId(), containsString("vnf-instance-id"));
        assertThat(actualVnf.getInstanceName(), is("vnf-instance-name"));
        assertThat(actualVnf.getAction(), is(Action.None));
        assertThat(actualVnf.getModelInfo().getModelType(), is("vnf"));
        assertThat(actualVnf.getModelInfo().getModelVersionId(), is("vnf-model-version-id"));
        assertThat(actualVnf.getVfModules().entrySet(), hasSize(expectedVfModules));
        assertThat(actualVnf.getNetworks().entrySet(), hasSize(expectedNetworks));
        assertThat(actualVnf.getTrackById(), containsString("vnf-instance-id"));
        assertNull(actualVnf.getModelInfo().getModelCustomizationName());
    }

    private void assertVfModule(VfModule actualVfModule, boolean hasCustomizationName) {
        assertThat(actualVfModule.getInstanceId(), is("vfModule-instance-id"));
        assertThat(actualVfModule.getInstanceName(), is("vfModule-instance-name"));
        assertThat(actualVfModule.getAction(), is(Action.None));
        assertThat(actualVfModule.getModelInfo().getModelType(), is("vfModule"));
        assertThat(actualVfModule.getModelInfo().getModelVersionId(), is("vfModule-model-version-id"));
        assertThat(actualVfModule.getTrackById(), is("vfModule-instance-id"));
        String expectedCustomizationName = hasCustomizationName ? "vfModule model customization name" : null;
        assertThat(actualVfModule.getModelInfo().getModelCustomizationName(), is(expectedCustomizationName));
        assertThat(actualVfModule.getCloudOwner(), is("cloudOwner1"));
        assertThat(actualVfModule.getLcpCloudRegionId(), is("lcpRegion2"));
        assertThat(actualVfModule.getTenantId(), is("tenant3"));
    }

    private void assertNetwork(Network actualNetwork, boolean hasCustomizationName) {
        assertThat(actualNetwork.getInstanceId(), containsString("network-instance-id"));
        assertThat(actualNetwork.getInstanceName(), is("network-instance-name"));
        assertThat(actualNetwork.getAction(), is(Action.None));
        assertThat(actualNetwork.getModelInfo().getModelType(), is("network"));
        assertThat(actualNetwork.getModelInfo().getModelVersionId(), is("network-model-version-id"));
        assertThat(actualNetwork.getTrackById(), containsString("network-instance-id"));
        String expectedCustomizationName = hasCustomizationName ? "network model customization name" : null;
        assertThat(actualNetwork.getModelInfo().getModelCustomizationName(), is(expectedCustomizationName));
        assertThat(actualNetwork.getCloudOwner(), is("att-nc"));
        assertThat(actualNetwork.getLcpCloudRegionId(), is("auk51a"));
        assertThat(actualNetwork.getTenantId(), is("b530fc990b6d4334bd45518bebca6a51"));

    }

    private AAITreeNode generateAaiTreeToConvert(int numberOfVnfs, int numberOfNetworks) {
        int counter = 0;
        AAITreeNode aaiTree = new AAITreeNode();
        aaiTree.setId("service-instance-id");
        aaiTree.setName("service-instance-name");
        aaiTree.setType(NodeType.SERVICE_INSTANCE);

        for (int i = 0; i < numberOfVnfs; i++) {
            aaiTree.getChildren().add(createVnf(counter++, false));
        }

        for (int i = 0; i < numberOfNetworks; i++) {
            aaiTree.getChildren().add(createNetwork(counter++, false));
        }

        aaiTree.getChildren().add(createVnfGroup(counter++));
        aaiTree.getChildren().add(createCollectionResource(counter++));

        return aaiTree;
    }
}