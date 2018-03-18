package org.onap.vid.controller.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class TestAsdcControllerTest {

    private TestAsdcController createTestSubject() {
        return new TestAsdcController();
    }

    @Test
    public void testGetModel() throws Exception {
        TestAsdcController testSubject;
        String modelId = "";
        HttpServletRequest request = null;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModel(modelId, request);
    }

}