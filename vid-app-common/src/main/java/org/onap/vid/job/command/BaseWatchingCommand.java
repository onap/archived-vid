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

import org.apache.commons.lang3.ObjectUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseWatchingCommand extends BaseInstantiationCommand implements JobCommand {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(BaseWatchingCommand.class);

    @Inject
    protected AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    private WatchChildrenJobsBL watchChildrenJobsBL;

    private List<String> childrenJobsIds;

    protected boolean isService;

    public BaseWatchingCommand() {}

    public BaseWatchingCommand(JobSharedData sharedData, List<String> childrenJobsIds, boolean isService) {
        init(sharedData, childrenJobsIds, isService);
    }

    @Override
    public NextCommand call() {
        Job.JobStatus cumulativeJobsStatus =  watchChildrenJobsBL.cumulateJobStatus(
                watchChildrenJobsBL.retrieveChildrenJobsStatus(childrenJobsIds),
                Job.JobStatus.COMPLETED);
        return getNextCommand(cumulativeJobsStatus);
    }

    protected abstract NextCommand getNextCommand(Job.JobStatus cumulativeJobsStatus);

    @Override
    public BaseWatchingCommand init(JobSharedData sharedData, Map<String, Object> commandData) {
        return init(
                sharedData,
                (List<String>) commandData.get("childrenJobs"),
                (boolean) commandData.get("isService")
        );
    }

    protected BaseWatchingCommand init(JobSharedData sharedData, List<String> childrenJobsIds, boolean isService) {
        super.init(sharedData);
        this.childrenJobsIds = ObjectUtils.defaultIfNull(childrenJobsIds, new ArrayList<>());
        this.isService = isService;
        return this;
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put("childrenJobs", childrenJobsIds);
        data.put("isService", isService);
        return data;
    }
}
