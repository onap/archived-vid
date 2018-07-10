package org.onap.vid.services;

import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.Job;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.onap.vid.mso.rest.AsyncRequestStatus;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface AsyncInstantiationBusinessLogic {

    List<String> PARAMS_TO_IGNORE = Arrays.asList("vnf_name", "vf_module_name");

    List<ServiceInfo> getAllServicesInfo();

    List<UUID> pushBulkJob(ServiceInstantiation request, String userId);

    RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateServiceInstantiationRequest(UUID uuid, ServiceInstantiation details, String userId);

    String getServiceInstantiationPath(ServiceInstantiation serviceInstantiationRequest);

    String getOrchestrationRequestsPath();

    ServiceInfo getServiceInfoByJobId(UUID jobUUID);

    List<JobAuditStatus> getAuditStatuses(UUID jobUUID, JobAuditStatus.SourceStatus source);

    ServiceInfo updateServiceInfo(UUID jobUUID, Consumer<ServiceInfo> serviceUpdater);

    ServiceInfo updateServiceInfoAndAuditStatus(UUID jobUuid, Job.JobStatus jobStatus);

    void auditVidStatus(UUID jobUUID, Job.JobStatus jobStatus);

    void auditMsoStatus(UUID jobUUID, AsyncRequestStatus.Request msoRequestStatus);

    void auditMsoStatus(UUID jobUUID, String jobStatus, String requestId, String additionalInfo);

    Job.JobStatus calcStatus(AsyncRequestStatus asyncRequestStatus);

    void handleFailedInstantiation(UUID jobUUID);

    void deleteJob(UUID jobId);

    void hideServiceInfo(UUID jobUUID);

    int getCounterForName(String name);

    int getMaxRetriesGettingFreeNameFromAai();

    void setMaxRetriesGettingFreeNameFromAai(int maxRetriesGettingFreeNameFromAai);

    String getUniqueName(String name, ResourceType resourceType);
}
