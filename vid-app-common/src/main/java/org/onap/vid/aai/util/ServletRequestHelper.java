package org.onap.vid.aai.util;

import javax.servlet.http.HttpServletRequest;
import static org.onap.vid.utils.Logging.getHttpServletRequest;

/**
 * Wrapper for getting current context attributes
 */
public class ServletRequestHelper {

    public HttpServletRequest getServletRequest() {
        return getHttpServletRequest();
    }
}
