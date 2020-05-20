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

package org.onap.vid.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.onap.vid.job.impl.JobSharedData;

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
        CREATING(false),
        PENDING_RESOURCE(false),
        COMPLETED_AND_PAUSED(true, false),
        ;

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

        public static final List<JobStatus> FINAL_STATUS = Stream.of(JobStatus.values()).filter(JobStatus::isFinal).collect(Collectors.toList());

    }
}
