package org.openecomp.simulator.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimulatorResponse {
    private int responseCode;
    private String body;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "SimulatorResponse{" +
                "responseCode=" + responseCode +
                ", body='" + body + '\'' +
                '}';
    }
}
