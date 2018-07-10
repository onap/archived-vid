package org.onap.vid.services;

import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.model.JobBulk;
import org.onap.vid.model.JobModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BulkInstantiationServiceImpl implements BulkInstantiationService {

    private JobsBrokerService jobsBrokerService;
    private JobAdapter jobAdapter;

    @Autowired
    public BulkInstantiationServiceImpl(JobsBrokerService jobsBrokerService, JobAdapter jobAdapter) {
        this.jobsBrokerService = jobsBrokerService;
        this.jobAdapter = jobAdapter;
    }

    @Override
    public JobBulk saveBulk(Map<String, Object> bulkRequest) {
        List<Job> jobList = jobAdapter.createBulkOfJobs(bulkRequest);
        jobList.forEach(jobsBrokerService::add);
        return jobAdapter.toModelBulk(jobList);
    }

    @Override
    public JobModel getJob(UUID uuid) {
        Job job = jobsBrokerService.peek(uuid);

        if (job == null || job.getUuid() == null) {
            throw new NotFoundException("Job with uuid " + uuid + " not found");
        }
        return jobAdapter.toModel(job);
    }


}
