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
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;
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
        String filter = "requestId:EQUALS:" + requestId + "&format=statusDetail";
        return getAuditStatusFromMso(jobId, filter, null);
    }

    @Override
    public List<JobAuditStatus> getAuditStatusFromMsoByInstanceId(JobAuditStatus.ResourceTypeFilter resourceTypeFilter, UUID instanceId, UUID jobId) {
        String filter = resourceTypeFilter.getFilterBy() + ":EQUALS:" + instanceId + "&format=statusDetail";
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
                convertAsyncRequestStatusToJobAuditStatusAdditionalInfo(status, defaultName)
        ).collect(Collectors.toList());
    }
    private JobAuditStatus convertAsyncRequestStatusToJobAuditStatusAdditionalInfo(AsyncRequestStatus status, String defaultName) {
        if (status == null) {
            return null;
        }
        UUID requestId = null;
        String instanceName = defaultName;
        String jobStatus = null;
        String additionalInfo = null;
        String finishTime = null;
        String instanceType = null;
        String modelType = "";
        String startTime = null;
        AsyncRequestStatus.Request request = status.request;
        if (request != null) {
            if (request.requestId != null) {
                requestId = UUID.fromString(request.requestId);
            }
            instanceName = extractInstanceName(instanceName, request);
            instanceType = request.requestType;
            if (request.requestDetails != null && request.requestDetails.modelInfo != null) {
                modelType = request.requestDetails.modelInfo.modelType;
            }
            startTime = request.startTime;

            if (request.requestStatus != null) {
                jobStatus = request.requestStatus.getRequestState();
                additionalInfo = buildAdditionalInfo(request);

                if (!request.requestStatus.getAdditionalProperties().isEmpty() &&
                    request.requestStatus.getAdditionalProperties().get("finishTime") != null) {
                    finishTime = request.requestStatus.getAdditionalProperties().get("finishTime").toString();
                } else {
                    finishTime = request.requestStatus.getTimestamp();
                }
            }
        }
        return new JobAuditStatus(requestId, instanceName, modelType, instanceType, startTime, finishTime,
            jobStatus, additionalInfo);
    }
    private String buildAdditionalInfo(AsyncRequestStatus.Request request) {
        String source = "";
        String statusMessage = "";
        String flowStatus = "";
        String subscriptionServiceType = "";
        String alacarte = "";
        String testApi = "";
        String projectName = "";
        String owningEntityId = "";
        String owningEntityName = "";
        String requestScope = "";
        String tenantId = "";
        String tenantName = "";
        String cloudOwner = "";
        String platformName = "";
        String lineOfBusiness = "";
        MessageFormat mfBasedOnService = null;
        String otherInfo = "";
        MessageFormat mf = new MessageFormat("{0}" +
            "{1}" +
            "{2}" +
            "{3}" +
            "{4}" +
            "{5}"+
            "{6}");
        requestScope = request.requestScope;
        statusMessage = request.requestStatus != null ? "<b>StatusMessage:</b>"+request.requestStatus.getStatusMessage()+ "</br>": "";
        if(request.requestDetails != null && request.requestDetails.requestInfo != null) {
            source = "<b>Source:</b> "+request.requestDetails.requestInfo.source + "</br>";
        }
        if(request.requestStatus != null && request.requestStatus.getFlowStatus() != null) {
            flowStatus = "<b>FlowStatus:</b> "+request.requestStatus.getFlowStatus()+ "</br>";
        }
        if(request.requestDetails != null && request.requestDetails.requestParameters != null &&
            request.requestDetails.requestParameters.subscriptionServiceType != null) {
            subscriptionServiceType = "<b>SubscriptionServiceType:</b> "+request.requestDetails.requestParameters.subscriptionServiceType+ "</br>";
        }
        if(request.requestDetails != null && request.requestDetails.requestParameters != null &&
            request.requestDetails.requestParameters.aLaCarte != null) {
            alacarte = "<b>Alacarte:</b> "+request.requestDetails.requestParameters.aLaCarte+ "</br>";
        }
        if(request.requestDetails != null && request.requestDetails.requestParameters != null &&
            request.requestDetails.requestParameters.testApi != null) {
            testApi = "<b>TestAPI:</b> "+request.requestDetails.requestParameters.testApi+ "</br>";
        }

        if(request.requestDetails != null) {
            if("service".equals(requestScope)) {
                mfBasedOnService = new MessageFormat("<b>ProjectName: {0}</br>" +
                    "<b>OwningEntityId:</b> {1}</br>" +
                    "<b>OwningEntityName:</b> {2}</br>");
                projectName = request.requestDetails.project != null ? request.requestDetails.project.projectName : "";
                owningEntityId = request.requestDetails.owningEntity != null ? request.requestDetails.owningEntity.owningEntityId : "";
                owningEntityName = request.requestDetails.owningEntity != null ? request.requestDetails.owningEntity.owningEntityName : "";
                Object[] arr1 = new Object[]{projectName, owningEntityId, owningEntityName};
                otherInfo = mfBasedOnService.format(arr1);
            } else if("vnf".equals(requestScope)) {
                mfBasedOnService = new MessageFormat("<b>TenantId:</b> {0}</br>" +
                    "<b>TenantName:</b> {1}</br>" +
                    "<b>CloudOwner:</b> {2}</br>" +
                    "<b>PlatformName:</b> {3}</br>" +
                    "<b>LineOfBusiness:</b> {4}</br>");
                tenantId = request.requestDetails.cloudConfiguration != null ? request.requestDetails.cloudConfiguration.tenantId : "";
                tenantName= request.requestDetails.cloudConfiguration != null ? request.requestDetails.cloudConfiguration.tenantName : "";
                cloudOwner= request.requestDetails.cloudConfiguration != null ? request.requestDetails.cloudConfiguration.cloudOwner : "";
                platformName= request.requestDetails.platform != null ? request.requestDetails.platform.platformName : "";
                lineOfBusiness= request.requestDetails.lineOfBusiness != null ? request.requestDetails.lineOfBusiness.lineOfBusinessName : "";
                Object[] arr2 = new Object[]{tenantId, tenantName, cloudOwner,platformName,lineOfBusiness};
                otherInfo = mfBasedOnService.format(arr2);
            } else if("vfModule".equals(requestScope)) {
                mfBasedOnService = new MessageFormat("<b>TenantId:</b> {0}</br>" +
                    "<b>TenantName:</b> {1}</br>" +
                    "<b>CloudOwner:</b> {2}</br>");
                tenantId = request.requestDetails.cloudConfiguration != null ? request.requestDetails.cloudConfiguration.tenantId : "";
                tenantName= request.requestDetails.cloudConfiguration != null ? request.requestDetails.cloudConfiguration.tenantName : "";
                cloudOwner= request.requestDetails.cloudConfiguration != null ? request.requestDetails.cloudConfiguration.cloudOwner : "";
                Object[] arr2 = new Object[]{tenantId, tenantName, cloudOwner};
                otherInfo = mfBasedOnService.format(arr2);
            }
        }
        Object[] objArray = {source, statusMessage, flowStatus, subscriptionServiceType, alacarte, testApi, otherInfo};
        return StringUtils.chomp(mf.format(objArray));
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
