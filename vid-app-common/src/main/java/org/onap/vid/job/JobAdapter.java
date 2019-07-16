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

import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.JobModel;

import java.util.Map;
import java.util.UUID;

/**
 * kind of factory for creating jobs and converting them to Job Model
 */
public interface JobAdapter {
    JobModel toModel(Job job);

    Job createServiceInstantiationJob(JobType jobType, AsyncJobRequest request, UUID templateId, String userId, String testApi, String optimisticUniqueServiceInstanceName, Integer indexInBulk);

    Job createChildJob(JobType jobType, AsyncJobRequest request, JobSharedData parentSharedData, Map<String, Object> jobData, int indexInBulk);

    // Marks types that are an AsyncJob payload
    interface AsyncJobRequest {
    }

}
