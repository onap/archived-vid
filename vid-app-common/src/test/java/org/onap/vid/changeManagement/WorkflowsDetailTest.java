package org.onap.vid.changeManagement;

import org.junit.Test;

public class WorkflowsDetailTest {

    private WorkflowsDetail createTestSubject() {
        return new WorkflowsDetail();
    }

    @Test
    public void testGetVnfDetails() throws Exception {
        WorkflowsDetail testSubject;
        VnfDetails result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfDetails();
    }

    @Test
    public void testSetVnfDetails() throws Exception {
        WorkflowsDetail testSubject;
        VnfDetails vnfDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfDetails(vnfDetails);
    }

    @Test
    public void testGetWorkflowName() throws Exception {
        WorkflowsDetail testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflowName();
    }

    @Test
    public void testSetWorkflowName() throws Exception {
        WorkflowsDetail testSubject;
        String workflowName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflowName(workflowName);
    }

    @Test
    public void testToString() throws Exception {
        WorkflowsDetail testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}