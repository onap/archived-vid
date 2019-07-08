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

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.reports.BasicReportGenerator;
import org.onap.vid.reports.DeploymentReportGenerator;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ErrorReportControllerTest {

	private BasicReportGenerator basicReportGenerator;
	private DeploymentReportGenerator deploymentReportGenerator;
	private HttpServletRequest httpServletRequest;

	private ErrorReportController errorReportController;

	@Before
	public void setUp() {
		basicReportGenerator = mock(BasicReportGenerator.class);
		deploymentReportGenerator = mock(DeploymentReportGenerator.class);
		httpServletRequest = mock(HttpServletRequest.class);

		errorReportController
				= new ErrorReportController(Arrays.asList(basicReportGenerator, deploymentReportGenerator));
	}

	@Test
	public void shouldGenerateBasicReportDataWhenNoParameters() {
		//given
		ReportCreationParameters parameters = new ReportCreationParameters();

		ImmutableMap<String, Object> expectedReport = ImmutableMap.<String, Object>builder()
						.put("X-ECOMP-RequestID", "request_id")
						.put("X-ECOMP-RequestID1", "request_id1")
						.put("X-ECOMP-RequestID2", "request_id2")
						.put("X-ECOMP-RequestID3", "request_id3")
						.build();

		when(basicReportGenerator.apply(httpServletRequest, parameters)).thenReturn(expectedReport);
		when(basicReportGenerator.canGenerate(parameters)).thenReturn(true);
		when(deploymentReportGenerator.canGenerate(parameters)).thenReturn(false);

		//when
		Map<String, Object> actualReport = errorReportController.generateReportsData(httpServletRequest, parameters);

		//then
		assertThat(actualReport).isEqualTo(expectedReport);
	}

	@Test
	public void shouldGenerateDeploymentReportDataWhenSpecificParameters() {
		//given
		ReportCreationParameters parameters =
				new ReportCreationParameters("request_id", "service_uuid");

		ImmutableMap<String, Object> basicReport = ImmutableMap.<String, Object>builder()
				.put("X-ECOMP-RequestID", "request_id")
				.put("X-ECOMP-RequestID1", "request_id1")
				.build();

		ImmutableMap<String, Object> extendedReport = ImmutableMap.<String, Object>builder()
				.put("serviceInstanceInfo", "serviceInstanceInfoVal")
				.put("serviceInstanceInfo1", "serviceInstanceInfoVal1")
				.build();

		HashMap<String, Object> expectedReport = new HashMap<>(basicReport);
		expectedReport.putAll(extendedReport);

		when(basicReportGenerator.apply(httpServletRequest, parameters)).thenReturn(basicReport);
		when(deploymentReportGenerator.apply(httpServletRequest, parameters)).thenReturn(extendedReport);
		when(basicReportGenerator.canGenerate(parameters)).thenReturn(true);
		when(deploymentReportGenerator.canGenerate(parameters)).thenReturn(true);

		//when
		Map<String, Object> actualReport = errorReportController.generateReportsData(httpServletRequest, parameters);

		//then
		assertThat(actualReport).isEqualTo(expectedReport);
	}
}