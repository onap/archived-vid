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
package org.onap.vid.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiOverTLSClient;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.StatusMetadata;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.scheduler.SchedulerService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProbeServiceTest {

	private AaiClient aaiClient;
	private AaiOverTLSClient newAaiClient;
	private SchedulerService schedulerService;
	private MsoBusinessLogic msoBusinessLogic;
	private VidService vidService;
	private List<ProbeInterface> probeInterfaces;
	private ProbeService probeService;

	@Before
	public void setUp() throws Exception {
		aaiClient = mock(AaiClient.class);
		newAaiClient = mock(AaiOverTLSClient.class);
		schedulerService = mock(SchedulerService.class);
		msoBusinessLogic = mock(MsoBusinessLogic.class);
		vidService = mock(VidService.class);

		probeInterfaces = Arrays.asList(aaiClient, newAaiClient, schedulerService, msoBusinessLogic, vidService);
		probeService = new ProbeService(probeInterfaces);
	}

	@Test
	public void shouldGetProbes() {
		//given
		StatusMetadata statusMetadata = mock(StatusMetadata.class);

		ExternalComponentStatus statusAai =
				new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, true, statusMetadata);
		ExternalComponentStatus statusScheduler =
				new ExternalComponentStatus(ExternalComponentStatus.Component.SCHEDULER, false, statusMetadata);
		ExternalComponentStatus statusMso =
				new ExternalComponentStatus(ExternalComponentStatus.Component.MSO, true, statusMetadata);
		ExternalComponentStatus statusSdc =
				new ExternalComponentStatus(ExternalComponentStatus.Component.SDC, false, statusMetadata);

		List<ExternalComponentStatus> expectedStatuses =
				Arrays.asList(statusAai, statusAai, statusScheduler, statusMso, statusSdc);

		when(aaiClient.probeComponent()).thenReturn(statusAai);
		when(newAaiClient.probeComponent()).thenReturn(statusAai);
		when(schedulerService.probeComponent()).thenReturn(statusScheduler);
		when(msoBusinessLogic.probeComponent()).thenReturn(statusMso);
		when(vidService.probeComponent()).thenReturn(statusSdc);

		//when
		List<ExternalComponentStatus> actualStatuses = probeService.getProbe();

		//then
		assertThat(actualStatuses).isEqualTo(expectedStatuses);
	}
}