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
import org.onap.vid.model.serviceInstantiation.InstanceGroup
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
class InstanceGroupCommand @Autowired constructor(
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
        private val LOGGER = EELFLoggerDelegate.getLogger(InstanceGroupCommand::class.java)
    }

    override fun createChildren(): Job.JobStatus {
        val dataForChild = buildDataForChild(getRequest(), actionPhase)

        childJobs = pushChildrenJobsToBroker(getRequest().vnfGroupMembers.values, dataForChild);

        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    override fun addMyselfToChildrenData(commandParentData: CommandParentData, request: BaseResource) {
        commandParentData.addInstanceId(CommandParentData.CommandDataKey.VNF_GROUP_INSTANCE_ID, request.instanceId)
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val serviceInstanceId = serviceInstanceIdFromRequest()

        val instantiatePath = asyncInstantiationBL.getInstanceGroupInstantiationPath()

        val requestDetailsWrapper = msoRequestBuilder.generateInstanceGroupInstantiationRequest(
                request as InstanceGroup,
                serviceModelInfoFromRequest(), serviceInstanceId,
                userId,
                testApi
        )

        val actionDescription = "create instance group in $serviceInstanceId"

        return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val path = asyncInstantiationBL.getInstanceGroupDeletePath(getRequest().instanceId)
        return MsoRestCallPlan(HttpMethod.DELETE, path, Optional.empty(), Optional.of(userId),
                "delete instance group with id ${getRequest().instanceId}")

    }

    override fun getRequest(): InstanceGroup {
        return sharedData.request as InstanceGroup
    }
}