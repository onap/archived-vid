package org.onap.portalapp.service;

import java.util.Set;

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
    }

    @Test
    public void testEditUserExtension() throws Exception {
        AdminAuthExtension testSubject;
        User user = null;

        // default test
        testSubject = createTestSubject();
        testSubject.editUserExtension(user);
    }

    @Test
    public void testSaveUserRoleExtension() throws Exception {
        AdminAuthExtension testSubject;
        Set<Role> roles = null;
        User user = null;

        // default test
        testSubject = createTestSubject();
        testSubject.saveUserRoleExtension(roles, user);
    }
}