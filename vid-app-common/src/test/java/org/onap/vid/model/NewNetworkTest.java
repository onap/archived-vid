package org.onap.vid.model;

import org.junit.Test;

public class NewNetworkTest {

    private NewNetwork createTestSubject() {
        return new NewNetwork();
    }

    @Test
    public void testGetModelCustomizationName() throws Exception {
        NewNetwork testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelCustomizationName();
    }

    @Test
    public void testSetModelCustomizationName() throws Exception {
        NewNetwork testSubject;
        String modelCustomizationName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelCustomizationName(modelCustomizationName);
    }
}