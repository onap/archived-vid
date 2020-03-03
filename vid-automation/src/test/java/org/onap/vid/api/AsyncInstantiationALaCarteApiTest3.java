package org.onap.vid.api;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost.DEFAULT_REQUEST_ID;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet.COMPLETE;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.RELATED_VNF1_ACTION;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.RELATED_VNF2_ACTION;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VNF_GROUP1_ACTION;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAISearchNodeQueryNonEmptyResult;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOAddOrRemoveOneInstanceGroupMember;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOAddOrRemoveOneInstanceGroupMember.InstanceGroupMemberAction;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateNetworkALaCarteServiceCypress2;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceMultipleVnfsServiceCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVnfALaCarteServiceCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVnfALaCarteServiceCypress2;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVnfGroup;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteBaseVfModuleCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteNetworkAlaCarteCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteVfModuleCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteVnfAlaCarteCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.onap.vid.model.asyncInstantiation.JobAuditStatus;
import org.onap.vid.model.asyncInstantiation.JobAuditStatus.SourceStatus;
import org.onap.vid.model.asyncInstantiation.ServiceInfo;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.model.JobStatus;
import vid.automation.test.model.ServiceAction;
import vid.automation.test.services.AsyncJobsService;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;

@FeatureTogglingTest({Features.FLAG_ASYNC_ALACARTE_VNF})
public class AsyncInstantiationALaCarteApiTest3 extends AsyncInstantiationBase {

    private static final String CREATE_BULK_OF_ALACARTE_REQUEST = "asyncInstantiation/vidRequestCreateALaCarte.json";
    private static final String CREATE_BULK_OF_ALACARTE_MULTIPLE_VNF_NETWORK_REQUEST_CYPRESS = "a-la-carte/redux-multiple-vnf-network.json";
    private static final String CREATE_BULK_OF_ALACARTE_REQUEST_WITH_VNF_GROUP = "VnfGroup/serviceWithVnfGroupCreateRequest.json";
    private static final String PAYLOAD_TEMPLATE_1_VNF_GROUP_WITH_3_MEMBERS_REQUEST = "VnfGroup/payloadTemplate1VnfGroupWith3MembersRequest.json";
    private static final String DELETE_AND_CREATE_NETWORK_FROM_SERVICE = "asyncInstantiation/vidRequestDelete1Create1Network.json";
    private static final String DELETE_SERVICE_WITH_NETWORK = "asyncInstantiation/vidRequestDeleteServiceWithNetwork.json";
    private static final String DELETE_AND_CREATE_VNF_FROM_SERVICE = "asyncInstantiation/vidRequestDelete1Create1Vnf.json";
    private static final String DELETE_SERVICE_WITH_VNF = "asyncInstantiation/vidRequestDeleteServiceWithVnf.json";


    private static final String FIRST_REQUEST_ID = "d1011670-0e1a-4b74-945d-8bf5aede1d9c";
    private static final String SECOND_REQUEST_ID = "e2011670-0e1a-4b74-945d-8bf5aede1d9c";
    private static final String THIRD_REQUEST_ID = "f3011670-0e1a-4b74-945d-8bf5aede1d9c";
    private static final String SERVICE_INSTANCE_ID = BaseMSOPreset.DEFAULT_INSTANCE_ID;
    private static final String MSO_COMPLETE_STATUS = "COMPLETE";
    private static final String MSO_FAILED_STATUS = "FAILED";


    @DataProvider
    public static Object[][] scenarios() {
        return new Object[][]{
                {Scenario.PARALLEL},
                {Scenario.COMPLETED},
//                {Scenario.NAME_TAKEN}, Not relevant because the name uniqueness is supported only for bulk in Macro
//                {Scenario.DUPLICATE_NAME}, Not relevant because name duplication is not handled in A La Carte
                {Scenario.IN_PROGRESS},
                {Scenario.MSO_FAIL}
        };
    }

    @AfterMethod
    protected void dropAllFromNameCounter() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.dropAllFromNameCounter();
    }

    @Test(dataProvider = "multipleVnfDataProvider")
    public void deployServiceFromCypress__multipleVnfsAndNetwork(String expectedStatus, JobStatus expectedJobStatus) {
        dropAllFromNameCounter();// needed because each data provider info not going to after method
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap.of(SERVICE_NAME, "serviceInstanceName");

        List<PresetMSOBaseCreateInstancePost> createPresets =   ImmutableList.of(
                new PresetMSOCreateServiceInstanceMultipleVnfsServiceCypress(names, 0, DEFAULT_REQUEST_ID),
                new PresetMSOCreateVnfALaCarteServiceCypress(FIRST_REQUEST_ID, SERVICE_INSTANCE_ID,"VFvGeraldine00001", "zzz1"),
                new PresetMSOCreateVnfALaCarteServiceCypress(SECOND_REQUEST_ID, SERVICE_INSTANCE_ID,"VFvGeraldine00001_001", "ONAP"),
                new PresetMSOCreateNetworkALaCarteServiceCypress2(THIRD_REQUEST_ID, SERVICE_INSTANCE_ID, "ExtVL")
        );
        List<PresetMSOOrchestrationRequestGet> inProgressPresets =  ImmutableList.of(
                new PresetMSOOrchestrationRequestGet(expectedStatus.equals("SERVICE_FAILED") ? MSO_FAILED_STATUS : MSO_COMPLETE_STATUS, DEFAULT_REQUEST_ID),
                new PresetMSOOrchestrationRequestGet( MSO_COMPLETE_STATUS, FIRST_REQUEST_ID, "First VNF instance was created successfully." ),
                new PresetMSOOrchestrationRequestGet(expectedStatus, SECOND_REQUEST_ID, expectedStatus.equals(MSO_COMPLETE_STATUS)?"Second VNF instance was created successfully.": MSO_BASE_ERROR),
                new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, THIRD_REQUEST_ID,"Network was created successfully.")
        );
        List<BasePreset> presets = getPresets( createPresets, inProgressPresets);

        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
        registerExpectationFromPreset(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC, RegistrationStrategy.APPEND);

        final List<String> uuids = createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_MULTIPLE_VNF_NETWORK_REQUEST_CYPRESS);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        //expected vid statuses
        ImmutableList<JobAuditStatus> vidAuditStatuses;

        if (expectedStatus.equals(MSO_COMPLETE_STATUS)){
            vidAuditStatuses = vidAuditStatusesCompleted(jobId);
        } else if (expectedStatus.equals("SERVICE_FAILED")){
            vidAuditStatuses = vidAuditStatusesFailed(jobId);
        } else {
            vidAuditStatuses = vidAuditStatusesCompletedWithErrors(jobId);
        }

        assertServiceInfoSpecific3(jobId, expectedJobStatus , names.get(SERVICE_NAME));
        assertAuditStatuses(jobId, vidAuditStatuses, null);
    }


    @DataProvider
    Object[][] multipleVnfDataProvider() {
        return new Object[][]{{MSO_FAILED_STATUS, JobStatus.COMPLETED_WITH_ERRORS},{MSO_COMPLETE_STATUS, JobStatus.COMPLETED}, {"SERVICE_FAILED", JobStatus.FAILED}};
    }

    @Test
    @FeatureTogglingTest(Features.FLAG_1902_VNF_GROUPING)
    public void deploy1ServiceWith1VnfGroup() {
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap.of(SERVICE_NAME, "Grouping_Service_Instance");
        String serviceReqId = "3cf5ea96-6b34-4945-b5b1-4a7798b1caf2";
        String serviceInstanceId = BaseMSOPreset.DEFAULT_INSTANCE_ID;
        String instanceGroupReqId = "715a5106-cdcc-44ee-8923-83d68a896908";

        List<PresetMSOBaseCreateInstancePost> createPresets = ImmutableList.of(
                new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService(names, 0, serviceReqId),
                new PresetMSOCreateVnfGroup("ABC", instanceGroupReqId, PresetMSOCreateVnfGroup.MODEL_INFO_1, serviceInstanceId, false));
        List<PresetMSOOrchestrationRequestGet> inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet("COMPLETE", serviceReqId),
                new PresetMSOOrchestrationRequestGet("COMPLETE", instanceGroupReqId, "Instance group was created successfully."));
        List<BasePreset> presets = getPresets(createPresets, inProgressPresets);

        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);

        final List<String> uuids = createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_REQUEST_WITH_VNF_GROUP);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        assertExpectedStatusAndServiceInfo(JobStatus.COMPLETED, jobId, new ServiceInfo(
                "us16807000", JobStatus.COMPLETED, false,
                "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "WayneHolland", "WATKINS",
                null, null,
                null, null,
                null, null,
                "TYLER SILVIA", "SILVIA ROBBINS",
                null, names.get(SERVICE_NAME),
                "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc", "Grouping Service for Test", "1.0",
                jobId, null, ServiceAction.INSTANTIATE, false));


        assertAuditStatuses(jobId, vidAuditStatusesCompleted(jobId),null);

        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry(createPresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(createPresets.get(1).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(1).getReqPath(), 1L)));
    }

    @DataProvider
    public static Object[][] addAndDeleteMembersMsoStatus() {
        return new Object[][]{
                {MSO_COMPLETE_STATUS, MSO_COMPLETE_STATUS, JobStatus.COMPLETED},
                {MSO_FAILED_STATUS, MSO_FAILED_STATUS, JobStatus.FAILED},
                {MSO_COMPLETE_STATUS, MSO_FAILED_STATUS, JobStatus.COMPLETED_WITH_ERRORS}
        };
    }

    @Test(dataProvider = "addAndDeleteMembersMsoStatus")
    @FeatureTogglingTest(Features.FLAG_1902_VNF_GROUPING)
    public void add1delete1GroupMembers_withGoodResponseFromMso_verifyStatusAndRequests(String firstMemberStatus, String secondMemberStatus, JobStatus expectedJobStatus) {

        String firstMemberRequestId = UUID.randomUUID().toString();
        String secondMemberRequestId = UUID.randomUUID().toString();

        List<PresetMSOAddOrRemoveOneInstanceGroupMember> instanceGroupMemberPreset = ImmutableList.of(
                new PresetMSOAddOrRemoveOneInstanceGroupMember("VNF_GROUP1_INSTANCE_ID", "RELATED_VNF1_INSTANCE_ID", getUserCredentials().getUserId(), firstMemberRequestId, InstanceGroupMemberAction.Add),
                new PresetMSOAddOrRemoveOneInstanceGroupMember("VNF_GROUP1_INSTANCE_ID", "RELATED_VNF2_INSTANCE_ID", getUserCredentials().getUserId(), secondMemberRequestId, InstanceGroupMemberAction.Remove)
        );
        List<PresetMSOOrchestrationRequestGet> inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet(firstMemberStatus, firstMemberRequestId),
                new PresetMSOOrchestrationRequestGet(secondMemberStatus, secondMemberRequestId)
        );

        List<BasePreset> presets = getGroupMembersPresets(instanceGroupMemberPreset,inProgressPresets);
        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
        final List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(
                VNF_GROUP1_ACTION, "None",
                RELATED_VNF1_ACTION, "Create",
                RELATED_VNF2_ACTION, "None_Delete"
        ), PAYLOAD_TEMPLATE_1_VNF_GROUP_WITH_3_MEMBERS_REQUEST);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        assertAuditStatuses(jobId, vidAuditStatuses(jobId, expectedJobStatus), null, 60);
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry(instanceGroupMemberPreset.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(instanceGroupMemberPreset.get(1).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(1).getReqPath(), 1L)
        ));
    }

    @Test
    @FeatureTogglingTest(Features.FLAG_1902_VNF_GROUPING)
    public void addVnfGroupWith2GroupMembers_withGoodResponseFromMso_verifyStatusAndRequests() {

        String vnfGroupRequestId = UUID.randomUUID().toString();
        String firstMemberRequestId = UUID.randomUUID().toString();
        String secondMemberRequestId = UUID.randomUUID().toString();

        List <PresetMSOCreateVnfGroup> vnfGroupPreset = ImmutableList.of(
                new PresetMSOCreateVnfGroup("VNF_GROUP1_INSTANCE_NAME",vnfGroupRequestId,PresetMSOCreateVnfGroup.MODEL_INFO_0,"service-instance-id", true));
        List<PresetMSOAddOrRemoveOneInstanceGroupMember> instanceGroupMemberPreset = ImmutableList.of(
                new PresetMSOAddOrRemoveOneInstanceGroupMember("VNF_GROUP1_INSTANCE_ID", "RELATED_VNF1_INSTANCE_ID", getUserCredentials().getUserId(), firstMemberRequestId, InstanceGroupMemberAction.Add),
                new PresetMSOAddOrRemoveOneInstanceGroupMember("VNF_GROUP1_INSTANCE_ID", "RELATED_VNF2_INSTANCE_ID", getUserCredentials().getUserId(), secondMemberRequestId, InstanceGroupMemberAction.Add)
        );
        List<PresetMSOOrchestrationRequestGet> inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, vnfGroupRequestId),
                new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, firstMemberRequestId),
                new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, secondMemberRequestId)
        );

        final ImmutableList.Builder<BasePreset> basePresetBuilder = new ImmutableList.Builder<>();
        basePresetBuilder
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .addAll(instanceGroupMemberPreset)
                .addAll(inProgressPresets)
                .addAll(vnfGroupPreset);
        List<BasePreset> presets = basePresetBuilder.build();

        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
        final List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(
                VNF_GROUP1_ACTION, "Create",
                RELATED_VNF1_ACTION, "Create",
                RELATED_VNF2_ACTION, "Create"
        ), PAYLOAD_TEMPLATE_1_VNF_GROUP_WITH_3_MEMBERS_REQUEST);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        assertAuditStatuses(jobId, vidAuditStatusesCompleted(jobId), null, 60);
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry(vnfGroupPreset.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(instanceGroupMemberPreset.get(0).getReqPath(), 2L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(1).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(2).getReqPath(), 1L)
        ));
    }

    @Test(dataProvider = "scenarios")
    public void aLaCarteServiceScenarioRunner(Scenario scenario) {
        /*
        This tests creates one or more a-la-carte requests, following one of the these scenarios:

        MSO_FAIL:        Submits to MSO, but while getting Orchestration status -> MSO
                         reports failure
        COMPLETED:       Clean legit flow.
        NAME_TAKEN:      Sends request for instance, where AAI reports the name is
                         already taken. Therefore, MSO expects added postfix _001.
        IN_PROGRESS:     Submits to MSO, but while getting Orchestration status -> MSO
                         reports IN_PROGRESS endlessly
        DUPLICATE_NAME:  Sends two requests for instances with the same name.
        PARALLEL:        Submits 3 requests, that must not interfere with each other.
         */

        registerExpectationFromPresets(ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet()
        ), RegistrationStrategy.CLEAR_THEN_SET);


        final String name0 = randomAlphabetic(18);

        OneServiceActor inProgressCase = new OneServiceActor(
                scenario == Scenario.PARALLEL || scenario == Scenario.IN_PROGRESS || scenario == Scenario.DUPLICATE_NAME,
                "IN_PROGRESS", "status #1",
                ImmutableMap.of(SERVICE_NAME, name0), 0,
                JobStatus.IN_PROGRESS, this::vidAuditStatusesInProgress
        );

        OneServiceActor completedCase = new OneServiceActor(
                scenario == Scenario.PARALLEL || scenario == Scenario.COMPLETED,
                MSO_COMPLETE_STATUS, "status #2",
                ImmutableMap.of(SERVICE_NAME, randomAlphabetic(5)), 0,
                JobStatus.COMPLETED, this::vidAuditStatusesCompleted
        );

        final String name1 = randomAlphabetic(5);
        OneServiceActor occupiedNameCase = new OneServiceActor(
                scenario == Scenario.NAME_TAKEN,
                MSO_COMPLETE_STATUS, "status #6",
                ImmutableMap.of(SERVICE_NAME, name1), 1,
                JobStatus.COMPLETED, this::vidAuditStatusesCompleted,
                ImmutableList.of(new PresetAAISearchNodeQueryNonEmptyResult("service-instance", name1))
        );

        OneServiceActor failedCase = new OneServiceActor(
                scenario == Scenario.PARALLEL || scenario == Scenario.MSO_FAIL,
                "FAILED", "status #3",
                ImmutableMap.of(SERVICE_NAME, randomAlphabetic(10)), 0,
                JobStatus.FAILED, this::vidAuditStatusesFailed
        );

        OneServiceActor duplicateNameCase = new OneServiceActor(
                scenario == Scenario.DUPLICATE_NAME,
                MSO_COMPLETE_STATUS, "status #4",
                ImmutableMap.of(SERVICE_NAME, name0), 1,
                JobStatus.COMPLETED, this::vidAuditStatusesCompleted
        );

        OneServiceActor inProgressTooLongCase = new OneServiceActor(
                scenario == Scenario.IN_PROGRESS,
                "IN_PROGRESS", "status #5",
                ImmutableMap.of(SERVICE_NAME, randomAlphabetic(10)), 0,
                JobStatus.FAILED, this::vidAuditStatusesFailed, 24
        );

        final List<OneServiceActor> servicesActors =
                ImmutableList.of(inProgressCase, completedCase, occupiedNameCase, failedCase, duplicateNameCase, inProgressTooLongCase);

        servicesActors.forEach(actor -> registerExpectationFromPresets(actor.getPresets(),
                RegistrationStrategy.APPEND));

        servicesActors.forEach(OneServiceActor::createInstances);

        servicesActors.forEach(OneServiceActor::assertServiceInfo);
        servicesActors.forEach(OneServiceActor::assertAuditStatuses2);
    }

    @Test
    public void delete1Create1NetworkFromService() {
        String deleteRequestId = UUID.randomUUID().toString();
        String createRequestId = UUID.randomUUID().toString();
        String serviceInstanceId = BaseMSOPreset.DEFAULT_INSTANCE_ID;
        String networkInstanceId = "NETWORK_INSTANCE_ID";

        registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIGetSubscribersGet(),
                new PresetMSODeleteNetworkAlaCarteCypress(deleteRequestId, serviceInstanceId, networkInstanceId, "us16807000"),
                new PresetMSOOrchestrationRequestGet(COMPLETE, deleteRequestId),
                new PresetMSOCreateNetworkALaCarteServiceCypress2(createRequestId, serviceInstanceId, "ExtVL", "action-data", "6b528779-44a3-4472-bdff-9cd15ec93450",
                    "xxx1,platform"),
                new PresetMSOOrchestrationRequestGet(COMPLETE, createRequestId),
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC
        ), RegistrationStrategy.CLEAR_THEN_SET);

        List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), DELETE_AND_CREATE_NETWORK_FROM_SERVICE);
        assertThat(uuids, hasSize(1));
        String jobId = uuids.get(0);

        assertExpectedStatusAndServiceInfo(JobStatus.COMPLETED, jobId, PATIENCE_LEVEL.FAIL_SLOW, new ServiceInfo(
                "us16807000", JobStatus.COMPLETED, false,
                "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "WayneHolland", "WATKINS",
                "JAG1", "YUDFJULP-JAG1",
                "092eb9e8e4b7412e8787dd091bc58e86", "USP-SIP-IC-24335-T-01",
                "AAIAIC25", null,
                "TYLER SILVIA", null,
                null, "InstanceName",
                "6b528779-44a3-4472-bdff-9cd15ec93450", "action-data", "1.0",
                jobId, null, ServiceAction.UPDATE, false)
        );
    }

    @Test
    public void deleteServiceWithNetwork() {
        String deleteNetworkRequestId = UUID.randomUUID().toString();
        String deleteServiceRequestId = UUID.randomUUID().toString();
        String serviceInstanceId = BaseMSOPreset.DEFAULT_INSTANCE_ID;
        String networkInstanceId = "NETWORK_INSTANCE_ID";

        registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIGetSubscribersGet(),
                new PresetMSODeleteNetworkAlaCarteCypress(deleteNetworkRequestId, serviceInstanceId, networkInstanceId, "us16807000"),
                new PresetMSOOrchestrationRequestGet(COMPLETE, deleteNetworkRequestId),
                new PresetMSODeleteService(deleteServiceRequestId, serviceInstanceId),
                new PresetMSOOrchestrationRequestGet(COMPLETE, deleteServiceRequestId),
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC
        ), RegistrationStrategy.CLEAR_THEN_SET);

        List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), DELETE_SERVICE_WITH_NETWORK);
        assertThat(uuids, hasSize(1));
        String jobId = uuids.get(0);

        assertExpectedStatusAndServiceInfo(JobStatus.COMPLETED, jobId, PATIENCE_LEVEL.FAIL_SLOW, new ServiceInfo(
                "us16807000", JobStatus.COMPLETED, false,
                "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "WayneHolland", "WATKINS",
                "JAG1", "YUDFJULP-JAG1",
                "092eb9e8e4b7412e8787dd091bc58e86", "USP-SIP-IC-24335-T-01",
                "AAIAIC25", null,
                "TYLER SILVIA", null,
                null, "InstanceName",
                "6b528779-44a3-4472-bdff-9cd15ec93450", "action-data", "1.0",
                jobId, null, ServiceAction.DELETE, false)
        );
    }

    @Test
    public void delete1VnfWithVfModulesAndCreate1VnfFromService() {
        String deleteVnfRequestId = UUID.randomUUID().toString();
        String createVnfRequestId = UUID.randomUUID().toString();
        String deleteVfModuleRequestId = UUID.randomUUID().toString();
        String deleteBaseVfModuleRequestId = UUID.randomUUID().toString();
        String serviceInstanceId = BaseMSOPreset.DEFAULT_INSTANCE_ID;
        String vnfInstanceId = "VNF_INSTANCE_ID";

        registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIGetSubscribersGet(),
                new PresetMSODeleteVnfAlaCarteCypress(deleteVnfRequestId, serviceInstanceId, vnfInstanceId, "us16807000"),
                new PresetMSOOrchestrationRequestGet(COMPLETE, deleteVnfRequestId),
                new PresetSDCGetServiceToscaModelGet("6b528779-44a3-4472-bdff-9cd15ec93450", "csar-withDynamicFields-ecompNamingFalse-partialModelDetails-vnfEcompNamingFalse.zip"),
                new PresetSDCGetServiceMetadataGet("6b528779-44a3-4472-bdff-9cd15ec93450", "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0" , "csar-withDynamicFields-ecompNamingFalse-partialModelDetails-vnfEcompNamingFalse.zip"),
                new PresetMSODeleteVfModuleCypress(deleteVfModuleRequestId, serviceInstanceId, vnfInstanceId, "VF_MODULE_INSTANCE_ID"),
                new PresetMSOOrchestrationRequestGet(COMPLETE, deleteVfModuleRequestId),
                new PresetMSODeleteBaseVfModuleCypress(deleteBaseVfModuleRequestId, serviceInstanceId, vnfInstanceId, "VF_MODULE_BASE_INSTANCE_ID"),
                new PresetMSOOrchestrationRequestGet(COMPLETE, deleteBaseVfModuleRequestId),
                new PresetMSOCreateVnfALaCarteServiceCypress2(createVnfRequestId, serviceInstanceId, "2017388_PASQUALEvPEmCaNkinstanceName", "zzz1"),
                new PresetMSOOrchestrationRequestGet(COMPLETE, createVnfRequestId),
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_SOME_LEGACY_REGION_TO_ATT_AIC,
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC,
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_AAIAIC25_TO_ATT_AIC
        ), RegistrationStrategy.CLEAR_THEN_SET);

        List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), DELETE_AND_CREATE_VNF_FROM_SERVICE);
        assertThat(uuids, hasSize(1));
        String jobId = uuids.get(0);

        assertExpectedStatusAndServiceInfo(JobStatus.COMPLETED, jobId, PATIENCE_LEVEL.FAIL_SLOW, new ServiceInfo(
                "us16807000", JobStatus.COMPLETED, false,
                "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "WayneHolland", "WATKINS",
                "NFT1", "NFTJSSSS-NFT1",
                "bae71557c5bb4d5aac6743a4e5f1d054", "AIN Web Tool-15-D-testalexandria",
                "hvf6", null,
                "TYLER SILVIA", null,
                "f8791436-8d55-4fde-b4d5-72dd2cf13cfb", "mCaNkinstancename",
                "6b528779-44a3-4472-bdff-9cd15ec93450", "action-data", "1.0",
                jobId, null, ServiceAction.UPDATE, false)
        );
    }

    @Test
    public void deleteServiceWithVnf() {
        String deleteVnfRequestId = UUID.randomUUID().toString();
        String deleteServiceRequestId = UUID.randomUUID().toString();
        String serviceInstanceId = BaseMSOPreset.DEFAULT_INSTANCE_ID;
        String vnfInstanceId = "VNF_INSTANCE_ID";

        registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIGetSubscribersGet(),
                new PresetMSODeleteVnfAlaCarteCypress(deleteVnfRequestId, serviceInstanceId, vnfInstanceId, "us16807000"),
                new PresetMSOOrchestrationRequestGet(COMPLETE, deleteVnfRequestId),
                new PresetMSODeleteService(deleteServiceRequestId, serviceInstanceId),
                new PresetMSOOrchestrationRequestGet(COMPLETE, deleteServiceRequestId),
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_SOME_LEGACY_REGION_TO_ATT_AIC
        ), RegistrationStrategy.CLEAR_THEN_SET);

        List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), DELETE_SERVICE_WITH_VNF);
        assertThat(uuids, hasSize(1));
        String jobId = uuids.get(0);

        assertExpectedStatusAndServiceInfo(JobStatus.COMPLETED, jobId, PATIENCE_LEVEL.FAIL_SLOW, new ServiceInfo(
                "us16807000", JobStatus.COMPLETED, false,
                "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "WayneHolland", "WATKINS",
                "NFT1", "NFTJSSSS-NFT1",
                "bae71557c5bb4d5aac6743a4e5f1d054", "AIN Web Tool-15-D-testalexandria",
                "hvf6", null,
                "TYLER SILVIA", null,
                "f8791436-8d55-4fde-b4d5-72dd2cf13cfb", "mCaNkinstancename",
                "6b528779-44a3-4472-bdff-9cd15ec93450", "action-data", "1.0",
                jobId, null, ServiceAction.DELETE, false)
        );
    }

    private ImmutableList<JobAuditStatus> vidAuditStatusesInProgress(String jobId) {
        return ImmutableList.of(
                vidAuditStatus(jobId, "PENDING", false),
                vidAuditStatus(jobId, "IN_PROGRESS", false)
        );
    }

    private ImmutableList<JobAuditStatus> vidAuditStatusesCompleted(String jobId) {
        return ImmutableList.of(
                vidAuditStatus(jobId, "PENDING", false),
                vidAuditStatus(jobId, "IN_PROGRESS", false),
                vidAuditStatus(jobId, "COMPLETED", true)
        );
    }

    private ImmutableList<JobAuditStatus> vidAuditStatuses(String jobId, JobStatus jobStatus) {
        switch(jobStatus) {
            case COMPLETED:
                return vidAuditStatusesCompleted(jobId);
            case COMPLETED_WITH_ERRORS:
                return vidAuditStatusesCompletedWithErrors(jobId);
            case FAILED:
                return vidAuditStatusesFailed(jobId);
            case IN_PROGRESS:
                return vidAuditStatusesInProgress(jobId);
        }

        return null;
    }

    private ImmutableList<JobAuditStatus> msoAuditStatuses(String jobId , String lastStatus, String lastAdditionalInfo) {
        final List<JobAuditStatus> auditMsoStatuses = getAuditStatuses(jobId, SourceStatus.MSO.name());
        final UUID actualRequestId = auditMsoStatuses.get(0).getRequestId();
        return ImmutableList.of(
                msoAuditStatus(jobId, "REQUESTED", null, actualRequestId),
                msoAuditStatus(jobId, lastStatus, lastAdditionalInfo, actualRequestId)
        );
    }

    private JobAuditStatus msoAuditStatus(String jobId, String jobStatus, String additionalInfo, UUID requestId) {
        return new JobAuditStatus(UUID.fromString(jobId), jobStatus, SourceStatus.MSO, requestId, additionalInfo, false);
    }

    private void assertServiceInfoSpecific3(String jobId, JobStatus jobStatus, String serviceInstanceName) {
        assertExpectedStatusAndServiceInfo(jobStatus, jobId, PATIENCE_LEVEL.FAIL_SLOW, new ServiceInfo(
                "us16807000", jobStatus, false,
                "aaa1", "aaa1", "yyy1",
                "YYY1", "UUUAIAAI-YYY1",
                "1178612d2b394be4834ad77f567c0af2", "AIN Web Tool-15-D-SSPtestcustome",
                "hvf6", null,
                "TYLER SILVIA", null,
                null, serviceInstanceName,
                "6e59c5de-f052-46fa-aa7e-2fca9d674c44", "ComplexService", "1.0",
                jobId, null, ServiceAction.INSTANTIATE, false)
        );
    }

    private List<BasePreset> getGroupMembersPresets(List<PresetMSOAddOrRemoveOneInstanceGroupMember> deleteMembersPreset, List<PresetMSOOrchestrationRequestGet> inProgressPresets) {
        final ImmutableList.Builder<BasePreset> basePresetBuilder = new ImmutableList.Builder<>();
        basePresetBuilder
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .addAll(deleteMembersPreset)
                .addAll(inProgressPresets);
        return basePresetBuilder.build();
    }


    enum Scenario {
        PARALLEL, COMPLETED, NAME_TAKEN, DUPLICATE_NAME, IN_PROGRESS, MSO_FAIL
    }

    class OneServiceActor {
        private final boolean isRelevant;
        private final String requestId;
        private final String msoInfo;
        private final Map<PresetMSOServiceInstanceGen2WithNames.Keys, String> serviceNames;
        private final int suffix;
        private final String expectedMsoLastStatus;
        private final JobStatus jobStatus;
        private final Function<String, List<JobAuditStatus>> jobAuditStatusesProvider;
        private String jobId;
        private int startedHoursAgo = 1;
        private List<BasePreset> morePresets = ImmutableList.of();

        private OneServiceActor(boolean isRelevant, String msoLastStatus, String msoInfo, Map<PresetMSOServiceInstanceGen2WithNames.Keys, String> serviceNames, int suffix, JobStatus jobStatus, Function<String, List<JobAuditStatus>> jobAuditStatusesProvider) {
            this.isRelevant = isRelevant;
            this.requestId = UUID.randomUUID().toString();
            this.msoInfo = msoInfo;
            this.serviceNames = serviceNames;
            this.suffix = suffix;
            this.expectedMsoLastStatus = msoLastStatus;
            this.jobStatus = jobStatus;
            this.jobAuditStatusesProvider = jobAuditStatusesProvider;
        }

        private OneServiceActor(boolean isRelevant, String msoLastStatus, String msoInfo, Map<PresetMSOServiceInstanceGen2WithNames.Keys, String> serviceNames, int suffix, JobStatus jobStatus, Function<String, List<JobAuditStatus>> jobAuditStatusesProvider, int startedHoursAgo) {
            this(isRelevant, msoLastStatus, msoInfo, serviceNames, suffix, jobStatus,  jobAuditStatusesProvider);
            this.startedHoursAgo = startedHoursAgo;
        }

        private OneServiceActor(boolean isRelevant, String msoLastStatus, String msoInfo, Map<PresetMSOServiceInstanceGen2WithNames.Keys, String> serviceNames, int suffix, JobStatus jobStatus, Function<String, List<JobAuditStatus>> jobAuditStatusesProvider, List<BasePreset> morePresets) {
            this(isRelevant, msoLastStatus, msoInfo, serviceNames, suffix, jobStatus,  jobAuditStatusesProvider);
            this.morePresets = morePresets;
        }

        public List<BasePreset> getPresets() {
            // if not relevant -> return empty list
            return isRelevant ?
                    ImmutableList.<BasePreset>builder().add(
                            new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService(serviceNames, suffix, requestId),
                            new PresetMSOOrchestrationRequestGet(expectedMsoLastStatus, requestId, msoInfo, startedHoursAgo)
                    ).addAll(morePresets).build()
                    : ImmutableList.of();
        }

        private void createInstances() {
            // call VID with 1 request, keep the job id
            if (!isRelevant) return;
            final List<String> jobIds = createBulkOfInstances(false, 1, serviceNames, CREATE_BULK_OF_ALACARTE_REQUEST);
            assertThat(jobIds, hasSize(1));
            jobId = jobIds.get(0);
        }

        private void assertServiceInfo() {
            if (!isRelevant) return;
            assertThat(jobId, is(not(nullValue())));
            assertServiceInfoSpecific1(jobId, jobStatus, serviceNames.get(SERVICE_NAME));
        }

        private void assertAuditStatuses2() {
            if (!isRelevant) return;
            assertThat(jobId, is(not(nullValue())));
            assertAuditStatuses(jobId, jobAuditStatusesProvider.apply(jobId), msoAuditStatuses(jobId, expectedMsoLastStatus, msoInfo));
        }
    }

}
