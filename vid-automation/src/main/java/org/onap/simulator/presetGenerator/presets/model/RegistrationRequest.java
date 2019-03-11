package org.onap.simulator.presetGenerator.presets.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

public class RegistrationRequest {

    public static class SimulatorRequest {
        public final HttpMethod method;
        public final String path;
        public final boolean strict;
        public final Map<String,String> headers;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final Map<String,List> queryParams;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final Object body;

        public SimulatorRequest(HttpMethod method, String path, Map<String, List> queryParams, Object body, boolean strictMatch, Map<String, String> headers) {
            this.method = method;
            this.path = path;
            this.queryParams = queryParams;
            this.body = body;
            this.strict = strictMatch;
            this.headers = headers;
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

    public static class Misc {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public Integer numberOfTimes;

        public boolean replace;

        public Misc(Integer numberOfTimes, boolean replace) {
            this.numberOfTimes = numberOfTimes;
            this.replace = replace;
        }
    }

    public SimulatorRequest simulatorRequest;
    public SimulatorResponse simulatorResponse;
    public Misc misc;

    public RegistrationRequest(SimulatorRequest simulatorRequest, SimulatorResponse simulatorResponse, Misc misc) {
        this.simulatorRequest = simulatorRequest;
        this.simulatorResponse = simulatorResponse;
        this.misc = misc;
    }

}
