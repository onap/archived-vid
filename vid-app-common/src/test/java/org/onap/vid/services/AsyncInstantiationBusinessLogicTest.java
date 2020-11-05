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

package org.onap.vid.services;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.JsonAssert.whenIgnoringPaths;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
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
import static org.onap.vid.job.Job.JobStatus.STOPPED;
import static org.onap.vid.testUtils.TestUtils.generateRandomAlphaNumeric;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.MockedAaiClientAndFeatureManagerConfig;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.exceptions.MaxRetriesException;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.MsoRequestBuilder;
import org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator;
import org.onap.vid.job.impl.JobDaoImpl;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.Action;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.ResourceInfo;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.MsoOperationalEnvironmentTest;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.RequestStatus;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.DaoUtils;
import org.onap.vid.utils.TimeUtils;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, MockedAaiClientAndFeatureManagerConfig.class})
public class AsyncInstantiationBusinessLogicTest extends AsyncInstantiationBaseTest {

    @Mock
    private JobAdapter jobAdapterMock;

    @Mock
    private JobsBrokerService jobsBrokerServiceMock;

    private AsyncInstantiationRepository asyncInstantiationRepository;

    private AuditService auditService;


    private AsyncInstantiationBusinessLogicImpl asyncInstantiationBL;

    protected MsoRequestBuilder msoRequestBuilder;

    private static final String UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE =
            "Failed to retrieve class .*ServiceInfo with jobId .* from table. no resource found";

    private static final String DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE =
            "Service status does not allow deletion from the queue";

    private String uuidRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private org.hamcrest.Matcher uuidRegexMatcher = is(matchesPattern(uuidRegex));


    @BeforeClass
    void initServicesInfoService() {
        MockitoAnnotations.initMocks(this);
        doReturn(false).when(featureManager).isActive(Features.FLAG_DISABLE_HOMING);
        AsyncInstantiationRepository realAsyncInstantiationRepository = new AsyncInstantiationRepository(dataAccessService);
        asyncInstantiationRepository = spy(realAsyncInstantiationRepository);

        auditService = new AuditServiceImpl(null, asyncInstantiationRepository);

        AsyncInstantiationBusinessLogicImpl realAsyncInstantiationBL = new AsyncInstantiationBusinessLogicImpl(jobAdapterMock, jobsBrokerServiceMock, sessionFactory, aaiClient, featureManager, cloudOwnerService, asyncInstantiationRepository, auditService);
        asyncInstantiationBL = Mockito.spy(realAsyncInstantiationBL);

        msoRequestBuilder = new MsoRequestBuilder(asyncInstantiationBL, cloudOwnerService, aaiClient, featureManager);

        createInstanceParamsMaps();
    }

    @BeforeMethod
    void defineMocks() {
        Mockito.reset(aaiClient);
        Mockito.reset(jobAdapterMock);
        Mockito.reset(jobsBrokerServiceMock);
        Mockito.reset(asyncInstantiationRepository);
        mockAaiClientAnyNameFree();
        enableAddCloudOwnerOnMsoRequest();
    }

    @BeforeMethod
    void resetServiceCount() {
        serviceCount = 0;
    }

    @AfterMethod
    void clearDb() {
        dataAccessService.deleteDomainObjects(JobDaoImpl.class, "1=1", getPropsMap());
        dataAccessService.deleteDomainObjects(ServiceInfo.class, "1=1", getPropsMap());
        dataAccessService.deleteDomainObjects(JobAuditStatus.class, "1=1", getPropsMap());
        dataAccessService.deleteDomainObjects(NameCounter.class, "1=1", getPropsMap());
    }


    private void createNewTestServicesInfoForFilter(String userId) {
        LocalDateTime createdDate, modifiedDate;
        LocalDateTime NOW = LocalDateTime.now();
        UUID uuid;

        // Old job
        uuid = UUID.randomUUID();
        addNewJob(uuid);
        createdDate = NOW.minusYears(1);
        addNewServiceInfo(uuid, userId, "Old", createdDate, createdDate, COMPLETED, false, false,
            MODEL_UUID);

        uuid = UUID.randomUUID();
        addNewJob(uuid);
        createdDate = NOW.minusDays(20);
        modifiedDate = NOW.minusDays(19);
        addNewServiceInfo(uuid, userId, "Hidden", createdDate, modifiedDate, PAUSE, true, false,
            MODEL_UUID);

        createNewTestServicesInfo(String.valueOf(userId));
    }

    private void createNewTestServicesInfo(String userId) {

        LocalDateTime createdDate, modifiedDate;
        LocalDateTime NOW = LocalDateTime.now();
        UUID uuid;

        uuid = UUID.randomUUID();
        addNewJob(uuid);

        createdDate = NOW.minusDays(40);
        addNewServiceInfo(uuid, userId, "service instance 5", createdDate, createdDate, COMPLETED, false, false,
            MODEL_UUID);
        addNewServiceInfo(uuid, userId, "service instance 6", createdDate, createdDate, STOPPED, false, false,
            MODEL_UUID);

        uuid = UUID.randomUUID();
        addNewJob(uuid);

        createdDate = NOW.minusDays(20);
        modifiedDate = NOW.minusDays(10);
        addNewServiceInfo(uuid, userId, "service instance 4", createdDate, modifiedDate, STOPPED, false, false,
            MODEL_UUID);
        addNewServiceInfo(uuid, userId, "service instance 2", createdDate, modifiedDate, COMPLETED, false, false,
            MODEL_UUID);
        addNewServiceInfo(uuid, userId, "service instance 3", createdDate, modifiedDate, PAUSE, false, false,
            MODEL_UUID);

        modifiedDate = NOW.minusDays(19);
        addNewServiceInfo(uuid, userId, "service instance 1", createdDate, modifiedDate, FAILED, false, false,
            MODEL_UUID);


        // Job to a different user
        uuid = UUID.randomUUID();
        addNewJob(uuid);

        createdDate = NOW.minusMonths(2);
        addNewServiceInfo(uuid, "2221", "service instance 7", createdDate, createdDate, COMPLETED, false, false,
            MODEL_UUID);

    }



    private UUID createServicesInfoWithDefaultValues(Job.JobStatus status) {

        LocalDateTime NOW = LocalDateTime.now();
        UUID uuid;

        uuid = UUID.randomUUID();
        addNewJob(uuid, status);

        addNewServiceInfo(uuid, null, "service instance 1", NOW, NOW, status, false, false,
            MODEL_UUID);

        return uuid;

    }

    private List<ServiceInfo> getFullList() {
        List<ServiceInfo> expectedOrderServiceInfo = dataAccessService.getList(ServiceInfo.class, getPropsMap());
        assertThat("Failed to retrieve all predefined services", expectedOrderServiceInfo.size(), equalTo(serviceCount));
        expectedOrderServiceInfo.sort(new ServiceInfoComparator());
        return expectedOrderServiceInfo;
    }



    private LocalDateTime fromDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void setCreateDateToServiceInfo(UUID jobUuid, LocalDateTime createDate) {
        List<ServiceInfo> serviceInfoList = dataAccessService.getList(ServiceInfo.class, getPropsMap());
        DaoUtils.tryWithSessionAndTransaction(sessionFactory, session -> {
            serviceInfoList.stream()
                    .filter(serviceInfo -> jobUuid.equals(serviceInfo.getJobId()))
                    .forEach(serviceInfo -> {
                        serviceInfo.setCreated(toDate(createDate));
                        session.saveOrUpdate(serviceInfo);
                    });
            return 1;
        });
    }

    private void addNewJob(UUID uuid) {
        addNewJob(uuid, null);
    }

    private void addNewJob(UUID uuid, Job.JobStatus status) {
        JobDaoImpl jobDao = new JobDaoImpl();
        jobDao.setUuid(uuid);
        jobDao.setStatus(status);
        dataAccessService.saveDomainObject(jobDao, getPropsMap());
    }

    private ServiceInstantiation addOriginalService(UUID jobId, String userID){
        addNewServiceInfo(jobId, userID, "name", LocalDateTime.now(), LocalDateTime.now(), COMPLETED_WITH_ERRORS, false,
            true,
            MODEL_UUID);
        assertThat(asyncInstantiationRepository.getServiceInfoByJobId(jobId).isRetryEnabled(), is(true));
        ServiceInstantiation originalServiceInstantiation = prepareServiceInstantiation(true, 1);
        doReturn(originalServiceInstantiation).when(asyncInstantiationRepository).getJobRequest(jobId);
        return originalServiceInstantiation;
    }

    private void assertRetryDisabled(UUID jobId){
        assertThat(asyncInstantiationRepository.getServiceInfoByJobId(jobId).isRetryEnabled(), is(false));
    }

    private void assertNewJobExistsAsExpectedAfterRetry(List<UUID> newJobIds, ServiceInstantiation expectedServiceInstantiation, UUID jobId, String userId){
        assertThat(newJobIds, hasSize(1));
        assertThat(newJobIds.get(0), not(equalTo(jobId)));

        ArgumentCaptor<ServiceInstantiation> requestsCaptor = ArgumentCaptor.forClass(ServiceInstantiation.class);
        ArgumentCaptor<UUID> uuidsCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<JobType> jobTypeCaptor = ArgumentCaptor.forClass(JobType.class);

        verify(asyncInstantiationRepository).addJobRequest(uuidsCaptor.capture(), requestsCaptor.capture());
        verify(jobAdapterMock).createServiceInstantiationJob(jobTypeCaptor.capture(), requestsCaptor.capture(), uuidsCaptor.capture(), eq(userId), any(), anyString(), anyInt());
        verify(jobsBrokerServiceMock).add(any());

        requestsCaptor.getAllValues().forEach(x->assertJsonEquals(expectedServiceInstantiation, x, whenIgnoringPaths(
                "trackById",
                "vnfs.2016-73_MOW-AVPN-vPE-BV-L.trackById",
                "vnfs.2016-73_MOW-AVPN-vPE-BV-L.vfModules.201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0.201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0:001.trackById",
                "vnfs.2016-73_MOW-AVPN-vPE-BV-L.vfModules.201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0.201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0:002.trackById"
        )));

    }

    @Test
    public void testServiceInfoAreOrderedAsExpected() {
        int userId = 2222;
        createNewTestServicesInfo(String.valueOf(userId));
        List<ServiceInfo> expectedOrderServiceInfo = getFullList();
        List<ServiceInfo> serviceInfoListResult = asyncInstantiationBL.getAllServicesInfo();
        assertThat("Services aren't ordered as expected", serviceInfoListResult, equalTo(expectedOrderServiceInfo));
    }

    @Test
    public void whenNewServiceInfoCreated_isRetryEnablesIsFalse() {
        UUID uuid = createServicesInfoWithDefaultValues(PENDING);
        assertFalse(asyncInstantiationRepository.getServiceInfoByJobId(uuid).isRetryEnabled());
    }

    @Test
    public void testServiceInfoAreFilteredAsExpected() {
        int userId = 2222;
        createNewTestServicesInfoForFilter(String.valueOf(userId));
        List<ServiceInfo> expectedOrderServiceInfo = getFullList();

        List<ServiceInfo> expectedFilterByUser = expectedOrderServiceInfo.stream().filter(x ->
                !x.getServiceInstanceName().equals("Old") && !x.getServiceInstanceName().equals("Hidden")

        ).collect(Collectors.toList());


        List<ServiceInfo> serviceInfoFilteredByUser = asyncInstantiationBL.getAllServicesInfo();
        assertThat("Services aren't ordered filtered as expected", serviceInfoFilteredByUser, equalTo(expectedFilterByUser));
    }

    @Test(dataProvider = "pauseAndInstanceParams")
    public void createMacroServiceInstantiationMsoRequestUniqueName(Boolean isPause, HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams) throws Exception {
        defineMocks();
        ServiceInstantiation serviceInstantiationPayload = generateMockMacroServiceInstantiationPayload(isPause, createVnfList(vfModuleInstanceParamsMap, vnfInstanceParams, true), null, 2, true, PROJECT_NAME, false);
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_service_request_unique_names.json");
        when(jobAdapterMock.createServiceInstantiationJob(any(), any(), any(), any(), any(), anyString(), any())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return new MockedJob((String)args[5]);
        });

        when(jobsBrokerServiceMock.add(any(MockedJob.class))).thenAnswer((Answer<UUID>) invocation -> {
            Object[] args = invocation.getArguments();
            MockedJob job = (MockedJob) args[0];
            MockedJob.putJob(job.uuid, job);
            return job.getUuid();
        });

        when(asyncInstantiationBL.isPartOfBulk(any())).thenReturn(true);

        List<UUID> uuids = asyncInstantiationBL.pushBulkJob(serviceInstantiationPayload, "az2016");
        for (int i = 0; i < 2; i++) {
            UUID currentUuid = uuids.get(i);
            RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                    msoRequestBuilder.generateMacroServiceInstantiationRequest(currentUuid, serviceInstantiationPayload,
                            MockedJob.getJob(currentUuid).getOptimisticUniqueServiceInstanceName(), "az2016");
            String unique =  i==0 ? "" : String.format("_00%s", i);
            String expected = IOUtils.toString(resource, "UTF-8")
                    .replace("{SERVICE_UNIQENESS}", unique)
                    .replace("{VNF_UNIQENESS}", unique)
                    .replace("{VF_MODULE_UNIQENESS}", unique)
                    .replace("{VF_MODULE_2_UNIQENESS}", unique)
                    .replace("{VG_UNIQUENESS}", unique);
            MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
            Optional<ServiceInfo> optionalServiceInfo = getJobById(currentUuid);
            assertThat(optionalServiceInfo.get().getServiceInstanceName(), equalTo("vPE_Service" + unique));
            verifySearchNodeTypeByName(unique, "vPE_Service", ResourceType.SERVICE_INSTANCE);
            verifySearchNodeTypeByName(unique, VNF_NAME, ResourceType.GENERIC_VNF);
            verifySearchNodeTypeByName(unique, "vmxnjr001_AVPN_base_vPE_BV_base", ResourceType.VF_MODULE);
            verifySearchNodeTypeByName(unique, "vmxnjr001_AVPN_base_vRE_BV_expansion", ResourceType.VF_MODULE);
            verifySearchNodeTypeByName(unique, "myVgName", ResourceType.VOLUME_GROUP);
        }
    }

    protected void verifySearchNodeTypeByName(String unique, String resourceName, ResourceType serviceInstance) {
        String uniqueName = resourceName + unique;
        verify(aaiClient, times(1)).isNodeTypeExistsByName(uniqueName, serviceInstance);
        when(aaiClient.isNodeTypeExistsByName(uniqueName, serviceInstance)).thenReturn(true);
    }




    @DataProvider
    public static Object[][] dataProviderForInstanceNames() {
        return new Object[][]{
                {true, ImmutableList.of("vPE_Service", "vPE_Service_001", "vPE_Service_002")},
                {false, ImmutableList.of("", "", "")},
        };
    }

    @Test(dataProvider="dataProviderForInstanceNames")
    public void pushBulkJob_bulkWithSize3_instancesNamesAreExactlyAsExpected(boolean isUserProvidedNaming, List<String> expectedNames) {
        final ServiceInstantiation request = prepareServiceInstantiation(isUserProvidedNaming, 3);


        asyncInstantiationBL.pushBulkJob(request, "myUserId");

        List<ServiceInfo> serviceInfoList = dataAccessService.getList(ServiceInfo.class, getPropsMap());
        assertEquals(serviceInfoList.stream().map(ServiceInfo::getServiceInstanceName).collect(Collectors.toList()), expectedNames);
    }

    protected ServiceInstantiation prepareServiceInstantiation(String projectName, boolean isUserProvidedNaming, int bulkSize) {
        final ServiceInstantiation request = generateMockMacroServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true),
                createPnfList(), bulkSize, isUserProvidedNaming, projectName, true
        );

        // in "createServiceInstantiationJob()" we will probe the service, with the generated names
        configureMockitoWithMockedJob();
        return request;
    }

    protected ServiceInstantiation prepareServiceInstantiation(boolean isUserProvidedNaming, int bulkSize) {
        return prepareServiceInstantiation(PROJECT_NAME, isUserProvidedNaming, bulkSize);
    }

    @DataProvider
    public static Object[][] dataProviderSummarizedMap() {
        return new Object[][]{
            {"/payload_jsons/templateSummarize4vnfs6vfmodules.json", ImmutableMap.of("vnf", 4L, "vfModule", 6L, "volumeGroup", 1L, "network", 2L)},
            {"/payload_jsons/templateSummarize3Vnfs8Vfmodule2VolumeGroups.json", ImmutableMap.of("vnf", 3L, "vfModule", 8L, "volumeGroup", 2L)},
            {"/payload_jsons/templateSummarize3Networks.json", ImmutableMap.of("network", 3L)},

        };
    }

    @Test(dataProvider = "dataProviderSummarizedMap")
    public void summarizedChildrenMap_givenServiceInstantiation_yieldCorrectMap(String pathInResource, Map<String, Long> expectedMap){
        ServiceInstantiation serviceInstantiation = TestUtils.readJsonResourceFileAsObject(
            pathInResource, ServiceInstantiation.class);
        Map<String, Long> childrenMap =  asyncInstantiationBL.summarizedChildrenMap(serviceInstantiation);
        assertEquals(childrenMap,expectedMap);
    }

    @Test
    public void requestSummaryOrNull_givenActionWhichIsNotCreate_yieldNullRegardlessOfPayload(){
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class);

        when(serviceInstantiation.getAction()).thenReturn(Action.Upgrade);
        when(featureManager.isActive(Features.FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE)).thenReturn(true);

        assertThat(asyncInstantiationBL.requestSummaryOrNull(serviceInstantiation), is(nullValue()));
    }

    @Test
    public void whenPushBulkJob_thenJobRequestIsSaveInJobRequestDb() {
        Mockito.reset(asyncInstantiationRepository);
        int bulkSize = 3;
        final ServiceInstantiation request = prepareServiceInstantiation(true, bulkSize);
        when(jobsBrokerServiceMock.add(any())).thenReturn(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<UUID> jobIds = asyncInstantiationBL.pushBulkJob(request, "abc");

        ArgumentCaptor<JobAdapter.AsyncJobRequest> asyncJobRequestCaptor = ArgumentCaptor.forClass(JobAdapter.AsyncJobRequest.class);
        ArgumentCaptor<ServiceInstantiation> requestsCaptor = ArgumentCaptor.forClass(ServiceInstantiation.class);
        ArgumentCaptor<UUID> uuidsCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(asyncInstantiationRepository, times(bulkSize)).addJobRequest(uuidsCaptor.capture(), requestsCaptor.capture());
        verify(jobsBrokerServiceMock, times(bulkSize)).add(any());
        verify(jobAdapterMock, times(bulkSize)).createServiceInstantiationJob(any(), asyncJobRequestCaptor.capture(), any(), any(), any(), any(), any());

        //verify that all for each job we saved an row in jobRequest table
        assertThat(uuidsCaptor.getAllValues(), containsInAnyOrder(jobIds.toArray()));

        //assert that each real job we created with the adaptor, request is save in jobRequest table
        assertThat(requestsCaptor.getAllValues(), containsInAnyOrder(asyncJobRequestCaptor.getAllValues().toArray()));

        assertThat(requestsCaptor.getAllValues(),everyItem(hasProperty("bulkSize", is(1))));

        //assert that the requests that save in DB are the same as original request expect of the trackById
        requestsCaptor.getAllValues().forEach(x->assertJsonEquals(request, x, whenIgnoringPaths(
                "bulkSize",
                "trackById",
                "vnfs.2016-73_MOW-AVPN-vPE-BV-L.trackById",
                "vnfs.2016-73_MOW-AVPN-vPE-BV-L.vfModules.201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0.201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0:001.trackById",
                "vnfs.2016-73_MOW-AVPN-vPE-BV-L.vfModules.201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0.201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0:002.trackById"
        )));

        //assert that each trackById on all bulk jobs is unique
        Set<String> usedUUID = new HashSet<>();
        requestsCaptor.getAllValues().forEach(x->assertTrackByIdRecursively(x, uuidRegexMatcher, usedUUID));
    }

    @Test
    public void whenRetryJob_prevJobRetryIsDisabled() {
        reset(asyncInstantiationRepository);
        UUID jobId = UUID.randomUUID();
        String userID = generateRandomAlphaNumeric(8);
        addOriginalService(jobId, userID);
        doReturn(mock(Map.class)).when(asyncInstantiationRepository).getResourceInfoByRootJobId(jobId);
        asyncInstantiationBL.retryJob(jobId, userID);
        assertRetryDisabled(jobId);
    }

    @Test
    public void whenRetryJobWithEditedData_prevJobRetryIsDisabled() {
        reset(asyncInstantiationRepository);
        UUID jobId = UUID.randomUUID();
        String userID = generateRandomAlphaNumeric(8);
        addOriginalService(jobId, userID);
        ServiceInstantiation editedServiceInstantiation = prepareServiceInstantiation("editedProjectName", true, 1);
        asyncInstantiationBL.retryJob(editedServiceInstantiation, jobId, userID);
        assertRetryDisabled(jobId);
    }

    @Test
    public void retryJobWithEditedData_expectedNewJobDifferentData() {
        reset(asyncInstantiationRepository);
        UUID jobId = UUID.randomUUID();
        String userID = generateRandomAlphaNumeric(8);
        addOriginalService(jobId, userID);
        ServiceInstantiation editedServiceInstantiation = prepareServiceInstantiation("editedProjectName", true, 1);
        List<UUID> newJobIds =  asyncInstantiationBL.retryJob(editedServiceInstantiation, jobId, userID);
        assertNewJobExistsAsExpectedAfterRetry(newJobIds, editedServiceInstantiation, jobId, userID);
    }

    @Test
    public void retryJob_expectedNewJob() {
        reset(asyncInstantiationRepository);
        UUID jobId = UUID.randomUUID();
        String userID = "az2016";
        ServiceInstantiation originalServiceInstantiation =  addOriginalService(jobId, userID);
        doReturn(mock(Map.class)).when(asyncInstantiationRepository).getResourceInfoByRootJobId(jobId);
        List<UUID> newJobIds = asyncInstantiationBL.retryJob(jobId, userID);
        assertNewJobExistsAsExpectedAfterRetry(newJobIds, originalServiceInstantiation, jobId, userID);
    }

    @Test (dataProvider = "aLaCarteAndMacroPayload")
    public void generateMockServiceInstantiationPayload_serializeBackAndForth_sourceShouldBeTheSame(ServiceInstantiation serviceInstantiationPayload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        final String asString = mapper.writeValueAsString(serviceInstantiationPayload);

        final ServiceInstantiation asObject = mapper.readValue(asString, ServiceInstantiation.class);
        final String asString2 = mapper.writeValueAsString(asObject);

        assertJsonEquals(asString, asString2);
    }

    @DataProvider
    public Object[][] aLaCarteAndMacroPayload() {
        ServiceInstantiation macroPayload = generateMockMacroServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, ImmutableList.of(vnfInstanceParamsMapWithParamsToRemove, vnfInstanceParamsMapWithParamsToRemove), true),
                createPnfList(), 2, false,PROJECT_NAME, false);
        ServiceInstantiation aLaCartePayload = generateALaCarteServiceInstantiationPayload();

        return new Object[][]{
                {macroPayload},
                {aLaCartePayload}
        };
    }

    public static class ServiceInfoComparator implements Comparator<ServiceInfo> {

        @Override
        public int compare(ServiceInfo o1, ServiceInfo o2) {
            int compare;

            compare = o1.getCreatedBulkDate().compareTo(o2.getCreatedBulkDate());
            if (compare != 0) {
                return -compare;
            }

            // check jobStatus priority
            int o1Priority = getPriority(o1);
            int o2Priority = getPriority(o2);
            compare = o1Priority - o2Priority;
            if (compare != 0) {
                return compare;
            }

            // check statusModifiedDate
            return o1.getStatusModifiedDate().compareTo(o2.getStatusModifiedDate());
        }

        private int getPriority(ServiceInfo o) throws JSONException {
            Job.JobStatus status = o.getJobStatus();
            switch (status) {
                case COMPLETED:
                case FAILED:
                    return 1;
                case IN_PROGRESS:
                    return 2;
                case PAUSE:
                    return 3;
                case STOPPED:
                case PENDING:
                    return 4;
                default:
                    return 5;
            }
        }
    }

    @DataProvider
    public Object[][] pauseAndInstanceParams() {
        return new Object[][]{
                {Boolean.TRUE, instanceParamsMapWithoutParams, Collections.EMPTY_LIST},
                {Boolean.FALSE, instanceParamsMapWithoutParams, Collections.EMPTY_LIST},
                {Boolean.TRUE, vfModuleInstanceParamsMapWithParamsToRemove, Collections.singletonList(vnfInstanceParamsMapWithParamsToRemove)}
        };
    }

    @Test
    public void testUpdateServiceInfo_WithExistingServiceInfo_ServiceInfoIsUpdated() {
        UUID uuid = createFakedJobAndServiceInfo();
        final String STEPH_CURRY = "Steph Curry";
        asyncInstantiationBL.updateServiceInfo(uuid, x -> {
            x.setServiceInstanceName(STEPH_CURRY);
            x.setJobStatus(Job.JobStatus.IN_PROGRESS);
        });
        Optional<ServiceInfo> optionalServiceInfo = getJobById(uuid);
        assertThat(optionalServiceInfo.get().getServiceInstanceName(), equalTo(STEPH_CURRY));
        assertThat(optionalServiceInfo.get().getJobStatus(), equalTo(Job.JobStatus.IN_PROGRESS));
    }

    private Optional<ServiceInfo> getJobById(UUID jobId) {
        List<ServiceInfo> serviceInfoList = dataAccessService.getList(ServiceInfo.class, null);
        return serviceInfoList.stream().filter(x -> jobId.equals(x.getJobId())).findFirst();
    }

    private UUID createFakedJobAndServiceInfo() {
        UUID uuid = UUID.randomUUID();
        addNewJob(uuid);
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceInstanceName("Lebron James");
        serviceInfo.setJobId(uuid);
        serviceInfo.setJobStatus(Job.JobStatus.PENDING);
        dataAccessService.saveDomainObject(serviceInfo, getPropsMap());
        return uuid;
    }

    @Test(expectedExceptions = NotFoundException.class, expectedExceptionsMessageRegExp = UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE)
    public void testUpdateServiceInfo_WithNonExisting_ThrowException() {
        asyncInstantiationBL.updateServiceInfo(UUID.randomUUID(), x -> x.setServiceInstanceName("not matter"));
    }

    @Test(expectedExceptions = NotFoundException.class, expectedExceptionsMessageRegExp = UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE)
    public void testUpdateServiceInfo_WithDoubleServiceWithSameJobUuid_ThrowException() {
        UUID uuid = createFakedJobAndServiceInfo();
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setJobId(uuid);
        dataAccessService.saveDomainObject(serviceInfo, getPropsMap());
        asyncInstantiationBL.updateServiceInfo(UUID.randomUUID(), x -> x.setServiceInstanceName("not matter"));
    }


    @DataProvider
    public static Object[][] isPauseAndPropertyDataProvider() {
        return new Object[][]{
                {true, "mso.restapi.serviceInstanceAssign"},
                {false, "mso.restapi.service.instance"},
        };
    }


    @Test(dataProvider = "isPauseAndPropertyDataProvider")
    public void testServiceInstantiationPath_RequestPathIsAsExpected(boolean isPause, String expectedProperty) {
        ServiceInstantiation serviceInstantiationPauseFlagTrue = generateMacroMockServiceInstantiationPayload(isPause, createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true), createPnfList());
        String path = asyncInstantiationBL.getServiceInstantiationPath(serviceInstantiationPauseFlagTrue);
        Assert.assertEquals(path, SystemProperties.getProperty(expectedProperty));
    }

    @Test
    public void testCreateVnfEndpoint_useProvidedInstanceId() {
        String path = asyncInstantiationBL.getVnfInstantiationPath("myGreatId");
        assertThat(path, equalTo("/serviceInstantiation/v7/serviceInstances/myGreatId/vnfs"));
    }



    @Test
    public void pushBulkJob_macroServiceverifyCreatedDateBehavior_createdDateIsTheSameForAllServicesInSameBulk() {
        LocalDateTime startTestDate = LocalDateTime.now().withNano(0);
        final ServiceInstantiation request = generateMockMacroServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true),
                createPnfList(), 100, true,PROJECT_NAME, true
        );

        pushJobAndAssertDates(startTestDate, request);
    }

    @Test
    public void whenCreateServiceInfo_thenModelId_isModelVersionId() {
        ServiceInfo serviceInfo = asyncInstantiationBL.createServiceInfo("userID",
                generateALaCarteWithVnfsServiceInstantiationPayload(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Date(),
                "myName", ServiceInfo.ServiceAction.INSTANTIATE, null);
        assertEquals(SERVICE_MODEL_VERSION_ID, serviceInfo.getServiceModelId());

    }

    @Test
    public void pushBulkJob_aLaCarteServiceverifyCreatedDateBehavior_createdDateIsTheSameForAllServicesInSameBulk() {
        LocalDateTime startTestDate = LocalDateTime.now().withNano(0);
        final ServiceInstantiation request = generateALaCarteServiceInstantiationPayload();
        pushJobAndAssertDates(startTestDate, request);
    }

    protected void pushJobAndAssertDates(LocalDateTime startTestDate, ServiceInstantiation request) {
        // in "createServiceInstantiationJob()" we will probe the service, with the generated names
        configureMockitoWithMockedJob();

        asyncInstantiationBL.pushBulkJob(request, "myUserId");
        List<ServiceInfo> serviceInfoList = dataAccessService.getList(ServiceInfo.class, getPropsMap());

        List<Date> creationDates = new ArrayList<>();
        for (ServiceInfo serviceInfo : serviceInfoList) {
            creationDates.add(serviceInfo.getCreatedBulkDate());
        }
        LocalDateTime endTestDate = LocalDateTime.now();

        //creation date of all services is the same
        Assert.assertTrue(creationDates.stream().distinct().count() <= 1);
        LocalDateTime creationDate = fromDate(creationDates.get(0));
        assertFalse(creationDate.isBefore(startTestDate));
        assertFalse(creationDate.isAfter(endTestDate));
    }

    protected void configureMockitoWithMockedJob() {
        Mockito.reset(jobAdapterMock);
        final Job job = mock(Job.class);
        when(job.getStatus()).thenReturn(PENDING);
        when(job.getUuid()).thenReturn(UUID.fromString("db2c5ed9-1c19-41ce-9cb7-edf0d878cdeb"));
        when(jobAdapterMock.createServiceInstantiationJob(any(), any(), any(), any(), any(), any(), any())).thenReturn(job);
        when(jobsBrokerServiceMock.add(job)).thenReturn(UUID.randomUUID());
    }

    @DataProvider
    public static Object[][] msoToJobStatusDataProvider() {
        return new Object[][]{
                {"IN_PROGRESS", JobStatus.IN_PROGRESS},
                {"INPROGRESS", JobStatus.IN_PROGRESS},
                {"IN ProGREsS", JobStatus.IN_PROGRESS},
                {"JAMES_HARDEN", JobStatus.IN_PROGRESS},
                {"FAILED", JobStatus.FAILED},
                {"COMpleTE", JobStatus.COMPLETED},
                {"PENDING", JobStatus.IN_PROGRESS},
                {"Paused", JobStatus.PAUSE},
                {"Pause", JobStatus.PAUSE},
                {"PENDING_MANUAL_TASK", JobStatus.PAUSE},
                {"UNLOCKED", JobStatus.IN_PROGRESS},
                {"AbORtEd", COMPLETED_WITH_ERRORS},
                {"RoLlED_baCK", FAILED},
                {"ROllED_BAcK_To_ASsIGnED", FAILED},
                {"rOLLED_bACK_tO_CrEATeD", FAILED},
        };
    }

    @Test(dataProvider = "msoToJobStatusDataProvider")
    public void whenGetStatusFromMso_calcRightJobStatus(String msoStatus, Job.JobStatus expectedJobStatus) {
        AsyncRequestStatus asyncRequestStatus = asyncRequestStatusResponse(msoStatus);
        assertThat(asyncInstantiationBL.calcStatus(asyncRequestStatus), equalTo(expectedJobStatus));
    }

    @DataProvider
    public static Object[][] msoRequestStatusFiles(Method test) {
        return new Object[][]{
                {"/responses/mso/orchestrationRequestsServiceInstance.json"},
                {"/responses/mso/orchestrationRequestsVnf.json"},
                {"/responses/mso/orchestrationRequestsMockedMinimalResponse.json"}
        };
    }

    @Test(dataProvider="msoRequestStatusFiles")
    public void verifyAsyncRequestStatus_canBeReadFromSample(String msoResponseFile) throws IOException {
        AsyncRequestStatus asyncRequestStatus = TestUtils.readJsonResourceFileAsObject(
                msoResponseFile,
                AsyncRequestStatus.class);
        assertThat(asyncRequestStatus.request.requestStatus.getRequestState(), equalTo("COMPLETE"));
    }

    @Test
    public void deleteJobInfo_pending_deleted() {
        doNothing().when(jobsBrokerServiceMock).delete(any());
        UUID uuid = createServicesInfoWithDefaultValues(PENDING);
        asyncInstantiationBL.deleteJob(uuid);
        assertNotNull(asyncInstantiationRepository.getServiceInfoByJobId(uuid).getDeletedAt(), "service info wasn't deleted");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE)
    public void deleteJobInfo_notAllowdStatus_shouldSendError() {
        UUID uuid = createServicesInfoWithDefaultValues(COMPLETED);
        doThrow(new IllegalStateException(DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE)).when(jobsBrokerServiceMock).delete(any());
        try {
            asyncInstantiationBL.deleteJob(uuid);
        } catch (Exception e) {
            assertNull(asyncInstantiationRepository.getServiceInfoByJobId(uuid).getDeletedAt(), "service info shouldn't deleted");
            throw e;
        }
    }

    @DataProvider
    public Object[][] jobStatusesFinal() {
        return Arrays.stream(Job.JobStatus.values())
                .filter(t -> ImmutableList.of(COMPLETED, FAILED, STOPPED).contains(t))
                .map(v -> new Object[]{v}).collect(Collectors.toList()).toArray(new Object[][]{});
    }

    @Test(dataProvider = "jobStatusesFinal")
    public void whenHideService_theServiceNotReturnedInServiceList(JobStatus jobStatus) {
        UUID uuidToHide = createServicesInfoWithDefaultValues(jobStatus);
        UUID uuidToShown = createServicesInfoWithDefaultValues(jobStatus);
        List<UUID> serviceInfoList = listServicesUUID();
        assertThat(serviceInfoList, hasItems(uuidToHide, uuidToShown));

        asyncInstantiationBL.hideServiceInfo(uuidToHide);
        serviceInfoList = listServicesUUID();
        assertThat(serviceInfoList, hasItem(uuidToShown));
        assertThat(serviceInfoList, not(hasItem(uuidToHide)));

    }

    protected List<UUID> listServicesUUID() {
        return asyncInstantiationBL.getAllServicesInfo().stream().map(ServiceInfo::getJobId).collect(Collectors.toList());
    }

    @DataProvider
    public Object[][] jobStatusesNotFinal() {
        return Arrays.stream(Job.JobStatus.values())
                .filter(t -> ImmutableList.of(PENDING, IN_PROGRESS, PAUSE).contains(t))
                .map(v -> new Object[]{v}).collect(Collectors.toList()).toArray(new Object[][]{});
    }

    @Test(dataProvider = "jobStatusesNotFinal",
            expectedExceptions = OperationNotAllowedException.class,
            expectedExceptionsMessageRegExp = "jobId.*Service status does not allow hide service, status = .*")
    public void hideServiceInfo_notAllowedStatus_shouldSendError(JobStatus jobStatus) {
        UUID uuid = createServicesInfoWithDefaultValues(jobStatus);
        try {
            asyncInstantiationBL.hideServiceInfo(uuid);
        } catch (Exception e) {
            assertFalse(asyncInstantiationRepository.getServiceInfoByJobId(uuid).isHidden(), "service info shouldn't be hidden");
            throw e;
        }
    }

    @Test
    public void whenUseGetCounterInMultiThreads_EachThreadGetDifferentCounter() throws InterruptedException {
        int SIZE = 200;
        ExecutorService executor = Executors.newFixedThreadPool(SIZE);
        List<Callable<Integer>> tasks = IntStream.rangeClosed(0, SIZE)
                .mapToObj(x-> ((Callable<Integer>)() -> asyncInstantiationBL.getCounterForName("a")))
                .collect(Collectors.toList());
        Set<Integer> expectedResults = IntStream.rangeClosed(0, SIZE).boxed().collect(Collectors.toSet());
        executor.invokeAll(tasks)
                .forEach(future -> {
                    try {
                        assertTrue( expectedResults.remove(future.get()), "got unexpected counter");
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        assertThat(expectedResults.size(), is(0));
    }

    @Test
    public void whenUseGetCounterForSameName_numbersReturnedByOrder() {

        String name = UUID.randomUUID().toString();
        int SIZE=10;
        for (int i=0; i<SIZE; i++) {
            assertThat(asyncInstantiationBL.getCounterForName(name), is(i));
        }
    }

    @Test
    public void whenNamedInUsedInAai_getNextNumber() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.GENERIC_VNF;
        when(aaiClient.isNodeTypeExistsByName(name, type)).thenReturn(true);
        when(aaiClient.isNodeTypeExistsByName(name+"_001", type)).thenReturn(false);
        assertThat(asyncInstantiationBL.getUniqueName(name, type), equalTo(name+"_001"));
    }

    @Test(enabled = false) //skip till we will handle macro bulk again...
    public void whenNamedNotInUsedInAai_getSameNameTwice() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.GENERIC_VNF;
        when(aaiClient.isNodeTypeExistsByName(name, type)).thenReturn(false);
        assertThat(asyncInstantiationBL.getUniqueName(name, type), equalTo(name));
        assertThat(asyncInstantiationBL.getUniqueName(name, type), equalTo(name));
        when(aaiClient.isNodeTypeExistsByName(name, type)).thenReturn(true);
        assertThat(asyncInstantiationBL.getUniqueName(name, type), equalTo(name+"_001"));
    }

    private String someCommonStepsAndGetName() {
        mockAaiClientAaiStatusOK();
        return UUID.randomUUID().toString();
    }

    @Test(expectedExceptions= ExceptionWithRequestInfo.class)
    public void whenAaiBadResponseCode_throwInvalidAAIResponseException() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.SERVICE_INSTANCE;
        when(aaiClient.isNodeTypeExistsByName(name, type)).thenThrow(aaiNodeQueryBadResponseException());
        asyncInstantiationBL.getUniqueName(name, type);
    }

    @Test(expectedExceptions=MaxRetriesException.class)
    public void whenAaiAlwaysReturnNameUsed_throwInvalidAAIResponseException() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.VF_MODULE;
        when(aaiClient.isNodeTypeExistsByName(any(), eq(type))).thenReturn(true);
        asyncInstantiationBL.setMaxRetriesGettingFreeNameFromAai(10);
        asyncInstantiationBL.getUniqueName(name, type);
    }

    @Test
    public void testFormattingOfNameAndCounter() {
        AsyncInstantiationBusinessLogicImpl bl = (AsyncInstantiationBusinessLogicImpl) asyncInstantiationBL;
        assertThat(bl.formatNameAndCounter("x", 0), equalTo("x"));
        assertThat(bl.formatNameAndCounter("x", 3), equalTo("x_003"));
        assertThat(bl.formatNameAndCounter("x", 99), equalTo("x_099"));
        assertThat(bl.formatNameAndCounter("x", 100), equalTo("x_100"));
        assertThat(bl.formatNameAndCounter("x", 1234), equalTo("x_1234"));
    }

    @Test
    public void pushBulkJob_verifyAlacarteFlow_useALaCartServiceInstantiationJobType(){
        final ServiceInstantiation request = generateALaCarteServiceInstantiationPayload();

        // in "createServiceInstantiationJob()" we will probe the service, with the generated names
        configureMockitoWithMockedJob();

        ArgumentCaptor<JobType> argumentCaptor = ArgumentCaptor.forClass(JobType.class);
        asyncInstantiationBL.pushBulkJob(request, "myUserId");
        verify(jobAdapterMock).createServiceInstantiationJob(argumentCaptor.capture(),any(),any(),anyString(), anyString(),  anyString(), anyInt());
        assertTrue(argumentCaptor.getValue().equals(JobType.ALaCarteServiceInstantiation));
    }

    @Test
    public void pushBulkJob_verifyMacroFlow_useMacroServiceInstantiationJobType(){
        final ServiceInstantiation request = generateMacroMockServiceInstantiationPayload(false, Collections.emptyMap(), Collections.emptyMap());

        // in "createServiceInstantiationJob()" we will probe the service, with the generated names
        configureMockitoWithMockedJob();

        ArgumentCaptor<JobType> argumentCaptor = ArgumentCaptor.forClass(JobType.class);
        asyncInstantiationBL.pushBulkJob(request, "myUserId");
        verify(jobAdapterMock).createServiceInstantiationJob(argumentCaptor.capture(),any(),any(),anyString(), any(),  anyString(), anyInt());
        assertTrue(argumentCaptor.getValue().equals(JobType.MacroServiceInstantiation));
    }



    @Test
    public void getALaCarteServiceDeletionPath_verifyPathIsAsExpected() {

        String expected = "/serviceInstantiation/v7/serviceInstances/f36f5734-e9df-4fbf-9f35-61be13f028a1";

        String result = asyncInstantiationBL.getServiceDeletionPath("f36f5734-e9df-4fbf-9f35-61be13f028a1");

        assertThat(expected,equalTo(result));
    }

    @Test
    public void getResumeRequestPath_verifyPathIsAsExpected() {

        String expected = "/orchestrationRequests/v7/rq1234d1-5a33-55df-13ab-12abad84e333/resume";

        String result = asyncInstantiationBL.getResumeRequestPath("rq1234d1-5a33-55df-13ab-12abad84e333");

        assertThat(expected, equalTo(result));
    }

    @Test
    public void getInstanceGroupsDeletionPath_verifyPathIsAsExpected()  {
        assertEquals(asyncInstantiationBL.getInstanceGroupDeletePath("9aada4af-0f9b-424f-ae21-e693bd3e005b"),
                "/serviceInstantiation/v7/instanceGroups/9aada4af-0f9b-424f-ae21-e693bd3e005b");
    }

    @Test
    public void whenLcpRegionNotEmpty_thenCloudRegionIdOfResourceIsLegacy() {
        String legacyCloudRegion = "legacyCloudRegion";
        Vnf vnf = new Vnf(new ModelInfo(), null, null, Action.Create.name(), null, "anyCloudRegion", legacyCloudRegion,
                null, null, null, false, null, null, UUID.randomUUID().toString(), null, null, null, "originalName");
        assertThat(vnf.getLcpCloudRegionId(), equalTo(legacyCloudRegion));
    }

    @Test
    public void whenLcpRegionNotEmpty_thenCloudRegionIdOfServiceIsLegacy() {
        String legacyCloudRegion = "legacyCloudRegion";
        ServiceInstantiation service = new ServiceInstantiation(new ModelInfo(), null, null, null, null, null, null,
                null, null, "anyCloudRegion", legacyCloudRegion, null, null, null, null, null, null, null, null, null, null,
                false, 1,false, false, null, null, Action.Create.name(), UUID.randomUUID().toString(), null, null, null, "originalName");
        assertThat(service.getLcpCloudRegionId(), equalTo(legacyCloudRegion));
    }

    @DataProvider
    public static Object[][] getJobTypeByRequest_verifyResultAsExpectedDataProvider() {
        return new Object[][]{
                {false, Action.Create, JobType.MacroServiceInstantiation},
                {true, Action.Create, JobType.ALaCarteServiceInstantiation},
                {true, Action.Delete, JobType.ALaCarteService},
        };
    }

    @Test(dataProvider = "getJobTypeByRequest_verifyResultAsExpectedDataProvider")
    public void getJobTypeByRequest_verifyResultAsExpected(boolean isALaCarte, Action action, JobType expectedJobType) {
        ServiceInstantiation service = createServiceWithIsALaCarteAndAction(isALaCarte, action);
        assertThat(asyncInstantiationBL.getJobType(service), equalTo(expectedJobType));
    }

    @NotNull
    protected ServiceInstantiation createServiceWithIsALaCarteAndAction(boolean isALaCarte, Action action) {
        return new ServiceInstantiation(new ModelInfo(), null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                false, 1, false, isALaCarte, null, null, action.name(),
                UUID.randomUUID().toString(), null, null, null, "originalName");
    }

    @DataProvider
    public static Object[][] isRetryEnabledForStatusDataProvider(Method test) {
        return new Object[][]{
                {FAILED, true, true},
                {COMPLETED_WITH_ERRORS, true, true},
                {COMPLETED_WITH_NO_ACTION, true, false},
                {COMPLETED, true, false},
                {IN_PROGRESS, true, false},
                {FAILED, false, false},
                {COMPLETED_WITH_ERRORS, false, false},
                {COMPLETED, false, false},
        };
    }

    @Test(dataProvider = "isRetryEnabledForStatusDataProvider")
    public void whenUpdateServiceInfoAndAuditStatus_thenServiceInfoRowIsUpdatedAndIsRetryIsRight(
            JobStatus jobStatus, boolean isRetryfeatureEnabled, boolean expectedIsRetry) {
        when(featureManager.isActive(Features.FLAG_1902_RETRY_JOB)).thenReturn(isRetryfeatureEnabled);
        UUID uuid = createFakedJobAndServiceInfo();
        asyncInstantiationBL.updateServiceInfoAndAuditStatus(uuid, jobStatus);
        ServiceInfo serviceInfo = ((List<ServiceInfo>)dataAccessService.getList(ServiceInfo.class, getPropsMap())).
                stream().filter(x->x.getJobId().equals(uuid)).findFirst().get();
        assertEquals(jobStatus, serviceInfo.getJobStatus());

        //we don't test serviceInfo.getStatusModifiedDate() because it's too complicated

        assertEquals(expectedIsRetry, serviceInfo.isRetryEnabled());
    }

    @Test
    public void givenServiceWithNullTrackByIds_whenReplaceTrackByIds_thenAllLevelsHasTrackByIdWithUUID() {
        ServiceInstantiation serviceInstantiation = FakeResourceCreator.createServiceWith2InstancesInEachLevel(Action.Create);
        //assert for the given that all trackById are null
        assertTrackByIdRecursively(serviceInstantiation, is(nullValue()), new HashSet<>());
        ServiceInstantiation modifiedServiceInstantiation = asyncInstantiationBL.prepareServiceToBeUnique(serviceInstantiation);
        assertTrackByIdRecursively(modifiedServiceInstantiation, uuidRegexMatcher, new HashSet<>());
    }

    private void assertTrackByIdRecursively(BaseResource baseResource, org.hamcrest.Matcher matcher, Set<String> usedUuids) {
        assertThat(baseResource.getTrackById(), matcher);
        if (baseResource.getTrackById()!=null) {
            assertThat(usedUuids, not(hasItem(baseResource.getTrackById())));
            usedUuids.add(baseResource.getTrackById());
        }
        baseResource.getChildren().forEach(x->assertTrackByIdRecursively(x, matcher, usedUuids));
    }

    @Test
    public void givenServicefromDB_returnsTheBulkRequest() throws IOException {
        ServiceInstantiation serviceInstantiation = TestUtils.readJsonResourceFileAsObject("/payload_jsons/VnfGroupCreate3Delete1None1Request.json", ServiceInstantiation.class);
        UUID jobId = UUID.randomUUID();
        doReturn(serviceInstantiation).when(asyncInstantiationRepository).getJobRequest(jobId);
        doReturn(mock(Map.class)).when(asyncInstantiationRepository).getResourceInfoByRootJobId(jobId);
        ServiceInstantiation modifiedServiceInstantiation = asyncInstantiationBL.getBulkForRetry(jobId);
        assertThat(modifiedServiceInstantiation, jsonEquals(serviceInstantiation).when(IGNORING_ARRAY_ORDER));
    }

    @Test
    public void givenServiceFromDB_returnsResolvedData() throws IOException {
        ServiceInstantiation serviceInstantiation = TestUtils.readJsonResourceFileAsObject("/payload_jsons/VnfGroupCreate3Delete1None1Request.json", ServiceInstantiation.class);
        ServiceInstantiation expectedServiceInstantiation = TestUtils.readJsonResourceFileAsObject("/payload_jsons/VnfGroupCreate3Delete1None1RequestResolvedForRetry.json", ServiceInstantiation.class);
        UUID jobId = UUID.randomUUID();
        AsyncRequestStatus asyncRequestStatus = TestUtils.readJsonResourceFileAsObject(
                "/responses/mso/orchestrationRequestsVnf.json",
                AsyncRequestStatus.class);
        Map<String, ResourceInfo> mockedResourceInfoMap = ImmutableMap.of(
                "groupingservicefortest..ResourceInstanceGroup..0:001", new ResourceInfo("groupingservicefortest..ResourceInstanceGroup..0:001",jobId,"VNF_GROUP1_INSTANCE_ID", COMPLETED, asyncRequestStatus),// TODO case: delete completed
                "ag5aav86u4j", new ResourceInfo("ag5aav86u4j",jobId, null, FAILED, asyncRequestStatus),// case: failed
                "asedrftjko", new ResourceInfo("asedrftjko",jobId, "VNF_GROUP1_INSTANCE_ID_3", COMPLETED, asyncRequestStatus),//case: completed after retry failed
                "rgedfdged4", new ResourceInfo("rgedfdged4", jobId,"VNF_GROUP1_INSTANCE_ID_4", COMPLETED, asyncRequestStatus ));// case: create completed

        doReturn(mockedResourceInfoMap).when(asyncInstantiationRepository).getResourceInfoByRootJobId(jobId);
        ServiceInstantiation modifiedServiceInstantiation = asyncInstantiationBL.enrichBulkForRetry(serviceInstantiation,jobId);
        assertThat(modifiedServiceInstantiation, jsonEquals(expectedServiceInstantiation).when(IGNORING_ARRAY_ORDER));
    }

    @DataProvider
    public static Object[][] readStatusMsgDataProvider(Method test) throws IOException {
        AsyncRequestStatus asyncRequestStatus = TestUtils.readJsonResourceFileAsObject(
                "/responses/mso/orchestrationRequestsVnf.json",
                AsyncRequestStatus.class);
        return new Object[][]{
                {null, null},
                {new AsyncRequestStatus(), null},
                {new AsyncRequestStatus(new AsyncRequestStatus.Request()), null},
                {new AsyncRequestStatus(new AsyncRequestStatus.Request(new RequestStatus())), null},
                {asyncRequestStatus, "Vnf has been created successfully."}
        };
    }

    @Test(dataProvider = "readStatusMsgDataProvider")
    public void resourceInfoReadStatusMsg_returnsStatusMsgOrNull(AsyncRequestStatus asyncRequestStatus, String expected) {
        ResourceInfo resourceInfo = new ResourceInfo("groupingservicefortest..ResourceInstanceGroup..0:001",UUID.randomUUID(),"VNF_GROUP1_INSTANCE_ID", COMPLETED, asyncRequestStatus);
        String msg= asyncInstantiationBL.readStatusMsg(resourceInfo);
        assertThat(msg, equalTo( expected));
    }

    @Test
    public void testAddResourceInfoForOkResponse() {
        reset(asyncInstantiationRepository);
        String serviceInstanceId = "service-instance-id";
        UUID jobUuid = UUID.randomUUID();

        asyncInstantiationBL.addResourceInfo(prepareSharedDataForAddResourceInfo(jobUuid), JobStatus.IN_PROGRESS, serviceInstanceId);

        ArgumentCaptor<ResourceInfo> resourceInfoCaptor = ArgumentCaptor.forClass(ResourceInfo.class);
        verify(asyncInstantiationRepository).saveResourceInfo(resourceInfoCaptor.capture());

        ResourceInfo resourceInfo = resourceInfoCaptor.getValue();
        assertResourceInfoValues(resourceInfo, serviceInstanceId, jobUuid, JobStatus.IN_PROGRESS);
        assertThat(resourceInfo.getErrorMessage(), is(nullValue()));
    }

    private JobSharedData prepareSharedDataForAddResourceInfo(UUID jobUuid) {
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class);
        when(serviceInstantiation.getTrackById()).thenReturn("track-by-id");
        return new JobSharedData(jobUuid, "", serviceInstantiation, "");
    }

    private void assertResourceInfoValues(ResourceInfo resourceInfo, String serviceInstanceId, UUID jobUuid, JobStatus jobStatus) {
        assertThat(resourceInfo.getInstanceId(), equalTo(serviceInstanceId));
        assertThat(resourceInfo.getJobStatus(), equalTo(jobStatus));
        assertThat(resourceInfo.getRootJobId(), equalTo(jobUuid));
        assertThat(resourceInfo.getTrackById(), equalTo("track-by-id"));
    }

    @DataProvider
    public static Object[][] addResourceInfoWithError() {
        String message = "Failed to create service instance";
        return new Object[][]{
                {500, message},
                {400, "{\"requestError\":{\"serviceException\":{\"messageId\":\"SVC0002\",\"text\":\"" + message + "\"}}}"},
                {199, "{\"serviceException\":{\"messageId\":\"SVC2000\",\"text\":\"Error: " + message + "\"}}"},
        };
    }

    @Test(dataProvider = "addResourceInfoWithError")
    public void testAddResourceInfoForErrorResponse(int errorCode, String errorMessage) {
        reset(asyncInstantiationRepository);
        UUID jobUuid = UUID.randomUUID();

        RestObject restObject = mock(RestObject.class);
        when(restObject.getStatusCode()).thenReturn(errorCode);
        when(restObject.getRaw()).thenReturn(errorMessage);
        asyncInstantiationBL.addFailedResourceInfo(prepareSharedDataForAddResourceInfo(jobUuid), restObject);

        ArgumentCaptor<ResourceInfo> resourceInfoCaptor = ArgumentCaptor.forClass(ResourceInfo.class);
        verify(asyncInstantiationRepository).saveResourceInfo(resourceInfoCaptor.capture());

        ResourceInfo resourceInfo = resourceInfoCaptor.getValue();
        assertResourceInfoValues(resourceInfo, null, jobUuid, JobStatus.FAILED);
        assertThat(resourceInfo.getErrorMessage().request.requestStatus.getStatusMessage(), containsString("Failed to create service instance"));
        assertThat(resourceInfo.getErrorMessage().request.requestStatus.getStatusMessage(), containsString(String.valueOf(errorCode)));
        ZonedDateTime parsedDate = TimeUtils.parseZonedDateTime(resourceInfo.getErrorMessage().request.requestStatus.getTimestamp());
        assertThat(parsedDate.toLocalDate(), is(LocalDate.now()));

        doReturn(resourceInfo).when(asyncInstantiationRepository).getResourceInfoByTrackId(any());
        JobAuditStatus jobAuditStatus = auditService.getResourceAuditStatus(resourceInfo.getTrackById());
        assertThat(jobAuditStatus.getJobStatus(), equalTo("FAILED"));
        assertThat(jobAuditStatus.getAdditionalInfo(), containsString("Failed to create service instance"));
        assertThat(jobAuditStatus.getAdditionalInfo(), containsString(String.valueOf(errorCode)));
        assertTrue(DateUtils.isSameDay(jobAuditStatus.getCreatedDate(), new Date()));
    }

    @DataProvider
    public static Object[][] updateResourceInfoParameters() {
        return new Object[][] {
                {JobStatus.COMPLETED, "Instance was created successfully"},
                {JobStatus.FAILED, "Failed to create instance"}
        };
    }

    @Test(dataProvider = "updateResourceInfoParameters")
    public void testUpdateResourceInfo(JobStatus jobStatus, String message) {
        reset(asyncInstantiationRepository);
        UUID jobUuid = UUID.randomUUID();
        JobSharedData sharedData = new JobSharedData(jobUuid, "", mock(ServiceInstantiation.class),"");

        ResourceInfo resourceInfoMock = new ResourceInfo();
        resourceInfoMock.setTrackById(UUID.randomUUID().toString());
        doReturn(resourceInfoMock).when(asyncInstantiationRepository).getResourceInfoByTrackId(any());

        AsyncRequestStatus asyncRequestStatus = asyncInstantiationBL.convertMessageToAsyncRequestStatus(message);

        asyncInstantiationBL.updateResourceInfo(sharedData, jobStatus, asyncRequestStatus);

        ArgumentCaptor<ResourceInfo> resourceInfoCaptor = ArgumentCaptor.forClass(ResourceInfo.class);
        verify(asyncInstantiationRepository).saveResourceInfo(resourceInfoCaptor.capture());

        ResourceInfo resourceInfo = resourceInfoCaptor.getValue();
        assertThat(resourceInfo.getJobStatus(), equalTo(jobStatus));
        if (jobStatus == JobStatus.FAILED) {
            assertThat(resourceInfo.getErrorMessage(), is(not(nullValue())));
            assertThat(resourceInfo.getErrorMessage().request.requestStatus.getStatusMessage(), equalTo(message));
            ZonedDateTime parsedDate = TimeUtils.parseZonedDateTime(resourceInfo.getErrorMessage().request.requestStatus.getTimestamp());
            assertThat(parsedDate.toLocalDate(), is(LocalDate.now()));
        } else {
            assertThat(resourceInfo.getErrorMessage(), is(nullValue()));
        }

        JobAuditStatus jobAuditStatus = auditService.getResourceAuditStatus(resourceInfo.getTrackById());
        if (jobStatus == JobStatus.FAILED) {
            assertThat(jobAuditStatus.getJobStatus(), equalTo("FAILED"));
            assertThat(jobAuditStatus.getAdditionalInfo(), equalTo(message));
        } else {
            assertThat(jobAuditStatus, is(nullValue()));
        }

    }

    static class MockedJob implements Job {

        private static Map<UUID, MockedJob> uuidToJob = new HashMap<>();

        public static void putJob(UUID uuid, MockedJob job) {
            uuidToJob.put(uuid, job);
        }

        public static MockedJob getJob(UUID uuid) {
            return uuidToJob.get(uuid);
        }


        private String optimisticUniqueServiceInstanceName;

        public MockedJob(String optimisticUniqueServiceInstanceName) {
            this.optimisticUniqueServiceInstanceName = optimisticUniqueServiceInstanceName;
        }

        private UUID uuid = UUID.randomUUID();

        @Override
        public UUID getUuid() {
            return uuid;
        }

        @Override
        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public JobStatus getStatus() {
            return JobStatus.PENDING;
        }

        @Override
        public void setStatus(JobStatus status) {

        }

        @Override
        public Map<String, Object> getData() {
            return null;
        }

        @Override
        public JobSharedData getSharedData() {
            return new JobSharedData(uuid, "", null,"");
        }

        @Override
        public void setTypeAndData(JobType jobType, Map<String, Object> commandData) {

        }

        @Override
        public UUID getTemplateId() {
            return null;
        }

        @Override
        public void setTemplateId(UUID templateId) {

        }

        @Override
        public Integer getIndexInBulk() {
            return null;
        }

        @Override
        public void setIndexInBulk(Integer indexInBulk) {

        }

        @Override
        public JobType getType() {
            return null;
        }

        public String getOptimisticUniqueServiceInstanceName() {
            return optimisticUniqueServiceInstanceName;
        }
    }


    @Test
    public void testGetVfModuleReplacePath_asMSOexpected()
    {
        String path = asyncInstantiationBL.getVfModuleReplacePath("myService", "myVNF", "myVFModule");
        assertThat(path, equalTo("/serviceInstantiation/v7/serviceInstances/myService/vnfs/myVNF/vfModules/myVFModule/replace"));
    }

    @Test
    public void whenCallClearStatusFromRequest_isFailedAndStatusAreRemoved() throws JsonProcessingException {
        ServiceInstantiation serviceInstantiation = JACKSON_OBJECT_MAPPER.readValue(
               "{"
                + "    \"modelInfo\": {"
                + "        \"modelType\": \"service\""
                + "    },"
                + "    \"isFailed\": true,"
                + "    \"statusMessage\": \"some status\","
                + "    \"vnfs\": {"
                + "        \"vProbe_NC_VNF\": {"
                + "            \"modelInfo\": {"
                + "                \"modelType\": \"vnf\""
                + "            },"
                + "            \"isFailed\": true,"
                + "            \"statusMessage\": \"other status\""
                + "        }"
                + "    }"
                + "}",
            ServiceInstantiation.class);
        asyncInstantiationBL.clearStatusFromRequest(serviceInstantiation);
        assertThat(serviceInstantiation, allOf(
            jsonPartEquals("isFailed", false),
            jsonPartEquals("statusMessage", null),
            jsonPartEquals("vnfs.vProbe_NC_VNF.isFailed", false),
            jsonPartEquals("vnfs.vProbe_NC_VNF.statusMessage", null)
        ));
    }


}
