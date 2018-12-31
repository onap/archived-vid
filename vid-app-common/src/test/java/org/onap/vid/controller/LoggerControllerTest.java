package org.onap.vid.controller;

import org.junit.Test;

import javax.ws.rs.NotAuthorizedException;


public class LoggerControllerTest {

    private LoggerController createTestSubject() {
        return new LoggerController();
    }

    /*@Test
    public void testGetLog() throws Exception {
        LoggerController testSubject;
        String loggerName = "";
        HttpServletRequest request = null;
        Integer limit = 0;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getLog(loggerName, request, limit);
    }*/

   
    @Test
    public void testNotAuthorizedHandler() throws Exception {
        LoggerController testSubject;
        NotAuthorizedException e = null;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.notAuthorizedHandler(e);
    }

    /*@Test
    public void testIoExceptionHandler() throws Exception {
        LoggerController testSubject;
        Exception e = null;
        ExceptionResponse result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.ioExceptionHandler(e);
    }*/
}