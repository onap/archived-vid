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

package org.onap.vid.job.impl;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.JobsBrokerService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DeleteOldJobsWorker extends QuartzJobBean {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(DeleteOldJobsWorker.class);

    private JobsBrokerService jobsBrokerService;
    private long secondsAgo;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        LOGGER.info(EELFLoggerDelegate.debugLogger, "delete old final jobs that has finished before {} seconds", secondsAgo);
        jobsBrokerService.deleteOldFinalJobs(secondsAgo);
    }

    //the following methods are used by quartz to inject members
    public void setJobsBrokerService(JobsBrokerService jobsBrokerService) {
        this.jobsBrokerService = jobsBrokerService;
    }

    public void setSecondsAgo(long secondsAgo) {
        this.secondsAgo = secondsAgo;
    }
}
