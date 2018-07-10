package org.onap.vid.model.probes;

import org.springframework.http.HttpMethod;

public class HttpRequestMetadata extends StatusMetadata {
    private final HttpMethod httpMethod;
    private final int httpCode;
    private final String url;
    private final String rawData;

    public HttpRequestMetadata(HttpMethod httpMethod, int httpCode, String url, String rawData, String description, long duration) {
        super(description, duration);
        this.httpMethod = httpMethod;
        this.url = url;
        this.httpCode = httpCode;
        this.rawData = rawData;
    }

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
}
