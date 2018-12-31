package org.onap.vid.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.exceptions.DbFailureUncheckedException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.MaxRetriesException;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.model.Action;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.NameCounter;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.*;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.model.*;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.RequestParameters;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.*;
import org.onap.vid.mso.model.VfModuleInstantiationRequestDetails.RequestParametersVfModule;
import org.onap.vid.mso.model.VfModuleInstantiationRequestDetails.UserParamMap;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.SubscriberInfo;
import org.onap.vid.properties.Features;
import org.onap.vid.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.togglz.core.manager.FeatureManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.onap.vid.controller.MsoController.SVC_INSTANCE_ID;
import static org.onap.vid.controller.MsoController.VNF_INSTANCE_ID;
import static org.onap.vid.utils.Logging.debugRequestDetails;

@Service
public class AsyncInstantiationBusinessLogicImpl implements
        AsyncInstantiationBusinessLogic {

    private static final int MAX_RETRIES_GETTING_COUNTER = 100;
    private static final int MAX_RETRIES_GETTING_FREE_NAME_FROM_AAI = 10000;
    public static final String NAME_FOR_CHECK_AAI_STATUS = "NAME_FOR_CHECK_AAI_STATUS";
    private static final String VID_SOURCE = "VID";

    private final DataAccessService dataAccessService;

    private final JobAdapter jobAdapter;

    private final JobsBrokerService jobService;

    private final CloudOwnerService cloudOwnerService;

    private SessionFactory sessionFactory;

    private AaiClientInterface aaiClient;

    private FeatureManager featureManager;

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
            .build();


    @Autowired
    public AsyncInstantiationBusinessLogicImpl(DataAccessService dataAccessService,
                                               JobAdapter jobAdapter,
                                               JobsBrokerService jobService,
                                               SessionFactory sessionFactory,
                                               AaiClientInterface aaiClient,
                                               FeatureManager featureManager,
                                               CloudOwnerService cloudOwnerService) {
        this.dataAccessService = dataAccessService;
        this.jobAdapter = jobAdapter;
        this.jobService = jobService;
        this.sessionFactory = sessionFactory;
        this.aaiClient = aaiClient;
        this.featureManager = featureManager;
        this.cloudOwnerService = cloudOwnerService;
    }

    @Override
    public List<ServiceInfo> getAllServicesInfo() {
        return dataAccessService.getList(ServiceInfo.class, filterByCreationDateAndNotDeleted(), orderByCreatedDateAndStatus(), null);
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
                "   WHEN 'COMPLETED_WITH_ERRORS' THEN 0\n" +
                "   WHEN 'IN_PROGRESS' THEN 1\n" +
                "   WHEN 'PAUSE' THEN 2\n" +
                "   WHEN 'PENDING' THEN 3\n" +
                "   WHEN 'STOPPED' THEN 3 END),\n" +
                "  statusModifiedDate ";
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
            ServiceInfo.ServiceAction serviceAction = getAction(request);
            JobType jobType = getJobType(request);
            final String optimisticUniqueServiceInstanceName = getOptimisticUniqueServiceInstanceName(request);
            Job job = jobAdapter.createServiceInstantiationJob(jobType, request, templateId, userId, optimisticUniqueServiceInstanceName, i);
            UUID jobId = jobService.add(job);
            dataAccessService.saveDomainObject(createServiceInfo(userId, request, jobId, templateId, createdBulkDate, optimisticUniqueServiceInstanceName, serviceAction), DaoUtils.getPropsMap());
            auditVidStatus(jobId, job.getStatus());
            uuids.add(jobId);
        }
        return uuids;
    }

    private ServiceInfo.ServiceAction getAction(ServiceInstantiation request) {
        if (request.getAction() == null) {
            //throw new GenericUncheckedException("Required 'action' field not provided at service level");
            return Action.Create.getServiceAction();
        }
        return request.getAction().getServiceAction();
    }


    private String getOptimisticUniqueServiceInstanceName(ServiceInstantiation request) {
        return StringUtils.isNotEmpty(request.getInstanceName()) ? getUniqueNameFromDbOnly(request.getInstanceName()) : request.getInstanceName();
    }

    protected ServiceInfo createServiceInfo(String userId, ServiceInstantiation serviceInstantiation, UUID jobId, UUID templateId, Date createdBulkDate, String optimisticUniqueServiceInstanceName, ServiceInfo.ServiceAction serviceAction) {
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
                serviceAction
        );
    }


    @Override
    public RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateMacroServiceInstantiationRequest(UUID jobId, ServiceInstantiation payload, String optimisticUniqueServiceInstanceName, String userId) {
        String serviceInstanceName = generateServiceName(jobId, payload, optimisticUniqueServiceInstanceName);

        List<ServiceInstantiationService> serviceInstantiationServiceList = generateServiceInstantiationServicesList(payload, serviceInstanceName, createServiceInstantiationVnfList(payload));

        RequestParameters requestParameters = new RequestParameters(payload.getSubscriptionServiceType(), false, serviceInstantiationServiceList);

        ServiceInstantiationRequestDetails requestDetails = generateServiceInstantiationRequestDetails(payload,requestParameters,serviceInstanceName, userId);

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(requestDetails);
        debugRequestDetails(requestDetailsWrapper, logger);

        return requestDetailsWrapper;
    }

    @Override
    public RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateALaCarteServiceInstantiationRequest(UUID jobId, ServiceInstantiation payload, String optimisticUniqueServiceInstanceName, String userId) {
        String serviceInstanceName = generateServiceName(jobId, payload, optimisticUniqueServiceInstanceName);

        List<UserParamNameAndValue> userParams = generateUserParamList();

        RequestParameters requestParameters = new RequestParameters(payload.getSubscriptionServiceType(), true, userParams, payload.getTestApi());

        ServiceInstantiationRequestDetails requestDetails = generateServiceInstantiationRequestDetails(payload,requestParameters,serviceInstanceName, userId);

        RequestDetailsWrapper<ServiceInstantiationRequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(requestDetails);
        debugRequestDetails(requestDetailsWrapper, logger);
        return requestDetailsWrapper;
    }


    @Override
    public RequestDetailsWrapper<ServiceDeletionRequestDetails> generateALaCarteServiceDeletionRequest(UUID jobId, ServiceInstantiation payload, String userId){

        ServiceDeletionRequestDetails.RequestParameters requestParameters = new ServiceDeletionRequestDetails.RequestParameters( true, payload.getTestApi());

        ServiceDeletionRequestDetails.RequestInfo requestInfo = new ServiceDeletionRequestDetails.RequestInfo(
                VID_SOURCE,
                userId);

        ServiceDeletionRequestDetails requestDetails = new ServiceDeletionRequestDetails(payload.getModelInfo(), requestInfo, requestParameters);

        RequestDetailsWrapper<ServiceDeletionRequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(requestDetails);
        debugRequestDetails(requestDetailsWrapper, logger);
        return requestDetailsWrapper;
    }

    @Override
    public RequestDetailsWrapper<VnfInstantiationRequestDetails> generateVnfInstantiationRequest(Vnf vnfDetails, ModelInfo serviceModelInfo, String serviceInstanceId, String userId) {

        VnfInstantiationRequestDetails.RequestInfo requestInfo = new VnfInstantiationRequestDetails.RequestInfo(
                getUniqueNameIfNeeded(vnfDetails.getInstanceName(), ResourceType.GENERIC_VNF),
                vnfDetails.getProductFamilyId(),
                VID_SOURCE,
                vnfDetails.isRollbackOnFailure(),
                userId);
        CloudConfiguration cloudConfiguration = generateCloudConfiguration(vnfDetails.getLcpCloudRegionId(), vnfDetails.getTenantId());
        VnfInstantiationRequestDetails.Platform platform = new VnfInstantiationRequestDetails.Platform(vnfDetails.getPlatformName());
        VnfInstantiationRequestDetails.LineOfBusiness lineOfBusiness = new VnfInstantiationRequestDetails.LineOfBusiness(vnfDetails.getLineOfBusiness());
        VnfInstantiationRequestDetails.RequestParameters requestParameters = new VnfInstantiationRequestDetails.RequestParameters(generateUserParamList());
        VnfInstantiationRequestDetails.RelatedInstance serviceInstance = new VnfInstantiationRequestDetails.RelatedInstance(serviceModelInfo, serviceInstanceId);
        List<VnfInstantiationRequestDetails.RelatedInstance> relatedInstanceList = new ArrayList<>();
        relatedInstanceList.add(serviceInstance);
        return new RequestDetailsWrapper<>(new VnfInstantiationRequestDetails(vnfDetails.getModelInfo(), cloudConfiguration, requestInfo, platform, lineOfBusiness, relatedInstanceList, requestParameters));
    }

    @Override
    public RequestDetailsWrapper<VfModuleInstantiationRequestDetails> generateVfModuleInstantiationRequest(VfModule vfModuleDetails, ModelInfo serviceModelInfo, String serviceInstanceId, ModelInfo vnfModelInfo, String vnfInstanceId, String vgInstanceId, String userId) {

        VfModuleInstantiationRequestDetails.RequestInfo requestInfo = new VfModuleInstantiationRequestDetails.RequestInfo(
                getUniqueNameIfNeeded(vfModuleDetails.getInstanceName(), ResourceType.VF_MODULE),
                null,
                VID_SOURCE,
                vfModuleDetails.isRollbackOnFailure(),
                userId);

        //cloud configuration
        CloudConfiguration cloudConfiguration = generateCloudConfiguration(vfModuleDetails.getLcpCloudRegionId(), vfModuleDetails.getTenantId());

        //request parameters
        List<UserParamMap<String, String>> userParams = aggregateAllInstanceParams(extractActualInstanceParams(vfModuleDetails.getInstanceParams()), vfModuleDetails.getSupplementaryParams());
        RequestParametersVfModule requestParameters = new RequestParametersVfModule(userParams, vfModuleDetails.isUsePreload());

        //related instance list
        VfModuleInstantiationRequestDetails.RelatedInstance serviceInstance = new VfModuleInstantiationRequestDetails.RelatedInstance(serviceModelInfo, serviceInstanceId);
        VfModuleInstantiationRequestDetails.RelatedInstance vnfInstance = new VfModuleInstantiationRequestDetails.RelatedInstance(vnfModelInfo, vnfInstanceId);
        List<VfModuleInstantiationRequestDetails.RelatedInstance> relatedInstanceList = new ArrayList<>();
        relatedInstanceList.add(serviceInstance);
        relatedInstanceList.add(vnfInstance);
        if (vgInstanceId != null) {
            ModelInfo volumeGroupModel = new ModelInfo();
            volumeGroupModel.setModelType("volumeGroup");
            VfModuleInstantiationRequestDetails.RelatedInstance volumeGroupInstance = new VfModuleInstantiationRequestDetails.RelatedInstance(volumeGroupModel, vgInstanceId, vfModuleDetails.getVolumeGroupInstanceName());
            relatedInstanceList.add(volumeGroupInstance);
        }

        return new RequestDetailsWrapper<>(new VfModuleInstantiationRequestDetails(vfModuleDetails.getModelInfo(), cloudConfiguration, requestInfo, relatedInstanceList, requestParameters));
    }

    protected CloudConfiguration generateCloudConfiguration(String lcpCloudRegionId, String tenantId) {
        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.setLcpCloudRegionId(lcpCloudRegionId);
        cloudConfiguration.setTenantId(tenantId);
        cloudOwnerService.enrichCloudConfigurationWithCloudOwner(cloudConfiguration, lcpCloudRegionId);
        return cloudConfiguration;
    }

    @Override
    public RequestDetailsWrapper<VolumeGroupRequestDetails> generateVolumeGroupInstantiationRequest(VfModule vfModuleDetails, ModelInfo serviceModelInfo, String serviceInstanceId, ModelInfo vnfModelInfo, String vnfInstanceId, String userId) {
        VolumeGroupRequestDetails.RequestInfo requestInfo = new VolumeGroupRequestDetails.RequestInfo(
                getUniqueNameIfNeeded(vfModuleDetails.getVolumeGroupInstanceName(), ResourceType.VOLUME_GROUP),
                null,
                VID_SOURCE,
                vfModuleDetails.isRollbackOnFailure(),
                userId);
        CloudConfiguration cloudConfiguration = generateCloudConfiguration(vfModuleDetails.getLcpCloudRegionId(), vfModuleDetails.getTenantId());

        List<UserParamMap<String, String>> userParams = aggregateAllInstanceParams(extractActualInstanceParams(vfModuleDetails.getInstanceParams()), vfModuleDetails.getSupplementaryParams());
        RequestParametersVfModule requestParameters = new RequestParametersVfModule(userParams, vfModuleDetails.isUsePreload());

        VfModuleInstantiationRequestDetails.RelatedInstance serviceInstance = new VfModuleInstantiationRequestDetails.RelatedInstance(serviceModelInfo, serviceInstanceId);
        VfModuleInstantiationRequestDetails.RelatedInstance vnfInstance = new VfModuleInstantiationRequestDetails.RelatedInstance(vnfModelInfo, vnfInstanceId);
        List<VfModuleInstantiationRequestDetails.RelatedInstance> relatedInstancs = ImmutableList.of(serviceInstance, vnfInstance);

        ModelInfo modelInfo = vfModuleDetails.getModelInfo();
        modelInfo.setModelType("volumeGroup");
        return new RequestDetailsWrapper<>(new VolumeGroupRequestDetails(modelInfo, cloudConfiguration, requestInfo, relatedInstancs, requestParameters));
    }

    protected List<UserParamMap<String, String>> aggregateAllInstanceParams(Map<String, String> instanceParams, Map<String, String> supplementaryParams) {
        Map<String, String> instanceParamsFinal = defaultIfNull(instanceParams, new HashMap<>());
        Map<String, String> supplementaryParamsFinal = defaultIfNull(supplementaryParams, new HashMap<>());

        if (!(instanceParamsFinal.isEmpty() && supplementaryParamsFinal.isEmpty())) {
            //remove duplicate keys from instanceParams if exist in supplementaryParams
            instanceParamsFinal = instanceParams.entrySet().stream().filter(m->
                    !supplementaryParamsFinal.containsKey(m.getKey())
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            //aggregate the 2 collections and format them as UserParamMap
            UserParamMap<String, String> aggregatedParams = new UserParamMap<>();
            aggregatedParams.putAll(instanceParamsFinal);
            aggregatedParams.putAll(supplementaryParamsFinal);

            return ImmutableList.of(aggregatedParams);
        }

        return Collections.emptyList();
    }

    @Override
    public RequestDetailsWrapper<NetworkInstantiationRequestDetails> generateNetworkInstantiationRequest(Network networkDetails, ModelInfo serviceModelInfo, String serviceInstanceId, String userId) {

        NetworkInstantiationRequestDetails.RequestInfo requestInfo = new NetworkInstantiationRequestDetails.RequestInfo(
                getUniqueNameIfNeeded(networkDetails.getInstanceName(), ResourceType.L3_NETWORK),
                networkDetails.getProductFamilyId(),
                VID_SOURCE,
                networkDetails.isRollbackOnFailure(),
                userId);
        CloudConfiguration cloudConfiguration = generateCloudConfiguration(networkDetails.getLcpCloudRegionId(), networkDetails.getTenantId());
        NetworkInstantiationRequestDetails.Platform platform = new NetworkInstantiationRequestDetails.Platform(networkDetails.getPlatformName());
        NetworkInstantiationRequestDetails.LineOfBusiness lineOfBusiness = new NetworkInstantiationRequestDetails.LineOfBusiness(networkDetails.getLineOfBusiness());
        NetworkInstantiationRequestDetails.RequestParameters requestParameters = new NetworkInstantiationRequestDetails.RequestParameters(generateUserParamList());
        NetworkInstantiationRequestDetails.RelatedInstance serviceInstance = new NetworkInstantiationRequestDetails.RelatedInstance(serviceModelInfo, serviceInstanceId);
        List<NetworkInstantiationRequestDetails.RelatedInstance> relatedInstanceList = new ArrayList<>();
        relatedInstanceList.add(serviceInstance);
        return new RequestDetailsWrapper<>(new NetworkInstantiationRequestDetails(networkDetails.getModelInfo(), cloudConfiguration, requestInfo, platform, lineOfBusiness, relatedInstanceList, requestParameters));
    }

    @Override
    public RequestDetailsWrapper<InstanceGroupInstantiationRequestDetails> generateInstanceGroupInstantiationRequest(InstanceGroup instanceGroupDetails, ModelInfo serviceModelInfo, String serviceInstanceId, String userId) {
        InstanceGroupInstantiationRequestDetails.RequestInfo requestInfo = new InstanceGroupInstantiationRequestDetails.RequestInfo(
                getUniqueNameIfNeeded(instanceGroupDetails.getInstanceName(), ResourceType.INSTANCE_GROUP),
                null,
                "VID",
                instanceGroupDetails.isRollbackOnFailure(),
                userId);
        InstanceGroupInstantiationRequestDetails.RequestParameters requestParameters = new InstanceGroupInstantiationRequestDetails.RequestParameters(generateUserParamList());
        InstanceGroupInstantiationRequestDetails.RelatedInstance serviceInstance = new InstanceGroupInstantiationRequestDetails.RelatedInstance(serviceModelInfo, serviceInstanceId);
        List<InstanceGroupInstantiationRequestDetails.RelatedInstance> relatedInstanceList = ImmutableList.of(serviceInstance);
        return new RequestDetailsWrapper<>(new InstanceGroupInstantiationRequestDetails(instanceGroupDetails.getModelInfo(), requestInfo, relatedInstanceList, requestParameters));
    }

    // TODO
    private List<UserParamNameAndValue> generateUserParamList() {
        return Collections.emptyList();
    }

    protected List<ServiceInstantiationService> generateServiceInstantiationServicesList(ServiceInstantiation payload, String serviceInstanceName, ServiceInstantiationVnfList vnfList) {
        List<ServiceInstantiationService> serviceInstantiationServiceList = new LinkedList<>();
        List<Map<String, String>> unFilteredInstanceParams = defaultIfNull(payload.getInstanceParams(), Collections.emptyList());
        List<Map<String, String>> filteredInstanceParams = removeUnNeededParams(unFilteredInstanceParams);
        ServiceInstantiationService serviceInstantiationService = new ServiceInstantiationService(
                payload.getModelInfo(),
                serviceInstanceName,
                filteredInstanceParams,
                vnfList
        );
        serviceInstantiationServiceList.add(serviceInstantiationService);
        return serviceInstantiationServiceList;
    }

    private ServiceInstantiationRequestDetails generateServiceInstantiationRequestDetails(ServiceInstantiation payload, RequestParameters requestParameters, String serviceInstanceName, String userId) {
        ServiceInstantiationRequestDetails.RequestInfo requestInfo = new ServiceInstantiationRequestDetails.RequestInfo(serviceInstanceName,
                payload.getProductFamilyId(),
                VID_SOURCE,
                payload.isRollbackOnFailure(),
                userId);
        ServiceInstantiationOwningEntity owningEntity = new ServiceInstantiationOwningEntity(payload.getOwningEntityId(), payload.getOwningEntityName());
        SubscriberInfo subscriberInfo = generateSubscriberInfo(payload);
        Project project = payload.getProjectName() != null ?  new Project(payload.getProjectName()) : null;
        return new ServiceInstantiationRequestDetails(payload.getModelInfo(), owningEntity, subscriberInfo, project, requestInfo, requestParameters);
    }


    protected SubscriberInfo generateSubscriberInfo(ServiceInstantiation payload) {
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setGlobalSubscriberId(payload.getGlobalSubscriberId());
        return subscriberInfo;
    }

    protected String generateServiceName(UUID jobId, ServiceInstantiation payload, String optimisticUniqueServiceInstanceName) {
        String serviceInstanceName = null;
        if(StringUtils.isNotEmpty(optimisticUniqueServiceInstanceName)) {
            serviceInstanceName = peekServiceName(jobId, payload, optimisticUniqueServiceInstanceName);
        }
        return serviceInstanceName;
    }

    protected String peekServiceName(UUID jobId, ServiceInstantiation payload, String optimisticUniqueServiceInstanceName) {
        String serviceInstanceName;
        // unique name already exist in service info. If it's free in AAI we use it
        if (isNameFreeInAai(optimisticUniqueServiceInstanceName, ResourceType.SERVICE_INSTANCE)) {
            serviceInstanceName =  optimisticUniqueServiceInstanceName;
        }
        //otherwise we used the original service instance name (from payload) to get a new unique name from DB and AAI
        else {
            serviceInstanceName = getUniqueName(payload.getInstanceName(), ResourceType.SERVICE_INSTANCE);
        }

        //update serviceInfo with new name if needed
        try {
            updateServiceInfo(jobId, x -> x.setServiceInstanceName(serviceInstanceName));
        } catch (Exception e) {
            logger.error("Failed updating service name {} in serviceInfo", serviceInstanceName, e);
        }

        return serviceInstanceName;
    }

    @Override
    public List<Map<String,String>> buildVnfInstanceParams(List<Map<String, String>> currentVnfInstanceParams, List<VfModuleMacro> vfModules){
        List<Map<String, String>> filteredVnfInstanceParams = removeUnNeededParams(currentVnfInstanceParams);

        if (!featureManager.isActive(Features.FLAG_SHIFT_VFMODULE_PARAMS_TO_VNF)) {
            return filteredVnfInstanceParams;
        }

        Map<String,String> vnfInstanceParams = extractActualInstanceParams(filteredVnfInstanceParams);
        vfModules.stream()
                .map(x->extractActualInstanceParams(x.getInstanceParams()))
                .forEach(vnfInstanceParams::putAll);
        return vnfInstanceParams.isEmpty() ? Collections.emptyList() : ImmutableList.of(vnfInstanceParams);
    }

    //Make sure we always get a one Map from InstanceParams
    private Map<String, String> extractActualInstanceParams(List<Map<String, String>> originalInstanceParams) {
        if (originalInstanceParams==null || originalInstanceParams.isEmpty() || originalInstanceParams.get(0)==null) {
            return new HashMap<>();
        }
        return originalInstanceParams.get(0);
    }

    private List<Map<String, String>> removeUnNeededParams(List<Map<String, String>> instanceParams) {
        List<String> keysToRemove = new ArrayList<>();
        if (instanceParams == null || instanceParams.isEmpty()) {
            return Collections.emptyList();
        }

        for (String key : instanceParams.get(0).keySet()) {
            for (String paramToIgnore : PARAMS_TO_IGNORE)
                if ((key.equalsIgnoreCase(paramToIgnore))) {
                    keysToRemove.add(key);
                }
        }

        Map<String, String> result = instanceParams.get(0).entrySet().stream()
                .filter(entry->!keysToRemove.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return result.isEmpty() ?  Collections.emptyList() : Collections.singletonList(result);
    }

    private ServiceInstantiationVnfList createServiceInstantiationVnfList(ServiceInstantiation payload) {
        CloudConfiguration cloudConfiguration = generateCloudConfiguration(payload.getLcpCloudRegionId(), payload.getTenantId());

        Map<String, Vnf> vnfs = payload.getVnfs();
        List<ServiceInstantiationVnf> vnfList = new ArrayList<>();
        for (Vnf vnf : vnfs.values()) {
            Map<String, Map<String, VfModule>> vfModules = vnf.getVfModules();
            List<VfModuleMacro> convertedUnFilteredVfModules = convertVfModuleMapToList(vfModules);
            List<VfModuleMacro> filteredVfModules = filterInstanceParamsFromVfModuleAndUniqueNames(convertedUnFilteredVfModules);
            ServiceInstantiationVnf serviceInstantiationVnf = new ServiceInstantiationVnf(
                    vnf.getModelInfo(),
                    cloudConfiguration,
                    vnf.getPlatformName(),
                    vnf.getLineOfBusiness(),
                    payload.getProductFamilyId(),
                    buildVnfInstanceParams(vnf.getInstanceParams(), filteredVfModules),
                    filteredVfModules,
                    getUniqueNameIfNeeded(vnf.getInstanceName(), ResourceType.GENERIC_VNF)
            );
            vnfList.add(serviceInstantiationVnf);
        }

        return new ServiceInstantiationVnfList(vnfList);
    }

    private List<VfModuleMacro> convertVfModuleMapToList(Map<String, Map<String, VfModule>> vfModules) {
        ObjectMapper mapper = new ObjectMapper();
        return vfModules.values().stream().flatMap(vfModule ->
                vfModule.values().stream().map(item -> {
                    List<UserParamMap<String, String>> aggregatedParams = aggregateAllInstanceParams(extractActualInstanceParams(item.getInstanceParams()), item.getSupplementaryParams());
                    List<Map<String, String>> aggregatedParamsConverted = mapper.convertValue(aggregatedParams, new TypeReference<List<Map>>(){});

                    return new VfModuleMacro(
                            item.getModelInfo(),
                            item.getInstanceName(),
                            item.getVolumeGroupInstanceName(),
                            aggregatedParamsConverted);
                    }
                )
        ).collect(Collectors.toList());
    }

    private List<VfModuleMacro> filterInstanceParamsFromVfModuleAndUniqueNames(List<VfModuleMacro> unFilteredVfModules) {
        return unFilteredVfModules.stream().map(vfModule ->
                new VfModuleMacro(
                        vfModule.getModelInfo(),
                        getUniqueNameIfNeeded(vfModule.getInstanceName(), ResourceType.VF_MODULE),
                        getUniqueNameIfNeeded(vfModule.getVolumeGroupInstanceName(), ResourceType.VOLUME_GROUP),
                        removeUnNeededParams(vfModule.getInstanceParams())))
                .collect(Collectors.toList());
    }

    private String getUniqueNameIfNeeded(String name, ResourceType resourceType) {
        return StringUtils.isNotEmpty(name) ? getUniqueName(name, resourceType) : null;
    }

    @Override
    public String getServiceInstantiationPath(ServiceInstantiation serviceInstantiationRequest) {
        //in case pause flag is true - use assign , else - use create.
        return MsoBusinessLogicImpl.validateEndpointPath(
                serviceInstantiationRequest.isPause() ?
                        MsoProperties.MSO_REST_API_SERVICE_INSTANCE_ASSIGN : MsoProperties.MSO_REST_API_SERVICE_INSTANCE_CREATE
        );
    }

    @Override
    public String getServiceDeletionPath(String serviceInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath( MsoProperties.MSO_DELETE_OR_UNASSIGN_REST_API_SVC_INSTANCE)  + "/" + serviceInstanceId;
    }

    @Override
    public String getVnfInstantiationPath(String serviceInstanceId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE).
                replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
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
    public String getInstanceGroupDeletePath(String instanceGroupId) {
        return MsoBusinessLogicImpl.validateEndpointPath(MsoProperties.MSO_REST_API_INSTANCE_GROUP)
                + '/' + instanceGroupId;
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
        auditVidStatus(jobUuid,jobStatus);
        return updateServiceInfo(jobUuid, x -> setServiceInfoStatus(x, jobStatus));
    }

    private void setServiceInfoStatus(ServiceInfo serviceInfo, JobStatus jobStatus) {
        serviceInfo.setJobStatus(jobStatus);
        serviceInfo.setStatusModifiedDate(new Date());
    }

    public ServiceInfo getServiceInfoByJobId(UUID jobUUID) {
        List<ServiceInfo> serviceInfoList = dataAccessService.getList(ServiceInfo.class, String.format(" where jobId = '%s' ", jobUUID), null, null);
        if (serviceInfoList.size() != 1) {
            throw new GenericUncheckedException("Failed to retrieve job with uuid " + jobUUID + " from ServiceInfo table. Instances found: " + serviceInfoList.size());
        }
        return serviceInfoList.get(0);
    }

    public List<JobAuditStatus> getAuditStatuses(UUID jobUUID, JobAuditStatus.SourceStatus source) {
        return dataAccessService.getList(
            JobAuditStatus.class,
            String.format(" where SOURCE = '%s' and JOB_ID = '%s'",source, jobUUID),
            " CREATED_DATE ", null);
    }

    private JobAuditStatus getLatestAuditStatus(UUID jobUUID, JobAuditStatus.SourceStatus source){
        List<JobAuditStatus> list = getAuditStatuses(jobUUID,source);
        return !list.isEmpty() ? list.get(list.size()-1) : null;
    }

    @Override
    public void auditVidStatus(UUID jobUUID, JobStatus jobStatus){
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
        if (latestStatus == null || !latestStatus.equals(jobAuditStatus))
            dataAccessService.saveDomainObject(jobAuditStatus, DaoUtils.getPropsMap());

    }

    public Job.JobStatus calcStatus(AsyncRequestStatus asyncRequestStatus) {
        String msoRequestState = asyncRequestStatus.request.requestStatus.getRequestState().toLowerCase().replaceAll("[^a-z]+", "");
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
            String message = String.format( "jobId %s: Service status does not allow hide service, status = %s",
                    serviceInfo.getJobId(),
                    serviceInfo.getJobStatus());
            logger.error(EELFLoggerDelegate.errorLogger, message);
            throw new OperationNotAllowedException(message);
        }
        serviceInfo.setHidden(true);
        dataAccessService.saveDomainObject(serviceInfo, DaoUtils.getPropsMap());
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

        throw new MaxRetriesException("find unused name for "+name, getMaxRetriesGettingFreeNameFromAai());
    }

    protected String getUniqueNameFromDbOnly(String name) {
        int counter = getCounterForName(name);
        return formatNameAndCounter(name, counter);
    }

    //the method is protected so we can call it in the UT
    protected String formatNameAndCounter(String name, int counter) {
        return counter==0 ? name : name + "_" + String.format("%03d", counter);
    }

    private boolean isNameFreeInAai(String name, ResourceType resourceType) throws ExceptionWithRequestInfo {
        return !aaiClient.isNodeTypeExistsByName(name, resourceType);
    }

}
