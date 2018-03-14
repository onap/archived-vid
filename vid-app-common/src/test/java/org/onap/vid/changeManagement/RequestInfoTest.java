package org.onap.vid.changeManagement;

import java.util.Map;

import org.junit.Test;

public class RequestInfoTest {

    private RequestInfo createTestSubject() {
        return new RequestInfo();
    }

    @Test
    public void testGetSource() throws Exception {
        RequestInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSource();
    }

    @Test
    public void testSetSource() throws Exception {
        RequestInfo testSubject;
        String source = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setSource(source);
    }

    @Test
    public void testGetSuppressRollback() throws Exception {
        RequestInfo testSubject;
        Boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSuppressRollback();
    }

    @Test
    public void testSetSuppressRollback() throws Exception {
        RequestInfo testSubject;
        Boolean suppressRollback = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setSuppressRollback(suppressRollback);
    }

    @Test
    public void testGetRequestorId() throws Exception {
        RequestInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestorId();
    }

    @Test
    public void testSetRequestorId() throws Exception {
        RequestInfo testSubject;
        String requestorId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestorId(requestorId);
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        RequestInfo testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        RequestInfo testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }
}