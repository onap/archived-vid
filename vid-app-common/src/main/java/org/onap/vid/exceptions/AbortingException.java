package org.onap.vid.exceptions;

public class AbortingException extends RuntimeException {

    public AbortingException(Throwable cause) {
        super(cause);
    }
}
