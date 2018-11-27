package org.onap.vid.client;

public class SyncRestClientException extends RuntimeException {
    public SyncRestClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
