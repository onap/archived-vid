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

package org.onap.vid.model.serviceInstantiation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Collections;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.mso.model.ModelInfo;

public class InstanceGroupMember extends BaseResource implements JobAdapter.AsyncJobRequest{

    public InstanceGroupMember(@JsonProperty("instanceId") String instanceId,
        @JsonProperty("action") String action,
        @JsonProperty("trackById") String trackById,
        @JsonProperty("isFailed") Boolean isFailed,
        @JsonProperty("statusMessage") String statusMessage,
        @JsonProperty("position") Integer position,
        @JsonProperty("originalName") String originalName) {
        super(new ModelInfo(), null, action, null, null, null, null, false, instanceId, trackById, isFailed, statusMessage,
            position, null, originalName);
    }

    @Override
    protected String getModelType() {
        return "vnf";
    }

    @Override
    public Collection<BaseResource> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public JobType getJobType() {
        return JobType.InstanceGroupMember;
    }
}
