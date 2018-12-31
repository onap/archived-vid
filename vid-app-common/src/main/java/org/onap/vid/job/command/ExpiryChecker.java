package org.onap.vid.job.command;

import java.time.ZonedDateTime;

public interface ExpiryChecker {

    boolean isExpired(ZonedDateTime jobStartTime);
}
