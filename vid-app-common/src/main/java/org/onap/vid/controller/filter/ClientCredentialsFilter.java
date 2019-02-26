/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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

    private static final EELFLoggerDelegate filterLogger = EELFLoggerDelegate.getLogger(ClientCredentialsFilter.class);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
            return;

        String expectedAuthorization = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_BASIC_AUTH);
        String actualAuthorization = ((HttpServletRequest)request).getHeader("Authorization");

        if (verifyClientCredentials(actualAuthorization, expectedAuthorization)) {
            filterLogger.warn(EELFLoggerDelegate.debugLogger,"Client credentials authenticated.");
            chain.doFilter(request, response);
            return;
        }

        filterLogger.warn(EELFLoggerDelegate.debugLogger,"Client did not provide the expected credentials.");
        ((HttpServletResponse) response).sendError(401);
    }

    public boolean verifyClientCredentials(String actualAuthorization, String expectedAuthorization)
    {
        if (StringUtils.isEmpty(expectedAuthorization))
        {
            filterLogger.warn(EELFLoggerDelegate.debugLogger,String.format("Expected Authorization is not configured (key: %s)", SchedulerProperties.SCHEDULER_BASIC_AUTH));
            return true;
        }

        if (StringUtils.isEmpty(actualAuthorization))
        {
            filterLogger.warn(EELFLoggerDelegate.debugLogger,"Authorization header is missing.");
            return false;
        }

        return actualAuthorization.equals(expectedAuthorization);
    }

}
