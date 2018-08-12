package org.opencomp.simulator.presetGenerator.presets.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

public class RegistrationRequest {

    public static class SimulatorRequest {
        public final HttpMethod method;
        public final String path;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final Map<String,List> queryParams;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final Object body;

        public SimulatorRequest(HttpMethod method, String path, Map<String, List> queryParams, Object body) {
            this.method = method;
            this.path = path;
            this.queryParams = queryParams;
            this.body = body;
        }
    }

    public static class SimulatorResponse {
        public final int responseCode;
        public final Map<String,String> responseHeaders;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final Object body;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final String file;

        public SimulatorResponse(int responseCode, Map<String, String> responseHeaders, Object body, String file) {
            this.responseCode = responseCode;
            this.responseHeaders = responseHeaders;
            this.body = body;
            this.file = file;
        }
    }

    public SimulatorRequest simulatorRequest;
    public SimulatorResponse simulatorResponse;

    public RegistrationRequest(SimulatorRequest simulatorRequest, SimulatorResponse simulatorResponse) {
        this.simulatorRequest = simulatorRequest;
        this.simulatorResponse = simulatorResponse;
    }
}
