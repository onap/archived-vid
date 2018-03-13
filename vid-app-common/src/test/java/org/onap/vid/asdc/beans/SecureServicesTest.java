package org.onap.vid.asdc.beans;

import java.util.Collection;

import org.junit.Test;

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
    }

    @Test
    public void testGetServices() throws Exception {
        SecureServices testSubject;
        Collection<Service> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getServices();
    }

    @Test
    public void testIsReadOnly() throws Exception {
        SecureServices testSubject;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.isReadOnly();
    }

    @Test
    public void testSetReadOnly() throws Exception {
        SecureServices testSubject;
        boolean readOnly = false;

        // default test
        testSubject = createTestSubject();
        testSubject.setReadOnly(readOnly);
    }
}