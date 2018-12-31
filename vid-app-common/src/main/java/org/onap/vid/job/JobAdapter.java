package org.onap.vid.job;

import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.JobModel;

import java.util.Map;
import java.util.UUID;

/**
 * kind of factory for creating jobs and converting them to Job Model
 */
public interface JobAdapter {
    JobModel toModel(Job job);

    Job createServiceInstantiationJob(JobType jobType, AsyncJobRequest request, UUID templateId, String userId, String optimisticUniqueServiceInstanceName, Integer indexInBulk);

    Job createChildJob(JobType jobType, Job.JobStatus jobStatus, AsyncJobRequest request, JobSharedData parentSharedData, Map<String, Object> jobData);

    // Marks types that are an AsyncJob payload
    interface AsyncJobRequest {
    }

}
