package org.onap.vid.job;

import java.util.UUID;

public class JobException extends RuntimeException {
    private final UUID jobUuid;

    public JobException(String message, UUID jobUuid, Throwable cause) {
        super(message, cause);
        this.jobUuid = jobUuid;
    }

    public UUID getJobUuid() {
        return jobUuid;
    }
}
