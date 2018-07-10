package org.onap.vid.aai;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class ExceptionWithRequestInfo extends RuntimeException {

    private final HttpMethod httpMethod;
    private final String requestedUrl;
    private final Integer httpCode;
    private final String rawData;

    public ExceptionWithRequestInfo(HttpMethod httpMethod, String requestedUrl, String rawData, Integer httpCode, Throwable cause) {
        super(toMessage(httpMethod, requestedUrl, cause), cause);
        this.httpMethod = httpMethod;
        this.requestedUrl = requestedUrl;
        this.rawData = rawData;
        this.httpCode = httpCode;
    }

    public ExceptionWithRequestInfo(HttpMethod httpMethod, String requestedUrl, Throwable cause) {
        this(httpMethod, requestedUrl, null, null, cause);
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

    public Integer getHttpCode() {
        return httpCode;
    }

    private static String toMessage(HttpMethod httpMethod, String requestedUrl, Throwable cause) {
        if (StringUtils.isEmpty(requestedUrl)) {
            return cause.toString();
        } else {
            return "" +
                    "Exception while handling " +
                    defaultIfNull(httpMethod, "request").toString() +
                    " " + requestedUrl +
                    ": " + cause.toString();
        }
    }
}
