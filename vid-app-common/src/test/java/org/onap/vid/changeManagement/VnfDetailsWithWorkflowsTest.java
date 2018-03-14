package org.onap.vid.changeManagement;

import java.util.List;

import org.junit.Test;

public class VnfDetailsWithWorkflowsTest {

    private VnfDetailsWithWorkflows createTestSubject() {
        return new VnfDetailsWithWorkflows();
    }

    @Test
    public void testGetWorkflows() throws Exception {
        VnfDetailsWithWorkflows testSubject;
        List<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflows();
    }

    @Test
    public void testSetWorkflows() throws Exception {
        VnfDetailsWithWorkflows testSubject;
        List<String> workflows = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflows(workflows);
    }
}