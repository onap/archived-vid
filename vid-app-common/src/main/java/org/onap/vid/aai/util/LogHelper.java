package org.onap.vid.aai.util;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Wrapper for logging messages
 */
public class LogHelper {
    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

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
        logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + msg);
    }

    void logDebug(String msg) {
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + msg);
    }
}
