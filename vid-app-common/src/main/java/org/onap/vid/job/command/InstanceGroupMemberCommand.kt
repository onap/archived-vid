package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.job.Job
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobCommand
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.model.serviceInstantiation.InstanceGroupMember
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
class InstanceGroupMemberCommand @Autowired constructor(
        private val asyncInstantiationBL: AsyncInstantiationBusinessLogic,
        restMso: RestMsoImplementation,
        private val msoRequestBuilder: MsoRequestBuilder,
        msoResultHandlerService: MsoResultHandlerService,
        inProgressStatusService:InProgressStatusService,
        watchChildrenJobsBL: WatchChildrenJobsBL,
        jobsBrokerService: JobsBrokerService,
        jobAdapter: JobAdapter,
        private val featureManager: FeatureManager
) : ResourceCommand(restMso, inProgressStatusService, msoResultHandlerService,
        watchChildrenJobsBL, jobsBrokerService, jobAdapter, featureManager), JobCommand {

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(InstanceGroupMemberCommand::class.java)
    }

    override fun createChildren(): Job.JobStatus {
        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val instanceGroupId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_GROUP_INSTANCE_ID)

        val instantiatePath = asyncInstantiationBL.getInstanceGroupMemberInstantiationPath(instanceGroupId)

        val requestDetailsWrapper = msoRequestBuilder.generateInstanceGroupMemberRequest(getRequest().instanceId, userId)

        val actionDescription = "add instance group member  ${getRequest().instanceId} to instance group $instanceGroupId"

        return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val instanceGroupId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.VNF_GROUP_INSTANCE_ID)
        val path = asyncInstantiationBL.getInstanceGroupMemberDeletePath(instanceGroupId)
        val requestDetailsWrapper = msoRequestBuilder.generateInstanceGroupMemberRequest(getRequest().instanceId, userId)
        return MsoRestCallPlan(HttpMethod.POST, path, Optional.of(requestDetailsWrapper), Optional.of(userId),
                "delete instance group member ${getRequest().instanceId} from instance group $instanceGroupId")
    }

    override fun getRequest(): InstanceGroupMember {
        return sharedData.request as InstanceGroupMember
    }
}