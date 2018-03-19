package org.onap.vid.mso.rest;

import org.junit.Test;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.mso.RestObject;

public class MsoRestClientNewTest {

    private MsoRestClientNew createTestSubject() {
        return new MsoRestClientNew();
    }

    @Test
    public void testCreateSvcInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createSvcInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateVnf() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createVnf(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateNwInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createNwInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateVolumeGroupInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createVolumeGroupInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateVfModuleInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createVfModuleInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateConfigurationInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createConfigurationInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteSvcInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteSvcInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteVnf() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteVnf(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteVfModule() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteVfModule(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteVolumeGroupInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteVolumeGroupInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteNwInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteNwInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetOrchestrationRequest() throws Exception {
        MsoRestClientNew testSubject;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject restObject = null;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.getOrchestrationRequest(t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetManualTasks() throws Exception {
        MsoRestClientNew testSubject;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject restObject = null;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.getManualTasks(t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails request = null;
        String path = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createInstance(request, path);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails request = null;
        String path = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteInstance(request, path);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetOrchestrationRequestsForDashboard() throws Exception {
        MsoRestClientNew testSubject;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject restObject = null;
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getOrchestrationRequestsForDashboard(t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetManualTasksByRequestId() throws Exception {
        MsoRestClientNew testSubject;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject restObject = null;
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getManualTasksByRequestId(t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCompleteManualTask() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject restObject = null;
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.completeManualTask(requestDetails, t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteConfiguration() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String pmc_endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteConfiguration(requestDetails, pmc_endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetConfigurationActiveStatus() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails request = null;
        String path = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.setConfigurationActiveStatus(request, path);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetPortOnConfigurationStatus() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails request = null;
        String path = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.setPortOnConfigurationStatus(request, path);
        } catch (Exception e) {
        }
    }

    @Test
    public void testChangeManagementUpdate() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetailsWrapper requestDetails = null;
        String endpoint = "";
        MsoResponseWrapperInterface result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.changeManagementUpdate(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetServiceInstanceStatus() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject<String> restObject = null;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.setServiceInstanceStatus(requestDetails, t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testRemoveRelationshipFromServiceInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.removeRelationshipFromServiceInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testAddRelationshipToServiceInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String addRelationshipsPath = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.addRelationshipToServiceInstance(requestDetails, addRelationshipsPath);
        } catch (Exception e) {
        }
    }
}