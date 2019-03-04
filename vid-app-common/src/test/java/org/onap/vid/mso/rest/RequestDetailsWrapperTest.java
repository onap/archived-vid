/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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

import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.RequestInfo;
import org.onap.vid.mso.model.RequestParameters;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestDetailsWrapperTest {

    private RequestDetailsWrapper requestDetailsWrapper;

    @Test
    public void shouldHaveProperConstructorAndGet(){
        //  given
        RequestDetails requestDetails = generateMockMsoRequest();

        //  when
        requestDetailsWrapper = new RequestDetailsWrapper(requestDetails);

        //  then
        assertThat(requestDetailsWrapper.getRequestDetails()).isEqualToComparingFieldByField(requestDetails);
    }


    private RequestDetails generateMockMsoRequest() {
        RequestDetails requestDetails = new RequestDetails();

        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.setTenantId("tenant-id");
        cloudConfiguration.setLcpCloudRegionId("lcp-region");
        requestDetails.setCloudConfiguration(cloudConfiguration);

        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelInvariantId("model-invarient-id");
        modelInfo.setModelCustomizationName("modelCustomizationName");
        modelInfo.setModelType("test-model-type");
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
