package org.onap.vid.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import java.io.InputStream;
import java.util.Map;

public interface SyncRestClientInterface {

    HttpResponse<JsonNode> post(String url, Map<String, String> headers, Object body);

    <T> HttpResponse<T> post(String url, Map<String, String> headers, Object body, Class<T> aClass);

    HttpResponse<JsonNode> get(String url, Map<String, String> headers,  Map<String, String> routeParams);

    <T> HttpResponse<T> get(String url, Map<String, String> headers, Map<String, String> routeParams, Class<T> aClass);

    HttpResponse<InputStream> getStream(String url, Map<String, String> headers, Map<String, String> routeParams);

    HttpResponse<JsonNode> put(String url, Map<String, String> headers, Object body);

    <T> HttpResponse<T> put(String url, Map<String, String> headers, Object body,  Class<T> aClass);

    <T> HttpResponse<T> delete(String url, Map<String, String> headers,  Class<T> aClass);

    HttpResponse<JsonNode> delete(String url, Map<String, String> headers);

}
