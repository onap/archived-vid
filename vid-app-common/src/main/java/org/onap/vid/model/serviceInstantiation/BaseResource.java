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

package org.onap.vid.model.serviceInstantiation;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.model.Action;
import org.onap.vid.mso.model.ModelInfo;

public abstract class BaseResource implements JobAdapter.AsyncJobRequest {

	public enum PauseInstantiation {
		afterCompletion
	}

	protected String instanceId;

	protected ModelInfo modelInfo;

	protected String instanceName;

	protected Action action;

	protected String lcpCloudRegionId;

	protected String tenantId;

	protected List<Map<String, String>> instanceParams;

	protected boolean rollbackOnFailure;

	protected String trackById;

	protected Boolean isFailed;

	protected String statusMessage;

	protected Integer position;

	@JsonInclude(NON_NULL)
	protected String originalName; //not used at backend, but stored for fronted

	@JsonInclude(NON_NULL)
	protected final PauseInstantiation pauseInstantiation;

	private static final Map<String, Action> actionStingToEnumMap = ImmutableMap.<String, Action>builder()
			.put("Delete", Action.Delete)
			.put("Create", Action.Create)
			.put("None", Action.None)
			.put("Update_Delete", Action.Delete)
			.put("None_Delete", Action.Delete)
			.put("Resume", Action.Resume)
			.put("Upgrade", Action.Upgrade)
			.put("None_Upgrade", Action.Upgrade)
			.build();


	protected BaseResource(@JsonProperty("modelInfo") ModelInfo modelInfo,
		@JsonProperty("instanceName") String instanceName,
		@JsonProperty("action") String action,
		@JsonProperty("lcpCloudRegionId") String lcpCloudRegionId,
		@JsonProperty("legacyRegion") String legacyRegion,
		@JsonProperty("tenantId") String tenantId,
		@JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
		@JsonProperty("rollbackOnFailure") boolean rollbackOnFailure,
		@JsonProperty("instanceId") String instanceId,
		@JsonProperty("trackById") String trackById,
		@JsonProperty("isFailed") Boolean isFailed,
		@JsonProperty("statusMessage") String statusMessage,
		@JsonProperty("position") Integer position,
		@JsonProperty("pauseInstantiation") PauseInstantiation pauseInstantiation,
		@JsonProperty("originalName") String originalName) {
		this.modelInfo = modelInfo;
		this.modelInfo.setModelType(getModelType());
		this.rollbackOnFailure = rollbackOnFailure;
		this.instanceName = StringUtils.defaultString(instanceName, "");
		this.action = actionStringToEnum(action);
		this.lcpCloudRegionId = StringUtils.isNotEmpty(legacyRegion) ? legacyRegion : lcpCloudRegionId;
		this.tenantId = tenantId;
		this.instanceParams = instanceParams;
		this.instanceId = instanceId;
		this.trackById = trackById;
		this.isFailed = isFailed!= null ? isFailed: false;
		this.statusMessage = statusMessage;
		this.position = position;
		this.pauseInstantiation = pauseInstantiation;
		this.originalName = originalName;
	}

	private Action actionStringToEnum(String actionAsString) {
		return actionStingToEnumMap.get(actionAsString);
	}

	public ModelInfo getModelInfo() {
		return modelInfo;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public Action getAction() {
		return (action == null ? Action.Create : action);
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

	public boolean isRollbackOnFailure() { return rollbackOnFailure; }

	public String getInstanceId() {
		return instanceId;
	}

	protected abstract String getModelType();

	public String getTrackById() {
		return trackById;
	}

	public void setTrackById(String trackById) {
		this.trackById = trackById;
	}

	public Boolean getIsFailed() {
		return isFailed;
	}

	public void setIsFailed(Boolean isFailed) {
		this.isFailed = isFailed;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getOriginalName() {
		return originalName;
	}

	@JsonIgnore
	public abstract Collection<? extends BaseResource> getChildren();

	@JsonIgnore
	public abstract JobType getJobType();

	@Nullable
	public PauseInstantiation getPauseInstantiation() {
		return pauseInstantiation;
	}
}
