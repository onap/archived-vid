package org.onap.vid.api;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static vid.automation.test.services.SimulatorApi.RegistrationStrategy;
import static vid.automation.test.services.SimulatorApi.registerExpectation;


public class pProbeAaiApiTest extends BaseApiAaiTest {

    private static final String GET_SERVICE_INSTANCE_NOT_FOUND_JSON = "get_service_instance_not_found.json";
    private static final String GET_SERVICE_INSTANCE_JSON = "get_service_instance.json";
    private static final String GET_LOGICAL_LINK_JSON = "get_logical_link.json";
    private static final String GET_LOGICAL_LINK_NOT_FOUND_JSON = "get_logical_link_not_found.json";
    private static final String [] GET_SPECIFIC_PNF_FINE_RESPONSE = {"aai_get_specific_pnf.json"};
    private static final String [] GET_SPECIFIC_PNF_ERROR_RESPONSE = {"aai_get_specific_pnf_error.json"};
    private static final String [] GET_PNF_BY_REGION_RESPONSE = {"aai_get_pnf_by_region.json"};
    private static final String [] GET_PNF_BY_REGION_RESPONSE_EMPTY = {"aai_get_pnf_by_region_error.json"};

    //Request
    private static final String GET_PNF_BY_REGION_AAI_EXPECTED_RESPONSE = "registration_to_simulator/body_jsons/aai_response_get_pnf_by_region.json";


    //URIs
    private static final String GET_SERVICE_INSTANCE_PNFS = "aai_get_service_instance_pnfs/31739f3e-526b-11e6-beb8-9e71128cae77/AIM Transport/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    private static final String GET_SPECIFIC_PNF_URI = "aai_get_pnfs/pnf/DEAAI78";
    private static final String GET_PNF_BY_REGION = "aai_get_pnf_instances/e433710f-9217-458d-a79d-1c7aff376d89/VIRTUAL USP/8a84e59b-45fe-4851-8ff1-34225a0b32c3/83b458fd-5dd3-419b-a9e3-7335814a0911/AAIAIC25/Cisco/Nexus 3048-TP";



    //Expected strings
    private static final String GET_SPECIFIC_PNF_EXPECTED = "{\"resourceVersion\":\"1494001797554\",\"relationshipList\":{\"relationship\":[{\"relatedTo\":\"complex\",\"relatedLink\":\"/aai/v11/cloud-infrastructure/complexes/complex/NAMEAAI2\",\"relationshipLabel\": \"onap.pnf\",\"relationshipData\":[{\"relationshipKey\":\"complex.physical-location-id\",\"relationshipValue\":\"NAMEAAI2\"}],\"relatedToProperty\":null,\"relationDataList\":[{\"relationshipKey\":\"complex.physical-location-id\",\"relationshipValue\":\"NAMEAAI2\"}],\"relatedToPropertyList\":null}]},\"pnfName\":\"DEAAI78\",\"pnfName2\":\"DEAAI78-name-2\",\"pnfName2Source\":\"DEAAI78-name-2-source\",\"pnfId\":\"DEAAI78-id\",\"equipType\":\"Switch\",\"equipVendor\":\"Cisco\",\"equipModel\":\"ASR1002-X\"}";

    @DataProvider
    public static Object[][] getAssociatedPnfs(Method test) {
        return new Object[][]{
                {GET_SERVICE_INSTANCE_JSON, GET_LOGICAL_LINK_JSON},

                //no need to call to getLogicalLink if service has direct relation to the pnfs
                {"get_service_instance_direct_relation_pnf.json", null}
        };
    }

    @Test(dataProvider = "getAssociatedPnfs")
    public void testGetAssociatedPnfs(String getServiceInstanceJson, String getLogicalLinkJson) {
        registerExpectation(getServiceInstanceJson, RegistrationStrategy.APPEND);
        if (getLogicalLinkJson!=null) {
            registerExpectation(getLogicalLinkJson, RegistrationStrategy.APPEND);
        }
        ResponseEntity<ArrayList> response = restTemplate.getForEntity(buildUri(GET_SERVICE_INSTANCE_PNFS), ArrayList.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        ArrayList pnfs = response.getBody();
        Assert.assertNotNull(pnfs);
        Assert.assertEquals(pnfs.size(), 2);
        Assert.assertEquals(pnfs.get(0), "tesaaisdgraclz1a1");
        Assert.assertEquals(pnfs.get(1), "tesai371ve2");
    }

    @Test
    public void testGetAssociatedPnfsByRegisteredServiceResponse() throws Exception {
        registerExpectation(GET_SERVICE_INSTANCE_NOT_FOUND_JSON, RegistrationStrategy.CLEAR_THEN_SET);
        ResponseEntity<ArrayList> response = restTemplate.getForEntity(buildUri(GET_SERVICE_INSTANCE_PNFS), ArrayList.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        ArrayList pnfs = response.getBody();
        Assert.assertNotNull(pnfs);
        Assert.assertEquals(pnfs.size(), 0);
    }

    @Test
    public void testGetAssociatedPnfsByRegisteredLogicalLinkResponse() throws Exception {
        registerExpectation(GET_SERVICE_INSTANCE_JSON, RegistrationStrategy.CLEAR_THEN_SET);
        registerExpectation(GET_LOGICAL_LINK_NOT_FOUND_JSON, RegistrationStrategy.APPEND);
        ResponseEntity<ArrayList> response = restTemplate.getForEntity(buildUri(GET_SERVICE_INSTANCE_PNFS), ArrayList.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        ArrayList pnfs = response.getBody();
        Assert.assertNotNull(pnfs);
        Assert.assertEquals(pnfs.size(), 0);
    }

    @Test
    public void testGetSpecificPnf() throws Exception {
        callAaiWithSimulatedErrorResponse(GET_SPECIFIC_PNF_FINE_RESPONSE,
                ImmutableMap.of(),
                buildUri(GET_SPECIFIC_PNF_URI), "",200,GET_SPECIFIC_PNF_EXPECTED, HttpMethod.GET);

    }

    @Test(dataProvider = "errorCodes")
    public void testGetSpecificPnfError(int errorCode) throws IOException, URISyntaxException {
        final String expectedResult ="{ \"requestError\": { \"serviceException\": { \"messageId\": \"SVC3001\", \"text\": \"Resource not found for %1 using id %2 (msg=%3) (ec=%4)\", \"variables\": [ \"GET\", \"network/pnfs/pnf/DEAAI78ff\", \"Node Not Found:No Node of type pnf found at: network/pnfs/pnf/DEAAI78ff\", \"ERR.5.4.6114\" ] } } }";

        callAaiWithSimulatedErrorResponse(GET_SPECIFIC_PNF_ERROR_RESPONSE,
                ImmutableMap.of("500", Integer.toString(errorCode),"\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(expectedResult)),
                buildUri(GET_SPECIFIC_PNF_URI), "",errorCode,expectedResult,HttpMethod.GET);

    }

    @Test
    public void testGetPnfDataByRegion() throws Exception {
        String expected = "{\"results\":[{\"id\":\"901128280\",\"url\":\"/aai/v12/network/pnfs/pnf/AS-pnf2-10219--as988q\",\"properties\":{\"pnfName\":\"AS-pnf2-10219--as988q\",\"equipType\":\"Switch\",\"equipVendor\":\"Cisco\",\"equipModel\":\"Nexus3048-TP\",\"inMaint\":false,\"resourceVersion\":\"1508776538192\"},\"nodeType\":\"pnf\",\"relatedTo\":[{\"id\":\"532488360\",\"url\":\"/aai/v12/business/customers/customer/customer-10219-as988q/service-subscriptions/service-subscription/serviceSub2-test-10219-as988q/service-instances/service-instance/serviceIns2-test-10219-as988q\",\"nodeType\":\"service-instance\",\"relationshipLabel\":\"uses\"},{\"id\":\"860164248\",\"url\":\"/aai/v12/cloud-infrastructure/complexes/complex/complex-10219--as988q\",\"nodeType\":\"complex\",\"relationshipLabel\":\"locatedIn\"}]}],\"additionalProperties\":{}}";
        callAaiWithSimulatedErrorResponse(GET_PNF_BY_REGION_RESPONSE,
                ImmutableMap.of(),
                buildUri(GET_PNF_BY_REGION), "",200,expected, HttpMethod.GET);

    }

    @Test(dataProvider = "errorCodes")
    public void testGetPnfDataByRegionError(int errorCode) throws IOException, URISyntaxException {
        final String expectedResult ="{\"results\":[]}";

        callAaiWithSimulatedErrorResponse(GET_PNF_BY_REGION_RESPONSE_EMPTY,
                ImmutableMap.of("500", Integer.toString(errorCode),"\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(expectedResult)),
                buildUri(GET_PNF_BY_REGION), "",errorCode,expectedResult,HttpMethod.GET);

    }

    @DataProvider
    public static Object[][] errorCodes(Method test) {
        return new Object[][]{
                {500},{505}, {400}, {401}, {405}
        };
    }

    @Test
    public void testGetPnfDataByRegionNoResults() throws IOException, URISyntaxException {
        final String registratedResult = "{\"results\":[]}";
        final String expectedResult ="{\"results\":[],\"additionalProperties\":{}}";
        final int expectedResponseCode = 200;
        callAaiWithSimulatedErrorResponse(GET_PNF_BY_REGION_RESPONSE_EMPTY,
                ImmutableMap.of("500", Integer.toString(expectedResponseCode),"\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(registratedResult)),
                buildUri(GET_PNF_BY_REGION), "",expectedResponseCode,expectedResult,HttpMethod.GET);

    }
}
