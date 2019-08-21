/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

package org.onap.vid.changeManagement;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"modelType",
"modelInvariantId",
"modelVersionId",
"modelName",
"modelVersion",
"modelCustomizationName",
"modelCustomizationId"
})
public class ModelInfo {
	
	@JsonProperty("modelType")
	private String modelType;
	@JsonProperty("modelInvariantId")
	private String modelInvariantId;
	@JsonProperty("modelVersionId")
	private String modelVersionId;
	@JsonProperty("modelName")
	private String modelName;
	@JsonProperty("modelVersion")
	private String modelVersion;
	@JsonProperty("modelCustomizationName")
	private String modelCustomizationName;
	@JsonProperty("modelCustomizationId")
	private String modelCustomizationId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	public ModelInfo(){

	}

	public ModelInfo(org.onap.vid.mso.model.ModelInfo modelInfo){
		this.setModelType(modelInfo.getModelType().toString());
		this.setModelInvariantId(modelInfo.getModelInvariantId());
		this.setModelVersionId(modelInfo.getModelNameVersionId());
		this.setModelName(modelInfo.getModelName());
		this.setModelVersion(modelInfo.getModelVersion());
		this.setModelCustomizationId(modelInfo.getModelCustomizationId());
		this.setModelVersionId(modelInfo.getModelVersionId());
	}

	@JsonProperty("modelType")
	public String getModelType() {
	return modelType;
	}

	@JsonProperty("modelType")
	public void setModelType(String modelType) {
	this.modelType = modelType;
	}

	@JsonProperty("modelInvariantId")
	public String getModelInvariantId() {
	return modelInvariantId;
	}

	@JsonProperty("modelInvariantId")
	public void setModelInvariantId(String modelInvariantId) {
	this.modelInvariantId = modelInvariantId;
	}

	@JsonProperty("modelVersionId")
	public String getModelVersionId() {
	return modelVersionId;
	}

	@JsonProperty("modelVersionId")
	public void setModelVersionId(String modelVersionId) {
	this.modelVersionId = modelVersionId;
	}

	@JsonProperty("modelName")
	public String getModelName() {
	return modelName;
	}

	@JsonProperty("modelName")
	public void setModelName(String modelName) {
	this.modelName = modelName;
	}

	@JsonProperty("modelVersion")
	public String getModelVersion() {
	return modelVersion;
	}

	@JsonProperty("modelVersion")
	public void setModelVersion(String modelVersion) {
	this.modelVersion = modelVersion;
	}

	@JsonProperty("modelCustomizationName")
	public String getModelCustomizationName() {
	return modelCustomizationName;
	}

	@JsonProperty("modelCustomizationName")
	public void setModelCustomizationName(String modelCustomizationName) {
	this.modelCustomizationName = modelCustomizationName;
	}

	@JsonProperty("modelCustomizationId")
	public String getModelCustomizationId() {
	return modelCustomizationId;
	}

	@JsonProperty("modelCustomizationId")
	public void setModelCustomizationId(String modelCustomizationId) {
	this.modelCustomizationId = modelCustomizationId;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}




}
