package org.onap.vid.aai;

import org.springframework.http.HttpMethod;

import javax.ws.rs.core.Response;

public class ResponseWithRequestInfo {
    private String requestUrl;
    private HttpMethod requestHttpMethod;
    private Response response;

    public ResponseWithRequestInfo(Response response, String requestUrl, HttpMethod requestHttpMethod) {
        this.response = response;
        this.requestUrl = requestUrl;
        this.requestHttpMethod = requestHttpMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public HttpMethod getRequestHttpMethod() {
        return requestHttpMethod;
    }

    public Response getResponse() {
        return response;
    }
}
