package org.onap.vid.logging;

import java.net.URI;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.onap.logging.filter.base.AbstractMetricLogFilter;


public abstract class ApacheClientMetricInterceptor extends AbstractMetricLogFilter<HttpRequest, HttpResponse, HttpMessage> {

    @Override
    protected void addHeader(HttpMessage httpMessage, String headerName, String headerValue) {
        httpMessage.addHeader(headerName, headerValue);
    }

    @Override
    protected String getTargetServiceName(HttpRequest httpRequest) {
        return httpRequest.getRequestLine().getUri();
    }

    @Override
    protected String getServiceName(HttpRequest httpRequest) {
        return URI.create(httpRequest.getRequestLine().getUri()).getPath();
    }

    @Override
    protected int getHttpStatusCode(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode();
    }

    @Override
    protected String getResponseCode(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getReasonPhrase();
    }

    @Override
    protected String getTargetEntity(HttpRequest httpRequest) {
        return "";
    }
}
