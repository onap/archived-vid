package org.onap.vid.aai.model;

import org.junit.Test;

public class ModelTest {

    private Model createTestSubject() {
        return new Model();
    }

    @Test
    public void testGetModelInvariantId() throws Exception {
        Model testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelInvariantId();
    }

    @Test
    public void testSetModelInvariantId() throws Exception {
        Model testSubject;
        String modelInvariantId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelInvariantId(modelInvariantId);
    }

    @Test
    public void testGetModelType() throws Exception {
        Model testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelType();
    }

    @Test
    public void testSetModelType() throws Exception {
        Model testSubject;
        String modelType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelType(modelType);
    }

    @Test
    public void testGetResourceVersion() throws Exception {
        Model testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResourceVersion();
    }

    @Test
    public void testSetResourceVersion() throws Exception {
        Model testSubject;
        String resourceVersion = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setResourceVersion(resourceVersion);
    }

    @Test
    public void testGetModelVers() throws Exception {
        Model testSubject;
        ModelVers result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelVers();
    }

    @Test
    public void testSetModelVers() throws Exception {
        Model testSubject;
        ModelVers modelVers = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setModelVers(modelVers);
    }
}