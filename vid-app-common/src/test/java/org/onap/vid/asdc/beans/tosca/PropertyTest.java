package org.onap.vid.asdc.beans.tosca;

import org.junit.Test;

public class PropertyTest {

    private Property createTestSubject() {
        return new Property();
    }

    @Test
    public void testGetType() throws Exception {
        Property testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getType();
    }

    @Test
    public void testGetDescription() throws Exception {
        Property testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getDescription();
    }

    @Test
    public void testGetEntry_schema() throws Exception {
        Property testSubject;
        Schema result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEntry_schema();
    }


    @Test
    public void testSetType() throws Exception {
        Property testSubject;
        String type = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setType(type);
    }

    @Test
    public void testSetDescription() throws Exception {
        Property testSubject;
        String description = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setDescription(description);
    }

    @Test
    public void testSetEntry_schema() throws Exception {
        Property testSubject;
        Schema entry_schema = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setEntry_schema(entry_schema);
    }


    @Test
    public void testGetDefault() throws Exception {
        Property testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getDefault();
    }

    @Test
    public void testIsRequired() throws Exception {
        Property testSubject;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.isRequired();
    }

    @Test
    public void testSetDefault() throws Exception {
        Property testSubject;
        String _default = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setDefault(_default);
    }

    @Test
    public void testSetRequired() throws Exception {
        Property testSubject;
        boolean required = false;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequired(required);
    }
}