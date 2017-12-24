package org.openecomp.simulator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimulatorResponse {
    private int responseCode;
    private Map<String, String> responseHeaders;
    private String body;
    private String file;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(JsonNode body) {
        this.body = body.isTextual() ? body.textValue() : body.toString();
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "SimulatorResponse{" +
                "responseCode=" + responseCode +
                ", body='" + body + '\'' +
                ", file='" + file + '\'' +
                ", responseHeaders='" + responseHeaders + '\'' +
                '}';
    }
}
