package org.onap.vid.aai.model;

import org.junit.Test;

public class LogicalLinkResponseTest {

    private LogicalLinkResponse createTestSubject() {
        return new LogicalLinkResponse();
    }

    @Test
    public void testGetLinkName() throws Exception {
        LogicalLinkResponse testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getLinkName();
    }

    @Test
    public void testSetLinkName() throws Exception {
        LogicalLinkResponse testSubject;
        String linkName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setLinkName(linkName);
    }

    @Test
    public void testGetInMaint() throws Exception {
        LogicalLinkResponse testSubject;
        Boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getInMaint();
    }

    @Test
    public void testSetInMaint() throws Exception {
        LogicalLinkResponse testSubject;
        Boolean inMaint = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setInMaint(inMaint);
    }

    @Test
    public void testGetLinkType() throws Exception {
        LogicalLinkResponse testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getLinkType();
    }

    @Test
    public void testSetLinkType() throws Exception {
        LogicalLinkResponse testSubject;
        String linkType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setLinkType(linkType);
    }

    @Test
    public void testGetResourceVersion() throws Exception {
        LogicalLinkResponse testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResourceVersion();
    }

    @Test
    public void testSetResourceVersion() throws Exception {
        LogicalLinkResponse testSubject;
        String resourceVersion = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setResourceVersion(resourceVersion);
    }

    @Test
    public void testGetPurpose() throws Exception {
        LogicalLinkResponse testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getPurpose();
    }

    @Test
    public void testSetPurpose() throws Exception {
        LogicalLinkResponse testSubject;
        String purpose = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setPurpose(purpose);
    }

    @Test
    public void testGetRelationshipList() throws Exception {
        LogicalLinkResponse testSubject;
        RelationshipList result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRelationshipList();
    }

    @Test
    public void testSetRelationshipList() throws Exception {
        LogicalLinkResponse testSubject;
        RelationshipList relationshipList = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRelationshipList(relationshipList);
    }
}