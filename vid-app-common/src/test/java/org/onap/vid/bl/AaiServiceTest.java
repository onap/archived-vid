package org.onap.vid.bl;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.model.*;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.services.AaiServiceImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AaiServiceTest {

    @InjectMocks
    private AaiServiceImpl aaiService;

    @Mock
    private AaiClientInterface aaiClientInterface;



    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSpecificPnf(){
        Pnf pnf = new Pnf();
        pnf.setJsonPnfId("11111");
        AaiResponse<Pnf> aaiResponse = new AaiResponse<>(pnf, "aaaa", 200);
        Mockito.doReturn(aaiResponse).when(aaiClientInterface).getSpecificPnf(Mockito.anyString());
        AaiResponse<Pnf> specificPnf = aaiService.getSpecificPnf("1345667");
        assertNotNull(specificPnf);
        pnf = specificPnf.getT();
        assertNotNull(pnf);
        assertEquals("11111",pnf.getPnfId());
        assertEquals("aaaa",specificPnf.getErrorMessage());
        assertEquals(200,specificPnf.getHttpCode());
    }

    @Test
    public void testPnfByRegion(){
        AaiGetPnfResponse aaiGetPnfResponse = new AaiGetPnfResponse();
        AaiResponse<AaiGetPnfResponse> aaiResponse = new AaiResponse<>(aaiGetPnfResponse, "", 200);
        Mockito.doReturn(aaiResponse).when(aaiClientInterface).getPNFData(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        AaiResponse<AaiGetPnfResponse> aaiGetPnfResponseWrapper = aaiService.getPNFData("1345667", "1345667", "1345667", "1345667", "1345667", "1345667", "1345667");
        assertNotNull(aaiGetPnfResponseWrapper);
        aaiGetPnfResponse = aaiGetPnfResponseWrapper.getT();
        assertNotNull(aaiGetPnfResponse);
    }

    @Test
    public void testGetAssociatedPnfs(){
        ServiceRelationships serviceRelationships = createServiceRelationships();
        AaiResponse<ServiceRelationships> aaiResponse = new AaiResponse<>(serviceRelationships, null, 200);
        Mockito.doReturn(aaiResponse).when(aaiClientInterface).getServiceInstance(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        LogicalLinkResponse logicalLinkResponse = createLogicalLinkResponse();
        AaiResponse<LogicalLinkResponse> aaiResponse1 = new AaiResponse<>(logicalLinkResponse, null, 200);
        Mockito.doReturn(aaiResponse1).when(aaiClientInterface).getLogicalLink("SANITY6758cce9%3ALAG1992%7CSANITY6785cce9%3ALAG1961");

        List<String> pnfList = aaiService.getServiceInstanceAssociatedPnfs("123", "456", "789");
        assertNotNull(pnfList);
        assertEquals(1, pnfList.size());
        assertEquals("SANITY6785cce9", pnfList.get(0));
    }

    private ServiceRelationships createServiceRelationships() {
        ServiceRelationships serviceRelationships = new ServiceRelationships();
        serviceRelationships.setJsonServiceInstanceName("test service");

        RelationshipData logicalLinksRelationshipData = new RelationshipData();
        logicalLinksRelationshipData.setJsonRelationshipKey("logical-link.link-name");
        logicalLinksRelationshipData.setJsonRelationshipValue("SANITY6758cce9:LAG1992|SANITY6785cce9:LAG1961");

        Relationship logicalLinksRelationship = new Relationship();
        logicalLinksRelationship.setJsonRelatedTo("logical-link");
        logicalLinksRelationship.setJsonRelationDataList(Arrays.asList(logicalLinksRelationshipData));

        RelationshipList logicalLinksRelationshipsList = new RelationshipList();
        logicalLinksRelationshipsList.setRelationship(Arrays.asList(logicalLinksRelationship));

        serviceRelationships.setJsonRelationshipList(logicalLinksRelationshipsList);
        return serviceRelationships;
    }

    private LogicalLinkResponse createLogicalLinkResponse() {
        LogicalLinkResponse logicalLinkResponse = new LogicalLinkResponse();
        logicalLinkResponse.setJsonLinkName("SANITY6758cce9:LAG1992|SANITY6785cce9:LAG1961");

        RelationshipData lagInterfaceRelationshipData = new RelationshipData();
        lagInterfaceRelationshipData.setJsonRelationshipKey("pnf.pnf-name");
        lagInterfaceRelationshipData.setJsonRelationshipValue("SANITY6785cce9");

        Relationship lagInterfaceRelationship = new Relationship();
        lagInterfaceRelationship.setJsonRelatedTo("lag-interface");
        lagInterfaceRelationship.setJsonRelationDataList(Arrays.asList(lagInterfaceRelationshipData));

        RelationshipList lagInterfaceRelationshipsList = new RelationshipList();
        lagInterfaceRelationshipsList.setRelationship(Arrays.asList(lagInterfaceRelationship));

        logicalLinkResponse.setJsonRelationshipList(lagInterfaceRelationshipsList);

        return logicalLinkResponse;
    }

    @DataProvider
    public static Object[][] getTenantsData() {
        return new Object[][] {
                {"customer1", "serviceType1", "tenant1", "customer1", "serviceType1", "tenant1", "id-1", true},
                {"customer1", "serviceType1", "TeNant1", "customer1", "serviceType1", "tenant1", "id-1", true},
                {"customer1", "serviceType1", "TENANT1", "customer1", "serviceType1", "tenant1", "id-1", true},
                {"customer1", "serviceType1", "tenant2", "customer1", "serviceType1", "tenant1", "tenant2", false},
                {"customer1", "serviceType1", null, "customer1", "serviceType1", "tenant1", "tenant2", true},
                {"customer2", "serviceType1", "tenant1", "customer1", "serviceType1", "tenant1", "id-1", false},
                {"customer1", "serviceType2", "tenant1", "customer1", "serviceType1", "tenant1", "id-1", false},
                {"customer2", "serviceType1", null, "customer1", "serviceType1", "tenant1", "id-1", false},
                {"customer1", "serviceType2", null, "customer1", "serviceType1", "tenant1", "id-1", false},
        };
    }

    @Test(dataProvider = "getTenantsData")
    public void testGetTenants(String userGlobalCustomerId, String userServiceType, String userTenantName, String serviceGlobalCustomerId,
                               String serviceServiceType, String serviceTenantName, String serviceTenantId, boolean expectedIsPermitted) {
        GetTenantsResponse[] getTenantsResponses = new GetTenantsResponse[] {new GetTenantsResponse(null, null, serviceTenantName, serviceTenantId, expectedIsPermitted)};
        AaiResponse<GetTenantsResponse[]> aaiResponse = new AaiResponse<>(getTenantsResponses, null, 200);
        Mockito.doReturn(aaiResponse).when(aaiClientInterface).getTenants(serviceGlobalCustomerId, serviceServiceType);
        Role role = new Role(null, userGlobalCustomerId, userServiceType, userTenantName);
        RoleValidator roleValidator = new RoleValidator(Collections.singletonList(role));
        AaiResponse<GetTenantsResponse[]> actualTenants = aaiService.getTenants(serviceGlobalCustomerId, serviceServiceType, roleValidator);

        assertThat(actualTenants.getT(), arrayWithSize(1));
        assertThat(actualTenants.getT()[0].tenantName, equalTo(serviceTenantName));
        //assertThat(actualTenants.getT()[0].isPermitted, equalTo(expectedIsPermitted));
    }
}
