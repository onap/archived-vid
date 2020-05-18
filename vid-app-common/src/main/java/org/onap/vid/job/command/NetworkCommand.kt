package org.onap.vid.job.command

import org.onap.vid.job.Job
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobCommand
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.model.Action
import org.onap.vid.model.serviceInstantiation.Network
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
class NetworkCommand @Autowired constructor(
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
    override fun createChildren(): Job.JobStatus {
        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val serviceInstanceId = serviceInstanceIdFromRequest()
        val instantiatePath = asyncInstantiationBL.getNetworkInstantiationPath(serviceInstanceId)
        val requestDetailsWrapper = msoRequestBuilder.generateNetworkInstantiationRequest(
                request as Network,
                serviceModelInfoFromRequest(),
                serviceInstanceId,
                userId,
                testApi
        )

        val actionDescription = "create network in $serviceInstanceId"

        return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val serviceInstanceId = serviceInstanceIdFromRequest()
        val path = asyncInstantiationBL.getNetworkDeletePath(serviceInstanceId, getRequest().instanceId)
        val requestDetailsWrapper = msoRequestBuilder.generateDeleteNetworkRequest(getRequest() as Network, userId)
        return MsoRestCallPlan(HttpMethod.DELETE, path, Optional.of(requestDetailsWrapper), Optional.of(userId),
                "delete network ${getRequest().instanceId} from service instance $serviceInstanceId")
    }

    override fun isDescendantHasAction(phase: Action): Boolean {
        return false
    }
}