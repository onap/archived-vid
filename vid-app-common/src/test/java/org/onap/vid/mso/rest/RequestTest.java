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

public class RequestTest {

    private Request createTestSubject() {
        return new Request();
    }

    @Test
    public void testGetInstanceIds() throws Exception {
        Request testSubject;
        InstanceIds result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getInstanceIds();
    }

    @Test
    public void testSetInstanceIds() throws Exception {
        Request testSubject;
        InstanceIds instanceIds = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setInstanceIds(instanceIds);
    }

    @Test
    public void testGetRequestDetails() throws Exception {
        Request testSubject;
        RequestDetails result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestDetails();
    }

    @Test
    public void testSetRequestDetails() throws Exception {
        Request testSubject;
        RequestDetails requestDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestDetails(requestDetails);
    }

    @Test
    public void testGetRequestStatus() throws Exception {
        Request testSubject;
        RequestStatus result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestStatus();
    }

    @Test
    public void testSetRequestStatus() throws Exception {
        Request testSubject;
        RequestStatus requestStatus = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestStatus(requestStatus);
    }

    @Test
    public void testToString() throws Exception {
        Request testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }

    @Test
    public void testEquals() throws Exception {
        Request testSubject;
        Object other = null;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.equals(other);
    }

    @Test
    public void testHashCode() throws Exception {
        Request testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.hashCode();
    }
}
