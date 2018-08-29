package org.onap.vid.aai.exceptions;

import org.onap.vid.aai.AaiResponse;
import org.onap.vid.exceptions.GenericUncheckedException;

/**
 * Created by Oren on 7/4/17.
 */
public class InvalidAAIResponseException extends GenericUncheckedException {
    public InvalidAAIResponseException(AaiResponse aaiResponse) {
        super(String.format("errorCode: %d, raw: %s", aaiResponse.getHttpCode(), aaiResponse.getErrorMessage()));
    }

    public InvalidAAIResponseException(int statusCode, String message) {
        super(String.format("errorCode: %d, raw: %s", statusCode, message));
    }
}
