package org.onap.vid.exceptions;

public class DbFailureUncheckedException extends GenericUncheckedException {


    public DbFailureUncheckedException(String message) {
        super(message);
    }

    public DbFailureUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbFailureUncheckedException(Throwable cause) {
        super(cause);
    }
}
