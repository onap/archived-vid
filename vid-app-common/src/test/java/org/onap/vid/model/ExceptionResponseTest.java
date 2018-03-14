package org.onap.vid.model;

import org.junit.Test;

public class ExceptionResponseTest {

    private ExceptionResponse createTestSubject() {
        return new ExceptionResponse();
    }

    @Test
    public void testGetException() throws Exception {
        ExceptionResponse testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getException();
    }

    @Test
    public void testSetException() throws Exception {
        ExceptionResponse testSubject;
        String exception = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setException(exception);
    }

    @Test
    public void testGetMessage() throws Exception {
        ExceptionResponse testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getMessage();
    }

    @Test
    public void testSetMessage() throws Exception {
        ExceptionResponse testSubject;
        String message = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setMessage(message);
    }
}