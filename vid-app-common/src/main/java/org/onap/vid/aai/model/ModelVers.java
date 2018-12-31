package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @JsonAlias("model-ver")
    public void setModelVer(List<ModelVer> modelVer) {
        this.modelVer = modelVer;
    }


}
