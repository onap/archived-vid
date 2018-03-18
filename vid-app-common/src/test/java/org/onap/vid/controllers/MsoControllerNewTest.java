package org.onap.vid.controllers;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.rest.RequestDetails;
import org.springframework.http.ResponseEntity;

public class MsoControllerNewTest {

    private MsoController createTestSubject() {
        try {
            return new MsoController(new MsoBusinessLogicImpl(null));
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    public void testCreateSvcInstance() throws Exception {
        MsoController testSubject;
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createSvcInstance(request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateVnf() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createVnf(serviceInstanceId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateNwInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createNwInstance(serviceInstanceId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateVolumeGroupInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String vnfInstanceId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createVolumeGroupInstance(serviceInstanceId, vnfInstanceId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateVfModuleInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String vnfInstanceId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createVfModuleInstance(serviceInstanceId, vnfInstanceId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateConfigurationInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createConfigurationInstance(serviceInstanceId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteSvcInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteSvcInstance(serviceInstanceId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteVnf() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String vnfInstanceId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteVnf(serviceInstanceId, vnfInstanceId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteConfiguration() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String configurationId = "";
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteConfiguration(serviceInstanceId, configurationId, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testActivateConfiguration() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String configurationId = "";
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.activateConfiguration(serviceInstanceId, configurationId, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeactivateConfiguration() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String configurationId = "";
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deactivateConfiguration(serviceInstanceId, configurationId, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDisablePortOnConfiguration() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String configurationId = "";
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.disablePortOnConfiguration(serviceInstanceId, configurationId, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testEnablePortOnConfiguration() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String configurationId = "";
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.enablePortOnConfiguration(serviceInstanceId, configurationId, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteVfModule() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String vnfInstanceId = "";
        String vfModuleId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteVfModule(serviceInstanceId, vnfInstanceId, vfModuleId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteVolumeGroupInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String vnfInstanceId = "";
        String volumeGroupId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteVolumeGroupInstance(serviceInstanceId, vnfInstanceId, volumeGroupId, request,
                    mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteNwInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        String networkInstanceId = "";
        HttpServletRequest request = null;
        RequestDetails mso_request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteNwInstance(serviceInstanceId, networkInstanceId, request, mso_request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetOrchestrationRequest() throws Exception {
        MsoController testSubject;
        String requestId = "";
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getOrchestrationRequest(requestId, request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetOrchestrationRequests() throws Exception {
        MsoController testSubject;
        String filterString = "";
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getOrchestrationRequests(filterString, request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testActivateServiceInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        RequestDetails requestDetails = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.activateServiceInstance(serviceInstanceId, requestDetails);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeactivateServiceInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        RequestDetails requestDetails = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deactivateServiceInstance(serviceInstanceId, requestDetails);
        } catch (Exception e) {
        }
    }

    @Test
    public void testManualTaskComplete() throws Exception {
        MsoController testSubject;
        String taskId = "";
        RequestDetails requestDetails = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.manualTaskComplete(taskId, requestDetails);
        } catch (Exception e) {
        }
    }

    @Test
    public void testRemoveRelationshipFromServiceInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        RequestDetails requestDetails = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.removeRelationshipFromServiceInstance(serviceInstanceId, requestDetails);
        } catch (Exception e) {
        }
    }

    @Test
    public void testAddRelationshipToServiceInstance() throws Exception {
        MsoController testSubject;
        String serviceInstanceId = "";
        RequestDetails requestDetails = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.addRelationshipToServiceInstance(serviceInstanceId, requestDetails);
        } catch (Exception e) {
        }
    }
}
