package org.onap.vid.api;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset.DEFAULT_INSTANCE_ID;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost.DEFAULT_REQUEST_ID;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet.COMPLETE;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;
import static vid.automation.test.services.SimulatorApi.retrieveRecordedRequests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOAddOrRemoveOneInstanceGroupMember;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOAddOrRemoveOneInstanceGroupMember.InstanceGroupMemberAction;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseDelete;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateNetworkALaCarteCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesAlacarteServiceCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVfModuleALaCarteCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVfModuleALaCarteCypress.Keys;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVnfALaCarteCypress2;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVnfGroup;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteALaCarteService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteInstanceGroup;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.onap.vid.model.asyncInstantiation.JobAuditStatus;
import org.onap.vid.model.asyncInstantiation.JobAuditStatus.SourceStatus;
import org.onap.vid.model.asyncInstantiation.ServiceInfo;
import org.onap.vid.more.LoggerFormatTest;
import org.onap.vid.more.LoggerFormatTest.LogName;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.model.JobStatus;
import vid.automation.test.model.ServiceAction;
import vid.automation.test.services.AsyncJobsService;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RecordedRequests;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;

@FeatureTogglingTest({Features.FLAG_ASYNC_ALACARTE_VNF})
public class AsyncInstantiationALaCarteApiTest extends AsyncInstantiationBase {

    private static final String CREATE_BULK_OF_ALACARTE_REQUEST = "asyncInstantiation/vidRequestCreateALaCarte.json";
    private static final String CREATE_BULK_OF_ALACARTE_REQUEST_CYPRESS = "a-la-carte/redux-a-la-carte.json";
    private static final String CREATE_BULK_OF_ALACARTE_NO_TESTAPI_REQUEST_CYPRESS = "a-la-carte/redux-a-la-carte-no-testapi.json";
    private static final String DELETE_BULK_OF_ALACARTE_REQUEST = "VnfGroup/ServiceWithVnfGroupsDeleteRequest.json";
    private static final String VIEW_EDIT_VNF_GROUPS_REQUEST = "VnfGroup/VnfGroupCreate1Delete1None1Request.json";
    private static final String DELETE_TWO_VNF_GROUPS_MEMBER_AND_ADD_ONE_REQUEST = "VnfGroup/vnfGroupCreate1VnfGroupAndDelete2VnfGroupsRequest.json";
    private static final String DELETE_SERVICE_WITH_TWO_VNF_GROUPS_REQUEST_WITH_GROUPMEMBERS = "VnfGroup/deleteServiceWith2VnfGroupsRequest_AndThreeGroupMembers.json";


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

    @Test
    public void deploy1Service0VnfWithStrictSimulatorCompare__verifyStatusAndAudit() {
        /*
        Legit Preset  ||  deploy 1 Service, no VNF inside
                -> JobStatus is Eventually success, audit
                   is adequate; strict simulator compare
         */

        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap.of(SERVICE_NAME, "calazixide85");
        List<PresetMSOBaseCreateInstancePost> createPresets =   ImmutableList.of(new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService(names, 0, DEFAULT_REQUEST_ID));
        List<PresetMSOOrchestrationRequestGet> inProgressPresets = ImmutableList.of(new PresetMSOOrchestrationRequestGet());
        List<BasePreset> presets = getPresets(createPresets, inProgressPresets);

        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
        final List<String> uuids = createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_REQUEST);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        assertServiceInfoSpecific1(jobId, JobStatus.COMPLETED, names.get(SERVICE_NAME));
        assertAuditStatuses(jobId, vidAuditStatusesCompleted(jobId), msoAuditStatusesCompleted(jobId));
    }

    @Test
    public void deployTwoServicesGetServicesFilterByModelId() {
        registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), RegistrationStrategy.CLEAR_THEN_SET);

        List<String> uuids = new LinkedList<>();
        try {
            //given
            final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap
                .of(SERVICE_NAME, "calazixide85");

            String SERVICE_MODEL_UUID = "e3c34d88-a216-4f1d-a782-9af9f9588705";

            uuids = Stream.of(
                createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_REQUEST).get(0),
                createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_REQUEST).get(0),
                createBulkOfInstances(false, 1, names, CREATE_BULK_OF_MACRO_REQUEST).get(0)
            ).collect(toList());

            //when
            ResponseEntity<List<ServiceInfo>> response = restTemplate.exchange(
                getTemplateInfoUrl(SERVICE_MODEL_UUID),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ServiceInfo>>() {
                });

            //then
            final List<ServiceInfo> body = response.getBody();

            //assert that service info list contains only services with desired modelId
            assertThat(body.stream().map(x -> x.serviceModelId).collect(toSet()),
                contains(SERVICE_MODEL_UUID));
            //assert that service info list contains the 2 first jobs
            assertThat(body.stream().map(x -> x.jobId).collect(toList()),
                hasItems(uuids.get(0), uuids.get(1)));
            //assert that service info list doesn't contains last jobs
            assertThat(body.stream().map(x -> x.jobId).collect(toList()),
                not(hasItems(uuids.get(2))));
        }
        finally {
            //clear jobs to not disturb next tests
            uuids.forEach(uuid->new AsyncJobsService().muteAsyncJobById(uuid));
        }
    }
    @Test
    public void deleteServiceWithTwoVnfGroups_andRetry() {
        String parentServiceInstanceId = "service-instance-id";
        String firstVnfGroupToDeleteInstanceId = "VNF_GROUP1_INSTANCE_ID";
        String secondVnfGroupToDeleteInstanceId = "VNF_GROUP2_INSTANCE_ID";
        String firstVnfGroupToDeleteRequestId = UUID.randomUUID().toString();
        String secondVnfGroupToDeleteRequestId = UUID.randomUUID().toString();
        String parentServiceRequestId = UUID.randomUUID().toString();
        List<String> vnfGroupMemberRemoveRequestsIds = ImmutableList.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

        //failed to delete vnf group, and then also service is not deleted
        List<PresetMSOBaseCreateInstancePost> createPresets = ImmutableList.of(
                new PresetMSOAddOrRemoveOneInstanceGroupMember(firstVnfGroupToDeleteInstanceId, "RELATED_VNF1_INSTANCE_ID", getUserCredentials().getUserId(), vnfGroupMemberRemoveRequestsIds.get(0), InstanceGroupMemberAction.Remove),
                new PresetMSOAddOrRemoveOneInstanceGroupMember(firstVnfGroupToDeleteInstanceId, "RELATED_VNF2_INSTANCE_ID", getUserCredentials().getUserId(), vnfGroupMemberRemoveRequestsIds.get(1), InstanceGroupMemberAction.Remove),
                new PresetMSOAddOrRemoveOneInstanceGroupMember(firstVnfGroupToDeleteInstanceId, "RELATED_VNF3_INSTANCE_ID", getUserCredentials().getUserId(), vnfGroupMemberRemoveRequestsIds.get(2), InstanceGroupMemberAction.Remove));

            List<PresetMSOBaseDelete> deletePresets = ImmutableList.of(
                    new PresetMSODeleteInstanceGroup(firstVnfGroupToDeleteRequestId, firstVnfGroupToDeleteInstanceId, getUserCredentials().getUserId()),
                    new PresetMSODeleteInstanceGroup(secondVnfGroupToDeleteRequestId, secondVnfGroupToDeleteInstanceId, getUserCredentials().getUserId())
            );

        List<PresetMSOOrchestrationRequestGet> inProgressPresets = new ArrayList<>();
        inProgressPresets.add(new  PresetMSOOrchestrationRequestGet( MSO_COMPLETE_STATUS, firstVnfGroupToDeleteRequestId));
        inProgressPresets.add(new  PresetMSOOrchestrationRequestGet( MSO_FAILED_STATUS, secondVnfGroupToDeleteRequestId));
        inProgressPresets.add(new  PresetMSOOrchestrationRequestGet( MSO_COMPLETE_STATUS, vnfGroupMemberRemoveRequestsIds.get(0)));
        inProgressPresets.add(new  PresetMSOOrchestrationRequestGet( MSO_COMPLETE_STATUS, vnfGroupMemberRemoveRequestsIds.get(1)));
        inProgressPresets.add(new  PresetMSOOrchestrationRequestGet( MSO_COMPLETE_STATUS, vnfGroupMemberRemoveRequestsIds.get(2)));

        List<BasePreset> presets = getPresets(deletePresets, createPresets, inProgressPresets);
        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
        final List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), DELETE_SERVICE_WITH_TWO_VNF_GROUPS_REQUEST_WITH_GROUPMEMBERS);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);
        assertServiceInfoSpecificDeletion(jobId, JobStatus.COMPLETED_WITH_ERRORS, "SERVICE_INSTANCE_NAME", "service-instance-type");
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry(deletePresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(deletePresets.get(1).getReqPath(), 1L),

                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(1).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(createPresets.get(0).getReqPath(), 3L)

        ));

        //retry to delete vnf-group and then delete service
        List<BasePreset> retryPresets = ImmutableList.of(
                new PresetMSODeleteInstanceGroup(secondVnfGroupToDeleteRequestId, secondVnfGroupToDeleteInstanceId, getUserCredentials().getUserId()),
                new PresetMSODeleteALaCarteService(parentServiceRequestId,parentServiceInstanceId ),
                new PresetMSOOrchestrationRequestGet( MSO_COMPLETE_STATUS, secondVnfGroupToDeleteRequestId),
                new PresetMSOOrchestrationRequestGet( MSO_COMPLETE_STATUS, parentServiceRequestId));
        registerExpectationFromPresets(retryPresets, RegistrationStrategy.CLEAR_THEN_SET);

        List<String> retryUuids = retryJob(jobId);
        assertThat(retryUuids, hasSize(1));
        final String retryJobId = retryUuids.get(0);
        assertServiceInfoSpecificDeletion(retryJobId, JobStatus.COMPLETED, "SERVICE_INSTANCE_NAME", "service-instance-type");

        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry(retryPresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(retryPresets.get(1).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(retryPresets.get(2).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(retryPresets.get(3).getReqPath(), 1L)
        ));
    }

    @DataProvider
    public static Object[][] msoRequestStatusDataProvider() {
        return new Object[][]{
                {MSO_COMPLETE_STATUS, JobStatus.COMPLETED},
                {MSO_FAILED_STATUS, JobStatus.FAILED}
        };
    }

    @Test(dataProvider = "msoRequestStatusDataProvider")
    public void deleteServiceWithStrictSimulatorCompare__verifyStatusAndAudit(String msoStatus, JobStatus expectedStatus) {
        List<PresetMSOBaseDelete> deletePresets =   ImmutableList.of(new PresetMSODeleteALaCarteService( DEFAULT_REQUEST_ID, SERVICE_INSTANCE_ID));
        List<PresetMSOOrchestrationRequestGet> inProgressPresets =  ImmutableList.of (new PresetMSOOrchestrationRequestGet(msoStatus),
        new PresetMSOOrchestrationRequestGet(msoStatus));
        List<BasePreset> presets = getDeletePresets(deletePresets, inProgressPresets);

        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
        final List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), DELETE_BULK_OF_ALACARTE_REQUEST);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        assertServiceInfoSpecificDeletion(jobId, expectedStatus, "wowServiceWithVnfGroping", "TYLER SILVIA");
        switch (expectedStatus) {
            case COMPLETED:
                assertAuditStatuses(jobId, vidAuditStatusesCompleted(jobId), msoAuditStatusesCompleted(jobId));
                break;

            case FAILED:
                assertAuditStatuses(jobId, vidAuditStatusesFailed(jobId), null);
                break;
        }

        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry(deletePresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L)
        ));
    }

    @Test(dataProvider = "msoRequestStatusDataProvider")
    public void deleteTwoGroupsAddOneGroup(String msoStatus, JobStatus expectedStatus) {
        String parentServiceInstanceId = "service-instance-id";
        String firstVnfGroupToDeleteInstanceId = "VNF_GROUP1_INSTANCE_ID";
        String secondVnfGroupToDeleteInstanceId = "VNF_GROUP2_INSTANCE_ID";
        String firstVnfGroupToDeleteRequestId = UUID.randomUUID().toString();
        String secondVnfGroupToDeleteRequestId = UUID.randomUUID().toString();
        String vnfGroupToCreateRequestId = UUID.randomUUID().toString();

        List<PresetMSOBaseDelete> deletePresets = ImmutableList.of(
                new PresetMSODeleteInstanceGroup(firstVnfGroupToDeleteRequestId, firstVnfGroupToDeleteInstanceId, getUserCredentials().getUserId()),
                new PresetMSODeleteInstanceGroup(secondVnfGroupToDeleteRequestId, secondVnfGroupToDeleteInstanceId, getUserCredentials().getUserId()));

        List<PresetMSOBaseCreateInstancePost> createPresets = ImmutableList.of(
                new PresetMSOCreateVnfGroup("VNF_GROUP3_INSTANCE_NAME", vnfGroupToCreateRequestId,
                        PresetMSOCreateVnfGroup.MODEL_INFO_0, parentServiceInstanceId, false));
        List<PresetMSOOrchestrationRequestGet> inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet(msoStatus, firstVnfGroupToDeleteRequestId),
                new PresetMSOOrchestrationRequestGet(msoStatus, secondVnfGroupToDeleteRequestId),
                new PresetMSOOrchestrationRequestGet(msoStatus, vnfGroupToCreateRequestId, "Instance group was created successfully.")
        );
        List<BasePreset> presets = getPresets(deletePresets, createPresets, inProgressPresets);
        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
        final List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), DELETE_TWO_VNF_GROUPS_MEMBER_AND_ADD_ONE_REQUEST);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        assertServiceInfoSpecificUpdate(jobId, expectedStatus, "SERVICE_INSTANCE_NAME");
        assertExpectedStatus(expectedStatus, jobId);
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry(deletePresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(deletePresets.get(1).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(1).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(createPresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(2).getReqPath(), 1L)
        ));

    }

    @Test
    public void viewEditVnfGroup__verifyStatusAndAudit() {
        String parentServiceInstanceId = "service-instance-id";
        String vnfGroupToDeleteInstanceId = "VNF_GROUP1_INSTANCE_ID";

        //failed to create vnf group, failed to remove 1 member (and then also vnf group isn't deleted)
        viewEditVnfGroup_registerPresets(parentServiceInstanceId, vnfGroupToDeleteInstanceId, MSO_FAILED_STATUS);

        final List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), VIEW_EDIT_VNF_GROUPS_REQUEST);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        assertServiceInfoSpecificUpdate(jobId, JobStatus.COMPLETED_WITH_ERRORS, "SERVICE_INSTANCE_NAME");
        assertExpectedStatus(JobStatus.COMPLETED_WITH_ERRORS, jobId);
        Map<String, Long> recordedRequest = SimulatorApi.retrieveRecordedRequestsPathCounter();
        assertThat(recordedRequest, allOf(
                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./instanceGroups/" + vnfGroupToDeleteInstanceId, 0L), //delete vnf group
                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./instanceGroups", 1L), //create vnf group
                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./instanceGroups/" + vnfGroupToDeleteInstanceId + "/removeMembers", 3L) //remove vnf group members
        ));

        //retry - vnf group create, 1 member remove, vnf group delete
        viewEditVnfGroup_registerPresets(parentServiceInstanceId, vnfGroupToDeleteInstanceId, MSO_COMPLETE_STATUS);
        final List<String> retryUuids = retryJob(jobId);
        assertThat(retryUuids, hasSize(1));
        final String retryJobId = retryUuids.get(0);

        assertServiceInfoSpecificUpdate(retryJobId, JobStatus.COMPLETED, "SERVICE_INSTANCE_NAME");
        assertExpectedStatus(JobStatus.COMPLETED, retryJobId);
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./instanceGroups/" + vnfGroupToDeleteInstanceId, 1L), //delete vnf group
                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./instanceGroups", 1L), //create vnf group
                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./instanceGroups/" + vnfGroupToDeleteInstanceId + "/removeMembers", 1L) //remove vnf group members
        ));
    }

    private void viewEditVnfGroup_registerPresets(String parentServiceInstanceId, String vnfGroupToDeleteInstanceId, String msoStatus) {
        String vnfGroupToDeleteRequestId = UUID.randomUUID().toString();
        String vnfGroupToCreateRequestId = UUID.randomUUID().toString();
        List<String> vnfGroupMemberRemoveRequestsIds = ImmutableList.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

        List<PresetMSOBaseDelete> deletePresets = ImmutableList.of(
                new PresetMSODeleteInstanceGroup(vnfGroupToDeleteRequestId, vnfGroupToDeleteInstanceId, getUserCredentials().getUserId()));
        List<PresetMSOBaseCreateInstancePost> createPresets = ImmutableList.of(
                new PresetMSOCreateVnfGroup("VNF_GROUP3_INSTANCE_NAME", vnfGroupToCreateRequestId,
                        PresetMSOCreateVnfGroup.MODEL_INFO_0, parentServiceInstanceId, false),
                new PresetMSOAddOrRemoveOneInstanceGroupMember(vnfGroupToDeleteInstanceId, "RELATED_VNF1_INSTANCE_ID", getUserCredentials().getUserId(), vnfGroupMemberRemoveRequestsIds.get(0), InstanceGroupMemberAction.Remove),
                new PresetMSOAddOrRemoveOneInstanceGroupMember(vnfGroupToDeleteInstanceId, "RELATED_VNF2_INSTANCE_ID", getUserCredentials().getUserId(), vnfGroupMemberRemoveRequestsIds.get(1), InstanceGroupMemberAction.Remove),
                new PresetMSOAddOrRemoveOneInstanceGroupMember(vnfGroupToDeleteInstanceId, "RELATED_VNF3_INSTANCE_ID", getUserCredentials().getUserId(), vnfGroupMemberRemoveRequestsIds.get(2), InstanceGroupMemberAction.Remove));
        List<PresetMSOOrchestrationRequestGet> inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, vnfGroupToDeleteRequestId), // delete instance group
                new PresetMSOOrchestrationRequestGet(msoStatus, vnfGroupToCreateRequestId, "Instance group was created successfully."), // create instance group
                new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, vnfGroupMemberRemoveRequestsIds.get(0)), // remove instance group member
                new PresetMSOOrchestrationRequestGet(msoStatus, vnfGroupMemberRemoveRequestsIds.get(1)), // remove instance group member
                new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, vnfGroupMemberRemoveRequestsIds.get(2))); // remove instance group member

        List<BasePreset> presets = getPresets(deletePresets, createPresets, inProgressPresets);
        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
    }

    private void assertExpectedStatus(JobStatus expectedStatus, String jobId) {
        switch (expectedStatus) {
            case COMPLETED:
                assertAuditStatuses(jobId, vidAuditStatusesCompleted(jobId), null);
                break;

            case FAILED:
                assertAuditStatuses(jobId, vidAuditStatusesFailed(jobId), null);
                break;
        }
    }

    @Test
    public void deploy1Service1FailedVnf__verifyStatus_andRetry() {
        //CreateServiceWithFailedVnf is common for API test and UI test,
        //so if you change it, make sure both test are compatible with your changes
        CreateServiceWithFailedVnf createServiceWithFailedVnf = new CreateServiceWithFailedVnf(this);
        createServiceWithFailedVnf.deployService1FailedVnf();
        createServiceWithFailedVnf.secondRegistration();
        createServiceWithFailedVnf.retryJob();
        createServiceWithFailedVnf.retryAssertion();
        createServiceWithFailedVnf.simulatorCallsAssertion();
    }

    @DataProvider
    Object[][] data_deploy1ServiceFromCypress__verifyStatusAndMsoCalls() {
        return Features.FLAG_ASYNC_ALACARTE_VFMODULE.isActive() ? new Object[][]{
                {"none", emptyMap(), emptyMap(), true},
                {"none", emptyMap(), emptyMap(), false},
                {"instance",  ImmutableMap.of("vnfs", 0L, "networks", 0L, "vfModules", 0L, "volumeGroups", 0L),
                        ImmutableMap.of("serviceInstances", 1L, "vnfs", 1L, "networks", 1L, "vfModules", 3L, "volumeGroups", 1L),true},
                {"network", emptyMap(),
                        ImmutableMap.of("networks", 1L), true},
                {"vnf0", ImmutableMap.of("vfModules", 0L, "volumeGroups", 0L),
                        ImmutableMap.of("vnfs", 1L, "vfModules", 3L, "volumeGroups", 1L), true},
                {"vfModule0", ImmutableMap.of("vfModules", 1L, "volumeGroups", 0L),
                        ImmutableMap.of("vfModules", 3L, "volumeGroups", 1L), true},
                {"volumeGroup", ImmutableMap.of("vfModules", 2L),
                        ImmutableMap.of("vfModules", 1L, "volumeGroups", 1L), true},
                {"vfModule1", emptyMap(),
                        ImmutableMap.of("vfModules", 1L, "volumeGroups", 1L), true},
                {"vfModule2", emptyMap(),
                        ImmutableMap.of("vfModules", 1L), true}
        } : new Object[][]{
                {"none", ImmutableMap.of("vfModules", 0L, "volumeGroups", 0L), emptyMap(), true}
        };
    }

    @Test(dataProvider = "data_deploy1ServiceFromCypress__verifyStatusAndMsoCalls")
    public void deploy1ServiceFromCypress__verifyStatusAndMsoCalls_andRetry(String whatToFail, Map<String, Long> pathCounterOverride, Map<String, Long> retryPathCounterOverride, boolean withTestApi) {
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap.of(SERVICE_NAME, "serviceInstanceName");
        String vnfRequestId = UUID.randomUUID().toString();
        registerPresetsForRetryTest(whatToFail, names, vnfRequestId, withTestApi);

        final List<String> uuids = createBulkOfInstances(false, 1, names, withTestApi? CREATE_BULK_OF_ALACARTE_REQUEST_CYPRESS: CREATE_BULK_OF_ALACARTE_NO_TESTAPI_REQUEST_CYPRESS);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        JobStatus finalJobStatus;
        switch (whatToFail) {
            case "none": finalJobStatus = JobStatus.COMPLETED; break;
            case "instance": finalJobStatus = JobStatus.FAILED; break;
            default: finalJobStatus = JobStatus.COMPLETED_WITH_ERRORS; break;
        }
        assertServiceInfoSpecific2(jobId, finalJobStatus, names.get(SERVICE_NAME));
        assertRecordedRequests(pathCounterOverride, 1L, vnfRequestId);

        if (!"none".equals(whatToFail)) {
            registerPresetsForRetryTest("none", names, vnfRequestId, withTestApi);

            List<String> retryUuids = retryJob(jobId);
            assertThat(retryUuids, hasSize(1));
            final String retryJobId = retryUuids.get(0);

            ServiceAction serviceAction = "instance".equals(whatToFail) ? ServiceAction.INSTANTIATE : ServiceAction.UPDATE;
            assertServiceInfoSpecific2(retryJobId, JobStatus.COMPLETED, names.get(SERVICE_NAME), serviceAction);

            assertRecordedRequests(retryPathCounterOverride, 0L, vnfRequestId);
        }
    }


    @Test
    public void deployServiceAfterDragAndDropVFModule__verifyOrderMsoCalls() {
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap
            .of(SERVICE_NAME, "serviceInstanceName");
        String vnfRequestId = UUID.randomUUID().toString();
        registerPresetsForRetryTest("none", names, vnfRequestId, false);

        final List<String> uuids = createBulkOfInstances(false, 1, names,
            CREATE_BULK_OF_ALACARTE_NO_TESTAPI_REQUEST_CYPRESS);

        final String jobId = uuids.get(0);

        assertServiceInfoSpecific2(jobId, JobStatus.COMPLETED, names.get(SERVICE_NAME));
        assertMSOcalledWithOrder();
    }

    @Test
    public void verifyMetricsLogInAsyncInstantiation() {

        final String UUID_REGEX = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

        final String msoURL = "/mso";

        deploy1ServiceFromCypress__verifyStatusAndMsoCalls_andRetry("none", emptyMap(), emptyMap(), true);
        List<String> logLines =  LoggerFormatTest.getLogLinesAsList(LogName.metrics2019, 200, 1, restTemplate, uri);
        List<RecordedRequests> underTestRequests = retrieveRecordedRequests();

        underTestRequests.forEach(request-> {
            assertThat("X-ONAP-RequestID", request.headers.get("X-ONAP-RequestID"), contains(matchesPattern(UUID_REGEX)));
            assertThat("X-ECOMP-RequestID", request.headers.get("X-ECOMP-RequestID"), contains(matchesPattern(UUID_REGEX)));
            assertThat("X-ECOMP-RequestID", request.headers.get("X-InvocationID"), contains(matchesPattern(UUID_REGEX)));
            assertThat("X-ONAP-PartnerName", request.headers.get("X-ONAP-PartnerName"), contains("VID.VID"));
        });

        List<String> allInvocationIds = new LinkedList<>();
        List<String> allMsoRequestsIds = new LinkedList<>();

        underTestRequests.forEach(request->{
            String invocationId = request.headers.get("X-InvocationID").get(0);
            allInvocationIds.add(invocationId);

            String requestId = request.headers.get("X-ONAP-RequestID").get(0);
            if (request.path.contains(msoURL)) {
                allMsoRequestsIds.add(requestId);
            }

            assertThat("request id and invocation id must be found in two rows",
                logLines,
                hasItems(
                    allOf(
                        containsString("RequestID="+requestId),
                        containsString("InvocationID="+ invocationId),
                        containsString("Invoke")),
                    allOf(
                        containsString("RequestID="+requestId),
                        containsString("InvocationID="+ invocationId),
                        containsString("InvokeReturn"))
                ));
        });

        //make sure no InvocationId is repeated twice
        assertThat("expect all InvocationIds to be unique",
            allInvocationIds, containsInAnyOrder(new HashSet<>(allInvocationIds).toArray()));

        //make sure no RequestId is repeated twice
        assertThat("expect all RequestIds to be unique",
            allMsoRequestsIds, containsInAnyOrder(new HashSet<>(allMsoRequestsIds).toArray()));

    }

    private void registerPresetsForRetryTest(String whatToFail, ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names, String vnfRequestId, boolean withTestApi ) {
        String networkRequestId = UUID.randomUUID().toString();
        String vfModule0RequestId = UUID.randomUUID().toString();
        String vfModule1RequestId = UUID.randomUUID().toString();
        String vfModule2RequestId = UUID.randomUUID().toString();
        String volumeGroupRequestId = UUID.randomUUID().toString();

        PresetMSOCreateVnfALaCarteCypress2 vnfPreset =
            new PresetMSOCreateVnfALaCarteCypress2(vnfRequestId, DEFAULT_INSTANCE_ID, vnfRequestId, "2017-488_PASQUALE-vPE", Constants.GR_API, withTestApi);

        List<PresetMSOBaseCreateInstancePost> createPresets = ImmutableList.of(
            new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteServiceCypress(names, 0, DEFAULT_REQUEST_ID, Constants.GR_API, withTestApi),
            vnfPreset,
            new PresetMSOCreateNetworkALaCarteCypress(networkRequestId, DEFAULT_INSTANCE_ID,  networkRequestId, "ExtVL", Constants.GR_API, withTestApi)
        );
        List<PresetMSOOrchestrationRequestGet> inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet("instance".equals(whatToFail) ? MSO_FAILED_STATUS : COMPLETE),
                new PresetMSOOrchestrationRequestGet("vnf0".equals(whatToFail) ? MSO_FAILED_STATUS : COMPLETE, vnfRequestId),
                new PresetMSOOrchestrationRequestGet("vfModule0".equals(whatToFail) ? MSO_FAILED_STATUS : COMPLETE, vfModule0RequestId),
                new PresetMSOOrchestrationRequestGet("vfModule1".equals(whatToFail) ? MSO_FAILED_STATUS : COMPLETE, vfModule1RequestId),
                new PresetMSOOrchestrationRequestGet("vfModule2".equals(whatToFail) ? MSO_FAILED_STATUS : COMPLETE, vfModule2RequestId),
                new PresetMSOOrchestrationRequestGet("volumeGroup".equals(whatToFail) ? MSO_FAILED_STATUS : COMPLETE, volumeGroupRequestId),
                new PresetMSOOrchestrationRequestGet("network".equals(whatToFail) ? MSO_FAILED_STATUS : COMPLETE, networkRequestId)
        );
        List<BasePreset> presetsWithoutVfModule = getPresets(createPresets, inProgressPresets);

        String vfModule1CloudRegionId = Features.FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF.isActive() ? vnfPreset.getLcpCloudRegionId() : "my region";

        Map<Keys, String> vfModule0And2LcpCloudRegionIdAndTenantIdNames =
            PresetMSOCreateVfModuleALaCarteCypress.lcpCloudRegionIdAndTenantIdNames(
                Features.FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF.isActive() ? vnfPreset.getLcpCloudRegionId() : "hvf6",
                Features.FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF.isActive() ? vnfPreset.getTenantId() : "624eb554b0d147c19ff8885341760481");

        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .addAll(presetsWithoutVfModule)
                .add(new PresetSDCGetServiceToscaModelGet("2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd", "csar-noDynamicFields-ecompNamingFalse-fullModelDetails.zip"))
                .add(new PresetSDCGetServiceMetadataGet("2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd", "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0", "csar-noDynamicFields-ecompNamingFalse-fullModelDetails.zip"))
                .add(new PresetMSOCreateVfModuleALaCarteCypress(vfModule0RequestId, DEFAULT_INSTANCE_ID, vnfRequestId, PresetMSOCreateVfModuleALaCarteCypress.module0Names, vfModule0And2LcpCloudRegionIdAndTenantIdNames, Constants.GR_API, withTestApi))
                .add(PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress.forVolumeGroup(volumeGroupRequestId, DEFAULT_INSTANCE_ID, vnfRequestId, vfModule1CloudRegionId, Constants.GR_API, withTestApi))
                .add(PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress.forVfModule(vfModule1RequestId, DEFAULT_INSTANCE_ID, vnfRequestId, volumeGroupRequestId, vfModule1CloudRegionId, Constants.GR_API, withTestApi))
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_JUST_ANOTHER_REGION_TO_ATT_AIC)
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC)
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MY_REGION_TO_ATT_AIC)
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_LCP_REGION_TEXT_TO_ATT_AIC)
                .add(new PresetMSOCreateVfModuleALaCarteCypress(vfModule2RequestId, DEFAULT_INSTANCE_ID, vnfRequestId, PresetMSOCreateVfModuleALaCarteCypress.module2Names, vfModule0And2LcpCloudRegionIdAndTenantIdNames, Constants.GR_API, withTestApi))
                .build();

        registerExpectationFromPresets(
                Features.FLAG_ASYNC_ALACARTE_VFMODULE.isActive() ? presets : presetsWithoutVfModule,
                RegistrationStrategy.CLEAR_THEN_SET);
    }

    private void assertRecordedRequests(Map<String, Long> pathCounterOverride, Long defaultValue, String vnfRequestId) {
        Long vfModulesDefaultValue = defaultValue == 1L ? 3L : 0L;

        //noinspection unchecked
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./serviceInstances",
                        pathCounterOverride.getOrDefault("serviceInstances", defaultValue)),

                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./serviceInstances/" + DEFAULT_INSTANCE_ID + "/networks",
                        pathCounterOverride.getOrDefault("networks", defaultValue)),

                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./serviceInstances/" + DEFAULT_INSTANCE_ID + "/vnfs",
                        pathCounterOverride.getOrDefault("vnfs", defaultValue)),

                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./serviceInstances/" + DEFAULT_INSTANCE_ID + "/vnfs/" + vnfRequestId + "/volumeGroups",
                        pathCounterOverride.getOrDefault("volumeGroups", defaultValue)),

                TestUtils.hasOrLacksOfEntry("/mso/serviceInstantiation/v./serviceInstances/" + DEFAULT_INSTANCE_ID + "/vnfs/" + vnfRequestId + "/vfModules",
                        pathCounterOverride.getOrDefault("vfModules", vfModulesDefaultValue))
        ));
    }


    private void assertMSOcalledWithOrder() {

        List<RecordedRequests> requests = retrieveRecordedRequests();

        String path = "/mso/serviceInstantiation/v7/serviceInstances/.*/vnfs/.*/vfModules";
        List<String> msoVFModulesRequests =
            requests.stream().filter(x -> x.path.matches(path)).map(x -> x.body).collect(toList());

        assertThat("request for vfNodule send with position order",
            msoVFModulesRequests,
            contains(
                containsString("2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0"),
                containsString("2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2"),
                containsString("2017488PasqualeVpe..PASQUALE_vRE_BV..module-1")
            ));
    }

    private ImmutableList<JobAuditStatus> vidAuditStatusesCompleted(String jobId) {
        return ImmutableList.of(
                vidAuditStatus(jobId, "PENDING", false),
                vidAuditStatus(jobId, "IN_PROGRESS", false),
                vidAuditStatus(jobId, "COMPLETED", true)
     );
    }

    private ImmutableList<JobAuditStatus> msoAuditStatusesCompleted(String jobId ) {
        return ImmutableList.of(
                msoAuditStatus(jobId, "REQUESTED", null, UUID.fromString(DEFAULT_REQUEST_ID)),
                msoAuditStatus(jobId, MSO_COMPLETE_STATUS, "Service Instance was created successfully.", UUID.fromString(DEFAULT_REQUEST_ID))
        );
    }

    private JobAuditStatus msoAuditStatus(String jobId, String jobStatus, String additionalInfo, UUID requestId) {
        return new JobAuditStatus(UUID.fromString(jobId), jobStatus, SourceStatus.MSO, requestId, additionalInfo, false);
    }

    private void assertServiceInfoSpecific2(String jobId, JobStatus jobStatus, String serviceInstanceName) {
        assertServiceInfoSpecific2(jobId, jobStatus, serviceInstanceName, ServiceAction.INSTANTIATE);
    }

    private void assertServiceInfoSpecific2(String jobId, JobStatus jobStatus, String serviceInstanceName, ServiceAction serviceAction) {
        assertExpectedStatusAndServiceInfo(jobStatus, jobId, PATIENCE_LEVEL.FAIL_VERY_SLOW, new ServiceInfo(
                "us16807000", jobStatus, false,
                "d61e6f2d-12fa-4cc2-91df-7c244011d6fc", "WayneHolland", "WATKINS",
                "JAG1", null,
                "092eb9e8e4b7412e8787dd091bc58e86", null,
                "AAIAIC25", null,
                "TYLER SILVIA", null,
                null, serviceInstanceName,
                "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd", "action-data", "1.0",
                jobId, null, serviceAction, false)
        );
    }

    private void assertServiceInfoSpecificDeletion(String jobId, JobStatus jobStatus, String serviceInstanceName, String serviceType) {
        assertExpectedStatusAndServiceInfo(jobStatus, jobId, PATIENCE_LEVEL.FAIL_SLOW, new ServiceInfo(
                "us16807000", jobStatus, false,
                null, null, null,
                null, null,
                null, null,
                null, null,
                serviceType, null,
                null, serviceInstanceName,
                "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc", "Grouping Service for Test", "1.0",
                jobId, null, ServiceAction.DELETE, false)
        );
    }

    private void assertServiceInfoSpecificUpdate(String jobId, JobStatus jobStatus, String serviceInstanceName) {
        assertExpectedStatusAndServiceInfo(jobStatus, jobId, PATIENCE_LEVEL.FAIL_SLOW, new ServiceInfo(
                "us16807000", jobStatus, false,
                null, null, null,
                null, null,
                null, null,
                null, null,
                "service-instance-type", null,
                "service-instance-id", serviceInstanceName,
                "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc", "Grouping Service for Test", "1.0",
                jobId, null, ServiceAction.UPDATE, false)
        );
    }

    enum Scenario {
        PARALLEL, COMPLETED, NAME_TAKEN, DUPLICATE_NAME, IN_PROGRESS, MSO_FAIL
    }

}
