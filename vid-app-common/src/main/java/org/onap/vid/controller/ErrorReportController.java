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

import org.jetbrains.annotations.NotNull;
import org.onap.portalsdk.core.controller.UnRestrictedBaseController;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.reports.ReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

@RestController
public class ErrorReportController extends UnRestrictedBaseController {

	private final List<ReportGenerator> providers;

//	@Autowired
//	public ErrorReportController(BasicReportGenerator basicReportGenerator, DeploymentReportGenerator deploymentReportGenerator) {
//		providers = ImmutableList.of(basicReportGenerator, deploymentReportGenerator);
//	}

	@Autowired
	public ErrorReportController(List<ReportGenerator> reportGenerators) {
		providers = reportGenerators;
	}

	@PostMapping(value = "error-report")
	public Map<String, Object> getErrorReport(HttpServletRequest request,
	                                          @RequestBody ReportCreationParameters creationParameters) {
		return generateReportsData(request, creationParameters);
	}

	@NotNull
	HashMap<String, Object> generateReportsData(HttpServletRequest request, ReportCreationParameters creationParameters) {
		return providers
				.parallelStream()
				.filter(provider -> provider.canGenerate(creationParameters))
				.map(provider -> provider.apply(request, creationParameters))
				.map(Map::entrySet)
				.flatMap(Collection::stream)
				.reduce(new HashMap<>(), this::putAndGet, concatenateMap());
	}

	@NotNull
	private HashMap<String, Object> putAndGet(HashMap<String, Object> map, Map.Entry<String, Object> entry) {
		map.put(entry.getKey(), entry.getValue());
		return map;
	}

	@NotNull
	private BinaryOperator<HashMap<String, Object>> concatenateMap() {
		return (map1, map2) -> {
			map1.putAll(map2);
			return map1;
		};
	}

}