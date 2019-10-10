package org.onap.vid.logging;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

public class ApacheClientMetricRequestInterceptor extends ApacheClientMetricInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest request, HttpContext context) {
        this.pre(request, request);
    }
}
