package org.onap.vid.services;

//
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableMap;
//import org.apache.commons.lang.RandomStringUtils;
//import org.apache.commons.lang3.RandomUtils;
//import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;
//import org.hibernate.SessionFactory;
//import org.onap.vid.exceptions.GenericUncheckedException;
//import org.onap.vid.exceptions.OperationNotAllowedException;
//import org.onap.vid.job.Job;
//import org.onap.vid.job.JobAdapter;
//import org.onap.vid.job.JobType;
//import org.onap.vid.job.JobsBrokerService;
//import org.onap.vid.job.impl.JobDaoImpl;
//import org.onap.vid.job.impl.JobsBrokerServiceInDatabaseImpl;
//import org.onap.vid.utils.DaoUtils;
//import org.onap.vid.config.DataSourceConfig;
//import org.onap.vid.config.JobAdapterConfig;
//import org.onap.portalsdk.core.domain.support.DomainVo;
//import org.onap.portalsdk.core.service.DataAccessService;
//import org.onap.portalsdk.core.util.SystemProperties;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.Assert;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import javax.inject.Inject;
//import java.lang.reflect.Method;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//import java.util.stream.Stream;
//
//import static java.util.concurrent.TimeUnit.MILLISECONDS;
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.both;
//import static org.hamcrest.Matchers.containsInAnyOrder;
//import static org.onap.vid.job.Job.JobStatus.*;
//import static org.onap.vid.utils.Streams.not;
//import static org.testng.Assert.assertNotNull;
//import static org.testng.AssertJUnit.assertEquals;
//
//@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, JobAdapterConfig.class})
//public class JobsBrokerServiceTest extends AbstractTestNGSpringContextTests {
//
//    private static final int JOBS_COUNT = 127;
//    private static final boolean DELETED = true;
//    private final ExecutorService executor = Executors.newFixedThreadPool(90);
//
//    private final Set<Long> threadsIds = new ConcurrentSkipListSet<>();
//
//    private final long FEW = 500;
//
//    private final String JOBS_SHOULD_MATCH = "the jobs that added and those that pulled must be the same";
//    private final String JOBS_PEEKED_SHOULD_MATCH = "the jobs that added and those that peeked must be the same";
//    private static final String DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE = "Service status does not allow deletion from the queue";
//    private static final String DELETE_SERVICE_NOT_EXIST_EXCEPTION_MESSAGE = "Service does not exist";
//    private JobsBrokerService broker;
//
//    @Inject
//    JobAdapter jobAdapter;
//    @Inject
//    private DataAccessService dataAccessService;
//    @Inject
//    private SessionFactory sessionFactory;
//
//    /*
//    - pulling jobs is limited to inserted ones
//    - putting back allows getting the job again
//    - multi threads safety
//    - any added job should be visible with view
//
//    - edges:
//        - pulling with empty repo should return empty optional
//        - pulling more than expected should return empty optional
//        - putting one, over-pulling from a different thread
//        - take before inserting, then insert while waiting
//
//     */
//
//    private class NoJobException extends RuntimeException {
//    }
//
//    private Future<Job> newJobAsync(JobsBrokerService b) {
//        return newJobAsync(b, createMockJob("user id"));
//    }
//
//    private Future<Job> newJobAsync(JobsBrokerService b, Job.JobStatus status) {
//        return newJobAsync(b, createMockJob("user id", status));
//    }
//
//    private Job createMockJob(String userId) {
//        return jobAdapter.createJob(
//                JobType.NoOp,
//                new JobAdapter.AsyncJobRequest() {
//                    public int nothing = 42;
//                },
//                UUID.randomUUID(),
//                userId,
//                RandomUtils.nextInt());
//    }
//
//    private Job createMockJob(String userId, Job.JobStatus jobStatus) {
//        Job job = createMockJob(userId);
//        job.setStatus(jobStatus);
//        return job;
//    }
//
//    private Future<Job> newJobAsync(JobsBrokerService b, Job job) {
//        final Future<Job> jobFuture = executor.submit(() -> {
//            accountThreadId();
//
//            b.add(job);
//
//            return job;
//        });
//        return jobFuture;
//    }
//
//    private void pushBackJobAsync(JobsBrokerService b, Job job) {
//        executor.submit(() -> {
//            accountThreadId();
//            b.pushBack(job);
//            return job;
//        });
//    }
//
//    private Future<Optional<Job>> pullJobAsync(JobsBrokerService broker) {
//        final Future<Optional<Job>> job = executor.submit(() -> {
//            accountThreadId();
//            // Pull only pending jobs, as H2 database does not support our SQL for in-progress jobs
//            return broker.pull(Job.JobStatus.PENDING, UUID.randomUUID().toString());
//        });
//        return job;
//    }
//
//    private Job waitForFutureOptionalJob(Future<Optional<Job>> retrievedOptionalJobFuture) {
//        try {
//            return retrievedOptionalJobFuture.get(FEW, MILLISECONDS).orElseThrow(NoJobException::new);
//        } catch (TimeoutException | InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private Job waitForFutureJob(Future<Job> retrievedJobFuture) {
//        try {
//            return retrievedJobFuture.get(FEW, MILLISECONDS);
//        } catch (TimeoutException | InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private List<Job> putAndGetALotOfJobs(JobsBrokerService broker) {
//        final List<Job> originalJobs = putALotOfJobs(broker);
//        final List<Job> retrievedJobs = getAlotOfJobs(broker);
//
//        assertThat(JOBS_SHOULD_MATCH, retrievedJobs, containsInAnyOrder(originalJobs.toArray()));
//
//        return retrievedJobs;
//    }
//
//    private List<Job> putALotOfJobs(JobsBrokerService broker) {
//        int n = JOBS_COUNT;
//        return IntStream.range(0, n)
//                .mapToObj(i -> newJobAsync(broker))
//                .map(this::waitForFutureJob)
//                .collect(Collectors.toList());
//    }
//
//    private List<Job> getAlotOfJobs(JobsBrokerService broker) {
//        int n = JOBS_COUNT;
//        return IntStream.range(0, n)
//                .mapToObj(i -> pullJobAsync(broker))
//                .map(this::waitForFutureOptionalJob)
//                .collect(Collectors.toList());
//    }
//
//    private void pushBackJobs(List<Job> jobs, JobsBrokerService broker) {
//        jobs.forEach(job -> pushBackJobAsync(broker, job));
//    }
//
//    private void accountThreadId() {
//        threadsIds.add(Thread.currentThread().getId());
//    }
//
//    @AfterMethod
//    public void threadsCounter() {
//        System.out.println("participating threads count: " + threadsIds.size());
//        threadsIds.clear();
//    }
//
//    @BeforeMethod
//    public void initializeBroker() {
//        broker = new JobsBrokerServiceInDatabaseImpl(dataAccessService, sessionFactory, 200, 0);
//        ((JobsBrokerServiceInDatabaseImpl) broker).deleteAll();
//    }
//
//    @Test
//    public void givenSingleJob_getIt_verifySameJob() {
//        final Job originalJob = waitForFutureJob(newJobAsync(broker));
//
//        final Job retrievedJob = waitForFutureOptionalJob(pullJobAsync(broker));
//        assertThat(JOBS_SHOULD_MATCH, retrievedJob, is(originalJob));
//    }
//
//    @Test
//    public void givenManyJobs_getJobsAndPushThemBack_alwaysSeeAllOfThemWithPeek() throws InterruptedException {
//        final List<Job> originalJobs = putALotOfJobs(broker);
//
//        MILLISECONDS.sleep(FEW);
//        assertThat(JOBS_PEEKED_SHOULD_MATCH, broker.peek(), containsInAnyOrder(originalJobs.toArray()));
//
//        final Job retrievedJob = waitForFutureOptionalJob(pullJobAsync(broker));
//
//        MILLISECONDS.sleep(FEW);
//        assertThat(JOBS_PEEKED_SHOULD_MATCH, broker.peek(), containsInAnyOrder(originalJobs.toArray()));
//
//        pushBackJobAsync(broker, retrievedJob);
//
//        MILLISECONDS.sleep(FEW);
//        assertThat(JOBS_PEEKED_SHOULD_MATCH, broker.peek(), containsInAnyOrder(originalJobs.toArray()));
//    }
//
//    @Test
//    public void givenManyJobs_getThemAll_verifySameJobs() {
//        putAndGetALotOfJobs(broker);
//    }
//
//    @Test
//    public void givenManyJobs_getThemAllThenPushBackandGet_verifySameJobs() {
//        final List<Job> retrievedJobs1 = putAndGetALotOfJobs(broker);
//
//        pushBackJobs(retrievedJobs1, broker);
//        final List<Job> retrievedJobs2 = getAlotOfJobs(broker);
//
//        assertThat(JOBS_SHOULD_MATCH, retrievedJobs2, containsInAnyOrder(retrievedJobs1.toArray()));
//    }
//
//    private static Date toDate(LocalDateTime localDateTime) {
//        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//    }
//
//    private void setModifiedDateToJob(UUID jobUuid, Date date) {
//        DomainVo job = dataAccessService.getDomainObject(JobDaoImpl.class, jobUuid, DaoUtils.getPropsMap());
//        job.setModified(date);
//        DaoUtils.tryWithSessionAndTransaction(sessionFactory, session -> {
//            session.saveOrUpdate(job);
//            return 1;
//        });
//    }
//
//
//    public static JobDaoImpl createNewJob(Integer indexInBulk, UUID templateId, String userId, Job.JobStatus status, String takenBy, LocalDateTime date) {
//        return createNewJob(indexInBulk, templateId, userId, status, takenBy, date, false);
//    }
//
//    public static JobDaoImpl createNewJob(Integer indexInBulk, UUID templateId, String userId, Job.JobStatus status, String takenBy, LocalDateTime date, boolean deleted){
//        JobDaoImpl job = new JobDaoImpl();
//        job.setTypeAndData(JobType.NoOp, ImmutableMap.of("x", RandomStringUtils.randomAlphanumeric(15)));
//        job.setIndexInBulk(indexInBulk);
//        job.setTemplateId(templateId);
//        job.setType(JobType.NoOp);
//        job.setStatus(status);
//        job.setTakenBy(takenBy);
//        job.setCreated(toDate(date));
//        job.setModified(toDate(date));
//        job.setUserId(userId);
//        if (deleted) {
//            job.setDeletedAt(new Date());
//        }
//        return job;
//    }
//
//    @DataProvider
//    public static Object[][] jobs(Method test) {
//        LocalDateTime oldestDate = LocalDateTime.now().minusHours(30);
//        UUID sameTemplate = UUID.randomUUID();
//        return new Object[][]{
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userId", PENDING, null, oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userId", PENDING, null, oldestDate),
//                        createNewJob(11, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(2)),
//                        createNewJob(44, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(5))),
//                        4,
//                        0,
//                        PENDING,
//                        "Broker should pull the first pending job by oldest date then by job index"
//                },
//                { ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userId", COMPLETED,null, oldestDate),
//                        createNewJob(11, UUID.randomUUID(), "userId", PENDING,null, oldestDate, DELETED),createNewJob(12, UUID.randomUUID(), "userId", FAILED,null, oldestDate),
//                        createNewJob(13, UUID.randomUUID(), "userId", IN_PROGRESS,null, oldestDate),
//                        createNewJob(14, UUID.randomUUID(), "userId", STOPPED,null, oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userId", PENDING,null, oldestDate),
//                        createNewJob(33, UUID.randomUUID(), "userId", PENDING,null, LocalDateTime.now().minusHours(2))),
//                  6,
//                  5,
//                  PENDING,
//                  "Broker should pull the only pending - first pending job by oldest job - ignore deleted,completed, failed, in-progress and stopped statuses"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(33, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
//                        2,
//                        -1,
//                        PENDING,
//                        "Broker should not pull any job when it exceeded mso limit with count (in-progress) statuses"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userId", PENDING, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(33, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
//                        2,
//                        -1,
//                        PENDING,
//                        "Broker should not pull any job when it exceeded mso limit with count(in-progress or pending && taken) statuses"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userId", PENDING, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(33, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
//                        3,
//                        2,
//                        PENDING,
//                        "Broker should pull first job when it doesn't exceeded mso limit with count(in-progress or pending && taken) statuses"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, sameTemplate, "userId", PENDING, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(22, sameTemplate, "userId", PENDING, null, oldestDate),
//                        createNewJob(33, sameTemplate, "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
//                        3,
//                        -1,
//                        PENDING,
//                        "Broker should not pull any job when there is another job from this template that was taken"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, sameTemplate, "userId", IN_PROGRESS, null, oldestDate),
//                        createNewJob(22, sameTemplate, "userId", PENDING, null, oldestDate),
//                        createNewJob(33, sameTemplate, "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
//                        3,
//                        -1,
//                        PENDING,
//                        "Broker should not pull any job when there is another job from this template that in progress"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, sameTemplate, "userId", FAILED, null, oldestDate),
//                        createNewJob(22, sameTemplate, "userId", STOPPED, null, oldestDate),
//                        createNewJob(33, sameTemplate, "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
//                        3,
//                        -1,
//                        PENDING,
//                        "Broker should not pull any job when there is another job from this template that was failed"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, sameTemplate, "userId", FAILED, null, oldestDate, DELETED),
//                        createNewJob(22, sameTemplate, "userId", STOPPED,null, oldestDate),
//                        createNewJob(33, sameTemplate, "userId", PENDING,null, LocalDateTime.now().minusHours(2))),
//                   3,
//                   2,
//                   PENDING,
//                   "Broker should pull pending job when there is another job from this template that was deleted, although failed"
//                },
//                { ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userA", IN_PROGRESS, null, oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userA", PENDING, null, oldestDate),
//                        createNewJob(33, UUID.randomUUID(), "userB", PENDING, null, LocalDateTime.now().minusHours(2))),
//                        3,
//                        2,
//                        PENDING,
//                        "Broker should prioritize jobs of user that has no in-progress jobs"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userA", PENDING, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userA", PENDING, null, oldestDate),
//                        createNewJob(33, UUID.randomUUID(), "userB", PENDING, null, LocalDateTime.now().minusHours(2))),
//                        3,
//                        2,
//                        PENDING,
//                        "Broker should prioritize jobs of user that has no taken jobs"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userA", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userA", PENDING, null, LocalDateTime.now().minusHours(2)),
//                        createNewJob(31, UUID.randomUUID(), "userB", IN_PROGRESS, null, LocalDateTime.now().minusHours(2)),
//                        createNewJob(32, UUID.randomUUID(), "userB", IN_PROGRESS, null, LocalDateTime.now().minusHours(2)),
//                        createNewJob(33, UUID.randomUUID(), "userB", PENDING, null, oldestDate)),
//                        5,
//                        4,
//                        PENDING,
//                        "Broker should take oldest job when there is one in-progress job to each user"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), UUID.randomUUID().toString(), IN_PROGRESS, null, oldestDate),
//                        createNewJob(22, UUID.randomUUID(), UUID.randomUUID().toString(), IN_PROGRESS, null, oldestDate),
//                        createNewJob(33, UUID.randomUUID(), UUID.randomUUID().toString(), PENDING, null, LocalDateTime.now().minusHours(2))),
//                        2,
//                        -1,
//                        PENDING,
//                        "Broker should not pull any job when it exceeded mso limit with count(in-progress or pending && taken) statuses"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
//                        createNewJob(22, UUID.randomUUID(), "userId", IN_PROGRESS, null, oldestDate),
//                        createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusHours(2)),
//                        createNewJob(44, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusHours(5))),
//                        20,
//                        1,
//                        IN_PROGRESS,
//                        "Broker with in progress topic should pull the first in progress and not taken job by oldest date"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userId", COMPLETED, null, oldestDate),
//                        createNewJob(12, UUID.randomUUID(), "userId", FAILED, null, oldestDate),
//                        createNewJob(13, UUID.randomUUID(), "userId", PENDING,null, oldestDate),
//                        createNewJob(14, UUID.randomUUID(), "userId", STOPPED,null, oldestDate),
//                        createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS,null, oldestDate, DELETED),createNewJob(22, UUID.randomUUID(), "userId", IN_PROGRESS,null, oldestDate),
//                        createNewJob(33, UUID.randomUUID(), "userId", IN_PROGRESS,null, LocalDateTime.now().minusHours(2))),
//                  20,
//                  5,
//                  IN_PROGRESS,
//                  "Broker with in progress topic should pull only in-progress jobs - first in-progress job by oldest date - ignore deleted,completed, failed, pending and stopped statuses"
//                },
//                {ImmutableList.of(
//                        createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now()),
//                        createNewJob(22, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusSeconds(1)),
//                        createNewJob(33, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusSeconds(2))),
//                        20,
//                        -1,
//                        IN_PROGRESS,
//                        "Broker with in progress topic should not pull any job if its modified date is smaller than now-interval (20 seconds)"
//                }
//
//        };
//    }
//
//
//    @Test(dataProvider = "jobs")
//    public void givenSomeJobs_pullNextJob_returnNextOrNothingAsExpected(List<JobDaoImpl> jobs, int msoLimit, int expectedIndexSelected, Job.JobStatus topic, String assertionReason) {
//        JobsBrokerServiceInDatabaseImpl broker = new JobsBrokerServiceInDatabaseImpl(dataAccessService, sessionFactory, msoLimit, 20);
//        for (JobDaoImpl job : jobs) {
//            Date modifiedDate = job.getModified();
//            broker.add(job);
//            setModifiedDateToJob(job.getUuid(), modifiedDate);
//        }
//        Optional<Job> nextJob = broker.pull(topic, UUID.randomUUID().toString());
//        boolean shouldAnyBeSelected = expectedIndexSelected >= 0;
//        Assert.assertEquals(nextJob.isPresent(), shouldAnyBeSelected, assertionReason);
//        if (shouldAnyBeSelected) {
//            Assert.assertEquals(jobs.get(expectedIndexSelected), nextJob.get(), assertionReason);
//        }
//    }
//
//    @DataProvider
//    public Object[][] topics() {
//        return Arrays.stream(Job.JobStatus.values())
//                .filter(not(t -> ImmutableList.of(PENDING, IN_PROGRESS).contains(t)))
//                .map(v -> new Object[]{v}).collect(Collectors.toList()).toArray(new Object[][]{});
//    }
//
//    @Test(dataProvider = "topics", expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp = "Unsupported topic.*")
//    public void pullUnexpectedTopic_exceptionIsThrown(Job.JobStatus topic) {
//        broker.pull(topic, UUID.randomUUID().toString());
//    }
//
//    @Test(expectedExceptions = NoJobException.class)
//    public void givenNonPendingJobs_getJobAsPendingTopic_verifyNothingRetrieved() {
//        Stream.of(Job.JobStatus.values())
//                .filter(not(s -> s.equals(PENDING)))
//                .map(s -> createMockJob("some user id", s))
//                .map(job -> newJobAsync(broker, job))
//                .map(this::waitForFutureJob)
//                .collect(Collectors.toList());
//
//        waitForFutureOptionalJob(pullJobAsync(broker));
//    }
//
//    @Test
//    public void givenPendingAndNonPendingJobs_getJobAsPendingTopic_verifyAJobRetrieved() {
//        newJobAsync(broker); // this negated the expected result of the call below
//        givenNonPendingJobs_getJobAsPendingTopic_verifyNothingRetrieved();
//    }
//
//    @Test(expectedExceptions = NoJobException.class)
//    public void givenManyJobs_pullThemAllAndAskOneMore_verifyFinallyNothingRetrieved() {
//        putAndGetALotOfJobs(broker);
//        waitForFutureOptionalJob(pullJobAsync(broker));
//    }
//
//    @Test(expectedExceptions = NoJobException.class)
//    public void givenNoJob_requestJob_verifyNothingRetrieved() throws InterruptedException, ExecutionException, TimeoutException {
//        final Future<Optional<Job>> futureOptionalJob = pullJobAsync(broker);
//        assertThat("job should not be waiting yet", futureOptionalJob.get(FEW, MILLISECONDS).isPresent(), is(false));
//        waitForFutureOptionalJob(futureOptionalJob);
//    }
//
//    @Test(expectedExceptions = IllegalStateException.class)
//    public void givenSinglePulledJob_pushBackDifferentJob_verifyPushingRejected() {
//        waitForFutureJob(newJobAsync(broker));
//        waitForFutureJob(newJobAsync(broker));
//        waitForFutureOptionalJob(pullJobAsync(broker));
//
//        Job myJob = createMockJob("user id");
//        myJob.setUuid(UUID.randomUUID());
//
//        broker.pushBack(myJob); //Should fail
//    }
//
//    @Test
//    public void givenSingleJob_pushBackModifiedJob_verifyPulledIsVeryVeryTheSame() {
//        final ImmutableMap<String, Object> randomDataForMostRecentJobType =
//                ImmutableMap.of("42", 42, "complex", ImmutableList.of("a", "b", "c"));
//
//        waitForFutureJob(newJobAsync(broker));
//        final Job job = waitForFutureOptionalJob(pullJobAsync(broker));
//
//        job.setStatus(Job.JobStatus.PENDING);
//        job.setTypeAndData(JobType.NoOp, ImmutableMap.of("good", "morning"));
//        job.setTypeAndData(JobType.HttpCall, ImmutableMap.of());
//        job.setTypeAndData(JobType.ServiceInstantiation, randomDataForMostRecentJobType);
//
//        broker.pushBack(job);
//        final Job retrievedJob = waitForFutureOptionalJob(pullJobAsync(broker));
//
//        assertThat(JOBS_SHOULD_MATCH, retrievedJob, is(job));
//        assertThat(JOBS_SHOULD_MATCH, retrievedJob.getData(), both(equalTo(job.getData())).and(equalTo(randomDataForMostRecentJobType)));
//        assertThat(JOBS_SHOULD_MATCH, jobDataReflected(retrievedJob), is(jobDataReflected(job)));
//    }
//
//    private static String jobDataReflected(Job job) {
//        return new ReflectionToStringBuilder(job, ToStringStyle.SHORT_PREFIX_STYLE)
//                .setExcludeFieldNames("created", "modified", "takenBy")
//                .toString();
//    }
//
//    @Test(expectedExceptions = IllegalStateException.class)
//    public void givenSingleJob_pushBackTwice_verifyPushingRejected() {
//        waitForFutureJob(newJobAsync(broker));
//        final Job job = waitForFutureOptionalJob(pullJobAsync(broker));
//
//        broker.pushBack(job);
//        broker.pushBack(job); //Should fail
//    }
//
//    @Test
//    public void addJob_PeekItById_verifySameJobWasPeeked() {
//        String userId = UUID.randomUUID().toString();
//        Job myJob = createMockJob(userId);
//        UUID uuid = broker.add(myJob);
//        Job peekedJob = broker.peek(uuid);
//        assertEquals("added testId is not the same as peeked TestsId",
//                userId,
//                peekedJob.getData().get("userId"));
//    }
//
//    @Test(dataProvider = "jobStatusesForSuccessDelete", expectedExceptions = NoJobException.class)
//       public void givenOneJob_deleteIt_canPeekOnItButCantPull(Job.JobStatus status) {
//        final Job job = waitForFutureJob(newJobAsync(broker, status));
//        broker.delete(job.getUuid());
//        assertNotNull(((JobDaoImpl) broker.peek(job.getUuid())).getDeletedAt(), "job should be deleted");
//        waitForFutureOptionalJob(pullJobAsync(broker));
//    }
//
//    @DataProvider
//    public static Object[][] jobStatusesForSuccessDelete() {
//        return new Object[][]{
//                {PENDING},
//                {STOPPED}
//        };
//    }
//
//    @Test(
//            dataProvider = "jobStatusesForFailedDelete",
//            expectedExceptions = OperationNotAllowedException.class,
//            expectedExceptionsMessageRegExp=DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE
//    )
//    public void deleteJob_notAllowedStatus_exceptionIsThrown(Job.JobStatus status, boolean taken) {
//        final Job job = waitForFutureJob(newJobAsync(broker, createMockJob("some user id", status)));
//
//        if (taken) {
//            waitForFutureOptionalJob(pullJobAsync(broker));
//        }
//
//
//        broker.delete(job.getUuid());
//    }
//
//    @DataProvider
//    public static Object[][] jobStatusesForFailedDelete() {
//        return new Object[][]{
//                {PENDING, true},
//                {IN_PROGRESS, false},
//                {COMPLETED, false},
//                {PAUSE, false},
//                {FAILED, false},
//        };
//    }
//
//    @Test(expectedExceptions = OperationNotAllowedException.class, expectedExceptionsMessageRegExp = DELETE_SERVICE_NOT_EXIST_EXCEPTION_MESSAGE)
//    public void deleteJob_notExist_exceptionIsThrown() {
//        waitForFutureJob(newJobAsync(broker, createMockJob("some user id", PENDING)));
//        broker.delete(new UUID(111, 111));
//    }
//
//}
