package org.onap.vid.aai.model;

import java.util.List;

import org.junit.Test;

public class ModelVersTest {

    private ModelVers createTestSubject() {
        return new ModelVers();
    }

    @Test
    public void testGetModelVer() throws Exception {
        ModelVers testSubject;
        List<ModelVer> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelVer();
    }

    @Test
    public void testSetModelVer() throws Exception {
        ModelVers testSubject;
        List<ModelVer> modelVer = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setModelVer(modelVer);
    }
}