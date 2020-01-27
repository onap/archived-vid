/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.model.aaiTree.NodeType.SERVICE_INSTANCE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiGetVnfResponse;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.ServiceInstancesSearchResults;
import org.onap.vid.aai.ServiceSubscription;
import org.onap.vid.aai.ServiceSubscriptions;
import org.onap.vid.aai.Services;
import org.onap.vid.aai.SubscriberFilteredResults;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetRelatedInstanceGroupsByVnfId;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelatedToProperty;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.GetServiceModelsByDistributionStatusResponse;
import org.onap.vid.aai.model.InstanceGroupInfo;
import org.onap.vid.aai.model.LogicalLinkResponse;
import org.onap.vid.aai.model.Model;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.aai.model.ModelVers;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.model.Properties;
import org.onap.vid.aai.model.Relationship;
import org.onap.vid.aai.model.RelationshipData;
import org.onap.vid.aai.model.RelationshipList;
import org.onap.vid.aai.model.Result;
import org.onap.vid.aai.model.ServiceRelationships;
import org.onap.vid.aai.model.VnfResult;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.RelatedVnf;
import org.onap.vid.model.aaiTree.ServiceInstance;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;

@RunWith(MockitoJUnitRunner.class)
public class AaiServiceImplTest {

    private static final String GLOBAL_CUSTOMER_ID = "GLOBAL_CUSTOMER_ID";
    private static final String CLOUD_REGION_ID = "CLOUD_REGION_ID";
    private static final String VNF_TYPE = "VNF_TYPE";
    private static final String TENANT_ID = "TENANT_ID";
    private static final String TENANT_NAME = "TENANT_NAME";
    private static final String SERVICE_TYPE = "SERVICE_TYPE";
    private static final String CORRECT_VALUE = "CORRECT_VALUE";
    private static final String SUBSCRIBER_ID = "SUBSCRIBER_ID_EXPECTED";
    private static final String STATUS_TEXT = "STATUS_TEXT";
    private static final String GLOBAL_SUBSCRIBER_ID = "GLOBAL_SUBSCRIBER_ID";
    private static final String VNF_INSTANCE_ID_OK = "VNF_INSTANCE_ID_OK";
    private static final String VNF_INSTANCE_ID_FAIL = "VNF_INSTANCE_ID_FAIL";
    private static final String PARENT_NAME = "PARENT_NAME";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String INVARIANT_ID = "INVARIANT_ID";
    private static final String GROUP_TYPE_FAILING = "GROUP_TYPE_FAILING";
    private static final String GROUP_ROLE_OK = "GROUP_ROLE_OK";
    private static final String GROUP_ROLE_FAILING = "GROUP_ROLE_FAILING";
    private static final String group_type_ok = "GROUP_TYPE_OK";
    private static final String CLOUD_TYPE = "CLOUD_TYPE";

    @Mock
    private AaiResponse<SubscriberList> responseAllSubscribers;
    @Mock
    private AaiResponse<OperationalEnvironmentList> aaiResponseOpEnvList;
    @Mock
    private AaiResponse aaiResponse;
    @Mock
    private AaiResponse<JsonNode> aaiResponseJsonNode;
    @Mock
    private RoleValidator roleValidator;

    @Mock
    private AaiClientInterface aaiClient;

    @Mock
    private AaiResponseTranslator aaiResponseTranslator;
    @Mock
    private Logging logging;
    @Mock
    private AAIServiceTree aaiServiceTree;
    @Spy
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    @InjectMocks
    private AaiServiceImpl aaiService;

    @Test
    public void shouldGetFullSubscriberListWithoutValidator() {
        when(aaiClient.getAllSubscribers()).thenReturn(responseAllSubscribers);
        assertThat(aaiService.getFullSubscriberList()).isEqualTo(responseAllSubscribers);
    }

    @Test
    public void shouldGetFullSubscriberListWithValidator() {
        Subscriber subscriber = createSubscriber();
        SubscriberList subscriberList = new SubscriberList(Collections.singletonList(subscriber));

        when(aaiClient.getAllSubscribers()).thenReturn(responseAllSubscribers);
        when(responseAllSubscribers.getT()).thenReturn(subscriberList);
        when(responseAllSubscribers.getErrorMessage()).thenReturn(STATUS_TEXT);
        when(responseAllSubscribers.getHttpCode()).thenReturn(HttpStatus.SC_OK);
        SubscriberFilteredResults expectedSubscribers = new SubscriberFilteredResults(roleValidator, subscriberList,
            STATUS_TEXT, HttpStatus.SC_OK);

        SubscriberFilteredResults actualSubscribers = aaiService.getFullSubscriberList(roleValidator);

        assertThat(actualSubscribers.getHttpCode()).isEqualTo(expectedSubscribers.getHttpCode());
        assertThat(actualSubscribers.getErrorMessage()).isEqualTo(expectedSubscribers.getErrorMessage());
    }

    @Test
    public void shouldGetOperationalEnvironments() {
        when(aaiClient.getOperationalEnvironments(anyString(), anyString()))
            .thenReturn(aaiResponseOpEnvList);

        AaiResponse<OperationalEnvironmentList> expectedEnvList =
            aaiService.getOperationalEnvironments(anyString(), anyString());

        assertThat(expectedEnvList).isEqualTo(aaiResponseOpEnvList);
    }

    @Test
    public void shouldGetSubscriberData() {
        Services services = createAaiResponseServices();
        AaiResponse<Services> aaiResponseServices = new AaiResponse<>(services, null, HttpStatus.SC_OK);

        when(aaiClient.getSubscriberData(SUBSCRIBER_ID, false)).thenReturn(aaiResponseServices);
        when(roleValidator.isServicePermitted(any())).thenReturn(Boolean.TRUE);

        AaiResponse actualResponse = aaiService.getSubscriberData(SUBSCRIBER_ID, roleValidator, false);
        List<ServiceSubscription> actualServiceSubscriptions = ((AaiResponse<Services>) actualResponse)
            .getT().serviceSubscriptions.serviceSubscription;

        assertThat(actualResponse).isEqualTo(aaiResponseServices);
        assertThat(actualServiceSubscriptions).allMatch(s -> s.isPermitted);
    }

    @Test
    public void shouldGetServiceInstanceEmptySearchResults() {
        ServiceInstancesSearchResults serviceInstancesSearchResults = new ServiceInstancesSearchResults();
        AaiResponse<ServiceInstancesSearchResults> emptyResponse = new AaiResponse<>(serviceInstancesSearchResults,
            null, HttpStatus.SC_OK);

        AaiResponse actualResponse = aaiService.getServiceInstanceSearchResults(null, null,
            null, null, null);

        assertThat(actualResponse).isEqualToComparingFieldByFieldRecursively(emptyResponse);
    }

    @Test
    public void shouldGetVersionByInvariantId() {
        Response response = mock(Response.class);
        when(aaiClient.getVersionByInvariantId(any())).thenReturn(response);

        Response actualResponse = aaiService.getVersionByInvariantId(any());

        assertThat(actualResponse).isEqualTo(response);
    }

    @Test
    public void shouldGetSpecificPnf() {
        AaiResponse<Pnf> expectedResponse = new AaiResponse<>(Pnf.builder().build(), null, HttpStatus.SC_OK);
        when(aaiClient.getSpecificPnf(anyString())).thenReturn(expectedResponse);

        AaiResponse<Pnf> actualResponse = aaiService.getSpecificPnf(anyString());

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldGetPnfData() {
        when(aaiClient.getPNFData(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
            anyString())).thenReturn(aaiResponse);

        AaiResponse actualResponse = aaiService.getPNFData(anyString(), anyString(), anyString(), anyString(),
            anyString(), anyString(), anyString());

        assertThat(actualResponse).isEqualTo(aaiResponse);
    }

    @Test
    public void shouldGetServices() {
        org.onap.vid.aai.model.AaiGetServicesRequestModel.Service s1 =
            createService("ID1", "V1", "D1");
        org.onap.vid.aai.model.AaiGetServicesRequestModel.Service s2 =
            createService("ID2", "V2", "D2");

        GetServicesAAIRespone services = new GetServicesAAIRespone();
        services.service = Arrays.asList(s1, s2);

        AaiResponse<GetServicesAAIRespone> aaiResponseServices =
            new AaiResponse<>(services, null, HttpStatus.SC_OK);

        when(aaiClient.getServices()).thenReturn(aaiResponseServices);

        Object actualObjectOfResponse = aaiService.getServices(roleValidator).getT();

        assertThat(actualObjectOfResponse).isNotNull();
        assertThat(((GetServicesAAIRespone) actualObjectOfResponse).service).allMatch(s -> s.isPermitted);
    }

    @Test
    public void shouldGetTenants() {
        GetTenantsResponse tenant1 = new GetTenantsResponse("REGION_ID1", "CLOUD_OWNER1",
            "TENANT_NAME1", "TENANT_ID1", true);
        GetTenantsResponse tenant2 = new GetTenantsResponse("REGION_ID2", "CLOUD_OWNER2",
            "TENANT_NAME2", "TENANT_ID2", false);
        GetTenantsResponse[] tenants = {tenant1, tenant2};
        AaiResponse<GetTenantsResponse[]> aaiGetTenantsResponse = new AaiResponse<>(tenants,
            null, HttpStatus.SC_OK);

        when(aaiClient.getTenants(anyString(), anyString())).thenReturn(aaiGetTenantsResponse);
        when(roleValidator.isTenantPermitted(anyString(), anyString(), anyString()))
            .thenReturn(Boolean.TRUE);

        GetTenantsResponse[] actualResponses = aaiService
            .getTenants(anyString(), anyString(), roleValidator).getT();

        assertThat(actualResponses).isNotNull();
        assertThat(actualResponses.length).isEqualTo(2);
        assertThat(actualResponses).allMatch(tenant -> tenant.isPermitted);
    }

    @Test
    public void shouldGetVNFDataOfInstanceWithoutFiltering() {
        when(aaiClient.getVNFData(anyString(), anyString(), anyString())).thenReturn(aaiResponse);

        AaiResponse actualResponse = aaiService.getVNFData(anyString(), anyString(), anyString());

        assertThat(actualResponse).isEqualTo(aaiResponse);
    }

    @Test
    public void shouldGetVNFDataOfServiceWithoutFiltering() {
        VnfResult vnfResult1 = createVnfResult("ID1", "generic-vnf");
        VnfResult vnfResult2 = createVnfResult("ID2", "service-instance");
        VnfResult vnfResult3 = createVnfResult("ID3", "anything-else");

        AaiResponse<AaiGetVnfResponse> aaiResponseGetVnfResponse = createAaiResponseVnfResponse(
            Arrays.asList(vnfResult1, vnfResult2, vnfResult3));

        when(aaiClient.getVNFData(GLOBAL_SUBSCRIBER_ID, SERVICE_TYPE)).thenReturn(aaiResponseGetVnfResponse);

        assertThat(aaiService.getVNFData(GLOBAL_SUBSCRIBER_ID, SERVICE_TYPE))
            .isEqualTo(aaiResponseGetVnfResponse);
    }

    @Test
    public void shouldGetNonNullVNFDataOfServiceWhenNoResult() {
        when(aaiClient.getVNFData(GLOBAL_SUBSCRIBER_ID, SERVICE_TYPE)).thenReturn(null);

        assertThat(aaiService.getVNFData(GLOBAL_SUBSCRIBER_ID, SERVICE_TYPE))
            .isEqualToComparingFieldByField(new AaiResponse());
    }

    @Test
    public void shouldGetAaiZones() {
        when(aaiClient.getAllAicZones()).thenReturn(aaiResponse);

        AaiResponse actualResponse = aaiService.getAaiZones();

        assertThat(actualResponse).isEqualTo(aaiResponse);
    }

    @Test
    public void shouldGetAicZoneForPnf() {
        ServiceRelationships relationsService = createServiceRelationships();
        AaiResponse<ServiceRelationships> expectedServiceInstanceResp =
            new AaiResponse<>(relationsService, null, HttpStatus.SC_OK);
        AaiResponse<String> expectedResponse = new AaiResponse<>(CORRECT_VALUE, null, HttpStatus.SC_OK);

        when(aaiClient.getServiceInstance(anyString(), anyString(), anyString()))
            .thenReturn(expectedServiceInstanceResp);

        AaiResponse actualResponse = aaiService.getAicZoneForPnf(anyString(), anyString(), anyString());

        assertThat(actualResponse).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void shouldGetNodeTemplateInstances() {
        when(aaiClient.getNodeTemplateInstances(anyString(), anyString(), anyString(),
            anyString(), anyString())).thenReturn(aaiResponse);

        AaiResponse expectedResponse = aaiService.getNodeTemplateInstances(anyString(), anyString(), anyString(),
            anyString(), anyString());

        assertThat(expectedResponse).isEqualTo(aaiResponse);
    }

    @Test
    public void shouldGetNetworkCollectionDetails() {
        when(aaiClient.getNetworkCollectionDetails(anyString())).thenReturn(aaiResponse);

        AaiResponse expectedResponse = aaiService.getNetworkCollectionDetails(anyString());

        assertThat(expectedResponse).isEqualTo(aaiResponse);
    }

    @Test
    public void shouldGetInstanceGroupsByCloudRegion() {
        AaiGetInstanceGroupsByCloudRegion aaiGetInstanceGroupsByCloudRegion =
            mock(AaiGetInstanceGroupsByCloudRegion.class);
        AaiResponse<AaiGetInstanceGroupsByCloudRegion> expectedResponse =
            new AaiResponse<>(aaiGetInstanceGroupsByCloudRegion, null, HttpStatus.SC_OK);

        when(aaiClient.getInstanceGroupsByCloudRegion(anyString(), anyString(), anyString()))
            .thenReturn(expectedResponse);
        AaiResponse<AaiGetInstanceGroupsByCloudRegion> actualResponse =
            aaiService.getInstanceGroupsByCloudRegion(anyString(), anyString(), anyString());

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldGetServicesByDistributionStatus() {
        Result resultWithModelType = createResult("MODEL_TYPE1", "1");
        Result resultWithEmptyModelType = createResult(null, "2");
        Result resultWithoutModel = new Result();
        resultWithoutModel.setModel(null);
        Result resultWithoutValidModel = createResultWithoutValidModel();
        List<Result> results = Arrays.asList(resultWithModelType, resultWithEmptyModelType, resultWithoutModel,
            resultWithoutValidModel);

        GetServiceModelsByDistributionStatusResponse serviceModels = new GetServiceModelsByDistributionStatusResponse();
        serviceModels.setResults(results);

        AaiResponse<GetServiceModelsByDistributionStatusResponse> serviceModelsByDistributionStatusResponse
            = new AaiResponse<>(serviceModels, null, HttpStatus.SC_OK);

        Service[] expectedServices = {
            createService("MODEL_TYPE1", "1"),
            createService("", "2")
        };

        when(aaiClient.getServiceModelsByDistributionStatus()).thenReturn(serviceModelsByDistributionStatusResponse);
        Collection<Service> actualServices = aaiService.getServicesByDistributionStatus();

        assertThat(actualServices)
            .hasSize(2)
            .usingFieldByFieldElementComparator()
            .containsExactly(expectedServices);
    }

    @Test
    public void shouldReturnEmptyListOfServices() {
        AaiResponse<GetServiceModelsByDistributionStatusResponse> emptyResponse
            = new AaiResponse<>(null, null, HttpStatus.SC_OK);

        when(aaiClient.getServiceModelsByDistributionStatus()).thenReturn(emptyResponse);
        Collection<Service> actualServices = aaiService.getServicesByDistributionStatus();

        assertThat(actualServices).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void shouldGetServiceInstanceAssociatedPnfs() {
        ServiceRelationships relationsList = createServiceRelationships();
        LogicalLinkResponse logicalLinkResponse = new LogicalLinkResponse();
        logicalLinkResponse.setRelationshipList(relationsList.getRelationshipList());

        AaiResponse<LogicalLinkResponse> aaiResponseLogicalLinkResponse =
            new AaiResponse<>(logicalLinkResponse, null, HttpStatus.SC_OK);
        AaiResponse<ServiceRelationships> aaiResponseServiceRelations =
            new AaiResponse<>(relationsList, null, HttpStatus.SC_OK);

        when(aaiClient.getServiceInstance(anyString(), anyString(), anyString()))
            .thenReturn(aaiResponseServiceRelations);
        when(aaiClient.getLogicalLink(anyString())).thenReturn(aaiResponseLogicalLinkResponse);

        List<String> expectedPnfs = Collections.singletonList(CORRECT_VALUE);
        List<String> actualPnfs = aaiService.getServiceInstanceAssociatedPnfs(anyString(), anyString(), anyString());

        assertThat(actualPnfs).isEqualTo(expectedPnfs);
    }

    @Test
    public void shouldGetPortMirroringConfigData() {
        AaiResponseTranslator.PortMirroringConfigData expectedData
            = mock(AaiResponseTranslator.PortMirroringConfigData.class);

        when(aaiClient.getCloudRegionAndSourceByPortMirroringConfigurationId(anyString()))
            .thenReturn(aaiResponseJsonNode);
        when(aaiResponseTranslator.extractPortMirroringConfigData(aaiResponseJsonNode)).thenReturn(expectedData);

        AaiResponseTranslator.PortMirroringConfigData actualData = aaiService.getPortMirroringConfigData(anyString());
        assertThat(actualData).isEqualTo(expectedData);
    }


    @Test
    public void shouldGetInstanceGroupsByVnfInstanceId() {
        List<InstanceGroupInfo> instanceGroupInfo = Collections.singletonList(new InstanceGroupInfo(CORRECT_VALUE));
        AaiGetRelatedInstanceGroupsByVnfId relatedInstanceGroups = new AaiGetRelatedInstanceGroupsByVnfId();
        relatedInstanceGroups.setRelationshipList(createRelationshipList());

        AaiResponse<AaiGetRelatedInstanceGroupsByVnfId> correctCodeResponse =
            new AaiResponse<>(relatedInstanceGroups, null, HttpStatus.SC_OK);

        AaiResponse<List<InstanceGroupInfo>> expectedCorrectCodeResponse =
            new AaiResponse<>(instanceGroupInfo, null, HttpStatus.SC_OK);
        AaiResponse<AaiGetRelatedInstanceGroupsByVnfId> expectedIncorrectCodeResponse =
            new AaiResponse<>(relatedInstanceGroups, null, HttpStatus.SC_PAYMENT_REQUIRED);
        List<InstanceGroupInfo> expectedCorrectResponseObject = expectedCorrectCodeResponse.getT();

        when(aaiClient.getInstanceGroupsByVnfInstanceId(VNF_INSTANCE_ID_OK)).thenReturn(correctCodeResponse);
        when(aaiClient.getInstanceGroupsByVnfInstanceId(VNF_INSTANCE_ID_FAIL))
            .thenReturn(expectedIncorrectCodeResponse);

        AaiResponse actualCorrectCodeResponse = aaiService.getInstanceGroupsByVnfInstanceId(VNF_INSTANCE_ID_OK);
        AaiResponse actualIncorrectCodeResponse = aaiService.getInstanceGroupsByVnfInstanceId(VNF_INSTANCE_ID_FAIL);

        List<InstanceGroupInfo> actualCorrectResponseObject =
            (List<InstanceGroupInfo>) actualCorrectCodeResponse.getT();

        assertThat(actualCorrectResponseObject)
            .usingFieldByFieldElementComparator()
            .hasSameElementsAs(expectedCorrectResponseObject);

        assertThat(actualIncorrectCodeResponse).isEqualTo(expectedIncorrectCodeResponse);
    }

    @Test
    public void shouldGetHomingDataByVfModule() {
        GetTenantsResponse expectedResponse = new GetTenantsResponse();
        when(aaiClient.getHomingDataByVfModule(anyString(), anyString())).thenReturn(expectedResponse);

        GetTenantsResponse actualResponse = aaiService.getHomingDataByVfModule(anyString(), anyString());
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldSearchGroupMembers() {
        Properties properties = createProperties();
        Map<String, Properties> regionsAndTenants = createRegionsAndTenantsMap(properties);

        AAITreeNode validTreeNode = new AAITreeNode();
        addAdditionalPropertiesToAaiTreeNode(validTreeNode);
        List<AAITreeNode> validNodes = Arrays.asList(validTreeNode, validTreeNode);

        AAITreeNode validBranch = createTree(validNodes);
        addAdditionalPropertiesToAaiTreeNode(validBranch);
        List<AAITreeNode> testedBranches = Collections.singletonList(validBranch);

        AAITreeNode testedTree = createTree(testedBranches);

        RelatedVnf expectedVnf = createExpectedVnf(validBranch);
        List<RelatedVnf> expectedResult = Collections.singletonList(expectedVnf);

        when(aaiServiceTree.buildAAITree(anyString(), isNull(), eq(HttpMethod.GET), any(), anyBoolean()))
            .thenReturn(Collections.singletonList(testedTree));
        when(aaiClient.getCloudRegionAndTenantByVnfId(anyString())).thenReturn(regionsAndTenants);
        when(logging.withMDC(any(), any(Function.class))).thenAnswer(invocation -> invocation.getArgument(1));

        List<RelatedVnf> actualGroupMembers = aaiService.searchGroupMembers(GLOBAL_CUSTOMER_ID, SERVICE_TYPE,
            INVARIANT_ID, GROUP_TYPE_FAILING, GROUP_ROLE_FAILING);

        assertThat(actualGroupMembers)
            .usingFieldByFieldElementComparator()
            .hasSameElementsAs(expectedResult);
    }

    @Test
    public void shouldGetPortMirroringSourcePorts() {
        PortDetailsTranslator.PortDetails details = mock(PortDetailsTranslator.PortDetails.class);
        List<PortDetailsTranslator.PortDetails> expectedDetailsList = Arrays.asList(
            details, details, details
        );

        when(aaiClient.getPortMirroringSourcePorts(anyString())).thenReturn(expectedDetailsList);
        List<PortDetailsTranslator.PortDetails> actualDetails = aaiService.getPortMirroringSourcePorts(anyString());

        assertThat(actualDetails).isEqualTo(expectedDetailsList);
    }

    @Test
    public void shouldGetAAIServiceTree() throws JsonProcessingException {
        ServiceInstance serviceInstance = mock(ServiceInstance.class);
        String expectedResult = new ObjectMapper().writeValueAsString(serviceInstance);

        when(aaiServiceTree.getServiceInstanceTopology(anyString(), anyString(), anyString()))
            .thenReturn(serviceInstance);
        String actualResult = aaiService.getAAIServiceTree(anyString(), anyString(), anyString());

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @NotNull
    private Map<String, Properties> createRegionsAndTenantsMap(Properties properties) {
        Map<String, Properties> regionsAndTenants = new HashMap<>();
        regionsAndTenants.put("tenant", properties);
        regionsAndTenants.put("cloud-region", properties);
        return regionsAndTenants;
    }

    private Properties createProperties() {
        Properties properties = new Properties();
        properties.setTenantId(TENANT_ID);
        properties.setTenantName(TENANT_NAME);
        properties.setCloudRegionId(CLOUD_REGION_ID);
        return properties;
    }

    @NotNull
    private RelatedVnf createExpectedVnf(AAITreeNode validBranch) {
        RelatedVnf expectedVnf = RelatedVnf.from(validBranch);
        expectedVnf.setTenantId(TENANT_ID);
        expectedVnf.setTenantName(TENANT_NAME);
        expectedVnf.setLcpCloudRegionId(CLOUD_REGION_ID);
        expectedVnf.setServiceInstanceId(PARENT_ID);
        expectedVnf.setServiceInstanceName(PARENT_NAME);
        expectedVnf.setInstanceType(VNF_TYPE);

        return expectedVnf;
    }


    private AAITreeNode createTree(List<AAITreeNode> children) {
        AAITreeNode tree = new AAITreeNode();
        tree.addChildren(children);
        tree.setId(PARENT_ID);
        tree.setName(PARENT_NAME);
        tree.setType(SERVICE_INSTANCE);
        return tree;
    }

    private void addAdditionalPropertiesToAaiTreeNode(AAITreeNode tree) {
        Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("instance-group-role", GROUP_ROLE_OK);
        additionalProperties.put("instance-group-type", group_type_ok);
        additionalProperties.put("vnf-type", VNF_TYPE);
        additionalProperties.put("cloud-region", CLOUD_TYPE);
        tree.setAdditionalProperties(additionalProperties);
    }

    private org.onap.vid.asdc.beans.Service createService(String category, String suffix) {
        return new Service.ServiceBuilder()
            .setUuid("MODELVER_VERSION_ID" + suffix)
            .setInvariantUUID("MODEL_INVARIANT_NAME" + suffix)
            .setCategory(category)
            .setVersion("MODELVER_VERSION" + suffix)
            .setName("MODELVER_NAME" + suffix)
            .setDistributionStatus("MODELVER_DIST_STATUS" + suffix)
            .setToscaModelURL(null)
            .setLifecycleState(null)
            .setArtifacts(null)
            .setResources(null)
            .build();
    }

    @NotNull
    private Result createResultWithoutValidModel() {
        ModelVers modelVers = new ModelVers();
        modelVers.setModelVer(Collections.singletonList(new ModelVer()));

        Model model = new Model();
        model.setModelVers(modelVers);

        Result result1 = new Result();
        result1.setModel(model);
        return result1;
    }

    @NotNull
    private Result createResult(String modelType, String suffix) {
        ModelVer modelVer = new ModelVer();
        modelVer.setModelVersionId("MODELVER_VERSION_ID" + suffix);
        modelVer.setModelVersion("MODELVER_VERSION" + suffix);
        modelVer.setModelName("MODELVER_NAME" + suffix);
        modelVer.setDistributionStatus("MODELVER_DIST_STATUS" + suffix);

        ModelVers modelVers = new ModelVers();
        modelVers.setModelVer(Collections.singletonList(modelVer));

        Model model = new Model();
        model.setModelType(modelType);
        model.setModelInvariantId("MODEL_INVARIANT_NAME" + suffix);
        model.setModelVers(modelVers);

        Result result = new Result();
        result.setModel(model);
        return result;
    }

    @NotNull
    private ServiceRelationships createServiceRelationships() {
        RelationshipList relationsList = createRelationshipList(CORRECT_VALUE);
        ServiceRelationships relationsService = new ServiceRelationships();
        relationsService.setRelationshipList(relationsList);
        return relationsService;
    }

    @NotNull
    private RelationshipList createRelationshipList(String expectedValue) {
        List<RelationshipData> relationsDataList = createRelationshipDataList(expectedValue);
        return createRelationshipList(relationsDataList);
    }

    @NotNull
    private RelationshipList createRelationshipList(List<RelationshipData> relationsDataList) {
        Relationship relation1 = crateRelationship("any", relationsDataList);
        Relationship relation2 = crateRelationship("zone", relationsDataList);
        Relationship relation3 = crateRelationship("logical-link", relationsDataList);
        Relationship relation4 = crateRelationship("lag-interface", relationsDataList);
        Relationship relation5 = crateRelationship("pnf", relationsDataList);

        RelationshipList relationsList = new RelationshipList();
        relationsList.setRelationship(Arrays.asList(relation1, relation2, relation3, relation4, relation5));
        return relationsList;
    }

    @NotNull
    private List<RelationshipData> createRelationshipDataList(String expectedValue) {
        RelationshipData relationData1 = createRelationshipData("any-key", "incorrect_key");
        RelationshipData relationData2 = createRelationshipData("zone.zone-id", expectedValue);
        RelationshipData relationData3 = createRelationshipData("logical-link.link-name", expectedValue);
        RelationshipData relationData4 = createRelationshipData("pnf.pnf-name", expectedValue);

        return Arrays.asList(relationData1, relationData2, relationData3, relationData4);
    }

    @NotNull
    private Relationship crateRelationship(String relatedTo, List<RelationshipData> relationsDataList) {
        Relationship relation = new Relationship();
        relation.setRelatedTo(relatedTo);
        relation.setRelationDataList(relationsDataList);
        return relation;
    }

    @NotNull
    private RelationshipData createRelationshipData(String key, String value) {
        RelationshipData relationData = new RelationshipData();
        relationData.setRelationshipKey(key);
        relationData.setRelationshipValue(value);
        return relationData;
    }

    private org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList createRelationshipList() {
        RelatedToProperty property1 =
            createRelatedToProperty("instance-group.instance-group-name", CORRECT_VALUE);
        RelatedToProperty property2 =
            createRelatedToProperty("anything-key", "anything-value");
        List<RelatedToProperty> properties = Arrays.asList(property1, property2);

        org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship relationship1 =
            createRelationship("instance-group", properties);
        org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship relationship2 =
            createRelationship("any-key", properties);

        List<org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship> relationships =
            Arrays.asList(relationship1, relationship2);

        org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList relationshipList =
            new org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList();
        relationshipList.setRelationship(relationships);

        return relationshipList;
    }

    @NotNull
    private org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship createRelationship(String relatedTo,
        List<RelatedToProperty> relatedToPropertyList) {
        org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship relationship1 =
            new org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship();
        relationship1.setRelatedTo(relatedTo);
        relationship1.setRelatedToPropertyList(relatedToPropertyList);
        return relationship1;
    }

    @NotNull
    private RelatedToProperty createRelatedToProperty(String key, String value) {
        RelatedToProperty prop = new RelatedToProperty();
        prop.setKey(key);
        prop.setValue(value);
        return prop;
    }

    @NotNull
    private AaiResponse<AaiGetVnfResponse> createAaiResponseVnfResponse(List<VnfResult> vnfResults) {
        AaiGetVnfResponse vnfResponse = new AaiGetVnfResponse();
        vnfResponse.setResults(vnfResults);
        return new AaiResponse<>(vnfResponse, null, HttpStatus.SC_OK);
    }

    private VnfResult createVnfResult(String id, String nodeType) {
        VnfResult result = new VnfResult();
        result.setJsonId(id);
        result.setJsonNodeType(nodeType);
        return result;
    }


    private org.onap.vid.aai.model.AaiGetServicesRequestModel.Service createService(String serviceId,
        String resourceVersion,
        String serviceDescription) {
        org.onap.vid.aai.model.AaiGetServicesRequestModel.Service service
            = new org.onap.vid.aai.model.AaiGetServicesRequestModel.Service();
        service.isPermitted = false;
        service.resourceVersion = resourceVersion;
        service.serviceDescription = serviceDescription;
        service.serviceId = serviceId;
        return service;
    }

    @NotNull
    private Services createAaiResponseServices() {
        ServiceSubscription sub1 = new ServiceSubscription();
        sub1.isPermitted = false;
        sub1.serviceType = "serviceSubsType1";

        ServiceSubscription sub2 = new ServiceSubscription();
        sub2.isPermitted = true;
        sub2.serviceType = "serviceSubsType2";

        ServiceSubscriptions serviceSubs = new ServiceSubscriptions();
        serviceSubs.serviceSubscription = Collections.singletonList(sub2);

        Services services = new Services();
        services.globalCustomerId = GLOBAL_CUSTOMER_ID;
        services.resourceVersion = "v-1";
        services.subscriberName = "name-1";
        services.subscriberType = "type-1";
        services.serviceSubscriptions = serviceSubs;
        return services;
    }

    @NotNull
    private Subscriber createSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.globalCustomerId = "id-1";
        subscriber.resourceVersion = "v-1";
        subscriber.subscriberName = "name-1";
        subscriber.subscriberType = "type-1";
        return subscriber;
    }

    @Test
    public void whenGetNewestModelVersionByInvariantId_thenReturnSameValueAsAaiClient() {
        String modelInvariantId = "123";
        ModelVer modelVer = mock(ModelVer.class);
        when(aaiClient.getLatestVersionByInvariantId(eq(modelInvariantId))).thenReturn(modelVer);
        assertThat(aaiService.getNewestModelVersionByInvariantId(modelInvariantId)).isEqualTo(modelVer);
    }
}
