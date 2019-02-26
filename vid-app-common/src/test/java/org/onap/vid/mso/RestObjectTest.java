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

public class RestObjectTest {

    private RestObject createTestSubject() {
        return new RestObject();
    }

    @Test
    public void testSet() throws Exception {
        RestObject testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.set(null);
    }

    @Test
    public void testGet() throws Exception {
        RestObject testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.get();
    }

    @Test
    public void testSetStatusCode() throws Exception {
        RestObject testSubject;
        int v = 0;

        // default test
        testSubject = createTestSubject();
        testSubject.setStatusCode(v);
    }

    @Test
    public void testGetStatusCode() throws Exception {
        RestObject testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getStatusCode();
    }

    @Test
    public void testGetRaw() throws Exception {
        RestObject testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRaw();
    }

    @Test
    public void testSetRaw() throws Exception {
        RestObject testSubject;
        String rawT = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setRaw(rawT);
    }

    @Test
    public void testToString() throws Exception {
        RestObject testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}
