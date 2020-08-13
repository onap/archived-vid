package org.onap.vid.api;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static vid.automation.test.utils.ExtendedHamcrestMatcher.hasItemsFromCollection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseDelete;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNames;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.vid.model.asyncInstantiation.JobAuditStatus;
import org.onap.vid.model.asyncInstantiation.ServiceInfo;
import org.onap.vid.model.mso.MsoResponseWrapper2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.JobStatus;
import vid.automation.test.model.ServiceAction;
import vid.automation.test.services.AsyncJobsService;
import vid.automation.test.services.SimulatorApi;

public class AsyncInstantiationBase extends BaseMsoApiTest {

    public static final String CREATE_BULK_OF_ALACARTE_REQUEST_WITH_VNF = "asyncInstantiation/vidRequestCreateALaCarteWithVnf.json";
    protected static final String CREATE_BULK_OF_MACRO_REQUEST = "asyncInstantiation/vidRequestCreateBulkOfMacro.json";

    protected static final String MSO_BASE_ERROR =
            "Received error from SDN-C: java.lang.IllegalArgumentException: All keys must be specified for class org."+
            "opendaylight.yang.gen.v1.org.onap.sdnc.northbound.generic.resource.rev170824.vf.module.assignments.vf."+
            "module.assignments.vms.VmKey. Missing key is getVmType. Supplied key is VmKey [].";
    protected static final String MSO_ERROR = MSO_BASE_ERROR + StringUtils.repeat(" and a lot of sentences for long message", 60);

    @BeforeClass
    protected void muteAndDropNameCounter() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.muteAllAsyncJobs();
        asyncJobsService.dropAllFromNameCounter();
    }

    @AfterMethod
    protected void muteAllAsyncJobs() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.muteAllAsyncJobs();
    }

    @DataProvider
    public static Object[][] trueAndFalse() {
            return new Object[][]{{TRUE},{FALSE}};
    }

    protected String getCreateBulkUri() {
        return uri.toASCIIString() + "/asyncInstantiation/bulk";
    }

    protected String getHideServiceUri(String jobId) {
        return uri.toASCIIString() + "/asyncInstantiation/hide/"+jobId;
    }

    protected String getServiceInfoUrl() {
        return uri.toASCIIString() + "/asyncInstantiation";
    }

    protected String getTemplateInfoUrl(String serviceModelId) {
        return uri.toASCIIString() + "/instantiationTemplates?serviceModelId=" + serviceModelId;
    }

    protected String getJobAuditUrl() {
        return uri.toASCIIString() + "/asyncInstantiation/auditStatus/{JOB_ID}?source={SOURCE}";
    }

    protected String getMsoJobAuditUrl() {
        return uri.toASCIIString() + "/asyncInstantiation/auditStatus/{JOB_ID}/mso";
    }

    protected String getDeleteServiceUrl(String uuid) {
        return uri.toASCIIString() + "/asyncInstantiation/job/" + uuid;
    }

    protected String getInstanceAuditInfoUrl() {
        return uri.toASCIIString() + "/asyncInstantiation/auditStatus/{TYPE}/{INSTANCE_ID}/mso";
    }

    protected String getRetryJobUrl() {
        return uri.toASCIIString() + "/asyncInstantiation/retry/{JOB_ID}";
    }
    protected String getTopologyForRetryUrl() {
        return uri.toASCIIString() + "/asyncInstantiation/bulkForRetry/{JOB_ID}";
    }


    protected String getRetryJobWithChangedDataUrl() {
        return uri.toASCIIString() + "/asyncInstantiation/retryJobWithChangedData/{JOB_ID}";
    }

    protected boolean getExpectedRetryEnabled(JobStatus jobStatus) {
        return Features.FLAG_1902_RETRY_JOB.isActive() && (jobStatus==JobStatus.FAILED || jobStatus==JobStatus.COMPLETED_WITH_ERRORS
                || jobStatus==JobStatus.FAILED_AND_PAUSED);
    }

    public List<BasePreset> getPresets(List<PresetMSOBaseDelete> presetOnDeleteList, List<PresetMSOBaseCreateInstancePost> presetOnCreateList, List<PresetMSOOrchestrationRequestGet> presetInProgressList) {

        final ImmutableList.Builder<BasePreset> basePresetBuilder = new ImmutableList.Builder<>();
        basePresetBuilder
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .addAll(presetOnDeleteList)
                .addAll(presetOnCreateList)
                .addAll(presetInProgressList);
        return basePresetBuilder.build();
    }

    public List<BasePreset> getDeletePresets(List<PresetMSOBaseDelete> presetOnDeleteList, List<PresetMSOOrchestrationRequestGet> presetInProgressList) {
        return getPresets(presetOnDeleteList, emptyList(), presetInProgressList);
    }

    public List<BasePreset> getPresets(List<PresetMSOBaseCreateInstancePost> presetOnCreateList, List<PresetMSOOrchestrationRequestGet> presetInProgressList) {
        return getPresets(emptyList(), presetOnCreateList, presetInProgressList);
    }

    public void assertServiceInfoSpecific1(String jobId, JobStatus jobStatus, String serviceInstanceName, String userName) {
        assertServiceInfoSpecific1(jobId, jobStatus, serviceInstanceName, userName, null, ServiceAction.INSTANTIATE);
    }

    public void assertServiceInfoSpecific1(String jobId, JobStatus jobStatus, String serviceInstanceName, String userName, String instanceId, ServiceAction action) {
        assertExpectedStatusAndServiceInfo(jobStatus, jobId, new ServiceInfo(
                userName, jobStatus, false,
                "038d99af-0427-42c2-9d15-971b99b9b489", "Lucine Sarika", "zasaki",
                "de738e5f-3704-4a14-b98f-3bf86ac0c0a0", "voloyakane-senamo",
                "c85f0e80-0636-44a4-8cb2-4ec00d056e79", "Hedvika Wendelin",
                "a93f8383-707e-43fa-8191-a6e69a1aab17", null,
                "TYLER SILVIA", "SILVIA ROBBINS",
                instanceId, serviceInstanceName,
                "e3c34d88-a216-4f1d-a782-9af9f9588705", "gayawabawe", "5.1",
                jobId, null, action, false)
        );
    }

    public void assertServiceInfoSpecific1(String jobId, JobStatus jobStatus, String serviceInstanceName) {
        assertServiceInfoSpecific1(jobId, jobStatus, serviceInstanceName, "us16807000");
    }

    protected void assertAuditStatuses(String jobId, List<JobAuditStatus> expectedVidStatuses, List<JobAuditStatus> expectedMsoStatuses) {
        assertAuditStatuses(jobId, expectedVidStatuses, expectedMsoStatuses, 15);
    }

    protected void assertAuditStatuses(String jobId, List<JobAuditStatus> expectedVidStatuses, List<JobAuditStatus> expectedMsoStatuses, long timeoutInSeconds) {
        assertAndRetryIfNeeded(() -> {
            final List<JobAuditStatus> auditVidStatuses = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.VID.name());
            assertThat(auditVidStatuses, hasItemsFromCollection(expectedVidStatuses));
            if (expectedMsoStatuses!=null) {
                final List<JobAuditStatus> auditMsoStatuses = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO.name());
                assertThat(auditMsoStatuses, containsInAnyOrder(expectedMsoStatuses.toArray()));
            }
        }, timeoutInSeconds);
    }

    protected void assertAndRetryIfNeeded(Runnable asserter, long timeoutInSeconds) {
        TestUtils.assertAndRetryIfNeeded(timeoutInSeconds, asserter);
    }

    protected ImmutableList<JobAuditStatus> vidAuditStatusesCompletedWithErrors(String jobId) {
        return ImmutableList.of(
                vidAuditStatus(jobId, "PENDING", false),
                vidAuditStatus(jobId, "IN_PROGRESS", false),
                vidAuditStatus(jobId, "COMPLETED_WITH_ERROR", true)
        );
    }

    protected ImmutableList<JobAuditStatus> vidAuditStatusesFailed(String jobId) {
        return ImmutableList.of(
                vidAuditStatus(jobId, "PENDING", false),
                vidAuditStatus(jobId, "IN_PROGRESS", false),
                vidAuditStatus(jobId, "FAILED", true)
        );
    }
    protected ImmutableList<JobAuditStatus> vidAuditStatusesFailedAndPaused(String jobId) {
        return ImmutableList.of(
                vidAuditStatus(jobId, "PENDING", false),
                vidAuditStatus(jobId, "IN_PROGRESS", false),
                vidAuditStatus(jobId, "FAILED_AND_PAUSED", true)
        );
    }
    protected JobAuditStatus vidAuditStatus(String jobId, String jobStatus, boolean isFinal) {
        return new JobAuditStatus(UUID.fromString(jobId), jobStatus, JobAuditStatus.SourceStatus.VID, null, null, isFinal);
    }

    public static class JobIdAndStatusMatcher extends BaseMatcher<ServiceInfo> {
        protected String expectedJobId;

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



    protected Map<Keys,String> generateNames() {
        return Stream.of(Keys.values()).collect(
                Collectors.toMap(x->x, x -> UUID.randomUUID().toString().replace("-","")));
    }

    protected ImmutableList<BasePreset> addPresetsForCreateBulkOfCreateInstances(int bulkSize, Map<Keys, String> names){
        ImmutableList<BasePreset> msoBulkPresets = generateMsoCreateBulkPresets(bulkSize, names);
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN3_TO_ATT_SABABA)
                .addAll(msoBulkPresets)
                .add(new PresetMSOOrchestrationRequestGet())
                .build();
        return presets;

    }

    protected ImmutableList<BasePreset> generateMsoCreateBulkPresets(int bulkSize, Map<Keys, String> names) {
        return IntStream.rangeClosed(0, bulkSize-1).
                mapToObj(i-> new PresetMSOCreateServiceInstanceGen2WithNames(names, i))
                .collect(ImmutableList.toImmutableList());
    }

    protected ResponseEntity<List<JobAuditStatus>> auditStatusCall(String url) {
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



    protected List<String> createBulkAndWaitForBeCompleted(int bulkSize){
        Map<Keys, String> names = generateNames();
        ImmutableList<BasePreset> presets = addPresetsForCreateBulkOfCreateInstances(bulkSize, names);
        final List<String> jobIds = createBulkOfMacroInstances(presets, false, bulkSize, names);
        Assert.assertEquals(jobIds.size(),bulkSize);

        waitForJobsToSuccessfullyCompleted(bulkSize, jobIds);
        return jobIds;
    }

    public void waitForJobsToSuccessfullyCompleted(int bulkSize, List<String> jobIds) {
        assertTrue(String.format("Not all services with ids: %s are in state completed after 30 sec",
                jobIds.stream().collect(joining(","))),

                Wait.waitFor(y-> serviceListCall().getBody().stream()
                        .filter(si -> jobIds.contains(si.jobId))
                        .filter(si -> si.jobStatus== JobStatus.COMPLETED)
                        .count() == bulkSize,
                null, 30, 1 ));
    }

    protected List<JobAuditStatus> getJobMsoAuditStatusForAlaCarte(String jobUUID, String requestId, String serviceInstanceId){
        String url = getMsoJobAuditUrl().replace("{JOB_ID}",jobUUID);

        if(!StringUtils.isEmpty(requestId)) {
            url = url + "?requestId=" + requestId;
            if(!StringUtils.isEmpty(serviceInstanceId)) {
                url = url + "&serviceInstanceId=" + serviceInstanceId;
            }
        }
        return callAuditStatus(url);
    }

    protected List<JobAuditStatus> getAuditStatuses(String jobUUID, String source){
        String url = getJobAuditUrl().replace("{JOB_ID}",jobUUID).replace("{SOURCE}", source);
        return callAuditStatus(url);
    }

    protected List<JobAuditStatus> getAuditStatusesForInstance(String type, String instanceId){
        String url = getInstanceAuditInfoUrl().replace("{TYPE}",type).replace("{INSTANCE_ID}", instanceId);
        return callAuditStatus(url);
    }

    private List<JobAuditStatus> callAuditStatus(String url) {
        ResponseEntity<List<JobAuditStatus>> statusesResponse = auditStatusCall(url);
        assertThat(statusesResponse.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));
        return statusesResponse.getBody();
    }

    protected Map<String, JobStatus> addBulkAllPendingButOneInProgress(){
        return addBulkAllPendingButOneInProgress(3);
    }

    protected Map<String, JobStatus> addBulkAllPendingButOneInProgress(int bulkSize){
        Map<Keys, String> names = generateNames();
        ImmutableList<BasePreset> msoBulkPresets = generateMsoCreateBulkPresets(bulkSize, names);
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN3_TO_ATT_SABABA)
                .addAll(msoBulkPresets)
                .add(new PresetMSOOrchestrationRequestGet("IN_PROGRESS"))
                .build();
        final List<String> jobIds = createBulkOfMacroInstances(presets, false, bulkSize, names);

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

    protected String deleteOneJobHavingTheStatus(Map<String, JobStatus> jobIdToStatus, JobStatus jobStatus) {
        final String jobToDelete = jobIdToStatus.entrySet().stream()
                .filter(entry -> entry.getValue().equals(jobStatus))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow(() -> new AssertionError("no job in " + jobStatus + " state: " + jobIdToStatus));


        restTemplate.delete(getDeleteServiceUrl(jobToDelete));

        return jobToDelete;
    }


    protected MsoResponseWrapper2 hideService(String jobId) {
        MsoResponseWrapper2 responseWrapper2 = callMsoForResponseWrapper(org.springframework.http.HttpMethod.POST, getHideServiceUri(jobId), "");
        return responseWrapper2;
    }

    protected List<String> createBulkOfInstancesAndAssert(ImmutableList<BasePreset> presets, boolean isPause, int bulkSize, JobStatus finalState, Map<Keys, String> names){
        List<String> jobIds = createBulkOfMacroInstances(presets, isPause, bulkSize, names);
        Assert.assertEquals(jobIds.size(), bulkSize);
        for(String jobId: jobIds) {
            assertExpectedStatusAndServiceInfo(isPause, finalState, names, jobId);
        }

        return jobIds;
    }

    protected void assertExpectedStatusAndServiceInfo(boolean isPause, JobStatus finalState, Map<Keys, String> names, String jobId) {
        assertExpectedStatusAndServiceInfo(finalState, jobId, new ServiceInfo("us16807000", JobStatus.IN_PROGRESS, isPause, "someID",
                "someName", "myProject", "NFT1", "NFTJSSSS-NFT1", "greatTenant", "greatTenant", "hvf3", null,
                "mySubType", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", null, names.get(Keys.SERVICE_NAME),
                "5c9e863f-2716-467b-8799-4a67f378dcaa", "AIM_TRANSPORT_00004", "1.0", jobId, null, ServiceAction.INSTANTIATE, false));
    }

    protected void assertExpectedStatusAndServiceInfo(JobStatus finalState, String jobId, ServiceInfo expectedServiceInfo) {
        assertExpectedStatusAndServiceInfo(finalState, jobId, PATIENCE_LEVEL.FAIL_FAST, expectedServiceInfo);
    }

    enum PATIENCE_LEVEL { FAIL_FAST, FAIL_SLOW, FAIL_VERY_SLOW }

    protected void assertExpectedStatusAndServiceInfo(JobStatus finalState, String jobId, PATIENCE_LEVEL patienceLevel, ServiceInfo expectedServiceInfo) {
        JobInfoChecker<Integer> jobInfoChecker = new JobInfoChecker<>(
                restTemplate, ImmutableSet.of(JobStatus.PENDING, JobStatus.IN_PROGRESS, finalState), jobId, expectedServiceInfo);
        boolean result = jobInfoChecker.test(null);
        assertTrue("service info of jobId: " + jobId + " was in status: " + jobInfoChecker.lastStatus, result);

        jobInfoChecker.setExpectedJobStatus(ImmutableSet.of(finalState));
        if (ImmutableList.of(JobStatus.COMPLETED, JobStatus.PAUSE).contains(finalState) && expectedServiceInfo.serviceInstanceId==null) {
            expectedServiceInfo.serviceInstanceId = BaseMSOPreset.DEFAULT_INSTANCE_ID;
        }
        result = Wait.waitFor(jobInfoChecker, null, 30, waitIntervalBy(patienceLevel));
        assertTrue("service info of jobId: " + jobId + " was in status: " + jobInfoChecker.lastStatus, result);
    }

    private int waitIntervalBy(PATIENCE_LEVEL patienceLevel) {
        switch (patienceLevel) {
            case FAIL_SLOW:
                return 2;
            case FAIL_VERY_SLOW:
                return 3;
            default:
                return 1;
        }
    }

    protected List<String> createBulkOfMacroInstances(ImmutableList<BasePreset> presets, boolean isPause, int bulkSize, Map<Keys, String> names) {
        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        return createBulkOfInstances(isPause, bulkSize, names, CREATE_BULK_OF_MACRO_REQUEST);
    }

    public List<String> createBulkOfInstances(boolean isPause, int bulkSize, Map<Keys, String> names, String requestDetailsFileName){

        String requestBody = TestUtils.convertRequest(objectMapper, requestDetailsFileName);
        requestBody = requestBody.replace("\"IS_PAUSE_VALUE\"", String.valueOf(isPause)).replace("\"BULK_SIZE\"", String.valueOf(bulkSize));
        for (Map.Entry<Keys, String> e : names.entrySet()) {
            requestBody = requestBody.replace(e.getKey().name(), e.getValue());
        }
        MsoResponseWrapper2 responseWrapper2 = callMsoForResponseWrapper(org.springframework.http.HttpMethod.POST, getCreateBulkUri(), requestBody);
        assertNotNull(responseWrapper2);
        return (List<String>)responseWrapper2.getEntity();
    }

    protected List<String> retryJob(String jobId) {
        ResponseEntity<String> retryBulkPayload = getRetryBulk(jobId);
        return retryJobWithChangedData(jobId, retryBulkPayload.getBody());
    }

    protected List<String> retryJobWithChangedData(String jobId, String requestBody) {
        String retryUri = getRetryJobWithChangedDataUrl();
        retryUri = retryUri.replace("{JOB_ID}", jobId);
        MsoResponseWrapper2 responseWrapper2 = callMsoForResponseWrapper(HttpMethod.POST, retryUri, requestBody);
        assertNotNull(responseWrapper2);
        return (List<String>)responseWrapper2.getEntity();
    }

    protected ResponseEntity<String> getRetryBulk(String jobId) {
        String retryUri = getTopologyForRetryUrl();
        retryUri = retryUri.replace("{JOB_ID}", jobId);
        return restTemplateErrorAgnostic.getForEntity(retryUri, String.class);
    }

    protected Object getResourceAuditInfo(String trackById) {
        return restTemplate.getForObject(buildUri("/asyncInstantiation/auditStatusForRetry/{trackById}"), Object.class, trackById);
    }

    public class JobInfoChecker<Integer> implements Predicate<Integer> {

        protected final RestTemplate restTemplate;
        protected Set<JobStatus> expectedJobStatus;
        protected ServiceInfo expectedServiceInfo;
        protected final String jobId;
        protected JobStatus lastStatus;

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

            if (expectedServiceInfo.serviceInstanceId != null && ImmutableList.of(JobStatus.COMPLETED, JobStatus.PAUSE, JobStatus.COMPLETED_WITH_ERRORS).contains(serviceInfoFromDB.jobStatus)) {
                MatcherAssert.assertThat("service instance id is wrong", serviceInfoFromDB.serviceInstanceId, CoreMatchers.is(expectedServiceInfo.serviceInstanceId));
            }
            if (expectedJobStatus.size()==1) {
                assertEquals("job status is wrong", getExpectedRetryEnabled((JobStatus)(expectedJobStatus.toArray()[0])), serviceInfoFromDB.isRetryEnabled);
            }
            lastStatus = serviceInfoFromDB.jobStatus;
            return expectedJobStatus.contains(serviceInfoFromDB.jobStatus);
        }
    }

    protected ResponseEntity<List<ServiceInfo>> serviceListCall() {
        return restTemplate.exchange(
                getServiceInfoUrl(),
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ServiceInfo>>() {});
    }

    //serialize fields except of fields we cannot know ahead of time
    protected static String serviceInfoDataReflected(ServiceInfo service1) {
        return new ReflectionToStringBuilder(service1, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("jobStatus", "templateId", "statusModifiedDate", "createdBulkDate", "serviceInstanceId", "serviceInstanceName", "isRetryEnabled")
                .toString();
    }

    protected Map<Keys, String> addBulkPendingWithCustomList(List<BasePreset> customPresets){
        Map<Keys, String> names = generateNames();
        final int bulkSize = 2 + customPresets.size();

        List<BasePreset> msoBulkPresets = generateMsoCreateBulkPresets(bulkSize, names);
        ImmutableList<BasePreset> presets = new ImmutableList.Builder<BasePreset>()
                .add(new PresetGetSessionSlotCheckIntervalGet())
                .add(new PresetAAIGetSubscribersGet())
                .add(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN3_TO_ATT_SABABA, PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC)
                .addAll(msoBulkPresets)
                .addAll(customPresets)
                .build();

        List<String> jobIds = createBulkOfMacroInstances(presets, false, bulkSize, names);
        Assert.assertEquals(jobIds.size(),bulkSize);

        return names;
    }

    protected void verifyAuditStatuses(String jobId, List<String> statuses, JobAuditStatus.SourceStatus source) {
        int statusesSize = statuses.size();
        AtomicReference<List<JobAuditStatus>> actualAudits = new AtomicReference<>();
        if (source.equals(JobAuditStatus.SourceStatus.VID)) {
            actualAudits.set(getAuditStatuses(jobId, JobAuditStatus.SourceStatus.VID.name()));
            org.junit.Assert.assertEquals("Received number of VID statuses is not as expected", statusesSize, actualAudits.get().size());
        } else {
            boolean isStatusedSizeAsExpected = Wait.waitFor(x-> {
                actualAudits.set(getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO.name()));
                return actualAudits.get().size() == statusesSize;
            },null,5,1);
            org.junit.Assert.assertTrue("Received number of MSO statuses is not as expected. Expected: " + statusesSize + ". Received: " + actualAudits.get().size(), isStatusedSizeAsExpected);
        }
        IntStream.range(0, statusesSize).forEach(i-> org.junit.Assert.assertEquals(source + " status #" + i + " is not as expected", statuses.get(i), actualAudits.get().get(i).getJobStatus()));
    }

    protected void verifyInstanceAuditStatuses(List<JobAuditStatus> expectedStatuses, List<JobAuditStatus> actualStatuses) {
        final int expectedSize = expectedStatuses.size();
        assertTrue("Expected statuses size is "+ expectedSize +", actual size is "+actualStatuses.size(), new Integer(expectedSize).equals(actualStatuses.size()));
        IntStream.range(0, expectedSize).forEach(i-> {

            final JobAuditStatus expectedStatus = expectedStatuses.get(i);
            final JobAuditStatus actualStatus = actualStatuses.get(i);
            org.junit.Assert.assertEquals("MSO status #" + i + " is not as expected", expectedStatus.getJobStatus(), actualStatus.getJobStatus());
            org.junit.Assert.assertEquals("MSO requestId #" + i + " is not as expected", expectedStatus.getRequestId(), actualStatus.getRequestId());
            org.junit.Assert.assertEquals("MSO additionalInfo #" + i + " is not as expected", expectedStatus.getAdditionalInfo(), actualStatus.getAdditionalInfo());
            org.junit.Assert.assertEquals("MSO jobID #" + i + " is not as expected", expectedStatus.getJobId(), actualStatus.getJobId());
            org.junit.Assert.assertEquals("MSO instanceName #" + i + " is not as expected", expectedStatus.getInstanceName(), actualStatus.getInstanceName());
            org.junit.Assert.assertEquals("MSO instanceType  #" + i + " is not as expected", expectedStatus.getInstanceType(), actualStatus.getInstanceType());
        });
    }
    protected static JobStatus getErrorStatus() {
            return Features.FLAG_2008_PAUSE_VFMODULE_INSTANTIATION_FAILURE.isActive() ?
                            JobStatus.FAILED_AND_PAUSED : JobStatus.COMPLETED_WITH_ERRORS;
   }
}
