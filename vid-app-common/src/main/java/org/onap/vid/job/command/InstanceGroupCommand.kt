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
import org.onap.vid.model.serviceInstantiation.InstanceGroup
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.util.*

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class InstanceGroupCommand @Autowired constructor(
        private val asyncInstantiationBL: AsyncInstantiationBusinessLogic,
        restMso: RestMsoImplementation,
        msoResultHandlerService: MsoResultHandlerService,
        inProgressStatusService:InProgressStatusService,
        watchChildrenJobsBL: WatchChildrenJobsBL
) : ResourceCommand(restMso, inProgressStatusService, msoResultHandlerService, watchChildrenJobsBL), JobCommand {

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(InstanceGroupCommand::class.java)
    }

    override fun createChildren(): Job.JobStatus {
        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val serviceInstanceId = commandParentData.getInstanceId(CommandParentData.CommandDataKey.SERVICE_INSTANCE_ID)
        val serviceModelInfo = commandParentData.getModelInfo(CommandParentData.CommandDataKey.SERVICE_MODEL_INFO)

        val instantiatePath = asyncInstantiationBL.getInstanceGroupInstantiationPath()

        val requestDetailsWrapper = asyncInstantiationBL.generateInstanceGroupInstantiationRequest(
                request as InstanceGroup,
                serviceModelInfo, serviceInstanceId,
                userId
        )

        val actionDescription = "create instance group in $serviceInstanceId"

        return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val path = asyncInstantiationBL.getInstanceGroupDeletePath(getRequest().instanceId)
        return MsoRestCallPlan(HttpMethod.DELETE, path, Optional.empty(), Optional.of(userId),
                "delete instance group with id ${getRequest().instanceId}")

    }

}
