package org.onap.vid.changeManagement;

import java.util.List;

import org.junit.Test;

public class VnfWorkflowRelationRequestTest {

    private VnfWorkflowRelationRequest createTestSubject() {
        return new VnfWorkflowRelationRequest();
    }

    @Test
    public void testGetWorkflowsDetails() throws Exception {
        VnfWorkflowRelationRequest testSubject;
        List<WorkflowsDetail> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflowsDetails();
    }

    @Test
    public void testSetWorkflowsDetails() throws Exception {
        VnfWorkflowRelationRequest testSubject;
        List<WorkflowsDetail> workflowsDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflowsDetails(workflowsDetails);
    }
}