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


import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.onap.vid.job.Job.JobStatus.COMPLETED;
import static org.onap.vid.job.Job.JobStatus.COMPLETED_WITH_ERRORS;
import static org.onap.vid.job.Job.JobStatus.COMPLETED_WITH_NO_ACTION;
import static org.onap.vid.job.Job.JobStatus.CREATING;
import static org.onap.vid.job.Job.JobStatus.FAILED;
import static org.onap.vid.job.Job.JobStatus.IN_PROGRESS;
import static org.onap.vid.job.Job.JobStatus.PAUSE;
import static org.onap.vid.job.Job.JobStatus.PENDING;
import static org.onap.vid.job.Job.JobStatus.PENDING_RESOURCE;
import static org.onap.vid.job.Job.JobStatus.RESOURCE_IN_PROGRESS;
import static org.onap.vid.job.Job.JobStatus.STOPPED;
import static org.onap.vid.testUtils.TestUtils.generateRandomAlphaNumeric;
import static org.onap.vid.utils.Streams.not;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.domain.support.DomainVo;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.JobAdapterConfig;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobAdapter.AsyncJobRequest;
import org.onap.vid.job.JobType;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.JobCommandFactoryTest;
import org.onap.vid.job.impl.JobDaoImpl;
import org.onap.vid.job.impl.JobSchedulerInitializer;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.job.impl.JobsBrokerServiceInDatabaseImpl;
import org.onap.vid.utils.DaoUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, JobAdapterConfig.class})
public class JobsBrokerServiceTest extends AbstractTestNGSpringContextTests {

    private static final Logger logger = LogManager.getLogger(JobsBrokerServiceTest.class);

    private static final int JOBS_COUNT = 127;
    private static final boolean DELETED = true;
    private final ExecutorService executor = Executors.newFixedThreadPool(90);

    private final Set<Long> threadsIds = new ConcurrentSkipListSet<>();

    private final long FEW = 1000;

    private final String JOBS_SHOULD_MATCH = "the jobs that added and those that pulled must be the same";
    private final String JOBS_PEEKED_SHOULD_MATCH = "the jobs that added and those that peeked must be the same";
    private static final String DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE = "Service status does not allow deletion from the queue";
    private static final String DELETE_SERVICE_NOT_EXIST_EXCEPTION_MESSAGE = "Service does not exist";
    private JobsBrokerService broker;

    @Inject
    JobAdapter jobAdapter;
    @Inject
    private DataAccessService dataAccessService;
    @Inject
    private SessionFactory sessionFactory;

    @Mock
    private VersionService versionService;

    @AfterMethod
    public void threadsCounter() {
        logger.info("participating threads count: " + threadsIds.size());
        threadsIds.clear();
    }

    @BeforeMethod
    public void initializeBroker() {
        MockitoAnnotations.initMocks(this);
        when(versionService.retrieveBuildNumber()).thenReturn("aBuildNumber");
        broker = new JobsBrokerServiceInDatabaseImpl(dataAccessService, sessionFactory, 200, 0, versionService);
        ((JobsBrokerServiceInDatabaseImpl) broker).deleteAll();
    }

    /*
    - pulling jobs is limited to inserted ones
    - putting back allows getting the job again
    - multi threads safety
    - any added job should be visible with view

    - edges:
        - pulling with empty repo should return empty optional
        - pulling more than expected should return empty optional
        - putting one, over-pulling from a different thread
        - take before inserting, then insert while waiting

     */

    private class NoJobException extends RuntimeException {
    }

    private Future<Job> newJobAsync(JobsBrokerService b) {
        return newJobAsync(b, createMockJob("user id"));
    }

    private Future<Job> newJobAsync(JobsBrokerService b, Job.JobStatus status) {
        return newJobAsync(b, createMockJob("user id", status));
    }

    private Job createMockJob(String userId) {
        return jobAdapter.createServiceInstantiationJob(
                JobType.NoOp,
                new JobCommandFactoryTest.MockedRequest(42,"nothing") ,
                UUID.randomUUID(),
                userId,
                null,
                "optimisticUniqueServiceInstanceName",
                RandomUtils.nextInt());
    }

    private Job createMockJob(String userId, Job.JobStatus jobStatus) {
        Job job = createMockJob(userId);
        job.setStatus(jobStatus);
        return job;
    }

    private Future<Job> newJobAsync(JobsBrokerService b, Job job) {
        final Future<Job> jobFuture = executor.submit(() -> {
            accountThreadId();

            b.add(job);

            return job;
        });
        return jobFuture;
    }

    private void pushBackJobAsync(JobsBrokerService b, Job job) {
        executor.submit(() -> {
            accountThreadId();
            b.pushBack(job);
            return job;
        });
    }

    private Future<Optional<Job>> pullJobAsync(JobsBrokerService broker) {
        final Future<Optional<Job>> job = executor.submit(() -> {
            accountThreadId();
            // Pull only pending jobs, as H2 database does not support our SQL for in-progress jobs
            return broker.pull(Job.JobStatus.PENDING, UUID.randomUUID().toString());
        });
        return job;
    }

    private Job waitForFutureOptionalJob(Future<Optional<Job>> retrievedOptionalJobFuture) {
        try {
            return retrievedOptionalJobFuture.get(FEW, MILLISECONDS).orElseThrow(NoJobException::new);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Job waitForFutureJob(Future<Job> retrievedJobFuture) {
        try {
            return retrievedJobFuture.get(FEW, MILLISECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Job> putAndGetALotOfJobs(JobsBrokerService broker) {
        final List<Job> originalJobs = putALotOfJobs(broker);
        final List<Job> retrievedJobs = getAlotOfJobs(broker);

        assertThat(JOBS_SHOULD_MATCH, retrievedJobs, containsInAnyOrder(originalJobs.toArray()));

        return retrievedJobs;
    }

    private List<Job> putALotOfJobs(JobsBrokerService broker) {
        int n = JOBS_COUNT;
        return IntStream.range(0, n)
                .mapToObj(i -> newJobAsync(broker))
                .map(this::waitForFutureJob)
                .collect(toList());
    }

    private List<Job> getAlotOfJobs(JobsBrokerService broker) {
        int n = JOBS_COUNT;
        return IntStream.range(0, n)
                .mapToObj(i -> pullJobAsync(broker))
                .map(this::waitForFutureOptionalJob)
                .collect(toList());
    }

    private void pushBackJobs(List<Job> jobs, JobsBrokerService broker) {
        jobs.forEach(job -> pushBackJobAsync(broker, job));
    }

    private void accountThreadId() {
        threadsIds.add(Thread.currentThread().getId());
    }

    @Test
    public void givenSingleJob_getIt_verifySameJob() {
        final Job originalJob = waitForFutureJob(newJobAsync(broker));

        final Job retrievedJob = waitForFutureOptionalJob(pullJobAsync(broker));
        assertThat(JOBS_SHOULD_MATCH, retrievedJob, is(originalJob));
    }

    @DataProvider
    public static Object[][] allTopics() {
        return JobSchedulerInitializer.WORKERS_TOPICS.stream()
                .map(topic -> new Object[] { topic })
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "allTopics")
    public void givenJobFromSameBuild_pullJobs_jobIsPulled(Job.JobStatus topic) {
        when(versionService.retrieveBuildNumber()).thenReturn("someVersion");
        Job mockedJob = createMockJob("user id", topic);
        UUID uuid = broker.add(mockedJob);
        assertEquals(uuid,  broker.pull(topic, UUID.randomUUID().toString()).get().getUuid());
    }


    @Test(dataProvider = "allTopics")
    public void givenJobFromOtherBuild_pullJobs_noneIsPulled(Job.JobStatus topic) {
        when(versionService.retrieveBuildNumber()).thenReturn("old");
        Job mockedJob = createMockJob("user id", topic);
        broker.add(mockedJob);
        when(versionService.retrieveBuildNumber()).thenReturn("new");
        assertFalse(broker.pull(topic, UUID.randomUUID().toString()).isPresent());
    }

    @Test
    public void givenJobWithNullBuildAndJobWithRealBuild_pullJobs_jobsWithNonNullIsPulled() {
        Job.JobStatus topic = PENDING;

        //push job with null build
        when(versionService.retrieveBuildNumber()).thenReturn(null);
        broker.add(createMockJob("user id", topic));

        //push job with "aBuild" build
        when(versionService.retrieveBuildNumber()).thenReturn("aBuild");
        UUID newJobId = broker.add(createMockJob("user id", topic));

        //pull jobs while current build is still "aBuild". Only the non null build is pulled
        assertEquals(newJobId,  broker.pull(topic, UUID.randomUUID().toString()).get().getUuid());

        //no more jobs to pull
        assertFalse(broker.pull(topic, UUID.randomUUID().toString()).isPresent());
    }


    @Test
    public void givenManyJobs_getJobsAndPushThemBack_alwaysSeeAllOfThemWithPeek() throws InterruptedException {
        final List<Job> originalJobs = putALotOfJobs(broker);

        MILLISECONDS.sleep(FEW);
        assertThat(JOBS_PEEKED_SHOULD_MATCH, broker.peek(), containsInAnyOrder(originalJobs.toArray()));

        final Job retrievedJob = waitForFutureOptionalJob(pullJobAsync(broker));

        MILLISECONDS.sleep(FEW);
        assertThat(JOBS_PEEKED_SHOULD_MATCH, broker.peek(), containsInAnyOrder(originalJobs.toArray()));

        pushBackJobAsync(broker, retrievedJob);

        MILLISECONDS.sleep(FEW);
        assertThat(JOBS_PEEKED_SHOULD_MATCH, broker.peek(), containsInAnyOrder(originalJobs.toArray()));
    }

    @Test
    public void givenManyJobs_getThemAll_verifySameJobs() {
        putAndGetALotOfJobs(broker);
    }

    @Test
    public void givenManyJobs_getThemAllThenPushBackandGet_verifySameJobs() {
        final List<Job> retrievedJobs1 = putAndGetALotOfJobs(broker);

        pushBackJobs(retrievedJobs1, broker);
        final List<Job> retrievedJobs2 = getAlotOfJobs(broker);

        assertThat(JOBS_SHOULD_MATCH, retrievedJobs2, containsInAnyOrder(retrievedJobs1.toArray()));
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void setModifiedDateToJob(UUID jobUuid, Date date) {
        DomainVo job = dataAccessService.getDomainObject(JobDaoImpl.class, jobUuid, DaoUtils.getPropsMap());
        job.setModified(date);
        DaoUtils.tryWithSessionAndTransaction(sessionFactory, session -> {
            session.saveOrUpdate(job);
            return 1;
        });
    }


    public static JobDaoImpl createNewJob(Integer indexInBulk, UUID templateId, String userId, Job.JobStatus status, String takenBy, LocalDateTime date) {
        return createNewJob(indexInBulk, templateId, userId, status, takenBy, date, false);
    }

    public static JobDaoImpl createNewJob(Integer indexInBulk, UUID templateId, String userId, Job.JobStatus status, String takenBy, LocalDateTime date, boolean deleted){
        JobDaoImpl job = new JobDaoImpl();
        job.setUuid(UUID.randomUUID());
        job.setTypeAndData(JobType.NoOp, ImmutableMap.of("x", RandomStringUtils.randomAlphanumeric(15)));
        job.setIndexInBulk(indexInBulk);
        job.setTemplateId(templateId);
        job.setType(JobType.NoOp);
        job.setStatus(status);
        job.setTakenBy(takenBy);
        job.setCreated(toDate(date));
        job.setModified(toDate(date));
        job.setUserId(userId);
        if (deleted) {
            job.setDeletedAt(new Date());
        }
        return job;
    }

    @DataProvider
    public static Object[][] jobs(Method test) {
        LocalDateTime oldestDate = LocalDateTime.now().minusHours(30);
        UUID sameTemplate = UUID.randomUUID();
        return new Object[][]{
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", PENDING, null, oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", PENDING, null, oldestDate),
                        () -> createNewJob(11, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(2)),
                        () -> createNewJob(44, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(5))),
                        4,
                        0,
                        PENDING,
                        "Broker should pull the first pending job by oldest date then by job index"
                },
                { ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", COMPLETED,null, oldestDate),
                        () -> createNewJob(11, UUID.randomUUID(), "userId", PENDING,null, oldestDate, DELETED),
                        () -> createNewJob(12, UUID.randomUUID(), "userId", FAILED,null, oldestDate),
                        () -> createNewJob(13, UUID.randomUUID(), "userId", IN_PROGRESS,null, oldestDate),
                        () -> createNewJob(14, UUID.randomUUID(), "userId", STOPPED,null, oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", PENDING,null, oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", PENDING,null, LocalDateTime.now().minusHours(2))),
                        6,
                        5,
                        PENDING,
                        "Broker should pull the only pending - first pending job by oldest job - ignore deleted,completed, failed, in-progress and stopped statuses"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
                        2,
                        -1,
                        PENDING,
                        "Broker should not pull any job when it exceeded mso limit with count (in-progress) statuses"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", PENDING, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
                        2,
                        -1,
                        PENDING,
                        "Broker should not pull any job when it exceeded mso limit with count(in-progress or pending && taken) statuses"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", PENDING, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now().minusHours(2)),
                        () -> createNewJob(12, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, UUID.randomUUID().toString(), oldestDate)
                ),
                        3,
                        2,
                        PENDING,
                        "Broker should pull first job when it doesn't exceeded mso limit with count(in-progress or pending && taken) statuses"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, sameTemplate, "userId", PENDING, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(22, sameTemplate, "userId", PENDING, null, oldestDate),
                        () -> createNewJob(33, sameTemplate, "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
                        3,
                        -1,
                        PENDING,
                        "Broker should not pull any job when there is another job from this template that was taken"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, sameTemplate, "userId", IN_PROGRESS, null, oldestDate),
                        () -> createNewJob(22, sameTemplate, "userId", PENDING, null, oldestDate),
                        () -> createNewJob(33, sameTemplate, "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
                        3,
                        -1,
                        PENDING,
                        "Broker should not pull any job when there is another job from this template that in progress"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, sameTemplate, "userId", FAILED, null, oldestDate),
                        () -> createNewJob(22, sameTemplate, "userId", STOPPED, null, oldestDate),
                        () -> createNewJob(33, sameTemplate, "userId", PENDING, null, LocalDateTime.now().minusHours(2))),
                        3,
                        -1,
                        PENDING,
                        "Broker should not pull any job when there is another job from this template that was failed"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, sameTemplate, "userId", FAILED, null, oldestDate, DELETED),
                        () -> createNewJob(22, sameTemplate, "userId", STOPPED,null, oldestDate),
                        () -> createNewJob(33, sameTemplate, "userId", PENDING,null, LocalDateTime.now().minusHours(2))),
                        3,
                        2,
                        PENDING,
                        "Broker should pull pending job when there is another job from this template that was deleted, although failed"
                },
                { ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userA", IN_PROGRESS, null, oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userA", PENDING, null, oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), "userB", PENDING, null, LocalDateTime.now().minusHours(2))),
                        3,
                        2,
                        PENDING,
                        "Broker should prioritize jobs of user that has no in-progress jobs"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userA", PENDING, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userA", PENDING, null, oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), "userB", PENDING, null, LocalDateTime.now().minusHours(2))),
                        3,
                        2,
                        PENDING,
                        "Broker should prioritize jobs of user that has no taken jobs"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userA", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userA", PENDING, null, LocalDateTime.now().minusHours(2)),
                        () -> createNewJob(31, UUID.randomUUID(), "userB", IN_PROGRESS, null, LocalDateTime.now().minusHours(2)),
                        () -> createNewJob(32, UUID.randomUUID(), "userB", IN_PROGRESS, null, LocalDateTime.now().minusHours(2)),
                        () -> createNewJob(33, UUID.randomUUID(), "userB", PENDING, null, oldestDate)),
                        5,
                        4,
                        PENDING,
                        "Broker should take oldest job when there is one in-progress job to each user"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), UUID.randomUUID().toString(), IN_PROGRESS, null, oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), UUID.randomUUID().toString(), IN_PROGRESS, null, oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), UUID.randomUUID().toString(), PENDING, null, LocalDateTime.now().minusHours(2))),
                        2,
                        -1,
                        PENDING,
                        "Broker should not pull any job when it exceeded mso limit with count(in-progress or pending && taken) statuses"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, UUID.randomUUID().toString(), oldestDate),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", IN_PROGRESS, null, oldestDate),
                        () -> createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusHours(2)),
                        () -> createNewJob(44, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusHours(5))),
                        20,
                        1,
                        IN_PROGRESS,
                        "Broker with in progress topic should pull the first in progress and not taken job by oldest date"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", COMPLETED, null, oldestDate),
                        () -> createNewJob(12, UUID.randomUUID(), "userId", FAILED, null, oldestDate),
                        () -> createNewJob(13, UUID.randomUUID(), "userId", PENDING, null, oldestDate),
                        () -> createNewJob(14, UUID.randomUUID(), "userId", STOPPED, null, oldestDate),
                        () -> createNewJob(15, UUID.randomUUID(), "userId", CREATING, null, oldestDate),
                        () -> createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, null, oldestDate, DELETED),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", IN_PROGRESS, null, oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusHours(2)),
                        () -> createNewJob(16, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, oldestDate)
                ),
                        20,
                        6,
                        IN_PROGRESS,
                        "Broker with in progress topic should pull only in-progress jobs - first in-progress job by oldest date - ignore all other statuses"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", COMPLETED, null, oldestDate),
                        () -> createNewJob(12, UUID.randomUUID(), "userId", FAILED, null, oldestDate),
                        () -> createNewJob(13, UUID.randomUUID(), "userId", PENDING, null, oldestDate),
                        () -> createNewJob(14, UUID.randomUUID(), "userId", STOPPED, null, oldestDate),
                        () -> createNewJob(15, UUID.randomUUID(), "userId", CREATING, null, oldestDate),
                        () -> createNewJob(11, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, oldestDate, DELETED),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, oldestDate),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now().minusHours(2)),
                        () -> createNewJob(16, UUID.randomUUID(), "userId", IN_PROGRESS, null, oldestDate)
                ),
                        20,
                        6,
                        RESOURCE_IN_PROGRESS,
                        "Broker with RESOURCE_IN_PROGRESS topic should pull only RESOURCE_IN_PROGRESS jobs - first RESOURCE_IN_PROGRESS job by oldest date - ignore all other statuses"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now()),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusSeconds(1)),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusSeconds(2))),
                        20,
                        -1,
                        IN_PROGRESS,
                        "Broker with in progress topic should not pull any job if its modified date is smaller than now-interval (20 seconds)"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now()),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now().minusSeconds(1)),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now().minusSeconds(2))),
                        20,
                        -1,
                        RESOURCE_IN_PROGRESS,
                        "Broker with RESOURCE_IN_PROGRESS topic should not pull any job if its modified date is smaller than now-interval (20 seconds)"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(11, UUID.randomUUID(), "userId", PENDING, null, LocalDateTime.now()),
                        () -> createNewJob(22, UUID.randomUUID(), "userId", CREATING, null, LocalDateTime.now().minusSeconds(1)),
                        () -> createNewJob(33, UUID.randomUUID(), "userId", CREATING, null, LocalDateTime.now().minusHours(2))),
                        1,
                        2,
                        CREATING,
                        "Broker with creating topic should pull oldest creating job and ignore mso limit"
                },
                {ImmutableList.of(
                        (Jobber)() -> createNewJob(33, UUID.randomUUID(), "userId", CREATING, null, LocalDateTime.now())),
                        1,
                        0,
                        CREATING,
                        "Broker with CREATING topic should pull CREATING job that was just modified"
                }

        };
    }

    public interface Jobber {
        // Will defer LocalDateTime.now() to test's "real-time"
        JobDaoImpl toJob();
    }

    @Test(dataProvider = "jobs")
    public void givenSomeJobs_pullNextJob_returnNextOrNothingAsExpected(List<Jobber> jobbers, int msoLimit, int expectedIndexSelected, Job.JobStatus topic, String assertionReason) {
        JobsBrokerServiceInDatabaseImpl aBroker = new JobsBrokerServiceInDatabaseImpl(dataAccessService, sessionFactory, msoLimit, 20, versionService);
        final List<JobDaoImpl> jobs = addJobsWithModifiedDate(jobbers, aBroker);
        Optional<Job> nextJob = aBroker.pull(topic, UUID.randomUUID().toString());
        boolean shouldAnyBeSelected = expectedIndexSelected >= 0;
        String pulledJobDesc = nextJob.map(job -> ". pulled job: " + job.toString()).orElse(". no job pulled");
        Assert.assertEquals(nextJob.isPresent(), shouldAnyBeSelected, assertionReason+pulledJobDesc);
        if (shouldAnyBeSelected) {
            Assert.assertEquals(jobs.get(expectedIndexSelected), nextJob.get(), assertionReason);
        }
    }

    @NotNull
    protected List<JobDaoImpl> addJobsWithModifiedDate(List<Jobber> jobbers, JobsBrokerService broker) {
        final List<JobDaoImpl> jobs = jobbers.stream().map(Jobber::toJob).collect(toList());
        return addJobsWithModifiedDateByJobDao(jobs, broker);
    }

    @NotNull
    private List<JobDaoImpl> addJobsWithModifiedDateByJobDao(List<JobDaoImpl> jobs, JobsBrokerService broker) {
        for (JobDaoImpl job : jobs) {
            Date modifiedDate = job.getModified();
            broker.add(job);
            setModifiedDateToJob(job.getUuid(), modifiedDate);
        }
        return jobs;
    }

    @DataProvider
    public static Object[][] jobsForTestingPendingResource(Method test) {
        UUID templateId1 = UUID.fromString("311a9196-bbc5-47a1-8b11-bf0f9db1c7ca");
        UUID templateId2 = UUID.fromString("4f1522f9-642e-49f7-af75-a2f344085bcc");
        return new Object[][]{
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(12, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(1, templateId2, "userId", CREATING, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(2, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(3, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(4, UUID.randomUUID(), "userId", COMPLETED, null, LocalDateTime.now().minusSeconds(1), false)
                ),
                        0,
                        "given there is only one in the queue in PENDING_RESOURCE and no other job with same templateId, then this job is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(2, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(3, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusSeconds(2), false),
                        () -> createNewJob(1, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now(), false)
                ),
                        2,
                        "given multiple jobs with same templateId in PENDING_RESOURCE, then job with lowest indexInBulk is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(1, templateId2, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(1, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now(), false)
                ),
                        1,
                        "given multiple jobs with same indexInBulk, then job with lowest templateId is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(1, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now(), false),
                        () -> createNewJob(2, templateId2, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusSeconds(1), false)
                ),
                        0,
                        "given multiple jobs with different indexInBulk and different templateId, then job with lowest indexInBulk is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(5, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(2), false),
                        () -> createNewJob(1, templateId1, "userId", PENDING_RESOURCE, "123", LocalDateTime.now(), false)
                ),
                        -1,
                        "given there is already taken job with same templateId, then no job is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(2, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(2), false),
                        () -> createNewJob(1, templateId1, "userId", PENDING_RESOURCE, "123", LocalDateTime.now(), false),
                        () -> createNewJob(9, templateId2, "userId", PENDING_RESOURCE, null, LocalDateTime.now(), false),
                        () -> createNewJob(8, templateId2, "userId", PENDING_RESOURCE, null, LocalDateTime.now(), false)
                ),
                        3,
                        "given 4 jobs, 2 jobs templateId1 but one of them is taken, and 2 jobs with templateId2, then select job with templateId2"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(5, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(1), false),
                        () -> createNewJob(1, templateId1, "userId", PENDING_RESOURCE, "123", LocalDateTime.now(), true)
                ),
                        0,
                        "given 2 jobs with same templateId, one of them is taken but deleted, then the other job is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(5, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(1), false),
                        () -> createNewJob(1, templateId1, "userId", IN_PROGRESS, null, LocalDateTime.now(), false)
                ),
                        -1,
                        "given 2 jobs with same templateId, one of them is IN_PROGRESS, then no job is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(5, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(1), false),
                        () -> createNewJob(1, templateId1, "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now(), false)
                ),
                        -1,
                        "given 2 jobs with same templateId, one of them is RESOURCE_IN_PROGRESS, then no job is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(6, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(2), false),
                        () -> createNewJob(5, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(1), false),
                        () -> createNewJob(1, templateId1, "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now(), true)
                ),
                        1,
                        "given 3 jobs with same templateId, one of them is RESOURCE_IN_PROGRESS but deleted, then other job with lowest indexInBulk is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(6, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(2), false),
                        () -> createNewJob(5, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(1), false),
                        () -> createNewJob(1, templateId1, "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now(), false),
                        () -> createNewJob(12, templateId2, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(2), false),
                        () -> createNewJob(11, templateId2, "userId", PENDING_RESOURCE, null, LocalDateTime.now(), false)
                ),
                        4,
                        "given 5 jobs, 3 with templateId1 that one of them is RESOURCE_IN_PROGRESS,"+
                                "2 with templateId2 both in PENDING_RESOURCE, then job with lowest indexInBulk from templateId2 is selected"

                },
                {ImmutableList.of( (Jobber)
                        () -> createNewJob(6, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(2), true)
                ),
                        -1,
                        "given 1 job in PENDING_RESOURCE but it's deleted, then no job is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(20, templateId1, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(1, templateId1, "userId", CREATING, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(2, templateId1, "userId", COMPLETED, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(3, templateId1, "userId", FAILED, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(4, templateId1, "userId", COMPLETED_WITH_ERRORS, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(5, templateId1, "userId", STOPPED, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(6, templateId1, "userId", PAUSE, null, LocalDateTime.now().minusSeconds(1), false)
                ),
                        0,
                        "given multiple jobs with same templateId, 1 in PENDING_RESOURCE, and other are not in progress, "+
                                "then the job in PENDING_RESOURCE is selected"
                },
                {ImmutableList.of( (Jobber)
                                () -> createNewJob(1, UUID.randomUUID(), "userId", CREATING, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(2, UUID.randomUUID(), "userId", COMPLETED, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(3, UUID.randomUUID(), "userId", FAILED, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(4, UUID.randomUUID(), "userId", COMPLETED_WITH_ERRORS, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(5, UUID.randomUUID(), "userId", IN_PROGRESS, null, LocalDateTime.now().minusSeconds(1), false),
                        () -> createNewJob(6, UUID.randomUUID(), "userId", RESOURCE_IN_PROGRESS, null, LocalDateTime.now().minusSeconds(1), false)
                ),
                        -1,
                        "given there is no job in PENDING_RESOURCE state, then no job is selected"
                },
                {ImmutableList.of( (Jobber)
                        () -> createNewJob(6, null, "userId", PENDING_RESOURCE, null, LocalDateTime.now().minusMinutes(2), false)
                ),
                        -1,
                        "given there is 1 job in PENDING_RESOURCE but without templateId, then no job is selected"
                },
        };
    }

    @Test(dataProvider = "jobsForTestingPendingResource")
    public void givenSomeJobs_pullPendingResource_returnNextOrNothingAsExpected(List<Jobber> jobbers, int expectedIndexSelected, String assertionReason) {
        givenSomeJobs_pullNextJob_returnNextOrNothingAsExpected(jobbers, 1, expectedIndexSelected, PENDING_RESOURCE, assertionReason);
    }

    public static JobDaoImpl createNewJob(Job.JobStatus status, String takenBy, long secondsOffset, boolean deleted) {
        return createNewJob(1, UUID.randomUUID(), "af456", status, takenBy, LocalDateTime.now().minusSeconds(secondsOffset), deleted);
    }

    @Test
    public void givenSomeJobs_deleteOldFinalJobs_onlyExpectedJobsAreDeleted() {
        long seconds = 999;
        final List<Pair<JobDaoImpl,Boolean>> jobs = ImmutableList.of(
                //not final
                Pair.of(createNewJob(IN_PROGRESS, null, seconds+1, false), true),
                Pair.of(createNewJob(RESOURCE_IN_PROGRESS, null, seconds+1, false), true),
                Pair.of(createNewJob(PENDING, null, seconds+1, false), true),
                Pair.of(createNewJob(CREATING, null, seconds+1, false), true),
                Pair.of(createNewJob(PENDING_RESOURCE, null, seconds+1, false), true),
                Pair.of(createNewJob(PAUSE, null, seconds+1, false), true),

                //final
                Pair.of(createNewJob(COMPLETED, null, seconds+1, false), false),
                Pair.of(createNewJob(FAILED, null, seconds+1, false), false),
                Pair.of(createNewJob(STOPPED, null, seconds+1, false), false),
                Pair.of(createNewJob(COMPLETED_WITH_ERRORS, null, seconds+1, true), false),
                Pair.of(createNewJob(COMPLETED_WITH_NO_ACTION, generateRandomAlphaNumeric(5), seconds+1, true), false),

                //final but not old
                Pair.of(createNewJob(COMPLETED, generateRandomAlphaNumeric(5), seconds-2, false), true),
                Pair.of(createNewJob(COMPLETED, generateRandomAlphaNumeric(5), seconds-400, false), true),
                Pair.of(createNewJob(COMPLETED, generateRandomAlphaNumeric(5), 0, false), true)
        );
        addJobsWithModifiedDateByJobDao(jobs.stream().map(Pair::getLeft).collect(Collectors.toList()), broker);
        assertEquals(jobs.size(), broker.peek().size());

        broker.deleteOldFinalJobs(seconds);
        Stream<Pair<UUID, Job.JobStatus>> expectedJobs = jobs.stream()
                .filter(Pair::getRight)
                .map(x -> Pair.of(
                        x.getLeft().getUuid(),
                        x.getLeft().getStatus()
                ));
        assertThat(broker.peek().stream().map(x->Pair.of(x.getUuid(), x.getStatus())).collect(Collectors.toList()),
                containsInAnyOrder(expectedJobs.toArray()));
    }

    @DataProvider
    public Object[][] topics() {
        return Arrays.stream(Job.JobStatus.values())
                .filter(not(t -> ImmutableList.of(PENDING, IN_PROGRESS, CREATING, RESOURCE_IN_PROGRESS, PENDING_RESOURCE).contains(t)))
                .map(v -> new Object[]{v}).collect(toList()).toArray(new Object[][]{});
    }

    @Test(dataProvider = "topics", expectedExceptions = GenericUncheckedException.class, expectedExceptionsMessageRegExp = "Unsupported topic.*")
    public void pullUnexpectedTopic_exceptionIsThrown(Job.JobStatus topic) {
        broker.pull(topic, UUID.randomUUID().toString());
    }

    @Test(expectedExceptions = NoJobException.class)
    public void givenNonPendingJobs_getJobAsPendingTopic_verifyNothingRetrieved() {
        Stream.of(Job.JobStatus.values())
                .filter(not(s -> s.equals(PENDING)))
                .map(s -> createMockJob("some user id", s))
                .map(job -> newJobAsync(broker, job))
                .map(this::waitForFutureJob)
                .collect(toList());

        waitForFutureOptionalJob(pullJobAsync(broker));
    }

    @Test
    public void givenPendingAndNonPendingJobs_getJobAsPendingTopic_verifyAJobRetrieved() {
        newJobAsync(broker); // this negated the expected result of the call below
        givenNonPendingJobs_getJobAsPendingTopic_verifyNothingRetrieved();
    }

    @Test(expectedExceptions = NoJobException.class)
    public void givenManyJobs_pullThemAllAndAskOneMore_verifyFinallyNothingRetrieved() {
        putAndGetALotOfJobs(broker);
        waitForFutureOptionalJob(pullJobAsync(broker));
    }

    @Test(expectedExceptions = NoJobException.class)
    public void givenNoJob_requestJob_verifyNothingRetrieved() throws InterruptedException, ExecutionException, TimeoutException {
        final Future<Optional<Job>> futureOptionalJob = pullJobAsync(broker);
        assertThat("job should not be waiting yet", futureOptionalJob.get(FEW, MILLISECONDS).isPresent(), is(false));
        waitForFutureOptionalJob(futureOptionalJob);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenSinglePulledJob_pushBackDifferentJob_verifyPushingRejected() {
        waitForFutureJob(newJobAsync(broker));
        waitForFutureJob(newJobAsync(broker));
        waitForFutureOptionalJob(pullJobAsync(broker));

        Job myJob = createMockJob("user id");
        myJob.setUuid(UUID.randomUUID());

        broker.pushBack(myJob); //Should fail
    }

    @Test
    public void givenSingleJob_pushBackModifiedJob_verifyPulledIsVeryVeryTheSame() {
        final ImmutableMap<String, Object> randomDataForMostRecentJobType =
                ImmutableMap.of("42", 42, "complex", ImmutableList.of("a", "b", "c"));

        waitForFutureJob(newJobAsync(broker));
        final Job job = waitForFutureOptionalJob(pullJobAsync(broker));

        job.setStatus(Job.JobStatus.PENDING);
        job.setTypeAndData(JobType.NoOp, ImmutableMap.of("good", "morning"));
        job.setTypeAndData(JobType.HttpCall, ImmutableMap.of());
        job.setTypeAndData(JobType.MacroServiceInstantiation, randomDataForMostRecentJobType);

        broker.pushBack(job);
        final Job retrievedJob = waitForFutureOptionalJob(pullJobAsync(broker));

        assertThat(JOBS_SHOULD_MATCH, retrievedJob, is(job));
        assertThat(JOBS_SHOULD_MATCH, retrievedJob.getData(), both(equalTo(job.getData())).and(equalTo(randomDataForMostRecentJobType)));
        assertThat(JOBS_SHOULD_MATCH, jobDataReflected(retrievedJob), is(jobDataReflected(job)));
    }

    private static String jobDataReflected(Job job) {
        return new ReflectionToStringBuilder(job, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("created", "modified", "takenBy")
                .toString();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenSingleJob_pushBackTwice_verifyPushingRejected() {
        waitForFutureJob(newJobAsync(broker));
        final Job job = waitForFutureOptionalJob(pullJobAsync(broker));

        broker.pushBack(job);
        broker.pushBack(job); //Should fail
    }

    @Test
    public void addJob_PeekItById_verifySameJobWasPeeked() {
        String userId = UUID.randomUUID().toString();
        Job myJob = createMockJob(userId);
        UUID uuid = broker.add(myJob);
        Job peekedJob = broker.peek(uuid);
        assertEquals("added testId is not the same as peeked TestsId",
                userId,
                peekedJob.getSharedData().getUserId());
    }

    @Test(dataProvider = "jobStatusesForSuccessDelete", expectedExceptions = NoJobException.class)
    public void givenOneJob_deleteIt_canPeekOnItButCantPull(Job.JobStatus status) {
        final Job job = waitForFutureJob(newJobAsync(broker, status));
        broker.delete(job.getUuid());
        assertNotNull(((JobDaoImpl) broker.peek(job.getUuid())).getDeletedAt(), "job should be deleted");
        waitForFutureOptionalJob(pullJobAsync(broker));
    }

    @DataProvider
    public static Object[][] jobStatusesForSuccessDelete() {
        return new Object[][]{
                {PENDING},
                {STOPPED}
        };
    }

    @Test(
            dataProvider = "jobStatusesForFailedDelete",
            expectedExceptions = OperationNotAllowedException.class,
            expectedExceptionsMessageRegExp=DELETE_SERVICE_INFO_STATUS_EXCEPTION_MESSAGE
    )
    public void deleteJob_notAllowedStatus_exceptionIsThrown(Job.JobStatus status, boolean taken) {
        final Job job = waitForFutureJob(newJobAsync(broker, createMockJob("some user id", status)));

        if (taken) {
            waitForFutureOptionalJob(pullJobAsync(broker));
        }


        broker.delete(job.getUuid());
    }

    @DataProvider
    public static Object[][] jobStatusesForFailedDelete() {
        return new Object[][]{
                {PENDING, true},
                {IN_PROGRESS, false},
                {COMPLETED, false},
                {PAUSE, false},
                {FAILED, false},
        };
    }

    @Test(expectedExceptions = OperationNotAllowedException.class, expectedExceptionsMessageRegExp = DELETE_SERVICE_NOT_EXIST_EXCEPTION_MESSAGE)
    public void deleteJob_notExist_exceptionIsThrown() {
        waitForFutureJob(newJobAsync(broker, createMockJob("some user id", PENDING)));
        broker.delete(new UUID(111, 111));
    }

    public static class MockAsyncRequest implements AsyncJobRequest {
        public String value;

        public MockAsyncRequest() {}

        public MockAsyncRequest(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Test
    public void twoJobsWithSamePosition_bothJobsArePulled(){
        UUID uuid = UUID.randomUUID();
        int positionInBulk = RandomUtils.nextInt();
        String userId = "userId";

        Optional<Job> firstPulledJob = createAddAndPullJob(uuid, positionInBulk, userId, "first value");
        Optional<Job> secondPulledJob = createAddAndPullJob(uuid, positionInBulk, userId, "second value");

        MockAsyncRequest firstValue = (MockAsyncRequest) firstPulledJob.get().getSharedData().getRequest();
        MockAsyncRequest secondValue = (MockAsyncRequest) secondPulledJob.get().getSharedData().getRequest();
        assertThat(ImmutableList.of(firstValue.value, secondValue.value),
            containsInAnyOrder("first value", "second value"));
    }

    private Optional<Job> createAddAndPullJob(UUID uuid, int positionInBulk, String userId, String s) {
        JobDaoImpl job1 = createNewJob(positionInBulk, uuid, userId, CREATING, null,
            LocalDateTime.now().minusSeconds(1), false);
        job1.setSharedData(new JobSharedData(null, userId, new MockAsyncRequest(s), "testApi"));
        broker.add(job1);
        return broker.pull(CREATING, userId);
    }
}
