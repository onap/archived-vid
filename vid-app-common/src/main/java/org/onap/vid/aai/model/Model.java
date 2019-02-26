/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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
