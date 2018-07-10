package org.onap.vid.aai;


import org.springframework.http.HttpMethod;

import java.io.Serializable;

public class AaiResponseWithRequestInfo<T> implements Serializable {
    private AaiResponse<T> aaiResponse;
    private String requestedUrl;
    private String rawData;
    private HttpMethod httpMethod;

    public AaiResponseWithRequestInfo(HttpMethod httpMethod, String requestedUrl, AaiResponse<T> aaiResponse, String rawData) {
        this.aaiResponse = aaiResponse;
        this.requestedUrl = requestedUrl;
        this.rawData = rawData;
        this.httpMethod = httpMethod;
    }

    public void setRequestedUrl(String requestedUrl) {
        this.requestedUrl = requestedUrl;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public AaiResponse<T> getAaiResponse() {
        return aaiResponse;
    }

    public String getRequestedUrl() {
        return requestedUrl;
    }

    public String getRawData() {
        return rawData;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
