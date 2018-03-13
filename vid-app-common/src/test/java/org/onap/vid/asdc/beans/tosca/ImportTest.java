package org.onap.vid.asdc.beans.tosca;

import org.junit.Test;

public class ImportTest {

    private Import createTestSubject() {
        return new Import();
    }

    @Test
    public void testGetFile() throws Exception {
        Import testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getFile();
    }

    @Test
    public void testSetFile() throws Exception {
        Import testSubject;
        String file = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setFile(file);
    }
}