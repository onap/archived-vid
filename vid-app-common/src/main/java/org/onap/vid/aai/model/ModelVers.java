package org.onap.vid.aai.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by moriya1 on 15/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelVers {

    @JsonProperty("model-ver")
    private List<ModelVer> modelVer;

    @JsonProperty("model-ver")
    public List<ModelVer> getModelVer() {
        return modelVer;
    }

    @JsonProperty("model-ver")
    public void setModelVer(List<ModelVer> modelVer) {
        this.modelVer = modelVer;
    }


}
