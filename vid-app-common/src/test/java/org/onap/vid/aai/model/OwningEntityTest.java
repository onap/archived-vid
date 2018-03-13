package org.onap.vid.aai.model;

import org.junit.Test;

public class OwningEntityTest {

    private OwningEntity createTestSubject() {
        return new OwningEntity();
    }

    @Test
    public void testGetOwningEntityId() throws Exception {
        OwningEntity testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOwningEntityId();
    }

    @Test
    public void testSetOwningEntityId() throws Exception {
        OwningEntity testSubject;
        String owningEntityId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setOwningEntityId(owningEntityId);
    }

    @Test
    public void testGetOwningEntityName() throws Exception {
        OwningEntity testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOwningEntityName();
    }

    @Test
    public void testSetOwningEntityName() throws Exception {
        OwningEntity testSubject;
        String owningEntityName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setOwningEntityName(owningEntityName);
    }
}