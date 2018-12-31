package org.onap.vid.job.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.JobCommandsConfigWithMockedMso;
import org.onap.vid.config.MockedAaiClientAndFeatureManagerConfig;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobType;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.CommandUtils;
import org.onap.vid.job.command.InternalState;
import org.onap.vid.model.Action;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.RequestReferences;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.properties.Features;
import org.onap.vid.services.AsyncInstantiationBaseTest;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.utils.DaoUtils;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.togglz.core.manager.FeatureManager;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Every.everyItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.onap.vid.job.Job.JobStatus.*;
import static org.onap.vid.model.JobAuditStatus.SourceStatus.MSO;
import static org.onap.vid.model.JobAuditStatus.SourceStatus.VID;
import static org.testng.AssertJUnit.*;

//it's more like integration test than UT
//But it's very hard to test in API test so I use UT
@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, MockedAaiClientAndFeatureManagerConfig.class, JobCommandsConfigWithMockedMso.class})
public class AsyncInstantiationIntegrationTest extends AsyncInstantiationBaseTest {

    private static final String FAILED_STR = "FAILED";
    private static final String COMPLETE_STR = "COMPLETE";
    private static final String IN_PROGRESS_STR = "IN_PROGRESS";
    private static final String REQUESTED = "REQUESTED";
    private static final String PENDING_MANUAL_TASK = "PENDING_MANUAL_TASK";
    public static final String RAW_DATA_FROM_MSO = "RAW DATA FROM MSO";
    private static String USER_ID =  "123";
    public static String REQUEST_ID = UUID.randomUUID().toString();
    public static String SERVICE_INSTANCE_ID = UUID.randomUUID().toString();

    @Inject
    private JobsBrokerService jobsBrokerService;

    @Inject
    private JobWorker jobWorker;

    @Inject
    private FeatureManager featureManager;

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    private RestMsoImplementation restMso;

    @Inject
    private DataAccessService dataAccessService;

    @Inject
    private CommandUtils commandUtils;

    @BeforeClass
    void initServicesInfoService() {
        createInstanceParamsMaps();
    }

    @BeforeMethod
    void clearDb() {
        dataAccessService.deleteDomainObjects(ServiceInfo.class, "1=1", DaoUtils.getPropsMap());
        dataAccessService.deleteDomainObjects(JobDaoImpl.class, "1=1", DaoUtils.getPropsMap());
        dataAccessService.deleteDomainObjects(NameCounter.class, "1=1", DaoUtils.getPropsMap());
    }

    @BeforeMethod
    void defineMocks() {
        mockAaiClientAnyNameFree();
    }

    //@Test
    public void whenPushNewBulk_thenAllServicesAreInPending() {

        pushMacroBulk();
        List<ServiceInfo> serviceInfoList = asyncInstantiationBL.getAllServicesInfo();
        assertThat( serviceInfoList, everyItem(hasProperty("jobStatus", is(PENDING))));
    }

    private List<UUID> pushMacroBulk() {
        ServiceInstantiation serviceInstantiation = generateMockMacroServiceInstantiationPayload(false,
                createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true),
                3, true,PROJECT_NAME, true);
        return asyncInstantiationBL.pushBulkJob(serviceInstantiation, USER_ID);
    }

    private UUID pushALaCarteWithVnf() {
        ServiceInstantiation serviceInstantiation = generateALaCarteWithVnfsServiceInstantiationPayload();
        List<UUID> uuids = asyncInstantiationBL.pushBulkJob(serviceInstantiation, USER_ID);
        assertThat(uuids, hasSize(1));
        return uuids.get(0);
    }

    private UUID pushALaCarteUpdateWithGroups() {
        ServiceInstantiation serviceInstantiation = generateALaCarteUpdateWith1ExistingGroup2NewGroupsPayload();
        List<UUID> uuids = asyncInstantiationBL.pushBulkJob(serviceInstantiation, USER_ID);
        assertThat(uuids, hasSize(1));
        return uuids.get(0);
    }

    public static RestObject<RequestReferencesContainer> createResponse(int statusCode) {
        return createResponse(statusCode, SERVICE_INSTANCE_ID, REQUEST_ID);
    }

    public static RestObject<RequestReferencesContainer> createResponse(int statusCode, String instanceId, String requestId) {
        RequestReferences requestReferences = new RequestReferences();
        requestReferences.setRequestId(requestId);
        requestReferences.setInstanceId(instanceId);
        RestObject<RequestReferencesContainer> restObject = new RestObject<>();
        restObject.set(new RequestReferencesContainer(requestReferences));
        restObject.setStatusCode(statusCode);
        restObject.setRaw(RAW_DATA_FROM_MSO);
        return restObject;
    }

    ImmutableList<String> statusesToStrings(JobStatus... jobStatuses) {
        return Stream.of(jobStatuses).map(
                Enum::toString).collect(ImmutableList.toImmutableList());
    }

    /*
    Make sure service state is in progress once request has sent to MSO
    Make sure service state is in progress once request has sent to MSO and MSO status is in_progress
    Make sure service state is Failed once we got from MSO failure state, and that job's are not collected any more.
    Make sure service state is Completed successfully once we got from MSO complete, and that next job is peeked.
    Once a service in the bulk is failed, other services moved to Stopped, and no other jobs from the bulk are peeked.
    */
    //@Test
    public void testStatusesOfMacroServiceInBulkDuringBulkLifeCycle() {
        when(restMso.PostForObject(any(), any(), eq(RequestReferencesContainer.class))).thenReturn(createResponse(200));
        ImmutableList<ImmutableList<String>> expectedStatusesForVid = ImmutableList.of(
                statusesToStrings(PENDING, IN_PROGRESS, COMPLETED),
                statusesToStrings(PENDING, IN_PROGRESS, FAILED),
                statusesToStrings(PENDING, STOPPED)
        );

        ImmutableList<ImmutableList<String>> expectedStatusesForMso = ImmutableList.of(
                ImmutableList.of(REQUESTED, IN_PROGRESS_STR, "not a state", FAILED_STR ,COMPLETE_STR),
                ImmutableList.of(REQUESTED, FAILED_STR),
                ImmutableList.of()
        );

        List<UUID> uuids = pushMacroBulk();
        pullPendingJobAndAssertJobStatus(JobStatus.IN_PROGRESS, PENDING);

        //assert that when get ProcessingException from restMso, status remain the same
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).thenThrow(new ProcessingException("fake message"));
        Job job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS);
        UUID firstHandledJobUUID = job.getUuid();
        listServicesAndAssertStatus(JobStatus.IN_PROGRESS, PENDING, job);

        //assert that when get IN_PROGRESS status from restMso, status remain IN_PROGRESS
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR));
        job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS);
        listServicesAndAssertStatus(JobStatus.IN_PROGRESS, PENDING, job);

        //assert that when get unrecognized status from restMso, status remain IN_PROGRESS
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject("not a state"));
        job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS);
        listServicesAndAssertStatus(JobStatus.IN_PROGRESS, PENDING, job);

        //assert that when get non 200 status code during IN_PROGRESS, status remain IN_PROGRESS
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR, 404));
        job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS);
        listServicesAndAssertStatus(JobStatus.IN_PROGRESS, PENDING, job);

        //when get job COMPLETE from MSO, service status become COMPLETED
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        job = pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, COMPLETED);
        List<ServiceInfo> serviceInfoList = listServicesAndAssertStatus(COMPLETED, PENDING, job);
        
        
        //for use later in the test
        Map<UUID, JobStatus> expectedJobStatusMap = serviceInfoList.stream().collect(
                Collectors.toMap(ServiceInfo::getJobId, x-> PENDING));
        expectedJobStatusMap.put(job.getUuid(), COMPLETED);

        //when handling another PENDING job, statuses are : COMPLETED, IN_PROGRESS, PENDING
        job =  pullJobProcessAndPushBack(PENDING, JobStatus.IN_PROGRESS);
        assertThat(job.getUuid(), not(equalTo(firstHandledJobUUID))); //assert different job was handled now
        expectedJobStatusMap.put(job.getUuid(), JobStatus.IN_PROGRESS);
        listServicesAndAssertStatus(expectedJobStatusMap);

        //when get FAILED status from MSO statuses are : COMPLETED, FAILED, STOPPED
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(FAILED_STR));
        job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.FAILED);
        expectedJobStatusMap.put(job.getUuid(), JobStatus.FAILED);
        expectedJobStatusMap = expectedJobStatusMap.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey(), e -> e.getValue() == PENDING ? JobStatus.STOPPED : e.getValue()
        ));

        listServicesAndAssertStatus(expectedJobStatusMap);
        IntStream.range(0, uuids.size()).forEach(i -> {
            UUID uuid = uuids.get(i);
            List<String> msoStatuses = asyncInstantiationBL.getAuditStatuses(uuid, MSO).stream().map(x -> x.getJobStatus()).collect(Collectors.toList());
            List<String> vidStatuses = asyncInstantiationBL.getAuditStatuses(uuid, VID).stream().map(x -> x.getJobStatus()).collect(Collectors.toList());
            assertThat(msoStatuses, is(expectedStatusesForMso.get(i)));
            assertThat(vidStatuses, is(expectedStatusesForVid.get(i)));
        });
        //
        assertFalse(jobsBrokerService.pull(PENDING, randomUuid()).isPresent());
        assertFalse(jobsBrokerService.pull(JobStatus.IN_PROGRESS, randomUuid()).isPresent());
    }


    @DataProvider
    public static Object[][] AlaCarteStatuses(Method test) {
        return new Object[][]{
                {COMPLETE_STR, JobStatus.COMPLETED, JobStatus.COMPLETED},
                {FAILED_STR, JobStatus.COMPLETED_WITH_ERRORS, JobStatus.FAILED},
        };
    }

    /*
    Make sure service state is in progress once request has sent to MSO
    Make sure service state is watching until state changes to complemented
    Make sure service state is watching until vnf state changes to completed
    Make sure service state is Completed successfully once we got from MSO complete for the vnf job.
    status Creating
     */
    //@Test(dataProvider = "AlaCarteStatuses")
    public void testStatusesOfServiceDuringALaCarteLifeCycleIgnoringVfModules(String msoVnfStatus, JobStatus expectedServiceStatus,  JobStatus expectedVnfStatus) {
        /*
            [v]  + push alacarte with 1 vnf
            [v]    verify STATUS pending
            [v]  + pull+execute  (should post to MSO)
            [v]    verify STATUS in progress
            [v]  + pull+execute  (should GET completed from MSO)
            [v]    verify STATUS in progress; TYPE watching
            [v]    verify job#2 *new* VNF job STATUS creating
            [v]  + pull+execute job#2 (should post to MSO)
            [v]    verify job#2 STATUS resource in progress
            [v]    verify job#1 STATUS in progress
            [v]  + pull+execute job#2 (should GET completed from MSO)
            [v]    verify job#2 STATUS completed
            [v]  + pull+execute job#1
            [v]    verify job#1 STATUS completed

           * not looking on audit (yet)
        */
        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VNF)).thenReturn(true);
        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VFMODULE)).thenReturn(false);
        final String SERVICE_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE_INSTANCE_ID = UUID.randomUUID().toString();
        final String VNF_REQUEST_ID = UUID.randomUUID().toString();


        //push alacarte with 1 vnf, verify STATUS pending
        UUID uuid = pushALaCarteWithVnf();
        singleServicesAndAssertStatus(JobStatus.PENDING, uuid);

        //mock mso to answer 200 of create service instance request, verify STATUS in progress
        when(restMso.PostForObject(any(), endsWith("serviceInstances"), eq(RequestReferencesContainer.class))).thenReturn(
                createResponse(200, SERVICE_INSTANCE_ID, SERVICE_REQUEST_ID));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.PENDING, JobStatus.IN_PROGRESS, JobType.InProgressStatus);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);

        //mock mso to answer COMPLETE for service instance create, job status shall remain IN_PROGRESS and type shall be Watching
        reset(restMso);
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS, JobType.Watching);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);

        //mock mso to answer 200 of create vnf instance request, pull+execute vnf job, STATUS resource in progress
        reset(restMso);
        when(restMso.PostForObject(any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs"), eq(RequestReferencesContainer.class))).thenReturn(
                createResponse(200, UUID.randomUUID().toString(), VNF_REQUEST_ID));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.CREATING, JobStatus.RESOURCE_IN_PROGRESS, JobType.VnfInProgressStatus);

        //verify service job  STATUS in progress
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS, JobType.Watching);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);

        //mock mso to answer msoVnfStatus (COMPLETE/FAILED) for vnf creation status,
        //job status shall be final (COMPLETE/COMPLETE_WITH_ERRORS)
        reset(restMso);
        when(restMso.GetForObject(endsWith(VNF_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(
                asyncRequestStatusResponseAsRestObject(msoVnfStatus));
        pullJobProcessAndPushBack(JobStatus.RESOURCE_IN_PROGRESS, expectedVnfStatus, false);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);
        pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, expectedServiceStatus, true);
        singleServicesAndAssertStatus(expectedServiceStatus, uuid);

    }

    /*
    this test is almost duplication of testStatusesOfServiceDuringALaCarteLifeCycleIgnoringVfModules.

    IgnoringVfModules test check the scenario while FLAG_ASYNC_ALACARTE_VFMODULE is off
    WithVfModules     test check the scenario while FLAG_ASYNC_ALACARTE_VFMODULE is on

    We shall consider later to remove testStatusesOfServiceDuringALaCarteLifeCycleIgnoringVfModules
    And union these tests to single one.
     */

    //@Test
    public void testALaCarteLifeCycle1Vnf2VfModules() {


        String msoVnfStatus = COMPLETE_STR;
        JobStatus expectedServiceStatus = IN_PROGRESS;
        JobStatus expectedVnfStatus = RESOURCE_IN_PROGRESS;
        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VNF)).thenReturn(true);
        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VFMODULE)).thenReturn(true);
        final String SERVICE_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE_INSTANCE_ID = UUID.randomUUID().toString();
        final String VNF_REQUEST_ID = UUID.randomUUID().toString();
        final String VNF_INSTANCE_ID = UUID.randomUUID().toString();
        final String VG_REQUEST_ID = UUID.randomUUID().toString();
        final String VG_INSTANCE_ID = UUID.randomUUID().toString();
        final String VF_MODULE_REQUEST_ID = UUID.randomUUID().toString();
        final String VF_MODULE_REQUEST_ID2 = UUID.randomUUID().toString();


        //push alacarte with 1 vnf, verify STATUS pending
        UUID uuid = pushALaCarteWithVnf();
        singleServicesAndAssertStatus(JobStatus.PENDING, uuid);

        /*---------- service -----------*/

        //mock mso to answer 200 of create service instance request, verify STATUS in progress
        when(restMso.PostForObject(any(), endsWith("serviceInstances"), eq(RequestReferencesContainer.class))).thenReturn(
                createResponse(200, SERVICE_INSTANCE_ID, SERVICE_REQUEST_ID));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.PENDING, JobStatus.IN_PROGRESS, JobType.InProgressStatus);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);

        //mock mso to answer COMPLETE for service instance create, job status shall remain IN_PROGRESS and type shall be Watching
        reset(restMso);
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS, JobType.Watching);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);

        /*---------- vnf -----------*/

        //mock mso to answer 200 of create vnf instance request, pull+execute vnf job, STATUS resource in progress
        reset(restMso);
        when(restMso.PostForObject(any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs"), eq(RequestReferencesContainer.class))).thenReturn(
                createResponse(200, VNF_INSTANCE_ID, VNF_REQUEST_ID));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.CREATING, JobStatus.RESOURCE_IN_PROGRESS, JobType.VnfInProgressStatus);

        //verify service job  STATUS in progress
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS, JobType.Watching);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);

        //mock mso to answer msoVnfStatus (COMPLETE/FAILED) for vnf creation status,
        //job status shall be final (COMPLETE/COMPLETE_WITH_ERRORS)
        reset(restMso);
        when(restMso.GetForObject(endsWith(VNF_REQUEST_ID), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        try {
            reset(commandUtils);
            when(commandUtils.isVfModuleBaseModule(SERVICE_MODEL_VERSION_ID, VF_MODULE_0_MODEL_VERSION_ID)).thenReturn(true);
            when(commandUtils.isVfModuleBaseModule(SERVICE_MODEL_VERSION_ID, VF_MODULE_1_MODEL_VERSION_ID)).thenReturn(false);
        } catch (AsdcCatalogException e) {

        }

        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.RESOURCE_IN_PROGRESS, JobStatus.RESOURCE_IN_PROGRESS, JobType.WatchingBaseModule);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);
        pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS, true);
        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);

        /*---------- vf Module without volume group name (base) -----------*/

        //vg name not exist, so vf module created immediately
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.CREATING, JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching);

        //verify vnf/volumeGroup job  STATUS still watching with resource in progress
        pullMultipleJobsFindExpectedProcessAndPushBack(JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching, JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching);

        //mock mso to answer 200 of create vfModule instance request, pull+execute volumeGroup job, STATUS resource in progress
        reset(restMso);
        when(restMso.PostForObject(any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs/" + VNF_INSTANCE_ID + "/vfModules"), eq(RequestReferencesContainer.class))).thenReturn(
                createResponse(200, UUID.randomUUID().toString(), VF_MODULE_REQUEST_ID));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.CREATING, JobStatus.RESOURCE_IN_PROGRESS, JobType.ResourceInProgressStatus);

        //mock mso to answer for vf module orchestration request
        reset(restMso);
        when(restMso.GetForObject(endsWith(VF_MODULE_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(
                asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        pullMultipleJobsFindExpectedProcessAndPushBack(JobStatus.RESOURCE_IN_PROGRESS, JobType.ResourceInProgressStatus, JobStatus.COMPLETED, JobType.ResourceInProgressStatus);

        //verify volume group become completed
        pullMultipleJobsFindExpectedProcessAndPushBack(JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching, JobStatus.COMPLETED, JobType.Watching);

        //vnf become watching after volume group completed, and new volume group created
        pullMultipleJobsFindExpectedProcessAndPushBack(JobStatus.RESOURCE_IN_PROGRESS, JobType.WatchingBaseModule, JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching);

        /*---------- volume group & vf module (non base) -----------*/

        /*---------- volume group -----------*/

        //mock mso to answer 200 of create volumeGroup instance request, pull+execute volumeGroup job, STATUS resource in progress
        reset(restMso);
        when(restMso.PostForObject(any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs/" + VNF_INSTANCE_ID + "/volumeGroups"), eq(RequestReferencesContainer.class))).thenReturn(
                createResponse(200, VG_INSTANCE_ID, VG_REQUEST_ID));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.CREATING, JobStatus.RESOURCE_IN_PROGRESS, JobType.VolumeGroupInProgressStatus);

        //verify vnf job  STATUS still watching with resource in progress
        pullMultipleJobsFindExpectedProcessAndPushBack(JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching, JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching);

        //mock mso to answer for volume group orchestration request
        reset(restMso);
        when(restMso.GetForObject(endsWith(VG_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(
                asyncRequestStatusResponseAsRestObject(msoVnfStatus));
        pullMultipleJobsFindExpectedProcessAndPushBack(JobStatus.RESOURCE_IN_PROGRESS, JobType.VolumeGroupInProgressStatus, JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching);

        /*---------- vfModule -----------*/

        //mock mso to answer 200 of create vfModule instance request, pull+execute volumeGroup job, STATUS resource in progress
        reset(restMso);
        when(restMso.PostForObject(any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs/" + VNF_INSTANCE_ID + "/vfModules"), eq(RequestReferencesContainer.class))).thenReturn(
                createResponse(200, UUID.randomUUID().toString(), VF_MODULE_REQUEST_ID2));
        pullJobProcessAndPushBackWithTypeAssertion(JobStatus.CREATING, JobStatus.RESOURCE_IN_PROGRESS, JobType.ResourceInProgressStatus);

        //mock mso to answer for vf module orchestration request
        reset(restMso);
        when(restMso.GetForObject(endsWith(VF_MODULE_REQUEST_ID2), eq(AsyncRequestStatus.class))).thenReturn(
                asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        pullMultipleJobsFindExpectedProcessAndPushBack(JobStatus.RESOURCE_IN_PROGRESS, JobType.ResourceInProgressStatus, JobStatus.COMPLETED, JobType.ResourceInProgressStatus);

        //execute twice - 1 for parent volume group, 1 for parent vnf
        pullAllJobProcessAndPushBackByType(JobStatus.RESOURCE_IN_PROGRESS, JobType.Watching , JobStatus.COMPLETED);

        singleServicesAndAssertStatus(JobStatus.IN_PROGRESS, uuid);
        pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.COMPLETED, true);
        singleServicesAndAssertStatus(JobStatus.COMPLETED, uuid);
    }

    //@Test
    public void testBadAaiResponseForSearchNamesAndBackToNormal() {
        when(aaiClient.isNodeTypeExistsByName(any(), any())).thenThrow(aaiNodeQueryBadResponseException());
        pushMacroBulk();        //JOB shall become IN_PROGRESS but service info is still pending
        Job job = pullJobProcessAndPushBack(PENDING, JobStatus.IN_PROGRESS, true);
        listServicesAndAssertStatus(PENDING, PENDING, job);

        //JOB shall remain in IN_PROGRESS
        job = pullJobProcessAndPushBack( JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS, true);
        //make sure the job command is still ServiceInstantiation
        assertThat(job.getType(), is(JobType.MacroServiceInstantiation));
        listServicesAndAssertStatus(PENDING, PENDING, job);

        //simulate AAI back to normal, AAI return name is free, and MSO return good response
        Mockito.reset(aaiClient); // must forget the "thenThrow"
        when(aaiClient.isNodeTypeExistsByName(any(), any())).thenReturn(false);
        when(restMso.PostForObject(any(),any(), eq(RequestReferencesContainer.class))).thenReturn(createResponse(200));
        job = pullJobProcessAndPushBack( JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS, true);
        listServicesAndAssertStatus(JobStatus.IN_PROGRESS, PENDING, job);

        //when get job COMPLETE from MSO, service status become COMPLETED
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        job = pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, COMPLETED);
        listServicesAndAssertStatus(COMPLETED, PENDING, job);
    }

    //@Test
    public void testAaiResponseNameUsedTillMaxRetries() {
        when(aaiClient.isNodeTypeExistsByName(any(), any())).thenReturn(true);
        asyncInstantiationBL.setMaxRetriesGettingFreeNameFromAai(10);
        pushMacroBulk();
        //JOB shall become IN_PROGRESS but service info is still pending
        Job job = pullJobProcessAndPushBack(PENDING, JobStatus.FAILED, true);
        listServicesAndAssertStatus(JobStatus.FAILED, JobStatus.STOPPED, job);
    }

    private Job pullJobProcessAndPushBack(JobStatus topic, JobStatus expectedNextJobStatus) {
        return pullJobProcessAndPushBack(topic, expectedNextJobStatus, true);
    }

    //return the pulled job (and not the pushed job)
    private Job pullJobProcessAndPushBack(JobStatus topic, JobStatus expectedNextJobStatus, boolean pullingAssertion) {
        Optional<Job> job = pullJob(topic, pullingAssertion);

        Job nextJob = jobWorker.executeJobAndGetNext(job.get());

        try {
            assertThat("next job not ok: " + nextJob.getData(), nextJob.getStatus(), is(expectedNextJobStatus));

            if (pullingAssertion) {
                //assert another pulling on same topic return no result (before push back)
                assertFalse(jobsBrokerService.pull(topic, randomUuid()).isPresent());
            }

        } finally {
            jobsBrokerService.pushBack(nextJob); // push back to let retries - even if any assertion failure
        }
        assertThat(jobsBrokerService.peek(job.get().getUuid()).getStatus(), is(expectedNextJobStatus));
        return job.get();
    }

    private Job pullJobProcessAndPushBackWithTypeAssertion(JobStatus topic, JobStatus expectedNextJobStatus,
                                                           JobType expectedNextJobType) {
        Job job = pullJobProcessAndPushBack(topic, expectedNextJobStatus, false);
        assertThat("job not ok: " + job.getData(), job.getType(), is(expectedNextJobType));
        return job;
    }

    private Job pullJobProcessAndPushBackWithTypeAssertion(JobStatus topic, JobStatus expectedNextJobStatus,
                                                           JobType expectedNextJobType, int retries) {
        return retryWithAssertionsLimit(retries, () -> {
            return pullJobProcessAndPushBackWithTypeAssertion(topic, expectedNextJobStatus, expectedNextJobType);
        });
    }

    private Job pullJobProcessAndPushBackWithTypeAssertion(JobStatus topic, JobStatus expectedNextJobStatus,
                                                           JobType expectedNextJobType, Action actionPhase, InternalState internalState, int retries) {
        return retryWithAssertionsLimit(retries, () -> {
            Job job = pullJobProcessAndPushBackWithTypeAssertion(topic, expectedNextJobStatus, expectedNextJobType);
            assertThat("job not ok: " + job.getData(), job.getData(), is(jsonPartEquals("actionPhase", actionPhase.name())));
            if (internalState != null) {
                assertThat("job not ok: " + job.getData(), job.getData(), is(jsonPartEquals("internalState", internalState.name())));
            }
            return job;
        });
    }

    private Job retryWithAssertionsLimit(int retries, Supplier<Job> supplier) {
        java.util.Stack<AssertionError> history = new Stack<>();

        do {
            try {
                return supplier.get();
            } catch (AssertionError assertionError) {
                history.push(assertionError);
            }
        } while (history.size() < retries);

        // No success:
        throw new AssertionError("No luck while all of these assertion errors: " + history.stream()
                .map(Throwable::getMessage)
                .map(s -> s.replace('\n', ' '))
                .map(s -> s.replaceAll("\\s{2,}"," "))
                .distinct()
                .collect(joining("\n   ", "\n   ", "")), history.peek());
    }

    private Job pullMultipleJobsFindExpectedProcessAndPushBack(JobStatus topic, JobType expectedCurrentJobType, JobStatus expectedNextJobStatus,
                                                               JobType expectedNextJobType) {
        List<Job> pulledJobs = new ArrayList<>();
        Job lastJob = null;
        while (lastJob == null || lastJob.getType() != expectedCurrentJobType) {
            lastJob = pullJob(topic, false).get();
            if (lastJob.getType() != expectedCurrentJobType) {
                pulledJobs.add(lastJob);
            }
        }

        Job nextJob = jobWorker.executeJobAndGetNext(lastJob);
        assertThat(nextJob.getStatus(), is(expectedNextJobStatus));
        assertThat(nextJob.getType(), is(expectedNextJobType));

        jobsBrokerService.pushBack(nextJob);
        assertThat(jobsBrokerService.peek(nextJob.getUuid()).getStatus(), is(expectedNextJobStatus));

        pulledJobs.forEach(job ->
                jobsBrokerService.pushBack(job)
        );

        return nextJob;
    }

    private void pullAllJobProcessAndPushBackByType(JobStatus topic, JobType commandType, JobStatus expectedFinalStatus) {
        Map<UUID, JobStatus> jobStatusMap = new HashMap<>();
        Optional<Job> job = pullJob(topic, false);
        for (int i=0; i<1000 && job.isPresent() && job.get().getType() == commandType; i++) {
            Job nextJob = jobWorker.executeJobAndGetNext(job.get());
            jobStatusMap.put(nextJob.getUuid(), nextJob.getStatus());
            jobsBrokerService.pushBack(nextJob);
            job = jobsBrokerService.pull(topic, UUID.randomUUID().toString());
        }
        assertThat(jobStatusMap.values(), everyItem(is(expectedFinalStatus)));

    }

    private Optional<Job> pullJob(JobStatus topic, boolean pullingAssertion) {
        if (pullingAssertion) {
            //assert pulling on inverse topic return no result
            assertFalse(jobsBrokerService.pull(inverseTopic(topic), randomUuid()).isPresent());
        }

        Optional<Job> job =  jobsBrokerService.pull(topic, randomUuid());
        assertTrue("no job fetched", job.isPresent());

        if (pullingAssertion) {
            //assert another pulling on same topic return no result
            assertFalse(jobsBrokerService.pull(topic, randomUuid()).isPresent());
        }

        return job;
    }

    private JobStatus inverseTopic(JobStatus topic) {
        return topic==JobStatus.IN_PROGRESS ? PENDING : JobStatus.IN_PROGRESS;
    }


    //@Test
    public void whenPushNewBulk_andGetNoResponseFromMsoOnCreation_thenServiceMoveToFailedAndOtherToStopped() {
        when(restMso.PostForObject(any(), any(), eq(RequestReferencesContainer.class))).thenReturn(createResponse(500));
        pushBulkPullPendingJobAndAssertJobStatus(JobStatus.FAILED, JobStatus.STOPPED);
    }

    //@Test
    public void whenMsoStatusIsPendingManualTask_ThenJobStatusIsPaused() {
        when(restMso.PostForObject(any(), any(), eq(RequestReferencesContainer.class))).thenReturn(createResponse(200));

        Job firstJob = pushBulkPullPendingJobAndAssertJobStatus(JobStatus.IN_PROGRESS, PENDING);

        //assert that when get ProcessingException from restMso, status remain the same
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(PENDING_MANUAL_TASK));
        Job job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS);
        listServicesAndAssertStatus(PAUSE, PENDING, job);

        //The paused job is pulled and remain in pause state. Other jobs from bulk remain pending
        job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS);
        listServicesAndAssertStatus(PAUSE, PENDING, job);

        //the job get IN_PROGRESS response (simulate activate operation) and status changed to IN_PROGRESS
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR));
        job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS);
        listServicesAndAssertStatus(JobStatus.IN_PROGRESS, PENDING, job);

        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        job =  pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, COMPLETED);
        listServicesAndAssertStatus(COMPLETED, PENDING, job);

        //Pulling PENDING job return another job
        assertThat(jobsBrokerService.pull(PENDING, randomUuid()).get().getUuid(), not(equalTo(job.getUuid())));


        ImmutableList<String> expectedStatusesForMso = ImmutableList.of(REQUESTED, PENDING_MANUAL_TASK, IN_PROGRESS_STR, COMPLETE_STR);
        List<String> msoStatuses = asyncInstantiationBL.getAuditStatuses(firstJob.getUuid(), MSO).stream().map(x -> x.getJobStatus()).collect(Collectors.toList());
        assertThat(msoStatuses, is(expectedStatusesForMso));

        ImmutableList<String> expectedStatusesForVid = statusesToStrings(PENDING, IN_PROGRESS, PAUSE, IN_PROGRESS, COMPLETED);
        List<String> vidStatuses = asyncInstantiationBL.getAuditStatuses(firstJob.getUuid(), VID).stream().map(x -> x.getJobStatus()).collect(Collectors.toList());
        assertThat(vidStatuses, is(expectedStatusesForVid));
    }

    private Job pushBulkPullPendingJobAndAssertJobStatus(JobStatus pulledJobStatus, JobStatus otherJobsStatus) {
        pushMacroBulk();
        return pullPendingJobAndAssertJobStatus(pulledJobStatus, otherJobsStatus);
    }

    private Job pullPendingJobAndAssertJobStatus(JobStatus pulledJobStatus, JobStatus otherJobsStatus) {
        Job job = pullJobProcessAndPushBack(PENDING, pulledJobStatus, false);
        listServicesAndAssertStatus(pulledJobStatus, otherJobsStatus, job);
        return job;
    }

    //@Test
    public void test2BulksLifeCyclesAreIndependent() {
        pushMacroBulk();
        when(restMso.PostForObject(any(), any(), eq(RequestReferencesContainer.class))).thenReturn(createResponse(200));
        //push 2nd job, then when pulling first job the job become in_progress, other jobs (from 2 bulks) remain pending
        Job firstJob = pushBulkPullPendingJobAndAssertJobStatus(JobStatus.IN_PROGRESS, PENDING);

        //assert we can pull another job from pending from other template id
        Job secondJob = pullJobProcessAndPushBack(PENDING, JobStatus.IN_PROGRESS, false);
        assertThat(firstJob.getTemplateId(), not(equalTo(secondJob.getTemplateId())));

        //assert no more PENDING jobs to pull
        assertFalse(jobsBrokerService.pull(PENDING, randomUuid()).isPresent());

        //when get FAILED status from MSO statuses for failed bulk are: FAILED, STOPPED, for other bulk: IN_PROGRESS, 2 pending
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(FAILED_STR));
        Job failedJob = pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.FAILED, false);
        Map<UUID, List<ServiceInfo>> servicesByTemplateId =
                asyncInstantiationBL.getAllServicesInfo()
                        .stream().collect(groupingBy(ServiceInfo::getTemplateId));
        assertServicesStatus(servicesByTemplateId.get(failedJob.getTemplateId()), JobStatus.FAILED, JobStatus.STOPPED, failedJob);
        Job successJob = failedJob.getUuid().equals(firstJob.getUuid()) ? secondJob : firstJob;
        assertServicesStatus(servicesByTemplateId.get(successJob.getTemplateId()), JobStatus.IN_PROGRESS, PENDING, successJob);

        //yet no more PENDING jobs to pull
        assertFalse(jobsBrokerService.pull(PENDING, randomUuid()).isPresent());

        //assert that job from non failed bulk can progress.
        //When completed,  failed bulk statuses: FAILED, STOPPED. Succeeded bulk statuses are : COMPLETED, 2 pending
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, COMPLETED, false);
        servicesByTemplateId =
                asyncInstantiationBL.getAllServicesInfo()
                        .stream().collect(groupingBy(ServiceInfo::getTemplateId));
        assertServicesStatus(servicesByTemplateId.get(failedJob.getTemplateId()), JobStatus.FAILED, JobStatus.STOPPED, failedJob);
        assertServicesStatus(servicesByTemplateId.get(successJob.getTemplateId()), COMPLETED, PENDING, successJob);

        //advance other jobs of succeeded bulk till al of them reach to COMPLETED
        pullJobProcessAndPushBack(PENDING, JobStatus.IN_PROGRESS, false);
        pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, COMPLETED, false);
        pullJobProcessAndPushBack(PENDING, JobStatus.IN_PROGRESS, false);
        pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, COMPLETED, false);
        servicesByTemplateId =
                asyncInstantiationBL.getAllServicesInfo()
                        .stream().collect(groupingBy(ServiceInfo::getTemplateId));
        assertServicesStatus(servicesByTemplateId.get(failedJob.getTemplateId()), JobStatus.FAILED, JobStatus.STOPPED, failedJob);
        assertServicesStatus(servicesByTemplateId.get(successJob.getTemplateId()), COMPLETED, COMPLETED, successJob);

        //assert no more PENDING jobs nor IN_PROGRESS jobs to pull
        assertFalse(jobsBrokerService.pull(PENDING, randomUuid()).isPresent());
        assertFalse(jobsBrokerService.pull(JobStatus.IN_PROGRESS, randomUuid()).isPresent());
    }

    public void deploy2NewGroupsToServiceWith1ExistingGroup() {

        /*
        new feature: skip service (existing impl) and skip group (new impl)
        service+group aren't touched, 2 groups ARE created

        [v]  success if all GROUPs success

        Next test variation should:
        [ ]  error if all GROUPs error
        [ ]  completed with error if 1 GROUP error


        [v]  + service with 3 groups - 1 action=none, 2 action=create; service's action=none
        [v]    verify STATUS pending
        [v]  + pull+execute  (should NOT post to MSO)
        [v]    verify STATUS in progress; TYPE watching
               ...
        [v]    verify job#2 *new* GROUP job STATUS completed with no action TYPE group INTERNAL STATE terminal PHASE delete
        [v]    verify job#3 *new* GROUP job STATUS completed with no action TYPE group INTERNAL STATE terminal PHASE delete
        [v]    verify job#4 *new* GROUP job STATUS completed with no action TYPE group INTERNAL STATE terminal PHASE delete

        [v]  + pull+execute job#1 (should NOT post to MSO)
        [v]    verify STATUS in progress; TYPE watching
        [v]    verify job#5 *new* GROUP job STATUS creating TYPE group INTERNAL STATE initial PHASE create
        [v]    verify job#6 *new* GROUP job STATUS creating TYPE group INTERNAL STATE initial PHASE create
        [v]    verify job#7 *new* GROUP job STATUS creating TYPE group INTERNAL STATE initial PHASE create

        [v]  + pull+execute job#5 (should NOT post to MSO)
        [v]    verify job#5 STATUS completed with no action TYPE group INTERNAL STATE terminal PHASE create
        [v]  + pull+execute job#1
        [v]    verify job#1 STATUS in progress; TYPE watching

        [v]  + pull+execute job#6 (should post to MSO)
        [v]    verify job#6 STATUS resource in progress
        [v]  + pull+execute job#1
        [v]    verify job#1 STATUS in progress; TYPE watching
        [v]  + pull+execute job#6 (should get from MSO)
        [v]    verify job#6 STATUS completed
        [v]  + pull+execute job#1
        [v]    verify job#1 STATUS in progress; TYPE watching

        [v]  + pull+execute job#7 (should post to MSO)
        [v]    verify job#7 STATUS resource in progress
        [v]  + pull+execute job#1
        [v]    verify job#1 STATUS in progress; TYPE watching
        [v]  + pull+execute job#7 (should get from MSO)
        [v]    verify job#7 STATUS completed
        [v]  + pull+execute job#1
        [v]    verify job#1 STATUS completed

         */

        final String GROUP1_REQUEST_ID = UUID.randomUUID().toString();
        final String GROUP1_INSTANCE_ID = UUID.randomUUID().toString();
        final String GROUP2_REQUEST_ID = UUID.randomUUID().toString();
        final String GROUP2_INSTANCE_ID = UUID.randomUUID().toString();

        // Utility method
        final BiConsumer<Action, JobStatus> verify_Job1InProgress = (phase, nextJobStatus) -> {
            pullJobProcessAndPushBackWithTypeAssertion(IN_PROGRESS, nextJobStatus, JobType.ALaCarteService, phase, InternalState.WATCHING, 2);
        };

        //service with 3 groups - 1 action=none, 2 action=create; service's action=none
        UUID uuid = pushALaCarteUpdateWithGroups();
        singleServicesAndAssertStatus(PENDING, uuid);

        // take from pending, put in-progress -> 3 delete-child were born
        pullJobProcessAndPushBackWithTypeAssertion(PENDING, IN_PROGRESS, JobType.ALaCarteService, Action.Delete, InternalState.WATCHING, 1);
        verifyQueueSizes(ImmutableMap.of(
                IN_PROGRESS, 1, CREATING, 3
        ));

        Stream.of(1, 2, 3).forEach(i -> {
            // take each child creating, put in-progress
            verify_Job1InProgress.accept(Action.Delete, IN_PROGRESS);
            pullJobProcessAndPushBackWithTypeAssertion(CREATING, RESOURCE_IN_PROGRESS, JobType.InstanceGroup, Action.Delete, null, 1);

            // execute each in-progress -> job is completed
            verify_Job1InProgress.accept(Action.Delete, IN_PROGRESS);
            pullJobProcessAndPushBackWithTypeAssertion(RESOURCE_IN_PROGRESS, COMPLETED/*_WITH_NO_ACTION*/, JobType.InstanceGroup,1);
        });
        verifyQueueSizes(ImmutableMap.of(
                IN_PROGRESS, 1, COMPLETED, 3
        ));

        // take job #1 from phase delete to phase create -> 3 create-child were born
        verify_Job1InProgress.accept(Action.Create, IN_PROGRESS);
        verifyQueueSizes(ImmutableMap.of(
                IN_PROGRESS, 1, CREATING, 3, COMPLETED, 3
        ));

        // prepare MSO mock
        when(restMso.PostForObject(any(), endsWith("instanceGroups"), eq(RequestReferencesContainer.class)))
                .thenReturn(createResponse(200, GROUP1_INSTANCE_ID, GROUP1_REQUEST_ID))
                .thenReturn(createResponse(200, GROUP2_INSTANCE_ID, GROUP2_REQUEST_ID))
                .thenReturn(null);
        when(restMso.GetForObject(argThat(uri -> StringUtils.endsWithAny(uri, GROUP1_REQUEST_ID, GROUP2_REQUEST_ID)), eq(AsyncRequestStatus.class))).
                thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        // take first "none" child from creating to completed
        // note there's no concrete mechanism that makes the first child be
        // the "action=None" case, but that's what happens, and following line
        // relies on that fact.
        pullJobProcessAndPushBackWithTypeAssertion(CREATING, COMPLETED_WITH_NO_ACTION, JobType.InstanceGroupInstantiation, 1);

        // take each of next two children from creating to in-progress, then to completed
        // verify job #1 is watching, and MSO is getting requests
        Stream.of(1, 2).forEach(i -> {
            verify_Job1InProgress.accept(Action.Create, IN_PROGRESS);
            pullJobProcessAndPushBackWithTypeAssertion(CREATING, RESOURCE_IN_PROGRESS, JobType.ResourceInProgressStatus);
            verify(restMso, times(i)).PostForObject(any(), any(), any());

            verify_Job1InProgress.accept(Action.Create, IN_PROGRESS);
            pullJobProcessAndPushBackWithTypeAssertion(RESOURCE_IN_PROGRESS, COMPLETED, JobType.ResourceInProgressStatus);
            verify(restMso, times(i)).GetForObject(any(), any());
        });

        // job #1 is done as all children are done
        verify_Job1InProgress.accept(Action.Create, COMPLETED);
        verifyQueueSizes(ImmutableMap.of(COMPLETED, 7));
    }

    private void verifyQueueSizes(ImmutableMap<JobStatus, Integer> expected) {
        final Collection<Job> peek = jobsBrokerService.peek();
        final Map<JobStatus, Long> jobTypes = peek.stream().collect(groupingBy(Job::getStatus, counting()));
        assertThat(jobTypes, is(expected));
    }

    private List<ServiceInfo> listServicesAndAssertStatus(JobStatus pulledJobStatus, JobStatus otherJobsStatus, Job job) {
        List<ServiceInfo> serviceInfoList = asyncInstantiationBL.getAllServicesInfo();
        assertServicesStatus(serviceInfoList, pulledJobStatus, otherJobsStatus, job);

        return serviceInfoList;
    }

    private ServiceInfo singleServicesAndAssertStatus(JobStatus expectedStatus, UUID jobUUID) {
        List<ServiceInfo> serviceInfoList = asyncInstantiationBL.getAllServicesInfo();
        assertEquals(1, serviceInfoList.size());
        ServiceInfo serviceInfo = serviceInfoList.get(0);
        assertThat(serviceInfo.getJobStatus(), is(expectedStatus));
        assertThat(serviceInfo.getJobId(), is(jobUUID));
        return serviceInfo;
    }

    private void assertServicesStatus(List<ServiceInfo> serviceInfoList, JobStatus pulledJobStatus, JobStatus otherJobsStatus, Job job) {
        serviceInfoList.forEach(si->{
            if (si.getJobId().equals(job.getUuid())) {
                assertThat(si.getJobStatus(), is(pulledJobStatus));
            }
            else {
                assertThat(si.getJobStatus(), is(otherJobsStatus));
            }
        });
    }

    private void listServicesAndAssertStatus(Map<UUID, JobStatus> expectedJobStatusMap) {
        Map<UUID, JobStatus> actualStatuses = asyncInstantiationBL.getAllServicesInfo()
                .stream().collect(Collectors.toMap(ServiceInfo::getJobId, ServiceInfo::getJobStatus));
        assertThat(actualStatuses.entrySet(), equalTo(expectedJobStatusMap.entrySet()));
    }

    private String randomUuid() {
        return UUID.randomUUID().toString();
    }
}
