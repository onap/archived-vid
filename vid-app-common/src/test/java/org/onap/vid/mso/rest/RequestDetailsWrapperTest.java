package org.onap.vid.mso.rest;

import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.RequestInfo;
import org.onap.vid.mso.model.RequestParameters;
import org.testng.annotations.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RequestDetailsWrapperTest {

    RequestDetailsWrapper requestDetailsWrapper;

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
