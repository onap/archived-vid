package org.onap.simulator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimulatorRequest {
    private String id;
    private String method;
    private String path;
    private String body;
    private Map<String, List<String>> queryParams;

    public Map<String, List<String>> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, List<String>> queryParams) {
        this.queryParams = queryParams;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(JsonNode body) {
        this.body = body.isTextual() ? body.textValue() : body.toString();
    }

    @Override
    public String toString() {
        return "SimulatorRequest{" +
                "id='" + id + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", body='" + body + '\'' +
                ", queryParams=" + queryParams +
                '}';
    }
}
