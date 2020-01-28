/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.bl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.model.AaiGetPnfResponse;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.LogicalLinkResponse;
import org.onap.vid.aai.model.Relationship;
import org.onap.vid.aai.model.RelationshipData;
import org.onap.vid.aai.model.RelationshipList;
import org.onap.vid.aai.model.ServiceRelationships;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.services.AaiServiceImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        Pnf pnf = Pnf.builder().withPnfId("11111").build();
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
        serviceRelationships.setServiceInstanceName("test service");

        RelationshipData logicalLinksRelationshipData = new RelationshipData();
        logicalLinksRelationshipData.setRelationshipKey("logical-link.link-name");
        logicalLinksRelationshipData.setRelationshipValue("SANITY6758cce9:LAG1992|SANITY6785cce9:LAG1961");

        Relationship logicalLinksRelationship = new Relationship();
        logicalLinksRelationship.setRelatedTo("logical-link");
        logicalLinksRelationship.setRelationDataList(Arrays.asList(logicalLinksRelationshipData));

        RelationshipList logicalLinksRelationshipsList = new RelationshipList();
        logicalLinksRelationshipsList.setRelationship(Arrays.asList(logicalLinksRelationship));

        serviceRelationships.setRelationshipList(logicalLinksRelationshipsList);
        return serviceRelationships;
    }

    private LogicalLinkResponse createLogicalLinkResponse() {
        LogicalLinkResponse logicalLinkResponse = new LogicalLinkResponse();
        logicalLinkResponse.setLinkName("SANITY6758cce9:LAG1992|SANITY6785cce9:LAG1961");

        RelationshipData lagInterfaceRelationshipData = new RelationshipData();
        lagInterfaceRelationshipData.setRelationshipKey("pnf.pnf-name");
        lagInterfaceRelationshipData.setRelationshipValue("SANITY6785cce9");

        Relationship lagInterfaceRelationship = new Relationship();
        lagInterfaceRelationship.setRelatedTo("lag-interface");
        lagInterfaceRelationship.setRelationDataList(Arrays.asList(lagInterfaceRelationshipData));

        RelationshipList lagInterfaceRelationshipsList = new RelationshipList();
        lagInterfaceRelationshipsList.setRelationship(Arrays.asList(lagInterfaceRelationship));

        logicalLinkResponse.setRelationshipList(lagInterfaceRelationshipsList);

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
        Role role = new Role(null, userGlobalCustomerId, userServiceType, userTenantName, "");
        RoleValidator roleValidator = RoleValidator.by(Collections.singletonList(role));
        AaiResponse<GetTenantsResponse[]> actualTenants = aaiService.getTenants(serviceGlobalCustomerId, serviceServiceType, roleValidator);

        assertThat(actualTenants.getT(), arrayWithSize(1));
        assertThat(actualTenants.getT()[0].tenantName, equalTo(serviceTenantName));
        //assertThat(actualTenants.getT()[0].isPermitted, equalTo(expectedIsPermitted));
    }
}
