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
