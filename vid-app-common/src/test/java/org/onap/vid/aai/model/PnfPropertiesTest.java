package org.onap.vid.aai.model;

import java.util.Map;

import org.junit.Test;

public class PnfPropertiesTest {

    private PnfProperties createTestSubject() {
        return new PnfProperties();
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        PnfProperties testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        PnfProperties testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }
}