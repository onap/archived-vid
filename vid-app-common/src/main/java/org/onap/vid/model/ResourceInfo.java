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

package org.onap.vid.model;

import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobException;
import org.onap.vid.mso.rest.AsyncRequestStatus;

@DynamicUpdate
@SelectBeforeUpdate
@Entity
@Table(name = "vid_resource_info")
public class ResourceInfo extends VidBaseEntity {

    private String trackById;
    private UUID rootJobId;
    private String instanceId;
    private Job.JobStatus jobStatus;
    private AsyncRequestStatus errorMessage;

    public ResourceInfo(){}

    public ResourceInfo(String trackById, UUID rootJobId, String instanceId, Job.JobStatus jobStatus, AsyncRequestStatus errorMessage) {
        this.trackById = trackById;
        this.rootJobId = rootJobId;
        this.instanceId = instanceId;
        this.jobStatus = jobStatus;
        this.errorMessage = errorMessage;
    }

    @Id
    @Column(name = "TRACK_BY_ID", columnDefinition = "CHAR(36)")
    public String getTrackById() {
        return trackById;
    }

    @Column(name = "ROOT_JOB_ID", columnDefinition = "CHAR(36)")
    @Type(type="org.hibernate.type.UUIDCharType")
    public UUID getRootJobId() {
        return rootJobId;
    }

    @Column(name="JOB_STATUS")
    @Enumerated(EnumType.STRING)
    public Job.JobStatus getJobStatus() {
        return jobStatus;
    }

    @Column(name="INSTANCE_ID")
    public String getInstanceId() {
        return instanceId;
    }

    public void setTrackById(String trackById) {
        this.trackById = trackById;
    }

    public void setRootJobId(UUID rootJobId) {
        this.rootJobId = rootJobId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    public void setJobStatus(Job.JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    @Column(name = "ERROR_MESSAGE", columnDefinition = "VARCHAR(30000)")
    public String getErrorMessageRaw() {
        try {
            return JACKSON_OBJECT_MAPPER.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            throw new GenericUncheckedException(e);
        }
    }

    public void setErrorMessageRaw(String failedMessage) {
        try {
            this.errorMessage = JACKSON_OBJECT_MAPPER.readValue(failedMessage, AsyncRequestStatus.class);
        } catch (IOException e) {
            throw new JobException("Error parsing mso failed message", rootJobId, e);
        }
    }

    @Transient
    public AsyncRequestStatus getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(AsyncRequestStatus errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null || getClass() != o.getClass()) 
            return false;
        ResourceInfo that = (ResourceInfo) o;
        return Objects.equals(trackById, that.trackById) &&
                Objects.equals(instanceId, that.instanceId) &&
                jobStatus == that.jobStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackById, instanceId, jobStatus);
    }
}
