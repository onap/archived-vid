package org.onap.vid.job;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.UUID;

public interface Job {

    UUID getUuid();

    void setUuid(UUID uuid);

    JobStatus getStatus();

    void setStatus(JobStatus status);

    @JsonIgnore
    Map<String, Object> getData();

    void setTypeAndData(JobType jobType, Map<String, Object> data);

    UUID getTemplateId();

    void setTemplateId(UUID templateId);

    void setIndexInBulk(Integer indexInBulk);

    JobType getType();

    enum JobStatus {
        COMPLETED(true),
        FAILED(true),
        IN_PROGRESS(false),
        PAUSE(false),
        PENDING(false),
        STOPPED(true);

        private final Boolean finalStatus;
        public Boolean isFinal(){return finalStatus;}

        JobStatus(Boolean finalStatus)
        {
            this.finalStatus = finalStatus ;
        }
    }
}
