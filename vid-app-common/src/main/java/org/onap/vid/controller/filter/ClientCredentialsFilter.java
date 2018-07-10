package org.onap.vid.controller.filter;

import org.apache.commons.lang3.StringUtils;
import org.onap.vid.scheduler.SchedulerProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amichai on 13/05/2018.
 */
@WebFilter(urlPatterns = "/change-management/workflow/*")
public class ClientCredentialsFilter  extends GenericFilterBean {

    private final static EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(ClientCredentialsFilter.class);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
            return;

        String expectedAuthorization = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_BASIC_AUTH);
        String actualAuthorization = ((HttpServletRequest)request).getHeader("Authorization");

        if (verifyClientCredentials(actualAuthorization, expectedAuthorization)) {
            LOGGER.warn(EELFLoggerDelegate.debugLogger,"Client credentials authenticated.");
            chain.doFilter(request, response);
            return;
        }

        LOGGER.warn(EELFLoggerDelegate.debugLogger,"Client did not provide the expected credentials.");
        ((HttpServletResponse) response).sendError(401);
    }

    public boolean verifyClientCredentials(String actualAuthorization, String expectedAuthorization)
    {
        if (StringUtils.isEmpty(expectedAuthorization))
        {
            LOGGER.warn(EELFLoggerDelegate.debugLogger,String.format("Expected Authorization is not configured (key: %s)", SchedulerProperties.SCHEDULER_BASIC_AUTH));
            return true;
        }

        if (StringUtils.isEmpty(actualAuthorization))
        {
            LOGGER.warn(EELFLoggerDelegate.debugLogger,"Authorization header is missing.");
            return false;
        }

        return actualAuthorization.equals(expectedAuthorization);
    }

}
