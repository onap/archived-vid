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
