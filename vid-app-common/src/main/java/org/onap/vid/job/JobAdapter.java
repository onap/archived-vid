package org.onap.vid.job;

import org.onap.vid.model.JobBulk;
import org.onap.vid.model.JobModel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * kind of factory for creating jobs and converting them to Job Model
 */
public interface JobAdapter {
    JobModel toModel(Job job);

    JobBulk toModelBulk(List<Job> jobList);

    List<Job> createBulkOfJobs(Map<String, Object> bulkRequest);

    Job createJob(JobType jobType, AsyncJobRequest request, UUID templateId, String userId, Integer indexInBulk);

    // Marks types that are an AsyncJob payload
    public interface AsyncJobRequest {
    }

}
