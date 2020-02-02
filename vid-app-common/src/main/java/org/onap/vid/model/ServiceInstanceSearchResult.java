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

package org.onap.vid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.roles.WithPermissionPropertiesOwningEntity;
import org.onap.vid.roles.WithPermissionPropertiesSubscriberAndServiceType;

public class ServiceInstanceSearchResult
	implements WithPermissionPropertiesSubscriberAndServiceType, WithPermissionPropertiesOwningEntity {

	private final String SUBSCRIBER_ID_FRONTEND_ALIAS = "globalCustomerId";

	private String serviceInstanceId;

	private String subscriberId;

	private String serviceType;

	private String serviceInstanceName;

	private String subscriberName;

	private String aaiModelInvariantId;

	private String aaiModelVersionId;

	private String owningEntityId;

	private boolean isPermitted;

	public ServiceInstanceSearchResult(){
	}
	
	public ServiceInstanceSearchResult(String serviceInstanceId, String subscriberId, String serviceType,
		String serviceInstanceName, String subscriberName, String aaiModelInvariantId,
		String aaiModelVersionId, String owningEntityId, boolean isPermitted) {
		this.serviceInstanceId = serviceInstanceId;
		this.subscriberId = subscriberId;
		this.serviceType = serviceType;
		this.serviceInstanceName = serviceInstanceName;
		this.subscriberName = subscriberName;
		this.aaiModelInvariantId = aaiModelInvariantId;
		this.aaiModelVersionId = aaiModelVersionId;
		this.owningEntityId = owningEntityId;
		this.isPermitted = isPermitted;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	@Override
	@JsonProperty(SUBSCRIBER_ID_FRONTEND_ALIAS)
	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	@Override
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceInstanceName() {
		return serviceInstanceName;
	}

	public void setServiceInstanceName(String serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public String getAaiModelInvariantId() {
		return aaiModelInvariantId;
	}

	public void setAaiModelInvariantId(String aaiModelInvariantId) {
		this.aaiModelInvariantId = aaiModelInvariantId;
	}

	public String getAaiModelVersionId() {
		return aaiModelVersionId;
	}

	public void setAaiModelVersionId(String aaiModelVersionId) {
		this.aaiModelVersionId = aaiModelVersionId;
	}

	@Override
	public String getOwningEntityId() {
		return owningEntityId;
	}

	public void setOwningEntityId(String owningEntityId) {
		this.owningEntityId = owningEntityId;
	}

	public boolean getIsPermitted() {
		return isPermitted;
	}

	public void setIsPermitted(boolean isPermitted) {
		this.isPermitted = isPermitted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ServiceInstanceSearchResult that = (ServiceInstanceSearchResult) o;

		return StringUtils.equals(serviceInstanceId, that.serviceInstanceId);
	}

	@Override
	public int hashCode() {
		return serviceInstanceId != null ? serviceInstanceId.hashCode() : 0;
	}
}
