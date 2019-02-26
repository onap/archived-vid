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

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ChangeManagementRequestTest {

    private ChangeManagementRequest createTestSubject() {
        return new ChangeManagementRequest();
    }

    @Test
    public void testGetRequestDetails() throws Exception {
        ChangeManagementRequest testSubject;
        List<RequestDetails> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestDetails();
    }

    @Test
    public void testSetRequestDetails() throws Exception {
        ChangeManagementRequest testSubject;
        List<RequestDetails> requestDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestDetails(requestDetails);
    }

    @Test
    public void testGetRequestType() throws Exception {
        ChangeManagementRequest testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestType();
    }

    @Test
    public void testSetRequestType() throws Exception {
        ChangeManagementRequest testSubject;
        String requestType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestType(requestType);
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        ChangeManagementRequest testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        ChangeManagementRequest testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }
}
