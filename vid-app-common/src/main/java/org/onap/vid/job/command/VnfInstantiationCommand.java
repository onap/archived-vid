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

package org.onap.vid.job.command;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.model.VnfInstantiationRequestDetails;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VnfInstantiationCommand extends ResourceInstantiationCommand {

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Override
    protected String getRequestPath() {
        return asyncInstantiationBL.getVnfInstantiationPath( commandParentData.getInstanceId(CommandDataKey.SERVICE_INSTANCE_ID));
    }

    @Override
    protected RequestDetailsWrapper<VnfInstantiationRequestDetails> generateMSORequest(JobAdapter.AsyncJobRequest request, String userId) {
        return asyncInstantiationBL.generateVnfInstantiationRequest(
                (Vnf) getSharedData().getRequest(),
                commandParentData.getModelInfo(CommandDataKey.SERVICE_MODEL_INFO),
                commandParentData.getInstanceId(CommandDataKey.SERVICE_INSTANCE_ID),
                getSharedData().getUserId()
        );
    }

    @Override
    protected String getJobAuditMSOStatus() {
        return "VNF_REQUESTED";
    }

    @Override
    protected NextCommand getNextCommand(String requestId, String instanceId) {
        return new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS,
                new VnfInProgressStatusCommand(getSharedData(), requestId, instanceId, commandParentData));
    }

}
