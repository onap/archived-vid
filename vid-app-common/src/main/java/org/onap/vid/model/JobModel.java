package org.onap.vid.model;

import org.onap.vid.job.Job;
import org.onap.vid.job.JobType;

import java.util.UUID;

public class JobModel {

    private UUID uuid;
    private Job.JobStatus status;
    private UUID templateId;
    private JobType type;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Job.JobStatus getStatus() {
        return status;
    }

    public void setStatus(Job.JobStatus status) {
        this.status = status;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

}
