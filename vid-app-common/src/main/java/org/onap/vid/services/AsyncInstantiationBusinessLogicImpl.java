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

import static com.google.common.collect.Streams.concat;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Stream.empty;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.onap.vid.controller.MsoController.SVC_INSTANCE_ID;
import static org.onap.vid.controller.MsoController.VNF_INSTANCE_ID;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.hibernate.SessionFactory;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.exceptions.DbFailureUncheckedException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.MaxRetriesException;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.Action;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.ResourceInfo;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.ServiceInfo.ServiceAction;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.MsoUtil;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.RequestStatus;
import org.onap.vid.properties.Features;
import org.onap.vid.utils.DaoUtils;
import org.onap.vid.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.togglz.core.manager.FeatureManager;

@Service
public class AsyncInstantiationBusinessLogicImpl implements
        AsyncInstantiationBusinessLogic {

    private static final int MAX_RETRIES_GETTING_COUNTER = 100;
    private static final int MAX_RETRIES_GETTING_FREE_NAME_FROM_AAI = 10000;
    public static final String NAME_FOR_CHECK_AAI_STATUS = "NAME_FOR_CHECK_AAI_STATUS";

    private final JobAdapter jobAdapter;

    private final JobsBrokerService jobService;

    private final CloudOwnerService cloudOwnerService;

    private final AsyncInstantiationRepository asyncInstantiationRepository;

    private SessionFactory sessionFactory;

    private AaiClientInterface aaiClient;

    private FeatureManager featureManager;

    private AuditService auditService;


    private int maxRetriesGettingFreeNameFromAai = MAX_RETRIES_GETTING_FREE_NAME_FROM_AAI;

    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AsyncInstantiationBusinessLogicImpl.class);
    private Map<String, JobStatus> msoStateToJobStatusMap = ImmutableMap.<String, JobStatus>builder()
            .put("inprogress", JobStatus.IN_PROGRESS)
            .put("failed", JobStatus.FAILED)
            .put("pause", JobStatus.PAUSE)
            .put("paused", JobStatus.PAUSE)
            .put("complete", JobStatus.COMPLETED)
            .put("pending", JobStatus.IN_PROGRESS)
            .put("pendingmanualtask", JobStatus.PAUSE)
            .put("unlocked", JobStatus.IN_PROGRESS)
            .put("aborted", JobStatus.COMPLETED_WITH_ERRORS)
            .put("rolledback", JobStatus.FAILED)
            .put("rolledbacktoassigned", JobStatus.FAILED)
            .put("rolledbacktocreated", JobStatus.FAILED)
            .build();


    @Autowired
    public AsyncInstantiationBusinessLogicImpl(JobAdapter jobAdapter,
                                               JobsBrokerService jobService,
                                               SessionFactory sessionFactory,
                                               AaiClientInterface aaiClient,
                                               FeatureManager featureManager,
                                               CloudOwnerService cloudOwnerService, AsyncInstantiationRepository asyncInstantiationRepository,
                                               AuditService auditService) {
        this.jobAdapter = jobAdapter;
        this.jobService = jobService;
        this.sessionFactory = sessionFactory;
        this.aaiClient = aaiClient;
        this.featureManager = featureManager;
        this.cloudOwnerService = cloudOwnerService;
        this.asyncInstantiationRepository = asyncInstantiationRepository;
        this.auditService = auditService;
    }

    @Override
    public List<ServiceInfo> getAllServicesInfo() {
        return asyncInstantiationRepository.getAllServicesInfo();
    }

    JobType getJobType(ServiceInstantiation request) {
        if (request.isALaCarte()) {
            switch (defaultIfNull(request.getAction(), Action.Create)) {
                case Delete:
                    return JobType.ALaCarteService;
                case None:
                    return JobType.ALaCarteService;
                default:
                    return JobType.ALaCarteServiceInstantiation;
            }
        } else {
            return JobType.MacroServiceInstantiation;
        }
    }

    @Override
    public List<UUID> pushBulkJob(ServiceInstantiation request, String userId) {

        List<UUID> uuids = new ArrayList<>();
        Date createdBulkDate = Calendar.getInstance().getTime();
        int bulkSize = request.getBulkSize();
        UUID templateId = UUID.randomUUID();
        for (int i = 0; i < bulkSize; i++) {
            ServiceInstantiation clonedServiceInstantiation = cloneServiceInstantiation(request);
            ServiceInstantiation requestPerJob = prepareServiceToBeUnique(clonedServiceInstantiation);
            requestPerJob = clearStatusFromRequest(requestPerJob);
            ServiceInfo.ServiceAction serviceAction = getAction(requestPerJob);
            JobType jobType = getJobType(requestPerJob);
            final String optimisticUniqueServiceInstanceName = bulkSize>1 ? //only bulk with more than 1 service need to get multiple names
                    getOptimisticUniqueServiceInstanceName(requestPerJob.getInstanceName()) : requestPerJob.getInstanceName();
            Job job = jobAdapter.createServiceInstantiationJob(jobType, requestPerJob, templateId, userId, request.getTestApi(), optimisticUniqueServiceInstanceName, i);
            UUID jobId = job.getUuid();

            asyncInstantiationRepository.saveServiceInfo(createServiceInfo(
                userId, requestPerJob, jobId, templateId, createdBulkDate,
                optimisticUniqueServiceInstanceName, serviceAction,
                requestSummaryOrNull(requestPerJob)));
            asyncInstantiationRepository.addJobRequest(jobId, requestPerJob);
            auditService.auditVidStatus(jobId, job.getStatus());
            uuids.add(jobId);

            jobService.add(job);
        }
        return uuids;
    }

    Map<String, Long> requestSummaryOrNull(ServiceInstantiation request) {
        if (!featureManager.isActive(Features.FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE)) {
            return null;
        }

        if (getAction(request) != ServiceAction.INSTANTIATE) {
            return null;
        }

        return summarizedChildrenMap(request);
    }

    public Map<String, Long> summarizedChildrenMap(ServiceInstantiation serviceInstantiation){
        Stream<String> existingTypesStream =
            allDeepChildResources(serviceInstantiation)
                .map(this::getModelTypes)
                .flatMap(identity());

        Map<String, Long> existingTypesCounters =
            existingTypesStream.collect(groupingBy(identity(), counting()));

        return existingTypesCounters;
    }

    private Stream<String> getModelTypes(BaseResource resource) {
        return concat(
            Stream.of(resource)
                .map(BaseResource::getModelInfo)
                .filter(Objects::nonNull)
                .map(ModelInfo::getModelType),
            streamVolumeGroups(resource)
        );
    }

    private Stream<String> streamVolumeGroups(BaseResource resource) {
        return hasVolumeGroup(resource)
            ? Stream.of("volumeGroup")
            : empty();
    }

    private boolean hasVolumeGroup(BaseResource resource) {
        return
            resource instanceof VfModule
            && isNotEmpty(((VfModule) resource).getVolumeGroupInstanceName());
    }

    private Stream<BaseResource> allDeepChildResources(BaseResource resource) {
        return resource
            .getChildren()
            .stream()
            .map(it -> concat(Stream.of(it), allDeepChildResources(it)))
            .flatMap(identity());
    }

    private ServiceInfo.ServiceAction getAction(ServiceInstantiation request) {
        if (request.getAction() == null) {
            //throw new GenericUncheckedException("Required 'action' field not provided at service level");
            return Action.Create.getServiceAction();
        }
        return request.getAction().getServiceAction();
    }


    private String getOptimisticUniqueServiceInstanceName(String instanceName) {
        return isNotEmpty(instanceName) ? getUniqueNameFromDbOnly(instanceName) : instanceName;
    }

    protected ServiceInfo createServiceInfo(String userId, ServiceInstantiation serviceInstantiation, UUID jobId,
        UUID templateId, Date createdBulkDate, String optimisticUniqueServiceInstanceName,
        ServiceInfo.ServiceAction serviceAction, Map<String, Long> requestSummary) {
        return new ServiceInfo(
                userId,
                serviceInstantiation.isALaCarte(),
                Job.JobStatus.PENDING, serviceInstantiation.isPause(), jobId, templateId,
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
                serviceInstantiation.getGlobalSubscriberId(),
                serviceInstantiation.getInstanceId(),
                optimisticUniqueServiceInstanceName,
                serviceInstantiation.getModelInfo().getModelVersionId(),
                serviceInstantiation.getModelInfo().getModelName(),
                serviceInstantiation.getModelInfo().getModelVersion(),
                createdBulkDate,
                serviceAction,
                false,
                requestSummary);
    }

    @Override
    public boolean isPartOfBulk(UUID jobId) {
        if (jobId == null) {
            return false;
    }
        ServiceInfo serviceInfo = asyncInstantiationRepository.getServiceInfoByJobId(jobId);
        UUID templateId = serviceInfo.getTemplateId();
        if (templateId != null) {
            return getNumberOfJobsInBulk(templateId) > 1;
    }
        return false;

    }

    private int getNumberOfJobsInBulk(UUID templateId) {
        String hqlSelectJob = "from JobDaoImpl where templateId = :templateId";
        return DaoUtils.tryWithSessionAndTransaction(sessionFactory, session ->
            session.createQuery(hqlSelectJob)
                    .setText("templateId", templateId.toString())
                    .list()
                    .size()
        );
    }

    @Override
    public String getServiceInstantiationPath(ServiceInstantiation serviceInstantiationRequest) {
        //in case pause flag is true - use assign , else - use create.
        return MsoBusinessLogicImpl.validateEndpointPath(
                serviceInstantiationRequest.isPause() ?
                        MsoProperties.MSO_REST_API_SERVICE_INSTANCE_ASSIGN : MsoProperties.MSO_RESTAPI_SERVICE_INSTANCE
        );
    }

    @Override
    public String getServiceDeletionPath(String serviceInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath( MsoProperties.MSO_RESTAPI_SERVICE_INSTANCE)  + "/" + serviceInstanceId;
    }

    @Override
    public String getVnfInstantiationPath(String serviceInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE).
                replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
    }

    @Override
    public String getVnfDeletionPath(String serviceInstanceId, String vnfInstanceId) {
        return (MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE)
                + '/' + vnfInstanceId)
                .replaceFirst(SVC_INSTANCE_ID, serviceInstanceId).replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
    }

    @Override
    public String getNetworkInstantiationPath(String serviceInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_NETWORK_INSTANCE).
                replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
    }

    @Override
    public String getVfmoduleInstantiationPath(String serviceInstanceId, String vnfInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE)
                .replaceFirst(SVC_INSTANCE_ID, serviceInstanceId)
                .replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
    }

    @Override
    public String getVfModuleReplacePath(String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId)
    {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE)
                .replaceFirst(SVC_INSTANCE_ID, serviceInstanceId)
                .replaceFirst(VNF_INSTANCE_ID, vnfInstanceId)
                + "/" + vfModuleInstanceId
                + "/replace";
    }

    @Override
    public String getVfModuleDeletePath(String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE)
                .replaceFirst(SVC_INSTANCE_ID, serviceInstanceId)
                .replaceFirst(VNF_INSTANCE_ID, vnfInstanceId)
                + "/" + vfModuleInstanceId;
    }

    @Override
    public String getVolumeGroupInstantiationPath(String serviceInstanceId, String vnfInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE)
                .replaceFirst(SVC_INSTANCE_ID, serviceInstanceId)
                .replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
    }

    @Override
    public String getInstanceGroupInstantiationPath() {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_INSTANCE_GROUP);
    }

    @Override
    public String getInstanceGroupMemberInstantiationPath(String vnfGroupInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_INSTANCE_GROUP)
                + '/' + vnfGroupInstanceId + "/addMembers";
    }

    @Override
    public String getInstanceGroupDeletePath(String instanceGroupId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_INSTANCE_GROUP)
                + '/' + instanceGroupId;
    }

    @Override
    public String getInstanceGroupMemberDeletePath(String vnfGroupInstanceId){
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_INSTANCE_GROUP)
                + '/' + vnfGroupInstanceId + "/removeMembers";
    }

    @Override
    public String getNetworkDeletePath(String serviceInstanceId, String networkInstanceId) {
        return (MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_NETWORK_INSTANCE)
                + "/" + networkInstanceId)
                .replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
    }

    @Override
    public String getResumeRequestPath(String requestId) {
        return MsoBusinessLogicImpl.validateEndpointPath("mso.restapi.resume.orc.req")
                .replaceFirst("<request_id>", requestId);
    }

    @Override
    public String getOrchestrationRequestsPath() {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_GET_ORC_REQ);
    }

    @Override
    public ServiceInfo updateServiceInfo(UUID jobUUID, Consumer<ServiceInfo> serviceUpdater) {
        ServiceInfo serviceInfo = asyncInstantiationRepository.getServiceInfoByJobId(jobUUID);
        serviceUpdater.accept(serviceInfo);
        asyncInstantiationRepository.saveServiceInfo(serviceInfo);
        return serviceInfo;
    }

    @Override
    public ServiceInfo updateServiceInfoAndAuditStatus(UUID jobUuid, JobStatus jobStatus) {
        auditService.auditVidStatus(jobUuid, jobStatus);
        return updateServiceInfo(jobUuid, x -> setServiceInfoStatus(x, jobStatus));
    }

    private boolean isRetryEnabledForStatus(JobStatus jobStatus) {
        return jobStatus==JobStatus.COMPLETED_AND_PAUSED || (featureManager.isActive(Features.FLAG_1902_RETRY_JOB) &&
                (jobStatus==JobStatus.COMPLETED_WITH_ERRORS || jobStatus==JobStatus.FAILED));
    }

    private void setServiceInfoStatus(ServiceInfo serviceInfo, JobStatus jobStatus) {
        serviceInfo.setJobStatus(jobStatus);
        serviceInfo.setStatusModifiedDate(new Date());
        serviceInfo.setRetryEnabled(isRetryEnabledForStatus(jobStatus));
    }

    public Job.JobStatus calcStatus(AsyncRequestStatus asyncRequestStatus) {
        String msoRequestState = asyncRequestStatus.request.requestStatus.getRequestState().toLowerCase().replaceAll("[^a-z]+", "");
        JobStatus jobStatus = msoStateToJobStatusMap.get(msoRequestState);
        return (jobStatus != null ? jobStatus : JobStatus.IN_PROGRESS);
    }

    @Override
    public void handleFailedInstantiation(UUID jobUUID) {
        ServiceInfo serviceInfo = asyncInstantiationRepository.getServiceInfoByJobId(jobUUID);
        List<ServiceInfo> serviceInfoList = asyncInstantiationRepository.getServiceInfoByTemplateIdAndJobStatus(serviceInfo.getTemplateId(), JobStatus.PENDING);
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
        ServiceInfo serviceInfo = asyncInstantiationRepository.getServiceInfoByJobId(jobUUID);
        if (!serviceInfo.getJobStatus().isFinal()) {
            String message = String.format("jobId %s: Service status does not allow hide service, status = %s",
                    serviceInfo.getJobId(),
                    serviceInfo.getJobStatus());
            logger.error(EELFLoggerDelegate.errorLogger, message);
            throw new OperationNotAllowedException(message);
        }
        serviceInfo.setHidden(true);
        asyncInstantiationRepository.saveServiceInfo(serviceInfo);
    }

    @Override
    public int getCounterForName(String name) {

        String hqlSelectNC = "from NameCounter where name = :name";
        String hqlUpdateCounter = "update NameCounter set counter = :newCounter " +
                "where name= :name " +
                "and counter= :prevCounter";

        Integer counter = null;
        GenericUncheckedException lastException = null;
        for (int i = 0; i< MAX_RETRIES_GETTING_COUNTER && counter==null; i++) {
            try {
                counter = calcCounter(name, hqlSelectNC, hqlUpdateCounter);
            }
            catch (GenericUncheckedException exception) {
                lastException = exception; //do nothing, we will try again in the loop
            }
        }

        if (counter!=null) {
            return counter;
        }

        throw lastException!=null ? new DbFailureUncheckedException(lastException) :
                new DbFailureUncheckedException("Failed to get counter for "+name+" due to unknown error");

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
                    return 0;
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

        for (int i=0; i<getMaxRetriesGettingFreeNameFromAai(); i++) {
            String newName = getUniqueNameFromDbOnly(name);
            if (isNameFreeInAai(newName, resourceType)) {
                return newName;
            }
        }

        throw new MaxRetriesException("can't find unused name for "+name, getMaxRetriesGettingFreeNameFromAai());
    }

    @Override
    public ServiceInstantiation prepareServiceToBeUnique(ServiceInstantiation serviceInstantiation) {
        serviceInstantiation.setBulkSize(1);
        return replaceAllTrackById(serviceInstantiation);
    }
    private<T extends BaseResource> T replaceAllTrackById(T resource) {
        resource.setTrackById(UUID.randomUUID().toString());
        resource.getChildren().forEach(this::replaceAllTrackById);
        return resource;
    }

    private ServiceInstantiation cloneServiceInstantiation(ServiceInstantiation serviceInstantiation) {
        try {
            return JACKSON_OBJECT_MAPPER.readValue(
                JACKSON_OBJECT_MAPPER.writeValueAsBytes(serviceInstantiation), ServiceInstantiation.class);
        } catch (IOException e) {
            throw new GenericUncheckedException(e);
        }
    }

    <T extends BaseResource> T clearStatusFromRequest(T resource) {
        resource.setIsFailed(false);
        resource.setStatusMessage(null);
        resource.getChildren().forEach(this::clearStatusFromRequest);
        return resource;
    }

    @Override
    public List<UUID> retryJob(ServiceInstantiation request, UUID jobId, String userId ) {
        updateServiceInfo(jobId, si->si.setRetryEnabled(false));
        return pushBulkJob(request, userId);
    }

    @Override
    public List<UUID> retryJob(UUID jobId, String userId) {
        ServiceInstantiation serviceInstantiationRequest = asyncInstantiationRepository.getJobRequest(jobId);
        enrichBulkForRetry(serviceInstantiationRequest, jobId);

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, "retry ServiceInstantiation request: "+
                    JACKSON_OBJECT_MAPPER.writeValueAsString(serviceInstantiationRequest));
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "failed to log retry of ServiceInstantiation request ", e);
        }
        return retryJob(serviceInstantiationRequest, jobId, userId);
    }

    @Override
    public ServiceInstantiation getBulkForRetry(UUID jobId) {
         return enrichBulkForRetry( asyncInstantiationRepository.getJobRequest(jobId), jobId);
    }

    @Override
    public void addResourceInfo(JobSharedData sharedData, Job.JobStatus jobStatus, String instanceId) {
        String trackById = ((BaseResource) sharedData.getRequest()).getTrackById();
        ResourceInfo resourceInfo = new ResourceInfo(trackById, sharedData.getRootJobId(), instanceId, jobStatus, null);
        asyncInstantiationRepository.saveResourceInfo(resourceInfo);
    }

    @Override
    public void addFailedResourceInfo(JobSharedData sharedData, RestObject msoResponse) {
        String trackById = ((BaseResource) sharedData.getRequest()).getTrackById();
        String errorMessage = MsoUtil.formatExceptionAdditionalInfo(msoResponse.getStatusCode(), msoResponse.getRaw());
        AsyncRequestStatus asyncRequestStatus = convertMessageToAsyncRequestStatus(errorMessage);
        ResourceInfo resourceInfo = new ResourceInfo(trackById, sharedData.getRootJobId(), null, JobStatus.FAILED, asyncRequestStatus);

        asyncInstantiationRepository.saveResourceInfo(resourceInfo);
    }

    @Override
    public void updateResourceInfo(JobSharedData sharedData, JobStatus jobStatus, AsyncRequestStatus message) {
        ResourceInfo resourceInfo = asyncInstantiationRepository.getResourceInfoByTrackId(((BaseResource) sharedData.getRequest()).getTrackById());
        resourceInfo.setJobStatus(jobStatus);
        if (jobStatus.isFailure()) {
            resourceInfo.setErrorMessage(message);
        }
        asyncInstantiationRepository.saveResourceInfo(resourceInfo);
    }

    public AsyncRequestStatus convertMessageToAsyncRequestStatus(String message) {
        RequestStatus requestStatus = new RequestStatus("FAILED", message, TimeUtils.zonedDateTimeToString(ZonedDateTime.now()));
        AsyncRequestStatus.Request request = new AsyncRequestStatus.Request(requestStatus);
        return new AsyncRequestStatus(request);
    }

    protected String getUniqueNameFromDbOnly(String name) {
        int counter = getCounterForName(name);
        return formatNameAndCounter(name, counter);
    }

    //the method is protected so we can call it in the UT
    protected String formatNameAndCounter(String name, int counter) {
        return counter==0 ? name : name + "_" + String.format("%03d", counter);
    }

    private boolean isNameFreeInAai(String name, ResourceType resourceType){
        return !aaiClient.isNodeTypeExistsByName(name, resourceType);
    }

    @Override
    public ServiceInstantiation enrichBulkForRetry(ServiceInstantiation serviceInstantiation, UUID jobId){
        Map<String, ResourceInfo> resourceInfoByTrackId = asyncInstantiationRepository.getResourceInfoByRootJobId(jobId);

        return setResourceStatus(resourceInfoByTrackId, serviceInstantiation);
    }

    protected String readStatusMsg(ResourceInfo resourceInfo){
        if(resourceInfo!=null && resourceInfo.getErrorMessage()!=null && resourceInfo.getErrorMessage().request != null &&resourceInfo.getErrorMessage().request.requestStatus != null ) {
            return resourceInfo.getErrorMessage().request.requestStatus.getStatusMessage();
        }
        return null;
    }

    private<T extends BaseResource> T setResourceStatus(Map<String, ResourceInfo> resourceInfoByTrackId, T resource) {
        ResourceInfo resourceInfo = resourceInfoByTrackId.get(resource.getTrackById());
        if(resourceInfo != null) {
            boolean failed = resourceInfo.getJobStatus().isFailure();
            resource.setIsFailed(failed);
            resource.setStatusMessage(readStatusMsg(resourceInfo));
            if (!failed) {
                // if(resource.getAction().equals(Action.Delete)){
                // TODO not yet implemented- completed after delete should remove the node
                resource.setAction(Action.None);
                resource.setInstanceId(resourceInfo.getInstanceId());
            }
        }
        resource.getChildren().forEach(child -> setResourceStatus(resourceInfoByTrackId, child));
        return resource;
    }

}
