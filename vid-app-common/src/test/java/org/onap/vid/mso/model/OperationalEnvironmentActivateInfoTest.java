package org.onap.vid.mso.model;

import org.junit.Test;
import org.onap.vid.controllers.OperationalEnvironmentController;
import org.onap.vid.controllers.OperationalEnvironmentController.OperationalEnvironmentActivateBody;
import org.onap.vid.controllers.OperationalEnvironmentController.OperationalEnvironmentManifest;

public class OperationalEnvironmentActivateInfoTest {

    private OperationalEnvironmentActivateInfo createTestSubject() {
        OperationalEnvironmentController.OperationalEnvironmentActivateBody a = new OperationalEnvironmentActivateBody("a", "b", "c", new OperationalEnvironmentManifest());
        return new OperationalEnvironmentActivateInfo(a, "", "");
    }

    @Test
    public void testGetUserId() throws Exception {
        OperationalEnvironmentActivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getUserId();
    }

    @Test
    public void testGetOperationalEnvironmentId() throws Exception {
        OperationalEnvironmentActivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentId();
    }

    @Test
    public void testToString() throws Exception {
        OperationalEnvironmentActivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}