package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.job.Job
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobCommand
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.model.Action
import org.onap.vid.model.serviceInstantiation.VfModule
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.util.*

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

    private fun planReplaceMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val serviceInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.SERVICE_INSTANCE_ID)
        val serviceModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.SERVICE_MODEL_INFO)
        val vnfModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.VNF_MODEL_INFO)
        val vnfInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_INSTANCE_ID)
        val replacePath = asyncInstantiationBL.getVfModuleReplacePath(serviceInstanceId, vnfInstanceId, getRequest().instanceId)

        val requestDetailsWrapper = msoRequestBuilder.generateVfModuleInstantiationRequest( 
                request as VfModule, serviceModelInfo, serviceInstanceId,vnfModelInfo, vnfInstanceId,null,userId, testApi)

        val actionDescription = "replace vfmodule ${request.instanceId}"

        return MsoRestCallPlan(HttpMethod.POST, replacePath, Optional.of(requestDetailsWrapper), Optional.of(userId), actionDescription)
    }

    override fun replaceMyself(): Job.JobStatus {
        try {
            val replaceMyselfCommand = planReplaceMyselfRestCall(commandParentData, sharedData.request, sharedData.userId, sharedData.testApi )
            return executeAndHandleMsoInstanceRequest(replaceMyselfCommand)
        } catch (exception: Exception) {
            LOGGER.error("Failed to replace instanceId ${getRequest().instanceId} ", exception)
            return Job.JobStatus.FAILED
        }
    }

    override fun isNeedToReplaceMySelf(): Boolean {
        return getActionType() == Action.Replace
    }
}
