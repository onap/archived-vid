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

import static org.onap.vid.job.Job.JobStatus.FAILED;
import static org.onap.vid.job.Job.JobStatus.STOPPED;
import static org.onap.vid.job.command.ResourceCommandKt.ACTION_PHASE;
import static org.onap.vid.job.command.ResourceCommandKt.INTERNAL_STATE;

import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.JobException;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.command.JobCommandFactory;
import org.quartz.JobExecutionContext;
import org.slf4j.MDC;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class JobWorker extends QuartzJobBean {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(JobWorker.class);

    private JobsBrokerService jobsBrokerService;
    private JobCommandFactory jobCommandFactory;
    private Job.JobStatus topic;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Optional<Job> job;

        job = pullJob();

        while (job.isPresent()) {
            Job nextJob = executeJobAndGetNext(job.get());
            pushBack(nextJob);

            job = pullJob();
        }
    }

    private Optional<Job> pullJob() {
        try {
            return jobsBrokerService.pull(topic, UUID.randomUUID().toString());
        } catch (Exception e) {
            LOGGER.error("failed to pull job from queue, breaking: {}", e, e);
            tryMutingJobFromException(e);

            return Optional.empty();
        }
    }

    private void pushBack(Job nextJob) {
        try {
            jobsBrokerService.pushBack(nextJob);
        } catch (Exception e) {
            LOGGER.error("failed pushing back job to queue: {}", e, e);
        }
    }

    protected Job executeJobAndGetNext(Job job) {
        setThreadWithRandomRequestId();

        Object internalState = job.getData().get(INTERNAL_STATE);
        Object actionPhase = job.getData().get(ACTION_PHASE);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "going to execute job {} of {}: {}/{}  {}/{}",
                StringUtils.substring(String.valueOf(job.getUuid()), 0, 8),
                StringUtils.substring(String.valueOf(job.getTemplateId()), 0, 8),
                job.getStatus(),
                job.getType(),
                actionPhase,
                internalState
                );

        NextCommand nextCommand = executeCommandAndGetNext(job);

        return setNextCommandInJob(nextCommand, job);
    }

    private void setThreadWithRandomRequestId() {
        //make sure requestIds in outgoing requests are not reused
        MDC.put(ONAPLogConstants.MDCs.REQUEST_ID, UUID.randomUUID().toString());
    }

    private NextCommand executeCommandAndGetNext(Job job) {
        NextCommand nextCommand;
        try {
            final JobCommand jobCommand = jobCommandFactory.toCommand(job);
            nextCommand = jobCommand.call();
        } catch (Exception e) {
            LOGGER.error("error while executing job from queue: {}", e, e);
            nextCommand = new NextCommand(FAILED);
        }

        if (nextCommand == null) {
            nextCommand = new NextCommand(STOPPED);
        }
        return nextCommand;
    }

    private Job setNextCommandInJob(NextCommand nextCommand, Job job) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "transforming job {} of {}: {}/{} -> {}{}",
                StringUtils.substring(String.valueOf(job.getUuid()), 0, 8),
                StringUtils.substring(String.valueOf(job.getTemplateId()), 0, 8),
                job.getStatus(), job.getType(),
                nextCommand.getStatus(),
                nextCommand.getCommand() != null ? ("/" + nextCommand.getCommand().getType()) : "");

        job.setStatus(nextCommand.getStatus());

        if (nextCommand.getCommand() != null) {
            job.setTypeAndData(nextCommand.getCommand().getType(), nextCommand.getCommand().getData());
        }

        return job;
    }

    private void tryMutingJobFromException(Exception e) {
        // If there's JobException in the stack, read job uuid from
        // the exception, and mute it in DB.
        final int indexOfJobException =
                ExceptionUtils.indexOfThrowable(e, JobException.class);

        if (indexOfJobException >= 0) {
            try {
                final JobException jobException = (JobException) ExceptionUtils.getThrowableList(e).get(indexOfJobException);
                LOGGER.info(EELFLoggerDelegate.debugLogger, "muting job: {} ({})", jobException.getJobUuid(), jobException.toString());
                final boolean success = jobsBrokerService.mute(jobException.getJobUuid());
                if (!success) {
                    LOGGER.error("failed to mute job {}", jobException.getJobUuid());
                }
            } catch (Exception e1) {
                LOGGER.error("failed to mute job: {}", e1, e1);
            }
        }
    }

    //used by quartz to inject JobsBrokerService into the job
    //see JobSchedulerInitializer
    public void setJobsBrokerService(JobsBrokerService jobsBrokerService) {
        this.jobsBrokerService = jobsBrokerService;
    }

    public void setJobCommandFactory(JobCommandFactory jobCommandFactory) {
        this.jobCommandFactory = jobCommandFactory;
    }

    public void setTopic(Job.JobStatus topic) {
        this.topic = topic;
    }
}
