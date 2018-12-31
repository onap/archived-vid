package org.onap.vid.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.model.Action;
import org.onap.vid.model.serviceInstantiation.*;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.RequestStatus;
import org.onap.vid.utils.TimeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.togglz.core.manager.FeatureManager;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;

import static java.util.Collections.emptyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AsyncInstantiationBaseTest extends AbstractTestNGSpringContextTests {

    public static final String OWNING_ENTITY_ID = "038d99af-0427-42c2-9d15-971b99b9b489";
    public static final String PACKET_CORE = "PACKET CORE";
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

    @Inject
    protected FeatureManager featureManager;

    @Inject
    protected AaiClientInterface aaiClient;

    @Inject
    protected CloudOwnerService cloudOwnerService;

    public ServiceInstantiation generateMockMacroServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure) {
        return generateMockServiceInstantiationPayload(isPause, vnfs, Collections.EMPTY_MAP, Collections.EMPTY_MAP, bulkSize, isUserProvidedNaming, projectName, rollbackOnFailure, false, null, Action.Create, null);
    }

    public ServiceInstantiation generateMockALaCarteServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs, Map<String, Network> networks, Map<String, InstanceGroup> vnfGroups, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure, String testApi) {
        return generateMockServiceInstantiationPayload(isPause, vnfs, networks, vnfGroups, bulkSize, isUserProvidedNaming, projectName, rollbackOnFailure, true, testApi, Action.Create, null);
    }

    public ServiceInstantiation generateMockALaCarteServiceDeletionPayload(boolean isPause, Map<String, Vnf> vnfs, Map<String, Network> networks, Map<String, InstanceGroup> vnfGroups, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure, String testApi, String instanceId) {
        return generateMockServiceInstantiationPayload(isPause, vnfs, networks, vnfGroups, bulkSize, isUserProvidedNaming, projectName, rollbackOnFailure, true, testApi, Action.Delete, instanceId);
    }
    private ServiceInstantiation generateMockServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs, Map<String, Network> networks, Map<String, InstanceGroup> vnfGroups, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure, boolean isAlacarte, String testApi, Action action, String instanceId) {
        ModelInfo modelInfo = createModelInfo();

        List<Map<String,String>> instanceParams = createInstanceParams();

        return new ServiceInstantiation ( modelInfo,
                AsyncInstantiationBusinessLogicTest.OWNING_ENTITY_ID,
                AsyncInstantiationBusinessLogicTest.PACKET_CORE,
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
                instanceParams,
                isPause,
                bulkSize,
                rollbackOnFailure,
                isAlacarte,
                testApi,
                instanceId,
                action.name()
                );
    }

    private List<Map<String,String>> createInstanceParams() {
        List<Map<String, String>> instanceParams = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("instanceParams_test1" , "some text");
        map.put("instanceParams_test2" , "another text");
        instanceParams.add(map);
        return instanceParams;
    }

    protected VfModule createVfModule(String modelName, String modelVersionId, String modelCustomizationId,
                                    List<Map<String, String>> instanceParams, Map<String, String> supplementaryParams, String instanceName, String volumeGroupInstanceName, boolean isAlacarte) {
        ModelInfo vfModuleInfo = new ModelInfo();
        vfModuleInfo.setModelType("vfModule");
        vfModuleInfo.setModelName(modelName);
        vfModuleInfo.setModelVersionId(modelVersionId);
        vfModuleInfo.setModelCustomizationId(modelCustomizationId);
        vfModuleInfo.setModelCustomizationName(modelName);

        if (isAlacarte) {
            vfModuleInfo.setModelInvariantId("22222222-f63c-463e-ba94-286933b895f9");
            vfModuleInfo.setModelVersion("10.0");
            return new VfModule(vfModuleInfo, instanceName, volumeGroupInstanceName, Action.Create.name(), "mdt1", null, "88a6ca3ee0394ade9403f075db23167e", instanceParams, supplementaryParams, false, true, null);
        }

        return new VfModule(vfModuleInfo, instanceName, volumeGroupInstanceName, Action.Create.name(), null, null, null, instanceParams, supplementaryParams, false, false, null);
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

    private ModelInfo createNetworkModelInfo(boolean isAlacarte) {
        ModelInfo vnfModelInfo = new ModelInfo();
        vnfModelInfo.setModelType("network");
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

    private ModelInfo createModelInfo() {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("service");
        modelInfo.setModelVersionId(SERVICE_MODEL_VERSION_ID);
        modelInfo.setModelVersion("10.0");
        modelInfo.setModelInvariantId("5d48acb5-097d-4982-aeb2-f4a3bd87d31b");
        modelInfo.setModelName("MOW AVPN vMX BV vPE 1 Service");
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
        VfModule vfModule1 = createVfModule("201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0", VF_MODULE_0_MODEL_VERSION_ID, VF_MODULE_0_MODEL_CUSTOMIZATION_NAME, instanceParams1, new HashMap<>(), (isUserProvidedNaming ? "vmxnjr001_AVPN_base_vPE_BV_base" : null), null, isAlacarte);
        List<Map<String, String>> instanceParams2 = ImmutableList.of(vfModuleInstanceParamsMap);
        VfModule vfModule2 = createVfModule("201673MowAvpnVpeBvL..AVPN_vRE_BV..module-1", VF_MODULE_1_MODEL_VERSION_ID, VF_MODULE_1_MODEL_CUSTOMIZATION_NAME, instanceParams2, new HashMap<>(), (isUserProvidedNaming ? "vmxnjr001_AVPN_base_vRE_BV_expansion": null), (isUserProvidedNaming ? "myVgName" : null), isAlacarte);

        String vfModuleModelName = vfModule1.getModelInfo().getModelName();
        vfModules.put(vfModuleModelName, new LinkedHashMap<>());

        vfModules.get(vfModuleModelName).put(vfModuleModelName + ":001", vfModule1);
        vfModules.get(vfModuleModelName).put(vfModuleModelName + ":002", vfModule2);

        Vnf vnf = new Vnf(vnfModelInfo, "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", (isUserProvidedNaming ? VNF_NAME : null), Action.Create.name(),
                "platformName", "mdt1", null, "88a6ca3ee0394ade9403f075db23167e", vnfInstanceParams,"lineOfBusinessName" , false, null, vfModules);

        vnfs.put(vnf.getModelInfo().getModelName(), vnf);
        return vnfs;
    }

    protected Map<String, Network> createNetworkList(List vnfInstanceParams, boolean isUserProvidedNaming, boolean isALaCarte) {
        Map<String, Network> networks = new HashMap<>();
        ModelInfo networkModelInfo = createNetworkModelInfo(isALaCarte);

        Network network = new Network(networkModelInfo, "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", isUserProvidedNaming ? VNF_NAME : null, Action.Create.name(),
                "platformName", "mdt1", null, "88a6ca3ee0394ade9403f075db23167e", vnfInstanceParams,"lineOfBusinessName" , false, null);

        networks.put(network.getModelInfo().getModelName(), network);
        return networks;
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

        return new InstanceGroup(modelInfo, (isUserProvidedNaming ? VNF_GROUP_NAME : null), action.name(), false, null);
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

    protected RestObject<AsyncRequestStatus> asyncRequestStatusResponseAsRestObject(String msoStatus) {
        return asyncRequestStatusResponseAsRestObject(msoStatus, 200);
    }

    protected RestObject<AsyncRequestStatus> asyncRequestStatusResponseAsRestObject(String msoStatus, int httpStatusCode) {
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
        Map<String, Vnf> vnfs = createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, Collections.singletonList(vnfInstanceParamsMapWithParamsToRemove) , true);
        ServiceInstantiation serviceInstantiation = generateMockALaCarteServiceInstantiationPayload(false, vnfs, emptyMap(), emptyMap(), 1, true, PROJECT_NAME, false, "VNF_API");
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
}
