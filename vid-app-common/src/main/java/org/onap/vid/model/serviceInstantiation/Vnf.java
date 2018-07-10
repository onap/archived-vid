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


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.domain.mso.ModelInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The Class VNF.
 */
public class Vnf  {
	private final ModelInfo modelInfo;

	private final String productFamilyId;

	private final String instanceName;

	private final String platformName;

	private final String lcpCloudRegionId;

	private final String tenantId;

	private final Boolean isUserProvidedNaming;

	private final List<Map<String, String>> instanceParams;

	private final String lineOfBusiness;


	private final Map<String, Map<String, VfModule>> vfModules;

	public Vnf(@JsonProperty("modelInfo") ModelInfo modelInfo,
			   @JsonProperty("productFamilyId") String productFamilyId,
			   @JsonProperty("instanceName") String instanceName,
			   @JsonProperty("isUserProvidedNaming") Boolean isUserProvidedNaming,
			   @JsonProperty("platformName") String platformName,
			   @JsonProperty("lcpCloudRegionId") String lcpCloudRegionId,
			   @JsonProperty("tenantId") String tenantId,
			   @JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
			   @JsonProperty("lineOfBusinessName") String lineOfBusiness,
			   @JsonProperty("vfModules") Map<String, Map<String, VfModule>> vfModules) {
		this.modelInfo = modelInfo;
		this.modelInfo.setModelType("vnf");
		this.productFamilyId = productFamilyId;
		this.instanceName = instanceName;
		this.isUserProvidedNaming = isUserProvidedNaming;
		this.platformName = platformName;
		this.lcpCloudRegionId = lcpCloudRegionId;
		this.tenantId = tenantId;
		this.instanceParams = instanceParams;
		this.vfModules = vfModules;
		this.lineOfBusiness = lineOfBusiness;
	}

	public ModelInfo getModelInfo() {
		return modelInfo;
	}

	public String getProductFamilyId() {
		return productFamilyId;
	}

	public String getInstanceName() {
		return instanceName;
	}

	@JsonProperty("isUserProvidedNaming")
	public Boolean isUserProvidedNaming() {
		return isUserProvidedNaming;
	}

	public String getPlatformName() {
		return platformName;
	}

	public String getLcpCloudRegionId() {
		return lcpCloudRegionId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public List<Map<String, String>> getInstanceParams() {
		return instanceParams == null ? Collections.emptyList() : instanceParams;
	}

	public  Map<String, Map<String, VfModule>> getVfModules() {
		return vfModules;
	}

	public String getLineOfBusiness() {
		return lineOfBusiness;
	}
}
