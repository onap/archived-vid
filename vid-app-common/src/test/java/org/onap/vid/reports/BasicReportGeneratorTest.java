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
import io.joshworks.restclient.http.Headers;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.exceptions.RestClientException;
import nu.xom.jaxen.util.SingletonList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.StatusMetadata;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.scheduler.SchedulerService;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.ProbeService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicReportGeneratorTest {

	private static final String USER_ATTRIBUTE = "userAttribute";
	private ReportCreationParameters creationParameters = new ReportCreationParameters();

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private HttpServletRequest request;
	@Mock
	private HttpResponse<SubscriberList> subscriberListResponse;
	@Mock
	private ProbeService probeService;

	@Mock
	private AaiClient aaiClient;
	@Mock
	private SchedulerService schedulerService;
	@Mock
	private AaiService aaiService;
	@Mock
	private RoleProvider roleProvider;
	@Mock
	private SystemPropertiesWrapper systemPropertiesWrapper;

	@InjectMocks
	private BasicReportGenerator basicReportGenerator;

	@Test
	public void shouldAlwaysReturnTrueAsConditionOfGeneration() {
		//given
		//when
		//then
		assertThat(basicReportGenerator.canGenerate(creationParameters)).isTrue();
	}

	@Test
	public void shouldGetUserIDFromSystemProperties() {
		//given
		final String expectedUserID = "user_id";
		User user = new User();
		user.setLoginId(expectedUserID);

		when(systemPropertiesWrapper.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn(USER_ATTRIBUTE);
		when(request.getSession().getAttribute(USER_ATTRIBUTE)).thenReturn(user);

		//when
		String actualUserID = basicReportGenerator.getUserIDFromSystemProperties(request);

		//then
		assertThat(actualUserID).isEqualTo(expectedUserID);
	}

	@Test
	public void shouldGetProbes() {
		//given
		ExternalComponentStatus status = new ExternalComponentStatus(
						ExternalComponentStatus.Component.MSO,
						true,
						mock(StatusMetadata.class));
		List<ExternalComponentStatus> expectedProbes = Collections.singletonList(status);

		when(probeService.getProbe()).thenReturn(expectedProbes);

		//when
		List<ExternalComponentStatus> actualProbes = basicReportGenerator.getProbe();

		//then
		assertThat(actualProbes).isEqualTo(expectedProbes);
	}

	@Test
	public void shouldGetFullSubscriberList() {
		//given
		SubscriberList subscriberList = new SubscriberList();
		Headers headers = new Headers();
		int status = HttpStatus.OK.value();

		ImmutableMap<String, Object> expectedSubscriberList = ImmutableMap.<String, Object>builder()
				.put("status", status)
				.put("body", subscriberList)
				.put("headers", headers)
				.build();

		when(aaiService.getFullSubscriberList()).thenReturn(subscriberListResponse);
		when(subscriberListResponse.getStatus()).thenReturn(status);
		when(subscriberListResponse.getBody()).thenReturn(subscriberList);
		when(subscriberListResponse.getHeaders()).thenReturn(headers);

		//when
		ImmutableMap<String, Object> actualSubscriberList = basicReportGenerator.getFullSubscriberList();

		//then
		assertThat(actualSubscriberList).isEqualTo(expectedSubscriberList);
	}

	@Test
	public void shouldReturnExceptionInfoWhileGettingFullSubscriberList() {
		//given
		RestClientException expectedException = mock(RestClientException.class);
		ImmutableMap<String, Object> expectedResult = ImmutableMap.of("exception", expectedException.toString());

		when(aaiService.getFullSubscriberList()).thenThrow(expectedException);

		//when
		ImmutableMap<String, Object> actualResult = basicReportGenerator.getFullSubscriberList();

		//then
		assertThat(actualResult).isEqualTo(expectedResult);
	}
}