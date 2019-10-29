/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.logging;

import java.net.URI;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.onap.logging.filter.base.AbstractMetricLogFilter;
import org.onap.logging.ref.slf4j.ONAPLogConstants;


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
        return String.valueOf(httpResponse.getStatusLine().getStatusCode());
    }

    @Override
    protected String getTargetEntity(HttpRequest httpRequest) {
        //fallback to default value that provided by AbstractMetricLogFilter
        return null;
    }

    @Override
    protected void additionalPre(HttpRequest request, HttpMessage message) {
        LoggingFilterHelper.updateInvocationIDInMdcWithHeaderValue(
            ()->message.getFirstHeader(ONAPLogConstants.Headers.INVOCATION_ID).getValue());
    }

}
