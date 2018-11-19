package org.onap.vid.client;

import java.io.IOException;

public class SyncRestClientException extends RuntimeException {
    public SyncRestClientException(Throwable cause) {
        super(cause);
    }

    public SyncRestClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
