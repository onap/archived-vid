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
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobCommand
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.model.serviceInstantiation.BaseResource
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.properties.VidProperties
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.services.AuditService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.togglz.core.manager.FeatureManager
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

const val UNIQUE_INSTANCE_NAME = "optimisticUniqueServiceInstanceName"

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
        jobsBrokerService: JobsBrokerService,
        private val msoRequestBuilder: MsoRequestBuilder,
        msoResultHandlerService: MsoResultHandlerService,
        jobAdapter: JobAdapter,
        restMso: RestMsoImplementation,
        auditService: AuditService,
        featureManager: FeatureManager
) : RootServiceCommand(restMso, inProgressStatusService, msoResultHandlerService,
        watchChildrenJobsBL, jobsBrokerService, jobAdapter, asyncInstantiationBL, auditService, msoRequestBuilder, featureManager), JobCommand {

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(ALaCarteServiceCommand::class.java)
    }

    override fun getRequest(): ServiceInstantiation {
        return msoResultHandlerService.getRequest(sharedData)
    }

    override fun createChildren(): Job.JobStatus {
        val dataForChild = buildDataForChild(getRequest(), actionPhase)

        childJobs = pushChildrenJobsToBroker(getRequest().children, dataForChild)

        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    override fun addMyselfToChildrenData(commandParentData: CommandParentData, request: BaseResource) {
        val instanceId = getActualInstanceId(request)
        commandParentData.addInstanceId(CommandParentData.CommandDataKey.SERVICE_INSTANCE_ID, instanceId)
        commandParentData.addModelInfo(CommandParentData.CommandDataKey.SERVICE_MODEL_INFO, request.modelInfo)
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val instantiatePath = asyncInstantiationBL.getServiceInstantiationPath(request as ServiceInstantiation)

        val requestDetailsWrapper = msoRequestBuilder.generateALaCarteServiceInstantiationRequest(
                request, optimisticUniqueServiceInstanceName, userId)

        val actionDescription = "create service instance"

        return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)
    }
}
