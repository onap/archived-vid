/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.job.Job
import org.onap.vid.job.impl.JobSharedData
import org.onap.vid.model.RequestReferencesContainer
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation
import org.onap.vid.mso.RestObject
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.services.AuditService
import org.onap.vid.utils.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class MsoResultHandlerService
@Autowired constructor(private val asyncInstantiationBL: AsyncInstantiationBusinessLogic, private val auditService: AuditService, private val logging: Logging) {

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(MsoResultHandlerService::class.java)
    }

    fun getRequest(jobSharedData: JobSharedData): ServiceInstantiation {
        return jobSharedData.request as ServiceInstantiation
    }

    fun handleRootResponse(sharedData: JobSharedData, msoResponse: RestObject<RequestReferencesContainer>): MsoResult {
        val jobUUID:UUID = sharedData.jobUuid
        return if (msoResponse.statusCode in 200..399) {
            val jobStatus = Job.JobStatus.IN_PROGRESS
            val msoResourceIds = MsoResourceIds(msoResponse.get().requestReferences.requestId, msoResponse.get().requestReferences.instanceId)
            auditService.auditVidStatus(jobUUID, jobStatus)
            setInitialRequestAuditStatusFromMso(jobUUID, msoResourceIds.requestId)
            asyncInstantiationBL.updateServiceInfo(jobUUID) { x ->
                x.jobStatus = jobStatus
                x.serviceInstanceId = msoResourceIds.instanceId
                x.msoRequestId = UUID.fromString(msoResourceIds.requestId)
            }
            asyncInstantiationBL.addResourceInfo(sharedData, jobStatus, msoResourceIds.instanceId)
            MsoResult(Job.JobStatus.COMPLETED_WITH_NO_ACTION, msoResourceIds)
        } else {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to instantiate root resource. Details: ${msoResponse.raw}")
            auditService.setFailedAuditStatusFromMso(jobUUID, logging.currentRequestId(), msoResponse.statusCode, msoResponse.raw)
            asyncInstantiationBL.addFailedResourceInfo(sharedData, msoResponse)
            return MsoResult(Job.JobStatus.FAILED)
        }
    }

    fun handleResponse(sharedData: JobSharedData, msoResponse: RestObject<RequestReferencesContainer>, actionDescription: String): MsoResult {
        return if (msoResponse.statusCode in 200..399) {
            val msoResourceIds = MsoResourceIds(msoResponse.get().requestReferences.requestId, msoResponse.get().requestReferences.instanceId)
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "Successfully sent $actionDescription. Request id: ${msoResourceIds.requestId}")
            asyncInstantiationBL.addResourceInfo(sharedData, Job.JobStatus.IN_PROGRESS, msoResourceIds.instanceId)
            MsoResult(Job.JobStatus.COMPLETED_WITH_NO_ACTION, msoResourceIds)
        } else {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to $actionDescription. Details: ${msoResponse.raw}")
            auditService.setFailedAuditStatusFromMso(sharedData.rootJobId, logging.currentRequestId(), msoResponse.statusCode, "Failed to $actionDescription. Details: ${msoResponse.raw}")
            asyncInstantiationBL.addFailedResourceInfo(sharedData, msoResponse)
            MsoResult(Job.JobStatus.FAILED)
        }
    }

    private fun setInitialRequestAuditStatusFromMso(jobUUID: UUID, requestId: String) {
        val initialMsoRequestStatus = "REQUESTED"
        auditService.auditMsoStatus(jobUUID, initialMsoRequestStatus, requestId, null)
    }
}
