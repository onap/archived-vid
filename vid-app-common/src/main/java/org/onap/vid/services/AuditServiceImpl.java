package org.onap.vid.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.AsyncRequestStatusList;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AuditServiceImpl implements AuditService{

    private final AsyncInstantiationBusinessLogic asyncInstantiationBL;
    private final RestMsoImplementation restMso;

    @Inject
    public AuditServiceImpl(AsyncInstantiationBusinessLogic asyncInstantiationBL, RestMsoImplementation restMso) {
        this.asyncInstantiationBL = asyncInstantiationBL;
        this.restMso = restMso;
    }

    @Override
    public void setFailedAuditStatusFromMso(UUID jobUuid, String requestId, int statusCode, String msoResponse){
        final String failedMsoRequestStatus = "FAILED";
        String additionalInfo = formatExceptionAdditionalInfo(statusCode, msoResponse);
        asyncInstantiationBL.auditMsoStatus(jobUuid, failedMsoRequestStatus, requestId, additionalInfo);
    }

    private String formatExceptionAdditionalInfo(int statusCode, String msoResponse) {
        String errorMsg = "Http Code:" + statusCode;
        if (!StringUtils.isEmpty(msoResponse)) {
            String filteredJson;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                filteredJson = StringUtils.defaultIfEmpty(
                        objectMapper.readTree(msoResponse).path("serviceException").toString().replaceAll("[\\{\\}]","") ,
                        msoResponse
                );
            } catch (JsonParseException e) {
                filteredJson = msoResponse;
            } catch (IOException e) {
                throw new GenericUncheckedException(e);
            }

            errorMsg = errorMsg + ", " + filteredJson;
        }
        return errorMsg;
    }

    @Override
    public List<JobAuditStatus> getAuditStatusFromMsoByRequestId(UUID jobId, UUID requestId) {
        String filter = "requestId:EQUALS:" + requestId;
        return getAuditStatusFromMso(jobId, filter, null);
    }

    @Override
    public List<JobAuditStatus> getAuditStatusFromMsoByServiceInstanceId(UUID jobId, UUID serviceInstanceId) {
        String filter = "serviceInstanceId:EQUALS:" + serviceInstanceId;
        return getAuditStatusFromMso(jobId, filter, serviceInstanceId);
    }

    @Override
    public List<JobAuditStatus> getAuditStatusFromMsoByJobId(UUID jobId) {
        List<JobAuditStatus> auditStatuses = asyncInstantiationBL.getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO);
        String instanceName = getInstanceNameFromServiceInfo(jobId);
        auditStatuses.stream().forEach(status ->
            status.setInstanceName(instanceName)
        );
        return auditStatuses;
    }



    private List<JobAuditStatus> getAuditStatusFromMso(UUID jobId, String filter, UUID serviceInstanceId) {

        String path = MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_GET_ORC_REQS) + "filter=" + filter;
        RestObject<AsyncRequestStatusList> msoResponse = restMso.GetForObject(path , AsyncRequestStatusList.class);
        if (msoResponse.getStatusCode() >= 400 || msoResponse.get() == null) {
            throw new BadResponseFromMso(msoResponse);
        }

        //add service name from service info for each audit status (in case that serviceInstanceId is null all statuses belong to service)
        String userInstanceName = serviceInstanceId == null ? getInstanceNameFromServiceInfo(jobId): null;
        return convertMsoResponseStatusToJobAuditStatus(msoResponse.get().getRequestList(), userInstanceName);
    }

    private String getInstanceNameFromServiceInfo(UUID jobId) {
        return asyncInstantiationBL.getServiceInfoByJobId(jobId).getServiceInstanceName();
    }

    protected List<JobAuditStatus> convertMsoResponseStatusToJobAuditStatus(List<AsyncRequestStatus> msoStatuses, String defaultName){
        return msoStatuses.stream().map(status -> {
            UUID requestId = null;
            String instanceName = defaultName;
            String jobStatus = null;
            String additionalInfo = null;
            String created = null;
            String instanceType = null;

            AsyncRequestStatus.Request request = status.request;
            if(request != null) {
                requestId = UUID.fromString(request.requestId);
                instanceName = extractInstanceName(instanceName, request);
                instanceType = request.requestScope;
                if(request.requestStatus != null) {
                    jobStatus = request.requestStatus.getRequestState();
                    additionalInfo = request.requestStatus.getStatusMessage();
                    if(!request.requestStatus.getAdditionalProperties().isEmpty()) {
                        created = request.requestStatus.getAdditionalProperties().get("finishTime") != null? request.requestStatus.getAdditionalProperties().get("finishTime").toString() : request.requestStatus.getTimestamp();
                    }
                }
            }
            return new JobAuditStatus(instanceName, jobStatus, requestId, additionalInfo, created, instanceType);
        }).collect(Collectors.toList());
    }

    private String extractInstanceName(String instanceName, AsyncRequestStatus.Request request) {
        if(request.requestDetails != null && request.requestDetails.requestInfo != null && request.requestDetails.requestInfo.instanceName != null) {
            instanceName = request.requestDetails.requestInfo.instanceName;
        }
        return instanceName;
    }

    public static class BadResponseFromMso extends RuntimeException {
        private final RestObject<AsyncRequestStatusList> msoResponse;

        public BadResponseFromMso(RestObject<AsyncRequestStatusList> msoResponse) {
            this.msoResponse = msoResponse;
        }

        public RestObject<AsyncRequestStatusList> getMsoResponse() {
            return msoResponse;
        }
    }
}
