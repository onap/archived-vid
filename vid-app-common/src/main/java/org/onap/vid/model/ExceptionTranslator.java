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

package org.onap.vid.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.onap.vid.utils.Logging.getMethodCallerName;

@ControllerAdvice
public class ExceptionTranslator {

    /** The logger. */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExceptionTranslator.class);

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ExceptionResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error(EELFLoggerDelegate.errorLogger, "{}: {}", getMethodCallerName(), ExceptionUtils.getMessage(e), e);
        Class<?> type = e.getRequiredType();
        String message;
        if (type.isEnum()) {
            message = "The parameter " + e.getName() + " must have a value among : " + StringUtils.join(type.getEnumConstants(), ", ");
        }
        else {
            message = "The parameter " + e.getName() + " must be of type " + type.getTypeName();
        }
        return new  ExceptionResponse(new ArgumentTypeMismatchException(message));
    }

    public static class ArgumentTypeMismatchException extends RuntimeException {
        public ArgumentTypeMismatchException(String message) {
            super(message);
        }
    }
}
