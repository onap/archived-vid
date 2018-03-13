package org.onap.vid.asdc.beans.tosca;

import org.junit.Test;

public class SchemaTest {

    private Schema createTestSubject() {
        return new Schema();
    }

    @Test
    public void testGetType() throws Exception {
        Schema testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getType();
    }

    @Test
    public void testSetType() throws Exception {
        Schema testSubject;
        String type = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setType(type);
    }
}