package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

        @JsonProperty("model-invariant-id")
        public void setJsonModelInvariantId(String modelInvariantId) {
            this.modelInvariantId = modelInvariantId;
        }

        public String getModelType() {
            return modelType;
        }

        @JsonProperty("model-type")
        public void setJsonModelType(String modelType) {
            this.modelType = modelType;
        }

        public String getResourceVersion() {
            return resourceVersion;
        }

        @JsonProperty("resource-version")
        public void setJsonResourceVersion(String resourceVersion) {
            this.resourceVersion = resourceVersion;
        }

        public ModelVers getModelVers() {
            return modelVers;
        }

        @JsonProperty("model-vers")
        public void setJsonModelVers(ModelVers modelVers) {
            this.modelVers = modelVers;
        }

}
