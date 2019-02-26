/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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
