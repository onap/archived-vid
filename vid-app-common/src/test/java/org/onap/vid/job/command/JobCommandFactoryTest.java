package org.onap.vid.job.command;

import com.google.common.collect.ImmutableMap;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.JobType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JobCommandFactoryTest {

    private JobCommandFactory jobCommandFactory;

    @Mock
    private Job job;

    @Mock
    private JobCommand mockCommand;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void setUp() {
        jobCommandFactory = new JobCommandFactory(any -> mockCommand);
    }

    @DataProvider
    public Object[][] jobTypes() {
        return Arrays.stream(
                JobType.values()
        ).map(v -> new Object[]{v}).collect(Collectors.toList()).toArray(new Object[][]{});

    }

    @Test(dataProvider = "jobTypes")
    public void givenJob_createCommandCallsTheInitAndReturnsTheInstance(JobType jobType) {

        final UUID uuid = UUID.randomUUID();
        final Map<String, Object> data = ImmutableMap.of("foo", "bar");

        when(job.getType()).thenReturn(jobType);
        when(job.getUuid()).thenReturn(uuid);
        when(job.getData()).thenReturn(data);

        final JobCommand command = jobCommandFactory.toCommand(job);

        verify(mockCommand).init(uuid, data);

        assertThat(command, equalTo(mockCommand));
    }

}