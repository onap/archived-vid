package org.onap.vid.factories;

import org.junit.Test;

public class MsoRequestFactoryTest {

    private MsoRequestFactory createTestSubject() {
        return new MsoRequestFactory();
    }

    @Test
    public void testCreateMsoRequest() throws Exception {
        MsoRequestFactory testSubject;
        String path = "";

        // default test
        testSubject = createTestSubject();
        testSubject.createMsoRequest(path);
    }
}