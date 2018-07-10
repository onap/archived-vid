package org.onap.vid.job;

public class NextCommand {
    private final Job.JobStatus status;
    private final JobCommand command;

    public NextCommand(Job.JobStatus nextStatus, JobCommand nextCommand) {
        this.status = nextStatus;
        this.command = nextCommand;
    }

    public NextCommand(Job.JobStatus nextStatus) {
        this.status = nextStatus;
        this.command = null;
    }

    public Job.JobStatus getStatus() {
        return status;
    }

    public JobCommand getCommand() {
        return command;
    }

}
