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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.UserParamNameAndValue;

/**
 * The Class VfModule.
 */
@JsonInclude(NON_NULL)
public class VfModule extends BaseResource implements JobAdapter.AsyncJobRequest {

	@JsonInclude(NON_NULL) @JsonProperty("volumeGroupName") private final String volumeGroupInstanceName;
	@JsonInclude(NON_NULL) @JsonProperty("sdncPreLoad") private Boolean usePreload;
	private List<UserParamNameAndValue> supplementaryParams;

	@JsonInclude(NON_NULL)
	private final Boolean retainVolumeGroups;

	@JsonInclude(NON_NULL)
	private Boolean retainAssignments;

	public VfModule(@JsonProperty("modelInfo") ModelInfo modelInfo,
		@JsonProperty("instanceName") String instanceName,
		@JsonProperty("volumeGroupName") @JsonAlias("volumeGroupInstanceName") String volumeGroupInstanceName,
		@JsonProperty("action") String action,
		@JsonProperty("lcpCloudRegionId") String lcpCloudRegionId,
		@JsonProperty("legacyRegion") String legacyRegion,
		@JsonProperty("tenantId") String tenantId,
		@JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
		@JsonProperty("supplementaryFileContent") List<UserParamNameAndValue> supplementaryParams,
		@JsonProperty("rollbackOnFailure") boolean rollbackOnFailure,
		@JsonProperty("sdncPreLoad") @JsonAlias("usePreload") Boolean usePreload,
		@JsonProperty("instanceId") String instanceId,
		@JsonProperty("trackById") String trackById,
		@JsonProperty("isFailed") Boolean isFailed,
		@JsonProperty("statusMessage") String statusMessage,
		@Nullable @JsonProperty("retainAssignments") Boolean retainAssignments,
		@Nullable @JsonProperty("retainVolumeGroups") Boolean retainVolumeGroups,
		@JsonProperty("position") Integer position,
		@JsonProperty("pauseInstantiation") PauseInstantiation pauseInstantiation,
		@JsonProperty("originalName") String originalName) {
		super(modelInfo, instanceName, action, lcpCloudRegionId, legacyRegion, tenantId, instanceParams, rollbackOnFailure, instanceId, trackById, isFailed, statusMessage,
			position, pauseInstantiation, originalName);
		this.volumeGroupInstanceName = volumeGroupInstanceName;
		this.usePreload = usePreload;
		this.supplementaryParams = supplementaryParams;
		this.retainAssignments = retainAssignments;
		this.retainVolumeGroups = retainVolumeGroups;
	}

	public String getVolumeGroupInstanceName() {
		return volumeGroupInstanceName;
	}

	public Boolean isUsePreload() {

		return usePreload;
	}

	public List<UserParamNameAndValue> getSupplementaryParams() {
		return supplementaryParams;
	}

	@Override
	protected String getModelType() {
		return "vfModule";
	}

	@Override
	public Collection<BaseResource> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public JobType getJobType() {
		return JobType.VfmoduleInstantiation;
	}

	@Nullable
	public Boolean isRetainAssignments() {
		return retainAssignments;
	}

	@Nullable
	public Boolean isRetainVolumeGroups() {
		return retainVolumeGroups;
	}

	public VfModule cloneWith(ModelInfo modelInfo, boolean shouldRollbackOnFailure) {
		return new VfModule(
				modelInfo,
				this.getInstanceName(),
				this.getVolumeGroupInstanceName(),
				this.getAction().toString(),
				this.getLcpCloudRegionId(),
				this.getLcpCloudRegionId(),
				this.getTenantId(),
				this.getInstanceParams(),
				this.getSupplementaryParams(),
				shouldRollbackOnFailure,
				this.isUsePreload(),
				this.getInstanceId(),
				this.getTrackById(),
				this.getIsFailed(),
				this.getStatusMessage(),
				this.isRetainAssignments(),
				this.isRetainVolumeGroups(),
				this.getPosition(),
				this.getPauseInstantiation(),
				this.getOriginalName()
		);
	}

	public VfModule cloneWith(String lcpCloudRegionId, String tenantId) {
		return new VfModule(
			this.getModelInfo(),
			this.getInstanceName(),
			this.getVolumeGroupInstanceName(),
			this.getAction().toString(),
			lcpCloudRegionId,
			lcpCloudRegionId,
			tenantId,
			this.getInstanceParams(),
			this.getSupplementaryParams(),
			this.isRollbackOnFailure(),
			this.isUsePreload(),
			this.getInstanceId(),
			this.getTrackById(),
			this.getIsFailed(),
			this.getStatusMessage(),
			this.isRetainAssignments(),
			this.isRetainVolumeGroups(),
			this.getPosition(),
			this.getPauseInstantiation(),
			this.getOriginalName()
		);
	}
}