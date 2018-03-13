package org.onap.vid.aai.model;

import org.junit.Test;

public class AaiRelationResponseTest {

    private AaiRelationResponse createTestSubject() {
        return new AaiRelationResponse();
    }

    @Test
    public void testGetResourceVersion() throws Exception {
        AaiRelationResponse testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResourceVersion();
    }

    @Test
    public void testSetResourceVersion() throws Exception {
        AaiRelationResponse testSubject;
        String resourceVersion = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setResourceVersion(resourceVersion);
    }

    @Test
    public void testGetRelationshipList() throws Exception {
        AaiRelationResponse testSubject;
        RelationshipList result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRelationshipList();
    }

    @Test
    public void testSetRelationshipList() throws Exception {
        AaiRelationResponse testSubject;
        RelationshipList relationshipList = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRelationshipList(relationshipList);
    }
}