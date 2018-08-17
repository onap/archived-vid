package org.onap.vid.services;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.SubscriberFilteredResults;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.RoleValidator;

public class AaiServiceImplTest {

    private AaiServiceImpl createTestSubject() {
        return new AaiServiceImpl();
    }

//    @Test
//    public void testGetFullSubscriberList() throws Exception {
//        AaiServiceImpl testSubject;
//        RoleValidator roleValidator = null;
//        SubscriberFilteredResults result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getFullSubscriberList(roleValidator);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetOperationalEnvironments() throws Exception {
//        AaiServiceImpl testSubject;
//        String operationalEnvironmentType = "";
//        String operationalEnvironmentStatus = "";
//        AaiResponse<OperationalEnvironmentList> result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetFullSubscriberList_1() throws Exception {
//        AaiServiceImpl testSubject;
//        AaiResponse<SubscriberList> result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getFullSubscriberList();
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetSubscriberData() throws Exception {
//        AaiServiceImpl testSubject;
//        String subscriberId = "";
//        RoleValidator roleValidator = null;
//        AaiResponse result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getSubscriberData(subscriberId, roleValidator);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetServiceInstanceSearchResults() throws Exception {
//        AaiServiceImpl testSubject;
//        String subscriberId = "";
//        String instanceIdentifier = "";
//        RoleValidator roleValidator = null;
//        List<String> owningEntities = null;
//        List<String> projects = null;
//        AaiResponse result;
//
//        // test 1
//        testSubject = createTestSubject();
//        subscriberId = null;
//        instanceIdentifier = null;
//        result = testSubject.getServiceInstanceSearchResults(subscriberId, instanceIdentifier, roleValidator,
//                owningEntities, projects);
//        Assert.assertNotEquals(null, result);
//
//        /*/ test 2
//        testSubject = createTestSubject();
//        subscriberId = "";
//        instanceIdentifier = null;
//        result = testSubject.getServiceInstanceSearchResults(subscriberId, instanceIdentifier, roleValidator,
//                owningEntities, projects);
//        Assert.assertNotEquals(null, result);
//
//        // test 3
//        testSubject = createTestSubject();
//        instanceIdentifier = null;
//        subscriberId = null;
//        result = testSubject.getServiceInstanceSearchResults(subscriberId, instanceIdentifier, roleValidator,
//                owningEntities, projects);
//        Assert.assertEquals(null, result);
//
//        // test 4
//        testSubject = createTestSubject();
//        instanceIdentifier = "";
//        subscriberId = null;
//        result = testSubject.getServiceInstanceSearchResults(subscriberId, instanceIdentifier, roleValidator,
//                owningEntities, projects);
//        Assert.assertEquals(null, result);
//
//        // test 5
//        testSubject = createTestSubject();
//        owningEntities = null;
//        result = testSubject.getServiceInstanceSearchResults(subscriberId, instanceIdentifier, roleValidator,
//                owningEntities, projects);
//        Assert.assertEquals(null, result);
//
//        // test 6
//        testSubject = createTestSubject();
//        projects = null;
//        result = testSubject.getServiceInstanceSearchResults(subscriberId, instanceIdentifier, roleValidator,
//                owningEntities, projects);
//        Assert.assertEquals(null, result);*/
//    }
//
//    @Test
//    public void testGetVersionByInvariantId() throws Exception {
//        AaiServiceImpl testSubject;
//        List<String> modelInvariantId = null;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            testSubject.getVersionByInvariantId(modelInvariantId);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetSpecificPnf() throws Exception {
//        AaiServiceImpl testSubject;
//        String pnfId = "";
//        AaiResponse<Pnf> result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getSpecificPnf(pnfId);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetPNFData() throws Exception {
//        AaiServiceImpl testSubject;
//        String globalCustomerId = "";
//        String serviceType = "";
//        String modelVersionId = "";
//        String modelInvariantId = "";
//        String cloudRegion = "";
//        String equipVendor = "";
//        String equipModel = "";
//        AaiResponse result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getPNFData(globalCustomerId, serviceType, modelVersionId, modelInvariantId,
//                    cloudRegion, equipVendor, equipModel);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetServices() throws Exception {
//        AaiServiceImpl testSubject;
//        RoleValidator roleValidator = null;
//        AaiResponse result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getServices(roleValidator);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetTenants() throws Exception {
//        AaiServiceImpl testSubject;
//        String globalCustomerId = "";
//        String serviceType = "";
//        RoleValidator roleValidator = null;
//        AaiResponse<GetTenantsResponse[]> result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getTenants(globalCustomerId, serviceType, roleValidator);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetVNFData() throws Exception {
//        AaiServiceImpl testSubject;
//        String globalSubscriberId = "";
//        String serviceType = "";
//        String serviceInstanceId = "";
//        AaiResponse result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getVNFData(globalSubscriberId, serviceType, serviceInstanceId);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetVNFData_1() throws Exception {
//        AaiServiceImpl testSubject;
//        String globalSubscriberId = "";
//        String serviceType = "";
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            testSubject.getVNFData(globalSubscriberId, serviceType);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetAaiZones() throws Exception {
//        AaiServiceImpl testSubject;
//        AaiResponse result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getAaiZones();
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetAicZoneForPnf() throws Exception {
//        AaiServiceImpl testSubject;
//        String globalCustomerId = "";
//        String serviceType = "";
//        String serviceId = "";
//        AaiResponse result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getAicZoneForPnf(globalCustomerId, serviceType, serviceId);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetNodeTemplateInstances() throws Exception {
//        AaiServiceImpl testSubject;
//        String globalCustomerId = "";
//        String serviceType = "";
//        String modelVersionId = "";
//        String modelInvariantId = "";
//        String cloudRegion = "";
//        AaiResponse result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId,
//                    modelInvariantId, cloudRegion);
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetServicesByDistributionStatus() throws Exception {
//        AaiServiceImpl testSubject;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            testSubject.getServicesByDistributionStatus();
//        } catch (
//
//        Exception e) {
//        }
//    }
//
//    @Test
//    public void testGetServiceInstanceAssociatedPnfs() throws Exception {
//        AaiServiceImpl testSubject;
//        String globalCustomerId = "";
//        String serviceType = "";
//        String serviceInstanceId = "";
//        List<String> result;
//
//        // default test
//        try {
//            testSubject = createTestSubject();
//            result = testSubject.getServiceInstanceAssociatedPnfs(globalCustomerId, serviceType, serviceInstanceId);
//        } catch (
//
//        Exception e) {
//        }
//    }
}