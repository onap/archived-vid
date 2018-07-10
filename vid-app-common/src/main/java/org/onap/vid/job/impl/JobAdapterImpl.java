package org.onap.vid.job.impl;

import com.google.common.collect.ImmutableMap;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.model.JobBulk;
import org.onap.vid.model.JobModel;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public JobBulk toModelBulk(List<Job> jobList) {
        return new JobBulk(jobList.stream().map(this::toModel).collect(Collectors.toList()));
    }

    @Override
    public Job createJob(JobType jobType, AsyncJobRequest request, UUID templateId, String userId, Integer indexInBulk){
        JobDaoImpl job = new JobDaoImpl();
        job.setStatus(Job.JobStatus.PENDING);
        job.setTypeAndData(jobType, ImmutableMap.of(
                "request", request,
                "userId", userId));
        job.setTemplateId(templateId);
        job.setIndexInBulk(indexInBulk);
        job.setUserId(userId);
        return job;
    }

    @Override
    public List<Job> createBulkOfJobs(Map<String, Object> bulkRequest) {
        int count;
        JobType jobType;

        try {
            count = (Integer) bulkRequest.get("count");
            jobType = JobType.valueOf((String) bulkRequest.get("type"));
        } catch (Exception exception) {
            throw new BadRequestException(exception);
        }
        List<Job> jobList = new ArrayList<>(count + 1);
        UUID templateId = UUID.randomUUID();
        for (int i = 0; i < count; i++) {
            Job child = new JobDaoImpl();
            child.setTypeAndData(jobType, bulkRequest);
            child.setStatus(Job.JobStatus.PENDING);
            child.setTemplateId(templateId);
            child.setIndexInBulk(i);
            jobList.add(child);
        }
        return jobList;
    }

}
