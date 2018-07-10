package org.onap.vid.exceptions;

public class MaxRetriesException extends GenericUncheckedException {

    public MaxRetriesException(String operationDetails, int numberOfRetries) {
        super(String.format("Max retries for %s, retries: %d", operationDetails, numberOfRetries));
    }
}
