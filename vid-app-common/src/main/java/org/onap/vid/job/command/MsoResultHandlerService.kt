package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.job.Job
import org.onap.vid.job.impl.JobSharedData
import org.onap.vid.model.RequestReferencesContainer
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation
import org.onap.vid.mso.RestObject
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.services.AuditService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class MsoResultHandlerService
@Autowired constructor(private val asyncInstantiationBL: AsyncInstantiationBusinessLogic, private val auditService: AuditService) {

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(MsoResultHandlerService::class.java)
    }

    fun getRequest(jobSharedData: JobSharedData): ServiceInstantiation {
        return jobSharedData.request as ServiceInstantiation
    }

    fun handleRootResponse(jobUUID: UUID, msoResponse: RestObject<RequestReferencesContainer>): MsoResult {
        return if (msoResponse.statusCode in 200..399) {
            val jobStatus = Job.JobStatus.IN_PROGRESS
            val msoResourceIds = MsoResourceIds(msoResponse.get().requestReferences.requestId, msoResponse.get().requestReferences.instanceId)
            asyncInstantiationBL.auditVidStatus(jobUUID, jobStatus)
            setInitialRequestAuditStatusFromMso(jobUUID, msoResourceIds.requestId)
            asyncInstantiationBL.updateServiceInfo(jobUUID) { x ->
                x.jobStatus = jobStatus
                x.serviceInstanceId = msoResourceIds.instanceId
                x.msoRequestId = UUID.fromString(msoResourceIds.requestId)
            }
            MsoResult(jobStatus, msoResourceIds)
        } else {
            auditService.setFailedAuditStatusFromMso(jobUUID, null, msoResponse.statusCode, msoResponse.raw)
            handleRootCommandFailed(jobUUID)
        }
    }

    fun handleResponse(msoResponse: RestObject<RequestReferencesContainer>, actionDescription: String): MsoResult {
        return if (msoResponse.statusCode in 200..399) {
            val msoResourceIds = MsoResourceIds(msoResponse.get().requestReferences.requestId, msoResponse.get().requestReferences.instanceId)
            LOGGER.debug("Successfully sent $actionDescription. Request id: ${msoResourceIds.requestId}")
            MsoResult(Job.JobStatus.COMPLETED_WITH_NO_ACTION, msoResourceIds)
        } else {
            LOGGER.debug("Failed to $actionDescription. Details: ${msoResponse.raw}")
            MsoResult(Job.JobStatus.FAILED)
        }
    }


    fun handleRootCommandFailed(jobUUID: UUID): MsoResult {
        asyncInstantiationBL.handleFailedInstantiation(jobUUID)
        return MsoResult(Job.JobStatus.FAILED)
    }

    private fun setInitialRequestAuditStatusFromMso(jobUUID: UUID, requestId: String) {
        val initialMsoRequestStatus = "REQUESTED"
        asyncInstantiationBL.auditMsoStatus(jobUUID, initialMsoRequestStatus, requestId, null)
    }
}
