package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.job.Job
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobCommand
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.model.*
import org.onap.vid.model.serviceInstantiation.VfModule
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.mso.model.ModelInfo
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.util.*

typealias ToscaVfm = org.onap.vid.model.VfModule

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class VfmoduleCommand @Autowired constructor(
        private val asyncInstantiationBL: AsyncInstantiationBusinessLogic,
        restMso: RestMsoImplementation,
        private val msoRequestBuilder: MsoRequestBuilder,
        msoResultHandlerService: MsoResultHandlerService,
        inProgressStatusService:InProgressStatusService,
        watchChildrenJobsBL: WatchChildrenJobsBL,
        jobsBrokerService: JobsBrokerService,
        jobAdapter: JobAdapter
) : ResourceCommand(restMso, inProgressStatusService, msoResultHandlerService,
        watchChildrenJobsBL, jobsBrokerService, jobAdapter), JobCommand {

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(VfmoduleCommand::class.java)
    }

    override fun createChildren(): Job.JobStatus {
        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val serviceInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.SERVICE_INSTANCE_ID)
        val serviceModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.SERVICE_MODEL_INFO)
        val vnfModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.VNF_MODEL_INFO)
        val vnfInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_INSTANCE_ID)
        val vgInstaceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VG_INSTANCE_ID)

         val instantiatePath = asyncInstantiationBL.getVfmoduleInstantiationPath(serviceInstanceId, vnfInstanceId)

        val requestDetailsWrapper = msoRequestBuilder.generateVfModuleInstantiationRequest(
                request as VfModule,
                serviceModelInfo, serviceInstanceId, vnfModelInfo, vnfInstanceId, vgInstaceId, userId, testApi)

        val actionDescription = "create vfmodule in $vnfInstanceId"

        return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)

    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val serviceInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.SERVICE_INSTANCE_ID)
        val vnfInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_INSTANCE_ID)

        val path = asyncInstantiationBL.getVfModuleDeletePath(serviceInstanceId, vnfInstanceId, getRequest().instanceId)
        val requestDetailsWrapper = msoRequestBuilder.generateDeleteVfModuleRequest(getRequest(), userId)
        return MsoRestCallPlan(HttpMethod.DELETE, path, Optional.of(requestDetailsWrapper), Optional.of(userId),
                "delete vfmodule ${getRequest().instanceId} from service instance $serviceInstanceId and vnf $vnfInstanceId")
    }

    override fun getRequest(): VfModule {
        return sharedData.request as VfModule
    }

    override fun isDescendantHasAction(phase: Action): Boolean {
        return false
    }

    private fun planReplaceMyselfRestCall3(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val serviceInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.SERVICE_INSTANCE_ID)
        val serviceModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.SERVICE_MODEL_INFO)
        val vnfModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.VNF_MODEL_INFO)
        val vnfInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_INSTANCE_ID)
        val replacePath = asyncInstantiationBL.getVfModuleReplacePath(serviceInstanceId, vnfInstanceId, getRequest().instanceId)

        amendModelInfoWithNewestModel(serviceModelInfo, vnfModelInfo, (request as VfModule).modelInfo)

        val requestDetailsWrapper = msoRequestBuilder.generateVfModuleInstantiationRequest(
                request as VfModule, serviceModelInfo, serviceInstanceId,vnfModelInfo, vnfInstanceId,null,userId, testApi)

        val actionDescription = "replace vfmodule ${request.instanceId}"

        return MsoRestCallPlan(HttpMethod.POST, replacePath, Optional.of(requestDetailsWrapper), Optional.of(userId), actionDescription)
    }

    private fun planReplaceMyselfRestCall(commandParentData: CommandParentData): MsoRestCallPlan {

        val newestModel = fetchNewestServiceModel()

        val serviceInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.SERVICE_INSTANCE_ID)
        val vnfInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_INSTANCE_ID)

        val (serviceModelInfo, vnfModelInfo, vfmModelInfo) = newestSelector(newestModel, commandParentData);

        val originalRequestWithNewestVfmModelInfo = getRequest().cloneWith(vfmModelInfo)

        val requestDetailsWrapper = msoRequestBuilder.generateVfModuleInstantiationRequest(
                originalRequestWithNewestVfmModelInfo, serviceModelInfo, serviceInstanceId,
                vnfModelInfo, vnfInstanceId, null, sharedData.userId, sharedData.testApi)


        val replacePath = asyncInstantiationBL.getVfModuleReplacePath(serviceInstanceId, vnfInstanceId, getRequest().instanceId)

        return MsoRestCallPlan(HttpMethod.POST, replacePath, Optional.of(requestDetailsWrapper), Optional.of(sharedData.userId),
                "replace vfmodule ${getRequest().instanceId}")
    }

    data class ModelsInfoTriplet(val serviceModelInfo: ModelInfo, val vnfModelInfo: ModelInfo, val vfmModelInfo: ModelInfo)

    private fun newestSelector(newestModel: ServiceModel, commandParentData: CommandParentData): ModelsInfoTriplet {
        val serviceModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.SERVICE_MODEL_INFO)
        val vfmModelInfo = getRequest().modelInfo
        val vnfModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.VNF_MODEL_INFO)

        val newestServiceModelInfo = newestServiceModelInfo(newestModel)
        val newestVfmModelInfo = newestVfmModelInfo(newestModel)
        val newestVnfModelInfo = newestVnfModelInfo(newestModel, commandParentData)

        return if (newestServiceModelInfo == null || newestVfmModelInfo == null || newestVnfModelInfo == null) {
            ModelsInfoTriplet(serviceModelInfo, vnfModelInfo, vfmModelInfo)
        } else {
            ModelsInfoTriplet(newestServiceModelInfo, newestVnfModelInfo, newestVfmModelInfo)
        }
    }

    private fun newestServiceModelInfo(newestModel: ServiceModel) = toModelInfo(newestModel.service)

    private fun newestVfmModelInfo(newestModel: ServiceModel): ModelInfo? {
        val vfmModelInfo = getRequest().modelInfo
        val newestVfm = selectVfm(newestModel, vfmModelInfo)
        return toModelInfo(newestVfm)
    }

    private fun newestVnfModelInfo(newestModel: ServiceModel, commandParentData: CommandParentData): ModelInfo? {
        val vnfModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.VNF_MODEL_INFO)
        val newestVnf = selectVnf(newestModel, vnfModelInfo)
        return toModelInfo(newestVnf)
    }

    private fun selectVfm(newestModel: ServiceModel, modelInfo: ModelInfo) = newestModel.vfModules[modelInfo.modelCustomizationId]

    private fun selectVnf(newestModel: ServiceModel, modelInfo: ModelInfo) = newestModel.vnfs[modelInfo.modelCustomizationId]

    private fun toModelInfo(toBeConverted: VNF?): ModelInfo? = toBeConverted?.let { toModelInfo(it, "vnf") }

    private fun toModelInfo(toBeConverted: ToscaVfm?): ModelInfo? = toBeConverted?.let { toModelInfo(it, "vfModule") }

    private fun toModelInfo(toBeConverted: MinimalNode, modelType: String): ModelInfo {
        val targetModelInfo = ModelInfo()

        targetModelInfo.modelType = modelType
        targetModelInfo.modelName = toBeConverted.name
        targetModelInfo.modelNameVersionId = null
        targetModelInfo.modelVersion = toBeConverted.version
        targetModelInfo.modelVersionId = toBeConverted.uuid
        targetModelInfo.modelInvariantId = toBeConverted.invariantUuid

        targetModelInfo.modelCustomizationId = when (toBeConverted) {
            is VNF -> toBeConverted.customizationUuid
            is ToscaVfm -> toBeConverted.customizationUuid
            else -> throw IllegalArgumentException()
        }

        targetModelInfo.modelCustomizationName = when (toBeConverted) {
            is VNF -> toBeConverted.modelCustomizationName
            is ToscaVfm -> toBeConverted.modelCustomizationName
            else -> throw IllegalArgumentException()
        }

        return targetModelInfo
    }

    private fun toModelInfo(toBeConverted: Service?): ModelInfo? {

        if (toBeConverted == null)
            return null

        val targetModelInfo = ModelInfo()

        targetModelInfo.modelVersionId = toBeConverted.uuid
        targetModelInfo.modelInvariantId = toBeConverted.invariantUuid
        targetModelInfo.modelVersion = toBeConverted.version
        //targetModelInfo.modelCustomizationId = toBeConverted.customizationUuid
        //targetModelInfo.modelCustomizationName = toBeConverted.modelCustomizationName
        targetModelInfo.modelType = "service"
        targetModelInfo.modelName = toBeConverted.name

        return targetModelInfo
    }

    private fun amendModelInfoWithNewestModel(serviceModelInfo: ModelInfo, vnfModelInfo: ModelInfo, vfmModelInfo: ModelInfo) {
        val newestModel = fetchNewestServiceModel()
        val newestService = newestModel.service

        val newestVfm = newestModel.vfModules[vfmModelInfo.modelCustomizationId]
        val newestVnf = newestModel.vnfs[vnfModelInfo.modelCustomizationId]

        if (!(newestService == null || newestVnf == null || newestVfm == null)) {

            serviceModelInfo.modelName = newestService.name
            serviceModelInfo.modelVersionId = newestService.uuid
            serviceModelInfo.modelVersion = newestService.version

            vnfModelInfo.modelName = newestVnf.name
            vnfModelInfo.modelVersionId = newestVnf.uuid
            vnfModelInfo.modelVersion = newestVnf.version
            vnfModelInfo.modelCustomizationId = newestVnf.customizationUuid
            vnfModelInfo.modelCustomizationName = newestVnf.modelCustomizationName

            vfmModelInfo.modelName = newestVfm.name
            vfmModelInfo.modelVersionId = newestVfm.uuid
            vfmModelInfo.modelVersion = newestVfm.version
            vfmModelInfo.modelCustomizationId = newestVfm.customizationUuid
            vfmModelInfo.modelCustomizationName = newestVfm.modelCustomizationName
        }
    }


    override fun replaceMyself(): Job.JobStatus {
        try {
            val replaceMyselfCommand = planReplaceMyselfRestCall(commandParentData)
            return executeAndHandleMsoInstanceRequest(replaceMyselfCommand)
        } catch (exception: Exception) {
            LOGGER.error("Failed to replace instanceId ${getRequest().instanceId} ", exception)
            return Job.JobStatus.FAILED
        }
    }

    override fun isNeedToReplaceMySelf(): Boolean {
        return getActionType() == Action.Upgrade
    }

    private fun fetchNewestServiceModel(): ServiceModel {
        val serviceModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.SERVICE_MODEL_INFO)
        var modelNewestUuid = commandUtils.getNewestModelUuid(serviceModelInfo.modelInvariantId);
        var serviceNewestModel = commandUtils.getServiceModel(modelNewestUuid);

        return serviceNewestModel;
    }
}
