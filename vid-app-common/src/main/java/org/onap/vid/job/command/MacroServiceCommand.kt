package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.aai.ExceptionWithRequestInfo
import org.onap.vid.changeManagement.RequestDetailsWrapper
import org.onap.vid.exceptions.AbortingException
import org.onap.vid.exceptions.MaxRetriesException
import org.onap.vid.exceptions.TryAgainException
import org.onap.vid.job.Job
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobCommand
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.model.Action
import org.onap.vid.model.VidNotions.ModelCategory.*
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.services.AuditService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.togglz.core.manager.FeatureManager
import java.util.*

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class MacroServiceCommand @Autowired constructor(
        inProgressStatusService: InProgressStatusService,
        watchChildrenJobsBL: WatchChildrenJobsBL,
        private val asyncInstantiationBL: AsyncInstantiationBusinessLogic,
        jobsBrokerService: JobsBrokerService,
        private val msoRequestBuilder: MsoRequestBuilder,
        msoResultHandlerService: MsoResultHandlerService,
        jobAdapter: JobAdapter,
        restMso: RestMsoImplementation,
        auditService: AuditService,
        private val featureManager: FeatureManager
) : RootServiceCommand(restMso, inProgressStatusService, msoResultHandlerService,
        watchChildrenJobsBL, jobsBrokerService, jobAdapter, asyncInstantiationBL, auditService, msoRequestBuilder, featureManager), JobCommand {


    companion object {
        private val Logger = EELFLoggerDelegate.getLogger(MacroServiceCommand::class.java)
    }

    override fun createChildren(): Job.JobStatus {
        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    private val pre1806Models = setOf(Transport, INFRASTRUCTURE_VPN, SERVICE_WITH_COLLECTION_RESOURCE);

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        try {
            val instantiatePath = asyncInstantiationBL.getServiceInstantiationPath(request as ServiceInstantiation)

            val requestDetailsWrapper = generateRequest(sharedData.jobUuid, request, optimisticUniqueServiceInstanceName, userId)

            val actionDescription = "create macro service instance"

            return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)
        }

        //Aai return bad response while checking names uniqueness
        catch (exception : ExceptionWithRequestInfo) {
            Logger.error("Failed to check name uniqueness in AAI. VID will try again later", exception)
            throw TryAgainException(exception);
        }

        //Vid reached to max retries while trying to find unique name in AAI
        catch (exception : MaxRetriesException) {
            Logger.error("Failed to find unused name in AAI", exception)
            throw AbortingException(exception);
        }
    }

    private fun generateRequest(jobUuid: UUID?, request: ServiceInstantiation, optimisticUniqueServiceInstanceName: String, userId: String): RequestDetailsWrapper<out Any> {
        // for transport or for infrastructure VPN - send the pre 1806 request
        if (shouldUsePre1806Request(request)){
            return msoRequestBuilder.generateMacroServicePre1806InstantiationRequest(request, userId)
        }
        return msoRequestBuilder.generateMacroServiceInstantiationRequest(jobUuid, request, optimisticUniqueServiceInstanceName, userId)
    }

    protected fun shouldUsePre1806Request(request: ServiceInstantiation): Boolean {
        return (request.vidNotions != null && pre1806Models.contains(request.vidNotions.modelCategory))
    }


    override fun handleInProgressStatus(jobStatus: Job.JobStatus): Job.JobStatus {
        asyncInstantiationBL.updateServiceInfoAndAuditStatus(sharedData.jobUuid, jobStatus)
        return if (jobStatus==Job.JobStatus.PAUSE) Job.JobStatus.IN_PROGRESS else jobStatus
    }

    override fun isDescendantHasAction(phase: Action): Boolean {
        return false
    }

}