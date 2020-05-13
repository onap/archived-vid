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


import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.mso.model.ModelInfo;

/**
 * The Class VNF.
 */
public class Vnf extends BaseResource implements JobAdapter.AsyncJobRequest {

	private final String productFamilyId;

	private final String platformName;

	private final String lineOfBusiness;

	private final Map<String, Map<String, VfModule>> vfModules;

	public Vnf(@JsonProperty("modelInfo") ModelInfo modelInfo,
		@JsonProperty("productFamilyId") String productFamilyId,
		@JsonProperty("instanceName") String instanceName,
		@JsonProperty("action") String action,
		@JsonProperty("platformName") String platformName,
		@JsonProperty("lcpCloudRegionId") String lcpCloudRegionId,
		@JsonProperty("legacyRegion") String legacyRegion,
		@JsonProperty("tenantId") String tenantId,
		@JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
		@JsonProperty("lineOfBusinessName") String lineOfBusiness,
		@JsonProperty("rollbackOnFailure") boolean rollbackOnFailure,
		@JsonProperty("instanceId") String instanceId,
		@JsonProperty("vfModules") Map<String, Map<String, VfModule>> vfModules,
		@JsonProperty("trackById") String trackById,
		@JsonProperty("isFailed") Boolean isFailed,
		@JsonProperty("statusMessage") String statusMessage,
		@JsonProperty("position") Integer position,
		@JsonProperty("originalName") String originalName) {

		super(modelInfo, instanceName, action, lcpCloudRegionId, legacyRegion, tenantId, instanceParams, rollbackOnFailure, instanceId, trackById, isFailed, statusMessage,
			position, null, originalName);
		this.productFamilyId = productFamilyId;
		this.platformName = platformName;
		this.lineOfBusiness = lineOfBusiness;
		this.vfModules = vfModules==null ? Collections.emptyMap() : vfModules;
	}

	public String getProductFamilyId() {
		return productFamilyId;
	}

	public String getPlatformName() {
		return platformName;
	}

	public String getLineOfBusiness() {
		return lineOfBusiness;
	}

	public  Map<String, Map<String, VfModule>> getVfModules() {
		return vfModules;
	}

	@Override
	protected String getModelType() {
		return "vnf";
	}

	@Override
	public Collection<BaseResource> getChildren() {
		return getVfModules().values().stream()
				.filter(Objects::nonNull)
				.flatMap(x->x.values().stream())
				.collect(toList());
	}

	@Override
	public JobType getJobType() {
		return JobType.VnfInstantiation;
	}
}
