package org.onap.vid.changeManagement;

import java.util.List;

import org.junit.Test;

public class VnfWorkflowRelationAllResponseTest {

    private VnfWorkflowRelationAllResponse createTestSubject() {
        return new VnfWorkflowRelationAllResponse();
    }

    @Test
    public void testGetVnfs() throws Exception {
        VnfWorkflowRelationAllResponse testSubject;
        List<VnfDetailsWithWorkflows> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfs();
    }

    @Test
    public void testSetVnfs() throws Exception {
        VnfWorkflowRelationAllResponse testSubject;
        List<VnfDetailsWithWorkflows> vnfs = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfs(vnfs);
    }
}