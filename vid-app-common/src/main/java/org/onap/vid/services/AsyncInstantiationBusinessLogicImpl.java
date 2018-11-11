/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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

import com.google.common.collect.ImmutableMap;
import io.joshworks.restclient.http.HttpResponse;
import java.io.IOException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiOverTLSClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.exceptions.InvalidAAIResponseException;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.domain.mso.CloudConfiguration;
import org.onap.vid.domain.mso.SubscriberInfo;
import org.onap.vid.exceptions.DbFailureUncheckedException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.MaxRetriesException;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.utils.DaoUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.onap.vid.utils.Logging.debugRequestDetails;

@Service
public class AsyncInstantiationBusinessLogicImpl implements AsyncInstantiationBusinessLogic {

    private static final int MAX_RETRIES_GETTING_COUNTER = 100;
    private static final int MAX_RETRIES_GETTING_FREE_NAME_FROM_AAI = 10000;
    private static final String NAME_FOR_CHECK_AAI_STATUS = "NAME_FOR_CHECK_AAI_STATUS";

    private final DataAccessService dataAccessService;

    private final JobAdapter jobAdapter;

    private final JobsBrokerService jobService;

    private SessionFactory sessionFactory;

    private AaiClientInterface aaiClient;

    private AaiOverTLSClientInterface aaiOverTLSClient;

    private int maxRetriesGettingFreeNameFromAai = MAX_RETRIES_GETTING_FREE_NAME_FROM_AAI;

    private static final EELFLoggerDelegate logger = EELFLoggerDelegate
        .getLogger(AsyncInstantiationBusinessLogicImpl.class);
    private Map<String, JobStatus> msoStateToJobStatusMap = ImmutableMap.<String, JobStatus>builder()
        .put("inprogress", JobStatus.IN_PROGRESS)
        .put("failed", JobStatus.FAILED)
        .put("pause", JobStatus.PAUSE)
        .put("paused", JobStatus.PAUSE)
        .put("complete", JobStatus.COMPLETED)
        .put("pending", JobStatus.IN_PROGRESS)
        .put("pendingmanualtask", JobStatus.PAUSE)
        .put("unlocked", JobStatus.IN_PROGRESS)
        .build();


    @Autowired
    public AsyncInstantiationBusinessLogicImpl(DataAccessService dataAccessService,
        JobAdapter jobAdapter,
        JobsBrokerService jobService,
        SessionFactory sessionFactory,
        AaiClientInterface aaiClient,
        AaiOverTLSClientInterface aaiOverTLSClient) {
        this.dataAccessService = dataAccessService;
        this.jobAdapter = jobAdapter;
        this.jobService = jobService;
        this.sessionFactory = sessionFactory;
        this.aaiClient = aaiClient;
        this.aaiOverTLSClient = aaiOverTLSClient;
    }

    @Override
    public List<ServiceInfo> getAllServicesInfo() {
        return dataAccessService
            .getList(ServiceInfo.class, filterByCreationDateAndNotDeleted(), orderByCreatedDateAndStatus(), null);
    }

    private String filterByCreationDateAndNotDeleted() {
        LocalDateTime minus3Months = LocalDateTime.now().minusMonths(3);
        Timestamp filterDate = Timestamp.valueOf(minus3Months);
        return " where" +
            "   hidden = false" +
            "   and deleted_at is null" +  // don't fetch deleted
            "   and created >= '" + filterDate + "' ";
    }

    private String orderByCreatedDateAndStatus() {
        return " createdBulkDate DESC ,\n" +
            "  (CASE jobStatus\n" +
            "   WHEN 'COMPLETED' THEN 0\n" +
            "   WHEN 'FAILED' THEN 0\n" +
            "   WHEN 'IN_PROGRESS' THEN 1\n" +
            "   WHEN 'PAUSE' THEN 2\n" +
            "   WHEN 'PENDING' THEN 3\n" +
            "   WHEN 'STOPPED' THEN 3 END),\n" +
            "  statusModifiedDate ";
    }

    @Override
    public List<UUID> pushBulkJob(ServiceInstantiation request, String userId) {
        List<UUID> uuids = new ArrayList<>();
        Date createdBulkDate = Calendar.getInstance().getTime();
        int bulkSize = request.getBulkSize();
        UUID templateId = UUID.randomUUID();
        for (int i = 0; i < bulkSize; i++) {
            Job job = jobAdapter.createJob(JobType.ServiceInstantiation, request, templateId, userId, i);
            UUID jobId = jobService.add(job);
            auditVidStatus(jobId, job.getStatus());
            uuids.add(jobId);
            dataAccessService.saveDomainObject(createServiceInfo(userId, request, jobId, templateId, createdBulkDate),
                DaoUtils.getPropsMap());
        }
        return uuids;
    }

    private ServiceInfo createServiceInfo(String userId, ServiceInstantiation serviceInstantiation, UUID jobId,
        UUID templateId, Date createdBulkDate) {
        return new ServiceInfo(
            userId, Job.JobStatus.PENDING, serviceInstantiation.isPause(), jobId, templateId,
            serviceInstantiation.getOwningEntityId(),
            serviceInstantiation.getOwningEntityName(),
            serviceInstantiation.getProjectName(),
            serviceInstantiation.getAicZoneId(),
            serviceInstantiation.getAicZoneName(),
            serviceInstantiation.getTenantId(),
            serviceInstantiation.getTenantName(),
            serviceInstantiation.getLcpCloudRegionId(),
            null,
            serviceInstantiation.getSubscriptionServiceType(),
            serviceInstantiation.getSubscriberName(),
            null,
            serviceInstantiation.getInstanceName(),
            serviceInstantiation.getModelInfo().getModelInvariantId(),
            serviceInstantiation.getModelInfo().getModelName(),
            serviceInstantiation.getModelInfo().getModelVersion(),
            createdBulkDate
        );
    }


    @Override
    public RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateServiceInstantiationRequest(UUID jobId,
        ServiceInstantiation payload, String userId) {

        ServiceInstantiationRequestDetails.ServiceInstantiationOwningEntity owningEntity = new ServiceInstantiationRequestDetails.ServiceInstantiationOwningEntity(
            payload.getOwningEntityId(), payload.getOwningEntityName());

        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setGlobalSubscriberId(payload.getGlobalSubscriberId());

        String serviceInstanceName = null;
        if (payload.isUserProvidedNaming()) {
            serviceInstanceName = getUniqueName(payload.getInstanceName(), ResourceType.SERVICE_INSTANCE);
            String finalServiceInstanceName = serviceInstanceName;
            updateServiceInfo(jobId, x -> x.setServiceInstanceName(finalServiceInstanceName));
        }
        ServiceInstantiationRequestDetails.RequestInfo requestInfo = new ServiceInstantiationRequestDetails.RequestInfo(
            serviceInstanceName,
            payload.getProductFamilyId(),
            "VID",
            payload.isRollbackOnFailure(),
            userId);

        List<ServiceInstantiationRequestDetails.ServiceInstantiationService> serviceInstantiationService = new LinkedList<>();
        List<Map<String, String>> unFilteredInstanceParams =
            payload.getInstanceParams() != null ? payload.getInstanceParams() : new LinkedList<>();
        List<Map<String, String>> filteredInstanceParams = removeUnNeededParams(unFilteredInstanceParams);
        ServiceInstantiationRequestDetails.ServiceInstantiationService serviceInstantiationService1 = new ServiceInstantiationRequestDetails.ServiceInstantiationService(
            payload.getModelInfo(),
            serviceInstanceName,
            filteredInstanceParams,
            createServiceInstantiationVnfList(payload)
        );
        serviceInstantiationService.add(serviceInstantiationService1);

        ServiceInstantiationRequestDetails.RequestParameters requestParameters = new ServiceInstantiationRequestDetails.RequestParameters(
            payload.getSubscriptionServiceType(), false, serviceInstantiationService);

        ServiceInstantiationRequestDetails.Project project =
            payload.getProjectName() != null ? new ServiceInstantiationRequestDetails.Project(payload.getProjectName())
                : null;

        ServiceInstantiationRequestDetails requestDetails = new ServiceInstantiationRequestDetails(
            payload.getModelInfo(), owningEntity, subscriberInfo,
            project, requestInfo, requestParameters);

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> requestDetailsWrapper = new RequestDetailsWrapper(
            requestDetails);
        debugRequestDetails(requestDetailsWrapper, logger);
        return requestDetailsWrapper;
    }

    private List<Map<String, String>> removeUnNeededParams(List<Map<String, String>> instanceParams) {
        List<String> keysToRemove = new ArrayList<>();
        if (instanceParams != null && !instanceParams.isEmpty()) {
            for (String key : instanceParams.get(0).keySet()) {
                for (String paramToIgnore : PARAMS_TO_IGNORE) {
                    if ((key.equalsIgnoreCase(paramToIgnore))) {
                        keysToRemove.add(key);
                    }
                }
            }
            for (String key : keysToRemove) {
                instanceParams.get(0).remove(key);
            }
            //TODO will be removed on once we stop using List<Map<String, String>>
            if (instanceParams.get(0).isEmpty()) {
                return Collections.emptyList();
            }
        }
        return instanceParams;
    }

    private ServiceInstantiationRequestDetails.ServiceInstantiationVnfList createServiceInstantiationVnfList(
        ServiceInstantiation payload) {
        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.setTenantId(payload.getTenantId());
        cloudConfiguration.setLcpCloudRegionId(payload.getLcpCloudRegionId());

        Map<String, Vnf> vnfs = payload.getVnfs();
        List<ServiceInstantiationRequestDetails.ServiceInstantiationVnf> vnfList = new ArrayList<>();
        for (Vnf vnf : vnfs.values()) {
            Map<String, Map<String, VfModule>> vfModules = vnf.getVfModules();
            List<VfModule> convertedUnFilteredVfModules = convertVfModuleMapToList(vfModules);
            List<VfModule> filteredVfModules = filterInstanceParamsFromVfModuleAndUniqueNames(
                convertedUnFilteredVfModules, vnf.isUserProvidedNaming());
            ServiceInstantiationRequestDetails.ServiceInstantiationVnf serviceInstantiationVnf = new ServiceInstantiationRequestDetails.ServiceInstantiationVnf(
                vnf.getModelInfo(),
                cloudConfiguration,
                vnf.getPlatformName(),
                vnf.getLineOfBusiness(),
                payload.getProductFamilyId(),
                removeUnNeededParams(vnf.getInstanceParams()),
                filteredVfModules,
                vnf.isUserProvidedNaming() ? getUniqueName(vnf.getInstanceName(), ResourceType.GENERIC_VNF) : null
            );
            vnfList.add(serviceInstantiationVnf);
        }

        return new ServiceInstantiationRequestDetails.ServiceInstantiationVnfList(vnfList);
    }

    private List<VfModule> convertVfModuleMapToList(Map<String, Map<String, VfModule>> vfModules) {
        return vfModules.values().stream().flatMap(vfModule -> vfModule.values().stream()).collect(Collectors.toList());
    }

    private List<VfModule> filterInstanceParamsFromVfModuleAndUniqueNames(List<VfModule> unFilteredVfModules,
        boolean isUserProvidedNaming) {
        return unFilteredVfModules.stream().map(vfModule ->
            new VfModule(
                vfModule.getModelInfo(),
                getUniqueNameIfNeeded(isUserProvidedNaming, vfModule.getInstanceName(), ResourceType.VF_MODULE),
                getUniqueNameIfNeeded(isUserProvidedNaming, vfModule.getVolumeGroupInstanceName(),
                    ResourceType.VOLUME_GROUP),
                removeUnNeededParams(vfModule.getInstanceParams())))
            .collect(Collectors.toList());
    }

    private String getUniqueNameIfNeeded(boolean isUserProvidedNaming, String name, ResourceType resourceType) {
        return isUserProvidedNaming && !StringUtils.isEmpty(name) ?
            getUniqueName(name, resourceType) : null;
    }

    @Override
    public String getServiceInstantiationPath(ServiceInstantiation serviceInstantiationRequest) {
        //in case pause flag is true - use assign , else - use create.
        return MsoBusinessLogicImpl.validateEndpointPath(
            serviceInstantiationRequest.isPause() ?
                "mso.restapi.serviceInstanceAssign" : "mso.restapi.serviceInstanceCreate"
        );
    }

    @Override
    public String getOrchestrationRequestsPath() {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_GET_ORC_REQ);
    }

    @Override
    public ServiceInfo updateServiceInfo(UUID jobUUID, Consumer<ServiceInfo> serviceUpdater) {
        ServiceInfo serviceInfo = getServiceInfoByJobId(jobUUID);
        serviceUpdater.accept(serviceInfo);
        dataAccessService.saveDomainObject(serviceInfo, DaoUtils.getPropsMap());
        return serviceInfo;
    }

    @Override
    public ServiceInfo updateServiceInfoAndAuditStatus(UUID jobUuid, JobStatus jobStatus) {
        auditVidStatus(jobUuid, jobStatus);
        return updateServiceInfo(jobUuid, x -> setServiceInfoStatus(x, jobStatus));
    }

    private void setServiceInfoStatus(ServiceInfo serviceInfo, JobStatus jobStatus) {
        serviceInfo.setJobStatus(jobStatus);
        serviceInfo.setStatusModifiedDate(new Date());
    }

    public ServiceInfo getServiceInfoByJobId(UUID jobUUID) {
        List<ServiceInfo> serviceInfoList = dataAccessService
            .getList(ServiceInfo.class, String.format(" where jobId = '%s' ", jobUUID), null, null);
        if (serviceInfoList.size() != 1) {
            throw new GenericUncheckedException(
                "Failed to retrieve job with uuid " + jobUUID + " from ServiceInfo table. Instances found: "
                    + serviceInfoList.size());
        }
        return serviceInfoList.get(0);
    }

    public List<JobAuditStatus> getAuditStatuses(UUID jobUUID, JobAuditStatus.SourceStatus source) {
        return dataAccessService.getList(
            JobAuditStatus.class,
            String.format(" where SOURCE = '%s' and JOB_ID = '%s'", source, jobUUID),
            " CREATED_DATE ", null);
    }

    private JobAuditStatus getLatestAuditStatus(UUID jobUUID, JobAuditStatus.SourceStatus source) {
        List<JobAuditStatus> list = getAuditStatuses(jobUUID, source);
        return !list.isEmpty() ? list.get(list.size() - 1) : null;
    }

    @Override
    public void auditVidStatus(UUID jobUUID, JobStatus jobStatus) {
        JobAuditStatus vidStatus = new JobAuditStatus(jobUUID, jobStatus.toString(), JobAuditStatus.SourceStatus.VID);
        auditStatus(vidStatus);
    }

    @Override
    public void auditMsoStatus(UUID jobUUID, AsyncRequestStatus.Request msoRequestStatus) {
        auditMsoStatus(jobUUID, msoRequestStatus.requestStatus.getRequestState(), msoRequestStatus.requestId,
            msoRequestStatus.requestStatus.getStatusMessage());
    }

    @Override
    public void auditMsoStatus(UUID jobUUID, String jobStatus, String requestId, String additionalInfo) {
        JobAuditStatus msoStatus = new JobAuditStatus(jobUUID, jobStatus, JobAuditStatus.SourceStatus.MSO,
            requestId != null ? UUID.fromString(requestId) : null,
            additionalInfo);
        auditStatus(msoStatus);
    }

    private void auditStatus(JobAuditStatus jobAuditStatus) {
        JobAuditStatus latestStatus = getLatestAuditStatus(jobAuditStatus.getJobId(), jobAuditStatus.getSource());
        if (latestStatus == null || !latestStatus.equals(jobAuditStatus)) {
            dataAccessService.saveDomainObject(jobAuditStatus, DaoUtils.getPropsMap());
        }

    }

    public Job.JobStatus calcStatus(AsyncRequestStatus asyncRequestStatus) {
        String msoRequestState = asyncRequestStatus.request.requestStatus.getRequestState().toLowerCase()
            .replaceAll("[^a-z]+", "");
        JobStatus jobStatus = msoStateToJobStatusMap.get(msoRequestState);
        return (jobStatus != null ? jobStatus : JobStatus.IN_PROGRESS);
    }

    @Override
    public void handleFailedInstantiation(UUID jobUUID) {
        ServiceInfo serviceInfo = updateServiceInfoAndAuditStatus(jobUUID, JobStatus.FAILED);
        List<ServiceInfo> serviceInfoList = dataAccessService.getList(
            ServiceInfo.class,
            String.format(" where templateId = '%s' and jobStatus = '%s'",
                serviceInfo.getTemplateId(),
                JobStatus.PENDING),
            null, null);
        serviceInfoList.forEach(si -> updateServiceInfoAndAuditStatus(si.getJobId(), JobStatus.STOPPED));
    }

    @Override
    public void deleteJob(UUID jobId) {
        jobService.delete(jobId);
        Date now = new Date();
        updateServiceInfo(jobId, x -> x.setDeletedAt(now));
    }

    @Override
    public void hideServiceInfo(UUID jobUUID) {
        ServiceInfo serviceInfo = getServiceInfoByJobId(jobUUID);
        if (!serviceInfo.getJobStatus().isFinal()) {
            String message = String.format("jobId %s: Service status does not allow hide service, status = %s",
                serviceInfo.getJobId(),
                serviceInfo.getJobStatus());
            logger.error(EELFLoggerDelegate.errorLogger, message);
            throw new OperationNotAllowedException(message);
        }
        serviceInfo.setHidden(true);
        dataAccessService.saveDomainObject(serviceInfo, DaoUtils.getPropsMap());
    }

    @Override
    public int

    getCounterForName(String name) {

        String hqlSelectNC = "from NameCounter where name = :name";
        String hqlUpdateCounter = "update NameCounter set counter = :newCounter " +
            "where name= :name " +
            "and counter= :prevCounter";

        Integer counter = null;
        GenericUncheckedException lastException = null;
        for (int i = 0; i < MAX_RETRIES_GETTING_COUNTER && counter == null; i++) {
            try {
                counter = calcCounter(name, hqlSelectNC, hqlUpdateCounter);
            } catch (GenericUncheckedException exception) {
                lastException = exception; //do nothing, we will try again in the loop
            }
        }

        if (counter != null) {
            return counter;
        }

        throw lastException != null ? new DbFailureUncheckedException(lastException) :
            new DbFailureUncheckedException("Failed to get counter for " + name + " due to unknown error");

    }

    private Integer calcCounter(String name, String hqlSelectNC, String hqlUpdateCounter) {
        Integer counter;
        counter = DaoUtils.tryWithSessionAndTransaction(sessionFactory, session -> {
            NameCounter nameCounter = (NameCounter) session.createQuery(hqlSelectNC)
                .setText("name", name)
                .uniqueResult();
            if (nameCounter != null) {
                int updatedRows = session.createQuery(hqlUpdateCounter)
                    .setText("name", nameCounter.getName())
                    .setInteger("prevCounter", nameCounter.getCounter())
                    .setInteger("newCounter", nameCounter.getCounter() + 1)
                    .executeUpdate();
                if (updatedRows == 1) {
                    return nameCounter.getCounter() + 1;
                }
            } else {
                Object nameAsId = session.save(new NameCounter(name));
                //if save success
                if (nameAsId != null) {
                    return 1;
                }
            }
            //in case of failure return null, in order to continue the loop
            return null;
        });
        return counter;
    }

    @Override
    public int getMaxRetriesGettingFreeNameFromAai() {
        return maxRetriesGettingFreeNameFromAai;
    }

    @Override
    public void setMaxRetriesGettingFreeNameFromAai(int maxRetriesGettingFreeNameFromAai) {
        this.maxRetriesGettingFreeNameFromAai = maxRetriesGettingFreeNameFromAai;
    }

    @Override
    public String getUniqueName(String name, ResourceType resourceType) {
        //check that name aai response well before increasing counter from DB
        //Prevents unnecessary increasing of the counter while AAI doesn't response
        isNameFreeInAai(NAME_FOR_CHECK_AAI_STATUS, resourceType);

        for (int i = 0; i < getMaxRetriesGettingFreeNameFromAai(); i++) {
            int counter = getCounterForName(name);
            String newName = formatNameAndCounter(name, counter);
            if (isNameFreeInAai(newName, resourceType)) {
                return newName;
            }
        }

        throw new MaxRetriesException("find unused name for " + name, getMaxRetriesGettingFreeNameFromAai());
    }

    //the method is protected so we can call it in the UT
    protected String formatNameAndCounter(String name, int counter) {
        return name + "_" + String.format("%03d", counter);
    }

    private boolean isNameFreeInAai(String name, ResourceType resourceType) throws InvalidAAIResponseException {
        HttpResponse<AaiNodeQueryResponse> aaiResponse = aaiOverTLSClient
            .searchNodeTypeByName(name, resourceType);
        if (aaiResponse.getStatus() > 399 || aaiResponse.getBody() == null) {
            try {
                String message = IOUtils.toString(aaiResponse.getRawBody(), "UTF-8");
                throw new InvalidAAIResponseException(aaiResponse.getStatus(), message);
            } catch (IOException e) {
                throw new InvalidAAIResponseException(aaiResponse.getStatus(), aaiResponse.getStatusText());
            }
        }
        return CollectionUtils.isEmpty(aaiResponse.getBody().resultData);
    }

}
