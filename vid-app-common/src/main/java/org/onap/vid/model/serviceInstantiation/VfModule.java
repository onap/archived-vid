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
import org.onap.vid.job.JobAdapter;
import org.onap.vid.mso.model.ModelInfo;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * The Class VfModule.
 */
@JsonInclude(NON_NULL)
public class VfModule extends BaseResource implements JobAdapter.AsyncJobRequest {

	@JsonInclude(NON_NULL) private final String volumeGroupInstanceName;
	private boolean usePreload;
	private Map<String, String> supplementaryParams;

	public VfModule( @JsonProperty("modelInfo") ModelInfo modelInfo,
                     @JsonProperty("instanceName") String instanceName,
                     @JsonProperty("volumeGroupName") String volumeGroupInstanceName,
					 @JsonProperty("action") String action,
                     @JsonProperty("lcpCloudRegionId") String lcpCloudRegionId,
                     @JsonProperty("legacyRegion") String legacyRegion,
                     @JsonProperty("tenantId") String tenantId,
                     @JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
                     @JsonProperty("supplementaryFileContent") Map<String, String> supplementaryParams,
                     @JsonProperty("rollbackOnFailure") boolean rollbackOnFailure,
                     @JsonProperty("sdncPreLoad") boolean usePreload,
					 @JsonProperty("instanceId") String instanceId) {
		super(modelInfo, instanceName, action, lcpCloudRegionId, legacyRegion, tenantId, instanceParams, rollbackOnFailure, instanceId);
		this.volumeGroupInstanceName = volumeGroupInstanceName;
		this.usePreload = usePreload;
		this.supplementaryParams = supplementaryParams;
	}

	public String getVolumeGroupInstanceName() {
		return volumeGroupInstanceName;
	}

	public boolean isUsePreload() {
		return usePreload;
	}

	public Map<String, String> getSupplementaryParams() {
		return supplementaryParams;
	}

	@Override
	protected String getModelType() {
		return "vfModule";
	}


}