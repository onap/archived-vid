package org.onap.vid.aai.model;

import org.junit.Test;

public class ResultTest {

    private Result createTestSubject() {
        return new Result();
    }

    @Test
    public void testGetModel() throws Exception {
        Result testSubject;
        Model result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModel();
    }

    @Test
    public void testSetModel() throws Exception {
        Result testSubject;
        Model model = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setModel(model);
    }
}