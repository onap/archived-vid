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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.onap.vid.model.VidNotions.InstantiationType;
import static org.onap.vid.model.VidNotions.InstantiationUI;
import static org.onap.vid.model.VidNotions.ModelCategory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.onap.portalsdk.core.domain.FusionObject;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.model.Action;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.ServiceInfo.ServiceAction;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.serviceInstantiation.InstanceGroup;
import org.onap.vid.model.serviceInstantiation.Network;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.UserParamNameAndValue;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.RequestStatus;
import org.onap.vid.properties.Features;
import org.onap.vid.utils.DaoUtils;
import org.onap.vid.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.togglz.core.manager.FeatureManager;

public class AsyncInstantiationBaseTest extends AbstractTestNGSpringContextTests {

    public static final String MODEL_UUID = "337be3fc-293e-43ec-af0b-cf932dad07e6";
    public static final String MODEL_UUID_2 = "ce052844-22ba-4030-a838-822f2b39eb9b";
    public static final String MODEL_UUID_3 = "47a071cd-99f7-49bb-bc8b-f957979d6fe1";

    public static final String OWNING_ENTITY_ID = "038d99af-0427-42c2-9d15-971b99b9b489";
    public static final String JULIO_ERICKSON = "JULIO ERICKSON";
    public static final String PROJECT_NAME = "{some project name}";
    public static final String SUBSCRIBER_ID = "{some subscriber id}";
    public static final String SUBSCRIBER_NAME = "{some subscriber name}";
    public static final String PRODUCT_FAMILY_ID = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
    public static final String INSTANCE_NAME = "vPE_Service";
    public static final String SUBSCRIPTION_SERVICE_TYPE = "VMX";
    public static final String LCP_CLOUD_REGION_ID = "mdt1";
    public static final String A6CA3EE0394ADE9403F075DB23167E = "88a6ca3ee0394ade9403f075db23167e";
    public static final String TENANT_NAME = "USP-SIP-IC-24335-T-01";
    public static final String AIC_ZONE_ID = "NFT1";
    public static final String AIC_ZONE_NAME = "NFTJSSSS-NFT1";
    public static final String TEST_API = "GR_API";
    public static final String SERVICE_MODEL_VERSION_ID = "3c40d244-808e-42ca-b09a-256d83d19d0a";
    public static final String VF_MODULE_0_MODEL_CUSTOMIZATION_NAME = "a25e8e8c-58b8-4eec-810c-97dcc1f5cb7f";
    public static final String VF_MODULE_1_MODEL_CUSTOMIZATION_NAME = "72d9d1cd-f46d-447a-abdb-451d6fb05fa8";
    public static final String VF_MODULE_0_MODEL_VERSION_ID = "4c75f813-fa91-45a4-89d0-790ff5f1ae79";
    public static final String VF_MODULE_1_MODEL_VERSION_ID = "56e2b103-637c-4d1a-adc8-3a7f4a6c3240";
    public static final String VNF_NAME = "vmxnjr001";
    public static final String VNF_GROUP_NAME = "VNF_GROUP_NAME";

    protected HashMap<String, String> instanceParamsMapWithoutParams;
    protected HashMap<String, String> vfModuleInstanceParamsMapWithParamsToRemove;
    protected HashMap<String, String> vnfInstanceParamsMapWithParamsToRemove;

    protected int serviceCount = 0;


    @Inject
    protected DataAccessService dataAccessService;

    @Inject
    protected FeatureManager featureManager;

    @Inject
    protected AaiClientInterface aaiClient;

    @Inject
    protected CloudOwnerService cloudOwnerService;

    @Autowired
    protected SessionFactory sessionFactory;


    protected static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    protected HashMap<String, Object> getPropsMap() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(FusionObject.Parameters.PARAM_USERID, 0);
        return props;
    }


    private void setCreateDateToServiceInfo(UUID jobUuid, LocalDateTime createDate) {
        List<ServiceInfo> serviceInfoList = dataAccessService.getList(ServiceInfo.class, getPropsMap());
        DaoUtils.tryWithSessionAndTransaction(sessionFactory, session -> {
            serviceInfoList.stream()
                .filter(serviceInfo -> jobUuid.equals(serviceInfo.getJobId()))
                .forEach(serviceInfo -> {
                    serviceInfo.setCreated(toDate(createDate));
                    session.saveOrUpdate(serviceInfo);
                });
            return 1;
        });
    }


    protected void addNewServiceInfo(UUID uuid, String userId, String serviceName, LocalDateTime createDate,
        LocalDateTime statusModifiedDate, JobStatus status, boolean isHidden, boolean retryEnabled,
        String modelUUID) {
        ServiceInfo serviceInfo = createServiceInfo(uuid, userId, serviceName, createDate, statusModifiedDate, status,
            isHidden, retryEnabled, modelUUID);
        dataAccessService.saveDomainObject(serviceInfo, getPropsMap());
        setCreateDateToServiceInfo(uuid, createDate);
        serviceCount++;
    }
    @NotNull
    private ServiceInfo createServiceInfo(UUID uuid, String userId, String serviceName, LocalDateTime createDate,
        LocalDateTime statusModifiedDate, JobStatus status, boolean isHidden, boolean retryEnabled, String modelUUID) {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setJobId(uuid);
        serviceInfo.setUserId(userId);
        serviceInfo.setServiceInstanceName(serviceName);
        serviceInfo.setStatusModifiedDate(toDate(statusModifiedDate));
        serviceInfo.setJobStatus(status);
        serviceInfo.setPause(false);
        serviceInfo.setOwningEntityId("1234");
        serviceInfo.setCreatedBulkDate(toDate(createDate));
        serviceInfo.setRetryEnabled(retryEnabled);
        serviceInfo.setServiceModelId(modelUUID);
        serviceInfo.setHidden(isHidden);
        return serviceInfo;
    }

    protected void addNewServiceInfoWithAction(UUID uuid, String userId, String serviceName, LocalDateTime createDate,
        LocalDateTime statusModifiedDate, JobStatus status, boolean isHidden, boolean retryEnabled,
        String modelUUID, ServiceAction action) {
        ServiceInfo serviceInfo = createServiceInfo(uuid, userId, serviceName, createDate, statusModifiedDate, status,
            isHidden, retryEnabled, modelUUID);
        serviceInfo.setAction(action);
        dataAccessService.saveDomainObject(serviceInfo, getPropsMap());
        setCreateDateToServiceInfo(uuid, createDate);
        serviceCount++;
    }


    public ServiceInstantiation generateMockMacroServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure) {
        return generateMockServiceInstantiationPayload(isPause, vnfs, Collections.EMPTY_MAP, Collections.EMPTY_MAP, bulkSize, isUserProvidedNaming, projectName, rollbackOnFailure, false, null, Action.Create, null);
    }

    public ServiceInstantiation generateMockALaCarteServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs, Map<String, Network> networks, Map<String, InstanceGroup> vnfGroups, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure, String testApi) {
        return generateMockServiceInstantiationPayload(isPause, vnfs, networks, vnfGroups, bulkSize, isUserProvidedNaming, projectName, rollbackOnFailure, true, testApi, Action.Create, null);
    }

    public ServiceInstantiation generateMockAlaCarteServiceDeletionPayload(boolean isPause, Map<String, Vnf> vnfs, Map<String, Network> networks, Map<String, InstanceGroup> vnfGroups, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure, String testApi, String instanceId) {
        return generateMockServiceInstantiationPayload(isPause, vnfs, networks, vnfGroups, bulkSize, isUserProvidedNaming, projectName, rollbackOnFailure, true, testApi, Action.Delete, instanceId);
    }

    public ServiceInstantiation generateMockServiceDeletionPayload(boolean isPause, Map<String, Vnf> vnfs, Map<String, Network> networks, Map<String, InstanceGroup> vnfGroups, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure, String testApi, String instanceId) {
        return generateMockServiceInstantiationPayload(isPause, vnfs, networks, vnfGroups, bulkSize, isUserProvidedNaming, projectName, rollbackOnFailure, false, testApi, Action.Delete, instanceId);
    }
    private ServiceInstantiation generateMockServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs, Map<String, Network> networks, Map<String, InstanceGroup> vnfGroups, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure, boolean isAlacarte, String testApi, Action action, String instanceId) {
        ModelInfo modelInfo = createModelInfo();

        List<Map<String,String>> instanceParams = createInstanceParams();

        return new ServiceInstantiation( modelInfo,
                AsyncInstantiationBusinessLogicTest.OWNING_ENTITY_ID,
                AsyncInstantiationBusinessLogicTest.JULIO_ERICKSON,
                projectName,
                AsyncInstantiationBusinessLogicTest.SUBSCRIBER_ID,
                AsyncInstantiationBusinessLogicTest.SUBSCRIBER_NAME,
                AsyncInstantiationBusinessLogicTest.PRODUCT_FAMILY_ID,
                isUserProvidedNaming ? AsyncInstantiationBusinessLogicTest.INSTANCE_NAME : null,
                AsyncInstantiationBusinessLogicTest.SUBSCRIPTION_SERVICE_TYPE,
                AsyncInstantiationBusinessLogicTest.LCP_CLOUD_REGION_ID,
                null,
                AsyncInstantiationBusinessLogicTest.A6CA3EE0394ADE9403F075DB23167E,
                AsyncInstantiationBusinessLogicTest.TENANT_NAME,
                AsyncInstantiationBusinessLogicTest.AIC_ZONE_ID,
                AsyncInstantiationBusinessLogicTest.AIC_ZONE_NAME,
                vnfs,
                networks,
                vnfGroups,
                null,
                instanceParams,
                isPause,
                bulkSize,
                rollbackOnFailure,
                isAlacarte,
                testApi,
                instanceId,
                action.name(),
                UUID.randomUUID().toString(), null, null, null, null);
    }

    private List<Map<String,String>> createInstanceParams() {
        List<Map<String, String>> instanceParams = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("instanceParams_test1" , "some text");
        map.put("instanceParams_test2" , "another text");
        instanceParams.add(map);
        return instanceParams;
    }

    protected VfModule createVfModule(
        String modelName, String modelVersionId, String modelCustomizationId,
        List<Map<String, String>> instanceParams, List<UserParamNameAndValue> supplementaryParams, String instanceName,
        String volumeGroupInstanceName, boolean isAlacarte, Boolean usePreload) {
        ModelInfo vfModuleInfo = new ModelInfo();
        vfModuleInfo.setModelType("vfModule");
        vfModuleInfo.setModelName(modelName);
        vfModuleInfo.setModelVersionId(modelVersionId);
        vfModuleInfo.setModelCustomizationId(modelCustomizationId);
        vfModuleInfo.setModelCustomizationName(modelName);

        if (isAlacarte) {
            vfModuleInfo.setModelInvariantId("22222222-f63c-463e-ba94-286933b895f9");
            vfModuleInfo.setModelVersion("10.0");
            return new VfModule(vfModuleInfo, instanceName, volumeGroupInstanceName, Action.Create.name(), "mdt1", null,
                "88a6ca3ee0394ade9403f075db23167e", instanceParams, supplementaryParams, false,
                usePreload, null, UUID.randomUUID().toString(), null, null,
                null, null, null, null, "originalName");
        }

        return new VfModule(vfModuleInfo, instanceName, volumeGroupInstanceName, Action.Create.name(), null, null, null,
                instanceParams, supplementaryParams, false, false, null, UUID.randomUUID().toString(), null,
            null, null, null, null, null, "originalName");
    }

    protected ModelInfo createVfModuleModelInfo(String modelName, String modelVersion, String modelVersionId, String modelInvariantId, String modelCustomizationId, String modelCustomizationName) {
        return createModelInfo("vfModule", modelName, modelVersion, modelVersionId, modelInvariantId, modelCustomizationId, modelCustomizationName);
    }

    protected VfModule createVfModuleForReplace(ModelInfo vfModuleModelInfo, String instanceName,
        String lcpCloudRegionId, String tenantId, Boolean retainAssignments, Boolean retainVolumeGroups, List<UserParamNameAndValue> supplementaryParams) {
        return new VfModule( vfModuleModelInfo, instanceName, null, Action.Upgrade.name(), lcpCloudRegionId, null, tenantId,
                null, supplementaryParams, true, null, null, UUID.randomUUID().toString(), null,
                null, retainAssignments, retainVolumeGroups, null, null, "originalName");
    }

    protected ModelInfo createVnfModelInfo(boolean isAlacarte) {
        ModelInfo vnfModelInfo = new ModelInfo();
        vnfModelInfo.setModelType("vnf");
        vnfModelInfo.setModelName("2016-73_MOW-AVPN-vPE-BV-L");
        vnfModelInfo.setModelVersionId("7f40c192-f63c-463e-ba94-286933b895f8");
        vnfModelInfo.setModelCustomizationName("2016-73_MOW-AVPN-vPE-BV-L 0");
        vnfModelInfo.setModelCustomizationId("ab153b6e-c364-44c0-bef6-1f2982117f04");
        //added two conditional fields according to MSO AID - needed only in alacarte
        if (isAlacarte) {
            vnfModelInfo.setModelInvariantId("11111111-f63c-463e-ba94-286933b895f9");
            vnfModelInfo.setModelVersion("10.0");
        }
        return vnfModelInfo;
    }

    protected ModelInfo createVnfModelInfo(String modelName, String modelVersion, String modelVersionId, String modelInvariantId, String modelCustomizationId, String modelCustomizationName) {
        return createModelInfo("vnf", modelName, modelVersion, modelVersionId, modelInvariantId, modelCustomizationId, modelCustomizationName);
    }

    private ModelInfo createNetworkModelInfo(boolean isAlacarte, String modelCustomizationId)
    {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("network");
        modelInfo.setModelName("2016-73_MOW-AVPN-vPE-BV-L");
        modelInfo.setModelVersionId("7f40c192-f63c-463e-ba94-286933b895f8");
        modelInfo.setModelCustomizationName("2016-73_MOW-AVPN-vPE-BV-L 0");
        modelInfo.setModelCustomizationId(modelCustomizationId);
        //added two conditional fields according to MSO AID - needed only in alacarte
        if (isAlacarte) {
            modelInfo.setModelInvariantId("11111111-f63c-463e-ba94-286933b895f9");
            modelInfo.setModelVersion("10.0");
        }
        return modelInfo;
    }

    private ModelInfo createModelInfo() {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("service");
        modelInfo.setModelVersionId(SERVICE_MODEL_VERSION_ID);
        modelInfo.setModelVersion("10.0");
        modelInfo.setModelInvariantId("5d48acb5-097d-4982-aeb2-f4a3bd87d31b");
        modelInfo.setModelName("MOW AVPN vMX BV vPE 1 Service");
        return modelInfo;
    }

    private ModelInfo createModelInfo(String modelType, String modelName, String modelVersion, String modelVersionId, String modelInvariantId, String modelCustomizationId, String modelCustomizationName ) {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType(modelType);
        modelInfo.setModelVersionId(modelVersionId);
        modelInfo.setModelVersion(modelVersion);
        modelInfo.setModelInvariantId(modelInvariantId);
        modelInfo.setModelName(modelName);
        modelInfo.setModelCustomizationId(modelCustomizationId);
        modelInfo.setModelCustomizationName(modelCustomizationName);
        return modelInfo;
    }

    protected Map<String, Vnf> createVnfList(HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams, boolean isUserProvidedNaming) {
        return createVnfList(vfModuleInstanceParamsMap, vnfInstanceParams, isUserProvidedNaming, false);
    }

    protected Map<String, Vnf> createVnfList(HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams, boolean isUserProvidedNaming, boolean isAlacarte) {
        Map<String, Vnf> vnfs = new HashMap<>();
        ModelInfo vnfModelInfo = createVnfModelInfo(isAlacarte);

        Map<String, Map<String, VfModule>> vfModules = new HashMap<>();

        List<Map<String, String>> instanceParams1 = ImmutableList.of((ImmutableMap.of("vmx_int_net_len", "24")));
        VfModule vfModule1 = createVfModule("201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0", VF_MODULE_0_MODEL_VERSION_ID, VF_MODULE_0_MODEL_CUSTOMIZATION_NAME,
            instanceParams1, emptyList(), (isUserProvidedNaming ? "vmxnjr001_AVPN_base_vPE_BV_base" : null), null, isAlacarte, true);
        List<Map<String, String>> instanceParams2 = ImmutableList.of(vfModuleInstanceParamsMap);
        VfModule vfModule2 = createVfModule("201673MowAvpnVpeBvL..AVPN_vRE_BV..module-1", VF_MODULE_1_MODEL_VERSION_ID, VF_MODULE_1_MODEL_CUSTOMIZATION_NAME,
            instanceParams2, emptyList(), (isUserProvidedNaming ? "vmxnjr001_AVPN_base_vRE_BV_expansion": null), (isUserProvidedNaming ? "myVgName" : null), isAlacarte, true);

        String vfModuleModelName = vfModule1.getModelInfo().getModelName();
        vfModules.put(vfModuleModelName, new LinkedHashMap<>());

        vfModules.get(vfModuleModelName).put(vfModuleModelName + ":001", vfModule1);
        vfModules.get(vfModuleModelName).put(vfModuleModelName + ":002", vfModule2);

        Vnf vnf = new Vnf(vnfModelInfo, "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", (isUserProvidedNaming ? VNF_NAME : null), Action.Create.name(),
                "platformName", "mdt1", null, "88a6ca3ee0394ade9403f075db23167e", vnfInstanceParams,"lineOfBusinessName" , false, null, vfModules,
                UUID.randomUUID().toString(), null, null, null, "originalName");

        vnfs.put(vnf.getModelInfo().getModelName(), vnf);
        return vnfs;
    }

    protected void mockAaiClientAaiStatusOK() {
        when(aaiClient.isNodeTypeExistsByName(eq(AsyncInstantiationBusinessLogicImpl.NAME_FOR_CHECK_AAI_STATUS), any())).thenReturn(false);
    }

    protected void enableAddCloudOwnerOnMsoRequest(boolean isActive) {
        // always turn on the feature flag
        when(featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(isActive);
        when(aaiClient.getCloudOwnerByCloudRegionId(anyString())).thenReturn("irma-aic");
    }

    protected void enableAddCloudOwnerOnMsoRequest() {
        enableAddCloudOwnerOnMsoRequest(true);
    }

    protected ServiceInstantiation generateMacroMockServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs) {
        return generateMockMacroServiceInstantiationPayload(isPause, vnfs, 1, true, PROJECT_NAME, false);
    }

    protected ServiceInstantiation generatePre1806MacroTransportServiceInstantiationPayload(String tenantId, String lcpCloudRegionId) {
        List<Map<String, String>> instanceParams = ImmutableList
            .of(ImmutableMap.of("someUserParam","someValue", "anotherUserParam","anotherValue"));
        ServiceInstantiation serviceInstantiation = new ServiceInstantiation(createServiceModelInfo(), "038d99af-0427-42c2-9d15-971b99b9b489",
                "JULIO ERICKSON", "some_project_name", "some_subscriber_id", "some_subscriber_name",
                "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", null, "MOG", lcpCloudRegionId, null, tenantId,
                null, null, null, Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, instanceParams, false, 1, false, false,
                null, null, null, null, null, null,
                new VidNotions(InstantiationUI.TRANSPORT_SERVICE, ModelCategory.Transport, InstantiationUI.TRANSPORT_SERVICE, InstantiationType.Macro), "originalName"
        );
        return serviceInstantiation;
    }

    public static class NetworkDetails {

        public NetworkDetails(String name, String modelCustomizationId) {
            this.name = name;
            this.modelCustomizationId = modelCustomizationId;
        }

        public String name;
        public String modelCustomizationId;
    }

    protected Map<String, Network> createNetworkList(List instanceParams, List<NetworkDetails> networkDetails, boolean isALaCarte) {
        Stream<Network> networkStream = networkDetails.stream().map(
                details->new Network(createNetworkModelInfo(isALaCarte, details.modelCustomizationId), "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
                details.name, Action.Create.name(),
                "platformName", "mdt1", null, "88a6ca3ee0394ade9403f075db23167e", instanceParams,"lineOfBusinessName" ,
                false, null, UUID.randomUUID().toString(), null, null, null, "originalName"));
//        I can't tell why compiler don't like the statement if it's only one line...
        return networkStream.collect(Collectors.toMap(network -> network.getModelInfo().getModelCustomizationId(), network -> network));
    }

    protected InstanceGroup createInstanceGroup(boolean isUserProvidedNaming, Action action) {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("instanceGroup");
        modelInfo.setModelName("2016-73_MOW-AVPN-vPE-BV-L");
        modelInfo.setModelVersionId("7f40c192-f63c-463e-ba94-286933b895f8");
        modelInfo.setModelCustomizationName("2016-73_MOW-AVPN-vPE-BV-L 0");
        modelInfo.setModelCustomizationId("ab153b6e-c364-44c0-bef6-1f2982117f04");
        modelInfo.setModelInvariantId("11111111-f63c-463e-ba94-286933b895f9");
        modelInfo.setModelVersion("10.0");

        return new InstanceGroup(modelInfo, (isUserProvidedNaming ? VNF_GROUP_NAME : null), action.name(), false, null, emptyMap(), UUID.randomUUID().toString(), null, null,
            null, "originalName");
    }

    protected ModelInfo createServiceModelInfo() {
        ModelInfo siModelInfo = new ModelInfo();
        siModelInfo.setModelName("sriov");
        siModelInfo.setModelType("service");
        siModelInfo.setModelInvariantId("ff3514e3-5a33-55df-13ab-12abad84e7ff");
        siModelInfo.setModelVersionId("fe6985cd-ea33-3346-ac12-ab121484a3fe");
        siModelInfo.setModelVersion("1.0");

        return siModelInfo;
    }

    protected ModelInfo createServiceModelInfo(String modelName, String modelVersion, String modelVersionId, String modelInvariantId, String modelCustomizationId, String modelCustomizationName) {
        return createModelInfo("service", modelName, modelVersion, modelVersionId, modelInvariantId, modelCustomizationId, modelCustomizationName);
    }

    protected void createInstanceParamsMaps() {
        instanceParamsMapWithoutParams = new HashMap<>();
        instanceParamsMapWithoutParams.put("availability_zone_0" , "mtpocdv-kvm-az01");
        instanceParamsMapWithoutParams.put("vre_a_volume_size_0" , "100");

        vfModuleInstanceParamsMapWithParamsToRemove = new HashMap<>();
        vfModuleInstanceParamsMapWithParamsToRemove.put(AsyncInstantiationBusinessLogic.PARAMS_TO_IGNORE.get(0), "should be removed");
        vfModuleInstanceParamsMapWithParamsToRemove.put("availability_zone_0" , "mtpocdv-kvm-az01");
        vfModuleInstanceParamsMapWithParamsToRemove.put("vre_a_volume_size_0" , "100");

        vnfInstanceParamsMapWithParamsToRemove = new HashMap<>();
        vnfInstanceParamsMapWithParamsToRemove.put(AsyncInstantiationBusinessLogic.PARAMS_TO_IGNORE.get(1), "should be removed");
    }

    public static AsyncRequestStatus asyncRequestStatusResponse(String msoStatus) {
        AsyncRequestStatus asyncRequestStatus = new AsyncRequestStatus(new AsyncRequestStatus.Request(new RequestStatus()));
        asyncRequestStatus.request.requestStatus.setRequestState(msoStatus);
        asyncRequestStatus.request.requestId = UUID.randomUUID().toString();
        asyncRequestStatus.request.startTime = TimeUtils.zonedDateTimeToString(ZonedDateTime.now());
        return asyncRequestStatus;
    }

    public static RestObject<AsyncRequestStatus> asyncRequestStatusResponseAsRestObject(String msoStatus) {
        return asyncRequestStatusResponseAsRestObject(msoStatus, 200);
    }

    public static RestObject<AsyncRequestStatus> asyncRequestStatusResponseAsRestObject(String msoStatus, int httpStatusCode) {
        RestObject<AsyncRequestStatus> restObject = new RestObject<>();
        restObject.set(asyncRequestStatusResponse(msoStatus));
        restObject.setStatusCode(httpStatusCode);
        return restObject;
    }

    protected void mockAaiClientAnyNameFree() {
        when(aaiClient.isNodeTypeExistsByName(any(), any())).thenReturn(false);
    }

    protected ExceptionWithRequestInfo aaiNodeQueryBadResponseException() {
        return new ExceptionWithRequestInfo(HttpMethod.GET, "url", "raw data", 500, null);
    }

    protected ServiceInstantiation generateALaCarteWithVnfsServiceInstantiationPayload() {
        Map<String, Vnf> vnfs = createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, Collections.singletonList(vnfInstanceParamsMapWithParamsToRemove) , true, true);
        ServiceInstantiation serviceInstantiation = generateMockALaCarteServiceInstantiationPayload(false, vnfs, emptyMap(), emptyMap(), 1, true, PROJECT_NAME, false, "VNF_API");
        return serviceInstantiation;
    }

    protected ServiceInstantiation generateALaCarteWithNetworksPayload(List<NetworkDetails> networkDetails) {
        Map<String, Network> networks = createNetworkList(emptyList(), networkDetails, true);
        ServiceInstantiation serviceInstantiation = generateMockALaCarteServiceInstantiationPayload(false, emptyMap(), networks, emptyMap(), 1, true, PROJECT_NAME, false, "VNF_API");
        return serviceInstantiation;
    }

    protected ServiceInstantiation generateALaCarteUpdateWith1ExistingGroup2NewGroupsPayload() {
        final InstanceGroup instanceGroup1 = createInstanceGroup(true, Action.None);
        final InstanceGroup instanceGroup2 = createInstanceGroup(false, Action.Create);
        final InstanceGroup instanceGroup3 = createInstanceGroup(true, Action.Create);
        Map<String, InstanceGroup> groups = ImmutableMap.of(
                "foo:001", instanceGroup1,
                "foo:002", instanceGroup2,
                "foo:003", instanceGroup3
        );
        return generateMockServiceInstantiationPayload(false, emptyMap(), emptyMap(), groups,
                1, true, PROJECT_NAME, false, true, "VNF_API",
                Action.None, "1234567890");
    }

    protected ServiceInstantiation generateALaCarteServiceInstantiationPayload() {
        return generateMockALaCarteServiceInstantiationPayload(false, Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, 1, true, PROJECT_NAME, false, "VNF_API");
    }
}
