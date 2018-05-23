package org.onap.vid.aai.util;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

import java.util.Date;

/**
 * Wrapper for logging messages
 */
public class LogHelper {
    private EELFLoggerDelegate logger;

    LogHelper(EELFLoggerDelegate logger) {
        this.logger = logger;
    }

    void multilog(String msg, Throwable e) {
        logInfo(msg, e);
        logDebug(msg, e);
    }

    void multilog(String msg) {
        logInfo(msg);
        logDebug(msg);
    }

    void logInfo(String msg, Throwable e) {
        logInfo(msg + e.toString());
    }

    void logDebug(String msg, Throwable e) {
        logDebug(msg + e.toString());
    }

    void logInfo(String msg) {
        logger.info(EELFLoggerDelegate.errorLogger, AAIRestInterface.dateFormat.format(new Date()) + "<== " + msg);
    }

    void logDebug(String msg) {
        logger.debug(EELFLoggerDelegate.debugLogger, AAIRestInterface.dateFormat.format(new Date()) + "<== " + msg);
    }
}
