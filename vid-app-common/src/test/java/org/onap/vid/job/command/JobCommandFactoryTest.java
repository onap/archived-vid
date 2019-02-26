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

package org.onap.vid.job.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.JobType;
import org.onap.vid.job.impl.JobSharedData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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

    public static class MockedRequest implements JobAdapter.AsyncJobRequest {

        final public int x;
        final public String y;

        @JsonCreator
        public MockedRequest(@JsonProperty("x")int x, @JsonProperty("y")String y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MockedRequest)) return false;
            MockedRequest that = (MockedRequest) o;
            return x == that.x &&
                    Objects.equals(y, that.y);
        }

        @Override
        public int hashCode() {

            return Objects.hash(x, y);
        }
    }

    @Test(dataProvider = "jobTypes")
    public void givenJob_createCommandCallsTheInitAndReturnsTheInstance(JobType jobType) {

        final UUID uuid = UUID.randomUUID();
        final Map<String, Object> data = ImmutableMap.of("foo", "bar");
        final JobSharedData sharedData = new JobSharedData(uuid, "userid", new MockedRequest(1,"a"));

        when(job.getType()).thenReturn(jobType);
        when(job.getUuid()).thenReturn(uuid);
        when(job.getData()).thenReturn(data);
        when(job.getSharedData()).thenReturn(sharedData);

        final JobCommand command = jobCommandFactory.toCommand(job);

        verify(mockCommand).init(sharedData, data);

        assertThat(command, equalTo(mockCommand));
    }

}
