package org.onap.vid.model;

import java.util.List;

import org.junit.Test;

public class ListOfErrorsResponseTest {

    private ListOfErrorsResponse createTestSubject() {
        return new ListOfErrorsResponse();
    }

    @Test
    public void testGetErrors() throws Exception {
        ListOfErrorsResponse testSubject;
        List<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getErrors();
    }

    @Test
    public void testSetErrors() throws Exception {
        ListOfErrorsResponse testSubject;
        List<String> errors = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setErrors(errors);
    }
}