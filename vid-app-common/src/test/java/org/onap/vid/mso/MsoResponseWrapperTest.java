package org.onap.vid.mso;

import org.junit.Test;

public class MsoResponseWrapperTest {

    private MsoResponseWrapper createTestSubject() {
        return new MsoResponseWrapper();
    }

    @Test
    public void testGetEntity() throws Exception {
        MsoResponseWrapper testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEntity();
    }

    @Test
    public void testGetStatus() throws Exception {
        MsoResponseWrapper testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getStatus();
    }

    @Test
    public void testSetStatus() throws Exception {
        MsoResponseWrapper testSubject;
        int v = 0;

        // default test
        testSubject = createTestSubject();
        testSubject.setStatus(v);
    }

    @Test
    public void testSetEntity() throws Exception {
        MsoResponseWrapper testSubject;
        String v = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setEntity(v);
    }

    @Test
    public void testToString() throws Exception {
        MsoResponseWrapper testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }

    @Test
    public void testGetResponse() throws Exception {
        MsoResponseWrapper testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResponse();
    }
}