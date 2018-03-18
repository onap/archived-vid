package org.onap.vid.mso.model;

import org.junit.Test;

public class OperationalEnvironmentDeactivateInfoTest {

    private OperationalEnvironmentDeactivateInfo createTestSubject() {
        return new OperationalEnvironmentDeactivateInfo("", "");
    }

    @Test
    public void testGetUserId() throws Exception {
        OperationalEnvironmentDeactivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getUserId();
    }

    @Test
    public void testGetOperationalEnvironmentId() throws Exception {
        OperationalEnvironmentDeactivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentId();
    }

    @Test
    public void testToString() throws Exception {
        OperationalEnvironmentDeactivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}