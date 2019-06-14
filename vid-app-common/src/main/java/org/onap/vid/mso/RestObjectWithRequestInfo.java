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

package org.onap.vid.mso;

import org.springframework.http.HttpMethod;

import java.util.Objects;

public class RestObjectWithRequestInfo<T> {

    private final RestObject<T> restObject;
    private final String requestedUrl;
    private final int httpCode;
    private final String rawData;
    private final HttpMethod httpMethod;

    public RestObjectWithRequestInfo(HttpMethod httpMethod, String requestedUrl, RestObject<T> restObject, int httpCode, String rawData) {
        this.restObject = restObject;
        this.requestedUrl = requestedUrl;
        this.httpCode = httpCode;
        this.rawData = rawData;
        this.httpMethod = httpMethod;
    }

    public RestObjectWithRequestInfo(HttpMethod httpMethod, String requestedUrl, RestObject<T> restObject) {
        this.httpMethod = httpMethod;
        this.requestedUrl = requestedUrl;
        this.restObject = restObject;
        this.httpCode = restObject.getStatusCode();
        this.rawData = (Objects.nonNull(restObject.getRaw())) ? restObject.getRaw() : "";
    }

    public RestObject<T> getRestObject() {
        return restObject;
    }

    public String getRequestedUrl() {
        return requestedUrl;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getRawData() {
        return rawData;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}

