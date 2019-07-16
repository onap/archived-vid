package org.onap.vid.services;

import org.onap.vid.job.Job;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.mso.rest.AsyncRequestStatus;

import java.util.List;
import java.util.UUID;

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
