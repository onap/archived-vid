package org.onap.vid.services;

import java.util.*;

import org.junit.Test;

public class WorkflowServiceImplTest {

    private WorkflowServiceImpl createTestSubject() {
        return new WorkflowServiceImpl();
    }

    @Test
    public void testGetWorkflowsForVNFs() throws Exception {
        WorkflowServiceImpl testSubject;
        Collection<String> vnfNames = new ArrayList<String>();
        Collection<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflowsForVNFs(vnfNames);
    }

    @Test
    public void testGetAllWorkflows() throws Exception {
        WorkflowServiceImpl testSubject;
        Collection<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAllWorkflows();
    }
}