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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.onap.vid.utils.Logging.getMethodCallerName;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public abstract class VidRestrictedBaseController extends RestrictedBaseController {

    protected final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(this.getClass().getName());

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LOGGER.error(EELFLoggerDelegate.errorLogger, "{}: {}", getMethodCallerName(), ExceptionUtils.getMessage(e), e);
        Class<?> type = e.getRequiredType();
        String message;
        if (type.isEnum()) {
            message = "The parameter " + e.getName() + " must have a value among : " + StringUtils.join(type.getEnumConstants(), ", ");
        }
        else {
            message = "The parameter " + e.getName() + " must be of type " + type.getTypeName();
        }
        return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value=INTERNAL_SERVER_ERROR)
    public ExceptionResponse exceptionHandler(Exception e) {
        return ControllersUtils.handleException(e, LOGGER);
    }
}
