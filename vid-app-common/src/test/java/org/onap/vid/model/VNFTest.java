package org.onap.vid.model;

import org.junit.Test;

public class VNFTest {

    private VNF createTestSubject() {
        return new VNF();
    }

    @Test
    public void testNormalizeName() throws Exception {
        String originalName = "test";
        String result;

        // default test
        result = VNF.normalizeName(originalName);
    }
}