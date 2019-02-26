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

package org.opencomp.vid.services;

import com.google.common.collect.ImmutableList;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.util.AAITreeConverter;
import org.onap.vid.model.Action;
import org.onap.vid.model.aaiTree.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.onap.vid.asdc.parser.ToscaParserImpl2.Constants.A_LA_CARTE;

public class AAITreeConverterTest {

    @InjectMocks
    private AAITreeConverter aaiTreeConverter;

    @BeforeTest
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvertTreeToUIModel_NoChildren() throws Exception {

        AAITreeNode aaiTree = generateAaiTreeToConvert(0, 0);

        ServiceInstance result = aaiTreeConverter.convertTreeToUIModel(aaiTree, "global-customer-id", "service-type", A_LA_CARTE);

        assertService(result, 0, 0, true);
    }

    @Test
    public void testConvertTreeToUIModel_MultipleChildren() throws Exception {

        AAITreeNode aaiTree = generateAaiTreeToConvert(2, 2);

        ServiceInstance serviceInstance = aaiTreeConverter.convertTreeToUIModel(aaiTree, "global-customer-id", "service-type", null);

        assertService(serviceInstance, 2, 2, false);

        int nodesCounter = 0;
        assertThat(serviceInstance.getVnfs().entrySet(), hasSize(2));
        assertVnf(serviceInstance.getVnfs().get("vnf-model-version-id:00"+(nodesCounter++)), 0, 0, false);
        assertVnf(serviceInstance.getVnfs().get("vnf-model-version-id:00"+(nodesCounter++)), 0, 0, false);

        assertThat(serviceInstance.getNetworks().entrySet(), hasSize(2));
        assertNetwork(serviceInstance.getNetworks().get("network-model-version-id:00"+(nodesCounter++)), false);
        assertNetwork(serviceInstance.getNetworks().get("network-model-version-id:00"+(nodesCounter)), false);
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
            vnfTreeNode.getChildren().add(createVfModule(nodesCounter++, customizationName));
        }

        for (Boolean customizationName: networks) {
            vnfTreeNode.getChildren().add(createNetwork(nodesCounter++, customizationName));
        }

        Vnf actualVnf = Vnf.from(vnfTreeNode);

        assertVnf(actualVnf, vfModules.size(), networks.size(), false);

        nodesCounter = 0;
        for (Boolean customizationName: vfModules) {
            String key = customizationName ? "vfModule key in model" : "vfModule-model-version-id";

            assertThat(actualVnf.getVfModules(), hasKey(key));
            assertThat(actualVnf.getVfModules().get(key), hasKey(key + ":00" + nodesCounter));
            VfModule actualVfModule = actualVnf.getVfModules().get(key).get(key + ":00" + nodesCounter);
            assertVfModule(actualVfModule, customizationName);
            nodesCounter++;
        }

        for (Boolean customizationName: networks) {
            String key = customizationName ? "network key in model" : "network-model-version-id";

            assertThat(actualVnf.getNetworks(), hasKey(key + ":00" + nodesCounter));
            Network actualNetwork = actualVnf.getNetworks().get(key + ":00" + nodesCounter);
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
        vnfTreeNode.setId("vnf-instance-id");
        vnfTreeNode.setName("vnf-instance-name");
        vnfTreeNode.setType("generic-vnf");
        vnfTreeNode.setModelVersionId("vnf-model-version-id");
        if (hasCustomizationName) {
            vnfTreeNode.setModelCustomizationName("vnf model customization name");
            vnfTreeNode.setKeyInModel("vnf key in model");
        }
        vnfTreeNode.setUniqueNumber(uniqueNumber);
        return vnfTreeNode;
    }

    private AAITreeNode createVfModule(int uniqueNumber, boolean hasCustomizationName) {
        AAITreeNode vfModuleTreeNode = new AAITreeNode();
        vfModuleTreeNode.setId("vfModule-instance-id");
        vfModuleTreeNode.setName("vfModule-instance-name");
        vfModuleTreeNode.setType("vf-module");
        vfModuleTreeNode.setModelVersionId("vfModule-model-version-id");
        if (hasCustomizationName) {
            vfModuleTreeNode.setModelCustomizationName("vfModule model customization name");
            vfModuleTreeNode.setKeyInModel("vfModule key in model");
        }
        vfModuleTreeNode.setUniqueNumber(uniqueNumber);

        return vfModuleTreeNode;
    }

    private AAITreeNode createNetwork(int uniqueNumber, boolean hasCustomizationName) {
        AAITreeNode networkTreeNode = new AAITreeNode();
        networkTreeNode.setId("network-instance-id");
        networkTreeNode.setName("network-instance-name");
        networkTreeNode.setType("l3-network");
        networkTreeNode.setModelVersionId("network-model-version-id");
        if (hasCustomizationName) {
            networkTreeNode.setModelCustomizationName("network model customization name");
            networkTreeNode.setKeyInModel("network key in model");
        }
        networkTreeNode.setUniqueNumber(uniqueNumber);

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

    private void assertVnf(Vnf actualVnf, int expectedVfModules, int expectedNetworks, boolean hasCustomizationName) {
        assertThat(actualVnf.getInstanceId(), is("vnf-instance-id"));
        assertThat(actualVnf.getInstanceName(), is("vnf-instance-name"));
        assertThat(actualVnf.getAction(), is(Action.None));
        assertThat(actualVnf.getModelInfo().getModelType(), is("vnf"));
        assertThat(actualVnf.getModelInfo().getModelVersionId(), is("vnf-model-version-id"));
        assertThat(actualVnf.getVfModules().entrySet(), hasSize(expectedVfModules));
        assertThat(actualVnf.getNetworks().entrySet(), hasSize(expectedNetworks));
        assertThat(actualVnf.getTrackById(), is(not(emptyOrNullString())));
        String expectedCustomizationName = hasCustomizationName ? "vnf model customization name" : null;
        assertThat(actualVnf.getModelInfo().getModelCustomizationName(), is(expectedCustomizationName));
    }

    private void assertVfModule(VfModule actualVfModule, boolean hasCustomizationName) {
        assertThat(actualVfModule.getInstanceId(), is("vfModule-instance-id"));
        assertThat(actualVfModule.getInstanceName(), is("vfModule-instance-name"));
        assertThat(actualVfModule.getAction(), is(Action.None));
        assertThat(actualVfModule.getModelInfo().getModelType(), is("vfModule"));
        assertThat(actualVfModule.getModelInfo().getModelVersionId(), is("vfModule-model-version-id"));
        assertThat(actualVfModule.getTrackById(), is(not(emptyOrNullString())));
        String expectedCustomizationName = hasCustomizationName ? "vfModule model customization name" : null;
        assertThat(actualVfModule.getModelInfo().getModelCustomizationName(), is(expectedCustomizationName));
    }

    private void assertNetwork(Network actualNetwork, boolean hasCustomizationName) {
        assertThat(actualNetwork.getInstanceId(), is("network-instance-id"));
        assertThat(actualNetwork.getInstanceName(), is("network-instance-name"));
        assertThat(actualNetwork.getAction(), is(Action.None));
        assertThat(actualNetwork.getModelInfo().getModelType(), is("network"));
        assertThat(actualNetwork.getModelInfo().getModelVersionId(), is("network-model-version-id"));
        assertThat(actualNetwork.getTrackById(), is(not(emptyOrNullString())));
        String expectedCustomizationName = hasCustomizationName ? "network model customization name" : null;
        assertThat(actualNetwork.getModelInfo().getModelCustomizationName(), is(expectedCustomizationName));
    }

    private AAITreeNode generateAaiTreeToConvert(int numberOfVnfs, int numberOfNetworks) {
        int counter = 0;
        AAITreeNode aaiTree = new AAITreeNode();
        aaiTree.setId("service-instance-id");
        aaiTree.setName("service-instance-name");

        for (int i = 0; i < numberOfVnfs; i++) {
            aaiTree.getChildren().add(createVnf(counter++, false));
        }

        for (int i = 0; i < numberOfNetworks; i++) {
            aaiTree.getChildren().add(createNetwork(counter++, false));
        }

        return aaiTree;
    }
}
