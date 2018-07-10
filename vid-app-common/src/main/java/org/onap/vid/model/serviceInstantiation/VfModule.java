/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.model.serviceInstantiation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.domain.mso.ModelInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * The Class VfModule.
 */
public class VfModule {



	private final ModelInfo modelInfo;

	@JsonInclude(NON_NULL) private final String instanceName;

	private final List<Map<String, String>> instanceParams;
	@JsonInclude(NON_NULL) private final String volumeGroupInstanceName;

	public VfModule(@JsonProperty("modelInfo") ModelInfo modelInfo,
					@JsonProperty("instanceName") String instanceName,
					@JsonProperty(value = "volumeGroupName") String volumeGroupInstanceName,
					@JsonProperty("instanceParams") List<Map<String, String>> instanceParams) {
		this.modelInfo = modelInfo;
		this.modelInfo.setModelType("vfModule");
		this.instanceName = instanceName;
		this.instanceParams = instanceParams;
		this.volumeGroupInstanceName = volumeGroupInstanceName;
	}

	public ModelInfo getModelInfo() {
		return modelInfo;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public String getVolumeGroupInstanceName() {
		return volumeGroupInstanceName;
	}

	public List<Map<String, String>> getInstanceParams() {
		return instanceParams == null ? Collections.emptyList() : instanceParams;
	}

}