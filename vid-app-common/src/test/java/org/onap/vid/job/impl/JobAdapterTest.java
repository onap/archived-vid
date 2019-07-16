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

package org.onap.vid.job.impl;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomUtils;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.job.command.JobCommandFactoryTest;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class JobAdapterTest {

    @Test
    public void testCreateServiceInstantiationJob() {
        FeatureManager featureManager = mock(FeatureManager.class);
        JobAdapter jobAdapter = new JobAdapterImpl(featureManager);

        JobType jobType = JobType.NoOp;
        JobAdapter.AsyncJobRequest request = new JobCommandFactoryTest.MockedRequest(42,"nothing");
        UUID templateId = UUID.randomUUID();
        String userId = "ou012t";
        String testApi = "VNF_API";
        String optimisticUniqueServiceInstanceName = "optimisticUniqueServiceInstanceName";
        int indexInBulk = RandomUtils.nextInt();
        Job job = jobAdapter.createServiceInstantiationJob(
                jobType,
                request,
                templateId,
                userId,
                testApi,
                optimisticUniqueServiceInstanceName,
                indexInBulk
                );

        assertEquals(job.getType(), jobType);
        assertEquals(job.getSharedData().getRequest(), request);
        assertEquals(job.getSharedData().getRequestType(), request.getClass());
        assertEquals(job.getSharedData().getUserId(), userId);
        assertEquals(job.getSharedData().getTestApi(), testApi);
        assertEquals(job.getSharedData().getJobUuid(), job.getUuid());
        assertEquals(job.getSharedData().getRootJobId(), job.getUuid());
        assertNotNull(job.getUuid());
        assertEquals(job.getTemplateId(), templateId);
        assertEquals(job.getData().get("optimisticUniqueServiceInstanceName"), optimisticUniqueServiceInstanceName);
        assertEquals((int)job.getIndexInBulk(), indexInBulk );
        assertEquals(job.getStatus(), Job.JobStatus.PENDING);
    }

    @Test(dataProvider = "trueAndFalse", dataProviderClass = TestUtils.class)
    public void testCreateChildJob(boolean isFlagExpCreateResourcesInParallel) {
        FeatureManager featureManager = mock(FeatureManager.class);
        when(featureManager.isActive(Features.FLAG_EXP_CREATE_RESOURCES_IN_PARALLEL)).thenReturn(isFlagExpCreateResourcesInParallel);
        Job.JobStatus expectedJobStatus = isFlagExpCreateResourcesInParallel ? Job.JobStatus.CREATING : Job.JobStatus.PENDING_RESOURCE;
        JobAdapter jobAdapter = new JobAdapterImpl(featureManager);

        UUID templateId = UUID.randomUUID();
        String userId = "ou012t";
        String testApi = "VNF_API";
        String optimisticUniqueServiceInstanceName = "optimisticUniqueServiceInstanceName";
        int indexInBulk = RandomUtils.nextInt();
        Job grandJob = jobAdapter.createServiceInstantiationJob(
                JobType.HttpCall,
                new JobCommandFactoryTest.MockedRequest(99, "anything"),
                templateId,
                userId,
                testApi,
                optimisticUniqueServiceInstanceName,
                indexInBulk
        );

        JobType jobType = JobType.NoOp;
        JobAdapter.AsyncJobRequest request = new JobCommandFactoryTest.MockedRequest(42,"nothing");
        Job parentJob = jobAdapter.createChildJob(jobType, request, grandJob.getSharedData(), ImmutableMap.of(), 1);

        assertEquals(parentJob.getType(), jobType);
        assertEquals(parentJob.getSharedData().getRequest(), request);
        assertEquals(parentJob.getSharedData().getRequestType(), request.getClass());
        assertEquals(parentJob.getSharedData().getUserId(), userId);
        assertEquals(parentJob.getSharedData().getTestApi(), testApi);
        assertEquals(parentJob.getSharedData().getJobUuid(), parentJob.getUuid());
        assertNotNull(parentJob.getUuid());
        assertNotEquals(parentJob.getUuid(), grandJob.getUuid());
        assertEquals(parentJob.getStatus(), expectedJobStatus);
        assertEquals(parentJob.getTemplateId(), grandJob.getUuid());
        assertEquals(parentJob.getIndexInBulk().intValue(), 1);
        assertEquals(parentJob.getSharedData().getRootJobId(), grandJob.getUuid());

        JobType jobType2 = JobType.AggregateState;
        JobAdapter.AsyncJobRequest request2 = new JobCommandFactoryTest.MockedRequest(66,"abc");
        Job job = jobAdapter.createChildJob(jobType2, request2, parentJob.getSharedData(), ImmutableMap.of(), 0);

        assertEquals(job.getType(), jobType2);
        assertEquals(job.getSharedData().getRequest(), request2);
        assertEquals(job.getSharedData().getRequestType(), request2.getClass());
        assertEquals(job.getSharedData().getUserId(), userId);
        assertEquals(job.getSharedData().getTestApi(), testApi);
        assertEquals(job.getSharedData().getJobUuid(), job.getUuid());
        assertNotNull(job.getUuid());
        assertNotEquals(job.getUuid(), parentJob.getUuid());
        assertEquals(job.getStatus(), expectedJobStatus);
        assertEquals(job.getSharedData().getRootJobId(), grandJob.getUuid());
        assertEquals(job.getTemplateId(), parentJob.getUuid());
        assertEquals(job.getIndexInBulk().intValue(), 0);

    }
}
