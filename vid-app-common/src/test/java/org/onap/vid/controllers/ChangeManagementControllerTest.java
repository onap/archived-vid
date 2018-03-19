package org.onap.vid.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.junit.Test;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.GetVnfWorkflowRelationRequest;
import org.onap.vid.changeManagement.VnfWorkflowRelationRequest;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.services.ChangeManagementServiceImpl;
import org.onap.vid.services.WorkflowService;
import org.onap.vid.services.WorkflowServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class ChangeManagementControllerTest {

    private ChangeManagementController createTestSubject() {
        return new ChangeManagementController(new WorkflowServiceImpl(), new ChangeManagementServiceImpl(null, null),
                null);
    }

    @Test
    public void testGetWorkflow() throws Exception {
        ChangeManagementController testSubject;
        Collection<String> vnfs = null;
        ResponseEntity<Collection<String>> result;

        // default test
        testSubject = createTestSubject();
        try {
            result = testSubject.getWorkflow(vnfs);
        } catch (

        Exception e) {
        }
    }

    @Test
    public void testGetMSOChangeManagements() throws Exception {
        ChangeManagementController testSubject;

        // default test
        testSubject = createTestSubject();
        try {
            testSubject.getMSOChangeManagements();
        } catch (

        Exception e) {
        }
    }

    @Test
    public void testChangeManagement() throws Exception {
        ChangeManagementController testSubject;
        String vnfName = "";
        HttpServletRequest request = null;
        ChangeManagementRequest changeManagmentRequest = null;
        ResponseEntity<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.changeManagement(vnfName, request, changeManagmentRequest);
    }

    @Test
    public void testUploadConfigUpdateFile() throws Exception {
        ChangeManagementController testSubject;
        MultipartFile file = null;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.uploadConfigUpdateFile(file);
    }

    @Test
    public void testGetSchedulerChangeManagements() throws Exception {
        ChangeManagementController testSubject;
        ResponseEntity<JSONArray> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSchedulerChangeManagements();
    }

    @Test
    public void testDeleteSchedule() throws Exception {
        ChangeManagementController testSubject;
        String scheduleId = "";
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.deleteSchedule(scheduleId);
    }

    @Test
    public void testGetWorkflows() throws Exception {
        ChangeManagementController testSubject;
        GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest = null;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflows(getVnfWorkflowRelationRequest);
    }

    @Test
    public void testCreateWorkflowRelation() throws Exception {
        ChangeManagementController testSubject;
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = null;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.createWorkflowRelation(vnfWorkflowRelationRequest);
    }

    @Test
    public void testGetAllWorkflowRelation() throws Exception {
        ChangeManagementController testSubject;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAllWorkflowRelation();
    }

    @Test
    public void testDeleteWorkflowRelation() throws Exception {
        ChangeManagementController testSubject;
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = null;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.deleteWorkflowRelation(vnfWorkflowRelationRequest);
    }

    @Test
    public void testClientDerivedExceptionAsBadRequest() throws Exception {
        ChangeManagementController testSubject;
        Exception e = null;
        MsoResponseWrapperInterface result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.clientDerivedExceptionAsBadRequest(e);
        } catch (

        Exception ex) {
        }
    }
}