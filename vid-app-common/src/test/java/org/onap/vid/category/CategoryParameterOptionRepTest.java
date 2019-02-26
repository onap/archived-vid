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

package org.onap.vid.category;

import org.junit.Test;

public class CategoryParameterOptionRepTest {

    private CategoryParameterOptionRep createTestSubject() {
        return new CategoryParameterOptionRep();
    }

    @Test
    public void testGetId() throws Exception {
        CategoryParameterOptionRep testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getId();
    }

    @Test
    public void testSetId() throws Exception {
        CategoryParameterOptionRep testSubject;
        String id = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setId(id);
    }

    @Test
    public void testGetName() throws Exception {
        CategoryParameterOptionRep testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getName();
    }

    @Test
    public void testSetName() throws Exception {
        CategoryParameterOptionRep testSubject;
        String name = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setName(name);
    }
}
