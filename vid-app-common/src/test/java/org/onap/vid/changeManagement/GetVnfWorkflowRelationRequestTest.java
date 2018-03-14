package org.onap.vid.changeManagement;

import java.util.List;

import org.junit.Test;

public class GetVnfWorkflowRelationRequestTest {

    private GetVnfWorkflowRelationRequest createTestSubject() {
        return new GetVnfWorkflowRelationRequest();
    }

    @Test
    public void testGetVnfDetails() throws Exception {
        GetVnfWorkflowRelationRequest testSubject;
        List<VnfDetails> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfDetails();
    }

    @Test
    public void testSetVnfDetails() throws Exception {
        GetVnfWorkflowRelationRequest testSubject;
        List<VnfDetails> vnfDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfDetails(vnfDetails);
    }
}