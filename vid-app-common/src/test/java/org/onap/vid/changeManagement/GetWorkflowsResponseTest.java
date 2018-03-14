package org.onap.vid.changeManagement;

import java.util.List;

import org.junit.Test;

public class GetWorkflowsResponseTest {

    private GetWorkflowsResponse createTestSubject() {
        return new GetWorkflowsResponse();
    }

    @Test
    public void testGetWorkflows() throws Exception {
        GetWorkflowsResponse testSubject;
        List<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflows();
    }

    @Test
    public void testSetWorkflows() throws Exception {
        GetWorkflowsResponse testSubject;
        List<String> workflows = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflows(workflows);
    }
}