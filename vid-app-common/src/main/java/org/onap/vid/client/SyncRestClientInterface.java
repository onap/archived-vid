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

    <T> HttpResponse<T> delete(String url, Map<String, String> headers, Class<T> aClass);

    HttpResponse<JsonNode> delete(String url, Map<String, String> headers);

    void destroy();

}
