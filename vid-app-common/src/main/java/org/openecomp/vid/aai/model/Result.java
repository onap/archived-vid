package org.openecomp.vid.aai.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by moriya1 on 15/10/2017.
 */
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
