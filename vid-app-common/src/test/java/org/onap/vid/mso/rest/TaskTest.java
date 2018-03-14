package org.onap.vid.mso.rest;

import java.util.List;

import org.junit.Test;

public class TaskTest {

    private Task createTestSubject() {
        return new Task();
    }

    @Test
    public void testGetTaskId() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getTaskId();
    }

    @Test
    public void testSetTaskId() throws Exception {
        Task testSubject;
        String taskId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setTaskId(taskId);
    }

    @Test
    public void testGetType() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getType();
    }

    @Test
    public void testSetType() throws Exception {
        Task testSubject;
        String type = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setType(type);
    }

    @Test
    public void testGetNfRole() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getNfRole();
    }

    @Test
    public void testSetNfRole() throws Exception {
        Task testSubject;
        String nfRole = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setNfRole(nfRole);
    }

    @Test
    public void testGetSubscriptionServiceType() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSubscriptionServiceType();
    }

    @Test
    public void testSetSubscriptionServiceType() throws Exception {
        Task testSubject;
        String subscriptionServiceType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setSubscriptionServiceType(subscriptionServiceType);
    }

    @Test
    public void testGetOriginalRequestId() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOriginalRequestId();
    }

    @Test
    public void testSetOriginalRequestId() throws Exception {
        Task testSubject;
        String originalRequestId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setOriginalRequestId(originalRequestId);
    }

    @Test
    public void testGetOriginalRequestorId() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOriginalRequestorId();
    }

    @Test
    public void testSetOriginalRequestorId() throws Exception {
        Task testSubject;
        String originalRequestorId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setOriginalRequestorId(originalRequestorId);
    }

    @Test
    public void testGetErrorSource() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getErrorSource();
    }

    @Test
    public void testSetErrorSource() throws Exception {
        Task testSubject;
        String errorSource = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setErrorSource(errorSource);
    }

    @Test
    public void testGetErrorCode() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getErrorCode();
    }

    @Test
    public void testSetErrorCode() throws Exception {
        Task testSubject;
        String errorCode = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setErrorCode(errorCode);
    }

    @Test
    public void testGetErrorMessage() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getErrorMessage();
    }

    @Test
    public void testSetErrorMessage() throws Exception {
        Task testSubject;
        String errorMessage = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setErrorMessage(errorMessage);
    }

    @Test
    public void testGetBuildingBlockName() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getBuildingBlockName();
    }

    @Test
    public void testSetBuildingBlockName() throws Exception {
        Task testSubject;
        String buildingBlockName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setBuildingBlockName(buildingBlockName);
    }

    @Test
    public void testGetBuildingBlockStep() throws Exception {
        Task testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getBuildingBlockStep();
    }

    @Test
    public void testSetBuildingBlockStep() throws Exception {
        Task testSubject;
        String buildingBlockStep = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setBuildingBlockStep(buildingBlockStep);
    }

    @Test
    public void testGetValidResponses() throws Exception {
        Task testSubject;
        List<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getValidResponses();
    }

    @Test
    public void testSetValidResponses() throws Exception {
        Task testSubject;
        List<String> validResponses = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setValidResponses(validResponses);
    }
}