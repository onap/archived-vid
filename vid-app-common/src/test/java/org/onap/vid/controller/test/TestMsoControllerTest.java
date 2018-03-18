package org.onap.vid.controller.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class TestMsoControllerTest {

    private TestMsoController createTestSubject() {
        return new TestMsoController();
    }


    @Test
    public void testGetOrchestrationRequests() throws Exception {
        TestMsoController testSubject;
        String filterString = "";
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOrchestrationRequests(filterString, request);
    }


}