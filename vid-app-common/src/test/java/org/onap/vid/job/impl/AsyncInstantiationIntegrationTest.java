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

package org.onap.vid.job.impl;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonNodePresent;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartMatches;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Every.everyItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.onap.vid.job.Job.JobStatus.COMPLETED;
import static org.onap.vid.job.Job.JobStatus.COMPLETED_WITH_ERRORS;
import static org.onap.vid.job.Job.JobStatus.COMPLETED_WITH_NO_ACTION;
import static org.onap.vid.job.Job.JobStatus.FAILED;
import static org.onap.vid.job.Job.JobStatus.IN_PROGRESS;
import static org.onap.vid.job.Job.JobStatus.PAUSE;
import static org.onap.vid.job.Job.JobStatus.PENDING;
import static org.onap.vid.job.Job.JobStatus.PENDING_RESOURCE;
import static org.onap.vid.job.Job.JobStatus.RESOURCE_IN_PROGRESS;
import static org.onap.vid.job.Job.JobStatus.STOPPED;
import static org.onap.vid.job.impl.JobSchedulerInitializer.WORKERS_TOPICS;
import static org.onap.vid.model.JobAuditStatus.SourceStatus.VID;
import static org.onap.vid.testUtils.TestUtils.readJsonResourceFileAsObject;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
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
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.model.serviceInstantiation.InstanceGroup;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.RequestReferences;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.AsyncRequestStatusList;
import org.onap.vid.properties.Features;
import org.onap.vid.services.AsyncInstantiationBaseTest;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.onap.vid.services.InstantiationTemplatesService;
import org.onap.vid.services.VersionService;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.DaoUtils;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

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
    private VersionService versionService;

    @Inject
    private JobsBrokerService jobsBrokerService;

    @Inject
    private JobWorker jobWorker;

    @Inject
    private FeatureManager featureManager;

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    private AuditService auditService;

    @Inject
    private RestMsoImplementation restMso;

    @Inject
    private DataAccessService dataAccessService;

    @Inject
    private CommandUtils commandUtils;

    @Inject
    private InstantiationTemplatesService instantiationTemplates;

    @BeforeClass
    void initServicesInfoService() {
        createInstanceParamsMaps();
        when(versionService.retrieveBuildNumber()).thenReturn("fakeBuild");
    }

    @BeforeMethod
    void clearDb() {
        dataAccessService.deleteDomainObjects(ServiceInfo.class, "1=1", DaoUtils.getPropsMap());
        dataAccessService.deleteDomainObjects(JobDaoImpl.class, "1=1", DaoUtils.getPropsMap());
        dataAccessService.deleteDomainObjects(NameCounter.class, "1=1", DaoUtils.getPropsMap());
    }

    @BeforeMethod
    void defineMocks() {
        Mockito.reset(restMso);
        Mockito.reset(aaiClient);
        Mockito.reset(commandUtils);
        mockAaiClientAnyNameFree();

        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VNF)).thenReturn(true);
        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VFMODULE)).thenReturn(true);
        when(featureManager.isActive(Features.FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF)).thenReturn(true);
    }

    @Test
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

    public static RestObject<RequestReferencesContainer> createResponseRandomIds(int statusCode) {
        return createResponse(statusCode, UUID.randomUUID().toString(), UUID.randomUUID().toString());
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
    @Test
    public void testStatusesOfMacroServiceInBulkDuringBulkLifeCycle() {

        final String SERVICE_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE_INSTANCE_ID = UUID.randomUUID().toString();
        final String SERVICE2_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE2_INSTANCE_ID = UUID.randomUUID().toString();

        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty())))
            .thenReturn(createResponse(200, SERVICE_INSTANCE_ID, SERVICE_REQUEST_ID));

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
        UUID firstJobUuid = uuids.get(0);
        UUID secondJobUuid = uuids.get(1);
        //assert that when get ProcessingException from restMso, status remain the same
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).thenThrow(new ProcessingException("fake message"));
        processJobsCountTimesAndAssertStatus(firstJobUuid, 10, IN_PROGRESS, PENDING);

        //assert that when get IN_PROGRESS status from restMso, status remain IN_PROGRESS
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR));
        processJobsCountTimesAndAssertStatus(firstJobUuid, 10, IN_PROGRESS, PENDING);

        //assert that when get unrecognized status from restMso, status remain IN_PROGRESS
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject("not a state"));
        processJobsCountTimesAndAssertStatus(firstJobUuid, 10, IN_PROGRESS, PENDING);

        //assert that when get non 200 status code during IN_PROGRESS, status remain IN_PROGRESS
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR, 404));
        processJobsCountTimesAndAssertStatus(firstJobUuid, 10, IN_PROGRESS, PENDING);

        //when get job COMPLETE from MSO, service status become COMPLETED
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, COMPLETED);
        List<ServiceInfo> serviceInfoList = listServicesAndAssertStatus(COMPLETED, PENDING, firstJobUuid);


        //for use later in the test
        Map<UUID, JobStatus> expectedJobStatusMap = serviceInfoList.stream().collect(
            Collectors.toMap(ServiceInfo::getJobId, x-> PENDING));
        expectedJobStatusMap.put(firstJobUuid, COMPLETED);

        //when handling another PENDING job, statuses are : COMPLETED, IN_PROGRESS, PENDING
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty())))
            .thenReturn(createResponse(200, SERVICE2_INSTANCE_ID, SERVICE2_REQUEST_ID));
        when(restMso.GetForObject(endsWith(SERVICE2_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR));
        processJobsCountTimes(10);

        expectedJobStatusMap.put(secondJobUuid, JobStatus.IN_PROGRESS);
        listServicesAndAssertStatus(expectedJobStatusMap);


        //when get FAILED status from MSO statuses are : COMPLETED, FAILED, STOPPED
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).thenReturn(asyncRequestStatusResponseAsRestObject(FAILED_STR));
        pullJobProcessAndPushBack(JobStatus.IN_PROGRESS, JobStatus.FAILED);
        expectedJobStatusMap.put(secondJobUuid, JobStatus.FAILED);
        expectedJobStatusMap = expectedJobStatusMap.entrySet().stream().collect(Collectors.toMap(
            e -> e.getKey(), e -> e.getValue() == PENDING ? JobStatus.STOPPED : e.getValue()
        ));

        listServicesAndAssertStatus(expectedJobStatusMap);
        IntStream.range(0, uuids.size()).forEach(i -> {
            UUID uuid = uuids.get(i);
            List<String> vidStatuses = auditService.getAuditStatuses(uuid, VID).stream().map(x -> x.getJobStatus()).collect(Collectors.toList());
            assertThat(vidStatuses, is(expectedStatusesForVid.get(i)));
        });

        //assert no more jobs to pull
        assertFalse(jobsBrokerService.pull(PENDING, randomUuid()).isPresent());
        assertFalse(jobsBrokerService.pull(JobStatus.IN_PROGRESS, randomUuid()).isPresent());
    }


    @DataProvider
    public static Object[][] AlaCarteStatuses(Method test) {
        return new Object[][]{
            {COMPLETE_STR, JobStatus.COMPLETED},
            {FAILED_STR, JobStatus.COMPLETED_WITH_ERRORS},
        };
    }

    /*
    Make sure service state is in progress once request has sent to MSO
    Make sure service state is watching until state changes to complemented
    Make sure service state is watching until vnf state changes to completed
    Make sure service state is Completed successfully once we got from MSO complete for the vnf job.
    status Creating
     */
    @Test(dataProvider = "AlaCarteStatuses")
    public void testStatusesOfServiceDuringALaCarteLifeCycleIgnoringVfModules(String msoVnfStatus, JobStatus expectedServiceStatus) {
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
        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VFMODULE)).thenReturn(false); // this makes the test pass without mocking the vfModules
        final String SERVICE_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE_INSTANCE_ID = UUID.randomUUID().toString();
        final String VNF_REQUEST_ID = UUID.randomUUID().toString();


        //push alacarte with 1 vnf, verify STATUS pending
        UUID uuid = pushALaCarteWithVnf();
        singleServicesAndAssertStatus(JobStatus.PENDING, uuid);
        //mock mso to answer 200 of create service instance request, verify STATUS in progress
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith("serviceInstances"), any())).thenReturn(
            createResponse(200, SERVICE_INSTANCE_ID, SERVICE_REQUEST_ID));
        //mock mso to answer COMPLETE for service instance create, job status shall remain IN_PROGRESS and type shall be Watching
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        //mock mso to answer 200 of create vnf instance request, pull+execute vnf job, STATUS resource in progress
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs"), any())).thenReturn(
            createResponse(200, UUID.randomUUID().toString(), VNF_REQUEST_ID));
        when(restMso.GetForObject(endsWith(VNF_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(
            asyncRequestStatusResponseAsRestObject(msoVnfStatus));

        processJobsCountTimesAndAssertStatus(uuid, 100, expectedServiceStatus);
        verify(restMso, times(1)).restCall(eq(HttpMethod.POST), any(), any(), eq("/serviceInstantiation/v7/serviceInstances"), any());
        verify(restMso, times(1)).restCall(eq(HttpMethod.POST), any(), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs"), any());
        verify(restMso, times(2)).GetForObject(any(), any());

    }

    /*
    this test is almost duplication of testStatusesOfServiceDuringALaCarteLifeCycleIgnoringVfModules.

    IgnoringVfModules test check the scenario while FLAG_ASYNC_ALACARTE_VFMODULE is off
    WithVfModules     test check the scenario while FLAG_ASYNC_ALACARTE_VFMODULE is on

    We shall consider later to remove testStatusesOfServiceDuringALaCarteLifeCycleIgnoringVfModules
    And union these tests to single one.
     */

    @Test
    public void testALaCarteLifeCycle1Vnf2VfModules() {


        String msoVnfStatus = COMPLETE_STR;
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
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith("serviceInstances"), any())).thenReturn(
            createResponse(200, SERVICE_INSTANCE_ID, SERVICE_REQUEST_ID));

        //mock mso to answer COMPLETE for service instance create
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        /*---------- vnf -----------*/

        //mock mso to answer 200 of create vnf instance request
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs"), any())).thenReturn(
            createResponse(200, VNF_INSTANCE_ID, VNF_REQUEST_ID));

        //mock mso to answer msoVnfStatus (COMPLETE/FAILED) for vnf creation status,
        when(restMso.GetForObject(endsWith(VNF_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        reset(commandUtils);
        when(commandUtils.isVfModuleBaseModule(eq(SERVICE_MODEL_VERSION_ID),
            argThat(it -> it.getModelCustomizationId().equals(VF_MODULE_0_MODEL_CUSTOMIZATION_NAME)))).thenReturn(true);
        when(commandUtils.isVfModuleBaseModule(eq(SERVICE_MODEL_VERSION_ID),
            argThat(it -> it.getModelCustomizationId().equals(VF_MODULE_1_MODEL_CUSTOMIZATION_NAME)))).thenReturn(false);

        /*---------- vf Module without volume group name (base) -----------*/

        //mock mso to answer 200 of create vfModule instance request, pull+execute volumeGroup job, STATUS resource in progress
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs/" + VNF_INSTANCE_ID + "/vfModules"), any())).thenReturn(
            createResponse(200, UUID.randomUUID().toString(), VG_REQUEST_ID));
        //mock mso to answer for vf module orchestration request
        when(restMso.GetForObject(endsWith(VF_MODULE_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(
            asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs/" + VNF_INSTANCE_ID + "/volumeGroups"), any())).thenReturn(
            createResponse(200, VG_INSTANCE_ID, VG_REQUEST_ID));
        //mock mso to answer for volume group orchestration request
        when(restMso.GetForObject(endsWith(VG_REQUEST_ID), eq(AsyncRequestStatus.class))).thenReturn(
            asyncRequestStatusResponseAsRestObject(msoVnfStatus));

        /*---------- vfModule -----------*/

        //mock mso to answer 200 of create vfModule instance request, pull+execute volumeGroup job, STATUS resource in progress
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs/" + VNF_INSTANCE_ID + "/vfModules"), any())).thenReturn(
            createResponse(200, UUID.randomUUID().toString(), VF_MODULE_REQUEST_ID2));

        //mock mso to answer for vf module orchestration request
        when(restMso.GetForObject(endsWith(VF_MODULE_REQUEST_ID2), eq(AsyncRequestStatus.class))).thenReturn(
            asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        processJobsCountTimesAndAssertStatus(uuid, 200, COMPLETED);
        verify(restMso, times(1)).restCall(eq(HttpMethod.POST), any(), any(), eq("/serviceInstantiation/v7/serviceInstances"), any());
        verify(restMso, times(1)).restCall(eq(HttpMethod.POST), any(), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs"), any());
        verify(restMso, times(1)).restCall(eq(HttpMethod.POST), any(), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs/" + VNF_INSTANCE_ID + "/volumeGroups"), any());
        verify(restMso, times(2)).restCall(eq(HttpMethod.POST), any(), any(), endsWith(SERVICE_INSTANCE_ID + "/vnfs/" + VNF_INSTANCE_ID + "/vfModules"), any());
        verify(restMso, times(5)).GetForObject(any(), any());
    }

    @Test
    public void testALaCarteLifeCycle2Networks() {

        //Create Service with 2 networks, and make sure they created in sequence (and not in parallel)
        //Config MSO to response 200 only to first network creation. And answer 500 for second one.
        //Then MSO return in_progress some times (like 10 times), and then return COMPLETE.
        //Only when MSO return COMPLETE for first network, config MSO to return 200 for second network creation

        final String SERVICE_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE_INSTANCE_ID = UUID.randomUUID().toString();
        final String NETWORK_REQUEST_ID1 = UUID.randomUUID().toString();
        final String NETWORK_INSTANCE_ID1 = UUID.randomUUID().toString();
        //TODO use them later for different networks
        final String NETWORK_REQUEST_ID2 = UUID.randomUUID().toString();
        final String NETWORK_INSTANCE_ID2 = UUID.randomUUID().toString();


        NetworkDetails networkDetails1 = new NetworkDetails("LukaDoncic", "1");
        NetworkDetails networkDetails2 = new NetworkDetails("KevinDurant", "2");

        /*---------- service -----------*/

        //mock mso to answer 200 of create service instance request, verify STATUS in progress
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith("serviceInstances"), any())).thenReturn(
            createResponse(200, SERVICE_INSTANCE_ID, SERVICE_REQUEST_ID));

        //mock mso to answer COMPLETE for service instance create
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        final MutableInt secondNetworkCode = new MutableInt(500);
        final MutableInt inProgressCount = new MutableInt(0);

        /*---------- network 1-----------*/

        //mock mso to answer 200 of first create network instance request
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class),
            MockitoHamcrest.argThat(jsonPartMatches("requestDetails.requestInfo.instanceName", equalTo(networkDetails1.name))) ,
            endsWith(SERVICE_INSTANCE_ID + "/networks"), any())).thenReturn(
            createResponse(200, NETWORK_INSTANCE_ID1, NETWORK_REQUEST_ID1));

        //mock mso to answer IN_PROGRESS 10 times, and only then COMPLETE for first network
        //Once COMPLETE, second network creation will return 200
        when(restMso.GetForObject(endsWith(NETWORK_REQUEST_ID1), eq(AsyncRequestStatus.class))).
            thenAnswer(x->{
                String status;
                if (inProgressCount.getValue()<10) {
                    status = IN_PROGRESS_STR;
                } else {
                    secondNetworkCode.setValue(200);
                    status = COMPLETE_STR;
                }
                inProgressCount.add(1);
                return asyncRequestStatusResponseAsRestObject(status);
            });

        /*---------- network 2-----------*/

        //mock MSO to return status code of secondNetworkCode (500 and 200 after first one COMPLETED)
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class),
            MockitoHamcrest.argThat(jsonPartMatches("requestDetails.requestInfo.instanceName", equalTo(networkDetails2.name))) ,
            endsWith(SERVICE_INSTANCE_ID + "/networks"), any())).thenAnswer(x->
            createResponse(secondNetworkCode.intValue(), NETWORK_INSTANCE_ID2, NETWORK_REQUEST_ID2));

//        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any() , endsWith(SERVICE_INSTANCE_ID + "/networks"), any())).thenReturn(
//                createResponse(200, NETWORK_INSTANCE_ID1, NETWORK_REQUEST_ID1));
        //mock mso to answer COMPLETE for network creation status,

        when(restMso.GetForObject(endsWith(NETWORK_REQUEST_ID2), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));


        /*---------- Create request and process it -----------*/
        //push alacarte with 2 networks, verify STATUS pending
        when(featureManager.isActive(Features.FLAG_EXP_CREATE_RESOURCES_IN_PARALLEL)).thenReturn(false);
        ServiceInstantiation serviceInstantiation = generateALaCarteWithNetworksPayload(ImmutableList.of(networkDetails1, networkDetails2));
        UUID uuid = asyncInstantiationBL.pushBulkJob(serviceInstantiation, USER_ID).get(0);
        singleServicesAndAssertStatus(JobStatus.PENDING, uuid);

        processJobsCountTimesAndAssertStatus(uuid, 200, COMPLETED);

        //validate the mso request id is the right one
        List<ServiceInfo> serviceInfoList = asyncInstantiationBL.getAllServicesInfo();
        ServiceInfo serviceInfo = serviceInfoList.get(0);
        assertThat(serviceInfo.getMsoRequestId(), is(UUID.fromString(SERVICE_REQUEST_ID)));

        /*---------- verify -----------*/
        verify(restMso, times(1)).restCall(eq(HttpMethod.POST), any(), any(), eq("/serviceInstantiation/v7/serviceInstances"), any());
        verify(restMso, times(2)).restCall(eq(HttpMethod.POST), any(), any(), endsWith(SERVICE_INSTANCE_ID + "/networks"), any());
        //get status
        verify(restMso, times(1)).GetForObject(endsWith(SERVICE_REQUEST_ID), any());
        verify(restMso, times(11)).GetForObject(endsWith(NETWORK_REQUEST_ID1), any());
        verify(restMso, times(1)).GetForObject(endsWith(NETWORK_REQUEST_ID2), any());
    }

    @Test
    public void testBadAaiResponseForSearchNamesAndBackToNormal() {
        when(aaiClient.isNodeTypeExistsByName(any(), any())).thenThrow(aaiNodeQueryBadResponseException());
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty()))).thenReturn(createResponse(200));
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        List<UUID> uuids = pushMacroBulk();
        processJobsCountTimesAndAssertStatus(uuids.get(0), 5, IN_PROGRESS, PENDING);  //JOB shall become IN_PROGRESS but service info is still pending

        //simulate AAI back to normal, AAI return name is free, and MSO return good response
        Mockito.reset(aaiClient); // must forget the "thenThrow"
        when(aaiClient.isNodeTypeExistsByName(any(), any())).thenReturn(false);
        processJobsCountTimesAndAssertStatus(uuids.get(0), 30, COMPLETED, COMPLETED);

    }

    @Test
    public void testAaiResponseNameUsedTillMaxRetries() {
        when(aaiClient.isNodeTypeExistsByName(any(), any())).thenReturn(true);
        //simulate MSO to return good result, for making sure we failed because of AAI error
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty()))).thenReturn(createResponse(200));
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        asyncInstantiationBL.setMaxRetriesGettingFreeNameFromAai(10);
        List<UUID> uuids = pushMacroBulk();
        processJobsCountTimesAndAssertStatus(uuids.get(0), 20, FAILED, STOPPED);
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

    private void simplePullJobProcessAndPushBack(JobStatus topic) {
        Optional<Job> optionalJob =  jobsBrokerService.pull(topic, randomUuid());
        optionalJob.ifPresent(job->{
            Job nextJob = jobWorker.executeJobAndGetNext(job);
            jobsBrokerService.pushBack(nextJob);
        });
    }

    private Job pullJobProcessAndPushBackWithTypeAssertion(JobStatus topic, JobStatus expectedNextJobStatus,
        JobType expectedNextJobType) {
        Job job = pullJobProcessAndPushBack(topic, expectedNextJobStatus, false);
        assertThat("job not ok: " + job.getData(), job.getType(), is(expectedNextJobType));
        return job;
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


    @Test
    public void whenPushNewBulk_andGetNoResponseFromMsoOnCreation_thenServiceMoveToFailedAndOtherToStopped() {
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty()))).thenReturn(createResponse(500));
        //assert that when get ProcessingException from restMso, status remain the same
        List<UUID> uuids = pushMacroBulk();
        processJobsCountTimesAndAssertStatus(uuids.get(0), 30, JobStatus.FAILED, JobStatus.STOPPED);
    }

    @Test
    public void whenMsoStatusIsPendingManualTask_ThenJobStatusIsPaused() {
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty()))).thenReturn(createResponse(200));
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(PENDING_MANUAL_TASK));

        //assert that when get ProcessingException from restMso, status remain the same
        List<UUID> uuids = pushMacroBulk();
        processJobsCountTimesAndAssertStatus(uuids.get(0), 30, PAUSE, PENDING);


        //the job get IN_PROGRESS response (simulate activate operation) and status changed to IN_PROGRESS
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR));
        processJobsCountTimesAndAssertStatus(uuids.get(0), 30, IN_PROGRESS, PENDING);

        //the job get COMPLETE response this job is copmpleted and then also other jobs
        when(restMso.GetForObject(any(), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        processJobsCountTimesAndAssertStatus(uuids.get(0), 200, COMPLETED, COMPLETED);

        ImmutableList<String> expectedStatusesForVid = statusesToStrings(PENDING, IN_PROGRESS, PAUSE, IN_PROGRESS, COMPLETED);
        List<String> vidStatuses = auditService.getAuditStatuses(uuids.get(0), VID).stream().map(x -> x.getJobStatus()).collect(Collectors.toList());
        assertThat(vidStatuses, is(expectedStatusesForVid));
    }

    private Job pushBulkPullPendingJobAndAssertJobStatus(JobStatus pulledJobStatus, JobStatus otherJobsStatus) {
        pushMacroBulk();
        return pullPendingJobAndAssertJobStatus(pulledJobStatus, otherJobsStatus);
    }

    private Job pullPendingJobAndAssertJobStatus(JobStatus pulledJobStatus, JobStatus otherJobsStatus) {
        Job job = pullJobProcessAndPushBack(PENDING, pulledJobStatus, false);
        listServicesAndAssertStatus(pulledJobStatus, otherJobsStatus, job.getUuid());
        return job;
    }

    @Test
    public void test2BulksLifeCyclesAreIndependent() {

        final String SERVICE1_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE1_INSTANCE_ID = UUID.randomUUID().toString();
        final String SERVICE2_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE2_INSTANCE_ID = UUID.randomUUID().toString();
        final String SERVICE3_4_REQUEST_ID = UUID.randomUUID().toString();
        final String SERVICE3_4_INSTANCE_ID = UUID.randomUUID().toString();


        //create first bulk and make one job in progress
        List<UUID> firstBulksIDs = pushMacroBulk();
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty())))
            .thenReturn(createResponse(200, SERVICE1_INSTANCE_ID, SERVICE1_REQUEST_ID));
        when(restMso.GetForObject(endsWith(SERVICE1_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR));
        processJobsCountTimesAndAssertStatus(firstBulksIDs.get(0), 30, IN_PROGRESS, PENDING);

        //create 2nd bulk, then when pulling first job the job become in_progress, other jobs (from 2 bulks) remain pending
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty())))
            .thenReturn(createResponse(200, SERVICE2_INSTANCE_ID, SERVICE2_REQUEST_ID));
        when(restMso.GetForObject(endsWith(SERVICE2_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR));
        List<UUID> secondBulksIDs = pushMacroBulk();
        processJobsCountTimes(30);
        Map<JobStatus, Long> statusCount = getJobStatusesCount();
        assertThat(statusCount.get(IN_PROGRESS), is(2L));
        assertThat(statusCount.get(PENDING), is(4L));

        //return failed to first job
        //first bulk statuses shall be: FAILED, STOPPED, STOPPED
        //second bulk statuses shall be: IN_PROGRESS, PENDING, PENDING
        when(restMso.GetForObject(endsWith(SERVICE1_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(FAILED_STR));
        processJobsCountTimes(30);
        Map<UUID, List<ServiceInfo>> servicesByTemplateId =
            asyncInstantiationBL.getAllServicesInfo()
                .stream().collect(groupingBy(ServiceInfo::getTemplateId));
        ServiceInfo failedJob = asyncInstantiationBL.getAllServicesInfo().stream().filter(x->x.getJobId().equals(firstBulksIDs.get(0))).findFirst().get();
        assertServicesStatus(servicesByTemplateId.get(failedJob.getTemplateId()), JobStatus.FAILED, JobStatus.STOPPED, failedJob.getJobId());
        ServiceInfo successJob = asyncInstantiationBL.getAllServicesInfo().stream().filter(x->x.getJobId().equals(secondBulksIDs.get(0))).findFirst().get();
        assertServicesStatus(servicesByTemplateId.get(successJob.getTemplateId()), JobStatus.IN_PROGRESS, PENDING, successJob.getJobId());

        //return completed to all other jobs
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty())))
            .thenReturn(createResponse(200, SERVICE3_4_INSTANCE_ID, SERVICE3_4_REQUEST_ID));
        when(restMso.GetForObject(endsWith(SERVICE2_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        when(restMso.GetForObject(endsWith(SERVICE3_4_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        processJobsCountTimes(30);
        servicesByTemplateId = asyncInstantiationBL.getAllServicesInfo().stream().collect(groupingBy(ServiceInfo::getTemplateId));
        assertServicesStatus(servicesByTemplateId.get(failedJob.getTemplateId()), JobStatus.FAILED, JobStatus.STOPPED, failedJob.getJobId());
        assertServicesStatus(servicesByTemplateId.get(successJob.getTemplateId()), COMPLETED, COMPLETED, successJob.getJobId());
        //assert no more PENDING jobs nor IN_PROGRESS jobs to pull
        assertFalse(jobsBrokerService.pull(PENDING, randomUuid()).isPresent());
        assertFalse(jobsBrokerService.pull(JobStatus.IN_PROGRESS, randomUuid()).isPresent());
    }

    protected Map<JobStatus, Long> getJobStatusesCount() {
        return asyncInstantiationBL.getAllServicesInfo().stream().collect(groupingBy(ServiceInfo::getJobStatus, counting()));
    }

    @Test
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
        pullJobProcessAndPushBackWithTypeAssertion(PENDING, IN_PROGRESS, JobType.ALaCarteService, Action.Create, InternalState.INITIAL, 1);
        verifyQueueSizes(ImmutableMap.of(
            IN_PROGRESS, 1
        ));

        // take job #1 from phase delete to phase create -> 3 create-child were born
        verify_Job1InProgress.accept(Action.Create, IN_PROGRESS);
        verifyQueueSizes(ImmutableMap.of(
            IN_PROGRESS, 1, PENDING_RESOURCE, 3
        ));

        // prepare MSO mock
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith("instanceGroups"), eq(Optional.empty())))
            .thenReturn(createResponse(200, GROUP1_INSTANCE_ID, GROUP1_REQUEST_ID))
            .thenReturn(createResponse(200, GROUP2_INSTANCE_ID, GROUP2_REQUEST_ID))
            .thenReturn(null);
        when(restMso.GetForObject(argThat(uri -> StringUtils.endsWithAny(uri, GROUP1_REQUEST_ID, GROUP2_REQUEST_ID)), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        // take first "none" child from creating to COMPLETED_WITH_NO_ACTION
        // note there's no concrete mechanism that makes the first child be
        // the "action=None" case, but that's what happens, and following lines
        // relies on that fact.
        pullJobProcessAndPushBackWithTypeAssertion(PENDING_RESOURCE, COMPLETED_WITH_NO_ACTION, JobType.InstanceGroup, Action.Create, null, 1);

        // take each of next two children from creating to in-progress, then to completed
        // verify job #1 is watching, and MSO is getting requests
        Stream.of(1, 2).forEach(i -> {
            verify_Job1InProgress.accept(Action.Create, IN_PROGRESS);
            pullJobProcessAndPushBackWithTypeAssertion(PENDING_RESOURCE, RESOURCE_IN_PROGRESS, JobType.InstanceGroup, Action.Create, InternalState.IN_PROGRESS, 1);
            verify(restMso, times(i)).restCall(any(), any(), any(), any(), any());

            verify_Job1InProgress.accept(Action.Create, IN_PROGRESS);
            pullJobProcessAndPushBackWithTypeAssertion(RESOURCE_IN_PROGRESS, COMPLETED, JobType.InstanceGroup, Action.Create, null, 3);
            verify(restMso, times(i)).GetForObject(any(), any());
        });

        // job #1 is done as all children are done
        verify_Job1InProgress.accept(Action.Create, COMPLETED);
        verifyQueueSizes(ImmutableMap.of(COMPLETED, 3, COMPLETED_WITH_NO_ACTION, 1));
    }

    @DataProvider
    public static Object[][] createAndDeleteIntegrationTestDataProvider(Method test) {
        return new Object[][]{
            {"create and delete both bad http code", createResponse(400), createResponse(500), null, null, FAILED, 0},
            {"create and delete success and status is success ", createResponseRandomIds(202), createResponseRandomIds(202),
                asyncRequestStatusResponseAsRestObject(COMPLETE_STR), asyncRequestStatusResponseAsRestObject(COMPLETE_STR), COMPLETED, 2},
            {"create and delete success, create status FAILED, delete status COMPLETED", createResponseRandomIds(202), createResponseRandomIds(202),
                asyncRequestStatusResponseAsRestObject(FAILED_STR), asyncRequestStatusResponseAsRestObject(COMPLETE_STR), COMPLETED_WITH_ERRORS, 2},
            {"create and delete success, create status FAILED, delete status FAILED", createResponseRandomIds(202), createResponseRandomIds(202),
                asyncRequestStatusResponseAsRestObject(FAILED_STR), asyncRequestStatusResponseAsRestObject(FAILED_STR), FAILED, 2},
            {"create success but delete failed and status is success ", createResponseRandomIds(202), createResponseRandomIds(400),
                asyncRequestStatusResponseAsRestObject(COMPLETE_STR), null, COMPLETED_WITH_ERRORS, 1},
            {"delete success but create failed and status is success ", createResponseRandomIds(400), createResponseRandomIds(202),
                null, asyncRequestStatusResponseAsRestObject(COMPLETE_STR), COMPLETED_WITH_ERRORS, 1},
            {"delete success but create failed and status of delete is FAILED ", createResponseRandomIds(400), createResponseRandomIds(202),
                null, asyncRequestStatusResponseAsRestObject(FAILED_STR), FAILED, 1}
        };
    }

    //this test is going along with AsyncInstantiationALaCarteApiTest.viewEditVnfGroup__verifyStatusAndAudit API test
    //The API test has only the happy flow scenario, while this test also test additional MSO responses (mostly non happy)
    @Test(dataProvider="createAndDeleteIntegrationTestDataProvider")
    public void vnfGropingIntegrationTest(
        String desc,
        RestObject<RequestReferencesContainer> createGroupResponse,
        RestObject<RequestReferencesContainer> deleteGroupResponse,
        RestObject<AsyncRequestStatus> createStatusResponse,
        RestObject<AsyncRequestStatus> deleteStatusResponse,
        JobStatus expectedJobStatus,
        int getStatusCounter) {

        UUID jobUUID = createAndDeleteIntegrationTest("/payload_jsons/VnfGroupCreate1Delete1None1Request.json",
            "/serviceInstantiation/v7/instanceGroups",
            createGroupResponse,
            "/serviceInstantiation/v7/instanceGroups/VNF_GROUP1_INSTANCE_ID",
            deleteGroupResponse,
            createStatusResponse,
            deleteStatusResponse,
            expectedJobStatus,
            getStatusCounter);

        ServiceInstantiation bulkForRetry = asyncInstantiationBL.getBulkForRetry(jobUUID);
        InstanceGroup vnfGroupShouldBeDeleted = bulkForRetry.getVnfGroups().get("groupingservicefortest..ResourceInstanceGroup..0:001");
        InstanceGroup vnfGroupShouldBeCreated = bulkForRetry.getVnfGroups().get("groupingservicefortest..ResourceInstanceGroup..0");

        if (deleteStatusResponse == null || deleteStatusResponse.get().request.requestStatus.getRequestState().equals(FAILED_STR)) {
            assertThat(vnfGroupShouldBeDeleted.getAction(), equalTo(Action.Delete));
            assertErrorForResource(vnfGroupShouldBeDeleted, deleteGroupResponse, deleteStatusResponse);
        }

        if (createStatusResponse == null || createStatusResponse.get().request.requestStatus.getRequestState().equals(FAILED_STR)) {
            assertThat(vnfGroupShouldBeCreated.getAction(), equalTo(Action.Create));
            assertErrorForResource(vnfGroupShouldBeCreated, createGroupResponse, createStatusResponse);
        }
    }

    //this test is going along with AsyncInstantiationALaCarteApiTest3.delete1Create1VnfFromService API test
    //The API test has only the happy flow scenario, while this test also test additional MSO responses (mostly non happy)
    @Test(dataProvider="createAndDeleteIntegrationTestDataProvider")
    public void vnfsIntegrationTest(
        String desc,
        RestObject<RequestReferencesContainer> createVnfResponse,
        RestObject<RequestReferencesContainer> deleteVnfResponse,
        RestObject<AsyncRequestStatus> createStatusResponse,
        RestObject<AsyncRequestStatus> deleteStatusResponse,
        JobStatus expectedJobStatus,
        int getStatusCounter) {

        createAndDeleteIntegrationTest("/payload_jsons/vnfDelete1Create1Request.json",
            "/serviceInstantiation/v7/serviceInstances/f8791436-8d55-4fde-b4d5-72dd2cf13cfb/vnfs",
            createVnfResponse,
            "/serviceInstantiation/v7/serviceInstances/f8791436-8d55-4fde-b4d5-72dd2cf13cfb/vnfs/VNF_INSTANCE_ID",
            deleteVnfResponse,
            createStatusResponse,
            deleteStatusResponse,
            expectedJobStatus,
            getStatusCounter);
    }

    @Test(dataProvider="createAndDeleteIntegrationTestDataProvider")
    public void vfModulesIntegrationTest(
        String desc,
        RestObject<RequestReferencesContainer> createVfModuleResponse,
        RestObject<RequestReferencesContainer> deleteVfModuleResponse,
        RestObject<AsyncRequestStatus> createStatusResponse,
        RestObject<AsyncRequestStatus> deleteStatusResponse,
        JobStatus expectedJobStatus,
        int getStatusCounter) {

        when(commandUtils.isVfModuleBaseModule(eq("6b528779-44a3-4472-bdff-9cd15ec93450"),
            argThat(it -> it.getModelCustomizationName().equals("2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0")))).thenReturn(true);
        when(commandUtils.isVfModuleBaseModule(eq("6b528779-44a3-4472-bdff-9cd15ec93450"),
            argThat(it -> it.getModelCustomizationName().equals("2017488PasqualeVpe..PASQUALE_vRE_BV..module-1")))).thenReturn(false);

        createAndDeleteIntegrationTest("/payload_jsons/vfModuleDelete1Create1None1Request.json",
            "/serviceInstantiation/v7/serviceInstances/f8791436-8d55-4fde-b4d5-72dd2cf13cfb/vnfs/VNF_INSTANCE_ID/vfModules",
            createVfModuleResponse,
            "/serviceInstantiation/v7/serviceInstances/f8791436-8d55-4fde-b4d5-72dd2cf13cfb/vnfs/VNF_INSTANCE_ID/vfModules/VF_MODULE_INSTANCE_ID",
            deleteVfModuleResponse,
            createStatusResponse,
            deleteStatusResponse,
            expectedJobStatus,
            getStatusCounter);
    }

    //this test is going along with AsyncInstantiationALaCarteApiTest.delete1Create1NetworkFromService API test
    //The API test has only the happy flow scenario, while this test also test additional MSO responses (mostly non happy)
    @Test(dataProvider="createAndDeleteIntegrationTestDataProvider")
    public void networksIntegrationTest(
        String desc,
        RestObject<RequestReferencesContainer> createNetworkResponse,
        RestObject<RequestReferencesContainer> deleteNetworkResponse,
        RestObject<AsyncRequestStatus> createStatusResponse,
        RestObject<AsyncRequestStatus> deleteStatusResponse,
        JobStatus expectedJobStatus,
        int getStatusCounter) {

        createAndDeleteIntegrationTest("/payload_jsons/networkDelete1Create1Request.json",
            "/serviceInstantiation/v7/serviceInstances/f8791436-8d55-4fde-b4d5-72dd2cf13cfb/networks",
            createNetworkResponse,
            "/serviceInstantiation/v7/serviceInstances/f8791436-8d55-4fde-b4d5-72dd2cf13cfb/networks/NETWORK_INSTANCE_ID",
            deleteNetworkResponse,
            createStatusResponse,
            deleteStatusResponse,
            expectedJobStatus,
            getStatusCounter);
    }

    private UUID createAndDeleteIntegrationTest(String payload,
        String createPath,
        RestObject<RequestReferencesContainer> createResponse,
        String deletePath,
        RestObject<RequestReferencesContainer> deleteResponse,
        RestObject<AsyncRequestStatus> createStatusResponse,
        RestObject<AsyncRequestStatus> deleteStatusResponse,
        JobStatus expectedJobStatus,
        int getStatusCounter) {
        UUID jobUUID = asyncInstantiationBL.pushBulkJob(
            readJsonResourceFileAsObject(payload, ServiceInstantiation.class), "userId")
            .get(0);

        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), eq(createPath), any())).thenReturn(createResponse);
        when(restMso.restCall(eq(HttpMethod.DELETE), eq(RequestReferencesContainer.class), any(), eq(deletePath), any())).thenReturn(deleteResponse);
        if  (createStatusResponse!=null) {
            when(restMso.GetForObject(endsWith(createResponse.get().getRequestReferences().getRequestId()), eq(AsyncRequestStatus.class))).thenReturn(createStatusResponse);
        }
        if  (deleteStatusResponse!=null) {
            when(restMso.GetForObject(endsWith(deleteResponse.get().getRequestReferences().getRequestId()), eq(AsyncRequestStatus.class))).thenReturn(deleteStatusResponse);
        }

        processJobsCountTimesAndAssertStatus(jobUUID, 40, expectedJobStatus);

        verify(restMso, times(1)).restCall(eq(HttpMethod.POST), any(), any(), eq(createPath), any());
        verify(restMso, times(1)).restCall(eq(HttpMethod.DELETE), any(), any(), eq(deletePath), any());
        verify(restMso, times(getStatusCounter)).GetForObject(any(), any());

        return jobUUID;
    }

    @Test
    public void whenCreateTransportService_thanExpectedPre1806MacroRequestSent() {
        UUID jobUUID = asyncInstantiationBL.pushBulkJob(generatePre1806MacroTransportServiceInstantiationPayload(null, null),"az2016").get(0);
        RestObject<RequestReferencesContainer> createResponse = createResponseRandomIds(202);

        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), eq("/serviceInstantiation/v7/serviceInstances"), any()))
            .thenReturn(createResponse);
        when(restMso.GetForObject(endsWith(createResponse.get().getRequestReferences().getRequestId()), eq(AsyncRequestStatus.class)))
            .thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));
        processJobsCountTimesAndAssertStatus(jobUUID, 20, COMPLETED);

        JsonNode expectedJson = readJsonResourceFileAsObject("/payload_jsons/pre_1806_macro_without_cloudConfiguration.json", JsonNode.class);
        ArgumentCaptor<RequestDetailsWrapper> requestCaptor = ArgumentCaptor.forClass(RequestDetailsWrapper.class);
        verify(restMso).restCall(any(), any(), requestCaptor.capture(), any(), any());
        requestCaptor.getAllValues().forEach(x->assertJsonEquals(expectedJson, x));
    }

    private void assertErrorForResource(BaseResource resource,
        RestObject<RequestReferencesContainer> deleteOrCreateResponse,
        RestObject<AsyncRequestStatus> statusResponse) {
        JobAuditStatus auditStatus = auditService.getResourceAuditStatus(resource.getTrackById());
        assertThat(auditStatus, is(notNullValue()));
        assertThat(auditStatus.getJobStatus(), equalTo(FAILED_STR));
        if (statusResponse == null) {
            String errorMessage = "Http Code:" + deleteOrCreateResponse.getStatusCode() + ", " + RAW_DATA_FROM_MSO;
            assertThat(auditStatus.getAdditionalInfo(), equalTo(errorMessage));
            assertThat(auditStatus.getRequestId(), is(nullValue()));
        } else {
            assertThat(auditStatus.getRequestId().toString(), equalTo(statusResponse.get().request.requestId));
        }
    }

    protected void processJobsCountTimesAndAssertStatus(UUID serviceJobId, int times, JobStatus expectedStatus) {
        processJobsCountTimes(times);
        singleServicesAndAssertStatus(expectedStatus, serviceJobId);
    }

    private void processJobsCountTimes(int times) {
        for (int i = 0; i < times; i++) {
            WORKERS_TOPICS.forEach(this::simplePullJobProcessAndPushBack);
        }
    }

    protected void processJobsCountTimesAndAssertStatus(UUID serviceJobId, int times, JobStatus expectedStatus, JobStatus otherJobsStatus) {
        processJobsCountTimes(times);
        listServicesAndAssertStatus(expectedStatus, otherJobsStatus, serviceJobId);
    }


    private void verifyQueueSizes(ImmutableMap<JobStatus, Integer> expected) {
        final Collection<Job> peek = jobsBrokerService.peek();
        final Map<JobStatus, Long> jobTypes = peek.stream().collect(groupingBy(Job::getStatus, counting()));
        assertThat(jobTypes, jsonEquals(expected));
    }

    private List<ServiceInfo> listServicesAndAssertStatus(JobStatus pulledJobStatus, JobStatus otherJobsStatus, UUID jobUUID) {
        List<ServiceInfo> serviceInfoList = asyncInstantiationBL.getAllServicesInfo();
        assertServicesStatus(serviceInfoList, pulledJobStatus, otherJobsStatus, jobUUID);

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

    private boolean isServiceOnStatus(JobStatus expectedStatus) {
        List<ServiceInfo> serviceInfoList = asyncInstantiationBL.getAllServicesInfo();
        assertEquals(1, serviceInfoList.size());
        return serviceInfoList.get(0).getJobStatus()==expectedStatus;
    }

    private void assertServicesStatus(List<ServiceInfo> serviceInfoList, JobStatus pulledJobStatus, JobStatus otherJobsStatus, UUID jobUUID) {
        serviceInfoList.forEach(si->{
            if (si.getJobId().equals(jobUUID)) {
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

    @Test
    public void whenResumeService_thanExpectedResumeRequestSent() {
        String instanceId = "a565e6ad-75d1-4493-98f1-33234b5c17e2"; //from feRequestResumeMacroService.json
        String originalRequestId = "894089b8-f7f4-418d-81da-34186fd32670"; //from msoResponseGetRequestsOfServiceInstance.json
        String resumeRequestId = randomUuid();
        String userId = TestUtils.generateRandomAlphaNumeric(6);

        //prepare mocks for get all requests for instance id
        RestObject<AsyncRequestStatusList> getRequestByIdResponse = createAsyncRequestStatusListByInstanceId();
        when(restMso.GetForObject(
            eq("/orchestrationRequests/v7?filter=serviceInstanceId:EQUALS:" + instanceId),
            eq(AsyncRequestStatusList.class)))
            .thenReturn(getRequestByIdResponse);

        //prepare mocks resume request
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), isNull(), eq(String.format("/orchestrationRequests/v7/%s/resume", originalRequestId)), eq(Optional.of(userId))))
            .thenReturn(createResponse(202, instanceId, resumeRequestId));

        //prepare mocks for get resume status
        when(restMso.GetForObject(eq("/orchestrationRequests/v7/" + resumeRequestId), eq(AsyncRequestStatus.class)))
            .thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR),
                asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR),
                asyncRequestStatusResponseAsRestObject(COMPLETE_STR));


        UUID jobUUID = asyncInstantiationBL.pushBulkJob(generateResumeMacroPayload(), userId).get(0);
        processJobsCountTimesAndAssertStatus(jobUUID, 20, COMPLETED);
        verify(restMso).GetForObject(
            eq("/orchestrationRequests/v7?filter=serviceInstanceId:EQUALS:" + instanceId),
            eq(AsyncRequestStatusList.class));
        verify(restMso).restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), isNull(), eq(String.format("/orchestrationRequests/v7/%s/resume", originalRequestId)), eq(Optional.of(userId)));
        verify(restMso, times(3)).GetForObject(eq("/orchestrationRequests/v7/" + resumeRequestId), eq(AsyncRequestStatus.class));
    }

    @Test
    public void givenResumeRequest_whenMsoReturnBadResponse_thanJobIsFailed() {
        //there is no mocks for restMSO which means restMso return bad response...
        UUID jobUUID = asyncInstantiationBL.pushBulkJob(generateResumeMacroPayload(), "abc").get(0);
        processJobsCountTimesAndAssertStatus(jobUUID, 20, FAILED);
    }

    @NotNull
    private RestObject<AsyncRequestStatusList> createAsyncRequestStatusListByInstanceId() {
        AsyncRequestStatusList asyncRequestStatusList = readJsonResourceFileAsObject(
            "/payload_jsons/resume/msoResponseGetRequestsOfServiceInstance.json",
            AsyncRequestStatusList.class);
        RestObject<AsyncRequestStatusList> getRequestByIdResponse = new RestObject<>();
        getRequestByIdResponse.set(asyncRequestStatusList);
        getRequestByIdResponse.setStatusCode(200);
        return getRequestByIdResponse;
    }

    private ServiceInstantiation generateResumeMacroPayload() {
        return readJsonResourceFileAsObject("/payload_jsons/resume/feRequestResumeMacroService.json", ServiceInstantiation.class);
    }

    @Test
    public void whenUpgradingVfModule_thenExpectedReplaceRequestSent() throws AsdcCatalogException {
        String currentServiceInstanceId = "6196ab1f-2349-4b32-9b6c-cffeb0ccc79c";
        String currentVnfInstanceId = "d520268f-7489-4662-be59-f81495b3a069";
        String currentVfModuleInstanceId = "b0732bed-3ddf-43cc-b193-7f18db84e476";

        assertTestPayloadFitsExpectedIds(upgradeVfModulePayload(), currentServiceInstanceId, currentVnfInstanceId, currentVfModuleInstanceId);

        String replaceRequestId = randomUuid();
        String userId = "az2016";

        String modelInvariantId = "b3a1a119-dede-4ed0-b077-2a617fa519a3";
        String newestModelUuid = "d9a5b318-187e-476d-97f7-a15687a927a9";

        String expectedMsoReplacePath = "/serviceInstantiation/v7/serviceInstances/"
            + currentServiceInstanceId + "/vnfs/" + currentVnfInstanceId + "/vfModules/" + currentVfModuleInstanceId + "/replace";

        when(commandUtils.getNewestModelUuid(eq(modelInvariantId))).thenReturn(newestModelUuid);
        when(commandUtils.getServiceModel(eq(newestModelUuid))).thenReturn(newestServiceModel());

        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), eq(expectedMsoReplacePath), eq(Optional.of(userId))))
            .thenReturn(createResponse(202, currentVfModuleInstanceId, replaceRequestId));

        when(restMso.GetForObject(eq("/orchestrationRequests/v7/" + replaceRequestId), eq(AsyncRequestStatus.class)))
            .thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR),
                asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR),
                asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        enableAddCloudOwnerOnMsoRequest();


        UUID jobUUID = asyncInstantiationBL.pushBulkJob(upgradeVfModulePayload(), userId).get(0);
        processJobsCountTimesAndAssertStatus(jobUUID, 20, COMPLETED);


        ArgumentCaptor<RequestDetailsWrapper> requestCaptor = ArgumentCaptor.forClass(RequestDetailsWrapper.class);
        verify(restMso, times(1)).restCall(
            eq(HttpMethod.POST),
            eq(RequestReferencesContainer.class),
            requestCaptor.capture(),
            eq(expectedMsoReplacePath),
            eq(Optional.of(userId))
        );

        JsonNode expectedPayloadToMso = readJsonResourceFileAsObject("/payload_jsons/vfmodule/upgrade_vfmodule_e2e__payload_to_mso.json", JsonNode.class);
        assertThat(requestCaptor.getValue(), jsonEquals(expectedPayloadToMso).when(IGNORING_ARRAY_ORDER));
    }

    @Test
    public void whenAddingNewNetwork_thenExpectedAddRequestSent() {
        String currentServiceInstanceId = "ce2821fc-3b28-4759-9613-1e514d7563c0";
        String addedNetworkInstanceModelCustomizationName = "OVS Provider";

        assertAddNetworkTestPayloadFitsExpectedIds(addNetworkBulkPayload(), currentServiceInstanceId, addedNetworkInstanceModelCustomizationName);

        String addNetworkRequestId = randomUuid();
        String userId = "az2016";

        String expectedMsoAddNetworkPath = "/serviceInstantiation/v7/serviceInstances/"
            + currentServiceInstanceId + "/networks";
        when(restMso.restCall(eq(HttpMethod.POST),eq(RequestReferencesContainer.class),any(), eq(expectedMsoAddNetworkPath), any()))
            .thenReturn(createResponse(202, currentServiceInstanceId, addNetworkRequestId));

        when(restMso.GetForObject(eq("/orchestrationRequests/v7/" + addNetworkRequestId),eq(AsyncRequestStatus.class)))
            .thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR),
                asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR),
                asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        enableAddCloudOwnerOnMsoRequest();
        UUID jobUUID = asyncInstantiationBL.pushBulkJob(addNetworkBulkPayload(), userId).get(0);
        processJobsCountTimesAndAssertStatus(jobUUID, 20, COMPLETED);

        ArgumentCaptor<RequestDetailsWrapper> requestCaptor = ArgumentCaptor.forClass(RequestDetailsWrapper.class);
        verify(restMso, times(1)).restCall(
            eq(HttpMethod.POST),
            eq(RequestReferencesContainer.class),
            requestCaptor.capture(),
            eq(expectedMsoAddNetworkPath),
            any()
        );

        JsonNode expectedPayloadToMso = readJsonResourceFileAsObject(
            "/payload_jsons/Network/addNetwork_e2e__payload_to_mso.json", JsonNode.class);

        assertThat(requestCaptor.getValue(), jsonEquals(expectedPayloadToMso).when(IGNORING_ARRAY_ORDER));
    }



    @Test
    public void whenDeletingVfModule_thenExpectedDeleteRequestSent()
    {
        String currentServiceInstanceId = "6196ab1f-2349-4b32-9b6c-cffeb0ccc79c";
        String currentVnfInstanceId = "d520268f-7489-4662-be59-f81495b3a069";
        String currentVfModuleInstanceId = "b0732bed-3ddf-43cc-b193-7f18db84e476";

        assertTestPayloadFitsExpectedIds(deleteVfModuleBulkPayload(), currentServiceInstanceId, currentVnfInstanceId, currentVfModuleInstanceId);

        String deleteRequestId = randomUuid();
        String userId = "az2016";


        String expectedMsoDeletePath = "/serviceInstantiation/v7/serviceInstances/"
                + currentServiceInstanceId + "/vnfs/" + currentVnfInstanceId + "/vfModules/" + currentVfModuleInstanceId;

        when(restMso.restCall(eq(HttpMethod.DELETE), eq(RequestReferencesContainer.class), any(), eq(expectedMsoDeletePath), eq(Optional.of(userId))))
                .thenReturn(createResponse(202, currentVfModuleInstanceId, deleteRequestId));

        when(restMso.GetForObject(eq("/orchestrationRequests/v7/" + deleteRequestId), eq(AsyncRequestStatus.class)))
                .thenReturn(asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR),
                        asyncRequestStatusResponseAsRestObject(IN_PROGRESS_STR),
                        asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        enableAddCloudOwnerOnMsoRequest();


        UUID jobUUID = asyncInstantiationBL.pushBulkJob(deleteVfModuleBulkPayload(), userId).get(0);
        processJobsCountTimesAndAssertStatus(jobUUID, 20, COMPLETED);


        ArgumentCaptor<RequestDetailsWrapper> requestCaptor = ArgumentCaptor.forClass(RequestDetailsWrapper.class);
        verify(restMso, times(1)).restCall(
                eq(HttpMethod.DELETE),
                eq(RequestReferencesContainer.class),
                requestCaptor.capture(),
                eq(expectedMsoDeletePath),
                eq(Optional.of(userId))
        );

        assertThat(requestCaptor.getValue(), jsonEquals(getDeleteVfModulePayloadToMso()));
    }


    private ServiceInstantiation deleteVfModuleBulkPayload() {
        return readJsonResourceFileAsObject("/payload_jsons/vfmodule/delete_1_vfmodule_expected_bulk.json", ServiceInstantiation.class);
    }

    private ServiceInstantiation addNetworkBulkPayload() {
        return readJsonResourceFileAsObject("/payload_jsons/Network/one_network_exists_add_another_network_expected_bulk.json", ServiceInstantiation.class);
    }

    private void assertAddNetworkTestPayloadFitsExpectedIds(ServiceInstantiation addNetworkPayload, String serviceInstanceId,
        String addedNetworkModelCustomizationName){
        assertThat(addNetworkPayload, jsonPartEquals("instanceId", serviceInstanceId));
        assertThat(addNetworkPayload, jsonNodePresent(
            "networks"
                + "." + addedNetworkModelCustomizationName
        ));
    }

    private String getDeleteVfModulePayloadToMso() {
        return "{ " +
                "  \"requestDetails\": { " +
                "    \"requestInfo\": { " +
                "      \"source\": \"VID\", " +
                "      \"requestorId\": \"az2016\" " +
                "    }, " +
                "    \"modelInfo\": { " +
                "      \"modelType\": \"vfModule\", " +
                "      \"modelName\": \"XbiTestModuleReplace..base_ocg..module-0\", " +
                "      \"modelVersionId\": \"04b21d26-9780-4956-8329-b22b049329f4\", " +
                "      \"modelVersion\": \"1.0\", " +
                "      \"modelInvariantId\": \"d887658e-2a89-4baf-83e2-b189601a1a7c\", " +
                "      \"modelCustomizationName\": \"XbiTestModuleReplace..base_ocg..module-0\", " +
                "      \"modelCustomizationId\": \"3f1f0fcb-8a88-4612-a794-3912613ed9e8\" " +
                "    }, " +
                "    \"cloudConfiguration\": { " +
                "      \"lcpCloudRegionId\": \"olson5a\", " +
                "      \"cloudOwner\": \"irma-aic\", " +
                "      \"tenantId\": \"7ff7b1a4fe954f71ab79d3160ec3eb08\" " +
                "    } " +
                "  } " +
                "}";
    }

    private void assertTestPayloadFitsExpectedIds(ServiceInstantiation upgradeVfModulePayload, String serviceInstanceId,
        String vnfInstanceId, String vfModuleInstanceId) {
        /*
        Just verifies the test and the input-file are using the same set of instance IDs
         */
        assertThat(upgradeVfModulePayload, jsonPartEquals("instanceId", serviceInstanceId));
        assertThat(upgradeVfModulePayload, jsonNodePresent(
            "vnfs"
                + "." + vnfInstanceId
                + ".vfModules"
                + ".xbitestmodulereplace0\\.\\.XbiTestModuleReplace\\.\\.base_ocg\\.\\.module-0"
                + "." + vfModuleInstanceId));
    }

    private ServiceModel newestServiceModel() {
        return readJsonResourceFileAsObject("/payload_jsons/vfmodule/upgrade_vfmodule_e2e__target_newest_service_model.json", ServiceModel.class);
    }

    private ServiceInstantiation upgradeVfModulePayload() {
        return readJsonResourceFileAsObject("/payload_jsons/vfmodule/upgrade_vfmodule_e2e__fe_input_cypress.json", ServiceInstantiation.class);
    }

    @Test
    public void deployService_failIt_retryDeploy_getRetryAsTemplate_makeSureFalsyIsFailedInTemplate() {

        final String SERVICE_REQUEST_ID = UUID.randomUUID().toString();

        //push alacarte with 1 vnf, verify STATUS pending
        UUID uuid = pushALaCarteWithVnf();
        singleServicesAndAssertStatus(JobStatus.PENDING, uuid);

        //mock mso to answer 200 of create service instance request, verify STATUS in progress
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith("serviceInstances"), any())).thenReturn(
            createResponse(200, SERVICE_INSTANCE_ID, SERVICE_REQUEST_ID));

        //mock mso to answer FAILED for service instance create
        final RestObject<AsyncRequestStatus> failedResponse = asyncRequestStatusResponseAsRestObject(FAILED_STR);
        final String failureDescription = "Some deep failure";
        failedResponse.get().request.requestStatus.setStatusMessage(failureDescription);
        when(restMso.GetForObject(endsWith(SERVICE_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(failedResponse);

        //Wait till job failed
        processJobsCountTimesAndAssertStatus(uuid, 3, FAILED);

        //make sure retry request jas isFailed = true, and status message is with failureDescription
        ServiceInstantiation retryRequest = asyncInstantiationBL.getBulkForRetry(uuid);
        assertTrue(retryRequest.getIsFailed());
        assertEquals(failureDescription, retryRequest.getStatusMessage());

        //deploy retry job and it's template
        UUID retryUuid = asyncInstantiationBL.pushBulkJob(retryRequest, USER_ID).get(0);
        ServiceInstantiation templateOfRetry = instantiationTemplates.getJobRequestAsTemplate(retryUuid);

        //make sure the template request has isFailed = false, and no status message
        assertFalse(templateOfRetry.getIsFailed());
        assertNull(templateOfRetry.getStatusMessage());
    }

    @Test
    public void oneVnfExistsAddAnotherVnf(){
        final String VNF_REQUEST_ID = UUID.randomUUID().toString();
        final String VNF_INSTANCE_ID = UUID.randomUUID().toString();
        ServiceInstantiation serviceInstantiation = readJsonResourceFileAsObject("/payload_jsons/vnf/one_vnf_exists_add_another_vnf_expected_bulk.json",
            ServiceInstantiation.class);
        List<UUID> uuids = asyncInstantiationBL.pushBulkJob(serviceInstantiation, USER_ID);
        assertThat(uuids, hasSize(1));

        //mock mso to answer 200 of create vnf instance request
        when(restMso.restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), endsWith("e6cc1c4f-05f7-49bc-8e86-ac2eb92baaaa/vnfs"), any())).thenReturn(
            createResponse(200, VNF_INSTANCE_ID, VNF_REQUEST_ID));

        //mock mso to answer msoVnfStatus (COMPLETE) for vnf creation status,
        when(restMso.GetForObject(endsWith(VNF_REQUEST_ID), eq(AsyncRequestStatus.class))).
            thenReturn(asyncRequestStatusResponseAsRestObject(COMPLETE_STR));

        processJobsCountTimesAndAssertStatus(uuids.get(0), 200, COMPLETED);
        verify(restMso, times(1)).restCall(eq(HttpMethod.POST), any(), any(), endsWith("e6cc1c4f-05f7-49bc-8e86-ac2eb92baaaa/vnfs"), any());
        verify(restMso, times(1)).GetForObject(any(), any());
    }

}
