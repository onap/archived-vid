package org.onap.vid.controller;

import org.apache.commons.lang.StringEscapeUtils;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.controllers.MsoConfig;
import org.onap.vid.controllers.MsoController;
import org.onap.vid.domain.mso.RequestInfo;
import org.onap.vid.factories.MsoRequestFactory;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.Assert.*;
import org.testng.annotations.Test;

import java.util.List;


@WebAppConfiguration
@ContextConfiguration(classes = {SystemProperties.class, MsoConfig.class})
public class MsoControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    MsoRequestFactory msoRequestFactory;

    @Test(enabled = false)
    public void testInstanceCreationNew() throws Exception {

        RequestDetails requestDetails = msoRequestFactory.createMsoRequest("msoRequest.json");
        MsoController msoController = new MsoController(null);
        //TODO: make ths test to really test something
        //ResponseEntity<String> responseEntityNew = msoController.createSvcInstanceNew(null, requestDetails);
        ResponseEntity<String> responseEntity = msoController.createSvcInstance(null, requestDetails);
        //Assert.assertEquals(responseEntityNew, responseEntity);

    }

    @Test(enabled = false)
    public void testInstanceCreationLocalWithRest() throws Exception {

        RequestDetails requestDetails = msoRequestFactory.createMsoRequest("msoRequest.json");
        MsoController msoController = new MsoController(null);
        ResponseEntity<String> responseEntityNew = msoController.createSvcInstance(null, requestDetails);
        //TODO: make ths test to really test something
//        ResponseEntity<String> responseEntityRest = msoController.createSvcInstanceNewRest(null, requestDetails);
//
//        Assert.assertEquals(responseEntityNew.getBody(), responseEntityRest.getBody());

    }

    @Test(enabled = false)
    public void testInstanceCreation() throws Exception {

        RequestDetails requestDetails = msoRequestFactory.createMsoRequest("msoRequest.json");
        MsoController msoController = new MsoController(null);
        ResponseEntity<String> responseEntity = msoController.createSvcInstance(null, requestDetails);


        Assert.assertEquals(responseEntity.getBody(), "{ \"status\": 200, \"entity\": {\n" +
                "  \"requestReferences\": {\n" +
                "    \"instanceId\": \"ba00de9b-3c3e-4b0a-a1ad-0c5489e711fb\",\n" +
                "    \"requestId\": \"311cc766-b673-4a50-b9c5-471f68914586\"\n" +
                "  }\n" +
                "}}");

    }

    @Test(enabled = false)
    public void testGetOrchestrationRequestsForDashboard() throws Exception {
        MsoController msoController = new MsoController(null);
        List<Request> orchestrationRequestsForDashboard = msoController.getOrchestrationRequestsForDashboard();

        Assert.assertEquals(orchestrationRequestsForDashboard.size(), 2);
    }

    @Test(enabled = false)
    public void testGetManualTasksByRequestId() throws Exception {
        MsoController msoController = new MsoController(null);
        List<Task> orchestrationRequestsForDashboard = msoController.getManualTasksByRequestId("za1234d1-5a33-55df-13ab-12abad84e335");

        Assert. assertEquals(orchestrationRequestsForDashboard.get(0).getTaskId(), "daf4dd84-b77a-42da-a051-3239b7a9392c");
    }


    public void testCompleteManualTask() throws Exception { // TODO not done yet
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setResponseValue("rollback");
        requestInfo.setRequestorId("abc");
        requestInfo.setSource("VID");
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setRequestInfo(requestInfo);
        MsoController msoController = new MsoController(null);
        ResponseEntity<String> responseEntity = msoController.manualTaskComplete("daf4dd84-b77a-42da-a051-3239b7a9392c", requestDetails);
        String assertString = "{ \\\"status\\\": 200, \\\"entity\\\": {\\n\" +\n" +
                "                \"  \\\"taskRequestReference\\\": {\\n\" +\n" +
                "                \"     \\\"taskId\\\": \\\"daf4dd84-b77a-42da-a051-3239b7a9392c\\\"\\n\" +\n" +
                "                \"      }\\n\" +\n" +
                "                \"}\\n\" +\n" +
                "                \"}";
        Assert.assertEquals(responseEntity.getBody(), StringEscapeUtils.unescapeJava(assertString));
    }


}
