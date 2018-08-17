package org.onap.vid.aai.exceptions;

import com.mashape.unirest.http.HttpResponse;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.exceptions.GenericUncheckedException;

/**
 * Created by Oren on 7/4/17.
 */
public class InvalidAAIResponseException extends GenericUncheckedException {
    public InvalidAAIResponseException(AaiResponse aaiResponse) {
        super(String.format("errorCode: %d, raw: %s", aaiResponse.getHttpCode(), aaiResponse.getErrorMessage()));
    }

    public InvalidAAIResponseException(int status, String message)  {
        super(String.format("errorCode: %d, raw: %s", status, message));
    }
}
