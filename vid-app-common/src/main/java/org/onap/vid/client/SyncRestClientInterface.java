/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

package org.onap.vid.client;

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;

import java.io.InputStream;
import java.util.Map;

public interface SyncRestClientInterface {
    class HEADERS {
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String AUTHORIZATION = "Authorization";
        public static final String X_ECOMP_INSTANCE_ID = "X-ECOMP-InstanceID";
    }

    HttpResponse<JsonNode> post(String url, Map<String, String> headers, Object body);

    <T> HttpResponse<T> post(String url, Map<String, String> headers, Object body, Class<T> aClass);

    HttpResponse<JsonNode> get(String url, Map<String, String> headers, Map<String, String> routeParams);

    <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> routeParams, Class<T> aClass);

    HttpResponse<InputStream> getStream(String url, Map<String, String> headers, Map<String, String> routeParams);

    HttpResponse<JsonNode> put(String url, Map<String, String> headers, Object body);

    <T> HttpResponse<T> put(String url, Map<String, String> headers, Object body, Class<T> aClass);

    <T> HttpResponse<T> delete(String url, Map<String, String> headers, Object body, Class<T> aClass);

    <T> HttpResponse<T> delete(String url, Map<String, String> headers, Class<T> aClass);

    HttpResponse<JsonNode> delete(String url, Map<String, String> headers);

    void destroy();

}
