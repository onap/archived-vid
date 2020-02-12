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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import javax.ws.rs.core.Response;
import org.mockito.Mock;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.util.AAITreeConverter;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.parser.ServiceModelInflator;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.Action;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.FailureAAITreeNode;
import org.onap.vid.model.aaiTree.ServiceInstance;
import org.onap.vid.model.aaiTree.Vnf;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class AAIServiceTreeIntegrativeTest {

    @Mock
    private AaiClientInterface aaiClient;

    @Mock
    private Response aaiGetVersionByInvariantIdResponse;

    @Mock
    ExceptionWithRequestInfo exceptionWithRequestInfo;

    @Mock
    VidService sdcService;

    @Mock
    ServiceModelInflator serviceModelInflator;

    @Mock
    FeatureManager featureManager;

    @Mock
    Logging logging;

    private AAITreeNodesEnricher aaiTreeNodesEnricher;
    private AAITreeNodeBuilder aaiTreeNodeBuilder;

    private AAITreeConverter aaiTreeConverter = new AAITreeConverter(new ModelUtil());

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final ObjectMapper mapper = new ObjectMapper();

    private String globalCustomerID = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
    private String serviceType = "vWINIFRED";
    private String serviceInstanceId = "62888f15-6d24-4f7b-92a7-c3f35beeb215";

    private String serviceInstanceRequestUri = "business/customers/customer/" +
            globalCustomerID +
            "/service-subscriptions/service-subscription/" +
            serviceType +
            "/service-instances/service-instance/" +
            serviceInstanceId;

    private static String ServiceInstanceResponseString = "{\"service-instance-id\":\"62888f15-6d24-4f7b-92a7-c3f35beeb215\"," +
            "\"service-instance-name\": \"Dror123\"," +
            "\"environment-context\": \"null\"," +
            "\"workload-context\": \"null\"," +
            "\"model-invariant-id\": \"35340388-0b82-4d3a-823d-cbddf842be52\"," +
            "\"model-version-id\": \"4e799efd-fd78-444d-bc25-4a3cde2f8cb0\"," +
            "\"resource-version\": \"1515515088894\"," +
            "\"orchestration-status\": \"Active\"," +
            "\"relationship-list\": {" +
            "\"relationship\": [{" +
            "\"related-to\": \"project\"," +
            "\"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
            "\"related-link\": \"/aai/v12/business/projects/project/DFW\"," +
            "\"relationship-data\": [{" +
            "\"relationship-key\": \"project.project-name\"," +
            "\"relationship-value\": \"WATKINS\"}]},{" +
            "\"related-to\": \"generic-vnf\"," +
            "\"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\"," +
            "\"related-link\": \"/aai/v12/network/generic-vnfs/generic-vnf/59bde732-9b84-46bd-a59a-3c45fee0538b\"," +
            "\"relationship-data\": [{" +
            "\"relationship-key\": \"generic-vnf.vnf-id\"," +
            "\"relationship-value\": \"59bde732-9b84-46bd-a59a-3c45fee0538b\"}]," +
            "\"related-to-property\": [{" +
            "\"property-key\": \"generic-vnf.vnf-name\"," +
            "\"property-value\": \"DROR_vsp\"}]},{" +
            "\"related-to\": \"owning-entity\"," +
            "\"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
            "\"related-link\": \"/aai/v12/business/owning-entities/owning-entity/43b8a85a-0421-4265-9069-117dd6526b8a\"," +
            "\"relationship-data\": [{" +
            "\"relationship-key\": \"owning-entity.owning-entity-id\"," +
            "\"relationship-value\": \"43b8a85a-0421-4265-9069-117dd6526b8a\"}]}]}}";

    private static String genericVnfRequestUri = "/aai/v12/network/generic-vnfs/generic-vnf/59bde732-9b84-46bd-a59a-3c45fee0538b";

    private String genericVnfResponseString(boolean isDuplicatedKeysInTenantRelation) {

        return
                "{\"nf-role\":\"\"," +
                        "\"service-id\":\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"," +
                        "\"relationship-list\":{" +
                        "\"relationship\":[{" +
                        "\"related-to\":\"service-instance\"," +
                        "\"relationship-data\":[{" +
                        "\"relationship-value\":\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"," +
                        "\"relationship-key\":\"customer.global-customer-id\"},{" +
                        "\"relationship-value\":\"vWINIFRED\"," +
                        "\"relationship-key\":\"service-subscription.service-type\"},{" +
                        "\"relationship-value\":\"62888f15-6d24-4f7b-92a7-c3f35beeb215\"," +
                        "\"relationship-key\":\"service-instance.service-instance-id\"}]," +
                        "\"related-link\":\"/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/vWINIFRED/service-instances/service-instance/62888f15-6d24-4f7b-92a7-c3f35beeb215\"," +
                        "\"relationship-label\":\"org.onap.relationships.inventory.ComposedOf\"," +
                        "\"related-to-property\":[{" +
                        "\"property-key\":\"service-instance.service-instance-name\"," +
                        "\"property-value\":\"Dror123\"}]},{" +
                        "\"related-to\":\"platform\"," +
                        "\"relationship-data\":[{" +
                        "\"relationship-value\":\"platformY\"," +
                        "\"relationship-key\":\"platform.platform-name\"}]," +
                        "\"related-link\":\"/aai/v12/business/platforms/platform/platformY\"," +
                        "\"relationship-label\":\"org.onap.relationships.inventory.Uses\"},{" +
                        "\"related-to\":\"line-of-business\"," +
                        "\"relationship-data\":[{" +
                        "\"relationship-value\":\"lob1, lobX\"," +
                        "\"relationship-key\":\"line-of-business.line-of-business-name\"}]," +
                        "\"related-link\":\"/aai/v12/business/lines-of-business/line-of-business/lob1%2C%20lobX\"," +
                        "\"relationship-label\":\"org.onap.relationships.inventory.Uses\"}," +
                        "            {" +
                        "                \"related-to\": \"tenant\"," +
                        "                \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                        "                \"related-link\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region\"," +
                        "                \"relationship-data\": [" +
                        "                    {" +
                        "                        \"relationship-key\": \"cloud-region.cloud-owner\"," +
                        "                        \"relationship-value\": \"irma-aic\"" +
                        "                    }," +
                        "                    {" +
                        "                        \"relationship-key\": \"cloud-region.cloud-region-id\"," +
                        "                        \"relationship-value\": \"aCloudRegionId\"" +
                        "                    }," +
                        "                    {" +
                        "                        \"relationship-key\": \"tenant.tenant-id\"," +
                        "                        \"relationship-value\": \"someTenantId123\"" +
                        "                    }," +
                        (isDuplicatedKeysInTenantRelation ?  "{\"relationship-key\": \"tenant.tenant-id\", \"relationship-value\": \"someTenantId456\"}, " : "" ) +
                        "                    {" +
                        "                        \"relationship-key\": \"vserver.vserver-id\"," +
                        "                        \"relationship-value\": \"5eef9f6d-9933-4bc6-9a1a-862d61309437\"" +
                        "                    }" +
                        "                ]," +
                        "                \"related-to-property\": [" +
                        "                    {" +
                        "                        \"property-key\": \"vserver.vserver-name\"," +
                        "                        \"property-value\": \"zolson5bfapn01dns002\"" +
                        "                    }" +
                        "                  ]" +
                        "           }" +
                        "]}," +
                        "\"vnf-id\":\"59bde732-9b84-46bd-a59a-3c45fee0538b\",\n" +
                        "\"nf-type\":\"\"," +
                        "\"prov-status\":\"PREPROV\"," +
                        "\"vnf-type\":\"Lital--1707097/Lital-VSP-1707097 0\"," +
                        "\"orchestration-status\":\"Created\"," +
                        "\"nf-naming-code\":\"\"," +
                        "\"in-maint\":true," +
                        "\"nf-function\":\"\"," +
                        "\"model-version-id\":\"11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0\"," +
                        "\"resource-version\":\"1522431420767\"," +
                        "\"model-customization-id\":\"14992bf5-d585-4b54-8101-7cf76774337a\"," +
                        "\"model-invariant-id\":\"55628ce3-ed56-40bd-9b27-072698ce02a9\"," +
                        "\"vnf-name\":\"DROR_vsp\"," +
                        "\"is-closed-loop-disabled\":true}";
    }

    private List<String> invariantIDs = Arrays.asList("35340388-0b82-4d3a-823d-cbddf842be52",
            "55628ce3-ed56-40bd-9b27-072698ce02a9");

    private static String getVersionByInvariantIdResponseString = "{" +
            "\"model\": [{" +
            "\"model-invariant-id\": \"55628ce3-ed56-40bd-9b27-072698ce02a9\"," +
            "\"model-type\": \"resource\"," +
            "\"resource-version\": \"1499690926297\"," +
            "\"model-vers\": {" +
            "\"model-ver\": [{" +
            "\"model-version-id\": \"11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0\"," +
            "\"model-name\": \"Lital-VSP-1707097\"," +
            "\"model-version\": \"2.0\",\n" +
            "\"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\"," +
            "\"model-description\": \"Lital-VSP-1707097-NEW\"," +
            "\"resource-version\": \"1499690926298\"," +
            "\"model-elements\": {" +
            "\"model-element\": [{" +
            "\"model-element-uuid\": \"a4f14ef7-daa2-4257-9b81-b4558dc4beaa\"," +
            "\"new-data-del-flag\": \"T\"," +
            "\"cardinality\": \"unbounded\"," +
            "\"resource-version\": \"1499690926300\"," +
            "\"relationship-list\": {" +
            "\"relationship\": [{" +
            "\"related-to\": \"model-ver\"," +
            "\"relationship-label\": \"org.onap.relationships.inventory.IsA\"," +
            "\"related-link\": \"/aai/v12/service-design-and-creation/models/model/acc6edd8-a8d4-4b93-afaa-0994068be14c/model-vers/model-ver/93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"," +
            "\"relationship-data\": [{" +
            "\"relationship-key\": \"model.model-invariant-id\"," +
            "\"relationship-value\": \"acc6edd8-a8d4-4b93-afaa-0994068be14c\"},{" +
            "\"relationship-key\": \"model-ver.model-version-id\"," +
            "\"relationship-value\": \"93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"}]," +
            "\"related-to-property\": [{" +
            "\"property-key\": \"model-ver.model-name\"," +
            "\"property-value\": \"generic-vnf\"}]}]}}]}," +
            "\"relationship-list\": {" +
            "\"relationship\": [{" +
            "\"related-to\": \"model-element\"," +
            "\"relationship-label\": \"org.onap.relationships.inventory.IsA\"," +
            "\"related-link\": \"/aai/v12/service-design-and-creation/models/model/35340388-0b82-4d3a-823d-cbddf842be52/model-vers/model-ver/4e799efd-fd78-444d-bc25-4a3cde2f8cb0/model-elements/model-element/344e8713-f0af-423a-b96d-f45b3a479d11/model-elements/model-element/9e8c8885-601a-4fd6-8424-c233a5333db6\"," +
            "\"relationship-data\": [{" +
            "\"relationship-key\": \"model.model-invariant-id\"," +
            "\"relationship-value\": \"35340388-0b82-4d3a-823d-cbddf842be52\"},{" +
            "\"relationship-key\": \"model-ver.model-version-id\"," +
            "\"relationship-value\": \"4e799efd-fd78-444d-bc25-4a3cde2f8cb0\"},{" +
            "\"relationship-key\": \"model-element.model-element-uuid\"," +
            "\"relationship-value\": \"344e8713-f0af-423a-b96d-f45b3a479d11\"},{" +
            "\"relationship-key\": \"model-element.model-element-uuid\"," +
            "\"relationship-value\": \"9e8c8885-601a-4fd6-8424-c233a5333db6\"}]}]}}]}},{" +
            "\"model-invariant-id\": \"35340388-0b82-4d3a-823d-cbddf842be52\"," +
            "\"model-type\": \"service\"," +
            "\"resource-version\": \"1499690928188\"," +
            "\"model-vers\": {" +
            "\"model-ver\": [{" +
            "\"model-version-id\": \"4e799efd-fd78-444d-bc25-4a3cde2f8cb0\"," +
            "\"model-name\": \"Lital--1707097\"," +
            "\"model-version\": \"1.0\"," +
            "\"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\"," +
            "\"model-description\": \"Lital--1707097\"," +
            "\"resource-version\": \"1499690928190\"," +
            "\"model-elements\": {" +
            "\"model-element\": [{" +
            "\"model-element-uuid\": \"344e8713-f0af-423a-b96d-f45b3a479d11\"," +
            "\"new-data-del-flag\": \"T\"," +
            "\"cardinality\": \"unbounded\"," +
            "\"resource-version\": \"1499690928191\"," +
            "\"relationship-list\": {" +
            "\"relationship\": [{" +
            "\"related-to\": \"model-ver\"," +
            "\"relationship-label\": \"org.onap.relationships.inventory.IsA\"," +
            "\"related-link\": \"/aai/v12/service-design-and-creation/models/model/82194af1-3c2c-485a-8f44-420e22a9eaa4/model-vers/model-ver/46b92144-923a-4d20-b85a-3cbd847668a9\"," +
            "\"relationship-data\": [{" +
            "\"relationship-key\": \"model.model-invariant-id\"," +
            "\"relationship-value\": \"82194af1-3c2c-485a-8f44-420e22a9eaa4\"},{" +
            "\"relationship-key\": \"model-ver.model-version-id\"," +
            "\"relationship-value\": \"46b92144-923a-4d20-b85a-3cbd847668a9\"}]," +
            "\"related-to-property\": [{" +
            "\"property-key\": \"model-ver.model-name\"," +
            "\"property-value\": \"service-instance\"}]}]}}]}}]}}]}";

    @BeforeMethod
    public void initMocks() {
        TestUtils.initMockitoMocks(this);
        reboundLoggingWithMdcMock();
        aaiTreeNodeBuilder = new AAITreeNodeBuilder(aaiClient, logging);
        aaiTreeNodesEnricher = new AAITreeNodesEnricher(aaiClient, null, featureManager, serviceModelInflator);

        when(featureManager.isActive(Features.FLAG_EXP_TOPOLOGY_TREE_VFMODULE_NAMES_FROM_OTHER_TOSCA_VERSIONS))
            .thenReturn(true);
    }

    private void reboundLoggingWithMdcMock() {
        when(logging.withMDC(any(), any(Callable.class))).thenAnswer(invocation -> invocation.getArgument(1));
        when(logging.withMDC(any(), any(Function.class))).thenAnswer(invocation -> invocation.getArgument(1));
    }

    public void getServiceInstanceTreeAndAssert(boolean isDuplicatedKeysInTenantRelation) throws IOException, AsdcCatalogException {
        when(aaiClient.typedAaiRest(URI.create(serviceInstanceRequestUri), JsonNode.class, null, HttpMethod.GET, false)).thenReturn(mapper.readTree(ServiceInstanceResponseString));
        when(aaiClient.typedAaiRest(URI.create(genericVnfRequestUri), JsonNode.class, null, HttpMethod.GET, false)).
                thenReturn(mapper.readTree(genericVnfResponseString(isDuplicatedKeysInTenantRelation)));
        when(aaiClient.getVersionByInvariantId(invariantIDs)).thenReturn(aaiGetVersionByInvariantIdResponse);

        when(aaiGetVersionByInvariantIdResponse.readEntity(String.class)).thenReturn(getVersionByInvariantIdResponseString);

        when(sdcService.getServiceModelOrThrow(any())).thenReturn(mock(ServiceModel.class));
        when(serviceModelInflator.toNamesByVersionId(any())).thenReturn(ImmutableMap.of(
                 "11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0", new ServiceModelInflator.Names("vnf-model-customization-name", "vnf-key-in-model")
        ));

        ServiceInstance root = new AAIServiceTree(aaiTreeNodeBuilder, aaiTreeNodesEnricher, aaiTreeConverter, sdcService, executorService)
                .getServiceInstanceTopology(globalCustomerID, serviceType, serviceInstanceId);

        assertServiceNode(root, 1);

        assertEquals(0, root.getExistingNetworksCounterMap().size());
        assertEquals(1, root.getExistingVNFCounterMap().size());
        assertEquals((Long)1L, root.getExistingVNFCounterMap().get("14992bf5-d585-4b54-8101-7cf76774337a"));

        assertVnfNode(root, isDuplicatedKeysInTenantRelation);
    }

    @Test
    public void getServiceInstanceTreeTestHappyFlow() throws IOException, AsdcCatalogException {
        getServiceInstanceTreeAndAssert(false);
    }

    @Test
    public void whenDuplicatedKeyInRelationshipData_thenVnfIsParsedButWithoutPlacement() throws IOException, AsdcCatalogException {
        getServiceInstanceTreeAndAssert(true);
    }

    private void mockAaiGetCall(String aaiPath, String jsonFilePath) {
        when(aaiClient.typedAaiRest(URI.create(aaiPath), JsonNode.class, null, HttpMethod.GET, false)).thenReturn(TestUtils.readJsonResourceFileAsObject(jsonFilePath, JsonNode.class));
    }

    @Test
    public void whenGetServiceInstanceWithCR_thenResultAreAsExpected() throws Exception {

        List<String> modelInvIds = ImmutableList.of(
                "868b109c-9481-4a18-891b-af974db7705a",
                "081ceb56-eb71-4566-a72d-3e7cbee5cdf1",
                "f6342be5-d66b-4d03-a1aa-c82c3094c4ea");

        mockAaiGetCall("business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Emanuel/service-instances/service-instance/a565e6ad-75d1-4493-98f1-33234b5c17e2",
            "/getTopology/serviceWithCR/serviceWithCR.json");
        mockAaiGetCall("/aai/v14/network/collections/collection/84a351ae-3601-45e2-98df-878d6c816abc",
            "/getTopology/serviceWithCR/CR.json");

        mockAaiGetCall("/aai/v14/network/instance-groups/instance-group/6b3536cf-3a12-457f-abb5-fa2203e0d923",
            "/getTopology/serviceWithCR/instanceGroup-NCF.json");

        when(aaiClient.getVersionByInvariantId(modelInvIds)).thenReturn(aaiGetVersionByInvariantIdResponse);

        when(aaiGetVersionByInvariantIdResponse.readEntity(String.class)).
                thenReturn(TestUtils.readFileAsString("/getTopology/serviceWithCR/service-design-and-creation.json"));

        when(sdcService.getServiceModelOrThrow(any())).thenReturn(
                TestUtils.readJsonResourceFileAsObject("/getTopology/serviceWithCR/serviceWithCRModel.json", ServiceModel.class));

        ServiceInstance serviceInstance = new AAIServiceTree(aaiTreeNodeBuilder,
            new AAITreeNodesEnricher(aaiClient, null, featureManager, new ServiceModelInflator()), aaiTreeConverter, sdcService, executorService)
                .getServiceInstanceTopology("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", "Emanuel", "a565e6ad-75d1-4493-98f1-33234b5c17e2");

        String expected = TestUtils.readFileAsString("/getTopology/serviceWithCR/getTopologyWithCR.json");
        assertThat(serviceInstance, jsonEquals(expected).when(IGNORING_ARRAY_ORDER));
    }

    protected void assertVnfNode(ServiceInstance root, boolean isExpectToPlacement) {
        Vnf vnf = root.getVnfs().get("59bde732-9b84-46bd-a59a-3c45fee0538b");
        assertEquals(Action.None, vnf.getAction());
        assertEquals("Created", vnf.getOrchStatus());
        assertEquals("PREPROV", vnf.getProvStatus());
        assertEquals(true, vnf.getInMaint());
        assertEquals("11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0", vnf.getModelInfo().getModelVersionId());
        assertEquals("14992bf5-d585-4b54-8101-7cf76774337a", vnf.getModelInfo().getModelCustomizationId());
        assertEquals("55628ce3-ed56-40bd-9b27-072698ce02a9", vnf.getModelInfo().getModelInvariantId());
        assertEquals("Lital-VSP-1707097", vnf.getModelInfo().getModelName());
        assertEquals("vnf-model-customization-name", vnf.getModelInfo().getModelCustomizationName());
        assertEquals("2.0", vnf.getModelInfo().getModelVersion());
        assertEquals("vnf", vnf.getModelInfo().getModelType());
        assertEquals("59bde732-9b84-46bd-a59a-3c45fee0538b", vnf.getInstanceId());
        assertEquals("DROR_vsp", vnf.getInstanceName());
        assertEquals("Lital--1707097/Lital-VSP-1707097 0", vnf.getInstanceType());
        assertEquals("11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0", vnf.getUuid());
        assertEquals("59bde732-9b84-46bd-a59a-3c45fee0538b", vnf.getTrackById());
        assertEquals(0, vnf.getVfModules().size());
        assertEquals(0, vnf.getNetworks().size());
        if (!isExpectToPlacement) {
            assertEquals("aCloudRegionId", vnf.getLcpCloudRegionId());
            assertEquals("someTenantId123", vnf.getTenantId());
            assertEquals("irma-aic", vnf.getCloudOwner());
        }
        else {
            assertNull(vnf.getLcpCloudRegionId());
            assertNull(vnf.getTenantId());
            assertNull(vnf.getCloudOwner());
        }
    }

    protected void assertServiceNode(ServiceInstance root, int expectedVnfSize) {
        assertEquals(Action.None, root.getAction());
        assertEquals("Active", root.getOrchStatus());
        assertEquals("4e799efd-fd78-444d-bc25-4a3cde2f8cb0", root.getModelInfo().getModelVersionId());
        assertEquals(null, root.getModelInfo().getModelCustomizationId());
        assertEquals("35340388-0b82-4d3a-823d-cbddf842be52", root.getModelInfo().getModelInvariantId());
        assertEquals("1.0", root.getModelInfo().getModelVersion());
        assertEquals("Lital--1707097", root.getModelInfo().getModelName());
        assertEquals("service", root.getModelInfo().getModelType());
        assertEquals("62888f15-6d24-4f7b-92a7-c3f35beeb215", root.getInstanceId());
        assertEquals("Dror123", root.getInstanceName());
        assertEquals(expectedVnfSize, root.getVnfs().size());
        assertEquals(0, root.getNetworks().size());
        //future - after add additional properties - assert it
    }

    @Test(expectedExceptions = GenericUncheckedException.class ,expectedExceptionsMessageRegExp = "AAI node fetching failed.")
    public void getServiceInstanceTreeTest_errorCreatingVnfNode() throws IOException, AsdcCatalogException {
        when(aaiClient.typedAaiRest(URI.create(serviceInstanceRequestUri), JsonNode.class, null, HttpMethod.GET, false)).thenReturn(mapper.readTree(ServiceInstanceResponseString));
        when(aaiClient.typedAaiRest(URI.create(genericVnfRequestUri), JsonNode.class, null, HttpMethod.GET, false)).thenThrow(exceptionWithRequestInfo);
        when(aaiClient.getVersionByInvariantId(any())).thenReturn(aaiGetVersionByInvariantIdResponse);
        when(exceptionWithRequestInfo.toString()).thenReturn("this is a fetching node exception");

        when(aaiGetVersionByInvariantIdResponse.readEntity(String.class)).thenReturn(getVersionByInvariantIdResponseString);

        when(sdcService.getServiceModelOrThrow(any())).thenReturn(mock(ServiceModel.class));
        when(serviceModelInflator.toNamesByVersionId(any())).thenReturn(ImmutableMap.of());

        new AAIServiceTree(aaiTreeNodeBuilder, aaiTreeNodesEnricher, aaiTreeConverter, sdcService, executorService)
                .getServiceInstanceTopology(globalCustomerID, serviceType, serviceInstanceId);
    }

    @Test(expectedExceptions = GenericUncheckedException.class ,expectedExceptionsMessageRegExp = "AAI node fetching failed.")
    public void testCreateFailureNode() {
        AAITreeNode failureNode = FailureAAITreeNode.of(new RuntimeException("Failed to retrieve node data."));
        failureNode.getId();
    }
}
