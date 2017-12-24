package org.opencomp.vid.api;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StringEscapeUtils;
import org.opencomp.vid.api.simulator.SimulatorApi;
import org.opencomp.vid.api.simulator.SimulatorApi.RegistrationStrategy;
import org.openecomp.vid.aai.AaiResponse;
import org.openecomp.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AaiApiTest extends BaseApiAaiTest {


    public static final String GET_OPERATIONAL_ENVIRONMENTS_JSON = "get_operational_environments_aai.json";
    public static final String GET_OPERATIONAL_ENVIRONMENTS_JSON_ERROR = "get_operational_environments_aai_error.json";
    public static final String [] AAI_GET_SERVICES_ERROR_SIMULATOR_RESPONSES = {"getServicesAaiErrorResp.json", "aai_get_full_subscribers.json"};
    public static final String [] AAI_GET_SERVICES_FINE_SIMULATOR_RESPONSES = {"getServicesAaiFineResp.json", "aai_get_full_subscribers.json"};

    public static final String OPERATIONAL_ENVIRONMENT_TYPE = "VNF";
    public static final String OPERATIONAL_ENVIRONMENT_STATUS = "Activate";
    public static final String BASE_GET_SERVICES_AAI_REQUEST_BODY = "{\"start\" : \"service-design-and-creation/models\", \"query\" : \"query/servceModelsbyDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\";}";
    public static final String GET_AAI_SERVIES_EXPECTED_RESULT = "{\"services\":[{\"uuid\":\"20c4431c-246d-11e7-93ae-92361f002671\",\"invariantUUID\":\"78ca26d0-246d-11e7-93ae-92361f002671\",\"name\":\"vSAMP10aDEV::base::module-0\",\"version\":\"2\",\"toscaModelURL\":null,\"category\":null,\"lifecycleState\":null,\"lastUpdaterUserId\":null,\"lastUpdaterFullName\":null,\"distributionStatus\":null,\"artifacts\":null,\"resources\":null},{\"uuid\":\"797a6c41-0f80-4d35-a288-3920c4e06baa\",\"invariantUUID\":\"5b607929-6088-4614-97ef-cac817508e0e\",\"name\":\"CONTRAIL30_L2NODHCP\",\"version\":\"1.0\",\"toscaModelURL\":null,\"category\":null,\"lifecycleState\":null,\"lastUpdaterUserId\":null,\"lastUpdaterFullName\":null,\"distributionStatus\":null,\"artifacts\":null,\"resources\":null},{\"uuid\":\"f1bde010-cc5f-4765-941f-75f15b24f9fc\",\"invariantUUID\":\"0143d57b-a517-4de9-a0a1-eb76db51f402\",\"name\":\"BkVmxAv061917..base_vPE_AV..module-0\",\"version\":\"2\",\"toscaModelURL\":null,\"category\":null,\"lifecycleState\":null,\"lastUpdaterUserId\":null,\"lastUpdaterFullName\":null,\"distributionStatus\":null,\"artifacts\":null,\"resources\":null},{\"uuid\":\"ipe-resource-id-ps-02\",\"invariantUUID\":\"ipe-resource-id-ps-02\",\"name\":\"abc\",\"version\":\"v1.0\",\"toscaModelURL\":null,\"category\":null,\"lifecycleState\":null,\"lastUpdaterUserId\":null,\"lastUpdaterFullName\":null,\"distributionStatus\":null,\"artifacts\":null,\"resources\":null},{\"uuid\":\"lmoser410-connector-model-version-id\",\"invariantUUID\":\"lmoser410-connector-model-id\",\"name\":\"connector\",\"version\":\"v1.0\",\"toscaModelURL\":null,\"category\":null,\"lifecycleState\":null,\"lastUpdaterUserId\":null,\"lastUpdaterFullName\":null,\"distributionStatus\":null,\"artifacts\":null,\"resources\":null},{\"uuid\":\"ff2ae348-214a-11e7-93ae-92361f002673\",\"invariantUUID\":\"3a97db99-c4bb-498a-a13a-38f65f1ced3d\",\"name\":\"vSAMP10aDEV::base::module-0\",\"version\":\"1.0\",\"toscaModelURL\":null,\"category\":null,\"lifecycleState\":null,\"lastUpdaterUserId\":null,\"lastUpdaterFullName\":null,\"distributionStatus\":null,\"artifacts\":null,\"resources\":null},{\"uuid\":\"204c641a-3494-48c8-979a-86856f5fd32a\",\"invariantUUID\":\"3c504d40-b847-424c-9d25-4fb7e0a3e994\",\"name\":\"named-query-element\",\"version\":\"1.0\",\"toscaModelURL\":null,\"category\":null,\"lifecycleState\":null,\"lastUpdaterUserId\":null,\"lastUpdaterFullName\":null,\"distributionStatus\":null,\"artifacts\":null,\"resources\":null},{\"uuid\":\"acba1f72-c6e0-477f-9426-ad190151e100\",\"invariantUUID\":\"93e56950-cb19-44e6-ace4-8b50f2d02e45\",\"name\":\"RG_6-19_Test\",\"version\":\"1.0\",\"toscaModelURL\":null,\"category\":null,\"lifecycleState\":null,\"lastUpdaterUserId\":null,\"lastUpdaterFullName\":null,\"distributionStatus\":null,\"artifacts\":null,\"resources\":null}],\"readOnly\":false}";

    private String getGetOperationEnvironmentsUri() {
        return uri.toASCIIString() + "/get_operational_environments";
    }


    private String getAaiServicesUri() {
        return uri.toASCIIString() +"/rest/models/services";
    }

    private String getGetOperationEnvironmentUriWithParameters(){
        String url = getGetOperationEnvironmentsUri();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("operationalEnvironmentStatus", OPERATIONAL_ENVIRONMENT_STATUS)
                .queryParam("operationalEnvironmentType", OPERATIONAL_ENVIRONMENT_TYPE);

        String urlWithParameters = builder.toUriString();
        return urlWithParameters;

    }

    private AaiResponse<OperationalEnvironmentList> loginAndDoGetWithUrl(String url){
        HttpHeaders headers = getCookieAndJsonHttpHeaders();
        ResponseEntity<AaiResponse<OperationalEnvironmentList>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                new ParameterizedTypeReference<AaiResponse<OperationalEnvironmentList>>() {});

        AaiResponse<OperationalEnvironmentList> response = responseEntity.getBody();
        return response;
    }

    @Test
    public void testErrorGetOperationalEnvironments() throws Exception{
        //Register required response
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENTS_JSON_ERROR, RegistrationStrategy.APPEND);
        String url = getGetOperationEnvironmentsUri();
        AaiResponse<OperationalEnvironmentList> response = loginAndDoGetWithUrl(url);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getHttpCode());
        assertEquals("simulated error text", response.getErrorMessage());


    }

    //This test requires a simulator which runs on VID
    @Test
    public void testSuccessGetOperationalEnvironments() {
        //Register required response
        String uuidOfOperationalEnvironment = "f07ca256-96dd-40ad-b4d2-7a77e2a974ed";
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENTS_JSON, ImmutableMap.of("UUID_of_Operational_Environment", uuidOfOperationalEnvironment), RegistrationStrategy.APPEND);
        String url = getGetOperationEnvironmentUriWithParameters();
        AaiResponse<OperationalEnvironmentList> response = loginAndDoGetWithUrl(url);
        assertEquals(HttpStatus.OK.value(), response.getHttpCode());
        OperationalEnvironmentList list = response.getT();
        assertNotEquals(null,list.getOperationalEnvironment());
        assertEquals(2,list.getOperationalEnvironment().size());
        assertEquals(uuidOfOperationalEnvironment,list.getOperationalEnvironment().get(0).getOperationalEnvironmentId());
        assertEquals(1,list.getOperationalEnvironment().get(0).getRelationshipList().getRelationship().size());
    }


    @Test(dataProvider = "errorCodes")
    public void getServicesWitErrorResonse(int errorCode) throws IOException, URISyntaxException {
        final String expectedResult ="{\"services\":[],\"readOnly\":false}";

        callAaiWithSimulatedErrorResponse(AAI_GET_SERVICES_ERROR_SIMULATOR_RESPONSES,
                ImmutableMap.of("500", Integer.toString(errorCode),"ERROR_PAYLOAD", StringEscapeUtils.escapeJson(expectedResult)),
                getAaiServicesUri(), "",200,expectedResult,HttpMethod.GET);

    }

    @Test
    public void getServicesFineRequest() throws IOException, URISyntaxException {

        callAaiWithSimulatedErrorResponse(AAI_GET_SERVICES_FINE_SIMULATOR_RESPONSES,
                ImmutableMap.of(),
                getAaiServicesUri(), "",200,GET_AAI_SERVIES_EXPECTED_RESULT,HttpMethod.GET);

    }
    @DataProvider
    public static Object[][] errorCodes(Method test) {
        return new Object[][]{
                {500},{505}, {400}, {401}, {405}
        };
    }

}
