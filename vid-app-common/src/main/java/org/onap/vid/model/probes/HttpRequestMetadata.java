/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

package org.onap.vid.model.probes;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.onap.vid.client.UnirestPatchKt.extractRawAsString;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.aai.ResponseWithRequestInfo;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;

public class HttpRequestMetadata extends StatusMetadata {
    private final HttpMethod httpMethod;
    private final int httpCode;
    private final String url;
    private String rawData = "";

    public HttpRequestMetadata(HttpMethod httpMethod, int httpCode, String url, String rawData, String description, long duration) {
        super(description, duration);
        this.httpMethod = httpMethod;
        this.url = url;
        this.httpCode = httpCode;
        this.rawData = rawData;
    }

    public HttpRequestMetadata(ResponseWithRequestInfo response, String description, long duration) {
        this(response, description, duration, true);
    }

    public HttpRequestMetadata(RestObjectWithRequestInfo response, String description, long duration) {
        super(description, duration);
        this.httpMethod = response.getHttpMethod();
        this.url = response.getRequestedUrl();
        this.httpCode = response.getHttpCode();
        this.rawData = response.getRawData();
    }

    public HttpRequestMetadata(ResponseWithRequestInfo response, String description, long duration, boolean readRawData) {
        super(description, duration);
        this.httpMethod = response.getRequestHttpMethod();
        this.url = response.getRequestUrl();
        this.httpCode = response.getResponse().getStatus();
        if (readRawData) {
            try {
                this.rawData = response.getResponse().readEntity(String.class);
            } catch (Exception e) {
                //Nothing to do here
            }
        }
    }

    public HttpRequestMetadata(ExceptionWithRequestInfo exception, long duration) {
        this(exception.getHttpMethod(),
                defaultIfNull(exception.getHttpCode(), 0),
                exception.getRequestedUrl(),
                exception.getRawData(),
                Logging.exceptionToDescription(exception.getCause()),
                duration);
    }

    public HttpRequestMetadata(HttpResponseWithRequestInfo response, String description, long duration, boolean readRawData) {
        super(description, duration);
        this.httpMethod = response.getRequestHttpMethod();
        this.url = response.getRequestUrl();
        this.httpCode = response.getResponse().getStatus();
        if (readRawData) {
            this.rawData = extractRawAsString(response.getResponse());
        }
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
        return StringUtils.substring(rawData, 0, 500);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("httpMethod", httpMethod)
                .add("httpCode", httpCode)
                .add("url", url)
                .add("duration", duration)
                .add("description", description)
                .add("rawData", rawData)
                .toString();
    }
}
