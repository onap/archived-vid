package org.onap.vid.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAISearchNodeQueryEmptyResult;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOAssignServiceInstanceGen2WithNames;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNames;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGetErrorResponse;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2ErrorResponse;
import org.onap.vid.model.asyncInstantiation.JobAuditStatus;
import org.onap.vid.model.asyncInstantiation.ServiceInfo;
import org.onap.vid.model.mso.MsoResponseWrapper2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.JobStatus;
import vid.automation.test.services.SimulatorApi;
import java.util.*;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

@FeatureTogglingTest({Features.FLAG_ASYNC_JOBS, Features.FLAG_ASYNC_INSTANTIATION})
public class AsyncInstantiationApiTest extends BaseMsoApiTest {

    private static final String CREATE_BULK_OF_MACRO_REQUEST = "asyncInstantiation/vidRequestCreateBulkOfMacro.json";

    @DataProvider
    public static Object[][] trueAndFalse() {
            return new Object[][]{{TRUE},{FALSE}};
    }

    private String getCreateBulkUri() {
        return uri.toASCIIString() + "/asyncInstantiation/bulk";
    }

    private String getHideServiceUri(String jobId) {
        return uri.toASCIIString() + "/asyncInstantiation/hide/"+jobId;
    }

    private String getServiceInfoUrl() {
        return uri.toASCIIString() + "/asyncInstantiation";
    }

    private String getJobAuditUrl() {
        return uri.toASCIIString() + "/asyncInstantiation/auditStatus/{JOB_ID}?source={SOURCE}";
    }

    private String getDeleteServiceUrl(String uuid) {
        return uri.toASCIIString() + "/asyncInstantiation/job/" + uuid;
    }

    public static class JobIdAndStatusMatcher extends BaseMatcher<ServiceInfo> {
        private String expectedJobId;

        public JobIdAndStatusMatcher(String expectedJobId) {
            this.expectedJobId = expectedJobId;
        }

        @Override
        public boolean matches(Object item) {
            if (!(item instanceof ServiceInfo)) {
                return false;
            }
            ServiceInfo serviceInfo = (ServiceInfo) item;
            return expectedJobId.equals(serviceInfo.jobId);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("failed to find job with uuid ")
                    .appendValue(expectedJobId);
        }
    }



    @Test
    public void createBulkOfCreateInstances(){
        Map<Keys, String> names = generateNames();
        final int bulkSize = 3;
        ImmutableList<BasePreset> presets = addPresetsForCreateBulkOfCreateInstances(bulkSize, names);
        createBulkOfInstancesAndAssert(presets, false, bulkSize, JobStatus.COMPLETED, names);
    }

    private Map<Keys,String> generateNames() {
        return Stream.of(Keys.values()).collect(
                Collectors.toMap(x->x, x -> UUID.randomUUID().toString().replace("-","")));
    }

    private ImmutableList<BasePreset> addPresetsForCreateBulkOfCreateInstances(int bulkSize, Map<Keys, String> names){
        ImmutableList<BasePreset> msoBulkPresets = IntStream.rangeClosed(1,bulkSize).
                mapToObj(i-> new PresetMSOCreateServiceInstanceGen2WithNames(names, i))
                .collect(ImmutableList.toImmutableList());
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(new PresetAAISearchNodeQueryEmptyResult())
                .addAll(msoBulkPresets)
                .add(new PresetMSOOrchestrationRequestGet())
                .build();
        return presets;

    }

    private ResponseEntity<List<JobAuditStatus>> auditStatusCall(String url) {
        return restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<JobAuditStatus>>() {});
    }

    @DataProvider
    public static Object[][] auditSources() {
        return new Object[][]{{JobAuditStatus.SourceStatus.VID},{JobAuditStatus.SourceStatus.MSO}};
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
                    .map(status->new JobAuditStatus(UUID.fromString(jobId),
                            status.name(),
                            JobAuditStatus.SourceStatus.VID,
                            null,
                            null,
                            status.equals(JobStatus.COMPLETED))).collect(toList());
            assertThat(actualVidAudits, is(expectedVidAudits));

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

    protected List<String> createBulkAndWaitForBeCompleted(int bulkSize){
        Map<Keys, String> names = generateNames();
        ImmutableList<BasePreset> presets = addPresetsForCreateBulkOfCreateInstances(bulkSize, names);
        final List<String> jobIds = createBulkOfInstances(presets, false, bulkSize, names);
        Assert.assertEquals(jobIds.size(),bulkSize);

        assertTrue(String.format("Not all services with ids: %s are in state completed after 30 sec",
                jobIds.stream().collect(joining(","))),

                Wait.waitFor(y-> serviceListCall().getBody().stream()
                        .filter(si -> jobIds.contains(si.jobId))
                        .filter(si -> si.jobStatus==JobStatus.COMPLETED)
                        .count() == bulkSize,
                null, 30, 1 ));
        return jobIds;
    }

    private List<JobAuditStatus> getAuditStatuses(String jobUUID, String source){
        String url = getJobAuditUrl().replace("{JOB_ID}",jobUUID).replace("{SOURCE}", source);
        ResponseEntity<List<JobAuditStatus>> statusesResponse = auditStatusCall(url);
        assertThat(statusesResponse.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
        return statusesResponse.getBody();
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

    private Map<String, JobStatus> addBulkAllPendingButOneInProgress(){
        return addBulkAllPendingButOneInProgress(3);
    }
    
    private Map<String, JobStatus> addBulkAllPendingButOneInProgress(int bulkSize){
        Map<Keys, String> names = generateNames();
        ImmutableList<BasePreset> msoBulkPresets = IntStream.rangeClosed(1,bulkSize)
                .mapToObj(i-> new PresetMSOCreateServiceInstanceGen2WithNames(names, i))
                .collect(ImmutableList.toImmutableList());
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAISearchNodeQueryEmptyResult())
                .add(new PresetAAIGetSubscribersGet())
                .addAll(msoBulkPresets)
                .add(new PresetMSOOrchestrationRequestGet("IN_PROGRESS"))
                .build();
        final List<String> jobIds = createBulkOfInstances(presets, false, bulkSize, names);

        // wait for single IN_PROGRESS, so statuses will stop from changing
        Wait.waitFor(foo -> serviceListCall().getBody().stream()
                        .filter(si -> jobIds.contains(si.jobId))
                        .anyMatch(si -> si.jobStatus.equals(JobStatus.IN_PROGRESS)),
                null, 20, 1);

        final Map<String, JobStatus> statusMapBefore = serviceListCall().getBody().stream()
                .filter(si -> jobIds.contains(si.jobId))
                .collect(toMap(si -> si.jobId, si -> si.jobStatus));

        assertThat(jobIds, hasSize(bulkSize));


        return statusMapBefore;
    }

    private String deleteOneJobHavingTheStatus(Map<String, JobStatus> jobIdToStatus, JobStatus jobStatus) {
        final String jobToDelete = jobIdToStatus.entrySet().stream()
                .filter(entry -> entry.getValue().equals(jobStatus))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow(() -> new AssertionError("no job in " + jobStatus + " state: " + jobIdToStatus));


        restTemplate.delete(getDeleteServiceUrl(jobToDelete));

        return jobToDelete;
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
        assertThat(statuses.get(JobStatus.IN_PROGRESS), everyItem(hasProperty("serviceInstanceName", endsWith("_001"))));

        assertThat(statuses.get(JobStatus.PENDING), hasSize(bulkSize - 1));
    }


    @Test(dataProvider = "trueAndFalse" )
    public void whenServiceInBulkFailed_otherServicesAreStopped(Boolean isPresetForCreate){
        Map<Keys, String> names = generateNames();
        final int bulkSize = 3;

        //if there is a preset for create,  service shall failed during in_progress (upon get status)
        //it there is no preset for create, service shall failed during pending (upon create request)
        List<BasePreset> msoBulkPresets = isPresetForCreate ?
                IntStream.rangeClosed(1,bulkSize)
                        .mapToObj(i-> new PresetMSOCreateServiceInstanceGen2WithNames(names, i))
                        .collect(ImmutableList.toImmutableList()) :
                new LinkedList<>();
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(new PresetAAISearchNodeQueryEmptyResult())
                .addAll(msoBulkPresets)
                .add(new PresetMSOOrchestrationRequestGet("FAILED"))
                .build();
        List<String> jobIds = createBulkOfInstances(presets, false, bulkSize, names);
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
        ImmutableList<BasePreset> msoBulkPresets = IntStream.rangeClosed(1,bulkSize)
                .mapToObj(i-> new PresetMSOAssignServiceInstanceGen2WithNames(names, i))
                .collect(ImmutableList.toImmutableList());
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(new PresetAAISearchNodeQueryEmptyResult())
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
                new PresetAAISearchNodeQueryEmptyResult(),
                new PresetMSOServiceInstanceGen2ErrorResponse(406));

        List<String> jobIds = createBulkOfInstancesAndAssert(presets, true,1, JobStatus.FAILED, generateNames());
        String jobId  = jobIds.get(0);
        List<JobAuditStatus> actualMsoAudits = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO.name());
        JobAuditStatus expectedMsoAudit = new JobAuditStatus(UUID.fromString(jobId),"FAILED",JobAuditStatus.SourceStatus.MSO,
                        null,
                        "Http Code:406, \"messageId\":\"SVC0002\",\"text\":\"JSON Object Mapping Request\"" ,
                        false);
        assertThat(actualMsoAudits.get(0), is(expectedMsoAudit));
    }

    @Test
    public void whenHideCompletedService_thenServiceNotReturnInServiceList(){
        List<String> services = createBulkAndWaitForBeCompleted(2);
        hideService(services.get(0));
        List<String> serviceInfoList = serviceListCall().getBody().stream().map(ServiceInfo::getJobId).collect(toList());
        assertThat(serviceInfoList, hasItem(services.get(1)));
        assertThat(serviceInfoList, not(hasItem(services.get(0))));
    }

    private MsoResponseWrapper2 hideService(String jobId) {
        MsoResponseWrapper2 responseWrapper2 = callMsoForResponseWrapper(org.springframework.http.HttpMethod.POST, getHideServiceUri(jobId), "");
        return responseWrapper2;
    }

    private List<String> createBulkOfInstancesAndAssert(ImmutableList<BasePreset> presets, boolean isPause, int bulkSize, JobStatus finalState, Map<Keys, String> names){
        List<String> jobIds = createBulkOfInstances(presets, isPause, bulkSize, names);
        Assert.assertEquals(jobIds.size(),bulkSize);
        for(String jobId: jobIds) {
            ServiceInfo expectedServiceInfo = new ServiceInfo("ab2222", JobStatus.IN_PROGRESS, isPause, "someID",
                    "someName", "myProject", "NFT1", "NFTJSSSS-NFT1", "greatTenant", "greatTenant", "mtn3", null,
                    "mySubType", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", null, names.get(Keys.SERVICE_NAME),
                    "300adb1e-9b0c-4d52-bfb5-fa5393c4eabb", "AIM_TRANSPORT_00004", "1.0", jobId, null);
            JobInfoChecker jobInfoChecker = new JobInfoChecker(
                    restTemplate, ImmutableSet.of(JobStatus.PENDING, JobStatus.IN_PROGRESS, finalState), jobId, expectedServiceInfo);
            boolean result = jobInfoChecker.test(null);
            assertTrue("service info of jobId: " + jobId + " was in status: " + jobInfoChecker.lastStatus, result);

            jobInfoChecker.setExpectedJobStatus(ImmutableSet.of(finalState));
            if (ImmutableList.of(JobStatus.COMPLETED, JobStatus.PAUSE).contains(finalState)) {
                expectedServiceInfo.serviceInstanceId = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
            }
            result = Wait.waitFor(jobInfoChecker, null, 20, 1);
            assertTrue("service info of jobId: " + jobId + " was in status: " + jobInfoChecker.lastStatus, result);
        }

        return jobIds;
    }

    private List<String> createBulkOfInstances(ImmutableList<BasePreset> presets, boolean isPause, int bulkSize, Map<Keys, String> names){

        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);

        String requestBody = TestUtils.convertRequest(objectMapper, CREATE_BULK_OF_MACRO_REQUEST);
        requestBody = requestBody.replace("\"IS_PAUSE_VALUE\"", String.valueOf(isPause)).replace("\"BULK_SIZE\"", String.valueOf(bulkSize));
        for (Map.Entry<Keys, String> e : names.entrySet()) {
            requestBody = requestBody.replace(e.getKey().name(), e.getValue());
        }
        MsoResponseWrapper2 responseWrapper2 = callMsoForResponseWrapper(org.springframework.http.HttpMethod.POST, getCreateBulkUri(), requestBody);
        assertNotNull(responseWrapper2);
        return (List<String>)responseWrapper2.getEntity();
    }

    public class JobInfoChecker<Integer> implements Predicate<Integer> {

        private final RestTemplate restTemplate;
        private Set<JobStatus> expectedJobStatus;
        private ServiceInfo expectedServiceInfo;
        private final String jobId;
        private JobStatus lastStatus;

        public JobInfoChecker(RestTemplate restTemplate, Set<JobStatus> expectedJobStatus, String jobId, ServiceInfo expectedServiceInfo) {
            this.restTemplate = restTemplate;
            this.expectedJobStatus = expectedJobStatus;
            this.jobId = jobId;
            this.expectedServiceInfo = expectedServiceInfo;
        }

        public void setExpectedJobStatus(Set<JobStatus> expectedJobStatus) {
            this.expectedJobStatus = expectedJobStatus;
        }

        @Override
        public boolean test(Integer integer) {
            ResponseEntity<List<ServiceInfo>> serviceListResponse = serviceListCall();
            assertThat(serviceListResponse.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
            assertThat(serviceListResponse.getBody(), hasItem(new JobIdAndStatusMatcher(jobId)));
            ServiceInfo serviceInfoFromDB = serviceListResponse.getBody().stream()
                    .filter(serviceInfo -> serviceInfo.jobId.equals(jobId))
                    .findFirst().orElse(null);
            Assert.assertNotNull(serviceInfoFromDB);
            Assert.assertEquals(serviceInfoDataReflected(serviceInfoFromDB), serviceInfoDataReflected(expectedServiceInfo));
            assertTrue("actual service instance doesn't contain template service name:" + expectedServiceInfo.serviceInstanceName,
                    serviceInfoFromDB.serviceInstanceName.contains(expectedServiceInfo.serviceInstanceName));
            if (serviceInfoFromDB.jobStatus==JobStatus.IN_PROGRESS || serviceInfoFromDB.jobStatus==JobStatus.COMPLETED) {
                assertTrue("actual service instance doesn't contain template service name and trailing numbers:" + expectedServiceInfo.serviceInstanceName,
                        serviceInfoFromDB.serviceInstanceName.contains(expectedServiceInfo.serviceInstanceName+"_00"));
            }

            if (expectedServiceInfo.serviceInstanceId != null) {
                assertThat(serviceInfoFromDB.serviceInstanceId, is(expectedServiceInfo.serviceInstanceId));
            }
            lastStatus = serviceInfoFromDB.jobStatus;
            return expectedJobStatus.contains(serviceInfoFromDB.jobStatus);
        }
    }

    private ResponseEntity<List<ServiceInfo>> serviceListCall() {
        return restTemplate.exchange(
                getServiceInfoUrl(),
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ServiceInfo>>() {});
    }

    //serialize fields except of fields we cannot know ahead of time
    private static String serviceInfoDataReflected(ServiceInfo service1) {
        return new ReflectionToStringBuilder(service1, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("jobStatus", "templateId", "statusModifiedDate", "createdBulkDate", "serviceInstanceId", "serviceInstanceName")
                .toString();
    }

    @Test
    public void errorResponseInGetStatusFromMso_getAuditStatusFromMso_errorMsgExistInAdditionalInfo(){
        Map<Keys, String> names = generateNames();
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(new PresetAAISearchNodeQueryEmptyResult())
                .add(new PresetMSOAssignServiceInstanceGen2WithNames(names, 1))
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

}
