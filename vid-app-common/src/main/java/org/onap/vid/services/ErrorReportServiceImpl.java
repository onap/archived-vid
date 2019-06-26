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

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.controller.ControllersUtils;
import org.onap.vid.model.GitRepositoryState;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.scheduler.SchedulerService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


@Service
public class ErrorReportServiceImpl implements ErrorReportService {

	private static final String GIT_PROPERTIES_FILENAME = "git.properties";
	private final AaiClient aaiClient;
	private final SchedulerService schedulerService;
	private AaiService aaiService;
	private RoleProvider roleProvider;
	private SystemPropertiesWrapper systemPropertiesWrapper;
	private VidService vidService;
	private MsoBusinessLogic msoBusinessLogic;

	@Autowired
	public ErrorReportServiceImpl(AaiService aaiService, AaiClient aaiClient, RoleProvider roleProvider, SchedulerService schedulerService, SystemPropertiesWrapper systemPropertiesWrapper, VidService vidService, MsoBusinessLogic msoBusinessLogic) {
		this.aaiService = aaiService;
		this.aaiClient = aaiClient;
		this.roleProvider = roleProvider;
		this.schedulerService = schedulerService;
		this.systemPropertiesWrapper = systemPropertiesWrapper;
		this.vidService = vidService;
		this.msoBusinessLogic = msoBusinessLogic;
	}

	public String getUserID(HttpServletRequest request) {
		return new ControllersUtils(systemPropertiesWrapper).extractUserId(request);
	}

	public GitRepositoryState getCommitInfo() throws IOException {
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream(GIT_PROPERTIES_FILENAME));
		return new GitRepositoryState(properties);
	}

	public List<ExternalComponentStatus> getProbe() {
		return Arrays.asList(
				aaiClient.probeAaiGetAllSubscribers(),
				schedulerService.probeGetSchedulerChangeManagements()
		);
	}

	public MsoResponseWrapper getOrchestrationRequest(String requestId) {
		return msoBusinessLogic.getOrchestrationRequest(requestId);
	}


	public Object getSdcServiceModelsInfo() {
		Object result;
		try {
			result = aaiService.getFullSubscriberList().getBody();
		} catch (Exception e) {
			result = e.toString();
		}
		return result;
	}

	public Object getServiceDetails(String serviceUuid) {
		Object result;
		try {
			result = vidService.getService(serviceUuid);
		} catch (AsdcCatalogException e) {
			result = e.toString();
		}
		return result;
	}

	public Object getFullSubscriberList(HttpServletRequest request) {
		Object result;
		try {
			RoleValidator roleValidator = RoleValidator.by(roleProvider.getUserRoles(request));
			result = aaiService.getFullSubscriberList(roleValidator);
		} catch (Exception e) {
			result = e.toString();
		}
		return result;
	}
}
