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

package org.onap.vid.mso;

import org.junit.Test;

public class MsoResponseWrapperTest {

    private MsoResponseWrapper createTestSubject() {
        return new MsoResponseWrapper();
    }

    @Test
    public void testGetEntity() throws Exception {
        MsoResponseWrapper testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEntity();
    }

    @Test
    public void testGetStatus() throws Exception {
        MsoResponseWrapper testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getStatus();
    }

    @Test
    public void testSetStatus() throws Exception {
        MsoResponseWrapper testSubject;
        int v = 0;

        // default test
        testSubject = createTestSubject();
        testSubject.setStatus(v);
    }

    @Test
    public void testSetEntity() throws Exception {
        MsoResponseWrapper testSubject;
        String v = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setEntity(v);
    }

    @Test
    public void testToString() throws Exception {
        MsoResponseWrapper testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }

    @Test
    public void testGetResponse() throws Exception {
        MsoResponseWrapper testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResponse();
    }
}
