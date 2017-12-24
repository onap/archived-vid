package org.openecomp.vid.aai.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by moriya1 on 15/10/2017.
 */
public class Model {


        @JsonProperty("model-invariant-id")
        private String modelInvariantId;
        @JsonProperty("model-type")
        private String modelType;
        @JsonProperty("resource-version")
        private String resourceVersion;
        @JsonProperty("model-vers")
        private ModelVers modelVers;

        @JsonProperty("model-invariant-id")
        public String getModelInvariantId() {
            return modelInvariantId;
        }

        @JsonProperty("model-invariant-id")
        public void setModelInvariantId(String modelInvariantId) {
            this.modelInvariantId = modelInvariantId;
        }

        @JsonProperty("model-type")
        public String getModelType() {
            return modelType;
        }

        @JsonProperty("model-type")
        public void setModelType(String modelType) {
            this.modelType = modelType;
        }

        @JsonProperty("resource-version")
        public String getResourceVersion() {
            return resourceVersion;
        }

        @JsonProperty("resource-version")
        public void setResourceVersion(String resourceVersion) {
            this.resourceVersion = resourceVersion;
        }

        @JsonProperty("model-vers")
        public ModelVers getModelVers() {
            return modelVers;
        }

        @JsonProperty("model-vers")
        public void setModelVers(ModelVers modelVers) {
            this.modelVers = modelVers;
        }

}
