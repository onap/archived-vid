package org.onap.vid.mso.rest;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class RequestListTest {

    private RequestList createTestSubject() {
        return new RequestList();
    }

    @Test
    public void testGetRequestList() throws Exception {
        RequestList testSubject;
        List<RequestWrapper> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestList();
    }

    @Test
    public void testSetRequestList() throws Exception {
        RequestList testSubject;
        List<RequestWrapper> l = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestList(l);
    }

    @Test
    public void testToString() throws Exception {
        RequestList testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        RequestList testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        RequestList testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }

    @Test
    public void testHashCode() throws Exception {
        RequestList testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.hashCode();
    }

    @Test
    public void testEquals() throws Exception {
        RequestList testSubject;
        Object other = null;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.equals(other);
    }
}