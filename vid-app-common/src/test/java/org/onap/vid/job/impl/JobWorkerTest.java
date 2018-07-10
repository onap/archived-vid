package org.onap.vid.job.impl;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hamcrest.Matcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.*;
import org.onap.vid.job.command.HttpCallCommand;
import org.onap.vid.job.command.JobCommandFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobWorkerTest {

    @InjectMocks
    private JobWorker jobWorker = new JobWorker();

    @Mock
    private JobCommandFactory jobCommandFactory;

    private final JobCommand jobCommand = mock(JobCommand.class);
    private Job jobUnderTest;
    private JobAdapter.AsyncJobRequest originalData;
    private JobType originalType;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

        when(jobCommandFactory.toCommand(any())).thenReturn(jobCommand);

        originalData = new JobAdapter.AsyncJobRequest() {
            public final Map datum = ImmutableMap.of("some", "data");
            public final String foobar = "aux";
        };

        originalType = JobType.ServiceInstantiation;
        jobUnderTest = new JobAdapterImpl().createJob(
                originalType,
                originalData,
                UUID.randomUUID(),
                "my user id",
                RandomUtils.nextInt()
        );
    }

    @Test
    public void executeJobAndStepToNext_givenNull_onlyStatusModified() {

        assertNextJobAfterExecuteJob(null, new String[]{"status"}, allOf(
                hasProperty("status", is(Job.JobStatus.STOPPED)),
                hasProperty("data", hasEntry("request", originalData)),
                hasProperty("type", is(originalType)))
        );
    }

    @Test
    public void executeJobAndStepToNext_givenNextJob_jobDataIsModified() {

        final Job.JobStatus nextStatus = Job.JobStatus.IN_PROGRESS;

        final UUID jobUuid = UUID.randomUUID();
        final NextCommand nextCommand = new NextCommand(nextStatus, new HttpCallCommand("my strange url", jobUuid));

        String[] excludedFields = {"status", "data", "type"};

        assertNextJobAfterExecuteJob(nextCommand, excludedFields, allOf(
                hasProperty("status", is(nextStatus)),
                hasProperty("data", is(nextCommand.getCommand().getData())),
                hasProperty("type", is(nextCommand.getCommand().getType())))
        );
    }

    private void assertNextJobAfterExecuteJob(NextCommand nextCommand, String[] excludedFields, Matcher<Job> jobMatcher) {
        when(jobCommand.call()).thenReturn(nextCommand);

        String jobBefore = new ReflectionToStringBuilder(jobUnderTest, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames(excludedFields).toString();

        ////// FUNCTION UNDER TEST /////
        Job nextJob = jobWorker.executeJobAndGetNext(jobUnderTest);
        ////////////////////////////////

        String jobAfter = new ReflectionToStringBuilder(nextJob, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames(excludedFields).toString();

        assertThat(nextJob, jobMatcher);
        assertThat(jobAfter, equalTo(jobBefore));
    }
}