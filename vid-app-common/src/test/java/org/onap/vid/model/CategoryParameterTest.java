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

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.junit.Test;

public class CategoryParameterTest {

    private CategoryParameter createTestSubject() {
        return new CategoryParameter();
    }

    @Test
    public void testGetFamily() throws Exception {
        CategoryParameter testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getFamily();
    }

    @Test
    public void testSetFamily() throws Exception {
        CategoryParameter testSubject;
        String family = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setFamily(family);
    }

    @Test
    public void testGetId() throws Exception {
        CategoryParameter testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getId();
    }

    @Test
    public void testGetCreated() throws Exception {
        CategoryParameter testSubject;
        Date result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCreated();
    }

    @Test
    public void testGetModified() throws Exception {
        CategoryParameter testSubject;
        Date result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModified();
    }

    @Test
    public void testGetCreatedId() throws Exception {
        CategoryParameter testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCreatedId();
    }

    @Test
    public void testGetModifiedId() throws Exception {
        CategoryParameter testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModifiedId();
    }

    @Test
    public void testGetName() throws Exception {
        CategoryParameter testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getName();
    }

    @Test
    public void testSetName() throws Exception {
        CategoryParameter testSubject;
        String name = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setName(name);
    }

    @Test
    public void testGetAuditUserId() throws Exception {
        CategoryParameter testSubject;
        Serializable result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAuditUserId();
    }

    @Test
    public void testGetRowNum() throws Exception {
        CategoryParameter testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRowNum();
    }

    @Test
    public void testGetAuditTrail() throws Exception {
        CategoryParameter testSubject;
        Set result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAuditTrail();
    }

    @Test
    public void testGetOptions() throws Exception {
        CategoryParameter testSubject;
        Set<CategoryParameterOption> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOptions();
    }

    @Test
    public void testSetOptions() throws Exception {
        CategoryParameter testSubject;
        Set<CategoryParameterOption> options = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setOptions(options);
    }

    @Test
    public void testAddOption() throws Exception {
        CategoryParameter testSubject;
        CategoryParameterOption option = null;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.addOption(option);
    }

    @Test
    public void testIsIdSupported() throws Exception {
        CategoryParameter testSubject;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.isIdSupported();
    }

    @Test
    public void testSetIdSupported() throws Exception {
        CategoryParameter testSubject;
        boolean idSupported = false;

        // default test
        testSubject = createTestSubject();
        testSubject.setIdSupported(idSupported);
    }
}
