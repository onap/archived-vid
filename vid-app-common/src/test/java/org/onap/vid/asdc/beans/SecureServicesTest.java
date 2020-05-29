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

package org.onap.vid.asdc.beans;

import java.util.Collection;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class SecureServicesTest {

    private SecureServices createTestSubject() {
        return new SecureServices();
    }

    @Test
    public void testSetServices() throws Exception {
        SecureServices testSubject;
        Collection<Service> services = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setServices(services);
        assertNotNull(testSubject);
    }

    @Test
    public void testGetServices() throws Exception {
        SecureServices testSubject;
        Collection<Service> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getServices();
        assertNotNull(testSubject);
    }

    @Test
    public void testIsReadOnly() throws Exception {
        SecureServices testSubject;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.isReadOnly();
        assertNotNull(testSubject);
    }

    @Test
    public void testSetReadOnly() throws Exception {
        SecureServices testSubject;
        boolean readOnly = false;

        // default test
        testSubject = createTestSubject();
        testSubject.setReadOnly(readOnly);
        assertNotNull(testSubject);
    }
}
