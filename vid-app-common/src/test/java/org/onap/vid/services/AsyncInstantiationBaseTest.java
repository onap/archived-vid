package org.onap.vid.services;

import com.google.common.collect.ImmutableMap;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.domain.mso.ModelInfo;
import org.onap.vid.domain.mso.RequestStatus;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AsyncInstantiationBusinessLogicTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.togglz.core.manager.FeatureManager;

import javax.inject.Inject;
import java.util.*;

import static org.mockito.Matchers.any;
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

    protected HashMap<String, String> instanceParamsMapWithoutParams;
    protected HashMap<String, String> vfModuleInstanceParamsMapWithParamsToRemove;
    protected HashMap<String, String> vnfInstanceParamsMapWithParamsToRemove;

    @Inject
    protected FeatureManager featureManager;

    @Inject
    protected AaiClientInterface aaiClient;

    public ServiceInstantiation generateMockServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs, int bulkSize, boolean isUserProvidedNaming, String projectName, boolean rollbackOnFailure) {
        ModelInfo modelInfo = createModelInfo();

        List<Map<String,String>> instanceParams = createInstanceParams();

        return new ServiceInstantiation (
                modelInfo,
                AsyncInstantiationBusinessLogicTest.OWNING_ENTITY_ID,
                AsyncInstantiationBusinessLogicTest.PACKET_CORE,
                projectName,
                AsyncInstantiationBusinessLogicTest.SUBSCRIBER_ID,
                AsyncInstantiationBusinessLogicTest.SUBSCRIBER_NAME,
                AsyncInstantiationBusinessLogicTest.PRODUCT_FAMILY_ID,
                isUserProvidedNaming ? AsyncInstantiationBusinessLogicTest.INSTANCE_NAME : ""  ,
                isUserProvidedNaming,
                AsyncInstantiationBusinessLogicTest.SUBSCRIPTION_SERVICE_TYPE,
                AsyncInstantiationBusinessLogicTest.LCP_CLOUD_REGION_ID,
                AsyncInstantiationBusinessLogicTest.A6CA3EE0394ADE9403F075DB23167E,
                AsyncInstantiationBusinessLogicTest.TENANT_NAME,
                AsyncInstantiationBusinessLogicTest.AIC_ZONE_ID,
                AsyncInstantiationBusinessLogicTest.AIC_ZONE_NAME,
                vnfs,
                instanceParams,
                isPause,
                bulkSize,
                rollbackOnFailure
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

    private VfModule createVfModule(String modelName, String modelVersionId, String modelCustomizationId,
                                    List<Map<String, String>> instanceParams, String instanceName, String volumeGroupInstanceName) {
        ModelInfo vfModuleInfo = new ModelInfo();
        vfModuleInfo.setModelType("vfModule");
        vfModuleInfo.setModelName(modelName);
        vfModuleInfo.setModelVersionId(modelVersionId);
        vfModuleInfo.setModelCustomizationId(modelCustomizationId);
        return new VfModule(vfModuleInfo , instanceName, volumeGroupInstanceName, instanceParams);
    }

    private ModelInfo createVnfModelInfo() {
        ModelInfo vnfModelInfo = new ModelInfo();
        vnfModelInfo.setModelType("vnf");
        vnfModelInfo.setModelName("2016-73_MOW-AVPN-vPE-BV-L");
        vnfModelInfo.setModelVersionId("7f40c192-f63c-463e-ba94-286933b895f8");
        vnfModelInfo.setModelCustomizationName("2016-73_MOW-AVPN-vPE-BV-L 0");
        vnfModelInfo.setModelCustomizationId("ab153b6e-c364-44c0-bef6-1f2982117f04");
        return vnfModelInfo;
    }

    private ModelInfo createModelInfo() {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType("service");
        modelInfo.setModelVersionId("3c40d244-808e-42ca-b09a-256d83d19d0a");
        modelInfo.setModelVersion("10.0");
        modelInfo.setModelInvariantId("5d48acb5-097d-4982-aeb2-f4a3bd87d31b");
        modelInfo.setModelName("MOW AVPN vMX BV vPE 1 Service");
        return modelInfo;
    }

    protected Map<String, Vnf> createVnfList(HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams, boolean isUserProvidedNaming) {
        Map<String, Vnf> vnfs = new HashMap<>();
        ModelInfo vnfModelInfo = createVnfModelInfo();

        Map<String, Map<String, VfModule>> vfModules = new HashMap<>();

        List<Map<String, String>> instanceParams1 =ImmutableList.of((ImmutableMap.of("vmx_int_net_len", "24")));
        VfModule vfModule1 = createVfModule("201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0", "4c75f813-fa91-45a4-89d0-790ff5f1ae79", "a25e8e8c-58b8-4eec-810c-97dcc1f5cb7f", instanceParams1, "vmxnjr001_AVPN_base_vPE_BV_base", null);
        List<Map<String, String>> instanceParams2 = ImmutableList.of(vfModuleInstanceParamsMap);
        VfModule vfModule2 = createVfModule("201673MowAvpnVpeBvL..AVPN_vRE_BV..module-1", "56e2b103-637c-4d1a-adc8-3a7f4a6c3240", "72d9d1cd-f46d-447a-abdb-451d6fb05fa8", instanceParams2, "vmxnjr001_AVPN_base_vRE_BV_expansion", "myVgName");

        String vfModuleModelName = vfModule1.getModelInfo().getModelName();
        vfModules.put(vfModuleModelName, new LinkedHashMap<>());

        vfModules.get(vfModuleModelName).put(vfModule1.getInstanceName(),vfModule1);
        vfModules.get(vfModuleModelName).put(vfModule2.getInstanceName(), vfModule2);

        Vnf vnf = new Vnf(vnfModelInfo, "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", "vmxnjr001", isUserProvidedNaming,
                "platformName", "mdt1", "88a6ca3ee0394ade9403f075db23167e", vnfInstanceParams,"lineOfBusinessName" ,vfModules);

        vnfs.put(vnf.getInstanceName(), vnf);
        return vnfs;
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

    protected AsyncRequestStatus asyncRequestStatusResponse(String msoStatus) {
        AsyncRequestStatus asyncRequestStatus = new AsyncRequestStatus(new AsyncRequestStatus.Request(new RequestStatus()));
        asyncRequestStatus.request.requestStatus.setRequestState(msoStatus);
        asyncRequestStatus.request.requestId = UUID.randomUUID().toString();
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
        when(aaiClient.searchNodeTypeByName(any(), any())).thenReturn(aaiNodeQueryResponseNameFree());
    }

    protected AaiResponse<AaiNodeQueryResponse> aaiNodeQueryResponseNameFree() {
        return new AaiResponse<>(new AaiNodeQueryResponse(null),"", 200);
    }

    protected AaiResponse<AaiNodeQueryResponse> aaiNodeQueryBadResponse() {
        return new AaiResponse<>(null,"", 404);
    }

    protected AaiResponse<AaiNodeQueryResponse> aaiNodeQueryResponseNameUsed(ResourceType type) {
        AaiNodeQueryResponse mockAaiNodeQuery = new AaiNodeQueryResponse(ImmutableList.of(new AaiNodeQueryResponse.ResultData(type, "/some/mocked/link")));
        return new AaiResponse<>(mockAaiNodeQuery,"", 200);
    }
}
