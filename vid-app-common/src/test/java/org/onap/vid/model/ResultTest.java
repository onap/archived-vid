package org.onap.vid.model;

import org.junit.Test;

public class ResultTest {

    private Result createTestSubject() {
        return new Result("");
    }

    @Test
    public void testGetResult() throws Exception {
        Result testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResult();
    }

    @Test
    public void testSetResult() throws Exception {
        Result testSubject;
        String result = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setResult(result);
    }
}