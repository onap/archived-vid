package org.onap.vid.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.io.IOUtils;
import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.onap.portalsdk.core.domain.FusionObject;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.MockedAaiClientAndFeatureManagerConfig;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.impl.JobDaoImpl;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.Action;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.JobAuditStatus.SourceStatus;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.*;
import org.onap.vid.mso.MsoOperationalEnvironmentTest;
import org.onap.vid.mso.model.*;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.collect.Maps.newHashMap;
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

    @Inject
    private DataAccessService dataAccessService;

    @Mock
    private JobAdapter jobAdapterMock;

    @Mock
    private JobsBrokerService jobsBrokerServiceMock;


    @Autowired
    private SessionFactory sessionFactory;

    private AsyncInstantiationBusinessLogicImpl asyncInstantiationBL;

    private int serviceCount = 0;

    private static final String UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE =
            "Failed to retrieve job with uuid .* from ServiceInfo table. Instances found: .*";

    private static final String DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE =
            "Service status does not allow deletion from the queue";

    @BeforeClass
    void initServicesInfoService() {
        MockitoAnnotations.initMocks(this);
        asyncInstantiationBL = new AsyncInstantiationBusinessLogicImpl(dataAccessService, jobAdapterMock, jobsBrokerServiceMock, sessionFactory, aaiClient, featureManager, cloudOwnerService);
        createInstanceParamsMaps();
    }

    @BeforeMethod
    void defineMocks() {
        Mockito.reset(aaiClient);
        Mockito.reset(jobAdapterMock);
        Mockito.reset(jobsBrokerServiceMock);
        mockAaiClientAnyNameFree();
        enableAddCloudOwnerOnMsoRequest();
    }

    private void enableAddCloudOwnerOnMsoRequest() {
        enableAddCloudOwnerOnMsoRequest(true);
    }

    private void enableAddCloudOwnerOnMsoRequest(boolean isActive) {
        // always turn on the feature flag
        when(featureManager.isActive(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).thenReturn(isActive);
        when(aaiClient.getCloudOwnerByCloudRegionId(anyString())).thenReturn("att-aic");
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

    //@Test
    public void testServiceInfoAreOrderedAsExpected() {
        int userId = 2222;
        createNewTestServicesInfo(String.valueOf(userId));
        List<ServiceInfo> expectedOrderServiceInfo = getFullList();
        List<ServiceInfo> serviceInfoListResult = asyncInstantiationBL.getAllServicesInfo();
        assertThat("Services aren't ordered as expected", serviceInfoListResult, equalTo(expectedOrderServiceInfo));
    }

    //@Test
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

    //@Test(dataProvider = "pauseAndInstanceParams", enabled = false) //Test is irrelevant with unique names feature
    public void createMacroServiceInstantiationMsoRequest(Boolean isPause, HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams) throws Exception {
        ServiceInstantiation serviceInstantiationPayload = generateMacroMockServiceInstantiationPayload(isPause, createVnfList(vfModuleInstanceParamsMap, vnfInstanceParams, true));
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_macro_service_request.json");
            RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                    asyncInstantiationBL.generateMacroServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");
            String expected = IOUtils.toString(resource, "UTF-8");
            MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }


    //@Test(dataProvider = "pauseAndInstanceParams")
    public void createMacroServiceInstantiationMsoRequestUniqueName(Boolean isPause, HashMap<String, String> vfModuleInstanceParamsMap, List vnfInstanceParams) throws Exception {
        defineMocks();
        ServiceInstantiation serviceInstantiationPayload = generateMockMacroServiceInstantiationPayload(isPause, createVnfList(vfModuleInstanceParamsMap, vnfInstanceParams, true), 2, true, PROJECT_NAME, false);
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_service_request_unique_names.json");
        when(jobAdapterMock.createServiceInstantiationJob(any(), any(), any(), any(), anyString(), any())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return new MockedJob((String)args[4]);
        });

        when(jobsBrokerServiceMock.add(any(MockedJob.class))).thenAnswer((Answer<UUID>) invocation -> {
            Object[] args = invocation.getArguments();
            MockedJob job = (MockedJob) args[0];
            MockedJob.putJob(job.uuid, job);
            return job.getUuid();
        });

        when(featureManager.isActive(Features.FLAG_SHIFT_VFMODULE_PARAMS_TO_VNF)).thenReturn(true);

        List<UUID> uuids = asyncInstantiationBL.pushBulkJob(serviceInstantiationPayload, "az2016");
        for (int i = 0; i < 2; i++) {
            UUID currentUuid = uuids.get(i);
            RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                    asyncInstantiationBL.generateMacroServiceInstantiationRequest(currentUuid, serviceInstantiationPayload,
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
        verify(aaiClient, times(1)).isNodeTypeExistsByName(resourceName + unique, serviceInstance);
    }

    private HashMap<String, Object> getPropsMap() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(FusionObject.Parameters.PARAM_USERID, 0);
        return props;
    }


    @DataProvider
    public static Object[][] dataProviderForInstanceNames() {
        return new Object[][]{
                {true, ImmutableList.of("vPE_Service", "vPE_Service_001", "vPE_Service_002")},
                {false, ImmutableList.of("", "", "")},
        };
    }

    //@Test(dataProvider="dataProviderForInstanceNames")
    public void pushBulkJob_bulkWithSize3_instancesNamesAreExactlyAsExpected(boolean isUserProvidedNaming, List<String> expectedNames) {
        int bulkSize = 3;

        final ServiceInstantiation request = generateMockMacroServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true),
                bulkSize, isUserProvidedNaming, PROJECT_NAME, true
        );

        // in "createServiceInstantiationJob()" we will probe the service, with the generated names
        configureMockitoWithMockedJob();


        asyncInstantiationBL.pushBulkJob(request, "myUserId");

        List<ServiceInfo> serviceInfoList = dataAccessService.getList(ServiceInfo.class, getPropsMap());
        assertEquals(serviceInfoList.stream().map(ServiceInfo::getServiceInstanceName).collect(Collectors.toList()), expectedNames);
    }

    //@Test (dataProvider = "aLaCarteAndMacroPayload")
    public void generateMockServiceInstantiationPayload_serializeBackAndForth_sourceShouldBeTheSame(ServiceInstantiation serviceInstantiationPayload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        final String asString = mapper.writeValueAsString(serviceInstantiationPayload);

        final ServiceInstantiation asObject = mapper.readValue(asString, ServiceInstantiation.class);
        final String asString2 = mapper.writeValueAsString(asObject);

        JsonAssert.assertJsonEquals(asString, asString2);
    }

    @DataProvider
    public Object[][] aLaCarteAndMacroPayload() {
        ServiceInstantiation macroPayload = generateMockMacroServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, ImmutableList.of(vnfInstanceParamsMapWithParamsToRemove, vnfInstanceParamsMapWithParamsToRemove), true),
                2, false,PROJECT_NAME, false);
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

    private ServiceInstantiation generateMacroMockServiceInstantiationPayload(boolean isPause, Map<String, Vnf> vnfs) {
        return generateMockMacroServiceInstantiationPayload(isPause, vnfs, 1, true, PROJECT_NAME, false);
    }

    //@Test
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

    //@Test(expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp = UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE)
    public void testUpdateServiceInfo_WithNonExisting_ThrowException() {
        asyncInstantiationBL.updateServiceInfo(UUID.randomUUID(), x -> x.setServiceInstanceName("not matter"));
    }

    //@Test(expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp = UPDATE_SERVICE_INFO_EXCEPTION_MESSAGE)
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
                {false, "mso.restapi.serviceInstanceCreate"},
        };
    }


    //@Test(dataProvider = "isPauseAndPropertyDataProvider")
    public void testServiceInstantiationPath_RequestPathIsAsExpected(boolean isPause, String expectedProperty) {
        ServiceInstantiation serviceInstantiationPauseFlagTrue = generateMacroMockServiceInstantiationPayload(isPause, createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true));
        String path = asyncInstantiationBL.getServiceInstantiationPath(serviceInstantiationPauseFlagTrue);
        Assert.assertEquals(path, SystemProperties.getProperty(expectedProperty));
    }

    //@Test
    public void testCreateVnfEndpoint_useProvidedInstanceId() {
        String path = asyncInstantiationBL.getVnfInstantiationPath("myGreatId");
        assertThat(path, equalTo("/serviceInstances/v7/myGreatId/vnfs"));
    }

    //@Test
    public void createServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected() throws IOException {
        createMacroServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(true);
    }

    //@Test
    public void createServiceInfo_WithUserProvidedNamingFalseAndNoVfmodules_ServiceInfoIsAsExpected() throws IOException {
        createMacroServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(false);
    }

    private void createMacroServiceInfo_WithUserProvidedNamingFalse_ServiceInfoIsAsExpected(boolean withVfmodules) throws IOException {
        when(featureManager.isActive(Features.FLAG_SHIFT_VFMODULE_PARAMS_TO_VNF)).thenReturn(true);

        ServiceInstantiation serviceInstantiationPayload = generateMockMacroServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, Collections.EMPTY_LIST, false),
                1,
                false, PROJECT_NAME, true);
        URL resource;
        if (withVfmodules) {
            resource = this.getClass().getResource("/payload_jsons/bulk_service_request_ecomp_naming.json");
        } else {
            // remove the vf modules
            serviceInstantiationPayload.getVnfs().values().forEach(vnf -> vnf.getVfModules().clear());
            resource = this.getClass().getResource("/payload_jsons/bulk_service_request_no_vfmodule_ecomp_naming.json");
        }

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                asyncInstantiationBL.generateMacroServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");

        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    //@Test
    public void createALaCarteService_WithUserProvidedNamingFalse_RequestDetailsIsAsExpected() throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generateMockALaCarteServiceInstantiationPayload(false,
                newHashMap(),
                newHashMap(),
                newHashMap(),
                1,
                false, PROJECT_NAME, true, null);

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                asyncInstantiationBL.generateALaCarteServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");

        URL resource = this.getClass().getResource("/payload_jsons/bulk_alacarte_service_request_naming_false.json");
        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    //@Test
    public void generateALaCarteServiceInstantiationRequest_withVnfList_HappyFllow() throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generateALaCarteWithVnfsServiceInstantiationPayload();
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                asyncInstantiationBL.generateALaCarteServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");

        String serviceExpected = IOUtils.toString(this.getClass().getResource("/payload_jsons/bulk_alacarte_service_request.json"), "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(serviceExpected, result);
    }

    //@Test(dataProvider = "createVnfParameters")
    public void createVnfRequestDetails_detailsAreAsExpected(boolean isFlagAddCloudOwnerActive, boolean isUserProvidedNaming, String file) throws IOException {

        final List<Vnf> vnfList = new ArrayList<>(createVnfList(new HashMap<>(), null, isUserProvidedNaming, true).values());
        ModelInfo siModelInfo = createServiceModelInfo();
        String serviceInstanceId = "aa3514e3-5a33-55df-13ab-12abad84e7aa";

        //we validate that the asyncInstantiationBL call to getUniqueName by simulate that aai retrun that original
        //vnf name is used, and only next picked name is free.
        Mockito.reset(aaiClient);
        mockAaiClientAaiStatusOK();
        when(aaiClient.isNodeTypeExistsByName(eq(VNF_NAME), eq(ResourceType.GENERIC_VNF))).thenReturn(true);
        when(aaiClient.isNodeTypeExistsByName(eq(VNF_NAME+"_001"), eq(ResourceType.GENERIC_VNF))).thenReturn(false);
        enableAddCloudOwnerOnMsoRequest(isFlagAddCloudOwnerActive);

        String expected = IOUtils.toString(this.getClass().getResource(file), "UTF-8");
        final RequestDetailsWrapper<VnfInstantiationRequestDetails> result = asyncInstantiationBL.generateVnfInstantiationRequest(vnfList.get(0), siModelInfo, serviceInstanceId, "pa0916");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @DataProvider
    public static Object[][] createVnfParameters() {
        return new Object[][]{
                {true, true, "/payload_jsons/bulk_vnf_request.json"},
                {false, true, "/payload_jsons/bulk_vnf_request_without_cloud_owner.json"},
                {true, false, "/payload_jsons/bulk_vnf_request_without_instance_name.json"},
        };
    }

    @DataProvider
    public static Object[][] vfModuleRequestDetails(Method test) {
        return new Object[][]{
                {"cc3514e3-5a33-55df-13ab-12abad84e7cc", true, "/payload_jsons/vfmodule_instantiation_request.json"},
                {null, true, "/payload_jsons/vfmodule_instantiation_request_without_volume_group.json"},
                {null, false, "/payload_jsons/vfmodule_instantiation_request_without_instance_name.json"}
        };
    }

    //@Test(dataProvider = "vfModuleRequestDetails")
    public void createVfModuleRequestDetails_detailsAreAsExpected(String volumeGroupInstanceId, boolean isUserProvidedNaming, String fileName) throws IOException {

        ModelInfo siModelInfo = createServiceModelInfo();
        ModelInfo vnfModelInfo = createVnfModelInfo(true);
        List<Map<String, String>> instanceParams = ImmutableList.of(ImmutableMap.of("vmx_int_net_len", "24",
                "vre_a_volume_size_0" , "120"));
        Map<String, String> supplementaryParams = ImmutableMap.of("vre_a_volume_size_0" , "100",
                "availability_zone_0" , "mtpocdv-kvm-az01");
        VfModule vfModule = createVfModule("201673MowAvpnVpeBvL..AVPN_vRE_BV..module-1", "56e2b103-637c-4d1a-adc8-3a7f4a6c3240",
                "72d9d1cd-f46d-447a-abdb-451d6fb05fa8", instanceParams, supplementaryParams,
                (isUserProvidedNaming ? "vmxnjr001_AVPN_base_vRE_BV_expansion": null), "myVgName", true);

        String serviceInstanceId = "aa3514e3-5a33-55df-13ab-12abad84e7aa";
        String vnfInstanceId = "bb3514e3-5a33-55df-13ab-12abad84e7bb";

        Mockito.reset(aaiClient);
        mockAaiClientAaiStatusOK();
        enableAddCloudOwnerOnMsoRequest();
        when(aaiClient.isNodeTypeExistsByName(eq("vmxnjr001_AVPN_base_vRE_BV_expansion"), eq(ResourceType.VF_MODULE))).thenReturn(false);

        String expected = IOUtils.toString(this.getClass().getResource(fileName), "UTF-8");
        final RequestDetailsWrapper<VfModuleInstantiationRequestDetails> result = asyncInstantiationBL.generateVfModuleInstantiationRequest(
                vfModule, siModelInfo, serviceInstanceId,
                vnfModelInfo, vnfInstanceId, volumeGroupInstanceId, "pa0916");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    @DataProvider
    public static Object[][] expectedAggregatedParams() {
        return new Object[][]{
                {ImmutableMap.of("a", "b", "c", "d"), ImmutableMap.of("e", "f", "g", "h"), ImmutableList.of(ImmutableMap.of("c", "d", "a", "b", "e", "f", "g", "h"))},
                {ImmutableMap.of("a", "b", "c", "g"), ImmutableMap.of("c", "d", "e", "f"), ImmutableList.of(ImmutableMap.of("a", "b", "c", "d", "e", "f"))},
                {ImmutableMap.of(), ImmutableMap.of("c", "d", "e", "f"), ImmutableList.of(ImmutableMap.of("c", "d", "e", "f"))},
                {ImmutableMap.of("a", "b", "c", "g"), ImmutableMap.of(), ImmutableList.of(ImmutableMap.of("a", "b", "c", "g"))},
                {ImmutableMap.of(), ImmutableMap.of(), ImmutableList.of()},
                {null, ImmutableMap.of(), ImmutableList.of()},
                {ImmutableMap.of(), null, ImmutableList.of()},
        };
    }

    //@Test(dataProvider = "expectedAggregatedParams")
    public void testAggregateInstanceParamsAndSuppFile(Map<String, String> instanceParams, Map<String, String> suppParams, List<VfModuleInstantiationRequestDetails.UserParamMap<String, String>> expected) {
        List<VfModuleInstantiationRequestDetails.UserParamMap<String, String>> aggParams = ((AsyncInstantiationBusinessLogicImpl)asyncInstantiationBL).aggregateAllInstanceParams(instanceParams, suppParams);
        assertThat("Aggregated params are not as expected", aggParams, equalTo(expected));
    }

    @DataProvider
    public static Object[][] expectedNetworkRequestDetailsParameters() {
        return new Object[][]{
            {true, "/payload_jsons/network_instantiation_request.json"},
            {false, "/payload_jsons/network_instantiation_request_without_instance_name.json"}
        };
    }

    //@Test(dataProvider = "expectedNetworkRequestDetailsParameters")
    public void createNetworkRequestDetails_detailsAreAsExpected(boolean isUserProvidedNaming, String filePath) throws IOException {

        final List<Network> networksList = new ArrayList<>(createNetworkList(null, isUserProvidedNaming, true).values());
        ModelInfo siModelInfo = createServiceModelInfo();
        String serviceInstanceId = "aa3514e3-5a33-55df-13ab-12abad84e7aa";

        Mockito.reset(aaiClient);
        mockAaiClientAaiStatusOK();
        enableAddCloudOwnerOnMsoRequest();
        when(aaiClient.isNodeTypeExistsByName(eq(VNF_NAME), eq(ResourceType.L3_NETWORK))).thenReturn(true);
        when(aaiClient.isNodeTypeExistsByName(eq(VNF_NAME+"_001"), eq(ResourceType.L3_NETWORK))).thenReturn(false);

        String expected = IOUtils.toString(this.getClass().getResource(filePath), "UTF-8");
        final RequestDetailsWrapper<NetworkInstantiationRequestDetails> result = asyncInstantiationBL.generateNetworkInstantiationRequest(networksList.get(0), siModelInfo, serviceInstanceId, "pa0916");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    //@Test
    public void createInstanceGroupRequestDetails_detailsAreAsExpected() throws IOException {

        final InstanceGroup instanceGroup = createInstanceGroup(true, Action.Create);
        ModelInfo siModelInfo = createServiceModelInfo();
        String serviceInstanceId = "aa3514e3-5a33-55df-13ab-12abad84e7aa";

        Mockito.reset(aaiClient);
        mockAaiClientAaiStatusOK();
        enableAddCloudOwnerOnMsoRequest();
        when(aaiClient.isNodeTypeExistsByName(eq(VNF_GROUP_NAME), eq(ResourceType.INSTANCE_GROUP))).thenReturn(true);
        when(aaiClient.isNodeTypeExistsByName(eq(VNF_GROUP_NAME+"_001"), eq(ResourceType.INSTANCE_GROUP))).thenReturn(false);

        String expected = IOUtils.toString(this.getClass().getResource("/payload_jsons/instance_group_instantiation_request.json"), "UTF-8");
        final RequestDetailsWrapper<InstanceGroupInstantiationRequestDetails> result = asyncInstantiationBL.generateInstanceGroupInstantiationRequest(instanceGroup, siModelInfo, serviceInstanceId, "az2018");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    //@Test
    public void checkIfNullProjectNameSentToMso(){
        ServiceInstantiation serviceInstantiationPayload = generateMockMacroServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, Collections.EMPTY_LIST, false),
                1,
                false,null,false);
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                asyncInstantiationBL.generateMacroServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");
        JsonNode jsonNode = new ObjectMapper().valueToTree(result.requestDetails);
        Assert.assertTrue(jsonNode.get("project").isNull());
        serviceInstantiationPayload = generateMockMacroServiceInstantiationPayload(true,
                createVnfList(vfModuleInstanceParamsMapWithParamsToRemove, Collections.EMPTY_LIST, false),
                1,
                false,"not null",false);
        result = asyncInstantiationBL.generateMacroServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");
        jsonNode = new ObjectMapper().valueToTree(result.requestDetails);
        Assert.assertTrue(jsonNode.get("project").get("projectName").asText().equalsIgnoreCase("not null"));



    }

    //@Test
    public void pushBulkJob_macroServiceverifyCreatedDateBehavior_createdDateIsTheSameForAllServicesInSameBulk() {
        LocalDateTime startTestDate = LocalDateTime.now().withNano(0);
        final ServiceInstantiation request = generateMockMacroServiceInstantiationPayload(
                false,
                createVnfList(instanceParamsMapWithoutParams, Collections.EMPTY_LIST, true),
                100, true,PROJECT_NAME, true
        );

        pushJobAndAssertDates(startTestDate, request);
    }

    //@Test
    public void whenCreateServiceInfo_thenModelId_isModelVersionId() {
        ServiceInfo serviceInfo = asyncInstantiationBL.createServiceInfo("userID",
                generateALaCarteWithVnfsServiceInstantiationPayload(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Date(),
                "myName", ServiceInfo.ServiceAction.INSTANTIATE);
        assertEquals(SERVICE_MODEL_VERSION_ID, serviceInfo.getServiceModelId());

    }

    //@Test
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
        when(jobAdapterMock.createServiceInstantiationJob(any(), any(), any(), any(), any(), any())).thenReturn(job);
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

    //@Test(dataProvider = "msoToJobStatusDataProvider")
    public void whenGetStatusFromMso_calcRightJobStatus(String msoStatus, Job.JobStatus expectedJobStatus) {
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


    //@Test(dataProvider = "auditStatuses")
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



    //@Test
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

    //@Test(dataProvider = "msoAuditStatuses")
    public void addSomeMsoStatuses_getThem_verifyGetInsertedWithoutDuplicates(UUID jobUuid, ImmutableList<JobAuditStatus> msoStatuses, ImmutableList<String> expectedStatuses, String assertionReason) {
        msoStatuses.forEach(status -> {
            asyncInstantiationBL.auditMsoStatus(status.getJobId(), status.getJobStatus(), status.getRequestId() != null ? status.getRequestId().toString() : null, status.getAdditionalInfo());
        });
        List<String> statusesFromDB = asyncInstantiationBL.getAuditStatuses(jobUuid, SourceStatus.MSO).stream().map(auditStatus -> auditStatus.getJobStatus()).collect(Collectors.toList());
        assertThat( assertionReason, statusesFromDB, is(expectedStatuses));
    }

    //@Test
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

    @DataProvider
    public static Object[][] msoRequestStatusFiles(Method test) {
        return new Object[][]{
                {"/responses/mso/orchestrationRequestsServiceInstance.json"},
                {"/responses/mso/orchestrationRequestsVnf.json"},
                {"/responses/mso/orchestrationRequestsMockedMinimalResponse.json"}
        };
    }

    //@Test(dataProvider="msoRequestStatusFiles")
    public void verifyAsyncRequestStatus_canBeReadFromSample(String msoResponseFile) throws IOException {
        AsyncRequestStatus asyncRequestStatus = TestUtils.readJsonResourceFileAsObject(
                msoResponseFile,
                AsyncRequestStatus.class);
        assertThat(asyncRequestStatus.request.requestStatus.getRequestState(), equalTo("COMPLETE"));
    }

    //@Test
    public void deleteJobInfo_pending_deleted() {
        doNothing().when(jobsBrokerServiceMock).delete(any());
        UUID uuid = createServicesInfoWithDefaultValues(PENDING);
        asyncInstantiationBL.deleteJob(uuid);
        assertNotNull(asyncInstantiationBL.getServiceInfoByJobId(uuid).getDeletedAt(), "service info wasn't deleted");
    }

    //@Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE)
    public void deleteJobInfo_notAllowdStatus_shouldSendError() {
        UUID uuid = createServicesInfoWithDefaultValues(COMPLETED);
        doThrow(new IllegalStateException(DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE)).when(jobsBrokerServiceMock).delete(any());
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

    //@Test(dataProvider = "jobStatusesFinal")
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

    //@Test(dataProvider = "jobStatusesNotFinal",
    //        expectedExceptions = OperationNotAllowedException.class,
    //        expectedExceptionsMessageRegExp = "jobId.*Service status does not allow hide service, status = .*")
    public void hideServiceInfo_notAllowedStatus_shouldSendError(JobStatus jobStatus) {
        UUID uuid = createServicesInfoWithDefaultValues(jobStatus);
        try {
            asyncInstantiationBL.hideServiceInfo(uuid);
        } catch (Exception e) {
            assertFalse(asyncInstantiationBL.getServiceInfoByJobId(uuid).isHidden(), "service info shouldn't be hidden");
            throw e;
        }
    }

    //@Test
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

    //@Test
    public void whenUseGetCounterForSameName_numbersReturnedByOrder() {

        String name = UUID.randomUUID().toString();
        int SIZE=10;
        for (int i=0; i<SIZE; i++) {
            assertThat(asyncInstantiationBL.getCounterForName(name), is(i));
        }
    }

    //@Test
    public void whenNamedInUsedInAai_getNextNumber() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.GENERIC_VNF;
        when(aaiClient.isNodeTypeExistsByName(name, type)).thenReturn(true);
        when(aaiClient.isNodeTypeExistsByName(name+"_001", type)).thenReturn(false);
        assertThat(asyncInstantiationBL.getUniqueName(name, type), equalTo(name+"_001"));
    }

    private String someCommonStepsAndGetName() {
        mockAaiClientAaiStatusOK();
        return UUID.randomUUID().toString();
    }

    private void mockAaiClientAaiStatusOK() {
        when(aaiClient.isNodeTypeExistsByName(eq(AsyncInstantiationBusinessLogicImpl.NAME_FOR_CHECK_AAI_STATUS), any())).thenReturn(false);
    }

    //@Test(expectedExceptions= ExceptionWithRequestInfo.class)
    public void whenAaiBadResponseCode_throwInvalidAAIResponseException() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.SERVICE_INSTANCE;
        when(aaiClient.isNodeTypeExistsByName(name, type)).thenThrow(aaiNodeQueryBadResponseException());
        asyncInstantiationBL.getUniqueName(name, type);
    }

    //@Test(expectedExceptions=MaxRetriesException.class)
    public void whenAaiAlwaysReturnNameUsed_throwInvalidAAIResponseException() {
        String name = someCommonStepsAndGetName();
        ResourceType type = ResourceType.VF_MODULE;
        when(aaiClient.isNodeTypeExistsByName(any(), eq(type))).thenReturn(true);
        asyncInstantiationBL.setMaxRetriesGettingFreeNameFromAai(10);
        asyncInstantiationBL.getUniqueName(name, type);
    }

    //@Test
    public void testFormattingOfNameAndCounter() {
        AsyncInstantiationBusinessLogicImpl bl = (AsyncInstantiationBusinessLogicImpl) asyncInstantiationBL;
        assertThat(bl.formatNameAndCounter("x", 0), equalTo("x"));
        assertThat(bl.formatNameAndCounter("x", 3), equalTo("x_003"));
        assertThat(bl.formatNameAndCounter("x", 99), equalTo("x_099"));
        assertThat(bl.formatNameAndCounter("x", 100), equalTo("x_100"));
        assertThat(bl.formatNameAndCounter("x", 1234), equalTo("x_1234"));
    }

    //@Test
    public void pushBulkJob_verifyAlacarteFlow_useALaCartServiceInstantiationJobType(){
        final ServiceInstantiation request = generateALaCarteServiceInstantiationPayload();

        // in "createServiceInstantiationJob()" we will probe the service, with the generated names
        configureMockitoWithMockedJob();

        ArgumentCaptor<JobType> argumentCaptor = ArgumentCaptor.forClass(JobType.class);
        asyncInstantiationBL.pushBulkJob(request, "myUserId");
        verify(jobAdapterMock).createServiceInstantiationJob(argumentCaptor.capture(),any(),any(),anyString(), anyString(), anyInt());
        assertTrue(argumentCaptor.getValue().equals(JobType.ALaCarteServiceInstantiation));
    }

    //@Test
    public void pushBulkJob_verifyMacroFlow_useMacroServiceInstantiationJobType(){
        final ServiceInstantiation request = generateMacroMockServiceInstantiationPayload(false, Collections.emptyMap());

        // in "createServiceInstantiationJob()" we will probe the service, with the generated names
        configureMockitoWithMockedJob();

        ArgumentCaptor<JobType> argumentCaptor = ArgumentCaptor.forClass(JobType.class);
        asyncInstantiationBL.pushBulkJob(request, "myUserId");
        verify(jobAdapterMock).createServiceInstantiationJob(argumentCaptor.capture(),any(),any(),anyString(), anyString(), anyInt());
        assertTrue(argumentCaptor.getValue().equals(JobType.MacroServiceInstantiation));
    }

    //@Test
    public void generateALaCarteServiceInstantiationRequest_verifyRequestIsAsExpected() throws IOException {
        ServiceInstantiation serviceInstantiationPayload = generateALaCarteServiceInstantiationPayload();
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_alacarte_service_request.json");
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> result =
                asyncInstantiationBL.generateALaCarteServiceInstantiationRequest(null, serviceInstantiationPayload, serviceInstantiationPayload.getInstanceName(), "az2016");
        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    //@Test
    public void generateALaCarteServiceDeletionRequest_verifyRequestIsAsExpected() throws IOException {
        final URL resource = this.getClass().getResource("/payload_jsons/bulk_alacarte_service_deletion_request.json");
        String expected = IOUtils.toString(resource, "UTF-8");

        ServiceInstantiation serviceDeletionPayload = generateALaCarteServiceDeletionPayload();
        RequestDetailsWrapper<ServiceDeletionRequestDetails> result =
                asyncInstantiationBL.generateALaCarteServiceDeletionRequest(null, serviceDeletionPayload, "az2016");

        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    //@Test
    public void getALaCarteServiceDeletionPath_verifyPathIsAsExpected() throws IOException {

        String expected = "/serviceInstantiation/v7/serviceInstances/f36f5734-e9df-4fbf-9f35-61be13f028a1";

        String result = asyncInstantiationBL.getServiceDeletionPath("f36f5734-e9df-4fbf-9f35-61be13f028a1");

        assertThat(expected,equalTo(result));
    }

    //@Test
    public void getInstanceGroupsDeletionPath_verifyPathIsAsExpected()  {

        assertEquals(asyncInstantiationBL.getInstanceGroupDeletePath("9aada4af-0f9b-424f-ae21-e693bd3e005b"),
                "/serviceInstantiation/v7/instanceGroups/9aada4af-0f9b-424f-ae21-e693bd3e005b");
    }

    @DataProvider
    public static Object[][] testBuildVnfInstanceParamsDataProvider(Method test) {
        return new Object[][]{
                {
                    Collections.EMPTY_LIST,
                    ImmutableList.of(
                        ImmutableList.of(ImmutableMap.of("k1","v1","k2","v2")),
                        ImmutableList.of(ImmutableMap.of("k3","v3","k2","v2"))
                    ),
                    true,
                    ImmutableList.of(ImmutableMap.of("k1","v1","k2","v2","k3","v3"))
                },
                {
                        ImmutableList.of(ImmutableMap.of("j1", "w1", "k1","v1", "vnf_name","w2", "vf_module_name","w3")), //vnf_name, vf_module_name are excluded
                        ImmutableList.of(
                                ImmutableList.of(ImmutableMap.of("k1","v1","k2","v2")),
                                ImmutableList.of(ImmutableMap.of("k3","v3","k2","v2")),
                                ImmutableList.of(Collections.EMPTY_MAP),
                                Collections.singletonList(null)
                        ),
                        true,
                        ImmutableList.of(ImmutableMap.of("k1","v1","k2","v2","k3","v3","j1", "w1"))
                },
                {
                        Collections.EMPTY_LIST,
                        Arrays.asList(null, null),
                        true,
                        Collections.EMPTY_LIST //mso is expect to empty list and not list with empty map
                },
                {
                        ImmutableList.of(Collections.EMPTY_MAP),
                        ImmutableList.of(
                                ImmutableList.of(Collections.EMPTY_MAP),
                                ImmutableList.of(Collections.EMPTY_MAP)
                        ),
                        true,
                        Collections.EMPTY_LIST //mso is expect to empty list and not list with empty map
                },
                {
                        Collections.EMPTY_LIST,
                        ImmutableList.of(
                                ImmutableList.of(ImmutableMap.of("k1","v1","k2","v2")),
                                ImmutableList.of(ImmutableMap.of("k3","v3","k2","v2"))
                        ),
                        false,
                        Collections.EMPTY_LIST //mso is expect to empty list and not list with empty map
                },
                {
                        ImmutableList.of(ImmutableMap.of("j1", "w1", "k1","v1", "vnf_name","w2", "vf_module_name","w3")),
                        ImmutableList.of(
                                ImmutableList.of(Collections.EMPTY_MAP)
                        ),
                        false,
                        ImmutableList.of(ImmutableMap.of("j1", "w1", "k1","v1"))
                },
                {
                        ImmutableList.of(ImmutableMap.of("vnf_name","w2", "vf_module_name", "w3", "j2", "w2", "j4","w4")),
                        ImmutableList.of(
                                ImmutableList.of(ImmutableMap.of("k1","v1","k2","v2")),
                                ImmutableList.of(ImmutableMap.of("k3","v3","k2","v2"))
                        ),
                        false,
                        ImmutableList.of(ImmutableMap.of("j2", "w2", "j4","w4"))
                },

        };
    }

    //@Test(dataProvider="testBuildVnfInstanceParamsDataProvider")
    public void testBuildVnfInstanceParams(List<Map<String, String>> currentVnfInstanceParams,
                                           List<List<Map<String, String>>> vfModulesInstanceParams,
                                           boolean isFeatureActive,
                                           List<Map<String,String>> expectedResult){
        when(featureManager.isActive(Features.FLAG_SHIFT_VFMODULE_PARAMS_TO_VNF)).thenReturn(isFeatureActive);
        List<VfModuleMacro> vfModules =
                vfModulesInstanceParams.stream().map(params-> new VfModuleMacro(new ModelInfo(), null, null, params)).collect(Collectors.toList());
        List<Map<String,String>> actual = asyncInstantiationBL.buildVnfInstanceParams(currentVnfInstanceParams, vfModules);
        assertThat(actual, equalTo(expectedResult));

    }

    //@Test
    public void whenLcpRegionNotEmpty_thenCloudRegionIdOfResourceIsLegacy() {
        String legacyCloudRegion = "legacyCloudRegion";
        Vnf vnf = new Vnf(new ModelInfo(), null, null, Action.Create.name(), null, "anyCloudRegion", legacyCloudRegion, null, null, null, false, null, null);
        assertThat(vnf.getLcpCloudRegionId(), equalTo(legacyCloudRegion));


    }

    //@Test
    public void whenLcpRegionNotEmpty_thenCloudRegionIdOfServiceIsLegacy() {
        String legacyCloudRegion = "legacyCloudRegion";
        ServiceInstantiation service = new ServiceInstantiation(new ModelInfo(), null, null, null, null, null, null,
                null, null, "anyCloudRegion", legacyCloudRegion, null, null, null, null, null, null, null, null,
                false, 1,false, false, null, null, Action.Create.name());
        assertThat(service.getLcpCloudRegionId(), equalTo(legacyCloudRegion));
    }

    //@Test
    public void createVolumeGroup_verifyResultAsExpected() throws IOException {
        final URL resource = this.getClass().getResource("/payload_jsons/volumegroup_instantiation_request.json");
        VfModule vfModule = createVfModule("201673MowAvpnVpeBvL..AVPN_vRE_BV..module-1",
                "56e2b103-637c-4d1a-adc8-3a7f4a6c3240",
                "72d9d1cd-f46d-447a-abdb-451d6fb05fa8",
                Collections.emptyList(),
                Collections.emptyMap(),
                "vmxnjr001_AVPN_base_vRE_BV_expansion",
                "myVgName",
                true);
        vfModule.getModelInfo().setModelInvariantId("ff5256d2-5a33-55df-13ab-12abad84e7ff");
        vfModule.getModelInfo().setModelVersion("1");
        ModelInfo vnfModelInfo = createVnfModelInfo(true);
        RequestDetailsWrapper<VolumeGroupRequestDetails> result =
                asyncInstantiationBL.generateVolumeGroupInstantiationRequest(vfModule,
                        createServiceModelInfo(),
                       "ff3514e3-5a33-55df-13ab-12abad84e7ff",
                        vnfModelInfo,
                        "vnfInstanceId",
                        "az2016");
        String expected = IOUtils.toString(resource, "UTF-8");
        MsoOperationalEnvironmentTest.assertThatExpectationIsLikeObject(expected, result);
    }

    //@Test
    public void getJobTypeByRequest_verifyResultAsExpected(){
        ServiceInstantiation service = new ServiceInstantiation(new ModelInfo(), null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                false, 1,false, false, null, null, Action.Create.name());
        JobType jobType = asyncInstantiationBL.getJobType(service) ;
        assertThat(jobType, equalTo(JobType.MacroServiceInstantiation));
        service = new ServiceInstantiation(new ModelInfo(), null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                false, 1,false, true, null, null, Action.Create.name());
        jobType = asyncInstantiationBL.getJobType(service);
        assertThat(jobType, equalTo(JobType.ALaCarteServiceInstantiation));
        service = new ServiceInstantiation(new ModelInfo(), null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                false, 1,false, true, null, null, Action.Delete.name());
        jobType = asyncInstantiationBL.getJobType(service);
        assertThat(jobType, equalTo(JobType.ALaCarteService));
    }

    protected ServiceInstantiation generateALaCarteServiceInstantiationPayload() {
        return generateMockALaCarteServiceInstantiationPayload(false, Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, 1, true, PROJECT_NAME, false, "VNF_API");
    }

    private ServiceInstantiation generateALaCarteServiceDeletionPayload() {
        return generateMockALaCarteServiceDeletionPayload(false, Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, 1, true, PROJECT_NAME, false, "VNF_API", "1234567890");
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
            return new JobSharedData(uuid, "", null);
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
}
