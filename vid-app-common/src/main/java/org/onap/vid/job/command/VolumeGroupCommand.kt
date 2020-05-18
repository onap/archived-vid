package org.onap.vid.job.command

import org.apache.commons.lang3.StringUtils.isNotEmpty
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.job.*
import org.onap.vid.model.Action
import org.onap.vid.model.serviceInstantiation.BaseResource
import org.onap.vid.model.serviceInstantiation.VfModule
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.togglz.core.manager.FeatureManager
import java.util.*

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class VolumeGroupCommand @Autowired constructor(
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
        private val LOGGER = EELFLoggerDelegate.getLogger(VolumeGroupCommand::class.java)
    }

    override fun createChildren(): Job.JobStatus {
        val request: VfModule = getRequest()
        val dataForChild = buildDataForChild(request, actionPhase)

        childJobs = pushChildrenJobsToBroker(listOf(request), dataForChild, JobType.VfmoduleInstantiation)

        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val serviceInstanceId = serviceInstanceIdFromRequest()
        val vnfInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_INSTANCE_ID)
        val vnfModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.VNF_MODEL_INFO)

        val instantiatePath = asyncInstantiationBL.getVolumeGroupInstantiationPath(serviceInstanceId,vnfInstanceId)

        val requestDetailsWrapper = msoRequestBuilder.generateVolumeGroupInstantiationRequest(
                request as VfModule,
                serviceModelInfoFromRequest(), serviceInstanceId,
                vnfModelInfo,vnfInstanceId,
                userId,
                testApi
        )

        val actionDescription = "create volumeGroup in $vnfInstanceId"

        return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        TODO("not implemented")
    }

    override fun isNeedToCreateMyself(): Boolean {
        return super.isNeedToCreateMyself() && isNotEmpty(getRequest().volumeGroupInstanceName)
    }

    override fun isNeedToDeleteMyself(): Boolean {
        return false
    }

    override fun getRequest(): VfModule {
        return sharedData.request as VfModule
    }

    override fun isDescendantHasAction(phase: Action): Boolean {
        return phase == getRequest().action
    }

    override fun addMyselfToChildrenData(commandParentData: CommandParentData, request: BaseResource) {
        commandParentData.addInstanceId(CommandParentData.CommandDataKey.VG_INSTANCE_ID, getActualInstanceId(request));
    }

    override fun replaceMyself(): Job.JobStatus {
        return Job.JobStatus.COMPLETED
    }
}
