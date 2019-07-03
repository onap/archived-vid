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

package org.onap.vid.reports;

import com.google.common.collect.ImmutableMap;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.services.VidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeploymentReportGenerator implements ReportGenerator {

	private final VidService vidService;
	private final MsoBusinessLogic msoBusinessLogic;

	@Autowired
	public DeploymentReportGenerator(VidService vidService, MsoBusinessLogic msoBusinessLogic) {
		this.vidService = vidService;
		this.msoBusinessLogic = msoBusinessLogic;
	}

	@Override
	public Map<String, Object> apply(HttpServletRequest request, ReportCreationParameters creationParameters) {
		return ImmutableMap.<String, Object>builder()
				.put("serviceInstanceInfo", getOrchestrationRequestFromMso(creationParameters.getRequestId()))
				.put("serviceDetails", getServiceDetails(creationParameters.getServiceUuid()))
				.build();
	}

	@Override
	public boolean canGenerate(ReportCreationParameters creationParameters) {
		return creationParameters.getRequestId() != null && creationParameters.getServiceUuid() != null;
	}

	MsoResponseWrapper getOrchestrationRequestFromMso(String requestId) {
		return msoBusinessLogic.getOrchestrationRequest(requestId);
	}

	Map<String, Object> getServiceDetails(String serviceUuid) {
		Map<String, Object> serviceDetails;
		try {
			org.onap.vid.model.Service serviceModel = vidService.getService(serviceUuid).getService();
			serviceDetails = ImmutableMap.of("details", serviceModel);
		} catch (AsdcCatalogException | NullPointerException e) {
			serviceDetails = generateServiceDetailsExceptionResponse(serviceUuid, e);
		}
		return serviceDetails;
	}

	Map<String, Object> generateServiceDetailsExceptionResponse(String serviceUuid, Exception e) {
		Map<String, Object> result = new HashMap<>();
		if (e.getClass() == NullPointerException.class) {
			result.put("message", "Service details for given uuid were not found");
		}
		result.put("exception", e.toString());
		result.put("serviceUuid", serviceUuid);
		return result;
	}
}
