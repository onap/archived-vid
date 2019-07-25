/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.model.errorReport;

public class ReportCreationParameters {
	private String requestId;
	private String serviceUuid;

	public ReportCreationParameters() {}

	public ReportCreationParameters(String requestId, String serviceUuid) {
		this.requestId = requestId;
		this.serviceUuid = serviceUuid;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getServiceUuid() {
		return serviceUuid;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setServiceUuid(String serviceUuid) {
		this.serviceUuid = serviceUuid;
	}
}