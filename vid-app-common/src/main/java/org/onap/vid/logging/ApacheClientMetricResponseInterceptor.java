package org.onap.vid.logging;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

public class ApacheClientMetricResponseInterceptor extends ApacheClientMetricInterceptor implements HttpResponseInterceptor {

    @Override
    public void process(HttpResponse response, HttpContext context)  {
        this.post(null, response);
    }
}
