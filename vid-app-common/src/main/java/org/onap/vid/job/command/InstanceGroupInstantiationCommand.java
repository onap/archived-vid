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

package org.onap.vid.job.command;//package org.onap.vid.job.command;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.model.Action;
import org.onap.vid.model.serviceInstantiation.InstanceGroup;
import org.onap.vid.mso.model.InstanceGroupInstantiationRequestDetails;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InstanceGroupInstantiationCommand extends ResourceInstantiationCommand {

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Override
    protected String getRequestPath() {
        return asyncInstantiationBL.getInstanceGroupInstantiationPath();
    }

    @Override
    protected RequestDetailsWrapper<InstanceGroupInstantiationRequestDetails> generateMSORequest(JobAdapter.AsyncJobRequest request, String userId) {
        return asyncInstantiationBL.generateInstanceGroupInstantiationRequest(
                (InstanceGroup) getSharedData().getRequest(),
                commandParentData.getModelInfo(CommandDataKey.SERVICE_MODEL_INFO),
                commandParentData.getInstanceId(CommandDataKey.SERVICE_INSTANCE_ID),
                getSharedData().getUserId()
        );
    }

    @Override
    protected String getJobAuditMSOStatus() {
        return "INSTANCE_GROUP_REQUESTED";
    }

    @Override
    protected boolean shouldInstantiateMyself() {
        return Action.Create == ((InstanceGroup) getSharedData().getRequest()).getAction();
    }
}
