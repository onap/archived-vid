package org.onap.vid.changeManagement;

import java.util.Map;

import org.junit.Test;

public class CloudConfigurationTest {

    private CloudConfiguration createTestSubject() {
        return new CloudConfiguration();
    }

    @Test
    public void testGetLcpCloudRegionId() throws Exception {
        CloudConfiguration testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getLcpCloudRegionId();
    }

    @Test
    public void testSetLcpCloudRegionId() throws Exception {
        CloudConfiguration testSubject;
        String lcpCloudRegionId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setLcpCloudRegionId(lcpCloudRegionId);
    }

    @Test
    public void testGetTenantId() throws Exception {
        CloudConfiguration testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getTenantId();
    }

    @Test
    public void testSetTenantId() throws Exception {
        CloudConfiguration testSubject;
        String tenantId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setTenantId(tenantId);
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        CloudConfiguration testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        CloudConfiguration testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }
}