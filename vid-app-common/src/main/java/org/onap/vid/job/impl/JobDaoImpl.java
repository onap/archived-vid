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


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.MoreObjects;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobException;
import org.onap.vid.job.JobType;
import org.onap.vid.model.VidBaseEntity;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

/*
 The following 2 annotations let hibernate to update only fields that actually have been changed.
 DynamicUpdate tell hibernate to update only dirty fields.
 SelectBeforeUpdate is needed since during update the entity is detached (get and update are in different sessions)
*/
@DynamicUpdate()
@SelectBeforeUpdate()
@Entity
@Table(name = "vid_job")
public class JobDaoImpl extends VidBaseEntity implements Job {

    private Job.JobStatus status;
    private JobType type;
    private JobData data = new JobData();
    private UUID templateId;
    private UUID uuid;
    private String takenBy;
    private String userId;
    private Integer age = 0;
    private Integer indexInBulk = 0;
    private Date deletedAt;
    private String build;

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
            return JACKSON_OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new GenericUncheckedException(e);
        }
    }

    public void setDataRaw(String data) {
        try {
            this.data = JACKSON_OBJECT_MAPPER.readValue(data, JobData.class);
        } catch (IOException e) {
            throw new JobException("Error parsing job's data", uuid, e);
        }
    }

    @Transient
    public Map<String, Object> getData() {
        return data.getCommandData().get(getType());
    }

    public void setSharedData(JobSharedData sharedData) {
        this.data.setSharedData(sharedData);
    }

    @Override
    @Transient
    public JobSharedData getSharedData() {
        return this.data.getSharedData();
    }

    @Override
    public void setTypeAndData(JobType jobType, Map<String, Object> data) {
        // *add* the data to map,
        // then change state to given type
        this.type = jobType;
        this.data.getCommandData().put(jobType, data);
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

    @Override
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

    @Column(name = "BUILD", columnDefinition = "VARCHAR(100)")
    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof JobDaoImpl))
            return false;
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
                .add("data", data)
                .add("templateId", templateId)
                .add("uuid", uuid)
                .add("takenBy", takenBy)
                .add("userId", userId)
                .add("age", age)
                .add("indexInBulk", indexInBulk)
                .add("deletedAt", deletedAt)
                .add("build", build)
                .toString();
    }
}
