package org.onap.portalapp.scheduler;

import java.util.List;

import org.junit.Test;

public class RegisterTest {

    private Register createTestSubject() {
        return new Register();
    }

    @Test
    public void testGetTriggers() throws Exception {
        Register testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.getTriggers();
    }

    @Test
    public void testGetScheduleTriggers() throws Exception {
        Register testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.getScheduleTriggers();
    }

    @Test
    public void testSetScheduleTriggers() throws Exception {
        Register testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.setScheduleTriggers(null);
    }
}