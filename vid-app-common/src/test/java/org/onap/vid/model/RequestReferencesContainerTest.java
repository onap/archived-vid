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
import org.onap.vid.mso.model.RequestReferences;

import java.util.Map;

public class RequestReferencesContainerTest {

    private RequestReferencesContainer createTestSubject() {
        return new RequestReferencesContainer(new RequestReferences());
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        RequestReferencesContainer testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        RequestReferencesContainer testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }

    @Test
    public void testGetRequestReferences() throws Exception {
        RequestReferencesContainer testSubject;
        RequestReferences result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestReferences();
    }

    @Test
    public void testToString() throws Exception {
        RequestReferencesContainer testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}
