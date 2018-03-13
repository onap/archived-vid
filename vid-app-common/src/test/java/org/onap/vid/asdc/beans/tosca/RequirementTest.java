package org.onap.vid.asdc.beans.tosca;

import java.util.Collection;

import org.junit.Test;

public class RequirementTest {

    private Requirement createTestSubject() {
        return new Requirement();
    }

    @Test
    public void testGetOccurrences() throws Exception {
        Requirement testSubject;
        Collection<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOccurrences();
    }

    @Test
    public void testGetCapability() throws Exception {
        Requirement testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCapability();
    }

    @Test
    public void testGetNode() throws Exception {
        Requirement testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getNode();
    }

    @Test
    public void testGetRelationship() throws Exception {
        Requirement testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRelationship();
    }

    @Test
    public void testSetOccurrences() throws Exception {
        Requirement testSubject;
        Collection<String> occurrences = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setOccurrences(occurrences);
    }

    @Test
    public void testSetCapability() throws Exception {
        Requirement testSubject;
        String capability = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setCapability(capability);
    }

    @Test
    public void testSetNode() throws Exception {
        Requirement testSubject;
        String node = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setNode(node);
    }

    @Test
    public void testSetRelationship() throws Exception {
        Requirement testSubject;
        String relationship = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setRelationship(relationship);
    }
}