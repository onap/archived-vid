package org.onap.vid.exceptions;

public class TryAgainException extends RuntimeException {

    public TryAgainException(Throwable cause) {
        super(cause);
    }
}
