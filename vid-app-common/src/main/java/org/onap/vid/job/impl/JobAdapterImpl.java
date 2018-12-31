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
