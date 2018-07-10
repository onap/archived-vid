package org.onap.vid.exceptions;

public class GenericUncheckedException extends RuntimeException {
    public GenericUncheckedException(String message) {
        super(message);
    }

    public GenericUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericUncheckedException(Throwable cause) {
        super(cause);
    }
}
