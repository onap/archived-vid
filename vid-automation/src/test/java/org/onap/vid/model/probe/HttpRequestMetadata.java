package org.onap.vid.model.probe;

import org.springframework.http.HttpMethod;

public class HttpRequestMetadata {


    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getUrl() {
        return url;
    }

    public String getRawData() {
        return rawData;
    }

    public float getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    private HttpMethod httpMethod;
    private int httpCode;
    private String url;
    private String rawData;
    private String description;
    private float duration;

    public HttpRequestMetadata(){
    }

    public HttpRequestMetadata(HttpMethod httpMethod,int httpCode, String url, String rawData, String description) {
        this.httpMethod = httpMethod;
        this.httpCode = httpCode;
        this.url = url;
        this.rawData = rawData;
        this.description = description;
    }
}