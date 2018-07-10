package org.onap.vid.exceptions;

public class OperationNotAllowedException extends GenericUncheckedException {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
