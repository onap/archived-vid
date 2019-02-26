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

package org.onap.vid.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.model.ExceptionResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;

import static org.onap.vid.utils.Logging.getMethodCallerName;

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
        logger.error(EELFLoggerDelegate.errorLogger, "{}: {}", getMethodCallerName(), ExceptionUtils.getMessage(e), e);

        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return exceptionResponse;
    }

    public static ResponseEntity handleWebApplicationException(WebApplicationException e, EELFLoggerDelegate logger) {
        return ResponseEntity.status(e.getResponse().getStatus()).body(ControllersUtils.handleException(e, logger));
    }

}
