package org.onap.vid.mso;

import org.junit.Test;

public class MsoResponseWrapper2Test {

    private MsoResponseWrapper2 createTestSubject() {
        return new MsoResponseWrapper2(new RestObject());
    }

    @Test
    public void testGetStatus() throws Exception {
        MsoResponseWrapper2 testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getStatus();
    }

    @Test
    public void testGetResponse() throws Exception {
        MsoResponseWrapper2 testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResponse();
    }

    @Test
    public void testGetEntity() throws Exception {
        MsoResponseWrapper2 testSubject;
        Object result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEntity();
    }
}