package org.onap.vid.aai.util;

import org.onap.vid.utils.Logging;

/**
 * Wrapper for getting current context attributes
 */
public class ServletRequestHelper {

    public String extractOrGenerateRequestId() {
        return Logging.extractOrGenerateRequestId();
    }
}
