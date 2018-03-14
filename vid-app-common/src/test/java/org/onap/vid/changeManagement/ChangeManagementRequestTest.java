package org.onap.vid.changeManagement;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ChangeManagementRequestTest {

    private ChangeManagementRequest createTestSubject() {
        return new ChangeManagementRequest();
    }

    @Test
    public void testGetRequestDetails() throws Exception {
        ChangeManagementRequest testSubject;
        List<RequestDetails> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestDetails();
    }

    @Test
    public void testSetRequestDetails() throws Exception {
        ChangeManagementRequest testSubject;
        List<RequestDetails> requestDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestDetails(requestDetails);
    }

    @Test
    public void testGetRequestType() throws Exception {
        ChangeManagementRequest testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestType();
    }

    @Test
    public void testSetRequestType() throws Exception {
        ChangeManagementRequest testSubject;
        String requestType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestType(requestType);
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        ChangeManagementRequest testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        ChangeManagementRequest testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }
}