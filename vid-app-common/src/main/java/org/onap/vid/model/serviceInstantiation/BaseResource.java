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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.model.Action;
import org.onap.vid.mso.model.ModelInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseResource implements JobAdapter.AsyncJobRequest {

	protected final String instanceId;
	protected ModelInfo modelInfo;

	protected String instanceName;

	protected final Action action;

	protected String lcpCloudRegionId;

	protected String tenantId;

	protected List<Map<String, String>> instanceParams;

	protected boolean rollbackOnFailure;

	private static final Map<String, Action> actionStingToEnumMap = ImmutableMap.of(
			"Delete", Action.Delete,
			"Create", Action.Create,
			"None", Action.None,
			"Update_Delete", Action.Delete,
			"None_Delete", Action.Delete
	);


	protected BaseResource(@JsonProperty("modelInfo") ModelInfo modelInfo,
						   @JsonProperty("instanceName") String instanceName,
						   @JsonProperty("action") String action,
						   @JsonProperty("lcpCloudRegionId") String lcpCloudRegionId,
						   @JsonProperty("legacyRegion") String legacyRegion,
						   @JsonProperty("tenantId") String tenantId,
						   @JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
						   @JsonProperty("rollbackOnFailure") boolean rollbackOnFailure,
						   @JsonProperty("instanceId") String instanceId) {
		this.modelInfo = modelInfo;
		this.modelInfo.setModelType(getModelType());
		this.rollbackOnFailure = rollbackOnFailure;
		this.instanceName = StringUtils.defaultString(instanceName, "");;
		this.action = actionStringToEnum(action);
		this.lcpCloudRegionId = StringUtils.isNotEmpty(legacyRegion) ? legacyRegion : lcpCloudRegionId;
		this.tenantId = tenantId;
		this.instanceParams = instanceParams;
		this.instanceId = instanceId;
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
		return action;
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
}
