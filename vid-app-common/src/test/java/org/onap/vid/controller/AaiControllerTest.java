package org.onap.vid.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.model.VersionByInvariantIdsRequest;
import org.onap.vid.services.AaiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AaiControllerTest {

    private AaiController createTestSubject() {
        return new AaiController();
    }

    @InjectMocks
    AaiController aaiController = new AaiController();

    @Mock
    AaiService aaiService;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @org.testng.annotations.Test
    public void getPortMirroringConfigData_givenThreeIds_ReturnsThreeResults() {

        final AaiResponseTranslator.PortMirroringConfigDataOk toBeReturnedForA = new AaiResponseTranslator.PortMirroringConfigDataOk("foobar");
        final AaiResponseTranslator.PortMirroringConfigDataError toBeReturnedForB = new AaiResponseTranslator.PortMirroringConfigDataError("foo", "{ baz: qux }");
        final AaiResponseTranslator.PortMirroringConfigDataOk toBeReturnedForC = new AaiResponseTranslator.PortMirroringConfigDataOk("corge");

        Mockito
                .doReturn(toBeReturnedForA)
                .doReturn(toBeReturnedForB)
                .doReturn(toBeReturnedForC)
                .when(aaiService).getPortMirroringConfigData(Mockito.anyString());

        final Map<String, AaiResponseTranslator.PortMirroringConfigData> result = aaiController.getPortMirroringConfigsData(ImmutableList.of("a", "b", "c"));

        assertThat(result, is(ImmutableMap.of(
                "a", toBeReturnedForA,
                "b", toBeReturnedForB,
                "c", toBeReturnedForC
        )));
    }



    @Test
    public void testWelcome() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        ModelAndView result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.welcome(request);
    }

    @Test
    public void testGetTargetProvStatus() throws Exception {
        AaiController testSubject;
        ResponseEntity<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getTargetProvStatus();
    }

    @Test
    public void testViewEditGetTenantsFromServiceType() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        String globalCustomerId = "";
        String serviceType = "";
        ResponseEntity<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.viewEditGetTenantsFromServiceType(request, globalCustomerId, serviceType);
    }

    
    
    
    
    @Test
    public void testGetAicZones() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getAicZones(request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetAicZoneForPnf() throws Exception {
        AaiController testSubject;
        String globalCustomerId = "";
        String serviceType = "";
        String serviceId = "";
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getAicZoneForPnf(globalCustomerId, serviceType, serviceId, request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetUserID() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getUserID(request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDoGetServices() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.doGetServices(request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetVersionByInvariantId() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        VersionByInvariantIdsRequest versions = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getVersionByInvariantId(request, versions);
        } catch (Exception e) {
        }
    }

    // @Test
    // public void testAaiResponseToResponseEntity() throws Exception {
    // AaiController testSubject;AaiResponse aaiResponseData = null;
    // ResponseEntity<String> result;
    //
    // // default test
    // }

    @Test
    public void testDoGetServiceInstance() throws Exception {
        AaiController testSubject;
        String serviceInstanceId = "";
        String serviceInstanceType = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.doGetServiceInstance(serviceInstanceId, serviceInstanceType);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDoGetServices_1() throws Exception {
        AaiController testSubject;
        String globalCustomerId = "";
        String serviceSubscriptionId = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.doGetServices(globalCustomerId, serviceSubscriptionId);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDoGetSubscriberList() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        String fullSet = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.doGetSubscriberList(request, fullSet);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetOperationalEnvironments() throws Exception {
        AaiController testSubject;
        String operationalEnvironmentType = "";
        String operationalEnvironmentStatus = "";
        AaiResponse<OperationalEnvironmentList> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetFullSubscriberList() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getFullSubscriberList(request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetVnfDataByGlobalIdAndServiceType() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        String globalCustomerId = "";
        String serviceType = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getVnfDataByGlobalIdAndServiceType(request, globalCustomerId, serviceType);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDoRefreshSubscriberList() throws Exception {
        AaiController testSubject;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.doRefreshSubscriberList();
        } catch (Exception e) {
        }
    }

    @Test
    public void testDoRefreshFullSubscriberList() throws Exception {
        AaiController testSubject;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.doRefreshFullSubscriberList();
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetSubscriberDetails() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        String subscriberId = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.GetSubscriberDetails(request, subscriberId);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSearchServiceInstances() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        String subscriberId = "";
        String instanceIdentifier = "";
        List<String> projects = null;
        List<String> owningEntities = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.SearchServiceInstances(request, subscriberId, instanceIdentifier, projects,
                    owningEntities);
        } catch (Exception e) {
        }
    }

    @Test
    public void testViewEditGetComponentList() throws Exception {
        AaiController testSubject;
        String namedQueryId = "";
        String globalCustomerId = "";
        String serviceType = "";
        String serviceInstance = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.viewEditGetComponentList(namedQueryId, globalCustomerId, serviceType, serviceInstance);
        } catch (Exception e) {
        }
    }

    @Test
    public void testViewEditGetComponentList_1() throws Exception {
        AaiController testSubject;
        String namedQueryId = "";
        String globalCustomerId = "";
        String serviceType = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.viewEditGetComponentList(namedQueryId, globalCustomerId, serviceType);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetNodeTemplateInstances() throws Exception {
        AaiController testSubject;
        String globalCustomerId = "";
        String serviceType = "";
        String modelVersionId = "";
        String modelInvariantId = "";
        String cloudRegion = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId,
                    modelInvariantId, cloudRegion);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetByUri() throws Exception {
        AaiController testSubject;
        HttpServletRequest request = null;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getByUri(request);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetSpecificConfiguration() throws Exception {
        AaiController testSubject;
        String configurationId = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getSpecificConfiguration(configurationId);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetServiceInstanceAssociatedPnfs() throws Exception {
        AaiController testSubject;
        String globalCustomerId = "";
        String serviceType = "";
        String serviceInstanceId = "";
        List<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getServiceInstanceAssociatedPnfs(globalCustomerId, serviceType, serviceInstanceId);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetSpecificPnf() throws Exception {
        AaiController testSubject;
        String pnfId = "";
        ResponseEntity result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getSpecificPnf(pnfId);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetPnfInstances() throws Exception {
        AaiController testSubject;
        String globalCustomerId = "";
        String serviceType = "";
        String modelVersionId = "";
        String modelInvariantId = "";
        String cloudRegion = "";
        String equipVendor = "";
        String equipModel = "";
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getPnfInstances(globalCustomerId, serviceType, modelVersionId, modelInvariantId,
                    cloudRegion, equipVendor, equipModel);
        } catch (Exception e) {
        }

    }

}