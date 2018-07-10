package org.onap.vid.job.impl;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import org.hibernate.annotations.Type;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobType;
import org.onap.vid.model.VidBaseEntity;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;

@Entity
@Table(name = "vid_job")
public class JobDaoImpl extends VidBaseEntity implements Job {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private Job.JobStatus status;
    private JobType type;
    private Map<JobType, Map<String, Object>> data = new TreeMap<>();
    private UUID templateId;
    private UUID uuid;
    private String takenBy;
    private String userId;
    private Integer age = 0;
    private Integer indexInBulk = 0;
    private Date deletedAt;

    @Id
    @Column(name = "JOB_ID", columnDefinition = "CHAR(36)")
    @Type(type="org.hibernate.type.UUIDCharType")
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    //we use uuid instead id. So making id Transient
    @Override
    @Transient
    @JsonIgnore
    public Long getId() {
        return this.getUuid().getLeastSignificantBits();
    }

    @Column(name = "JOB_STATUS")
    @Enumerated(EnumType.STRING)
    public Job.JobStatus getStatus() {
        return status;
    }

    public void setStatus(Job.JobStatus status) {
        this.status = status;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "JOB_TYPE")
    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    //the columnDefinition is relevant only for UT
    @Column(name = "JOB_DATA", columnDefinition = "VARCHAR(30000)")
    public String getDataRaw() {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new GenericUncheckedException(e);
        }
    }

    public void setDataRaw(String data) {
        try {
            this.data = objectMapper.readValue(data, new TypeReference<Map<JobType, Map<String, Object>>>() {
            });
        } catch (IOException e) {
            throw new GenericUncheckedException(e);
        }
    }

    @Transient
    public Map<String, Object> getData() {
        return data.get(getType());
    }

    @Override
    public void setTypeAndData(JobType jobType, Map<String, Object> data) {
        // *add* the data to map,
        // then change state to given type
        this.type = jobType;
        this.data.put(jobType, data);
    }

    @Column(name = "TAKEN_BY")
    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "TEMPLATE_ID", columnDefinition = "CHAR(36)")
    public UUID getTemplateId() {
        return templateId;
    }

    @Override
    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    @Column(name="INDEX_IN_BULK")
    public Integer getIndexInBulk() {
        return indexInBulk;
    }

    @Override
    public void setIndexInBulk(Integer indexInBulk) {
        this.indexInBulk = indexInBulk;
    }

    @Column(name="USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name="AGE")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(name="DELETED_AT")
    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobDaoImpl)) return false;
        JobDaoImpl daoJob = (JobDaoImpl) o;
        return Objects.equals(getUuid(), daoJob.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("status", status)
                .add("type", type)
                .add("templateId", templateId)
                .add("uuid", uuid)
                .add("takenBy", takenBy)
                .add("userId", userId)
                .add("age", age)
                .add("created", created)
                .add("modified", modified)
                .add("deletedAt", deletedAt)
                .add("data", data)
                .toString();
    }
}
