package org.opencomp.vid.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.mockito.ArgumentCaptor;
import org.opencomp.vid.testUtils.RegExMatcher;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.changeManagement.ChangeManagementRequest;
import org.openecomp.vid.controller.MsoConfig;
import org.openecomp.vid.controller.WebConfig;
import org.openecomp.vid.model.RequestReferencesContainer;
import org.openecomp.vid.mso.RestObject;
import org.openecomp.vid.mso.rest.MsoRestClientNew;
import org.openecomp.vid.mso.rest.RequestDetails;
import org.openecomp.vid.properties.AsdcClientConfiguration;
import org.openecomp.vid.services.ChangeManagementService;
import org.openecomp.vid.services.ChangeManagementServiceImpl;
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
    private MsoRestClientNew restClientUnderTest;

   // @Test
    void testInPlaceSoftwareUpdateRequest() throws Exception {


        doReturn(new RestObject<RequestReferencesContainer>()).when(restClientUnderTest).PostForObject(anyObject(), anyString(), anyString(), anyObject());

        URL requestJsonUrl = this.getClass().getResource("/services/change_management_software_update_request.json");
        ChangeManagementRequest changeManagementRequest = objectMapper.readValue(requestJsonUrl, ChangeManagementRequest.class);
        changeManagementService.doChangeManagement(changeManagementRequest, "vidVnf");

        ArgumentCaptor<String> endpointCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sourceIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> requestCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Class> responseTypeCaptor = ArgumentCaptor.forClass(Class.class);
        verify(restClientUnderTest).PostForObject(requestCaptor.capture(), sourceIdCaptor.capture(), endpointCaptor.capture(), responseTypeCaptor.capture());

        org.openecomp.vid.changeManagement.RequestDetails expectedRequest = changeManagementRequest.getRequestDetails().get(0);

        String serviceInstanceId = expectedRequest.getRelatedInstList().get(0).getRelatedInstance().instanceId;
        ;
        String vnfInstanceId = expectedRequest.getVnfInstanceId();
        String regEx = String.format("/serviceInstances/v[0-9]+/%s/vnfs/%s/inPlaceSoftwareUpdate", serviceInstanceId, vnfInstanceId);
        assertThat(endpointCaptor.getValue(), RegExMatcher.matchesRegEx(regEx));

        assertThat(requestCaptor.getValue(), instanceOf(RequestDetails.class));
        RequestDetails actualRequest = ((RequestDetails) requestCaptor.getValue());

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
            MsoRestClientNew spyClient = spy(new MsoRestClientNew());
            return spyClient;
        }

        @Bean
        public ChangeManagementService getChangeManagementService() {
            return new ChangeManagementServiceImpl(null, getMsoBusinessLogic());
        }
    }
}
