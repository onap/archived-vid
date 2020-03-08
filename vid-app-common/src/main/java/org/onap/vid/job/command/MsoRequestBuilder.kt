package org.onap.vid.job.command

import com.google.common.collect.ImmutableList
import org.apache.commons.lang3.ObjectUtils.defaultIfNull
import org.apache.commons.lang3.StringUtils
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.aai.AaiClientInterface
import org.onap.vid.aai.ExceptionWithRequestInfo
import org.onap.vid.aai.model.ResourceType
import org.onap.vid.changeManagement.RequestDetailsWrapper
import org.onap.vid.model.serviceInstantiation.*
import org.onap.vid.mso.model.*
import org.onap.vid.mso.model.BaseResourceInstantiationRequestDetails.*
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.UserParamNameAndValue
import org.onap.vid.mso.rest.SubscriberInfo
import org.onap.vid.properties.Features
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.services.CloudOwnerService
import org.onap.vid.services.UserParamsContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.togglz.core.manager.FeatureManager
import java.util.*
import java.util.Collections.emptyList
import java.util.stream.Collectors

@Service
class MsoRequestBuilder
@Autowired constructor(private val asyncInstantiationBL: AsyncInstantiationBusinessLogic,
                       private val cloudOwnerService: CloudOwnerService,
                       private val aaiClient: AaiClientInterface,
                       private val featureManager: FeatureManager) {

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(MsoRequestBuilder::class.java)
        private const val VID_SOURCE = "VID"
        private const val DISABLED_HOMING_VALUE = "none"
    }

    fun generateALaCarteServiceInstantiationRequest(payload: ServiceInstantiation, optimisticUniqueServiceInstanceName: String, userId: String): RequestDetailsWrapper<ServiceInstantiationRequestDetails> {
        val userParams = generateUserParamList()

        val requestParameters = ServiceInstantiationRequestDetails.RequestParameters(payload.subscriptionServiceType, true, userParams, payload.testApi)

        val requestDetails = generateServiceInstantiationRequestDetails(payload, requestParameters, optimisticUniqueServiceInstanceName, userId)

        return RequestDetailsWrapper(requestDetails)
    }

    fun generateServiceDeletionRequest(payload: ServiceInstantiation, userId: String): RequestDetailsWrapper<ServiceDeletionRequestDetails> {

        val requestParameters = ServiceDeletionRequestDetails.RequestParameters(payload.isALaCarte, payload.testApi)

        val requestInfo = ServiceDeletionRequestDetails.RequestInfo(
                VID_SOURCE,
                userId)

        val requestDetails = ServiceDeletionRequestDetails(payload.modelInfo, requestInfo, requestParameters)

        return RequestDetailsWrapper(requestDetails)
    }

    fun generateMacroServiceInstantiationRequest(jobId: UUID?, payload: ServiceInstantiation, optimisticUniqueServiceInstanceName: String, userId: String): RequestDetailsWrapper<ServiceInstantiationRequestDetails> {
        val serviceInstanceName = generateServiceName(jobId, payload, optimisticUniqueServiceInstanceName)

        val serviceInstantiationServiceList = generateMacroServiceInstantiationRequestParams(payload, serviceInstanceName, jobId)


        val requestParameters = ServiceInstantiationRequestDetails.RequestParameters(payload.subscriptionServiceType, false, serviceInstantiationServiceList)

        val requestDetails = generateServiceInstantiationRequestDetails(payload, requestParameters, serviceInstanceName, userId)

        return RequestDetailsWrapper(requestDetails)
    }

    fun generateNetworkInstantiationRequest(networkDetails: Network, serviceModelInfo: ModelInfo, serviceInstanceId: String, userId: String, testApi: String?): RequestDetailsWrapper<NetworkInstantiationRequestDetails> {
        val requestInfo = generateRequestInfo(networkDetails.instanceName, ResourceType.L3_NETWORK, networkDetails.isRollbackOnFailure, networkDetails.productFamilyId, userId)
        val cloudConfiguration = generateCloudConfiguration(networkDetails.lcpCloudRegionId, networkDetails.tenantId)
        val platform = Platform(networkDetails.platformName)
        val lineOfBusiness = LineOfBusiness.of(networkDetails.lineOfBusiness)
        val requestParameters = BaseResourceInstantiationRequestDetails.RequestParameters(generateUserParamList(), testApi)
        val relatedInstanceList = generateRelatedInstances(mapOf(serviceInstanceId to serviceModelInfo))
        return RequestDetailsWrapper(NetworkInstantiationRequestDetails(networkDetails.modelInfo, cloudConfiguration, requestInfo, platform, lineOfBusiness, relatedInstanceList, requestParameters))
    }

    fun generateVnfInstantiationRequest(vnfDetails: Vnf, serviceModelInfo: ModelInfo, serviceInstanceId: String, userId: String, testApi: String?): RequestDetailsWrapper<VnfInstantiationRequestDetails> {
        val requestInfo = generateRequestInfo(vnfDetails.instanceName, ResourceType.GENERIC_VNF, vnfDetails.isRollbackOnFailure, vnfDetails.productFamilyId, userId)
        val cloudConfiguration = generateCloudConfiguration(vnfDetails.lcpCloudRegionId, vnfDetails.tenantId)
        val platform = Platform(vnfDetails.platformName)
        val lineOfBusiness = LineOfBusiness.of(vnfDetails.lineOfBusiness)
        val requestParameters = BaseResourceInstantiationRequestDetails.RequestParameters(generateUserParamList(), testApi)
        val relatedInstanceList = generateRelatedInstances(mapOf(serviceInstanceId to serviceModelInfo))
        return RequestDetailsWrapper(VnfInstantiationRequestDetails(vnfDetails.modelInfo, cloudConfiguration, requestInfo, platform, lineOfBusiness, relatedInstanceList, requestParameters))
    }

    fun generateDeleteVnfRequest(vnfDetails: Vnf, userId: String): RequestDetailsWrapper<VnfInstantiationRequestDetails> {
        val requestInfo = generateRequestInfo(null, null, null, null, userId)
        val cloudConfiguration = generateCloudConfiguration(vnfDetails.lcpCloudRegionId, vnfDetails.tenantId)
        return RequestDetailsWrapper(VnfInstantiationRequestDetails(vnfDetails.modelInfo, cloudConfiguration, requestInfo, null, null, null, null))
    }

    protected fun generateVfModuleRequestWithRequestParams(
            vfModuleDetails: VfModule, serviceModelInfo: ModelInfo,
            serviceInstanceId: String, vnfModelInfo: ModelInfo, vnfInstanceId: String, vgInstanceId: String?, userId: String,
            requestParameters: (userParams: List<UserParamTypes>) -> RequestParametersVfModuleOrVolumeGroup
    ): RequestDetailsWrapper<VfModuleOrVolumeGroupRequestDetails> {
        val requestInfo = generateRequestInfo(vfModuleDetails.instanceName, ResourceType.VF_MODULE, vfModuleDetails.isRollbackOnFailure, null, userId)

        //cloud configuration
        val cloudConfiguration = generateCloudConfiguration(vfModuleDetails.lcpCloudRegionId, vfModuleDetails.tenantId)

        val userParams = UserParamsContainer(extractActualInstanceParams(vfModuleDetails.instanceParams), vfModuleDetails.supplementaryParams)

        //related instance list
        val relatedInstanceList = generateRelatedInstances(mapOf(serviceInstanceId to serviceModelInfo, vnfInstanceId to vnfModelInfo))
        if (StringUtils.isNotEmpty(vgInstanceId)) {
            val volumeGroupModel = ModelInfo()
            volumeGroupModel.modelType = "volumeGroup"
            relatedInstanceList.add(RelatedInstance(volumeGroupModel, vgInstanceId, vfModuleDetails.volumeGroupInstanceName))
        }

        return RequestDetailsWrapper(VfModuleOrVolumeGroupRequestDetails(vfModuleDetails.modelInfo, cloudConfiguration, requestInfo, relatedInstanceList, requestParameters(userParams.toALaCarte())))
    }

    fun generateVfModuleInstantiationRequest(
            vfModuleDetails: VfModule, serviceModelInfo: ModelInfo,
            serviceInstanceId: String, vnfModelInfo: ModelInfo, vnfInstanceId: String,
            vgInstanceId: String?, userId: String, testApi: String?
    ): RequestDetailsWrapper<VfModuleOrVolumeGroupRequestDetails> {
        val requestParameters = { userParams: List<UserParamTypes> ->
            RequestParametersVfModuleOrVolumeGroupInstantiation(userParams, vfModuleDetails.isUsePreload, testApi)
        }

        return generateVfModuleRequestWithRequestParams(vfModuleDetails, serviceModelInfo, serviceInstanceId,
                vnfModelInfo, vnfInstanceId, vgInstanceId, userId, requestParameters)
    }

    fun generateVfModuleReplaceRequest(
            vfModuleDetails: VfModule, serviceModelInfo: ModelInfo,
            serviceInstanceId: String, vnfModelInfo: ModelInfo, vnfInstanceId: String,
            vgInstanceId: String?, userId: String, testApi: String?
    ): RequestDetailsWrapper<VfModuleOrVolumeGroupRequestDetails> {
        val requestParameters = { userParams: List<UserParamTypes> ->
            RequestParametersVfModuleUpgrade(userParams, vfModuleDetails.isUsePreload, testApi,
                    vfModuleDetails.isRetainAssignments, nullSafeNegate(vfModuleDetails.isRetainVolumeGroups))
        }

        return generateVfModuleRequestWithRequestParams(vfModuleDetails, serviceModelInfo, serviceInstanceId,
                vnfModelInfo, vnfInstanceId, vgInstanceId, userId, requestParameters)
    }

    private fun nullSafeNegate(booleanValue: Boolean?): Boolean? = booleanValue?.not()

    fun generateVolumeGroupInstantiationRequest(vfModuleDetails: VfModule, serviceModelInfo: ModelInfo, serviceInstanceId: String, vnfModelInfo: ModelInfo, vnfInstanceId: String, userId: String, testApi: String?): RequestDetailsWrapper<VolumeGroupRequestDetails> {
        val requestInfo = generateRequestInfo(vfModuleDetails.volumeGroupInstanceName, ResourceType.VOLUME_GROUP, vfModuleDetails.isRollbackOnFailure, null, userId)
        val cloudConfiguration = generateCloudConfiguration(vfModuleDetails.lcpCloudRegionId, vfModuleDetails.tenantId)
        val userParams = UserParamsContainer(extractActualInstanceParams(vfModuleDetails.instanceParams), vfModuleDetails.supplementaryParams)
        val requestParameters = RequestParametersVfModuleOrVolumeGroupInstantiation(userParams.toALaCarte(), vfModuleDetails.isUsePreload, testApi)
        val relatedInstances = generateRelatedInstances(mapOf(serviceInstanceId to serviceModelInfo, vnfInstanceId to vnfModelInfo))

        vfModuleDetails.modelInfo.modelType = "volumeGroup"
        return RequestDetailsWrapper(VolumeGroupRequestDetails(vfModuleDetails.modelInfo, cloudConfiguration, requestInfo, relatedInstances, requestParameters))
    }

    fun generateInstanceGroupInstantiationRequest(instanceGroupDetails: InstanceGroup, serviceModelInfo: ModelInfo, serviceInstanceId: String, userId: String, testApi: String?): RequestDetailsWrapper<InstanceGroupInstantiationRequestDetails> {
        val requestInfo = generateRequestInfo(instanceGroupDetails.instanceName, ResourceType.INSTANCE_GROUP, instanceGroupDetails.isRollbackOnFailure, null, userId)
        val requestParameters = BaseResourceInstantiationRequestDetails.RequestParameters(generateUserParamList(), testApi)
        val relatedInstanceList = generateRelatedInstances(mapOf(serviceInstanceId to serviceModelInfo))
        return RequestDetailsWrapper(InstanceGroupInstantiationRequestDetails(instanceGroupDetails.modelInfo, requestInfo, relatedInstanceList, requestParameters))
    }

    fun generateInstanceGroupMemberRequest(instanceGroupMemberId: String, userId: String): RequestDetailsWrapper<AddOrRemoveInstanceGroupMemberRequestDetails> {
        val requestInfo = generateRequestInfo(null, null, null, null, userId)
        val modelInfo = ModelInfo()
        modelInfo.modelType = "vnf"
        val relatedInstanceList = generateRelatedInstances(mapOf(instanceGroupMemberId to modelInfo))
        return RequestDetailsWrapper(AddOrRemoveInstanceGroupMemberRequestDetails(requestInfo, relatedInstanceList))
    }

    fun generateDeleteNetworkRequest(networkDetails: Network, userId: String): RequestDetailsWrapper<NetworkInstantiationRequestDetails> {
        val requestInfo = generateRequestInfo(null, null, null, null, userId)
        val cloudConfiguration = generateCloudConfiguration(networkDetails.lcpCloudRegionId, networkDetails.tenantId)
        return RequestDetailsWrapper(NetworkInstantiationRequestDetails(networkDetails.modelInfo, cloudConfiguration, requestInfo, null, null, null, null))
    }

    fun generateDeleteVfModuleRequest(vfModuleDetails: VfModule, userId: String): RequestDetailsWrapper<VfModuleOrVolumeGroupRequestDetails> {
        val requestInfo = generateRequestInfo(null, null, null, null, userId)
        val cloudConfiguration = generateCloudConfiguration(vfModuleDetails.lcpCloudRegionId, vfModuleDetails.tenantId)
        return RequestDetailsWrapper(VfModuleOrVolumeGroupRequestDetails(vfModuleDetails.modelInfo, cloudConfiguration, requestInfo, null, null))
    }

    private fun generateServiceName(jobId: UUID?, payload: ServiceInstantiation, optimisticUniqueServiceInstanceName: String): String? {
        var serviceInstanceName: String? = null
        if (StringUtils.isNotEmpty(optimisticUniqueServiceInstanceName)) {
            serviceInstanceName = peekServiceName(jobId, payload, optimisticUniqueServiceInstanceName)
        }
        return serviceInstanceName
    }

    private fun peekServiceName(jobId: UUID?, payload: ServiceInstantiation, optimisticUniqueServiceInstanceName: String): String {
        val serviceInstanceName: String
        // unique name already exist in service info. If it's free in AAI we use it
        if (isNameFreeInAai(optimisticUniqueServiceInstanceName, ResourceType.SERVICE_INSTANCE)) {
            serviceInstanceName = optimisticUniqueServiceInstanceName
        } else {
            serviceInstanceName = asyncInstantiationBL.getUniqueName(payload.instanceName, ResourceType.SERVICE_INSTANCE)
        }//otherwise we used the original service instance name (from payload) to get a new unique name from DB and AAI

        //update serviceInfo with new name if needed
        try {
            asyncInstantiationBL.updateServiceInfo(jobId) { x -> x.serviceInstanceName = serviceInstanceName }
        } catch (e: Exception) {
            LOGGER.error("Failed updating service name {} in serviceInfo", serviceInstanceName, e)
        }

        return serviceInstanceName
    }

    @Throws(ExceptionWithRequestInfo::class)
    private fun isNameFreeInAai(name: String, resourceType: ResourceType): Boolean {
        return !aaiClient.isNodeTypeExistsByName(name, resourceType)
    }

    private fun generateServiceInstantiationServicesList(payload: ServiceInstantiation, serviceInstanceName: String?, vnfList: ServiceInstantiationRequestDetails.ServiceInstantiationVnfList): List<ServiceInstantiationRequestDetails.ServiceInstantiationService> {
        val serviceInstantiationServiceList = LinkedList<ServiceInstantiationRequestDetails.ServiceInstantiationService>()
        val unFilteredInstanceParams = defaultIfNull<List<MutableMap<String, String>>>(payload.instanceParams, emptyList())
        val filteredInstanceParams = removeUnNeededParams(unFilteredInstanceParams)
        val serviceInstantiationService = ServiceInstantiationRequestDetails.ServiceInstantiationService(
                payload.modelInfo,
                serviceInstanceName,
                filteredInstanceParams,
                vnfList
        )
        serviceInstantiationServiceList.add(serviceInstantiationService)
        return serviceInstantiationServiceList
    }

    private fun removeUnNeededParams(instanceParams: List<MutableMap<String, String>>?): List<MutableMap<String, String>> {
        val keysToRemove = mutableListOf<String>()
        if (instanceParams.isNullOrEmpty()) {
            return emptyList()
        }

        for (key in instanceParams[0].keys) {
            for (paramToIgnore in AsyncInstantiationBusinessLogic.PARAMS_TO_IGNORE)
                if (key.equals(paramToIgnore, ignoreCase = true)) {
                    keysToRemove.add(key)
                }
        }

        val result: MutableMap<String, String> = instanceParams[0].entries.stream()
                .filter { entry -> !keysToRemove.contains(entry.key) }
                .collect(Collectors.toMap({ it.key }, { it.value }))

        return if (result.isEmpty()) emptyList() else listOf(result)
    }

    private fun createServiceInstantiationVnfList(jobId: UUID?, payload: ServiceInstantiation): ServiceInstantiationRequestDetails.ServiceInstantiationVnfList {
        val cloudConfiguration = generateCloudConfiguration(payload.lcpCloudRegionId, payload.tenantId)
        val isBulk = asyncInstantiationBL.isPartOfBulk(jobId)

        val vnfs = payload.vnfs
        val vnfList = mutableListOf<ServiceInstantiationRequestDetails.ServiceInstantiationVnf>()
        for (vnf in vnfs.values) {
            val vfModules = vnf.vfModules
            val convertedUnFilteredVfModules = convertVfModuleMapToList(vfModules)
            val filteredVfModules = filterInstanceParamsFromVfModuleAndUniqueNames(convertedUnFilteredVfModules, isBulk)
            val serviceInstantiationVnf = ServiceInstantiationRequestDetails.ServiceInstantiationVnf(
                    vnf.modelInfo,
                    cloudConfiguration,
                    vnf.platformName,
                    vnf.lineOfBusiness,
                    payload.productFamilyId,
                    buildVnfInstanceParams(vnf.instanceParams, filteredVfModules),
                    filteredVfModules,
                    getUniqueNameIfNeeded(vnf.instanceName, ResourceType.GENERIC_VNF, isBulk)
            )
            vnfList.add(serviceInstantiationVnf)
        }

        return ServiceInstantiationRequestDetails.ServiceInstantiationVnfList(vnfList)
    }

    private fun convertVfModuleMapToList(vfModules: Map<String, Map<String, VfModule>>): List<VfModuleMacro> {
        return vfModules.values.stream().flatMap { vfModule ->
            vfModule.values.stream().map { item ->
                val userParams = UserParamsContainer(extractActualInstanceParams(item.instanceParams), item.supplementaryParams)

                VfModuleMacro(
                        item.modelInfo,
                        item.instanceName,
                        item.volumeGroupInstanceName,
                        userParams.toMacroPost1806())
            }
        }.collect(Collectors.toList<VfModuleMacro>())
    }

    //Make sure we always get a one Map from InstanceParams
    private fun extractActualInstanceParams(originalInstanceParams: List<MutableMap<String, String>>?): MutableMap<String, String> {
        return if (originalInstanceParams.isNullOrEmpty() || originalInstanceParams[0].isNullOrEmpty()) {
            mutableMapOf()
        } else originalInstanceParams[0]
    }

    private fun filterInstanceParamsFromVfModuleAndUniqueNames(unFilteredVfModules: List<VfModuleMacro>, isBulk: Boolean): List<VfModuleMacro> {
        return unFilteredVfModules.stream().map { vfModule ->
            VfModuleMacro(
                    vfModule.modelInfo,
                    getUniqueNameIfNeeded(vfModule.instanceName, ResourceType.VF_MODULE, isBulk),
                    getUniqueNameIfNeeded(vfModule.volumeGroupInstanceName, ResourceType.VOLUME_GROUP, isBulk),
                    removeUnNeededParams(vfModule.instanceParams))
        }
                .collect(Collectors.toList<VfModuleMacro>())
    }

    fun buildVnfInstanceParams(currentVnfInstanceParams: List<MutableMap<String, String>>, vfModules: List<VfModuleMacro>): List<Map<String, String>> {
        val filteredVnfInstanceParams = removeUnNeededParams(currentVnfInstanceParams)

        val vnfInstanceParams = extractActualInstanceParams(filteredVnfInstanceParams)
        vfModules.stream()
                .map { x -> extractActualInstanceParams(x.instanceParams) }
                .forEach { vnfInstanceParams.putAll(it) }
        return if (vnfInstanceParams.isEmpty()) emptyList() else ImmutableList.of(vnfInstanceParams)
    }

    private fun generateServiceInstantiationRequestDetails(payload: ServiceInstantiation, requestParameters: ServiceInstantiationRequestDetails.RequestParameters, serviceInstanceName: String?, userId: String): ServiceInstantiationRequestDetails {
        val requestInfo = ServiceInstantiationRequestDetails.RequestInfo(serviceInstanceName,
                payload.productFamilyId,
                VID_SOURCE,
                payload.isRollbackOnFailure,
                userId)
        val owningEntity = ServiceInstantiationRequestDetails.ServiceInstantiationOwningEntity(payload.owningEntityId, payload.owningEntityName)
        val subscriberInfo = generateSubscriberInfo(payload)
        val project = if (payload.projectName != null) ServiceInstantiationRequestDetails.Project(payload.projectName) else null
        return ServiceInstantiationRequestDetails(payload.modelInfo, owningEntity, subscriberInfo, project, requestInfo, requestParameters)
    }

    private fun generateSubscriberInfo(payload: ServiceInstantiation): SubscriberInfo {
        val subscriberInfo = SubscriberInfo()
        subscriberInfo.globalSubscriberId = payload.globalSubscriberId
        return subscriberInfo
    }

    private fun generateCloudConfiguration(lcpCloudRegionId: String?, tenantId: String?): CloudConfiguration {
        val cloudConfiguration = CloudConfiguration(lcpCloudRegionId, tenantId)
        if (lcpCloudRegionId != null) {
            cloudOwnerService.enrichCloudConfigurationWithCloudOwner(cloudConfiguration, lcpCloudRegionId)
        }
        return cloudConfiguration
    }

    private fun generateRelatedInstances(relatedInstances: Map<String, ModelInfo>): MutableList<RelatedInstance> {
        return relatedInstances.entries.stream()
                .map { RelatedInstance(it.value, it.key) }
                .collect(Collectors.toList())
    }

    private fun generateRequestInfo(instanceName: String?, resourceType: ResourceType?, rollbackOnFailure: Boolean?, productFamilyId: String?, userId: String): BaseResourceInstantiationRequestDetails.RequestInfo {
        return BaseResourceInstantiationRequestDetails.RequestInfo(
                if (resourceType == null) null else getUniqueNameIfNeeded(instanceName, resourceType, false),
                productFamilyId,
                VID_SOURCE,
                rollbackOnFailure,
                userId)

    }

    private fun getUniqueNameIfNeeded(name: String?, resourceType: ResourceType, isBulk: Boolean): String? {
        return if (StringUtils.isNotEmpty(name)) {
            if (isBulk) asyncInstantiationBL.getUniqueName(name, resourceType) else name
        } else {
            null
        }
    }

    private fun generateUserParamList(): List<UserParamNameAndValue> {
        return emptyList()
    }

    fun generateMacroServicePre1806InstantiationRequest(payload: ServiceInstantiation, userId: String): RequestDetailsWrapper<ServiceInstantiationRequestDetails> {
        val requestInfo = ServiceInstantiationRequestDetails.RequestInfo(payload.instanceName, payload.productFamilyId, VID_SOURCE, payload.isRollbackOnFailure, userId)
        val userParams = UserParamsContainer(generateSingleMapFromInstanceParams(payload.instanceParams), emptyList())
        val requestParameters = ServiceInstantiationRequestDetails.RequestParameters(payload.subscriptionServiceType, false, userParams.toMacroPre1806())
        val subscriberInfo = generateSubscriberInfoPre1806(payload)
        val project = if (payload.projectName != null) ServiceInstantiationRequestDetails.Project(payload.projectName) else null
        val owningEntity = ServiceInstantiationRequestDetails.ServiceInstantiationOwningEntity(payload.owningEntityId, payload.owningEntityName)
        val cloudConfiguration = generateCloudConfiguration(payload.lcpCloudRegionId, payload.tenantId)
        val relatedInstanceList = generateRelatedInstanceListForVrfEntry(payload.vrfs)

        return RequestDetailsWrapper(ServiceInstantiationPre1806RequestDetails(
                payload.modelInfo,
                owningEntity,
                subscriberInfo,
                project,
                requestInfo,
                requestParameters,
                cloudConfiguration,
                relatedInstanceList))
    }

    private fun generateSingleMapFromInstanceParams(instanceParams: List<Map<String, String>>): Map<String, String> {
        return if (instanceParams.isNullOrEmpty()) emptyMap() else instanceParams[0]
    }

    private fun generateSubscriberInfoPre1806(payload: ServiceInstantiation): SubscriberInfo {
        val subscriberInfo = SubscriberInfo()
        subscriberInfo.globalSubscriberId = payload.globalSubscriberId
        subscriberInfo.subscriberName = payload.subscriberName
        return subscriberInfo
    }

    private fun generateRelatedInstanceListForVrfEntry(vrfEntries: MutableMap<String, VrfEntry>): List<RelatedInstance> {
        //fe send map of vrfs, with maps of networks and vpns, but actually we expect to only one vpn and one network
        return if (vrfEntries.isEmpty() || vrfEntries.values.first().vpns.isEmpty() || vrfEntries.values.first().networks.isEmpty()) emptyList()
        else {
            val vpn = vrfEntries.values.first().vpns.values.first()
            val network = vrfEntries.values.first().networks.values.first()
            listOf(vpn, network).map { RelatedInstance(it.modelInfo, it.instanceId, it.instanceName) }
        }
    }

    private fun generateMacroServiceInstantiationRequestParams(payload: ServiceInstantiation, serviceInstanceName: String?, jobId: UUID?): List<UserParamTypes> {
        val userParams = generateServiceInstantiationServicesList(payload, serviceInstanceName, createServiceInstantiationVnfList(jobId, payload))

        return userParams.plus(homingSolution())
    }

    private fun homingSolution(): List<UserParamTypes> {
        return if (featureManager.isActive(Features.FLAG_DISABLE_HOMING)) {
            listOf(ServiceInstantiationRequestDetails.HomingSolution(DISABLED_HOMING_VALUE))
        } else {
            listOf()
        }
    }
}
