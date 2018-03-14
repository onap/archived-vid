package org.onap.vid.mso.rest;

import org.junit.Test;

public class ResponseTest {

    private Response createTestSubject() {
        return new Response();
    }

    @Test
    public void testGetStatus() throws Exception {
        Response testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getStatus();
    }

    @Test
    public void testSetStatus() throws Exception {
        Response testSubject;
        int status = 0;

        // default test
        testSubject = createTestSubject();
        testSubject.setStatus(status);
    }

    @Test
    public void testGetEntity() throws Exception {
        Response testSubject;
        RequestList result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEntity();
    }

    @Test
    public void testSetEntity() throws Exception {
        Response testSubject;
        RequestList entity = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setEntity(entity);
    }
}