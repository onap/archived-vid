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

package org.onap.vid.model;

public class ServiceInstanceSearchResult {

	private String serviceInstanceId;

	private String globalCustomerId;

	private String serviceType;

	private String serviceInstanceName;

	private String subscriberName;

	private String aaiModelInvariantId;

	private String aaiModelVersionId;

	private boolean isPermitted;

	public ServiceInstanceSearchResult(){

	}
	public ServiceInstanceSearchResult(String serviceInstanceId, String globalCustomerId, String serviceType,
									   String serviceInstanceName, String subscriberName, String aaiModelInvariantId,
									   String aaiModelVersionId, boolean isPermitted) {
		this.serviceInstanceId = serviceInstanceId;
		this.globalCustomerId = globalCustomerId;
		this.serviceType = serviceType;
		this.serviceInstanceName = serviceInstanceName;
		this.subscriberName = subscriberName;
		this.aaiModelInvariantId = aaiModelInvariantId;
		this.aaiModelVersionId = aaiModelVersionId;
		this.isPermitted = isPermitted;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public String getGlobalCustomerId() {
		return globalCustomerId;
	}

	public void setGlobalCustomerId(String globalCustomerId) {
		this.globalCustomerId = globalCustomerId;
	}

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

	public boolean getIsPermitted() {
		return isPermitted;
	}

	public void setIsPermitted(boolean isPermitted) {
		this.isPermitted = isPermitted;
	}

	@Override
	public boolean equals(Object other){
		if (other instanceof ServiceInstanceSearchResult) {
			ServiceInstanceSearchResult serviceInstanceSearchResultOther = (ServiceInstanceSearchResult) other;
			if (this.getServiceInstanceId().equals(serviceInstanceSearchResultOther.getServiceInstanceId())) {
				return true;
			}
		}
		return false;

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + serviceInstanceId.hashCode();
		return result;
	}
}