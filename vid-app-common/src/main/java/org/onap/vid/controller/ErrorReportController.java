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

package org.onap.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.onap.portalsdk.core.controller.UnRestrictedBaseController;
import org.onap.vid.services.ErrorReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.onap.portalsdk.core.util.SystemProperties.ECOMP_REQUEST_ID;

@RestController
@RequestMapping(value = "error-report")
public class ErrorReportController extends UnRestrictedBaseController {

	private ErrorReportService errorReportService;

	@Autowired
	public ErrorReportController(ErrorReportService errorReportService) {
		this.errorReportService = errorReportService;
	}


	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> viewEditServiceInstanceErrorReport(HttpServletRequest request) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Map<String, Object> report = new HashMap<>();

		report.put("X-ECOMP-RequestID", request.getHeader(ECOMP_REQUEST_ID));
		report.put("aaiGetFullSubscribers", errorReportService.getFullSubscriberList(request));
		report.put("userID", errorReportService.getUserID(request));
		report.put("commitInfo", errorReportService.getCommitInfo());
		report.put("probeInfo", errorReportService.getProbe());
		report.put("fullSubscribersList-", errorReportService.getSdcServiceModelsInfo());

		return new ResponseEntity<>(mapper.writeValueAsString(report), HttpStatus.OK);
	}

	@GetMapping(value = "/{requestId}/{serviceUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deploySdcServiceInstanceErrorReporting(HttpServletRequest request,
	                                                                     @PathVariable String requestId,
	                                                                     @PathVariable String serviceUuid) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Map<String, Object> report = new HashMap<>();

		report.put("X-ECOMP-RequestID", request.getHeader(ECOMP_REQUEST_ID));
		report.put("aaiGetFullSubscribers", errorReportService.getFullSubscriberList(request));
		report.put("userID", errorReportService.getUserID(request));
		report.put("commitInfo", errorReportService.getCommitInfo());
		report.put("probeInfo", errorReportService.getProbe());

		report.put("serviceInstanceInfo", errorReportService.getOrchestrationRequest(requestId));
		report.put("serviceDetails", errorReportService.getServiceDetails(serviceUuid));

		return new ResponseEntity<>(mapper.writeValueAsString(report), HttpStatus.OK);
	}
}