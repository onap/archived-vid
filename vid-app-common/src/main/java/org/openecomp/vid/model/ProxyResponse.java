package org.openecomp.vid.model;

import com.google.common.base.MoreObjects;

/**
 * Created by Oren on 7/10/17.
 */
public class ProxyResponse {

    protected String errorMessage;

    protected int httpCode;

    public String getErrorMessage() {
        return errorMessage;
    }


    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("httpCode", httpCode)
                .add("errorMessage", errorMessage)
                .toString();
    }
}
