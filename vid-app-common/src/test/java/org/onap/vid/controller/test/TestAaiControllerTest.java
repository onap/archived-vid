package org.onap.vid.controller.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class TestAaiControllerTest {

    private TestAaiController createTestSubject() {
        return new TestAaiController();
    }

    @Test
    public void testGetSubscriptionServiceTypeList() throws Exception {
        TestAaiController testSubject;
        String globalCustomerId = "";
        HttpServletRequest request = null;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSubscriptionServiceTypeList(globalCustomerId, request);
    }


}