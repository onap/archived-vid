package org.onap.vid.model;

import java.util.Map;

import org.junit.Test;
import org.onap.vid.domain.mso.RequestReferences;

public class RequestReferencesContainerTest {

    private RequestReferencesContainer createTestSubject() {
        return new RequestReferencesContainer(new RequestReferences());
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        RequestReferencesContainer testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        RequestReferencesContainer testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }

    @Test
    public void testGetRequestReferences() throws Exception {
        RequestReferencesContainer testSubject;
        RequestReferences result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestReferences();
    }

    @Test
    public void testToString() throws Exception {
        RequestReferencesContainer testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}