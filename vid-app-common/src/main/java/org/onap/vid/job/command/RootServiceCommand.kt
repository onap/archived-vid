package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.job.Job
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobCommand
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.job.impl.JobSharedData
import org.onap.vid.model.Action
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.services.AuditService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import java.util.*

abstract class RootServiceCommand @Autowired constructor(
        restMso: RestMsoImplementation,
        inProgressStatusService: InProgressStatusService,
        msoResultHandlerService: MsoResultHandlerService,
        watchChildrenJobsBL: WatchChildrenJobsBL,
        jobsBrokerService: JobsBrokerService,
        jobAdapter: JobAdapter,
        private val asyncInstantiationBL: AsyncInstantiationBusinessLogic,
        private val auditService: AuditService,
        private val msoRequestBuilder: MsoRequestBuilder
) : ResourceCommand(restMso, inProgressStatusService, msoResultHandlerService,
        watchChildrenJobsBL, jobsBrokerService, jobAdapter), JobCommand {

    lateinit var optimisticUniqueServiceInstanceName: String

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(RootServiceCommand::class.java)
    }

    final override fun onInitial(phase: Action) {
        if (phase== Action.Delete) {
            asyncInstantiationBL.updateServiceInfoAndAuditStatus(sharedData.jobUuid, Job.JobStatus.IN_PROGRESS)
        }
    }

    final override fun getExternalInProgressStatus() = Job.JobStatus.IN_PROGRESS

    final override fun getData(): Map<String, Any?> {
        return super.getData() + mapOf(UNIQUE_INSTANCE_NAME to optimisticUniqueServiceInstanceName)
    }

    final override fun onFinal(jobStatus: Job.JobStatus) {
        asyncInstantiationBL.updateServiceInfoAndAuditStatus(sharedData.jobUuid, jobStatus)
        if (jobStatus.isFailure) {
            asyncInstantiationBL.handleFailedInstantiation(sharedData.jobUuid)
        }
    }

    final override fun init(sharedData: JobSharedData, commandData: Map<String, Any>): ResourceCommand {
        optimisticUniqueServiceInstanceName = commandData.getOrDefault(UNIQUE_INSTANCE_NAME, "") as String
        return super<ResourceCommand>.init(sharedData, commandData)
    }

    final override fun isServiceCommand(): Boolean = true

    final override fun getExpiryChecker(): ExpiryChecker {
        return ServiceExpiryChecker()
    }

    override fun resumeMyself(): Job.JobStatus {
        val requestType = "createInstance"
        val scope = "service"
        val serviceInstanceId = getActualInstanceId(getRequest())
        try {
            val requests = auditService.retrieveRequestsFromMsoByServiceIdAndRequestTypeAndScope(serviceInstanceId, requestType, scope)
            if (requests.isEmpty() || requests[0].requestId == null) {
                LOGGER.error("Failed to retrieve requestId with type: $type, scope: $scope for service instanceId $serviceInstanceId ")
                return Job.JobStatus.FAILED
            }
            val createMyselfCommand = planResumeMyselfRestCall(requests[0].requestId, sharedData.userId)
            return executeAndHandleMsoInstanceRequest(createMyselfCommand)
        } catch (exception: Exception) {
            LOGGER.error("Failed to resume instanceId $serviceInstanceId ", exception)
            return Job.JobStatus.FAILED
        }
    }

    private fun planResumeMyselfRestCall(requestId: String, userId: String): MsoRestCallPlan {
        val path = asyncInstantiationBL.getResumeRequestPath(requestId)
        return MsoRestCallPlan(HttpMethod.POST, path, Optional.empty(), Optional.of(userId), "resume request $requestId")
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val requestDetailsWrapper = msoRequestBuilder.generateServiceDeletionRequest(
                request as ServiceInstantiation, userId
        )
        val path = asyncInstantiationBL.getServiceDeletionPath(request.instanceId)
        return MsoRestCallPlan(HttpMethod.DELETE, path, Optional.of(requestDetailsWrapper), Optional.empty(),
                "delete instance with id ${request.instanceId}")
    }
}