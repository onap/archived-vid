package org.opencomp.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.controller.MsoConfig;
import org.openecomp.vid.controller.MsoController;
import org.openecomp.vid.domain.mso.RequestInfo;
import org.openecomp.vid.factories.MsoRequestFactory;
import org.openecomp.vid.mso.rest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SystemProperties.class , MsoConfig.class})
public class MsoControllerTest {

    @Autowired
    MsoRequestFactory msoRequestFactory;

    @Test
    public void testInstanceCreationNew() throws Exception {

        RequestDetails requestDetails = msoRequestFactory.createMsoRequest("msoRequest.json");
        MsoController msoController = new MsoController();
        ResponseEntity<String> responseEntityNew = msoController.createSvcInstanceNew(null, requestDetails);
        ResponseEntity<String> responseEntity = msoController.createSvcInstance(null, requestDetails);
        assertEquals(responseEntityNew, responseEntity);

    }

    @Test
    public void testInstanceCreationLocalWithRest() throws Exception {

        RequestDetails requestDetails = msoRequestFactory.createMsoRequest("msoRequest.json");
        MsoController msoController = new MsoController();
        ResponseEntity<String> responseEntityNew = msoController.createSvcInstance(null, requestDetails);
        ResponseEntity<String> responseEntityRest = msoController.createSvcInstanceNewRest(null, requestDetails);

        assertEquals(responseEntityNew.getBody(), responseEntityRest.getBody());

    }

    @Test
    public void testInstanceCreation() throws Exception {

        RequestDetails requestDetails = msoRequestFactory.createMsoRequest("msoRequest.json");
        MsoController msoController = new MsoController();
        ResponseEntity<String> responseEntity = msoController.createSvcInstance(null, requestDetails);


        assertEquals(responseEntity.getBody(), "{ \"status\": 200, \"entity\": {\n" +
                "  \"requestReferences\": {\n" +
                "    \"instanceId\": \"ba00de9b-3c3e-4b0a-a1ad-0c5489e711fb\",\n" +
                "    \"requestId\": \"311cc766-b673-4a50-b9c5-471f68914586\"\n" +
                "  }\n" +
                "}}");

    }

    @Test
    public void testGetOrchestrationRequestsForDashboard() throws Exception{
        MsoController msoController = new MsoController();
        List<Request> orchestrationRequestsForDashboard = msoController.getOrchestrationRequestsForDashboard();

        assertEquals(orchestrationRequestsForDashboard.size() , 2);
    }

    @Test
    public void testGetManualTasksByRequestId() throws Exception{
        MsoController msoController = new MsoController();
        List<Task> orchestrationRequestsForDashboard = msoController.getManualTasksByRequestId("za1234d1-5a33-55df-13ab-12abad84e335");

        assertEquals(orchestrationRequestsForDashboard.get(0).getTaskId() , "daf4dd84-b77a-42da-a051-3239b7a9392c");
    }


    public void testCompleteManualTask() throws Exception{ // TODO not done yet
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setResponseValue("rollback");
        requestInfo.setRequestorId("abc");
        requestInfo.setSource("VID");
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setRequestInfo(requestInfo);
        MsoController msoController = new MsoController();
        ResponseEntity<String> responseEntity = msoController.manualTaskComplete("daf4dd84-b77a-42da-a051-3239b7a9392c" , requestDetails);
        String assertString = "{ \\\"status\\\": 200, \\\"entity\\\": {\\n\" +\n" +
                "                \"  \\\"taskRequestReference\\\": {\\n\" +\n" +
                "                \"     \\\"taskId\\\": \\\"daf4dd84-b77a-42da-a051-3239b7a9392c\\\"\\n\" +\n" +
                "                \"      }\\n\" +\n" +
                "                \"}\\n\" +\n" +
                "                \"}";
        assertEquals(responseEntity.getBody() , StringEscapeUtils.unescapeJava(assertString));
    }





}
