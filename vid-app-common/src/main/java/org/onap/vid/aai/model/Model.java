package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by moriya1 on 15/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Model {

        private String modelInvariantId;
        private String modelType;
        private String resourceVersion;
        private ModelVers modelVers;

        public String getModelInvariantId() {
            return modelInvariantId;
        }

        @JsonAlias("model-invariant-id")
        public void setModelInvariantId(String modelInvariantId) {
            this.modelInvariantId = modelInvariantId;
        }

        public String getModelType() {
            return modelType;
        }

        @JsonAlias("model-type")
        public void setModelType(String modelType) {
            this.modelType = modelType;
        }

        public String getResourceVersion() {
            return resourceVersion;
        }

        @JsonAlias("resource-version")
        public void setResourceVersion(String resourceVersion) {
            this.resourceVersion = resourceVersion;
        }

        public ModelVers getModelVers() {
            return modelVers;
        }

        @JsonAlias("model-vers")
        public void setModelVers(ModelVers modelVers) {
            this.modelVers = modelVers;
        }

}
