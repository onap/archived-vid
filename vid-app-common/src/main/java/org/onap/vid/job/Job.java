package org.onap.vid.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.onap.vid.job.impl.JobSharedData;

import java.util.Map;
import java.util.UUID;

public interface Job {

    UUID getUuid();

    void setUuid(UUID uuid);

    JobStatus getStatus();

    void setStatus(JobStatus status);

    @JsonIgnore
    Map<String, Object> getData();

    JobSharedData getSharedData();

    void setTypeAndData(JobType jobType, Map<String, Object> data);

    UUID getTemplateId();

    void setTemplateId(UUID templateId);

    Integer getIndexInBulk();

    void setIndexInBulk(Integer indexInBulk);

    JobType getType();

    enum JobStatus {
        COMPLETED(true, false),
        FAILED(true, true),
        IN_PROGRESS(false),
        RESOURCE_IN_PROGRESS(false),
        PAUSE(false),
        PENDING(false),
        STOPPED(true, true),
        COMPLETED_WITH_ERRORS(true, true),
        COMPLETED_WITH_NO_ACTION(true, false),
        CREATING(false);

        private final Boolean finalStatus;
        public Boolean isFinal(){return finalStatus;}

        private final Boolean failure;
        public Boolean isFailure() {
            return failure;
        }

        JobStatus(Boolean finalStatus)
        {
            this(finalStatus, false);
        }

        JobStatus(Boolean finalStatus, boolean failure) {
            this.finalStatus = finalStatus;
            this.failure = failure;
        }

    }
}
