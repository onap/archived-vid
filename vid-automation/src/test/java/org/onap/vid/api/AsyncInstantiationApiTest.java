package org.onap.vid.api;

import com.google.common.collect.ImmutableList;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.*;
import org.onap.vid.model.asyncInstantiation.JobAuditStatus;
import org.onap.vid.model.asyncInstantiation.ServiceInfo;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.JobStatus;
import vid.automation.test.services.SimulatorApi;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static vid.automation.test.infra.Features.FLAG_1906_INSTANTIATION_API_USER_VALIDATION;
import static vid.automation.test.utils.ExtendedHamcrestMatcher.hasItemsFromCollection;

@FeatureTogglingTest({Features.FLAG_ASYNC_JOBS, Features.FLAG_ASYNC_INSTANTIATION})
public class AsyncInstantiationApiTest extends AsyncInstantiationBase {
    private static final Logger logger = LogManager.getLogger(AsyncInstantiationApiTest.class);

    private static final String MSO_BASE_ERROR =
            "Received error from SDN-C: java.lang.IllegalArgumentException: All keys must be specified for class org."+
            "opendaylight.yang.gen.v1.org.onap.sdnc.northbound.generic.resource.rev170824.vf.module.assignments.vf."+
            "module.assignments.vms.VmKey. Missing key is getVmType. Supplied key is VmKey [].";
    private static final String MSO_ERROR = MSO_BASE_ERROR + StringUtils.repeat(" and a lot of sentences for long message", 60);

    private static final String INSTANCE_GROUP_ID_LABEL = "instanceGroupId";

    private static final String INSTANCE_GROUP_LABEL = "instanceGroup";

    @Test
    public void createBulkOfCreateInstances(){
        Map<Keys, String> names = generateNames();
        final int bulkSize = 3;
        ImmutableList<BasePreset> presets = addPresetsForCreateBulkOfCreateInstances(bulkSize, names);
        createBulkOfInstancesAndAssert(presets, false, bulkSize, JobStatus.COMPLETED, names);
    }

    @Test(dataProvider = "auditSources")
   public void getAuditStatus_nonExistingJobId_returnsEmptyList(JobAuditStatus.SourceStatus source){
       List<JobAuditStatus> audits = getAuditStatuses(UUID.randomUUID().toString(), source.name());
       Assert.assertEquals(audits.size(),0);
   }

    @Test(expectedExceptions = HttpClientErrorException.class)
    public void getAuditStatus_nonExistingSource_returnsError() {
        try {
            getAuditStatuses(UUID.randomUUID().toString(), new RandomString(8).nextString());
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(e.getResponseBodyAsString(),"The parameter source must have a value among : MSO, VID");
            assertThat(e.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            throw e;
        }
    }

    @Test()
    public void simulateBulkRequest_getAuditStatus_auditStatusesReturnedAccordingSource() {
        final int bulkSize = 2;
        final List<String> jobIds = createBulkAndWaitForBeCompleted(bulkSize);

        for(String jobId: jobIds) {
            List<JobAuditStatus> actualVidAudits = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.VID.name());
            List<JobAuditStatus> expectedVidAudits = Stream.of(JobStatus.PENDING, JobStatus.IN_PROGRESS, JobStatus.COMPLETED)
                    .map(status-> vidAuditStatus(jobId, status.name(), status.equals(JobStatus.COMPLETED))).collect(toList());
            assertThat(actualVidAudits, hasItemsFromCollection(expectedVidAudits));

            List<JobAuditStatus> actualMsoAudits = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO.name());
            List<JobAuditStatus> expectedMsoAudits = Stream.of("REQUESTED", "COMPLETE")
                    .map(status-> new JobAuditStatus(UUID.fromString(jobId),
                            status,
                            JobAuditStatus.SourceStatus.MSO,
                            UUID.fromString("c0011670-0e1a-4b74-945d-8bf5aede1d9c"),
                            status.equals("COMPLETE") ? "Service Instance was created successfully." : null,
                            false)).collect(toList());
            assertThat(actualMsoAudits, is(expectedMsoAudits));
        }
    }


    @Test(expectedExceptions = HttpClientErrorException.class)
    public void addBulkAndDeleteInProgress_deletionIsRejected(){
        try {
            final Map<String, JobStatus> jobs = addBulkAllPendingButOneInProgress();
            deleteOneJobHavingTheStatus(jobs, JobStatus.IN_PROGRESS);
        } catch (HttpClientErrorException e) {
            JsonAssert.assertJsonPartEquals(
                    "Service status does not allow deletion from the queue (Request id: null)",
                    e.getResponseBodyAsString(),
                    "message"
            );
            assertThat(e.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));

            throw e;
        }
    }

    @Test
    public void addBulkAndDeletePending_deletedIsHiddenFromServiceInfoResults(){
        Map<String, JobStatus> statusesBefore = addBulkAllPendingButOneInProgress();

        final String deletedJob = deleteOneJobHavingTheStatus(statusesBefore, JobStatus.PENDING);

        final Map<String, JobStatus> statusesNow = serviceListCall().getBody().stream()
                .filter(si -> statusesBefore.keySet().contains(si.jobId))
                .collect(toMap(si -> si.jobId, si -> si.jobStatus));

        statusesBefore.remove(deletedJob);
        assertThat("deleted job shall not be present in StatusInfo response", statusesNow, is(statusesBefore));
    }

    @Test(invocationCount = 3)
    public void createBulkOfCreateInstancesWithSinglePreset_firstOneInProgressOtherArePending(){
        final int bulkSize = 3;
        Map<String, JobStatus> statusMap = addBulkAllPendingButOneInProgress(bulkSize);
        Set<String> jobIds = statusMap.keySet();

        final Map<JobStatus, List<ServiceInfo>> statuses = serviceListCall().getBody().stream()
                .filter(si -> jobIds.contains(si.jobId))
                .collect(groupingBy(ServiceInfo::getJobStatus));

        // Variable "statuses" contains two lists by status:
        // IN_PROGRESS:  The ultimate first job - named with _001 - is always the only one in progress
        // PENDING:      The other two jobs - named with _002 and _003 - are the still pending
        assertThat(jobIds, hasSize(bulkSize));
        assertThat(statuses.get(JobStatus.IN_PROGRESS), hasSize(1));

        assertThat(statuses.get(JobStatus.PENDING), hasSize(bulkSize - 1));
    }


    @Test(dataProvider = "trueAndFalse" )
    public void whenServiceInBulkFailed_otherServicesAreStopped(Boolean isPresetForCreate){
        Map<Keys, String> names = generateNames();
        final int bulkSize = 3;

        //if there is a preset for create,  service shall failed during in_progress (upon get status)
        //it there is no preset for create, service shall failed during pending (upon create request)
        List<BasePreset> msoBulkPresets = isPresetForCreate ?
                generateMsoCreateBulkPresets(bulkSize, names) :
                new LinkedList<>();
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN3_TO_ATT_SABABA)
                .addAll(msoBulkPresets)
                .add(new PresetMSOOrchestrationRequestGet("FAILED"))
                .build();
        List<String> jobIds = createBulkOfMacroInstances(presets, false, bulkSize, names);
        Assert.assertEquals(jobIds.size(),bulkSize);
        boolean result = Wait.waitFor(x->{
                List<ServiceInfo> serviceInfoList = serviceListCall().getBody();
                Map<JobStatus, Long> statusCount = serviceInfoList.stream().filter(si->jobIds.contains(si.jobId)).collect(groupingBy(ServiceInfo::getJobStatus, counting()));
                return Objects.equals(statusCount.get(JobStatus.FAILED), 1L) && Objects.equals(statusCount.get(JobStatus.STOPPED), 2L);
            }, null, 15, 1);
        assertTrue(String.format("failed to get jobs [%s] to state of: 1 failed and 2 stopped ",
                String.join(",", jobIds)),result);
    }

    @Test
    public void createBulkOfAssignInstances(){
        Map<Keys, String> names = generateNames();
        final int bulkSize = 2;
        ImmutableList<BasePreset> msoBulkPresets = IntStream.rangeClosed(0, bulkSize-1)
                .mapToObj(i-> new PresetMSOAssignServiceInstanceGen2WithNames(names, i))
                .collect(ImmutableList.toImmutableList());
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN3_TO_ATT_SABABA)
                .addAll(msoBulkPresets)
                .add(new PresetMSOOrchestrationRequestGet())
                .build();
        createBulkOfInstancesAndAssert(presets, true, bulkSize, JobStatus.COMPLETED, names);
    }  

    @Test
    public void tryToCreateBulkOfAssignInstancesErrorResponseFromMso(){
        ImmutableList<BasePreset> presets = ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetMSOServiceInstanceGen2ErrorResponse(406));

        List<String> jobIds = createBulkOfInstancesAndAssert(presets, true,1, JobStatus.FAILED, generateNames());
        String jobId  = jobIds.get(0);
        List<JobAuditStatus> actualMsoAudits = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO.name());
        JobAuditStatus expectedMsoAudit = new JobAuditStatus(UUID.fromString(jobId), "FAILED", JobAuditStatus.SourceStatus.MSO,
                        null,
                        "Http Code:406, \"messageId\":\"SVC0002\",\"text\":\"JSON Object Mapping Request\"" ,
                        false);
        assertThat(actualMsoAudits.get(0), is(expectedMsoAudit));
    }

    @Test
    public void whenGetLongErrorMessageFromMso_ThenAuditFirst2000Chars() {
        Map<Keys, String> names = generateNames();
        ImmutableList<BasePreset> presets = ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetMSOCreateServiceInstanceGen2WithNames(names, 0),
                new PresetMSOOrchestrationRequestGet("FAILED", PresetMSOOrchestrationRequestGet.DEFAULT_REQUEST_ID, MSO_ERROR));

        List<String> jobIds = createBulkOfInstancesAndAssert(presets, false, 1, JobStatus.FAILED, names);
        String jobId  = jobIds.get(0);
        List<JobAuditStatus> actualMsoAudits = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO.name());
        Optional<JobAuditStatus> jobAuditStatus = actualMsoAudits.stream().filter(x -> x.getJobStatus().equals("FAILED")).findFirst();
        assertTrue(jobAuditStatus.isPresent());
        assertThat(jobAuditStatus.get().getAdditionalInfo(), startsWith(MSO_BASE_ERROR));
        assertThat(jobAuditStatus.get().getAdditionalInfo().length(), is(2000));
    }

    @Test
    public void whenHideCompletedService_thenServiceNotReturnInServiceList(){
        List<String> services = createBulkAndWaitForBeCompleted(2);
        hideService(services.get(0));
        List<String> serviceInfoList = serviceListCall().getBody().stream().map(ServiceInfo::getJobId).collect(toList());
        assertThat(serviceInfoList, hasItem(services.get(1)));
        assertThat(serviceInfoList, not(hasItem(services.get(0))));
    }

    @Test
    public void errorResponseInGetStatusFromMso_getAuditStatusFromMso_errorMsgExistInAdditionalInfo(){
        Map<Keys, String> names = generateNames();
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN3_TO_ATT_SABABA)
                .add(new PresetMSOAssignServiceInstanceGen2WithNames(names, 0))
                .add(new PresetMSOOrchestrationRequestGetErrorResponse(406))
                .build();

        final List<String> jobIds = createBulkOfInstancesAndAssert(presets, true,1, JobStatus.IN_PROGRESS, names);
        String jobId = jobIds.get(0);
        Wait.waitFor(y-> getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO.name()).stream()
                        .anyMatch(si -> si.getJobStatus().equals("FAILED")),
                null, 10, 1 );
        List<JobAuditStatus> actualMsoAudits = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO.name());
        List<JobAuditStatus> expectedMsoAudits = Stream.of("REQUESTED", "FAILED")
                .map(status -> new JobAuditStatus(UUID.fromString(jobId),
                        status,
                        JobAuditStatus.SourceStatus.MSO,
                        UUID.fromString("c0011670-0e1a-4b74-945d-8bf5aede1d9c"),
                        status.equals("FAILED") ? "Http Code:406, \"messageId\":\"SVC0002\",\"text\":\"JSON Object Mapping Request\"" : null,
                        false)).collect(toList());
        assertThat(actualMsoAudits, is(expectedMsoAudits));

    }

    @Test
    public void inProgressJobMoreThan24HoursIsFailedInVidAudit(){
        addBulkPendingWithCustomList(Collections.singletonList(new PresetMSOOrchestrationRequestGet("IN_PROGRESS",24)));

        AtomicReference<ServiceInfo> inProgressJob = new AtomicReference<>();
        boolean isJobFound = Wait.waitFor(x->{
            List<ServiceInfo> serviceInfoList = serviceListCall().getBody();
            inProgressJob.set(serviceInfoList.stream().
                    filter(serviceInfo -> serviceInfo.serviceInstanceId.equals(PresetMSOOrchestrationRequestGet.DEFAULT_SERVICE_INSTANCE_ID) && serviceInfo.jobStatus.equals(JobStatus.FAILED))
                    .findFirst()
                    .orElse(null));
            return inProgressJob.get() != null;
        }, null, 15, 1);

        org.junit.Assert.assertTrue("Job with DEFAULT_SERVICE_INSTANCE_ID and status FAILED should present", isJobFound);

        verifyAuditStatuses(inProgressJob.get().jobId, Arrays.asList(JobStatus.PENDING.name(), JobStatus.IN_PROGRESS.name(),JobStatus.FAILED.name()), JobAuditStatus.SourceStatus.VID);
        verifyAuditStatuses(inProgressJob.get().jobId, Arrays.asList("REQUESTED", "IN_PROGRESS"), JobAuditStatus.SourceStatus.MSO);
    }

    @Test
    public void inProgressJobLessThan24HoursIsStillInProgressInVidAudit(){
        addBulkPendingWithCustomList(Collections.singletonList(new PresetMSOOrchestrationRequestGet("IN_PROGRESS",23)));

        AtomicReference<ServiceInfo> inProgressJob = new AtomicReference<>();
        boolean isJobFound = Wait.waitFor(x->{
            List<ServiceInfo> serviceInfoList = serviceListCall().getBody();
            inProgressJob.set(serviceInfoList.stream().filter(serviceInfo -> serviceInfo.serviceInstanceId.equals(PresetMSOOrchestrationRequestGet.DEFAULT_SERVICE_INSTANCE_ID))
                    .findFirst()
                    .orElse(null));
            return inProgressJob.get() != null;
        }, null, 15, 1);

        org.junit.Assert.assertTrue("Job with DEFAULT_SERVICE_INSTANCE_ID should present", isJobFound);
        org.junit.Assert.assertEquals("Tested job status is not as expected", JobStatus.IN_PROGRESS, inProgressJob.get().getJobStatus());

        verifyAuditStatuses(inProgressJob.get().jobId, Arrays.asList(JobStatus.PENDING.name(), JobStatus.IN_PROGRESS.name()), JobAuditStatus.SourceStatus.VID);
        verifyAuditStatuses(inProgressJob.get().jobId, Arrays.asList("REQUESTED", "IN_PROGRESS"), JobAuditStatus.SourceStatus.MSO);
    }

    @Test
    public void verifyAuditStatusOfInstanceGroupId(){
        SimulatorApi.registerExpectationFromPreset(new PresetMSOOrchestrationRequestsManyInstanceStatusesGet(INSTANCE_GROUP_ID_LABEL, INSTANCE_GROUP_LABEL), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        final List<JobAuditStatus> expectedAuditStatusList = getAuditStatusesForInstance("VNFGROUP", "df305d54-75b4-431b-adb2-eb6b9e5460df");
        verifyInstanceAuditStatuses(Arrays.asList(
                new JobAuditStatus("groupTestName", "IN_PROGRESS", UUID.fromString("28502bd2-3aff-4a03-9f2b-5a0d1cb1ca24") , INSTANCE_GROUP_LABEL+" instance creation", null, INSTANCE_GROUP_LABEL),
                new JobAuditStatus("groupTestName", "COMPLETE",UUID.fromString("28502bd2-3aff-4a03-9f2b-5a0d1cb1ca24") , INSTANCE_GROUP_LABEL+" instance creation", null, INSTANCE_GROUP_LABEL),
                new JobAuditStatus("groupTestName", "IN_PROGRESS", UUID.fromString("f711f0ff-24b6-4d7f-9314-4b4eae15f48c") , INSTANCE_GROUP_LABEL+" instance deletion", null, INSTANCE_GROUP_LABEL),
                new JobAuditStatus("groupTestName", "COMPLETE",UUID.fromString("f711f0ff-24b6-4d7f-9314-4b4eae15f48c")  , INSTANCE_GROUP_LABEL+" instance deletion", null, INSTANCE_GROUP_LABEL)),
                expectedAuditStatusList);
    }

    @Test(expectedExceptions = HttpClientErrorException.class)
    public void verifyAuditStatusOfInstanceGroupId_notExistingVidType(){
        try {
            getAuditStatusesForInstance("KUKU", "df305d54-75b4-431b-adb2-eb6b9e5460df");
        } catch (HttpClientErrorException e){ //to verify the properiatary statusCode field
            assertThat("Code is not as expected", HttpStatus.BAD_REQUEST.equals(e.getStatusCode()));
            throw e;
        }
    }

    @Test(expectedExceptions = HttpServerErrorException.class)
    public void verifyAuditStatusOfInstanceGroupId_notExistingMsoInstanceId(){
        try {
            getAuditStatusesForInstance("VNFGROUP", "df305d54-75b4-431b-adb2-eb6b9e5460aa");
        } catch (HttpServerErrorException e){ //to verify the properiatary statusCode field
            assertThat("Code is not as expected", HttpStatus.INTERNAL_SERVER_ERROR.equals(e.getStatusCode()));
            throw e;
        }
    }

    @DataProvider
    public static Object[][] macroAndALaCarteBulk(){
        return new Object[][]{{CREATE_BULK_OF_MACRO_REQUEST}, {CREATE_BULK_OF_ALACARTE_REQUEST_WITH_VNF}};
    }

    @Test(dataProvider = "macroAndALaCarteBulk", expectedExceptions = HttpClientErrorException.class)
    @FeatureTogglingTest(FLAG_1906_INSTANTIATION_API_USER_VALIDATION)
    public void verifyCreateBulkOfInstancesUserPermissionValidation(String requestDetailsFileName) {
        login(new UserCredentials("mo57174000", "mo57174000", null, null, null));
        try {
            createBulkOfInstances(false, 1, Collections.EMPTY_MAP, requestDetailsFileName);
        } catch (HttpClientErrorException e){
            assertEquals("Code is not as expected", HttpStatus.FORBIDDEN.value(), e.getStatusCode().value());
            throw e;
        }
        finally {
            login();
        }

    }

}
