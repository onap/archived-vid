package org.onap.vid.roles;

import org.junit.Test;

public class RoleTest {

    private Role createTestSubject() {
        return new Role(EcompRole.READ, "", "", "");
    }

    @Test
    public void testGetEcompRole() throws Exception {
        Role testSubject;
        EcompRole result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEcompRole();
    }

    @Test
    public void testGetSubscribeName() throws Exception {
        Role testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSubscribeName();
    }

    @Test
    public void testSetSubscribeName() throws Exception {
        Role testSubject;
        String subscribeName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setSubscribeName(subscribeName);
    }

    @Test
    public void testGetServiceType() throws Exception {
        Role testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getServiceType();
    }

    @Test
    public void testGetTenant() throws Exception {
        Role testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getTenant();
    }
}