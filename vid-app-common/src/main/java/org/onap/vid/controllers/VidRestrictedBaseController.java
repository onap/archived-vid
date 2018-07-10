package org.onap.vid.controllers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.onap.vid.model.ExceptionResponse;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
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
        ResponseEntity  response = new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value=INTERNAL_SERVER_ERROR)
    public ExceptionResponse exceptionHandler(Exception e) {
        return ControllersUtils.handleException(e, LOGGER);
    }
}
