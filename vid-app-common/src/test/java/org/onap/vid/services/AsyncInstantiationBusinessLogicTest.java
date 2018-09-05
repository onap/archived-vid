package org.onap.vid.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.io.IOUtils;
import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.exceptions.InvalidAAIResponseException;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.MaxRetriesException;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.impl.JobDaoImpl;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.JobAuditStatus.SourceStatus;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.utils.DaoUtils;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.MockedAaiClientAndFeatureManagerConfig;
import org.onap.vid.mso.MsoOperationalEnvironmentTest;
import org.onap.vid.services.AsyncInstantiationBaseTest;
import org.onap.portalsdk.core.domain.FusionObject;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.onap.vid.job.Job.JobStatus.*;
import static org.testng.Assert.*;

@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, MockedAaiClientAndFeatureManagerConfig.class})
public class AsyncInstantiationBusinessLogicTest extends AsyncInstantiationBaseTest {
/*
TO BE FIXED
    @Inject
    private DataAccessService dataAccessService;

    @Mock
    private JobAdapter jobAdapter;

    @Mock
    private JobsBrokerService jobsBrokerService;



    @Autowired
    private SessionFactory sessionFactory;

    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    private int serviceCount = 0;

    private static final String UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE =
            "Failed to retrieve job with uuid .* from ServiceInfo table. Instances found: .*";

    private static final String DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE =
            "Service status does not allow deletion from the queue";

    @BeforeClass
    void initServicesInfoService() {
        MockitoAnnotations.initMocks(this);
        asyncInstantiationBL = new AsyncInstantiationBusinessLogicImpl(dataAccessService, jobAdapter, jobsBrokerService, sessionFactory, aaiClient);
        createInstanceParamsMaps();
    }

    @BeforeMethod
    void defineMocks() {
        mockAaiClientAnyNameFree();
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
        addNewServiceInfo(uuid, userId, "Old", createdDate, createdDate, COMPLETED, false);

        uuid = UUID.randomUUID();
        addNewJob(uuid);
        createdDate = NOW.minusDays(20);
        modifiedDate = NOW.minusDays(19);
        addNewServiceInfo(uuid, userId, "Hidden", createdDate, modifiedDate, PAUSE, true);

        createNewTestServicesInfo(String.valueOf(userId));
    }

    private void createNewTestServicesInfo(String userId) {

        LocalDateTime createdDate, modifiedDate;
        LocalDateTime NOW = LocalDateTime.now();
        UUID uuid;

        uuid = UUID.randomUUID();
        addNewJob(uuid);

        createdDate = NOW.minusDays(40);
        addNewServiceInfo(uuid, userId, "service instance 5", createdDate, createdDate, COMPLETED, false);
        addNewServiceInfo(uuid, userId, "service instance 6", createdDate, createdDate, STOPPED, false);

        uuid = UUID.randomUUID();
        addNewJob(uuid);

        createdDate = NOW.minusDays(20);
        modifiedDate = NOW.minusDays(10);
        addNewServiceInfo(uuid, userId, "service instance 4", createdDate, modifiedDate, STOPPED, false);
        addNewServiceInfo(uuid, userId, "service instance 2", createdDate, modifiedDate, COMPLETED, false);
        addNewServiceInfo(uuid, userId, "service instance 3", createdDate, modifiedDate, PAUSE, false);

        modifiedDate = NOW.minusDays(19);
        addNewServiceInfo(uuid, userId, "service instance 1", createdDate, modifiedDate, FAILED, false);


        // Job to a different user
        uuid = UUID.randomUUID();
        addNewJob(uuid);

        createdDate = NOW.minusMonths(2);
        addNewServiceInfo(uuid, "2221", "service instance 7", createdDate, createdDate, COMPLETED, false);

    }

    private UUID createServicesInfoWithDefaultValues(Job.JobStatus status) {

        LocalDateTime NOW = LocalDateTime.now();
        UUID uuid;

        uuid = UUID.randomUUID();
        addNewJob(uuid, status);

        addNewServiceInfo(uuid, null, "service instance 1", NOW, NOW, status, false);

        return uuid;

    }

    private List<ServiceInfo> getFullList() {
        List<ServiceInfo> expectedOrderServiceInfo = dataAccessService.getList(ServiceInfo.class, getPropsMap());
        assertThat("Failed to retrieve all predefined services", expectedOrderServiceInfo.size(), equalTo(serviceCount));
        expectedOrderServiceInfo.sort(new ServiceInfoComparator());
        return expectedOrderServiceInfo;
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime fromDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void addNewServiceInfo(UUID uuid, String userId, String serviceName, LocalDateTime createDate, LocalDateTime statusModifiedDate, Job.JobStatus status, boolean isHidden) {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setJobId(uuid);
        serviceInfo.setUserId(userId);
        serviceInfo.setServiceInstanceName(serviceName);
        serviceInfo.setStatusModifiedDate(toDate(statusModifiedDate));
        serviceInfo.setJobStatus(status);
        serviceInfo.setPause(false);
        serviceInfo.setOwningEntityId("1234");
        serviceInfo.setCreatedBulkDate(toDate(createDate));

        serviceInfo.setHidden(isHidden);
        dataAccessService.saveDomainObject(serviceInfo, getPropsMap());
        setCreateDateToServiceInfo(uuid, createDate);
        serviceCount++;

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

    @Test
    public void testServiceInfoAreOrderedAsExpected() {
        int userId = 2222;
        createNewTestServicesInfo(String.valueOf(userId));
        List<ServiceInfo> expectedOrderServiceInfo = getFullList();
        List<ServiceInfo> serviceInfoListResult = asyncInstantiationBL.getAllServicesInfo();
        assertThat("Services aren't ordered as expected", serviceInfoListResult, equalTo(expectedOrderServiceInfo));
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

    @Test(dataProvider = "pauseAndInstanceParams", enabled = false) //Test is irrelevant with unique names feature
    public void createServiceInstantiationMsoRequest(Boolean isPause, HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams) throws Exception {
        ServiceInstantiation serviceInstantiationPayload = generateMockServiceInstantiationPayload(isPause, createVnfList(vfModuleInstanceParamsMap, vnfInstanceParams, true));
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_service_request.json");
            RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                    asyncInstantiationBL.generateServiceInstantiationRequest(null, serviceInstantiationPayload, "az2016");
            String expected = IOUtils.toString(resource, "UTF-8");
            MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test(dataProvider = "pauseAndInstanceParams")
    public void createServiceInstantiationMsoRequestUniqueName(Boolean isPause, HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams) throws Exception {
        Mockito.reset(aaiClient);
        mockAaiClientAnyNameFree();
        ServiceInstantiation serviceInstantiationPayload = generateMockServiceInstantiationPayload(isPause, createVnfList(vfModuleInstanceParamsMap, vnfInstanceParams, true));
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_service_request_unique_names.json");
        List<UUID> uuids = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UUID currentUuid = createJobAndServiceInfo();
            uuids.add(currentUuid);
            RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                    asyncInstantiationBL.generateServiceInstantiationRequest(currentUuid, serviceInstantiationPayload, "az2016");
            String unique =  String.format("00%s", i + 1);
            String expected = IOUtils.toString(resource, "UTF-8")
                    .replace("{SERVICE_UNIQENESS}", unique)
                    .replace("{VNF_UNIQENESS}", unique)
                    .replace("{VF_MODULE_UNIQENESS}", unique)
                    .replace("{VF_MODULE_2_UNIQENESS}", unique)
                    .replace("{VG_UNIQUENESS}", unique);
            MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
            Optional<ServiceInfo> optionalServiceInfo = getJobById(currentUuid);
            assertThat(optionalServiceInfo.get().getServiceInstanceName(), equalTo("vPE_Service_" + unique));
            verifySearchNodeTypeByName(unique, "vPE_Service_", ResourceType.SERVICE_INSTANCE);
            verifySearchNodeTypeByName(unique, "vmxnjr001_", ResourceType.GENERIC_VNF);
            verifySearchNodeTypeByName(unique, "vmxnjr001_AVPN_base_vPE_BV_base_", ResourceType.VF_MODULE);
            verifySearchNodeTypeByName(unique, "vmxnjr001_AVPN_base_vRE_BV_expansion_", ResourceType.VF_MODULE);
            verifySearchNodeTypeByName(unique, "myVgName_", ResourceType.VOLUME_GROUP);
        }
    }

    protected void verifySearchNodeTypeByName(String unique, String resourceName, ResourceType serviceInstance) {
        verify(aaiClient, times(1)).searchNodeTypeByName(resourceName + unique, serviceInstance);
    }

    private HashMap<String, Object> getPropsMap() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(FusionObject.Parameters.PARAM_USERID, 0);
        return props;
    }

    @Test(enabled = false) //probably not needed with name uniqueness feature
    public void pushBulkJob_bulkWithSize3_instancesNamesAreExactlyAsExpected() {
        int bulkSize = 3;

        final ServiceInstantiation request = generateMockServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true),
                bulkSize, true,PROJECT_NAME, true
        );

        // in "createJob()" we will probe the service, with the generated names
        final Job job = mock(Job.class);
        when(job.getStatus()).thenReturn(PENDING);
        when(jobAdapter.createJob(any(), any(), any(), any(), any())).thenReturn(job);


        final List<UUID> uuids = asyncInstantiationBL.pushBulkJob(request, "myUserId");


        ArgumentCaptor<ServiceInstantiation> serviceInstantiationCaptor = new ArgumentCaptor<ServiceInstantiation>();
        verify(jobAdapter, times(bulkSize)).createJob(any(), serviceInstantiationCaptor.capture(), any(), any(), any());

        assertThat(serviceInstantiationCaptor.getAllValues().stream().map(v -> v.getInstanceName()).collect(Collectors.toList()),
                containsInAnyOrder("vPE_Service_001", "vPE_Service_002", "vPE_Service_003"));

        assertThat(uuids, hasSize(bulkSize));
    }

    @Test
    public void generateMockServiceInstantiationPayload_serializeBackAndForth_sourceShouldBeTheSame() throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generateMockServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, ImmutableList.of(vnfInstanceParamsMapWithParamsToRemove, vnfInstanceParamsMapWithParamsToRemove), true),
                2, false,PROJECT_NAME, false);
        ObjectMapper mapper = new ObjectMapper();
        final String asString = mapper.writeValueAsString(serviceInstantiationPayload);

        final ServiceInstantiation asObject = mapper.readValue(asString, ServiceInstantiation.class);
        final String asString2 = mapper.writeValueAsString(asObject);

        JsonAssert.assertJsonEquals(asString, asString2);
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

    private ServiceInstantiation generateMockServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs) {
        return generateMockServiceInstantiationPayload(isPause, vnfs, 1, true, PROJECT_NAME, false);
    }

    @Test
    public void testUpdateServiceInfo_WithExistingServiceInfo_ServiceInfoIsUpdated() {
        UUID uuid = createJobAndServiceInfo();
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

    private UUID createJobAndServiceInfo() {
        UUID uuid = UUID.randomUUID();
        addNewJob(uuid);
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceInstanceName("Lebron James");
        serviceInfo.setJobId(uuid);
        serviceInfo.setJobStatus(Job.JobStatus.PENDING);
        dataAccessService.saveDomainObject(serviceInfo, getPropsMap());
        return uuid;
    }

    @Test(expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp = UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE)
    public void testUpdateServiceInfo_WithNonExisting_ThrowException() {
        asyncInstantiationBL.updateServiceInfo(UUID.randomUUID(), x -> x.setServiceInstanceName("not matter"));
    }

    @Test(expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp = UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE)
    public void testUpdateServiceInfo_WithDoubleServiceWithSameJobUuid_ThrowException() {
        UUID uuid = createJobAndServiceInfo();
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setJobId(uuid);
        dataAccessService.saveDomainObject(serviceInfo, getPropsMap());
        asyncInstantiationBL.updateServiceInfo(UUID.randomUUID(), x -> x.setServiceInstanceName("not matter"));
    }



    @Test
    public void testRequestPath_WithPauseFlagTrue_RequestPathIsAsExpected() {
        ServiceInstantiation serviceInstantiationPauseFlagTrue = generateMockServiceInstantiationPayload(true, createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true));
        String path = asyncInstantiationBL.getServiceInstantiationPath(serviceInstantiationPauseFlagTrue);
        Assert.assertEquals(path, SystemProperties.getProperty("mso.restapi.serviceInstanceAssign"));
    }

    @Test
    public void testRequestPath_WithPauseFlagFalse_RequestPathIsAsExpected() {
        ServiceInstantiation serviceInstantiationPauseFlagFalse = generateMockServiceInstantiationPayload(false, createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true));
        String path = asyncInstantiationBL.getServiceInstantiationPath(serviceInstantiationPauseFlagFalse);
        Assert.assertEquals(path, SystemProperties.getProperty("mso.restapi.serviceInstanceCreate"));
    }

    @Test
    public void createServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected() throws IOException {
        createServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(true);
    }

    @Test
    public void createServiceInfo_WithUserProvidedNamingFalseAndNoVfmodules_ServiceInfoIsAsExpected() throws IOException {
        createServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(false);
    }

    private void createServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(boolean withVfmodules) throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generateMockServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, Collections.EMPTY_LIST, false),
                1,
                false,PROJECT_NAME, true);
        URL resource;
        if (withVfmodules) {
            resource = this.getClass().getResource("/payload_jsons/bulk_service_request_ecomp_naming.json");
        } else {
            // remove the vf modules
            serviceInstantiationPayload.getVnfs().values().forEach(vnf -> vnf.getVfModules().clear());
            resource = this.getClass().getResource("/payload_jsons/bulk_service_request_no_vfmodule_ecomp_naming.json");
        }

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                asyncInstantiationBL.generateServiceInstantiationRequest(null, serviceInstantiationPayload, "az2016");

        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @Test
    public void checkIfNullProjectNameSentToMso(){
        ServiceInstantiation serviceInstantiationPayload = generateMockServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, Collections.EMPTY_LIST, false),
                1,
                false,null,false);
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                asyncInstantiationBL.generateServiceInstantiationRequest(null, serviceInstantiationPayload, "az2016");
        JsonNode jsonNode = new ObjectMapper().valueToTree(result.requestDetails);
        Assert.assertTrue(jsonNode.get("project").isNull());
        serviceInstantiationPayload = generateMockServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, Collections.EMPTY_LIST, false),
                1,
                false,"not null",false);
        result = asyncInstantiationBL.generateServiceInstantiationRequest(null, serviceInstantiationPayload, "az2016");
        jsonNode = new ObjectMapper().valueToTree(result.requestDetails);
        Assert.assertTrue(jsonNode.get("project").get("projectName").asText().equalsIgnoreCase("not null"));



    }

    @Test
    public void pushBulkJob_verifyCreatedDateBehavior_createdDateIsTheSameForAllServicesInSameBulk() {
        LocalDateTime startTestDate = LocalDateTime.now().withNano(0);
        final ServiceInstantiation request = generateMockServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true),
                100, true,PROJECT_NAME, true
        );

        // in "createJob()" we will probe the service, with the generated names
        final Job job = mock(Job.class);
        when(job.getStatus()).thenReturn(PENDING);
        when(jobAdapter.createJob(any(), any(), any(), any(), any())).thenReturn(job);

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
                {"UNLOCKED", JobStatus.IN_PROGRESS}
        };
    }

    @Test(dataProvider = "msoToJobStatusDataProvider")
    void whenGetStatusFromMso_calcRightJobStatus(String msoStatus, Job.JobStatus expectedJobStatus) {
        AsyncRequestStatus asyncRequestStatus = asyncRequestStatusResponse(msoStatus);
        assertThat(asyncInstantiationBL.calcStatus(asyncRequestStatus), equalTo(expectedJobStatus));
    }

    private void createNewAuditStatus(JobAuditStatus auditStatus)
    {
        Date createdDate= auditStatus.getCreated();
        dataAccessService.saveDomainObject(auditStatus, getPropsMap());
        setDateToStatus(auditStatus.getSource(), auditStatus.getJobStatus(), createdDate);
    }



    private static final String MSO_ARBITRARY_STATUS = "completed mso status";

    @DataProvider
    public static Object[][] auditStatuses(Method test) {
        return new Object[][]{
                {
                        SourceStatus.VID,
                        new String[]{ JobStatus.PENDING.toString(), JobStatus.IN_PROGRESS.toString()}
                },
                {       SourceStatus.MSO,
                        new String[]{ JobStatus.IN_PROGRESS.toString(), MSO_ARBITRARY_STATUS }
                }
        };

    }

    private void setDateToStatus(SourceStatus source, String status, Date date) {
        List<JobAuditStatus> jobAuditStatusList = dataAccessService.getList(JobAuditStatus.class, getPropsMap());
        DaoUtils.tryWithSessionAndTransaction(sessionFactory, session -> {
            jobAuditStatusList.stream()
                    .filter(auditStatus -> source.equals(auditStatus.getSource()) && status.equals(auditStatus.getJobStatus()))
                    .forEach(auditStatus -> {
                        auditStatus.setCreated(date);
                        session.saveOrUpdate(auditStatus);
                    });
            return 1;
        });
    }


    @Test(dataProvider = "auditStatuses")
    public void givenSomeAuditStatuses_getStatusesOfSpecificSourceAndJobId_getSortedResultsMatchingToParameters(SourceStatus expectedSource, String [] expectedSortedStatuses){
        UUID jobUuid = UUID.randomUUID();
        List<JobAuditStatus> auditStatusList = com.google.common.collect.ImmutableList.of(
                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.VID, toDate(LocalDateTime.now().minusHours(2))),
                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, UUID.randomUUID(),"",toDate(LocalDateTime.now().minusHours(30))),
                new JobAuditStatus(jobUuid, MSO_ARBITRARY_STATUS, SourceStatus.MSO, UUID.randomUUID(),"",toDate(LocalDateTime.now().minusHours(3))),
                new JobAuditStatus(jobUuid, PENDING.toString(), SourceStatus.VID, toDate(LocalDateTime.now().minusHours(3))),
                new JobAuditStatus(UUID.randomUUID(), PENDING.toString(), SourceStatus.VID, toDate(LocalDateTime.now().minusHours(3))));
        auditStatusList.forEach((auditStatus) -> createNewAuditStatus(auditStatus));
        List<JobAuditStatus> statuses = asyncInstantiationBL.getAuditStatuses(jobUuid, expectedSource);
        List<String> statusesList = statuses.stream().map(status -> status.getJobStatus()).collect(Collectors.toList());
        Assert.assertTrue(statuses.stream().allMatch(status -> (status.getSource().equals(expectedSource)&& status.getJobId().equals(jobUuid))),"Only statuses of " + expectedSource + " for " + jobUuid + " should be returned. Returned statuses: " + String.join(",", statusesList ));
        assertThat(statusesList, contains(expectedSortedStatuses));
    }



    @Test
    public void addSomeVidStatuses_getThem_verifyGetInsertedWithoutDuplicates(){
        ImmutableList<JobStatus> statusesToBeInserted = ImmutableList.of(PENDING, IN_PROGRESS, IN_PROGRESS, COMPLETED);
        UUID jobUuid = UUID.randomUUID();
        statusesToBeInserted.forEach(status->
            {
                asyncInstantiationBL.auditVidStatus(jobUuid, status);
            });
        List<String> statusesFromDB = asyncInstantiationBL.getAuditStatuses(jobUuid, SourceStatus.VID).stream().map(auditStatus -> auditStatus.getJobStatus()).collect(Collectors.toList());
        List<String> statusesWithoutDuplicates = statusesToBeInserted.stream().distinct().map(x -> x.toString()).collect(Collectors.toList());
        assertThat(statusesFromDB, is(statusesWithoutDuplicates));
    }

    @DataProvider
    public static Object[][] msoAuditStatuses(Method test) {
        UUID jobUuid = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();
        return new Object[][]{
                {
                        jobUuid,
                        ImmutableList.of(
                                new JobAuditStatus(jobUuid, PENDING.toString(), SourceStatus.MSO, null, null),
                                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, requestId, null),
                                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, requestId, null),
                                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, requestId, null),
                                new JobAuditStatus(jobUuid, COMPLETED.toString(), SourceStatus.MSO, requestId, null)),
                        ImmutableList.of(PENDING.toString(), IN_PROGRESS.toString(), COMPLETED.toString()),
                        "All distinct statuses should be without duplicates"
                },
                {
                        jobUuid,
                        ImmutableList.of(
                                new JobAuditStatus(jobUuid, PENDING.toString(), SourceStatus.MSO, null, null),
                                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, requestId, null),
                                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, requestId, "aa"),
                                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, requestId, "aa"),
                                new JobAuditStatus(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, UUID.randomUUID(), "aa"),
                                new JobAuditStatus(jobUuid, COMPLETED.toString(), SourceStatus.MSO, requestId, null)),
                        ImmutableList.of(PENDING.toString(), IN_PROGRESS.toString(), IN_PROGRESS.toString(),IN_PROGRESS.toString(), COMPLETED.toString()),
                        "Statuses should be without duplicates only with same requestId and additionalInfo"

                }
        };
    }

    @Test(dataProvider = "msoAuditStatuses")
    public void addSomeMsoStatuses_getThem_verifyGetInsertedWithoutDuplicates(UUID jobUuid, ImmutableList<JobAuditStatus> msoStatuses, ImmutableList<String> expectedStatuses, String assertionReason) {
        msoStatuses.forEach(status -> {
            asyncInstantiationBL.auditMsoStatus(status.getJobId(), status.getJobStatus(), status.getRequestId() != null ? status.getRequestId().toString() : null, status.getAdditionalInfo());
        });
        List<String> statusesFromDB = asyncInstantiationBL.getAuditStatuses(jobUuid, SourceStatus.MSO).stream().map(auditStatus -> auditStatus.getJobStatus()).collect(Collectors.toList());
        assertThat( assertionReason, statusesFromDB, is(expectedStatuses));
    }

    @Test
    public void addSameStatusOfVidAndMso_verifyThatBothWereAdded(){
        UUID jobUuid = UUID.randomUUID();
        JobStatus sameStatus = IN_PROGRESS;
        asyncInstantiationBL.auditMsoStatus(jobUuid, sameStatus.toString(),null,null);
        asyncInstantiationBL.auditVidStatus(jobUuid, sameStatus);
        List<JobAuditStatus> list = dataAccessService.getList(
                JobAuditStatus.class,
                String.format(" where JOB_ID = '%s'", jobUuid),
                null, null);
        Assert.assertEquals(list.size(),2);
        assertThat(list,everyItem(hasProperty("jobStatus", is(sameStatus.toString()))));
    }

    @Test
    public void verifyAsyncRequestStatus_canBeReadFromSample() throws IOException {
        String body = "{" +
                "  \"request\": {" +
                "    \"requestId\": \"c0011670-0e1a-4b74-945d-8bf5aede1d9c\"," +
                "    \"startTime\": \"Mon, 11 Dec 2017 07:27:49 GMT\"," +
                "    \"requestScope\": \"service\"," +
                "    \"requestType\": \"createInstance\"," +
                "    \"instanceReferences\": {" +
                "      \"serviceInstanceId\": \"f8791436-8d55-4fde-b4d5-72dd2cf13cfb\"," +
                "      \"serviceInstanceName\": \"asdfasdf234234asdf\"," +
                "      \"requestorId\": \"il883e\"" +
                "    }," +
                "    \"requestStatus\": {" +
                "      \"requestState\": \"COMPLETE\"," +
                "      \"statusMessage\": \"Service Instance was created successfully.\"," +
                "      \"percentProgress\": 100," +
                "      \"finishTime\": \"Mon, 11 Dec 2017 07:27:53 GMT\"" +
                "    }" +
                "  }" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        AsyncRequestStatus asyncRequestStatus = objectMapper.readValue(body, AsyncRequestStatus.class);
        assertThat(asyncRequestStatus.request.requestStatus.getRequestState(), equalTo("COMPLETE"));

    }

    @Test
    public void deleteJobInfo_pending_deleted() {
        doNothing().when(jobsBrokerService).delete(any());
        UUID uuid = createServicesInfoWithDefaultValues(PENDING);
        asyncInstantiationBL.deleteJob(uuid);
        assertNotNull(asyncInstantiationBL.getServiceInfoByJobId(uuid).getDeletedAt(), "service info wasn't deleted");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE)
    public void deleteJobInfo_notAllowdStatus_shouldSendError() {
        UUID uuid = createServicesInfoWithDefaultValues(COMPLETED);
        doThrow(new IllegalStateException(DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE)).when(jobsBrokerService).delete(any());
        try {
            asyncInstantiationBL.deleteJob(uuid);
        } catch (Exception e) {
            assertNull(asyncInstantiationBL.getServiceInfoByJobId(uuid).getDeletedAt(), "service info shouldn't deleted");
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

    @Test(  dataProvider = "jobStatusesNotFinal",
            expectedExceptions = OperationNotAllowedException.class,
            expectedExceptionsMessageRegExp = "jobId.*Service status does not allow hide service, status = .*")
    public void hideServiceInfo_notAllowedStatus_shouldSendError(JobStatus jobStatus) {
        UUID uuid = createServicesInfoWithDefaultValues(jobStatus);
        try {
            asyncInstantiationBL.hideServiceInfo(uuid);
        } catch (Exception e) {
            assertFalse(asyncInstantiationBL.getServiceInfoByJobId(uuid).isHidden(), "service info shouldn't be hidden");
            throw e;
        }
    }

    @Test
    public void whenUseGetCounterInMultiThreads_EachThreadGetDifferentCounter() throws InterruptedException {
        int SIZE = 200;
        ExecutorService executor = Executors.newFixedThreadPool(SIZE);
        List<Callable<Integer>> tasks = IntStream.rangeClosed(1, SIZE)
                .mapToObj(x-> ((Callable<Integer>)() -> asyncInstantiationBL.getCounterForName("a")))
                .collect(Collectors.toList());
        Set<Integer> expectedResults = IntStream.rangeClosed(1, SIZE).boxed().collect(Collectors.toSet());
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
        for (int i=1; i<=SIZE; i++) {
            assertThat(asyncInstantiationBL.getCounterForName(name), is(i));
        }
    }

    @Test
    public void whenNamedInUsedInAai_getNextNumber() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.GENERIC_VNF;
        when(aaiClient.searchNodeTypeByName(name+"_001", type)).thenReturn(aaiNodeQueryResponseNameUsed(type));
        when(aaiClient.searchNodeTypeByName(name+"_002", type)).thenReturn(aaiNodeQueryResponseNameFree());
        assertThat(asyncInstantiationBL.getUniqueName(name, type), equalTo(name+"_002"));
    }

    private String someCommonStepsAndGetName() {
        mockAaiClientAaiStatusOK();
        return UUID.randomUUID().toString();
    }

    private void mockAaiClientAaiStatusOK() {
        when(aaiClient.searchNodeTypeByName(eq(AsyncInstantiationBusinessLogicImpl.NAME_FOR_CHECK_AAI_STATUS), any())).thenReturn(aaiNodeQueryResponseNameFree());
    }

    @Test(expectedExceptions=InvalidAAIResponseException.class)
    public void whenAaiBadResponseCode_throwInvalidAAIResponseException() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.SERVICE_INSTANCE;
        when(aaiClient.searchNodeTypeByName(name+"_001", type)).thenReturn(aaiNodeQueryBadResponse());
        asyncInstantiationBL.getUniqueName(name, type);
    }

    @Test(expectedExceptions=MaxRetriesException.class)
    public void whenAaiAlwaysReturnNameUsed_throwInvalidAAIResponseException() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.VF_MODULE;
        when(aaiClient.searchNodeTypeByName(any(), eq(type))).thenReturn(aaiNodeQueryResponseNameUsed(type));
        asyncInstantiationBL.setMaxRetriesGettingFreeNameFromAai(10);
        asyncInstantiationBL.getUniqueName(name, type);
    }

    @Test
    public void testFormattingOfNameAndCounter() {
        AsyncInstantiationBusinessLogicImpl bl = (AsyncInstantiationBusinessLogicImpl) asyncInstantiationBL;
        assertThat(bl.formatNameAndCounter("x", 3), equalTo("x_003"));
        assertThat(bl.formatNameAndCounter("x", 99), equalTo("x_099"));
        assertThat(bl.formatNameAndCounter("x", 100), equalTo("x_100"));
        assertThat(bl.formatNameAndCounter("x", 1234), equalTo("x_1234"));
    }*/
}
