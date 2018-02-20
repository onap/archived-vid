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
