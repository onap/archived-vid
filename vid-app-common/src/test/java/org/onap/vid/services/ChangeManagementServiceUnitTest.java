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
package org.onap.vid.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.mockito.ArgumentCaptor;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.controllers.MsoConfig;
import org.onap.vid.controllers.WebConfig;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.properties.AsdcClientConfiguration;
import org.onap.vid.scheduler.SchedulerRestInterfaceIfc;
import org.onap.vid.testUtils.RegExMatcher;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@Test
@ContextConfiguration(classes = {WebConfig.class, AsdcClientConfiguration.class, SystemProperties.class, ChangeManagementServiceUnitTest.TestMsoConfig.class})
@WebAppConfiguration
public class ChangeManagementServiceUnitTest extends AbstractTestNGSpringContextTests {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Inject
    private ChangeManagementService changeManagementService;
    @Inject
    private MsoInterface restClientUnderTest;

   // @Test
    void testInPlaceSoftwareUpdateRequest() throws Exception {


        doReturn(new HttpResponse<>(anyObject(), RequestReferencesContainer.class, anyObject())).when(restClientUnderTest).post(anyString(), anyObject(), anyObject());

        URL requestJsonUrl = this.getClass().getResource("/services/change_management_software_update_request.json");
        ChangeManagementRequest changeManagementRequest = objectMapper.readValue(requestJsonUrl, ChangeManagementRequest.class);
        changeManagementService.doChangeManagement(changeManagementRequest, "vidVnf");

        ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<RequestDetailsWrapper> requestCaptor = ArgumentCaptor.forClass(RequestDetailsWrapper.class);
        ArgumentCaptor<Class> responseTypeCaptor = ArgumentCaptor.forClass(Class.class);
        verify(restClientUnderTest).post(endpointCaptor.capture(), requestCaptor.capture(), responseTypeCaptor.capture());

        org.onap.vid.changeManagement.RequestDetails expectedRequest = changeManagementRequest.getRequestDetails().get(0);

        String serviceInstanceId = expectedRequest.getRelatedInstList().get(0).getRelatedInstance().instanceId;
        ;
        String vnfInstanceId = expectedRequest.getVnfInstanceId();
        String regEx = String.format("/serviceInstances/v[0-9]+/%s/vnfs/%s/inPlaceSoftwareUpdate", serviceInstanceId, vnfInstanceId);
        assertThat(endpointCaptor.getValue(), RegExMatcher.matchesRegEx(regEx));
        assertThat(requestCaptor.getValue(), instanceOf(RequestDetails.class));
        RequestDetails actualRequest = ((RequestDetails) requestCaptor.getValue().requestDetails);

        assertThat(actualRequest.getCloudConfiguration().getTenantId(), equalTo(expectedRequest.getCloudConfiguration().getTenantId()));
        assertThat(actualRequest.getCloudConfiguration().getLcpCloudRegionId(), equalTo(expectedRequest.getCloudConfiguration().getLcpCloudRegionId()));
        assertThat(actualRequest.getRequestInfo(), equalTo(expectedRequest.getRequestInfo()));
        assertThat(actualRequest.getRequestParameters(), equalTo(expectedRequest.getRequestParameters()));

        URL expectedMsoRequestUrl = this.getClass().getResource("/services/change_management_software_update_expected_mso_request.json");
        String expectedMsoRequestString = IOUtils.toString(expectedMsoRequestUrl, "UTF-8");
        String actualRequestString = objectMapper.writeValueAsString(actualRequest);
        try {
            JSONAssert.assertEquals("built mso request is not ok", expectedMsoRequestString, actualRequestString, JSONCompareMode.NON_EXTENSIBLE);
        } catch (AssertionError | Exception e) {
            System.out.println("requestDetailsAsString: \n" + actualRequestString);
            System.out.println("expected: \n" + expectedMsoRequestString);
            throw e;
        }

    }

    @Configuration
    public static class TestMsoConfig extends MsoConfig {

        @Override
        public MsoRestClientNew getMsoClient() {
            MsoRestClientNew spyClient = spy(new MsoRestClientNew(new SyncRestClient(), ""));
            return spyClient;
        }

        @Bean
        public ChangeManagementService getChangeManagementService(DataAccessService dataAccessService, MsoBusinessLogic msoInterface, SchedulerRestInterfaceIfc schedulerRestInterface) {
            return new ChangeManagementServiceImpl(dataAccessService, msoInterface, schedulerRestInterface);
        }
    }
}
