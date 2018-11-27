package org.onap.vid.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.GetVnfWorkflowRelationRequest;
import org.onap.vid.changeManagement.VnfWorkflowRelationRequest;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.scheduler.RestObject;
import org.onap.vid.scheduler.SchedulerRestInterfaceIfc;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.services.ChangeManagementServiceImpl;
import org.onap.vid.services.WorkflowServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import java.util.Collection;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class ChangeManagementControllerTest {

    private ChangeManagementController createTestSubject() throws Exception {
        final WorkflowServiceImpl workflowService = mock(WorkflowServiceImpl.class);
        final DataAccessService dataAccessService = mock(DataAccessService.class);
        final MsoBusinessLogic msoBusinessLogic = mock(MsoBusinessLogic.class);
        final SchedulerRestInterfaceIfc schedulerRestInterface = mock(SchedulerRestInterfaceIfc.class);

        // These ones will suffice these tests, by mocking a very minimal AAI response:
        //   - testDeleteSchedule
        //   - testDeleteWorkflowRelation
        //   - testGetSchedulerChangeManagements
        //   - testCreateWorkflowRelation
        //   - testGetWorkflows
        doAnswer(pretend200OkWithValidJsonPayload()).when(schedulerRestInterface).Get(any(), any(), any(), any());
        doAnswer(pretend200OkWithValidJsonPayload()).when(schedulerRestInterface).Delete(any(), any(), any(), any());

        final ChangeManagementService changeManagementService = new ChangeManagementServiceImpl(dataAccessService, msoBusinessLogic, schedulerRestInterface);

        return new ChangeManagementController(workflowService, changeManagementService, new ObjectMapper());
    }

    private Answer pretend200OkWithValidJsonPayload() {
        return mockitoInvocation -> {
            final RestObject<String> restObject = mockitoInvocation.getArgument(3);
            restObject.setStatusCode(200);
            restObject.set("[]");
            return null;
        };
    }

    @Test
    public void testGetWorkflow() throws Exception {
        ChangeManagementController testSubject;
        Collection<String> vnfs = null;
        ResponseEntity<Collection<String>> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflow(vnfs);
    }

    @Test
    public void testGetMSOChangeManagements() throws Exception {
        ChangeManagementController testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.getMSOChangeManagements();
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
        GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest = new GetVnfWorkflowRelationRequest(emptyList());
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflows(getVnfWorkflowRelationRequest);
    }

    @Test
    public void testCreateWorkflowRelation() throws Exception {
        ChangeManagementController testSubject;
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(emptyList());
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
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(emptyList());
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.deleteWorkflowRelation(vnfWorkflowRelationRequest);
    }

    @Test
    public void testClientDerivedExceptionAsBadRequest() throws Exception {
        ChangeManagementController testSubject;
        Exception e = new BadRequestException();
        MsoResponseWrapperInterface result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.clientDerivedExceptionAsBadRequest(e);
    }
}