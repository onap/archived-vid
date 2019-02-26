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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.properties.Features;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class CloudOwnerServiceTest {

    @Mock
    private AaiClientInterface aaiClient;

    @Mock
    private FeatureManager featureManager;

    @InjectMocks
    private CloudOwnerServiceImpl cloudOwnerService;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        Mockito.reset(aaiClient);
        Mockito.reset(featureManager);
    }

    @DataProvider
    public static Object[][] testEnrichRequestDataProvider() {
        return new Object[][]{{true}, {false}};
    }

    @Test(dataProvider = "testEnrichRequestDataProvider")
    public void whenCloudConfigurationInAdditionalProperties_cloudConfigurationIsEnrichedWithCloudOwner(boolean isFeatureActive) {
        when(featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(isFeatureActive);
        String lcpCloudRegionId = "lcp1";
        RequestDetails requestDetails = createRequestDetailsWithCloudConfigurationInAdditionalProperties(lcpCloudRegionId);
        String aaiCloudOwner = "myCloudOwner";
        when(aaiClient.getCloudOwnerByCloudRegionId(lcpCloudRegionId)).thenReturn(aaiCloudOwner);
        cloudOwnerService.enrichRequestWithCloudOwner(requestDetails);
        if (isFeatureActive) {
            assertEquals(aaiCloudOwner, requestDetails.extractValueByPathUsingAdditionalProperties(
                    ImmutableList.of("requestDetails", "cloudConfiguration", "cloudOwner"), String.class));
        }
        else {
            Map<String,Object> cloudConfiguration = requestDetails.extractValueByPathUsingAdditionalProperties(
                    ImmutableList.of("requestDetails", "cloudConfiguration"), Map.class);
            assertThat(cloudConfiguration, not(hasKey("cloudOwner")));
        }
    }

    @Test(dataProvider = "testEnrichRequestDataProvider")
    public void whenCloudConfigurationInRequestDetailsField_cloudConfigurationIsEnrichedWithCloudOwner(boolean isFeatureActive) {
        when(featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(isFeatureActive);
        String lcpCloudRegionId = "lcp1";
        RequestDetails requestDetails = createRequestDetailsWithCloudField(lcpCloudRegionId);
        String aaiCloudOwner = "myCloudOwner";
        when(aaiClient.getCloudOwnerByCloudRegionId(lcpCloudRegionId)).thenReturn(aaiCloudOwner);
        cloudOwnerService.enrichRequestWithCloudOwner(requestDetails);
        if (isFeatureActive) {
            assertEquals(aaiCloudOwner, requestDetails.getCloudConfiguration().getCloudOwner());
        }
        else {
            assertNull(requestDetails.getCloudConfiguration().getCloudOwner());
        }
    }

    private RequestDetails createRequestDetailsWithCloudConfigurationInAdditionalProperties(String lcpCloudRegionId) {
        RequestDetails requestDetails = new RequestDetails();
        Map<String, Object> cloudConfiguration = new HashMap<>();
        cloudConfiguration.put("lcpCloudRegionId", lcpCloudRegionId);
        requestDetails.setAdditionalProperty("requestDetails",
                ImmutableMap.of("cloudConfiguration", cloudConfiguration));
        return requestDetails;
    }

    private RequestDetails createRequestDetailsWithCloudField(String lcpCloudRegionId) {
        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.setLcpCloudRegionId(lcpCloudRegionId);
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setCloudConfiguration(cloudConfiguration);
        return requestDetails;
    }

    @Test(expectedExceptions= GenericUncheckedException.class)
    public void whenAaiClientThrowException_thenExceptionIsPopulatedByEnrichMethod() {
        when(featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(true);
        String lcpCloudRegionId = "lcp1";
        RequestDetails requestDetails = createRequestDetailsWithCloudConfigurationInAdditionalProperties(lcpCloudRegionId);
        when(aaiClient.getCloudOwnerByCloudRegionId(lcpCloudRegionId)).thenThrow(new RuntimeException());
        cloudOwnerService.enrichRequestWithCloudOwner(requestDetails);
    }

    @Test
    public void whenThereIsNoCloudConfiguration_enrichmentMethodNotFailed() {
        when(featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(true);
        RequestDetails requestDetails = new RequestDetails();
        cloudOwnerService.enrichRequestWithCloudOwner(requestDetails);
        //if no exception was thrown test success
    }
}
