package org.onap.vid.aai.model;

import java.util.Map;

import org.junit.Test;

public class PnfResultTest {

    private PnfResult createTestSubject() {
        return new PnfResult();
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        PnfResult testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        PnfResult testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }
}