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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WatchingCommand extends BaseWatchingCommand {

    public WatchingCommand() {}

    public WatchingCommand(JobSharedData sharedData, List<String> childrenJobsIds, boolean isService) {
        super(sharedData, childrenJobsIds, isService);
    }

    protected NextCommand getNextCommand(Job.JobStatus cumulativeJobsStatus) {
        if (cumulativeJobsStatus==Job.JobStatus.IN_PROGRESS) {
            return (isService) ? new NextCommand(Job.JobStatus.IN_PROGRESS, this)
                               : new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, this);
        }
        if (isService) {
            asyncInstantiationBL.updateServiceInfoAndAuditStatus(getSharedData().getJobUuid(), cumulativeJobsStatus);
        }
        return new NextCommand(cumulativeJobsStatus);
    }

}
