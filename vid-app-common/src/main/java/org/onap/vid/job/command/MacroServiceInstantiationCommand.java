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
import org.onap.vid.job.JobCommand;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MacroServiceInstantiationCommand extends ServiceInstantiationCommand implements JobCommand {

    public MacroServiceInstantiationCommand() {
        // empty constructor
    }

    @Override
    protected RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateServiceInstantiationRequest() {
        return asyncInstantiationBL.generateMacroServiceInstantiationRequest(
                getSharedData().getJobUuid(), getRequest(), optimisticUniqueServiceInstanceName, getSharedData().getUserId()
        );
    }

}
