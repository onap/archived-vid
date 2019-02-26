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

import com.fasterxml.jackson.core.JsonParseException;
import org.apache.xmlbeans.SystemProperties;
import org.hamcrest.Matcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.model.probes.ErrorMetadata;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.onap.vid.scheduler.SchedulerServiceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.model.probes.ExternalComponentStatus.Component.SCHEDULER;

@ContextConfiguration(classes = {SystemProperties.class})
@WebAppConfiguration
public class SchedulerServiceImplTest extends AbstractTestNGSpringContextTests {

    @InjectMocks
    private SchedulerServiceImpl schedulerService;

    @Mock
    private ChangeManagementService changeManagementService;


    @BeforeTest
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void reset() {
        Mockito.reset(changeManagementService);
    }

    @Test
    public void probeGetSchedulerChangeManegements_verifyGoodRequest(){
        String responseString = "[" +
                "  {" +
                "    \"vnfName\": \"dbox0001v\"," +
                "    \"status\": \"Triggered\"," +
                "    \"aotsChangeId\": \"CHG000000000001\"," +
                "    \"aotsApprovalStatus\": \"Approved\"," +
                "    \"groupId\": \"groupId\"," +
                "    \"dispatchTime\": \"2018-05-09T14:05:43Z\"," +
                "    \"msoRequestId\": \"2fb4edd1-01c4-4fee-bd4a-4ae6282aa213\"," +
                "    \"scheduleRequest\": {" +
                "      \"id\": 1," +
                "      \"createDateTime\": \"2018-05-09T14:05:34Z\"," +
                "      \"optimizerAttemptsToSchedule\": 0," +
                "      \"optimizerTransactionId\": \"70f05563-6705-4be0-802a-8b6b78a69d63\"," +
                "      \"scheduleId\": \"70f05563-6705-4be0-802a-8b6b78a69d63\"," +
                "      \"scheduleName\": \"70f05563-6705-4be0-802a-8b6b78a69d63\"," +
                "      \"status\": \"Notifications Initiated\"," +
                "      \"userId\": \"wl849v\"," +
                "      \"domain\": \"ChangeManagement\"," +
                "      \"domainData\": [" +
                "        {" +
                "          \"id\": 1," +
                "          \"name\": \"WorkflowName\"," +
                "          \"value\": \"VNF In Place Software Update\"" +
                "        }," +
                "        {" +
                "          \"id\": 2," +
                "          \"name\": \"CallbackUrl\"," +
                "          \"value\": \"https://vid-web-ete-new.ecomp.cci.att.com:8000/vid/change-management/workflow/\"" +
                "        }," +
                "        {" +
                "          \"id\": 3," +
                "          \"name\": \"CallbackData\"," +
                "          \"value\": \"{\\\"requestType\\\":\\\"VNF In Place Software Update\\\",\\\"requestDetails\\\":[{\\\"vnfName\\\":\\\"dbox0001v\\\",\\\"vnfInstanceId\\\":\\\"815d38c0-b686-491c-9a74-0b49add524ca\\\",\\\"modelInfo\\\":{\\\"modelType\\\":\\\"vnf\\\",\\\"modelInvariantId\\\":\\\"59f4e0b2-e1b0-4e3b-bae3-e7b8c5d32985\\\",\\\"modelVersionId\\\":\\\"345643c1-3a51-423f-aac1-502e027d8dab\\\",\\\"modelName\\\":\\\"dbox0001v\\\",\\\"modelCustomizationId\\\":\\\"01ce23cb-d276-4d71-a5f1-f9d42d0df543\\\"},\\\"cloudConfiguration\\\":{\\\"lcpCloudRegionId\\\":\\\"dpa2b\\\",\\\"tenantId\\\":\\\"b60da4f71c1d4b35b8113d4eca6deaa1\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"wl849v\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"eb774932-e9fa-4c7f-bbc1-229b6b2b11e2\\\",\\\"modelInfo\\\":{\\\"modelType\\\":\\\"service\\\",\\\"modelInvariantId\\\":\\\"57dd617b-d64e-4441-a287-4d158b24ba65\\\",\\\"modelVersionId\\\":\\\"345643c1-3a51-423f-aac1-502e027d8dab\\\",\\\"modelName\\\":\\\"control_loop_dbe_svc\\\",\\\"modelVersion\\\":\\\"2.0\\\"}}}],\\\"requestParameters\\\":{\\\"payload\\\":\\\"{\\\\\\\"existing_software_version\\\\\\\":\\\\\\\"2\\\\\\\",\\\\\\\"new_software_version\\\\\\\":\\\\\\\"1\\\\\\\",\\\\\\\"operations_timeout\\\\\\\":\\\\\\\"3\\\\\\\"}\\\",\\\"testApi\\\":\\\"GR_API\\\"}}]}\"" +
                "        }" +
                "      ]," +
                "      \"scheduleApprovals\": []" +
                "    }," +
                "    \"schedulesId\": 0" +
                "  }" +
                "]";;

        final RestObject mockedRestObject = mock(RestObject.class);

        final RestObjectWithRequestInfo restObjectWithRequestInfo = new RestObjectWithRequestInfo(HttpMethod.GET, "my pretty url", mockedRestObject, 200, responseString);

        when(changeManagementService.getSchedulerChangeManagementsWithRequestInfo()).thenReturn(
                restObjectWithRequestInfo
        );

        final ExternalComponentStatus schedulerStatus = schedulerService.probeGetSchedulerChangeManagements();

        assertSchedulerStatus(schedulerStatus, true);
        assertMetadata(schedulerStatus, 200, startsWith(responseString.substring(0, 400)), "my pretty url", equalTo("OK"));
    }

    @Test
    public void probeGetSchedulerChangeManegements_response200OkButEmptyPayload_shouldDescribeCorrectly() {
        String responseString = "" +
                "[]";

        final RestObject mockedRestObject = mock(RestObject.class);

        final RestObjectWithRequestInfo restObjectWithRequestInfo = new RestObjectWithRequestInfo(HttpMethod.GET, "my pretty url", mockedRestObject, 200, responseString);

        when(changeManagementService.getSchedulerChangeManagementsWithRequestInfo()).thenReturn(
                restObjectWithRequestInfo
        );

        final ExternalComponentStatus schedulerStatus = schedulerService.probeGetSchedulerChangeManagements();

        assertSchedulerStatus(schedulerStatus, true);

       assertMetadata(schedulerStatus, 200, equalTo(responseString), "my pretty url", containsString("OK"));
   }

    @Test
    public void probeGetSchedulerChangeManegements_response200OkButInvalidPayload_shouldDescribeCorrectly() {
        String responseString = "this payload is an invalid json";

        final RestObject mockedRestObject = mock(RestObject.class);

        final RestObjectWithRequestInfo restObjectWithRequestInfo = new RestObjectWithRequestInfo(HttpMethod.GET, "my pretty url", mockedRestObject, 200, responseString);

        when(changeManagementService.getSchedulerChangeManagementsWithRequestInfo()).thenThrow(new ExceptionWithRequestInfo(HttpMethod.GET,
                "my pretty url", responseString, 200, new JsonParseException(null, "Unrecognized token")));

        final ExternalComponentStatus schedulerStatus = schedulerService.probeGetSchedulerChangeManagements();

        assertSchedulerStatus(schedulerStatus, false);

        assertMetadata(schedulerStatus, 200, equalTo(responseString), "my pretty url", containsString("JsonParseException: Unrecognized token"));
    }

    @Test
    public void probeGetSchedulerChangeManegements_throwNotFoundException_resultIsWithErrorMetadata() {
        when(changeManagementService.getSchedulerChangeManagementsWithRequestInfo()).thenThrow(
                new GenericUncheckedException(new NotFoundException("Get with status = 400")));

        final ExternalComponentStatus schedulerStatus = schedulerService.probeGetSchedulerChangeManagements();

        assertThat(schedulerStatus.isAvailable(), is(false));
        assertThat(schedulerStatus.getComponent(), is(SCHEDULER));
        assertThat(schedulerStatus.getMetadata(), instanceOf(ErrorMetadata.class));

        final ErrorMetadata metadata = ((ErrorMetadata) schedulerStatus.getMetadata());
        org.junit.Assert.assertThat(metadata.getDescription(), containsString("NotFoundException: Get with status = 400"));
    }

    private void assertSchedulerStatus(ExternalComponentStatus schedulerStatus, boolean isAvailable) {
        assertThat(schedulerStatus.isAvailable(), is(isAvailable));
        assertThat(schedulerStatus.getComponent(), is(SCHEDULER));
        assertThat(schedulerStatus.getMetadata(), instanceOf(HttpRequestMetadata.class));
    }

    private void assertMetadata(ExternalComponentStatus schedulerStatus, int httpCode, Matcher<String> rawData, String url, Matcher<String> descriptionMatcher) {
        final HttpRequestMetadata metadata = ((HttpRequestMetadata) schedulerStatus.getMetadata());
        org.junit.Assert.assertThat(metadata.getHttpMethod(), equalTo(HttpMethod.GET));
        org.junit.Assert.assertThat(metadata.getHttpCode(), equalTo(httpCode));
        org.junit.Assert.assertThat(metadata.getUrl(), equalTo(url));
        org.junit.Assert.assertThat(metadata.getRawData(), rawData);
        org.junit.Assert.assertThat(metadata.getDescription(), descriptionMatcher);
    }
}
