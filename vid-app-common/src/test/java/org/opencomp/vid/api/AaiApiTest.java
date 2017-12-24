package org.opencomp.vid.api;

import com.google.common.collect.ImmutableMap;
import org.opencomp.vid.api.simulator.SimulatorApi;
import org.openecomp.vid.aai.AaiResponse;
import org.openecomp.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AaiApiTest extends BaseApiTest{


    public static final String GET_OPERATIONAL_ENVIRONMENTS_JSON = "get_operational_environments_aai.json";
    public static final String GET_OPERATIONAL_ENVIRONMENTS_JSON_ERROR = "get_operational_environments_aai_error.json";
    public static final String OPERATIONAL_ENVIRONMENT_TYPE = "VNF";
    public static final String OPERATIONAL_ENVIRONMENT_STATUS = "Activate";
    private final RestTemplate restTemplate = new RestTemplate();

    private String getGetOperationEnvironmentsUri() {
        return uri.toASCIIString() + "/get_operational_environments";
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
         SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENTS_JSON_ERROR);
        String url = getGetOperationEnvironmentsUri();
        AaiResponse<OperationalEnvironmentList> response = loginAndDoGetWithUrl(url);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),response.getHttpCode());
        assertEquals("simulated error text", response.getErrorMessage());


    }

    //This test requires a simulator which runs on VID
    @Test
    public void testSuccessGetOperationalEnvironments() throws Exception{
        //Register required response
        String uuidOfOperationalEnvironment = "f07ca256-96dd-40ad-b4d2-7a77e2a974ed";
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENTS_JSON, ImmutableMap.of("UUID_of_Operational_Environment", uuidOfOperationalEnvironment));
        String url = getGetOperationEnvironmentUriWithParameters();
        AaiResponse<OperationalEnvironmentList> response = loginAndDoGetWithUrl(url);
        assertEquals(HttpStatus.OK.value(), response.getHttpCode());
        OperationalEnvironmentList list = response.getT();
        assertNotEquals(null,list.getOperationalEnvironment());
        assertEquals(2,list.getOperationalEnvironment().size());
        assertEquals(uuidOfOperationalEnvironment,list.getOperationalEnvironment().get(0).getOperationalEnvironmentId());
        assertEquals(1,list.getOperationalEnvironment().get(0).getRelationshipList().size());
    }


}
