/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.job.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.mockito.Mock;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.model.JobBulk;
import org.onap.vid.model.JobModel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobAdapterImplTest {


    private static final int SAMPLE_INDEX = 10;
    private static final String SAMPLE_USER_ID = "sampleUserId";

    @Mock
    private Job job;

    @Mock
    private JobAdapter.AsyncJobRequest asyncJobRequest;

    private UUID sampleUuid=UUID.randomUUID();

    private JobAdapterImpl jobAdapter = new JobAdapterImpl();

    @BeforeMethod
    public void setUp() {
        initMocks(this);

        when(job.getUuid()).thenReturn(sampleUuid);
        when(job.getStatus()).thenReturn(Job.JobStatus.IN_PROGRESS);
        when(job.getTemplateId()).thenReturn(sampleUuid);
    }

    @Test
    public void shouldConvertJobToJobModel() {


        JobModel convertedJob = jobAdapter.toModel(job);

        assertThat(convertedJob.getUuid()).isEqualByComparingTo(sampleUuid);
        assertThat(convertedJob.getStatus()).isEqualByComparingTo(Job.JobStatus.IN_PROGRESS);
        assertThat(convertedJob.getTemplateId()).isEqualByComparingTo(sampleUuid);
    }


    @Test
    public void shouldProperlyCreateJob() {
        UUID uuid = UUID.randomUUID();

        Job createdJob = jobAdapter.createJob(JobType.ServiceInstantiation, asyncJobRequest, uuid, SAMPLE_USER_ID, SAMPLE_INDEX);

        assertThat(createdJob.getStatus()).isEqualByComparingTo(Job.JobStatus.PENDING);
        assertThat(createdJob.getTemplateId()).isEqualByComparingTo(uuid);
        assertThat(createdJob.getType()).isEqualByComparingTo(JobType.ServiceInstantiation);
        assertThat(createdJob.getData()).isEqualTo(ImmutableMap.of("request", asyncJobRequest, "userId", SAMPLE_USER_ID));
    }

    @Test
    public void shouldProperlyCreateBulkOfJobs(){
        List<Job> bulkOfJobs = jobAdapter.createBulkOfJobs(ImmutableMap.of("count", 5, "type", JobType.InProgressStatus.name()));


        assertThat(bulkOfJobs).hasSize(5);

        Stream<Job> jobStream = bulkOfJobs.stream().filter(x -> JobType.InProgressStatus.equals(x.getType()) && Job.JobStatus.PENDING.equals(x.getStatus()));

        assertThat(jobStream).hasSize(5);
    }


    @Test
    public void shouldConvertListToBulkJob(){
        JobBulk jobBulk = jobAdapter.toModelBulk(ImmutableList.of(job, job));

        assertThat(jobBulk.getJobs()).hasSize(2);
    }
}
