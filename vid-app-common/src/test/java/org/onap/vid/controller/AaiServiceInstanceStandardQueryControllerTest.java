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

package org.onap.vid.controller;

import com.google.common.collect.ImmutableList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.model.Service;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.VidNotions.ModelCategory;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.properties.Features;
import org.onap.vid.services.AAIServiceTree;
import org.onap.vid.services.VidService;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AaiServiceInstanceStandardQueryControllerTest {

    @InjectMocks
    private AaiServiceInstanceStandardQueryController aaiServiceInstanceStandardQueryController;

    @Mock
    private FeatureManager featureManager;

    @Mock
    private VidService sdcService;

    @Mock
    private AAIServiceTree aaiServiceTree;


    //Don't use initMocks with @BeforeMethod
    //because AaiServiceInstanceStandardQueryController contains final members that can not be injected twice
    //See https://stackoverflow.com/questions/20046210/mockito-injectmocks-strange-behaviour-with-final-fields?answertab=active#tab-top
    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        reset(sdcService, featureManager);
    }

    @Test
    public void getNetworksToVlansByServiceInstance_given5G_PROVIDER_NETWORK_aaiIsAccessed() throws AsdcCatalogException {
        //  - turn on FLAG_PRESENT_PROVIDER_NETWORKS
        //  - mock an model with 5G_PROVIDER_NETWORK
        //  - request it's AAI network->vlan mapping
        //  - assert that AAI was accessed

        when(featureManager.isActive(Features.FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS)).thenReturn(true);
        when(aaiServiceTree.buildAAITree(any(), any(), any(HttpMethod.class), any(), anyBoolean())).thenReturn(ImmutableList.of(mock(AAITreeNode.class)));

        final UUID randomModelUuid = UUID.randomUUID();
        mockServiceModel(ModelCategory.IS_5G_PROVIDER_NETWORK_MODEL, randomModelUuid);

        doGetNetworksToVlansByServiceInstance(randomModelUuid);

        verify(aaiServiceTree).buildAAITree(any(), any(), any(HttpMethod.class), any(), anyBoolean());
    }

    @Test
    public void getNetworksToVlansByServiceInstance_givenNon5G_PROVIDER_NETWORK_aaiIsNotAccessed() throws AsdcCatalogException {
        //  - turn on FLAG_PRESENT_PROVIDER_NETWORKS
        //  - mock an model without 5G_PROVIDER_NETWORK (i.e. OTHER)
        //  - request it's AAI network->vlan mapping
        //  - assert that AAI was not accessed
        //  - empty result was responded

        when(featureManager.isActive(Features.FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS)).thenReturn(true);

        final UUID randomModelUuid = UUID.randomUUID();
        mockServiceModel(ModelCategory.OTHER, randomModelUuid);

        assertThat(doGetNetworksToVlansByServiceInstance(randomModelUuid).serviceNetworks, hasSize(0));
        verifyZeroInteractions(aaiServiceTree);
    }

    @Test
    public void isModelOf5g_givenServiceWithFabricConfiguration_returnTrue() throws AsdcCatalogException {
        final UUID randomModelUuid = UUID.randomUUID();
        mockServiceModel(ModelCategory.IS_5G_FABRIC_CONFIGURATION_MODEL, randomModelUuid, VidNotions.InstantiationUI.SERVICE_WITH_FABRIC_CONFIGURATION);

        assertTrue(aaiServiceInstanceStandardQueryController.isModelOf5g(randomModelUuid));
    }

    private void mockServiceModel(ModelCategory modelCategory, UUID randomModelUuid) throws AsdcCatalogException {
        mockServiceModel(modelCategory, randomModelUuid, VidNotions.InstantiationUI.LEGACY);
    }

    private void mockServiceModel(ModelCategory modelCategory, UUID randomModelUuid, VidNotions.InstantiationUI instantiationUI) throws AsdcCatalogException {
        ServiceModel mockedModel = mock(ServiceModel.class);
        Service mockedService = mock(Service.class);
        when(mockedModel.getService()).thenReturn(mockedService);
        when(mockedService.getVidNotions()).thenReturn(
                new VidNotions(instantiationUI, modelCategory, VidNotions.InstantiationUI.LEGACY, VidNotions.InstantiationType.ALaCarte)
        );

        when(sdcService.getService(randomModelUuid.toString())).thenReturn(mockedModel);
    }

    private AaiServiceInstanceStandardQueryController.VlansByNetworksHierarchy doGetNetworksToVlansByServiceInstance(UUID randomModelUuid) throws AsdcCatalogException {
        return aaiServiceInstanceStandardQueryController.getNetworksToVlansByServiceInstance(
                new MockHttpServletRequest(),
                randomModelUuid,
                "my global customer id",
                "my service type",
                "my instance id");
    }
}