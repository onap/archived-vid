/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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
package org.onap.vid.mso.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.mockito.Mock;
import org.onap.vid.changeManagement.RequestDetails;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.domain.mso.CloudConfiguration;
import org.onap.vid.domain.mso.ModelInfo;
import org.onap.vid.domain.mso.RequestInfo;
import org.onap.vid.domain.mso.RequestParameters;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.onap.vid.controllers.LocalWebConfig;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.properties.BaseUrlProvider;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;


@ContextConfiguration(classes = {LocalWebConfig.class, SystemProperties.class})
@WebAppConfiguration
public class MsoRestClientTest {


    @Mock
    private BaseUrlProvider baseUrlProvider;

    private MsoBusinessLogic msoBusinessLogic = new MsoBusinessLogicImpl(new MsoRestClientNew(new SyncRestClient(), baseUrlProvider), null);
    private ObjectMapper om = new ObjectMapper();

    @Test
    public void createInPlaceMsoRequest() {
        String result = null;
        try {
            RequestDetails requestDetails = generateMockMsoRequest();
            result = om.writeValueAsString(msoBusinessLogic.generateInPlaceMsoRequest(requestDetails));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            Assert.fail("Failed to create mso request");
        }
        JSONObject jsonObj = new JSONObject(result);
        Assert.assertNotNull(jsonObj.getJSONObject("requestDetails"));
    }

    private RequestDetails generateMockMsoRequest() {
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setVnfInstanceId("vnf-instance-id");
        requestDetails.setVnfName("vnf-name");
        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.setTenantId("tenant-id");
        cloudConfiguration.setLcpCloudRegionId("lcp-region");
        requestDetails.setCloudConfiguration(cloudConfiguration);
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelInvariantId("model-invarient-id");
        modelInfo.setModelCustomizationName("modelCustomizationName");
        requestDetails.setModelInfo(modelInfo);
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setRequestorId("ok883e");
        requestInfo.setSource("VID");
        requestDetails.setRequestInfo(requestInfo);
        RequestParameters requestParameters = new RequestParameters();
        requestParameters.setSubscriptionServiceType("subscriber-service-type");
        requestParameters.setAdditionalProperty("a", 1);
        requestParameters.setAdditionalProperty("b", 2);
        requestParameters.setAdditionalProperty("c", 3);
        requestParameters.setAdditionalProperty("d", 4);
        String payload = "{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}";
        requestParameters.setAdditionalProperty("payload", payload);

        requestDetails.setRequestParameters(requestParameters);
        return requestDetails;
    }
}
