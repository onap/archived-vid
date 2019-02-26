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
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class TempFilterForCORS extends GenericFilterBean {

    private static final String ENV_MODE = "env.mode";
    private Boolean devMode = null;

     //dev mode is initialized here since @WebFilter doesn't support @Autowired
    //So we are sure that SystemProperties bean was initialed only after the first call to doFilter
    private boolean isDevMode() {
        if (devMode!=null) {
            return devMode;
        }
        else {
            if (!SystemProperties.containsProperty(ENV_MODE)) {
                devMode = Boolean.FALSE;
                return devMode;
            }

            String envMode = SystemProperties.getProperty(ENV_MODE);
            devMode = StringUtils.equalsIgnoreCase(envMode, "dev") ;
        }
        return devMode;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (isDevMode() && response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            ((HttpServletResponse) response).addHeader("Access-Control-Allow-Credentials", "true");
        }
        chain.doFilter(request, response);
    }
}
