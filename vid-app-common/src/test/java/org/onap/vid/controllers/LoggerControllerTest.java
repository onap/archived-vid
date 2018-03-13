package org.onap.vid.controllers;

import static org.junit.Assert.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;

import org.junit.Assert;
import org.junit.Test;
import org.onap.vid.model.ExceptionResponse;


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