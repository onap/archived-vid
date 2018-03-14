package org.onap.vid.services;

import static org.junit.Assert.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.GetVnfWorkflowRelationRequest;
import org.onap.vid.changeManagement.RequestDetails;
import org.onap.vid.changeManagement.VnfWorkflowRelationAllResponse;
import org.onap.vid.changeManagement.VnfWorkflowRelationRequest;
import org.onap.vid.changeManagement.VnfWorkflowRelationResponse;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.onap.vid.mso.rest.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class ChangeManagementServiceImplTest {

    private ChangeManagementServiceImpl createTestSubject() {
        return new ChangeManagementServiceImpl(new DataAccessServiceImpl(), new MsoBusinessLogicImpl(new MsoRestClientNew()));
    }

    @Test
    public void testGetMSOChangeManagements() throws Exception {
        ChangeManagementServiceImpl testSubject;
        Collection<Request> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getMSOChangeManagements();
    }

    @Test
    public void testDoChangeManagement() throws Exception {
        ChangeManagementServiceImpl testSubject;
        ChangeManagementRequest request = null;
        String vnfName = "";
        ResponseEntity<String> result;

        // test 1
        testSubject = createTestSubject();
        request = null;
        result = testSubject.doChangeManagement(request, vnfName);
        Assert.assertEquals(null, result);
    }

    
    @Test
    public void testGetSchedulerChangeManagements() throws Exception {
        ChangeManagementServiceImpl testSubject;
        JSONArray result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSchedulerChangeManagements();
    }

    @Test
    public void testDeleteSchedule() throws Exception {
        ChangeManagementServiceImpl testSubject;
        String scheduleId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.deleteSchedule(scheduleId);
    }



    /*
     * TODO: fix private ChangeManagementServiceImpl createTestSubject() {
     * return new ChangeManagementServiceImpl(); }
     */

    /*
     * @Test public void testGetMSOChangeManagements() throws Exception {
     * ChangeManagementServiceImpl testSubject; Collection<Request> result;
     * 
     * // default test testSubject = createTestSubject(); result =
     * testSubject.getMSOChangeManagements(); }
     * 
     * 
     * @Test public void testFindRequestByVnfName() throws Exception {
     * ChangeManagementServiceImpl testSubject;List<RequestDetails> requests =
     * null; String vnfName = ""; RequestDetails result;
     * 
     * // test 1 testSubject=createTestSubject();requests = null;
     * result=Deencapsulation.invoke(testSubject, "findRequestByVnfName", new
     * Object[]{List<RequestDetails>.class, vnfName}); Assert.assertEquals(null,
     * result); }
     */
    /*
     * 
     * @Test public void testDoChangeManagement() throws Exception {
     * ChangeManagementServiceImpl testSubject; ChangeManagementRequest request
     * = null; String vnfName = ""; ResponseEntity<String> result;
     * 
     * // test 1 testSubject = createTestSubject(); request = null; result =
     * testSubject.doChangeManagement(request, vnfName);
     * Assert.assertEquals(null, result); }
     * 
     * 
     * @Test public void testGetSchedulerChangeManagements() throws Exception {
     * ChangeManagementServiceImpl testSubject; JSONArray result;
     * 
     * // default test testSubject = createTestSubject(); result =
     * testSubject.getSchedulerChangeManagements(); }
     */
}