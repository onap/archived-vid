package org.onap.vid.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost.DEFAULT_REQUEST_ID;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseDelete;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2ErrorResponse;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteALaCarteService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGetErrorResponse;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestsGetByRequestId;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestsGetByServiceInstanceId;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames;
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

@FeatureTogglingTest({Features.FLAG_ASYNC_JOBS, Features.FLAG_ASYNC_INSTANTIATION, Features.FLAG_ASYNC_ALACARTE_VNF})
public class AsyncInstantiationALaCarteApiTest2 extends AsyncInstantiationBase {

    private static final String CREATE_BULK_OF_ALACARTE_MULTIPLE_VNF_NETWORK_REQUEST_CYPRESS = "a-la-carte/redux-multiple-vnf-network.json";
    private static final String DELETE_BULK_OF_ALACARTE_REQUEST = "VnfGroup/ServiceWithVnfGroupsDeleteRequest.json";
    private static final String SERVICE_INSTANCE_ID = BaseMSOPreset.DEFAULT_INSTANCE_ID;


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
    public void deleteService_withBadResponseFromMso_verifyStatus() {
        List<PresetMSOBaseDelete> deletePresets =   ImmutableList.of(new PresetMSODeleteALaCarteService( DEFAULT_REQUEST_ID, SERVICE_INSTANCE_ID, 500));
        List<PresetMSOOrchestrationRequestGet> inProgressPresets = ImmutableList.of(new PresetMSOOrchestrationRequestGet());
        List<BasePreset> presets = getDeletePresets(deletePresets, inProgressPresets);

        registerExpectationFromPresets(presets, RegistrationStrategy.CLEAR_THEN_SET);
        final List<String> uuids = createBulkOfInstances(false, 1, ImmutableMap.of(), DELETE_BULK_OF_ALACARTE_REQUEST);

        assertThat(uuids, hasSize(1));
        final String jobId = uuids.get(0);

        assertServiceInfoSpecificDeletion(jobId, JobStatus.FAILED,  "wowServiceWithVnfGroping", "TYLER SILVIA");
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                TestUtils.hasOrLacksOfEntry(deletePresets.get(0).getReqPath(), 1L),
                TestUtils.hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 0L)
        ));
    }


    @Test
    public void instantiationFailedForService(){
        CreateInstanceWithFailedService createInstanceWithFailedService = new CreateInstanceWithFailedService(this);
        createInstanceWithFailedService.deployServiceFailedInstance();
    }

    @Test
    void getBulkForRetry_notFoundException(){
        CreateServiceWithFailedVnf createServiceWithFailedVnf = new CreateServiceWithFailedVnf(this);
        createServiceWithFailedVnf.getBulkForRetryNotFound();
    }


    @Test
    public void deploy1Service1FailedVnf_EditJobSendingSameDataAndRetry_verifyNewJobWithSameData() {
        CreateServiceWithFailedVnf createServiceWithFailedVnf = new CreateServiceWithFailedVnf(this);
        String bulkRetryPayload = createServiceWithFailedVnf.deployService1FailedVnf();
        createServiceWithFailedVnf.assertResourceAuditStatus(bulkRetryPayload);
        createServiceWithFailedVnf.secondRegistration();
        createServiceWithFailedVnf.retryJobWithOtherDataAndAssert(bulkRetryPayload);
    }

    @Test
    public void deploy1Service1FailedVnf_EditSomeDetailsAndRetry_verifyNewJobWithEditedData() {
        CreateServiceWithFailedVnf createServiceWithFailedVnf = new CreateServiceWithFailedVnf(this);
        String originalBulkForRetry =  createServiceWithFailedVnf.deployService1FailedVnf();
        createServiceWithFailedVnf.assertResourceAuditStatus(originalBulkForRetry);
        String changedData = createServiceWithFailedVnf.changeSomeDataAndRegisterToSimulator(originalBulkForRetry);
        createServiceWithFailedVnf.retryJobWithOtherDataAndAssert(changedData);
    }

    private List<JobAuditStatus> getExpectedAuditFromFile(String fileName) throws IOException {
        String content = TestUtils.convertRequest(objectMapper, fileName);
        List<JobAuditStatus> auditStatusList = ImmutableList.copyOf(objectMapper.readValue(content, JobAuditStatus[].class));
        return auditStatusList;


    }

    @Test
    public void getAuditInfoForALaCarteByServiceInstanceId() throws IOException {
        final String expectedMsoAuditInfo = "a-la-carte/auditInfoMSOALaCarte.json";
        registerExpectationFromPreset(
                new PresetMSOOrchestrationRequestsGetByServiceInstanceId(),
                RegistrationStrategy.CLEAR_THEN_SET);
        List<JobAuditStatus> actualMsoAudits = getJobMsoAuditStatusForAlaCarte(UUID.randomUUID().toString(), "aa1234d1-5a33-55df-13ab-12abad84e333", "bc305d54-75b4-431b-adb2-eb6b9e546014");
        List<JobAuditStatus> expectedMsoAudits = getExpectedAuditFromFile(expectedMsoAuditInfo);
        assertThat(actualMsoAudits, is(expectedMsoAudits));

    }

    @Test
    public void getAuditInfoForALaCarteByRequestId() {
        registerExpectationFromPreset(
                new PresetMSOOrchestrationRequestsGetByRequestId(),
                RegistrationStrategy.CLEAR_THEN_SET);
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap.of(SERVICE_NAME, "serviceInstanceName");
        String uuid = createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_MULTIPLE_VNF_NETWORK_REQUEST_CYPRESS).get(0);
        List<JobAuditStatus> actualMsoAudits = getJobMsoAuditStatusForAlaCarte( uuid, "405652f4-ceb3-4a75-9474-8aea71480a77", null);
        List<JobAuditStatus> expectedMsoAudits =  ImmutableList.of(
                new JobAuditStatus("serviceInstanceName", "FAILED", UUID.fromString("405652f4-ceb3-4a75-9474-8aea71480a77"),"Service Instance was failed.",false, "service"));
        assertThat(actualMsoAudits, is(expectedMsoAudits));

    }

    @Test
    public void getAuditInfoForALaCarteByJobId() throws IOException {
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetMSOCreateServiceInstanceGen2ErrorResponse(),
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet()
                ),
                RegistrationStrategy.CLEAR_THEN_SET);
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap.of(SERVICE_NAME, "serviceInstanceName");
        String uuid = createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_MULTIPLE_VNF_NETWORK_REQUEST_CYPRESS).get(0);

        assertAndRetryIfNeeded(() -> {
            final List<JobAuditStatus> actualMsoAudits = getJobMsoAuditStatusForAlaCarte( uuid, null, null);
            List<JobAuditStatus> expectedMsoAudits =  ImmutableList.of(
                    new JobAuditStatus(UUID.fromString(uuid), "FAILED", SourceStatus.MSO, null,
                            "Http Code:500, \"messageId\":\"SVC0002\",\"text\":\"JSON Object Mapping Request\"", false, "serviceInstanceName"));
            assertThat(actualMsoAudits, is(expectedMsoAudits));
        }, 15);

        //assert error audit status
        Map<String, Object> bulkForRetry = objectMapper.readValue(getRetryBulk(uuid).getBody(), new TypeReference<Map<String, Object>>(){});
        String serviceTrackById = bulkForRetry.get("trackById").toString();
        Map<String, Object> resourceAuditInfo = (Map) getResourceAuditInfo(serviceTrackById);
        assertThat(resourceAuditInfo.get("jobStatus"), equalTo("FAILED"));
        assertThat(resourceAuditInfo.get("additionalInfo"), equalTo("Http Code:500, \"messageId\":\"SVC0002\",\"text\":\"JSON Object Mapping Request\""));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void getAuditInfoForALaCarte_badResponseFromMso_throwsException() {
        registerExpectationFromPreset(
                new PresetMSOOrchestrationRequestGetErrorResponse(500),
                RegistrationStrategy.CLEAR_THEN_SET);
        getJobMsoAuditStatusForAlaCarte( UUID.randomUUID().toString(), "405652f4-ceb3-4a75-9474-8aea71480a77", null);
    }

    private void assertServiceInfoSpecificDeletion(String jobId, JobStatus jobStatus, String serviceInstanceName, String serviceType) {
        assertExpectedStatusAndServiceInfo(jobStatus, jobId, true, new ServiceInfo(
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


    enum Scenario {
        PARALLEL, COMPLETED, NAME_TAKEN, DUPLICATE_NAME, IN_PROGRESS, MSO_FAIL
    }

}
