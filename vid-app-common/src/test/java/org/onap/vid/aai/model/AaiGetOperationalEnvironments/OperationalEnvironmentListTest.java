package org.onap.vid.aai.model.AaiGetOperationalEnvironments;

import java.util.List;

import org.junit.Test;
import org.onap.vid.aai.OperationalEnvironment;

public class OperationalEnvironmentListTest {

    private OperationalEnvironmentList createTestSubject() {
        return new OperationalEnvironmentList();
    }

    @Test
    public void testGetOperationalEnvironment() throws Exception {
        OperationalEnvironmentList testSubject;
        List<OperationalEnvironment> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironment();
    }

    @Test
    public void testSetOperationalEnvironment() throws Exception {
        OperationalEnvironmentList testSubject;
        List<OperationalEnvironment> operationalEnvironment = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setOperationalEnvironment(operationalEnvironment);
    }
}