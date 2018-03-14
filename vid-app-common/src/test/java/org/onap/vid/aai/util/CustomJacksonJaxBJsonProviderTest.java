package org.onap.vid.aai.util;

import org.junit.Test;

public class CustomJacksonJaxBJsonProviderTest {

    private CustomJacksonJaxBJsonProvider createTestSubject() {
        return new CustomJacksonJaxBJsonProvider();
    }

    @Test
    public void testGetMapper() throws Exception {
        CustomJacksonJaxBJsonProvider testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.getMapper();
    }
}