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

package org.onap.vid.services;

import java.util.List;
import java.util.UUID;
import org.onap.vid.job.Job;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.mso.rest.AsyncRequestStatus;

public interface AuditService {

    void setFailedAuditStatusFromMso(UUID jobUuid, String requestId, int statusCode, String msoResponse);

    List<JobAuditStatus> getAuditStatusFromMsoByRequestId(UUID jobId, UUID requestId);

    List<JobAuditStatus> getAuditStatusFromMsoByInstanceId(JobAuditStatus.ResourceTypeFilter resourceTypeFilter, UUID instanceId, UUID jobId);

    List<JobAuditStatus> getAuditStatusFromMsoByJobId(UUID jobId);

    void auditVidStatus(UUID jobUUID, Job.JobStatus jobStatus);

    void auditMsoStatus(UUID jobUUID, AsyncRequestStatus.Request msoRequestStatus);

    void auditMsoStatus(UUID jobUUID, String jobStatus, String requestId, String additionalInfo);

    List<AsyncRequestStatus.Request> retrieveRequestsFromMsoByServiceIdAndRequestTypeAndScope(String instanceId, String requestType, String modelType);

    List<JobAuditStatus> getAuditStatuses(UUID jobUUID, JobAuditStatus.SourceStatus source);

    JobAuditStatus getResourceAuditStatus(String trackById);
}
