package org.onap.vid.mso.rest;

import org.junit.Test;

public class RequestWrapperTest {

    private RequestWrapper createTestSubject() {
        return new RequestWrapper();
    }

    @Test
    public void testGetRequest() throws Exception {
        RequestWrapper testSubject;
        Request result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequest();
    }

    @Test
    public void testSetRequest() throws Exception {
        RequestWrapper testSubject;
        Request request = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequest(request);
    }
}