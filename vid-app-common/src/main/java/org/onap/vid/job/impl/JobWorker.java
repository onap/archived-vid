package org.onap.vid.job.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.*;
import org.onap.vid.job.command.JobCommandFactory;
import org.onap.vid.properties.Features;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.togglz.core.manager.FeatureManager;

import java.util.Optional;
import java.util.UUID;

import static org.onap.vid.job.Job.JobStatus.FAILED;
import static org.onap.vid.job.Job.JobStatus.STOPPED;

@Component
public class JobWorker extends QuartzJobBean {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(JobWorker.class);

    private JobsBrokerService jobsBrokerService;
    private FeatureManager featureManager;
    private JobCommandFactory jobCommandFactory;
    private Job.JobStatus topic;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Optional<Job> job;

        if (!isMsoNewApiActive()) {
            return;
        }

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
            LOGGER.error(EELFLoggerDelegate.errorLogger, "failed to pull job from queue, breaking: {}", e, e);
            tryMutingJobFromException(e);

            return Optional.empty();
        }
    }

    private void pushBack(Job nextJob) {
        try {
            jobsBrokerService.pushBack(nextJob);
        } catch (Exception e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "failed pushing back job to queue: {}", e, e);
        }
    }

    protected Job executeJobAndGetNext(Job job) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "going to execute job {} of {}: {}/{}",
                StringUtils.substring(String.valueOf(job.getUuid()), 0, 8),
                StringUtils.substring(String.valueOf(job.getTemplateId()), 0, 8),
                job.getStatus(), job.getType());

        NextCommand nextCommand = executeCommandAndGetNext(job);

        return setNextCommandInJob(nextCommand, job);
    }

    private NextCommand executeCommandAndGetNext(Job job) {
        NextCommand nextCommand;
        try {
            final JobCommand jobCommand = jobCommandFactory.toCommand(job);
            nextCommand = jobCommand.call();
        } catch (Exception e) {
            LOGGER.error("error while executing job from queue: {}", e);
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

    private boolean isMsoNewApiActive() {
        return featureManager.isActive(Features.FLAG_ASYNC_INSTANTIATION);
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
                    LOGGER.error(EELFLoggerDelegate.errorLogger, "failed to mute job {}", jobException.getJobUuid());
                }
            } catch (Exception e1) {
                LOGGER.error(EELFLoggerDelegate.errorLogger, "failed to mute job: {}", e1, e1);
            }
        }
    }

    //used by quartz to inject JobsBrokerService into the job
    //see JobSchedulerInitializer
    public void setJobsBrokerService(JobsBrokerService jobsBrokerService) {
        this.jobsBrokerService = jobsBrokerService;
    }

    public void setFeatureManager(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    public void setJobCommandFactory(JobCommandFactory jobCommandFactory) {
        this.jobCommandFactory = jobCommandFactory;
    }

    public void setTopic(Job.JobStatus topic) {
        this.topic = topic;
    }
}
