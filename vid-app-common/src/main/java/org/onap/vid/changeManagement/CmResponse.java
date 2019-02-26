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

package org.onap.vid.changeManagement;

public class CmResponse {
	
	public String orchestratorRequestId;
	public String serviceInstanceId;
	public String vnfInstanceId;
	public String vnfName;
	
	public CmResponse(String vnfName){
		this.orchestratorRequestId = "Request Id";
		this.serviceInstanceId = "Service instance Id";
		this.vnfInstanceId = "Vnf instance Id";
		this.vnfName = vnfName;
	}

}
