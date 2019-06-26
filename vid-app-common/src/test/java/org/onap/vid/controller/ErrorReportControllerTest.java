package org.onap.vid.controller;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.reports.BasicReportGenerator;
import org.onap.vid.reports.DeploymentReportGenerator;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ErrorReportControllerTest {

	@Mock
	private BasicReportGenerator basicReportGenerator;
	@Mock
	private DeploymentReportGenerator deploymentReportGenerator;
	@Mock
	private HttpServletRequest httpServletRequest;

	@InjectMocks
	private ErrorReportController errorReportController;

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