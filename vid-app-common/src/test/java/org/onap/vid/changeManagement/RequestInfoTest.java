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
