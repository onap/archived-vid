package org.onap.vid.model;

import java.util.Map;

import org.junit.Test;
import org.onap.vid.asdc.beans.tosca.ToscaModel;
import org.onap.vid.model.Service;
import java.util.*;
import org.junit.Assert;

public class ServiceModelTest {

    private ServiceModel createTestSubject() {
        return new ServiceModel();
    }

    @Test
    public void testGetService() throws Exception {
        ServiceModel testSubject;
        Service result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getService();
    }

    @Test
    public void testGetVnfs() throws Exception {
        ServiceModel testSubject;
        Map<String, VNF> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfs();
    }

    @Test
    public void testGetNetworks() throws Exception {
        ServiceModel testSubject;
        Map<String, Network> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getNetworks();
    }

    @Test
    public void testSetService() throws Exception {
        ServiceModel testSubject;
        Service service = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setService(service);
    }

    @Test
    public void testSetVnfs() throws Exception {
        ServiceModel testSubject;
        Map<String, VNF> vnfs = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfs(vnfs);
    }

    @Test
    public void testSetNetworks() throws Exception {
        ServiceModel testSubject;
        Map<String, Network> networks = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setNetworks(networks);
    }

    @Test
    public void testGetVfModules() throws Exception {
        ServiceModel testSubject;
        Map<String, VfModule> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVfModules();
    }

    @Test
    public void testGetVolumeGroups() throws Exception {
        ServiceModel testSubject;
        Map<String, VolumeGroup> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVolumeGroups();
    }

    @Test
    public void testSetVfModules() throws Exception {
        ServiceModel testSubject;
        Map<String, VfModule> vfModules = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVfModules(vfModules);
    }

    @Test
    public void testSetVolumeGroups() throws Exception {
        ServiceModel testSubject;
        Map<String, VolumeGroup> volumeGroups = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVolumeGroups(volumeGroups);
    }

    @Test
    public void testAssociateGroups() throws Exception {
        ServiceModel testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.associateGroups();
    }

    @Test
    public void testSetServiceProxies() throws Exception {
        ServiceModel testSubject;
        Map<String, ServiceProxy> serviceProxies = null;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.setServiceProxies(serviceProxies);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetPnfs() throws Exception {
        ServiceModel testSubject;
        Map<String, Node> pnfs = null;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.setPnfs(pnfs);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetConfigurations() throws Exception {
        ServiceModel testSubject;
        Map<String, PortMirroringConfig> configurations = null;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.setConfigurations(configurations);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetServiceProxies() throws Exception {
        ServiceModel testSubject;
        Map<String, ServiceProxy> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getServiceProxies();
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetPnfs() throws Exception {
        ServiceModel testSubject;
        Map<String, Node> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getPnfs();
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetConfigurations() throws Exception {
        ServiceModel testSubject;
        Map<String, PortMirroringConfig> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getConfigurations();
        } catch (Exception e) {
        }
    }

    @Test
    public void testExtractService() throws Exception {
        ToscaModel serviceToscaModel = null;
        org.onap.vid.asdc.beans.Service asdcServiceMetadata = null;
        Service result;

        // default test
        try {
            result = ServiceModel.extractService(serviceToscaModel, asdcServiceMetadata);
        } catch (Exception e) {
        }
    }

    @Test
    public void testExtractGroups() throws Exception {
        ToscaModel serviceToscaModel = null;
        ServiceModel serviceModel = null;

        // default test
        try {
            ServiceModel.extractGroups(serviceToscaModel, serviceModel);
        } catch (Exception e) {
        }
    }
}