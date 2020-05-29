/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.service;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.domain.User;

public class AdminAuthExtensionTest {

    private AdminAuthExtension createTestSubject() {
        return new AdminAuthExtension();
    }

    @Test
    public void testSaveUserExtension() throws Exception {
        AdminAuthExtension testSubject;
        User user = null;

        // default test
        testSubject = createTestSubject();
        testSubject.saveUserExtension(user);
        Assert.assertNotNull(testSubject);
    }

    @Test
    public void testEditUserExtension() throws Exception {
        AdminAuthExtension testSubject;
        User user = null;

        // default test
        testSubject = createTestSubject();
        testSubject.editUserExtension(user);
        Assert.assertNotNull(testSubject);
    }

    @Test
    public void testSaveUserRoleExtension() throws Exception {
        AdminAuthExtension testSubject;
        Set<Role> roles = null;
        User user = null;

        // default test
        testSubject = createTestSubject();
        testSubject.saveUserRoleExtension(roles, user);
        Assert.assertNotNull(testSubject);
    }
}