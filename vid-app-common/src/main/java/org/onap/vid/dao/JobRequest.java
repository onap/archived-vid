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

package org.onap.vid.dao;

import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;
import org.onap.vid.model.VidBaseEntity;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;


@DynamicUpdate
@SelectBeforeUpdate
@Entity
@Table(name = "vid_job_request")
public class JobRequest extends VidBaseEntity {

    private UUID jobId;
    private ServiceInstantiation request;


    public JobRequest(UUID jobId, ServiceInstantiation request) {
        this.jobId = jobId;
        this.request = request;
    }

    public JobRequest() {
    }

    @Id
    @Column(name = "JOB_ID", columnDefinition = "CHAR(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    @Transient
    public ServiceInstantiation getRequest() {
        return request;
    }

    public void setRequest(ServiceInstantiation request) {
        this.request = request;
    }

    //the columnDefinition is used only in UT
    @Column(name = "REQUEST", columnDefinition = "VARCHAR(30000)")
    public String getRequestRaw() {
        try {
            return JACKSON_OBJECT_MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRequestRaw(String raw) {
        try {
            this.request = JACKSON_OBJECT_MAPPER.readValue(raw, ServiceInstantiation.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof JobRequest))
            return false;
        JobRequest that = (JobRequest) o;
        return Objects.equals(getJobId(), that.getJobId()) &&
                Objects.equals(getRequest(), that.getRequest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobId(), getRequest());
    }
}

