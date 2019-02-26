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

public class MsoResponseWrapper2Test {

    private MsoResponseWrapper2 createTestSubject() {
        return new MsoResponseWrapper2(new RestObject());
    }

    @Test
    public void testGetStatus() throws Exception {
        MsoResponseWrapper2 testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getStatus();
    }

    @Test
    public void testGetResponse() throws Exception {
        MsoResponseWrapper2 testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResponse();
    }

    @Test
    public void testGetEntity() throws Exception {
        MsoResponseWrapper2 testSubject;
        Object result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEntity();
    }
}
