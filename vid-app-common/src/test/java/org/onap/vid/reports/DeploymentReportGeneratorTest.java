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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.model.Service;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.services.VidService;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeploymentReportGeneratorTest {

	@Mock
	private MsoBusinessLogic msoBusinessLogic;
	@Mock
	private VidService vidService;

	@InjectMocks
	private DeploymentReportGenerator deploymentReportGenerator;

	@Test
	public void shouldReturnTrueIfConditionsOfGenerationAreFulfilled() {
		//given
		ReportCreationParameters parameters = new ReportCreationParameters();
		parameters.setRequestId("request_id");
		parameters.setServiceUuid("service_uuid");

		//when
		boolean actualResult = deploymentReportGenerator.canGenerate(parameters);

		//then
		assertThat(actualResult).isTrue();
	}

	@Test
	public void shouldGetOrchestrationRequestFromMso() {
		//given
		final String requestId = "request_id";
		MsoResponseWrapper expectedOrchestrationRequest = mock(MsoResponseWrapper.class);

		when(msoBusinessLogic.getOrchestrationRequest(requestId)).thenReturn(expectedOrchestrationRequest);

		//when
		MsoResponseWrapper actualOrchestrationRequest =
				deploymentReportGenerator.getOrchestrationRequestFromMso(requestId);

		//then
		assertThat(actualOrchestrationRequest).isEqualTo(expectedOrchestrationRequest);
	}

	@Test
	public void shouldGetServiceDetails() throws AsdcCatalogException {
		//given
		final String SERVICE_UUID = "service_uuid";
		ServiceModel serviceModel = mock(ServiceModel.class);
		Service expectedService = new Service();
		ImmutableMap<String, Object> expectedServiceDetails = ImmutableMap.of("details", expectedService);

		when(vidService.getService(SERVICE_UUID)).thenReturn(serviceModel);
		when(serviceModel.getService()).thenReturn(expectedService);

		//when
		Map<String, Object> actualServiceDetails = deploymentReportGenerator.getServiceDetails(SERVICE_UUID);

		//then
		assertThat(actualServiceDetails).isEqualTo(expectedServiceDetails);
	}

	@Test
	public void shouldGetNullPointerExceptionInfoInMapWhenWrongUuidIsGiven() {
		//given
		final String NOT_EXISTING_UUID = "8ad001d0-1111-2222-3333-123412341234";
		NullPointerException expectedException = new NullPointerException("msg");

		Map<String, Object> expectedResult = ImmutableMap.<String, Object>builder()
				.put("message", "Service details for given uuid were not found")
				.put("exception", expectedException.toString())
				.put("serviceUuid", NOT_EXISTING_UUID)
				.build();

		//when
		Map<String, Object> actualResult =
				deploymentReportGenerator.generateServiceDetailsExceptionResponse(NOT_EXISTING_UUID, expectedException);

		//then
		assertThat(actualResult).isEqualTo(expectedResult);
	}
}