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

import org.junit.Assert;
import org.junit.Test;

public class VnfDetailsTest {

    private VnfDetails createTestSubject() {
        return new VnfDetails();
    }

    @Test
    public void testGetUUID() throws Exception {
        VnfDetails testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getUUID();
    }

    @Test
    public void testSetUUID() throws Exception {
        VnfDetails testSubject;
        String uUID = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setUUID(uUID);
    }

    @Test
    public void testGetInvariantUUID() throws Exception {
        VnfDetails testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getInvariantUUID();
    }

    @Test
    public void testSetInvariantUUID() throws Exception {
        VnfDetails testSubject;
        String invariantUUID = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setInvariantUUID(invariantUUID);
    }

    @Test
    public void testEquals() throws Exception {
        VnfDetails testSubject;
        Object o = null;
        boolean result;

        // test 1
        testSubject = createTestSubject();
        o = null;
        result = testSubject.equals(o);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testHashCode() throws Exception {
        VnfDetails testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.hashCode();
    }

    @Test
    public void testToString() throws Exception {
        VnfDetails testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}
