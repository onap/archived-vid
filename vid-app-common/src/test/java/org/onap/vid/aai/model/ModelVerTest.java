package org.onap.vid.aai.model;

import org.junit.Test;

public class ModelVerTest {

    private ModelVer createTestSubject() {
        return new ModelVer();
    }

    @Test
    public void testGetModelVersionId() throws Exception {
        ModelVer testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelVersionId();
    }

    @Test
    public void testSetModelVersionId() throws Exception {
        ModelVer testSubject;
        String modelVersionId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelVersionId(modelVersionId);
    }

    @Test
    public void testGetModelName() throws Exception {
        ModelVer testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelName();
    }

    @Test
    public void testSetModelName() throws Exception {
        ModelVer testSubject;
        String modelName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelName(modelName);
    }

    @Test
    public void testGetModelVersion() throws Exception {
        ModelVer testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelVersion();
    }

    @Test
    public void testSetModelVersion() throws Exception {
        ModelVer testSubject;
        String modelVersion = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelVersion(modelVersion);
    }

    @Test
    public void testGetDistributionStatus() throws Exception {
        ModelVer testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getDistributionStatus();
    }

    @Test
    public void testSetDistributionStatus() throws Exception {
        ModelVer testSubject;
        String distributionStatus = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setDistributionStatus(distributionStatus);
    }

    @Test
    public void testGetResourceVersion() throws Exception {
        ModelVer testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResourceVersion();
    }

    @Test
    public void testSetResourceVersion() throws Exception {
        ModelVer testSubject;
        String resourceVersion = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setResourceVersion(resourceVersion);
    }

    @Test
    public void testGetModelDescription() throws Exception {
        ModelVer testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelDescription();
    }

    @Test
    public void testSetModelDescription() throws Exception {
        ModelVer testSubject;
        String modelDescription = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelDescription(modelDescription);
    }
}