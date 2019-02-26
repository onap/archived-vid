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


import org.springframework.http.HttpMethod;


public class AaiResponseWithRequestInfo<T> {
    private AaiResponse<T> aaiResponse;
    private String requestedUrl;
    private String rawData;
    private HttpMethod httpMethod;

    public AaiResponseWithRequestInfo(HttpMethod httpMethod, String requestedUrl, AaiResponse<T> aaiResponse, String rawData) {
        this.aaiResponse = aaiResponse;
        this.requestedUrl = requestedUrl;
        this.rawData = rawData;
        this.httpMethod = httpMethod;
    }

    public void setRequestedUrl(String requestedUrl) {
        this.requestedUrl = requestedUrl;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public AaiResponse<T> getAaiResponse() {
        return aaiResponse;
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
}
