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
import org.onap.vid.aai.AaiClient;
import org.onap.vid.controller.ControllersUtils;
import org.onap.vid.model.GitRepositoryState;
import org.onap.vid.model.errorReport.ReportCreationParameters;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.scheduler.SchedulerService;
import org.onap.vid.services.AaiService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.onap.portalsdk.core.util.SystemProperties.ECOMP_REQUEST_ID;

@Component
public class BasicReportGenerator implements ReportGenerator {

	private static final String GIT_PROPERTIES_FILENAME = "git.properties";
	private final AaiClient aaiClient;
	private final SchedulerService schedulerService;
	private final AaiService aaiService;
	private final RoleProvider roleProvider;
	private final SystemPropertiesWrapper systemPropertiesWrapper;

	@Autowired
	public BasicReportGenerator(AaiClient aaiClient, SchedulerService schedulerService, AaiService aaiService,
	                            RoleProvider roleProvider, SystemPropertiesWrapper systemPropertiesWrapper) {
		this.aaiClient = aaiClient;
		this.schedulerService = schedulerService;
		this.aaiService = aaiService;
		this.roleProvider = roleProvider;
		this.systemPropertiesWrapper = systemPropertiesWrapper;
	}

	@Override
	public Map<String, Object> apply(HttpServletRequest request, ReportCreationParameters creationParameters) {
		return ImmutableMap.<String, Object>builder()
				.put("X-ECOMP-RequestID", request.getHeader(ECOMP_REQUEST_ID))
				.put("aaiGetFullSubscribers", getFullSubscriberList(request))
				.put("userID", getUserIDFromSystemProperties(request))
				.put("commitInfo", getCommitInfoFromGitProperties())
				.put("fullSubscribersList", getSdcServiceModelsInfo())
				.put("probeInfo", getProbe())
				.build();
	}

	@Override
	public boolean canGenerate(ReportCreationParameters creationParameters) {
		return true;
	}

	private String getUserIDFromSystemProperties(HttpServletRequest request) {
		return new ControllersUtils(systemPropertiesWrapper).extractUserId(request);
	}

	private GitRepositoryState getCommitInfoFromGitProperties() {
		try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(GIT_PROPERTIES_FILENAME)) {
			Properties properties = new Properties();
			properties.load(resourceAsStream);
			return new GitRepositoryState(properties);
		} catch (IOException e) {
			return GitRepositoryState.EMPTY;
		}
	}

	private List<ExternalComponentStatus> getProbe() {
		return Arrays.asList(
				aaiClient.probeAaiGetAllSubscribers(),
				schedulerService.probeGetSchedulerChangeManagements()
		);
	}

	private Object getSdcServiceModelsInfo() {
		try {
			return aaiService.getFullSubscriberList();
		} catch (Exception e) {
			return e.toString();
		}
	}

	private Object getFullSubscriberList(HttpServletRequest request) {
		try {
			RoleValidator roleValidator = RoleValidator.by(roleProvider.getUserRoles(request));
			return aaiService.getFullSubscriberList(roleValidator);
		} catch (Exception e) {
			return e.toString();
		}
	}
}
