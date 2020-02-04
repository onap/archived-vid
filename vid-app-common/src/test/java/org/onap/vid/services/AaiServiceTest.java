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

package org.onap.vid.services;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.ServiceInstance;
import org.onap.vid.aai.model.AaiGetPnfResponse;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.LogicalLinkResponse;
import org.onap.vid.aai.model.OwningEntityResponse;
import org.onap.vid.aai.model.ProjectResponse;
import org.onap.vid.aai.model.Relationship;
import org.onap.vid.aai.model.RelationshipData;
import org.onap.vid.aai.model.RelationshipList;
import org.onap.vid.aai.model.ServiceRelationships;
import org.onap.vid.model.ServiceInstanceSearchResult;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.roles.RoleValidatorFactory;
import org.onap.vid.roles.WithPermissionProperties;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AaiServiceTest {

    @InjectMocks
    private AaiServiceImpl aaiService;

    @Mock
    private AaiClientInterface aaiClientInterface;

    @Mock
    private RoleValidatorFactory roleValidatorFactory;

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
                {"customer1", "serviceType1", "tenant2", "customer1", "serviceType1", "tenant1", "tenant2", false},
                {"customer1", "serviceType1", null, "customer1", "serviceType1", "tenant1", "tenant2", true},
                {"customer2", "serviceType1", "tenant1", "customer1", "serviceType1", "tenant1", "id-1", false},
                {"customer1", "serviceType2", "tenant1", "customer1", "serviceType1", "tenant1", "id-1", false},
                {"customer2", "serviceType1", null, "customer1", "serviceType1", "tenant1", "id-1", false},
                {"customer1", "serviceType2", null, "customer1", "serviceType1", "tenant1", "id-1", false},
        };
    }

    @Test(dataProvider = "getTenantsData")
    public void testGetTenants(String userGlobalCustomerId, String userServiceType, String userTenantName,
                                String serviceGlobalCustomerId, String serviceServiceType, String serviceTenantName,
                                String serviceTenantId, boolean expectedIsPermitted) {
        GetTenantsResponse[] getTenantsResponses = new GetTenantsResponse[] {new GetTenantsResponse(null, null, serviceTenantName, serviceTenantId, false)};
        AaiResponse<GetTenantsResponse[]> aaiResponse = new AaiResponse<>(getTenantsResponses, null, 200);
        Mockito.doReturn(aaiResponse).when(aaiClientInterface).getTenants(serviceGlobalCustomerId, serviceServiceType);

        RoleValidator roleValidatorMock = mock(RoleValidator.class);
        when(roleValidatorMock.isTenantPermitted(
            eq(userGlobalCustomerId), eq(userServiceType),
            (userTenantName == null) ? anyString() : eq(userTenantName))
        ).thenReturn(true);

        AaiResponse<GetTenantsResponse[]> actualTenants = aaiService.getTenants(serviceGlobalCustomerId, serviceServiceType, roleValidatorMock);

        assertThat(actualTenants.getT(), arrayWithSize(1));
        assertThat(actualTenants.getT()[0].tenantName, equalTo(serviceTenantName));
        assertThat(actualTenants.getT()[0].isPermitted, equalTo(expectedIsPermitted));
    }

    @DataProvider
    public static Object[][] instanceGroupsRoleAndType() {
        return new Object[][]{
                {"group role & type are same as requested", ImmutableMap.of("SERVICE-ACCESS", "LOAD-GROUP"), false},
                {"1 group properties is same as requested, 1 is not same", ImmutableMap.of("SERVICE-ACCESS", "LOAD-GROUP",
                                                                    "dummy", "dummy"), false},
                {"only group type is same as requested", ImmutableMap.of("dummy", "LOAD-GROUP"), true},
                {"only group role is same as requested", ImmutableMap.of("SERVICE-ACCESS", "dummy"), true},
                {"group role & type are not same as requested", ImmutableMap.of("dummy", "dummy"), true},
                {"vnf is not related to instance group", ImmutableMap.of(), true},
        };
    }

    @Test(dataProvider = "instanceGroupsRoleAndType")
    public void testFilterInstanceGroupByRoleAndType(String description, Map<String, String> instanceGroupsProperties, boolean expectedMatch) {
        List<AAITreeNode> instanceGroups = new ArrayList<>();

        for (Map.Entry<String, String> props: instanceGroupsProperties.entrySet()) {
            AAITreeNode instanceGroup = new AAITreeNode();
            Map<String, Object> additionalProperties = ImmutableMap.of(
                    "instance-group-role", props.getKey(),
                    "instance-group-type", props.getValue());
            instanceGroup.setAdditionalProperties(additionalProperties);
            instanceGroups.add(instanceGroup);
        }

        boolean anyMatch = aaiService.isInstanceGroupsNotMatchRoleAndType(instanceGroups, "SERVICE-ACCESS", "LOAD-GROUP");
        assertThat(anyMatch, equalTo(expectedMatch));
    }

    @DataProvider
    public static Object[][] dataToDestroy() {
        return new Object[][]{
            {"nothing"}, {"relationship-list"}, {"relationship"}, {"relationship-data"} ,{"owning-entity-id"}
        };
    }


    @Test(dataProvider = "dataToDestroy")
    public void relatedOwningEntityId_givenInstanceAndOptionalError_extractCorrectlyOrReturnNull(String dataToDestroy) throws JsonProcessingException {
        ServiceInstance serviceInstance = new ObjectMapper().readValue((""
            + "{ "
            + "  \"service-instance-id\": \"5d521981-33be-4bb5-bb20-5616a9c52a5a\", "
            + "  \"service-instance-name\": \"dfgh\", "
            + "  \"service-type\": \"\", "
            + "  \"service-role\": \"\", "
            + "  \"environment-context\": \"null\", "
            + "  \"workload-context\": \"null\", "
            + "  \"model-invariant-id\": \"331a194d-9248-4533-88bc-62c812ccb5c1\", "
            + "  \"model-version-id\": \"171b3887-e73e-479d-8ef8-2690bf74f2aa\", "
            + "  \"resource-version\": \"1508832105498\", "
            + "  \"orchestration-status\": \"Active\", "
            + "  \"relationship-list\": { "
            + "    \"relationship\": [ "
            + "      { "
            + "        \"related-to\": \"project\", "
            + "        \"related-link\": \"/aai/v11/business/projects/project/Kennedy\", "
            + "        \"relationship-data\": [ "
            + "          { "
            + "            \"relationship-key\": \"project.project-name\", "
            + "            \"relationship-value\": \"Kennedy\" "
            + "          } "
            + "        ] "
            + "      }, "
            + "      { "
            + "        \"related-to\": \"owning-entity\", "
            + "        \"related-link\": \"/aai/v11/business/owning-entities/owning-entity/4d4ecf59-41f1-40d4-818d-885234680a42\", "
            + "        \"relationship-data\": [ "
            + "          { "
            + "            \"relationship-key\": \"owning-entity.owning-entity-id\", "
            + "            \"relationship-value\": \"4d4ecf59-41f1-40d4-818d-885234680a42\" "
            + "          } "
            + "        ] "
            + "      } "
            + "    ] "
            + "  } "
            + "}").replace(dataToDestroy, "omitted"), ServiceInstance.class);

        if (dataToDestroy.equals("nothing")) {
            assertThat(aaiService.relatedOwningEntityId(serviceInstance), is("4d4ecf59-41f1-40d4-818d-885234680a42"));
        } else {
            assertThat(aaiService.relatedOwningEntityId(serviceInstance), is(nullValue()));
        }
    }

    @Test
    public void testGetServicesByOwningEntityId() {

        //given
        List<String>  owningEntityIds = ImmutableList.of("43b8a85a-0421-4265-9069-117dd6526b8a", "26dcc4aa-725a-447d-8346-aa26dfaa4eb7");
        OwningEntityResponse owningEntityResponse = TestUtils.readJsonResourceFileAsObject("/responses/aai/listServicesByOwningEntity.json", OwningEntityResponse.class);
        when(aaiClientInterface.getServicesByOwningEntityId(owningEntityIds)).thenReturn(new AaiResponse<>(owningEntityResponse, "", 200));
        RoleValidator roleValidator = createAlwaysTrueRoleValidator();

        //when
        List<ServiceInstanceSearchResult> result = aaiService.getServicesByOwningEntityId(owningEntityIds, roleValidator);

        //then
        ServiceInstanceSearchResult expected1 = new ServiceInstanceSearchResult(
            "af9d52f9-13b2-4657-a198-463677f82dc0", "256cddb4-3aa1-43cc-a08f-315bb50b275e", "MSO-dev-service-type", "xbghrftgr_shani", null, null, null, "43b8a85a-0421-4265-9069-117dd6526b8a", true);
        ServiceInstanceSearchResult expected2 = new ServiceInstanceSearchResult(
            "49769492-5def-4c89-8e73-b236f958fa40", "e02fd6f2-7fc2-434b-a92d-15abdb24b68d", "JUST-another-service-type", "fghghfhgf",  null, null, null, "43b8a85a-0421-4265-9069-117dd6526b8a", true);
        ServiceInstanceSearchResult expected3 = new ServiceInstanceSearchResult(
            "1d8fd482-2f53-4d62-a7bd-20e4bab14c45", "256cddb4-3aa1-43cc-a08f-315bb50b275e", "MSO-dev-service-type", "Bryant", null, null, null, "26dcc4aa-725a-447d-8346-aa26dfaa4eb7", true);

        assertThat(result, jsonEquals(ImmutableList.of(expected1, expected2, expected3)).when(IGNORING_ARRAY_ORDER).whenIgnoringPaths("[*].subscriberName")); //ignore in array
    }

    @NotNull
    private RoleValidator createAlwaysTrueRoleValidator() {
        RoleValidator roleValidator  = mock(RoleValidator.class);
        when(roleValidator.isServicePermitted(any(WithPermissionProperties.class))).thenReturn(true);
        return roleValidator;
    }

    @Test
    public void testGetServicesByProjectNames() {

        //given
        List<String>  projectNames = ImmutableList.of("x1", "y2");
        ProjectResponse projectResponse = TestUtils.readJsonResourceFileAsObject("/responses/aai/listServicesByProject.json", ProjectResponse.class);
        when(aaiClientInterface.getServicesByProjectNames(projectNames)).thenReturn(new AaiResponse<>(projectResponse, "", 200));
        RoleValidator roleValidator = createAlwaysTrueRoleValidator();

        //when
        List<ServiceInstanceSearchResult> result = aaiService.getServicesByProjectNames(projectNames, roleValidator);

        //then
        ServiceInstanceSearchResult expected1 = new ServiceInstanceSearchResult(
            "3f826016-3ac9-4928-9561-beee75fd91d5", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", "Emanuel", "Lital_SRIOV2_001", null, null, null, null, true);
        ServiceInstanceSearchResult expected2 = new ServiceInstanceSearchResult(
            "7e4f8130-5dee-47c4-8770-1abc5f5ded83", "3d15d7ea-4174-49b6-89ec-e569381f7231", "vMOG", "justAname",  null, null, null, null, true);
        ServiceInstanceSearchResult expected3 = new ServiceInstanceSearchResult(
            "ff2d9326-1ef5-4760-aba0-0eaf372ae675", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", "Yoda", "anotherName", null, null, null, null, true);

        assertThat(result, jsonEquals(ImmutableList.of(expected1, expected2, expected3)).when(IGNORING_ARRAY_ORDER).whenIgnoringPaths("[*].subscriberName")); //ignore in array
    }

}
