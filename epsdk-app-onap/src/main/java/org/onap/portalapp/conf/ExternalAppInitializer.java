/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.conf;

import static org.onap.vid.controller.LoggerController.VID_IS_STARTED;

import java.util.TimeZone;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.onap.logging.filter.base.AbstractAuditLogFilter;
import org.onap.logging.filter.base.AbstractMetricLogFilter;
import org.onap.portalsdk.core.conf.AppInitializer;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.slf4j.LoggerFactory;

public class ExternalAppInitializer extends AppInitializer {

	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(ExternalAppInitializer.class);

	@Override
	protected Class<?>[] getServletConfigClasses() {
		Class<?> appConfigClass = ExternalAppConfig.class;
		// Show something on stdout to indicate the app is starting.
		LOG.info("ExternalAppInitializer: servlet configuration class is " + appConfigClass.getName());
		return new Class[] { appConfigClass };
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		setDefaultTimeZoneToUTC();
		setPartnerName();
		logVidIsStarted();
	}

	//mark in logs that vid is started.
	private void logVidIsStarted() {
		LoggerFactory.getLogger(AbstractMetricLogFilter.class).info(VID_IS_STARTED);
		LoggerFactory.getLogger(AbstractAuditLogFilter.class).info(VID_IS_STARTED);
	}

	private void setPartnerName() {
		//org.onap.logging.filter.base.AbstractMetricLogFilter read this system property
		System.setProperty("partnerName", "VID.VID");
	}

	//set time zone to UTC so Dates would be written to DB in UTC timezone
	private void setDefaultTimeZoneToUTC() {
		System.setProperty("user.timezone", "UTC");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC")); //since TimeZone cache previous user.timezone
	}
}
