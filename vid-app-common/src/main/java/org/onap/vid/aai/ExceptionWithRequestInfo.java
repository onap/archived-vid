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
            return String.valueOf(cause);
        } else {
            return "" +
                    "Exception while handling " +
                    defaultIfNull(httpMethod, "request").toString() +
                    " " + requestedUrl +
                    ": " + (cause == null ? "null" : cause.toString());
        }
    }
}
