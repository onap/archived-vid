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
import org.onap.vid.changeManagement.RequestDetailsWrapper
import org.onap.vid.job.*
import org.onap.vid.model.Action
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.mso.model.ServiceDeletionRequestDetails
import org.onap.vid.properties.VidProperties
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class ServiceExpiryChecker : ExpiryChecker {

    override fun isExpired(jobStartTime: ZonedDateTime?): Boolean {
        val now = ZonedDateTime.now()
        val maxHoursInProgress = VidProperties.getLongProperty(VidProperties.VID_JOB_MAX_HOURS_IN_PROGRESS)
        val hoursBetween = ChronoUnit.HOURS.between(jobStartTime, now)
        return maxHoursInProgress in 1..hoursBetween
    }
}


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ALaCarteServiceCommand @Autowired constructor(
        inProgressStatusService: InProgressStatusService,
        watchChildrenJobsBL: WatchChildrenJobsBL,
        private val asyncInstantiationBL: AsyncInstantiationBusinessLogic,
        private val jobsBrokerService: JobsBrokerService,
        msoResultHandlerService: MsoResultHandlerService,
        private val jobAdapter: JobAdapter,
        restMso: RestMsoImplementation
) : ResourceCommand(restMso, inProgressStatusService, msoResultHandlerService, watchChildrenJobsBL), JobCommand {

    override fun getExpiryChecker(): ExpiryChecker {
        return ServiceExpiryChecker();
    }

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(ALaCarteServiceCommand::class.java)
    }

    override fun getRequest(): ServiceInstantiation {
        return msoResultHandlerService.getRequest(sharedData)
    }

    override fun createChildren(): Job.JobStatus {
        val dataForChild = buildDataForChild(getRequest())//.plus(ACTION_PHASE to actionPhase)

        val childJobType = when (actionPhase) {
            Action.Create -> JobType.InstanceGroupInstantiation
            Action.Delete -> JobType.InstanceGroup
            else -> return Job.JobStatus.COMPLETED
        }

        childJobs = getRequest().vnfGroups
                .map { jobAdapter.createChildJob(childJobType, Job.JobStatus.CREATING, it.value, sharedData, dataForChild) }
                .map { jobsBrokerService.add(it) }
                .map { it.toString() }

        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    private fun buildDataForChild(request: ServiceInstantiation): Map<String, Any> {
        val commandParentData = CommandParentData()
        commandParentData.addInstanceId(CommandParentData.CommandDataKey.SERVICE_INSTANCE_ID, request.instanceId)
        commandParentData.addModelInfo(CommandParentData.CommandDataKey.SERVICE_MODEL_INFO, request.modelInfo)
        return commandParentData.parentData
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        TODO("not implemented")
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val requestDetailsWrapper = generateServiceDeletionRequest()
        val path = asyncInstantiationBL.getServiceDeletionPath(getRequest().instanceId)
        return MsoRestCallPlan(HttpMethod.DELETE, path, Optional.of(requestDetailsWrapper), Optional.empty(),
                "delete instance with id ${getRequest().instanceId}")

    }

    override fun handleInProgressStatus(jobStatus: Job.JobStatus): Job.JobStatus {
        if (jobStatus==Job.JobStatus.FAILED) {
            asyncInstantiationBL.handleFailedInstantiation(sharedData.jobUuid)
            return jobStatus
        }

        asyncInstantiationBL.updateServiceInfoAndAuditStatus(sharedData.jobUuid, jobStatus)
        return  if (jobStatus == Job.JobStatus.PAUSE) Job.JobStatus.IN_PROGRESS else jobStatus
    }


    private fun generateServiceDeletionRequest(): RequestDetailsWrapper<ServiceDeletionRequestDetails> {
        return asyncInstantiationBL.generateALaCarteServiceDeletionRequest(
                sharedData.jobUuid, getRequest(), sharedData.userId
        )
    }

    override fun getExternalInProgressStatus() = Job.JobStatus.IN_PROGRESS

    override fun isServiceCommand(): Boolean = true

    override fun onFinal(jobStatus: Job.JobStatus) {
        asyncInstantiationBL.updateServiceInfoAndAuditStatus(sharedData.jobUuid, jobStatus)
    }

    override fun onInitial(phase: Action) {
        if (phase== Action.Delete) {
            asyncInstantiationBL.updateServiceInfoAndAuditStatus(sharedData.jobUuid, Job.JobStatus.IN_PROGRESS)
        }
    }
}
