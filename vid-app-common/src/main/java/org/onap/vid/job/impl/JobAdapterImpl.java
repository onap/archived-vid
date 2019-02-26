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
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.model.JobModel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class JobAdapterImpl implements JobAdapter {

    @Override
    public JobModel toModel(Job job) {
        JobModel jobModel = new JobModel();
        jobModel.setUuid(job.getUuid());
        jobModel.setStatus(job.getStatus());
        jobModel.setTemplateId(job.getTemplateId());
        return jobModel;
    }

    @Override
    public Job createServiceInstantiationJob(JobType jobType, AsyncJobRequest request, UUID templateId, String userId, String optimisticUniqueServiceInstanceName, Integer indexInBulk){
        JobDaoImpl job = createJob(jobType, Job.JobStatus.PENDING , userId);
        job.setSharedData(new JobSharedData(job.getUuid(), userId, request));
        job.setTypeAndData(jobType, ImmutableMap.of(
                "optimisticUniqueServiceInstanceName", optimisticUniqueServiceInstanceName
        ));
        job.setTemplateId(templateId);
        job.setIndexInBulk(indexInBulk);
        return job;
    }

    @Override
    public Job createChildJob(JobType jobType, Job.JobStatus jobStatus, AsyncJobRequest request, JobSharedData parentSharedData, Map<String, Object> jobData) {
        JobDaoImpl job = createJob(jobType, jobStatus , parentSharedData.getUserId());
        job.setSharedData(new JobSharedData(job.getUuid(), request, parentSharedData));
        job.setTypeAndData(jobType, jobData);
        return job;
    }

    protected JobDaoImpl createJob(JobType jobType, Job.JobStatus jobStatus, String userId) {
        JobDaoImpl job = new JobDaoImpl();
        job.setTypeAndData(jobType, null);
        job.setStatus(jobStatus);
        job.setUuid(UUID.randomUUID());
        job.setUserId(userId);
        return job;
    }

}
