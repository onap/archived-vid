package org.onap.vid.aai;

import org.junit.Test;
import org.onap.vid.aai.model.RelationshipList;

public class OperationalEnvironmentTest {

    private OperationalEnvironment createTestSubject() {
        return new OperationalEnvironment();
    }

    @Test
    public void testGetOperationalEnvironmentId() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentId();
    }

    @Test
    public void testSetOperationalEnvironmentId() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setOperationalEnvironmentId(operationalEnvironmentId);
    }

    @Test
    public void testGetOperationalEnvironmentName() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentName();
    }

    @Test
    public void testSetOperationalEnvironmentName() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setOperationalEnvironmentName(operationalEnvironmentName);
    }

    @Test
    public void testGetOperationalEnvironmentType() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentType();
    }

    @Test
    public void testSetOperationalEnvironmentType() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setOperationalEnvironmentType(operationalEnvironmentType);
    }

    @Test
    public void testGetOperationalEnvironmentStatus() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentStatus();
    }

    @Test
    public void testSetOperationalEnvironmentStatus() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentStatus = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setOperationalEnvironmentStatus(operationalEnvironmentStatus);
    }

    @Test
    public void testGetTenantContext() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getTenantContext();
    }

    @Test
    public void testSetTenantContext() throws Exception {
        OperationalEnvironment testSubject;
        String tenantContext = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setTenantContext(tenantContext);
    }

    @Test
    public void testGetWorkloadContext() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkloadContext();
    }

    @Test
    public void testSetWorkloadContext() throws Exception {
        OperationalEnvironment testSubject;
        String workloadContext = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkloadContext(workloadContext);
    }

    @Test
    public void testGetResourceVersion() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResourceVersion();
    }

    @Test
    public void testSetResourceVersion() throws Exception {
        OperationalEnvironment testSubject;
        String resourceVersion = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setResourceVersion(resourceVersion);
    }

    @Test
    public void testGetRelationshipList() throws Exception {
        OperationalEnvironment testSubject;
        RelationshipList result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRelationshipList();
    }

    @Test
    public void testSetRelationshipList() throws Exception {
        OperationalEnvironment testSubject;
        RelationshipList relationshipList = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRelationshipList(relationshipList);
    }
}