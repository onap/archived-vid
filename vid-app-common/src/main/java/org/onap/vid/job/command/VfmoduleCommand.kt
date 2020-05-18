package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.exceptions.GenericUncheckedException
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
import org.togglz.core.manager.FeatureManager
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
        jobAdapter: JobAdapter,
        featureManager: FeatureManager
) : ResourceCommand(restMso, inProgressStatusService, msoResultHandlerService,
        watchChildrenJobsBL, jobsBrokerService, jobAdapter, featureManager), JobCommand {

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(VfmoduleCommand::class.java)
    }

    override fun createChildren(): Job.JobStatus {
        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val serviceInstanceId = serviceInstanceIdFromRequest()
        val serviceModelInfo = serviceModelInfoFromRequest()
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
        val serviceInstanceId = serviceInstanceIdFromRequest()
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

    private fun planReplaceMyselfRestCall(commandParentData: CommandParentData): MsoRestCallPlan {

        val newestModel = fetchNewestServiceModel()

        val serviceInstanceId = serviceInstanceIdFromRequest()
        val vnfInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_INSTANCE_ID)

        val (serviceModelInfo, vnfModelInfo, vfmModelInfo) = newestSelector(newestModel, commandParentData);

        val originalRequestWithNewestVfmModelInfo = getRequest().cloneWith(vfmModelInfo)

        val requestDetailsWrapper = msoRequestBuilder.generateVfModuleReplaceRequest(
                originalRequestWithNewestVfmModelInfo, serviceModelInfo, serviceInstanceId,
                vnfModelInfo, vnfInstanceId, null, sharedData.userId, sharedData.testApi)


        val replacePath = asyncInstantiationBL.getVfModuleReplacePath(serviceInstanceId, vnfInstanceId, getRequest().instanceId)

        return MsoRestCallPlan(HttpMethod.POST, replacePath, Optional.of(requestDetailsWrapper), Optional.of(sharedData.userId),
                "replace vfmodule ${getRequest().instanceId}")
    }

    data class ModelsInfoTriplet(val serviceModelInfo: ModelInfo, val vnfModelInfo: ModelInfo, val vfmModelInfo: ModelInfo)

    private fun newestSelector(newestModel: ServiceModel, commandParentData: CommandParentData): ModelsInfoTriplet {
        try {
            return ModelsInfoTriplet(
                    newestServiceModelInfo(newestModel),
                    newestVnfModelInfo(newestModel, commandParentData),
                    newestVfmModelInfo(newestModel)
            )
        } catch (e: Exception) {
            throw GenericUncheckedException("Cannot upgrade ${serviceModelInfoFromRequest()} to ${newestModel.service}", e)
        }
    }

    private fun newestServiceModelInfo(newestModel: ServiceModel) = toModelInfo(newestModel.service)

    private fun newestVfmModelInfo(newestModel: ServiceModel): ModelInfo {
        val vfmModelInfo = getRequest().modelInfo
        val matchingVfm = selectVfm(newestModel, vfmModelInfo)
        return toModelInfo(matchingVfm)
    }

    private fun newestVnfModelInfo(newestModel: ServiceModel, commandParentData: CommandParentData): ModelInfo {
        val vnfModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.VNF_MODEL_INFO)
        val matchingVnf = selectVnf(newestModel, vnfModelInfo)
        return toModelInfo(matchingVnf)
    }

    internal fun selectVfm(serviceModel: ServiceModel, modelInfo: ModelInfo): ToscaVfm =
            exactlyOne("vfModule for modelCustomizationName \"${modelInfo.modelCustomizationName}\"") {
                serviceModel.vfModules.values.single { it.modelCustomizationName == modelInfo.modelCustomizationName }
            }

    internal fun selectVnf(serviceModel: ServiceModel, modelInfo: ModelInfo): VNF =
            exactlyOne("VNF for modelCustomizationName \"${modelInfo.modelCustomizationName}\"") {
                serviceModel.vnfs.values.single { it.modelCustomizationName == modelInfo.modelCustomizationName }
            }

    private fun <T: Any> exactlyOne(predicateDescription: String, itemSupplier: () -> T): T {
        return try {
            itemSupplier.invoke()
        } catch (cause: Exception) {
            throw IllegalStateException("Cannot match ${predicateDescription}: ${cause.localizedMessage}", cause)
        }
    }

    private fun toModelInfo(toBeConverted: VNF): ModelInfo = toModelInfo(toBeConverted, "vnf")

    private fun toModelInfo(toBeConverted: ToscaVfm): ModelInfo = toModelInfo(toBeConverted, "vfModule")

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

    internal fun toModelInfo(toBeConverted: Service): ModelInfo {
        val targetModelInfo = ModelInfo()

        targetModelInfo.modelVersionId = toBeConverted.uuid
        targetModelInfo.modelInvariantId = toBeConverted.invariantUuid
        targetModelInfo.modelVersion = toBeConverted.version
        targetModelInfo.modelType = "service"
        targetModelInfo.modelName = toBeConverted.name

        return targetModelInfo
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

    @Throws(IllegalStateException::class)
    private fun fetchNewestServiceModel(): ServiceModel {
        val serviceModelInfo = serviceModelInfoFromRequest()
        val modelNewestUuid = commandUtils.getNewestModelUuid(serviceModelInfo.modelInvariantId);

        val serviceNewestModel = commandUtils.getServiceModel(modelNewestUuid);

        return serviceNewestModel;
    }
}
