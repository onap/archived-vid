package org.onap.vid.controllers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.model.ExceptionResponse;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.att.eelf.configuration.Configuration.MDC_KEY_REQUEST_ID;
import static org.onap.vid.utils.Logging.getMethodName;

public class ControllersUtils {


    public static String extractUserId(HttpServletRequest request) {
        String userId = "";
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
            if (user != null) {
                //userId = user.getHrid();
                userId = user.getLoginId();
                if (userId == null)
                    userId = user.getOrgUserId();
            }
        }
        return userId;
    }

    public static ExceptionResponse handleException(Exception e, EELFLoggerDelegate logger) {
        logger.error(EELFLoggerDelegate.errorLogger, "{}: {}", getMethodName(), ExceptionUtils.getMessage(e), e);

        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return exceptionResponse;
    }
}
