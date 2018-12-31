package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by moriya1 on 15/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelVers {

    private List<ModelVer> modelVer;

    public List<ModelVer> getModelVer() {
        return modelVer;
    }

    @JsonProperty("model-ver")
    public void setJsonModelVer(List<ModelVer> modelVer) {
        this.modelVer = modelVer;
    }


}
