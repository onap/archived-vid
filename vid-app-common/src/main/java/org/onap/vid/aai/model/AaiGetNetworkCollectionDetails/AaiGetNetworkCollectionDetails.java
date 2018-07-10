package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AaiGetNetworkCollectionDetails {

    public AaiGetNetworkCollectionDetails(){
        results = new Result();
    }
    @JsonProperty("results")
    private Result results = null;

    @JsonProperty("results")
    public Result getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(Result results) {
        this.results = results;
    }
}
