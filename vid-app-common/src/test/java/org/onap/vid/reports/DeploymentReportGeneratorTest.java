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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.services.VidService;

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
		final String serviceUuid = "service_uuid";
		ServiceModel expectedServiceModel = mock(ServiceModel.class);

		when(vidService.getService(serviceUuid)).thenReturn(expectedServiceModel);

		//when
		Object actualServiceModel = deploymentReportGenerator.getServiceDetails(serviceUuid);

		//then
		assertThat(actualServiceModel).isEqualTo(expectedServiceModel);
	}

	@Test
	public void shouldGetExceptionInfoWhenExceptionThrown() throws AsdcCatalogException {
		//given
		final String serviceUuid = "service_uuid";
		AsdcCatalogException thrownException = new AsdcCatalogException("msg");

		when(vidService.getService(serviceUuid)).thenThrow(thrownException);

		//when
		Object actualServiceModel = deploymentReportGenerator.getServiceDetails(serviceUuid);

		//then
		assertThat(actualServiceModel).isEqualTo(thrownException.toString());
	}
}