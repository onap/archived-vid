package org.onap.vid.job.command;

import com.google.common.collect.ImmutableMap;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.job.*;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.serviceInstantiation.Network;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.properties.Features;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.core.env.Environment;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.onap.vid.job.Job.JobStatus.*;

public class ServiceInProgressStatusCommandTest {


    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Mock
    private JobsBrokerService jobsBrokerService;

    @Mock
    private JobAdapter jobAdapter;

    @Mock
    private FeatureManager featureManager;

    @Mock
    private JobSharedData sharedData;

    @Mock
    private Environment environment;

    @Mock
    private ServiceInstantiation request;

    @Mock
    private InProgressStatusService inProgressStatusService;

    @InjectMocks
    private ServiceInProgressStatusCommand command = new ServiceInProgressStatusCommand();

    @DataProvider
    public static Object[][] isNeedToCreateChildJobsDataProvider() {
        return new Object[][]{
                {new TreeMap<String,Vnf>() ,                 true, true, false},
                {null ,                                      true, true, false},
                {ImmutableMap.of("a",mock(Vnf.class)),   false, true, false},
                {ImmutableMap.of("a",mock(Vnf.class)),   true, false, false},
                {ImmutableMap.of("a",mock(Vnf.class)),   true, true, true},
        };
    }

    @DataProvider
    public static Object[][] processJobStatusData() {
        return new Object[][]{
                /* {MSO jobStatus, jobStartTime, isNeedToCreateChildJobs(), property vid.job.max.hoursInProgress, expected nextCommand.getStatus() } */
                {IN_PROGRESS,           false, IN_PROGRESS},
                {FAILED,                false, FAILED},
                {PAUSE,                 false, IN_PROGRESS},
                {COMPLETED,             false, COMPLETED},
                {COMPLETED,             true,  IN_PROGRESS},
                {RESOURCE_IN_PROGRESS,  false, RESOURCE_IN_PROGRESS},
                {PENDING,               false, PENDING},
                {STOPPED,               false, STOPPED},
                {COMPLETED_WITH_ERRORS, false, COMPLETED_WITH_ERRORS},
                {CREATING,              false, CREATING}
        };
    }

    @DataProvider
    public static Object[][] isExpiredJobStatusData() {
        return new Object[][]{
                {ZonedDateTime.now(), "24", false},
                {getTimeNowMinus(2),  "1",  true},
                {getTimeNowMinus(24), "24",  true},
                {getTimeNowMinus(2),  "0",  false},
                {getTimeNowMinus(2),  "-1", false},
                {getTimeNowMinus(2),  "",   false},
                {getTimeNowMinus(2),  "a",  false}
        };
    }

    private static ZonedDateTime getTimeNowMinus(int hoursAgo) {
        return ZonedDateTime.ofInstant(Instant.now().minus(hoursAgo, ChronoUnit.HOURS), ZoneOffset.UTC);
    }

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "isNeedToCreateChildJobsDataProvider" )
    public void testIsNeedToCreateChildJobs(Map<String, Vnf> serviceVnfs, boolean isALaCarte,
                                            boolean isFeatureEnabled, boolean expected) {
        MockitoAnnotations.initMocks(this);
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class);
        when(serviceInstantiation.getVnfs()).thenReturn(serviceVnfs);
        when(serviceInstantiation.isALaCarte()).thenReturn(isALaCarte);
        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VNF)).thenReturn(isFeatureEnabled);
        assertThat(command.isNeedToCreateChildJobs(serviceInstantiation), is(expected));
    }

    @Test
    public void whenGetFromMsoCompletedAndALaCarte_generateNewJobsForVnfs() {
        UUID uuid = UUID.randomUUID();
        String userId = "mockedUserID";
        Vnf vnf1 = mock(Vnf.class);
        Vnf vnf2 = mock(Vnf.class);
        Network network1 = mock(Network.class);
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class);
        when(serviceInstantiation.getVnfs()).thenReturn(ImmutableMap.of("a", vnf1, "b", vnf2));
        when(serviceInstantiation.getNetworks()).thenReturn(ImmutableMap.of("c", network1));
        when(serviceInstantiation.isALaCarte()).thenReturn(true);
        when(serviceInstantiation.getModelInfo()).thenReturn(new ModelInfo());

        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VNF)).thenReturn(true);

        UUID uuid1 = UUID.fromString("12345678-1234-1234-1234-123456789012");
        UUID uuid2 = UUID.fromString("12345678-1234-1234-1234-123456789013");
        UUID uuid3 = UUID.fromString("12345678-1234-1234-1234-123456789014");
        when(jobsBrokerService.add(any())).thenReturn(uuid1).thenReturn(uuid2).thenReturn(uuid3);

        JobSharedData sharedData = new JobSharedData(uuid, userId, serviceInstantiation);
        command.init(sharedData, "", "");
        when(inProgressStatusService.call(any(), eq(sharedData), any())).thenReturn(Job.JobStatus.COMPLETED);
        NextCommand nextCommand = command.call();

        ArgumentCaptor<JobAdapter.AsyncJobRequest> argumentCaptor = ArgumentCaptor.forClass(JobAdapter.AsyncJobRequest.class);
        verify(jobAdapter, times(2)).createChildJob(eq(JobType.VnfInstantiation), eq(Job.JobStatus.CREATING), argumentCaptor.capture(), eq(sharedData), any());
        verify(jobAdapter, times(1)).createChildJob(eq(JobType.NetworkInstantiation), eq(Job.JobStatus.CREATING), argumentCaptor.capture(), eq(sharedData), any());
        assertThat(argumentCaptor.getAllValues(), containsInAnyOrder(vnf1, vnf2, network1));

        verify(jobsBrokerService, times(3)).add(any());

        //verify we don't update service info during this case, which shall stay in_progress
        verify(asyncInstantiationBL, never()).updateServiceInfo(any(), any());

        assertThat(nextCommand.getStatus(), is(Job.JobStatus.IN_PROGRESS));
        assertThat(nextCommand.getCommand().getType(), is(new WatchingCommand().getType()));
        assertThat(nextCommand.getCommand().getData().get("childrenJobs"), is(Arrays.asList(uuid1.toString(), uuid2.toString(), uuid3.toString())));
        assertThat(nextCommand.getCommand().getData().get("isService"), is(true));
    }

    @Test(dataProvider = "processJobStatusData")
    public void processJobStatusTest(Job.JobStatus jobStatus, boolean isNeedToCreateChildJobs, Job.JobStatus expectedStatus) {

        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VNF)).thenReturn(true);
        // All mocks under are used for isNeedToCreateChildJobs=true case
        when(sharedData.getRequest()).thenReturn(request);
        when(request.isALaCarte()).thenReturn(true);
        Map vnfs = mock(Map.class);
        ModelInfo modelInfo = mock(ModelInfo.class);
      
        // if vnfs.isEmpty -> isNeedToCreateChildJobs will return false
        when(vnfs.isEmpty()).thenReturn(!isNeedToCreateChildJobs);
      
        when(request.getVnfs()).thenReturn(vnfs);
        when(request.getModelInfo()).thenReturn(modelInfo);
        command.instanceId = "MockInstId";

        NextCommand nextCommand = command.processJobStatus(jobStatus);
        Assert.assertEquals(nextCommand.getStatus(), expectedStatus);
        if (isNeedToCreateChildJobs) {
            Assert.assertEquals(nextCommand.getCommand().getClass(), WatchingCommand.class);
        } else {
            Assert.assertEquals(nextCommand.getCommand(), command);
        }
    }

    @Test(dataProvider = "isExpiredJobStatusData")
    public void isExpiredJobStatusTest(ZonedDateTime jobStartTime, String configValue, boolean expectedResult) {
        SystemProperties systemProperties = new SystemProperties();
        systemProperties.setEnvironment(environment);
        when(environment.getRequiredProperty(VidProperties.VID_JOB_MAX_HOURS_IN_PROGRESS)).thenReturn(configValue);
        when(environment.containsProperty(VidProperties.VID_JOB_MAX_HOURS_IN_PROGRESS)).thenReturn(true);
        Assert.assertEquals(command.getExpiryChecker().isExpired(jobStartTime), expectedResult);
    }
}
