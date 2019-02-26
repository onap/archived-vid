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

import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestObject;

import javax.inject.Inject;


public abstract class BaseRootCommand extends CommandBase{

    @Inject
    private MsoResultHandlerService msoResultHandlerService;

    @Override
    protected CommandBase init(JobSharedData sharedData) {
        super.init(sharedData);
        return this;
    }

    protected ServiceInstantiation getRequest() {
        return msoResultHandlerService.getRequest(getSharedData());
    }

    protected NextCommand handleRootResponse(RestObject<RequestReferencesContainer> msoResponse){
        MsoResult msoResult = msoResultHandlerService.handleRootResponse(getSharedData().getJobUuid(), msoResponse);
        return new NextCommand(msoResult.getJobStatus(),
                (msoResult.getMsoResourceIds()!=null) ?
                        new ServiceInProgressStatusCommand(getSharedData(), msoResult.getMsoResourceIds()) :
                        null
        );

    }

    protected NextCommand handleCommandFailed() {
        return new NextCommand(msoResultHandlerService.handleRootCommandFailed(getSharedData().getJobUuid()).getJobStatus());
    }

}
