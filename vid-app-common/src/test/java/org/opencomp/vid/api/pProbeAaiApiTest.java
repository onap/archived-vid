package org.opencomp.vid.api;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.opencomp.vid.api.simulator.SimulatorApi.RegistrationStrategy;
import static org.opencomp.vid.api.simulator.SimulatorApi.registerExpectation;


public class pProbeAaiApiTest extends AaiApiTest {

    private static final String GET_SERVICE_INSTANCE_NOT_FOUND_JSON = "get_service_instance_not_found.json";
    private static final String GET_SERVICE_INSTANCE_JSON = "get_service_instance.json";
    private static final String GET_LOGICAL_LINK_JSON = "get_logical_link.json";
    private static final String GET_LOGICAL_LINK_NOT_FOUND_JSON = "get_logical_link_not_found.json";
    private static final String [] GET_SPECIFIC_PNF_FINE_RESPONSE = {"aai_get_specific_pnf.json"};
    private static final String [] GET_SPECIFIC_PNF_ERROR_RESPONSE = {"aai_get_specific_pnf_error.json"};

    //URIs
    private static final String GET_SERVICE_INSTANCE_PNFS = "aai_get_service_instance_pnfs/31739f3e-526b-11e6-beb8-9e71128cae77/AIM%20Transport/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    private static final String GET_SPECIFIC_PNF_URI = "aai_get_pnfs/pnf/DEAAI78";


    //Expected strings
    private static final String GET_SPECIFIC_PNF_EXPECTED = "{\"resourceVersion\":\"1494001797554\",\"relationshipList\":{\"relationship\":[{\"relatedTo\":\"complex\",\"relatedLink\":\"/aai/v11/cloud-infrastructure/complexes/complex/NAMEAAI2\",\"relationshipData\":[{\"relationshipKey\":\"complex.physical-location-id\",\"relationshipValue\":\"NAMEAAI2\"}],\"relatedToProperty\":null,\"relationDataList\":[{\"relationshipKey\":\"complex.physical-location-id\",\"relationshipValue\":\"NAMEAAI2\"}],\"relatedToPropertyList\":null}]},\"pnfName\":\"DEAAI78\",\"pnfName2\":\"DEAAI78-name-2\",\"pnfName2Source\":\"DEAAI78-name-2-source\",\"pnfId\":\"DEAAI78-id\",\"equipType\":\"Switch\",\"equipVendor\":\"Cisco\",\"equipModel\":\"ASR1002-X\"}";

    @Test
    public void testGetAssociatedPnfs() throws Exception {
        registerExpectation(GET_SERVICE_INSTANCE_JSON, RegistrationStrategy.CLEAR_THEN_SET);
        registerExpectation(GET_LOGICAL_LINK_JSON, RegistrationStrategy.APPEND);
        ResponseEntity<ArrayList> response = restTemplate.getForEntity(buildUri(GET_SERVICE_INSTANCE_PNFS), ArrayList.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        ArrayList pnfs = response.getBody();
        Assert.assertNotNull(pnfs);
        Assert.assertEquals(pnfs.size(), 2);
        Assert.assertEquals(pnfs.get(0), "tesaaisdgraclz1a1");
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
}
