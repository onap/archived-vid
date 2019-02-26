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
