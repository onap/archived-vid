package org.onap.vid.aai.model;

import java.util.Map;

import org.junit.Test;

public class AaiGetPnfResponseTest {

    private AaiGetPnfResponse createTestSubject() {
        return new AaiGetPnfResponse();
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        AaiGetPnfResponse testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        AaiGetPnfResponse testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }

    @Test
    public void testToString() throws Exception {
        AaiGetPnfResponse testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}