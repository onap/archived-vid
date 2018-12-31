package org.onap.vid.services;

import org.onap.vid.model.JobAuditStatus;

import java.util.List;
import java.util.UUID;

public interface AuditService {

    void setFailedAuditStatusFromMso(UUID jobUuid, String requestId, int statusCode, String msoResponse);


    List<JobAuditStatus> getAuditStatusFromMsoByRequestId(UUID jobId, UUID requestId);

    List<JobAuditStatus> getAuditStatusFromMsoByServiceInstanceId(UUID jobId, UUID serviceInstanceId);

    List<JobAuditStatus> getAuditStatusFromMsoByJobId(UUID jobId);
}
