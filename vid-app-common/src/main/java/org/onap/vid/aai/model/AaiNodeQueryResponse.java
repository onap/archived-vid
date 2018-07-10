package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AaiNodeQueryResponse {

    public static class ResultData {

        public final ResourceType resourceType;
        public final String resourceLink;

        public ResultData(@JsonProperty("resource-type") ResourceType resourceType,
                          @JsonProperty("resource-link") String resourceLink) {
            this.resourceType = resourceType;
            this.resourceLink = resourceLink;
        }
    }

    public final List<ResultData> resultData;

    public AaiNodeQueryResponse(@JsonProperty("result-data") List<ResultData> resultData) {
        this.resultData = resultData;
    }
}
