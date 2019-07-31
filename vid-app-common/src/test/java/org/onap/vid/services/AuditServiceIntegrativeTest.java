package org.onap.vid.services;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Every.everyItem;
import static org.onap.vid.job.Job.JobStatus.COMPLETED;
import static org.onap.vid.job.Job.JobStatus.IN_PROGRESS;
import static org.onap.vid.job.Job.JobStatus.PAUSE;
import static org.onap.vid.job.Job.JobStatus.PENDING;
import static org.onap.vid.utils.DaoUtils.getPropsMap;
import static org.testng.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.SessionFactory;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.JobCommandsConfigWithMockedMso;
import org.onap.vid.config.MockedAaiClientAndFeatureManagerConfig;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.impl.JobDaoImpl;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.JobAuditStatus.SourceStatus;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.mso.rest.AsyncRequestStatusList;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, MockedAaiClientAndFeatureManagerConfig.class, JobCommandsConfigWithMockedMso.class})
public class AuditServiceIntegrativeTest extends AbstractTestNGSpringContextTests {

    @Inject
    private DataAccessService dataAccessService;

    @Inject
    private AuditService auditService;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeClass
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    void clearDb() {
        dataAccessService.deleteDomainObjects(JobDaoImpl.class, "1=1", getPropsMap());
        dataAccessService.deleteDomainObjects(ServiceInfo.class, "1=1", getPropsMap());
        dataAccessService.deleteDomainObjects(JobAuditStatus.class, "1=1", getPropsMap());
        dataAccessService.deleteDomainObjects(NameCounter.class, "1=1", getPropsMap());
    }


    @Test
    public void testConvertMsoResponseStatusToJobAuditStatus_missingDateFromMso_shouldNoError() throws IOException {
        final AsyncRequestStatusList asyncRequestStatusList = TestUtils.readJsonResourceFileAsObject("/orchestrationRequestsByServiceInstanceId.json", AsyncRequestStatusList.class);

        AuditServiceImpl auditService = new AuditServiceImpl(null, null);
        final List<JobAuditStatus> jobAuditStatuses = auditService.convertMsoResponseStatusToJobAuditStatus(asyncRequestStatusList.getRequestList(), "foo");

        final List<Date> dates = jobAuditStatuses.stream().map(JobAuditStatus::getCreatedDate).collect(toList());
        final List<String> statuses = jobAuditStatuses.stream().map(JobAuditStatus::getJobStatus).collect(toList());

        assertThat(dates, containsInAnyOrder(notNullValue(), notNullValue(), nullValue()));
        assertThat(statuses, containsInAnyOrder("COMPLETE", "COMPLETE", "IN_PROGRESS"));
    }


    @DataProvider
    public static Object[][] resourceTypeFilter() {

        return new Object[][]{
                {JobAuditStatus.ResourceTypeFilter.SERVICE, "serviceInstanceId"},
                {JobAuditStatus.ResourceTypeFilter.VNF,"vnfInstanceId"},
                {JobAuditStatus.ResourceTypeFilter.VFMODULE,"vfModuleInstanceId"},
                {JobAuditStatus.ResourceTypeFilter.NETWORK,"networkInstanceId"},
                {JobAuditStatus.ResourceTypeFilter.VNFGROUP,"instanceGroupId"},
        };

    }


    //In case any of ResourceTypeFilter are accidentally changed or removed
    @Test(dataProvider = "resourceTypeFilter")
    public void testAllInstanceTypesForAuditInfoExist(JobAuditStatus.ResourceTypeFilter resourceTypeFilter, String value){
        assertThat("ResourceTypeFilter value changed! Check the relevant usage vs MSO before changing it.", resourceTypeFilter.getFilterBy().equals(value));
    }

    private static final String MSO_ARBITRARY_STATUS = "completed mso status";

    @DataProvider
    public static Object[][] auditStatuses(Method test) {
        return new Object[][]{
                {
                        SourceStatus.VID,
                        new String[]{ PENDING.toString(), IN_PROGRESS.toString(), COMPLETED.toString()}
                },
                {       SourceStatus.MSO,
                        new String[]{ IN_PROGRESS.toString(), MSO_ARBITRARY_STATUS }
                }
        };

    }

    @Test(dataProvider = "auditStatuses")
    public void givenSomeAuditStatuses_getStatusesOfSpecificSourceAndJobId_getSortedResultsMatchingToParameters(
        SourceStatus expectedSource, String [] expectedSortedStatuses){
        UUID jobUuid = UUID.randomUUID();

        final Date nowMinus3 = toDate(LocalDateTime.now().minusHours(3));
        final Date nowMinus2 = toDate(LocalDateTime.now().minusHours(2));
        final Date nowMinus30 = toDate(LocalDateTime.now().minusHours(30));

        List<JobAuditStatus> auditStatusList = com.google.common.collect.ImmutableList.of(
            JobAuditStatus.createForTest(jobUuid, PENDING.toString(), SourceStatus.VID, nowMinus3, 0),
            JobAuditStatus.createForTest(jobUuid, IN_PROGRESS.toString(), SourceStatus.VID, nowMinus3, 1),
            JobAuditStatus.createForTest(jobUuid, IN_PROGRESS.toString(), SourceStatus.MSO, UUID.randomUUID(),"", nowMinus30),
            JobAuditStatus.createForTest(jobUuid, COMPLETED.toString(), SourceStatus.VID, nowMinus2, 2),
            JobAuditStatus.createForTest(jobUuid, MSO_ARBITRARY_STATUS, SourceStatus.MSO, UUID.randomUUID(),"", nowMinus3),

            JobAuditStatus.createForTest(UUID.randomUUID(), PENDING.toString(), SourceStatus.VID, nowMinus3, 0)
        );
        auditStatusList.forEach((auditStatus) -> createNewAuditStatus(auditStatus));

        List<JobAuditStatus> statuses = auditService.getAuditStatuses(jobUuid, expectedSource);
        List<String> statusesList = statuses.stream().map(status -> status.getJobStatus()).collect(toList());

        assertTrue(statuses.stream().allMatch(status -> (status.getSource().equals(expectedSource)&& status.getJobId().equals(jobUuid))),"Only statuses of " + expectedSource + " for " + jobUuid + " should be returned. Returned statuses: " + String.join(",", statusesList ));
        assertThat(statusesList + " is not ok", statusesList, contains(expectedSortedStatuses));
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void createNewAuditStatus(JobAuditStatus auditStatus)
    {
        Date createdDate= auditStatus.getCreated();
        dataAccessService.saveDomainObject(auditStatus, getPropsMap());
        setDateToStatus(auditStatus.getSource(), auditStatus.getJobStatus(), createdDate);
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

    @Test
    public void addSomeVidStatuses_getThem_verifyGetIsWithoutDuplicates(){
        final Stream<JobStatus> statusesToPush = Stream.of(PENDING, IN_PROGRESS, IN_PROGRESS, PAUSE, PAUSE, IN_PROGRESS, COMPLETED);
        final Stream<JobStatus> statusesToExpect = Stream.of(PENDING, IN_PROGRESS, PAUSE, IN_PROGRESS, COMPLETED);
        final UUID jobUuid = UUID.randomUUID();

        // Set up
        statusesToPush.forEach(status-> auditService.auditVidStatus(jobUuid, status));

        // Given
        List<JobAuditStatus> auditStatusesFromDB = auditService.getAuditStatuses(jobUuid, SourceStatus.VID);
        List<Pair<String, Integer>> statusesAndOrdinalsFromDB =
            auditStatusesFromDB.stream()
                .map(status -> Pair.of(status.getJobStatus(), status.getOrdinal()))
                .collect(toList());

        // Assert that
        List<Pair<String, Integer>> expectedStatusesAndOrdinals =
            Streams.zip(
                statusesToExpect.map(Objects::toString),
                IntStream.range(0, 100).boxed(), // max=100 will be truncated
                Pair::of
            ).collect(toList());

        assertThat(statusesAndOrdinalsFromDB, is(expectedStatusesAndOrdinals));
    }

    @Test
    public void addSameStatusOfVidAndMso_verifyThatBothWereAdded(){
        UUID jobUuid = UUID.randomUUID();
        Job.JobStatus sameStatus = IN_PROGRESS;
        auditService.auditMsoStatus(jobUuid, sameStatus.toString(),null,null);
        auditService.auditVidStatus(jobUuid, sameStatus);
        List<JobAuditStatus> list = dataAccessService.getList(
                JobAuditStatus.class,
                String.format(" where JOB_ID = '%s'", jobUuid),
                null, null);
        Assert.assertEquals(list.size(),2);
        assertThat(list,everyItem(hasProperty("jobStatus", is(sameStatus.toString()))));
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
            auditService.auditMsoStatus(status.getJobId(), status.getJobStatus(), status.getRequestId() != null ? status.getRequestId().toString() : null, status.getAdditionalInfo());
        });
        List<String> statusesFromDB = auditService.getAuditStatuses(jobUuid, SourceStatus.MSO).stream().map(auditStatus -> auditStatus.getJobStatus()).collect(
            toList());
        assertThat( assertionReason, statusesFromDB, is(expectedStatuses));
    }
}