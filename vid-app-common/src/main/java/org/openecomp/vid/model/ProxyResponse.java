package org.openecomp.vid.model;

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

}
