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

import static org.apache.commons.lang3.ObjectUtils.notEqual;

import org.jetbrains.annotations.NotNull;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.job.Job;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.mso.*;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.AsyncRequestStatusList;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AuditServiceImpl implements AuditService{

    private final RestMsoImplementation restMso;
    private final AsyncInstantiationRepository asyncInstantiationRepository;

    @Inject
    public AuditServiceImpl(RestMsoImplementation restMso, AsyncInstantiationRepository asyncInstantiationRepository) {
        this.restMso = restMso;
        this.asyncInstantiationRepository = asyncInstantiationRepository;
    }

    @Override
    public void setFailedAuditStatusFromMso(UUID jobUuid, String requestId, int statusCode, String msoResponse){
        final String failedMsoRequestStatus = "FAILED";
        String additionalInfo = MsoUtil.formatExceptionAdditionalInfo(statusCode, msoResponse);
        auditMsoStatus(jobUuid, failedMsoRequestStatus, requestId, additionalInfo);
    }

    @Override
    public List<JobAuditStatus> getAuditStatusFromMsoByRequestId(UUID jobId, UUID requestId) {
        String filter = "requestId:EQUALS:" + requestId;
        return getAuditStatusFromMso(jobId, filter, null);
    }

    @Override
    public List<JobAuditStatus> getAuditStatusFromMsoByInstanceId(JobAuditStatus.ResourceTypeFilter resourceTypeFilter, UUID instanceId, UUID jobId) {
        String filter = resourceTypeFilter.getFilterBy() + ":EQUALS:" + instanceId;
        return getAuditStatusFromMso(jobId, filter, instanceId);
    }

    @Override
    public List<JobAuditStatus> getAuditStatusFromMsoByJobId(UUID jobId) {
        List<JobAuditStatus> auditStatuses = getAuditStatuses(jobId, JobAuditStatus.SourceStatus.MSO);
        String instanceName = getInstanceNameFromServiceInfo(jobId);
        auditStatuses.stream().forEach(status ->
                status.setInstanceName(instanceName)
        );
        return auditStatuses;
    }

    @Override
    public void auditVidStatus(UUID jobUUID, Job.JobStatus jobStatus){
        JobAuditStatus vidStatus = new JobAuditStatus(jobUUID, jobStatus.toString(), JobAuditStatus.SourceStatus.VID);
        auditStatus(vidStatus);
    }

    @Override
    public void auditMsoStatus(UUID jobUUID, AsyncRequestStatus.Request msoRequestStatus){
        auditMsoStatus(jobUUID, msoRequestStatus.requestStatus.getRequestState(), msoRequestStatus.requestId, msoRequestStatus.requestStatus.getStatusMessage());
    }

    @Override
    public void auditMsoStatus(UUID jobUUID, String jobStatus, String requestId, String additionalInfo){
        JobAuditStatus msoStatus = new JobAuditStatus(jobUUID, jobStatus, JobAuditStatus.SourceStatus.MSO,
                requestId != null ? UUID.fromString(requestId) : null,
                additionalInfo);
        auditStatus(msoStatus);
    }

    private void auditStatus(JobAuditStatus jobAuditStatus){
        JobAuditStatus latestStatus = getLatestAuditStatus(jobAuditStatus.getJobId(), jobAuditStatus.getSource());

        if (notEqual(jobAuditStatus, latestStatus)) {
            jobAuditStatus.setOrdinal(nextOrdinalAfter(latestStatus));
            asyncInstantiationRepository.addJobAudiStatus(jobAuditStatus);
        }
    }

    protected int nextOrdinalAfter(JobAuditStatus jobAuditStatus) {
        return jobAuditStatus == null ? 0 : (jobAuditStatus.getOrdinal() + 1);
    }

    private JobAuditStatus getLatestAuditStatus(UUID jobUUID, JobAuditStatus.SourceStatus source){
        List<JobAuditStatus> list = getAuditStatuses(jobUUID, source);
        return !list.isEmpty() ? list.get(list.size()-1) : null;
    }

    public List<JobAuditStatus> getAuditStatuses(UUID jobUUID, JobAuditStatus.SourceStatus source) {
        return asyncInstantiationRepository.getAuditStatuses(jobUUID, source);
    }

    @Override
    //modelType is requestScope in MSO response
    public List<AsyncRequestStatus.Request> retrieveRequestsFromMsoByServiceIdAndRequestTypeAndScope(String instanceId, String requestType, String modelType) {
        String filter = JobAuditStatus.ResourceTypeFilter.SERVICE.getFilterBy() + ":EQUALS:" + instanceId;
        List<AsyncRequestStatus> msoStatuses = getAsyncRequestStatusListFromMso(filter);
        return msoStatuses.stream()
                .filter(x -> Objects.equals(x.request.requestType, requestType) && Objects.equals(x.request.requestScope, modelType))
                .map(x -> x.request)
                .collect(Collectors.toList());
    }

    private List<JobAuditStatus> getAuditStatusFromMso(UUID jobId, String filter, UUID instanceId) {

        List<AsyncRequestStatus> msoStatuses = getAsyncRequestStatusListFromMso(filter);

        //add service name from service info for each audit status (in case that serviceInstanceId is null all statuses belong to service)
        String userInstanceName = (instanceId == null && jobId != null) ? getInstanceNameFromServiceInfo(jobId) : null;
        return convertMsoResponseStatusToJobAuditStatus(msoStatuses, userInstanceName);
    }

    @NotNull
    private List<AsyncRequestStatus> getAsyncRequestStatusListFromMso(String filter) {
        String path = MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_GET_ORC_REQS) + "filter=" + filter;
        RestObject<AsyncRequestStatusList> msoResponse = restMso.GetForObject(path , AsyncRequestStatusList.class);
        if (msoResponse.getStatusCode() >= 400 || msoResponse.get() == null) {
            throw new BadResponseFromMso(msoResponse);
        }
        return msoResponse.get().getRequestList();
    }

    private String getInstanceNameFromServiceInfo(UUID jobId) {
        return asyncInstantiationRepository.getServiceInfoByJobId(jobId).getServiceInstanceName();
    }

    protected List<JobAuditStatus> convertMsoResponseStatusToJobAuditStatus(List<AsyncRequestStatus> msoStatuses, String defaultName){
        return msoStatuses.stream().map(status ->
                convertAsyncRequestStatusToJobAuditStatus(status, defaultName)
        ).collect(Collectors.toList());
    }

    private JobAuditStatus convertAsyncRequestStatusToJobAuditStatus(AsyncRequestStatus status, String defaultName){
        if (status == null) {
            return null;
        }

        UUID requestId = null;
        String instanceName = defaultName;
        String jobStatus = null;
        String additionalInfo = null;
        String created = null;
        String instanceType = null;

        AsyncRequestStatus.Request request = status.request;
        if(request != null) {
            if (request.requestId != null) {
                requestId = UUID.fromString(request.requestId);
            }
            instanceName = extractInstanceName(instanceName, request);
            instanceType = request.requestScope;
            if(request.requestStatus != null) {
                jobStatus = request.requestStatus.getRequestState();
                additionalInfo = request.requestStatus.getStatusMessage();
                if(!request.requestStatus.getAdditionalProperties().isEmpty() &&
                        request.requestStatus.getAdditionalProperties().get("finishTime") != null) {
                    created = request.requestStatus.getAdditionalProperties().get("finishTime").toString();
                } else {
                    created = request.requestStatus.getTimestamp();
                }
            }
        }
        return new JobAuditStatus(instanceName, jobStatus, requestId, additionalInfo, created, instanceType);
    }

    private String extractInstanceName(String instanceName, AsyncRequestStatus.Request request) {
        if(request.requestDetails != null && request.requestDetails.requestInfo != null && request.requestDetails.requestInfo.instanceName != null) {
            instanceName = request.requestDetails.requestInfo.instanceName;
        }
        return instanceName;
    }

    @Override
    public JobAuditStatus getResourceAuditStatus(String trackById) {
        AsyncRequestStatus asyncRequestStatus = asyncInstantiationRepository.getResourceInfoByTrackId(trackById).getErrorMessage();
        return convertAsyncRequestStatusToJobAuditStatus(asyncRequestStatus, null);
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
