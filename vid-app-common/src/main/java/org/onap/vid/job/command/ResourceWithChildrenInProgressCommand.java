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

import org.onap.vid.job.Job;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;

import java.util.HashMap;
import java.util.Map;


public class ResourceWithChildrenInProgressCommand extends BaseInProgressStatusCommand {

    public ResourceWithChildrenInProgressCommand() {
    }

    public ResourceWithChildrenInProgressCommand(JobSharedData sharedData,
                                                 String requestId,
                                                 String instanceId,
                                                 CommandParentData commandParentData) {
        init(sharedData, requestId, instanceId, commandParentData);
    }

    protected BaseInProgressStatusCommand init(JobSharedData sharedData,
                                               String requestId,
                                               String instanceId,
                                               CommandParentData commandParentData) {
        init(sharedData, requestId, instanceId);
        this.commandParentData= commandParentData;
        return this;
    }


    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>(super.getData());
        data.putAll(buildDataForChild());
        return data;
    }

    @Override
    public BaseInProgressStatusCommand init(JobSharedData sharedData, Map<String, Object> commandData) {
        return init(
                sharedData,
                (String) commandData.get("requestId"),
                (String) commandData.get("instanceId"),
                commandParentData.initParentData(commandData));
    }

    protected Map<String, Object> buildDataForChild() {
       return commandParentData.getParentData();
    }



    @Override
    protected NextCommand processJobStatus(Job.JobStatus jobStatus) {
        return new NextCommand(jobStatus, this);
    }

    @Override
    protected ExpiryChecker getExpiryChecker() {
        return x->false;
    }

}
