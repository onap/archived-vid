package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by moriya1 on 15/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    @JsonProperty("model")
    private Model model;

    @JsonProperty("model")
    public Model getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(Model model) {
        this.model = model;
    }



}
