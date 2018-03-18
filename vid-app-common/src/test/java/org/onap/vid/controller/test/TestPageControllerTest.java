package org.onap.vid.controller.test;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

public class TestPageControllerTest {

    private TestPageController createTestSubject() {
        return new TestPageController();
    }

    @Test
    public void testTestMsoPage() throws Exception {
        TestPageController testSubject;
        ModelAndView result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.testMsoPage();
    }

    @Test
    public void testTestViewEditPage() throws Exception {
        TestPageController testSubject;
        ModelAndView result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.testViewEditPage();
    }
}