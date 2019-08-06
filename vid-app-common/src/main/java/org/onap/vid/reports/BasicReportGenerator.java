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

import static org.onap.portalsdk.core.util.SystemProperties.ECOMP_REQUEST_ID;

import com.google.common.collect.ImmutableMap;
import io.joshworks.restclient.http.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.onap.vid.aai.AaiOverTLSClientInterface;
import org.onap.vid.controller.ControllersUtils;
import org.onap.vid.model.GitRepositoryState;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.services.ProbeService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasicReportGenerator implements ReportGenerator {

	private static final String GIT_PROPERTIES_FILENAME = "git.properties";
	private final AaiOverTLSClientInterface aaiOverTLSClient;
	private final SystemPropertiesWrapper systemPropertiesWrapper;
	private final ProbeService probeService;

	@Autowired
	public BasicReportGenerator(AaiOverTLSClientInterface aaiOverTLSClient, SystemPropertiesWrapper systemPropertiesWrapper,
	                            ProbeService probeService) {
		this.aaiOverTLSClient = aaiOverTLSClient;
		this.systemPropertiesWrapper = systemPropertiesWrapper;
		this.probeService = probeService;
	}

	@Override
	public Map<String, Object> apply(HttpServletRequest request, ReportCreationParameters creationParameters) {
		return ImmutableMap.<String, Object>builder()
				.put("X-ECOMP-RequestID", request.getHeader(ECOMP_REQUEST_ID))
				.put("aaiFullSubscriberList", getFullSubscriberList())
				.put("userID", getUserIDFromSystemProperties(request))
				.put("commitInfo", getCommitInfoFromGitProperties())
				.put("probeInfo", getProbe())
				.build();
	}

	@Override
	public boolean canGenerate(ReportCreationParameters creationParameters) {
		return true;
	}

	private GitRepositoryState getCommitInfoFromGitProperties() {
		GitRepositoryState gitRepositoryState;
		try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(GIT_PROPERTIES_FILENAME)) {
			Properties properties = new Properties();
			properties.load(resourceAsStream);
			gitRepositoryState = new GitRepositoryState(properties);
		} catch (IOException e) {
			gitRepositoryState = GitRepositoryState.EMPTY;
		}
		return gitRepositoryState;
	}

	String getUserIDFromSystemProperties(HttpServletRequest request) {
		return new ControllersUtils(systemPropertiesWrapper).extractUserId(request);
	}

	List<ExternalComponentStatus> getProbe() {
		return probeService.getProbe();
	}

	ImmutableMap<String, Object> getFullSubscriberList() {
		ImmutableMap<String, Object> fullSubscriberList;
		try {
			HttpResponse<SubscriberList> fullSubscriberListResponse = aaiOverTLSClient.getAllSubscribers();
			fullSubscriberList = ImmutableMap.<String, Object>builder()
					.put("status", fullSubscriberListResponse.getStatus())
					.put("body", fullSubscriberListResponse.getBody())
					.put("headers", fullSubscriberListResponse.getHeaders())
					.build();
		} catch (Exception e) {
			fullSubscriberList = ImmutableMap.of("exception", e.toString());
		}
		return fullSubscriberList;
	}
}
