/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.properties;

import org.apache.commons.lang3.StringUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.model.ModelConstants;
/**
 * The Class VidProperties.
 */
public class VidProperties extends SystemProperties {

	//VID General Properties
	public static final String MSO_DISPLAY_TEST_API_ON_SCREEN="mso.displayTestAPIOnScreen";
	public static final String HOMEPAGE_CONTACT_US_URL = "homepage_contact_us_url";
	public static final String MSO_DEFAULT_TEST_API="mso.defaultTestAPI";
	public static final String MSO_MAX_OPENED_INSTANTIATION_REQUESTS="mso.maxOpenedInstantiationRequests";
	public static final String MSO_ASYNC_POLLING_INTERVAL_SECONDS="mso.asyncPollingIntervalSeconds";
	public static final String PROBE_SDC_MODEL_UUID="probe.sdc.model.uuid";
	public static final String PORTAL_APP_PASSWORD_ENVIRONMENT_VARIABLE_NAME="VID_PORTAL_APP_PASSWORD";

	/** The Constant VID_TRUSTSTORE_FILENAME. */
	public static final String VID_TRUSTSTORE_FILENAME = "vid.truststore.filename";

	/** The Constant VID_TRUSTSTORE_PASSWD_X. */
	public static final String VID_TRUSTSTORE_PASSWD_X = "vid.truststore.passwd.x";

	/** The Constant FILESEPARATOR. */
	public static final String FILESEPARATOR = (System.getProperty("file.separator") == null) ? "/" : System.getProperty("file.separator");

	/** The Constant LOG. */
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VidProperties.class);

	public static final String VID_JOB_MAX_HOURS_IN_PROGRESS = "vid.asyncJob.maxHoursInProgress";

	public static final String VID_THREAD_COUNT = "vid.thread.count";
	public static final String VID_THREAD_TIMEOUT = "vid.thread.timeout";

	/**
	 * Gets the asdc model namespace prefix property
	 *
	 * @return the property value or a default value
	 */
	public static String getAsdcModelNamespace() {
		String methodName = "getAsdcModelNamespace ";
		String asdcModelNamespace = ModelConstants.DEFAULT_ASDC_MODEL_NAMESPACE;
		try {
			asdcModelNamespace = SystemProperties.getProperty(ModelConstants.ASDC_MODEL_NAMESPACE);
			if ( asdcModelNamespace == null || asdcModelNamespace.isEmpty()) {
				asdcModelNamespace = ModelConstants.DEFAULT_ASDC_MODEL_NAMESPACE;
			}
		}
		catch ( Exception e ) {
			LOG.error (EELFLoggerDelegate.errorLogger, methodName + "unable to find the value, using the default "
					+ ModelConstants.DEFAULT_ASDC_MODEL_NAMESPACE);
			asdcModelNamespace = ModelConstants.DEFAULT_ASDC_MODEL_NAMESPACE;
		}
		return (asdcModelNamespace);
	}
	/**
	 * Gets the specified property value. If the property is not defined, returns a default value.
	 *
	 * @return the property value or a default value
	 */
	public static String getPropertyWithDefault ( String propName, String defaultValue ) {
		String methodName = "getPropertyWithDefault ";
		String propValue = defaultValue;
		try {
			propValue = SystemProperties.getProperty(propName);
			if ( propValue == null || propValue.isEmpty()) {
				propValue = defaultValue;
			}
		}
		catch ( Exception e ) {
			LOG.error (EELFLoggerDelegate.errorLogger, methodName + "unable to find the value, using the default "
					+ defaultValue);
			propValue = defaultValue;
		}
		return (propValue);
	}

	public static long getLongProperty(String key) {
		return getLongProperty(key, 0);
	}

	public static long getLongProperty(String key, long defaultValue) {
		if (!containsProperty(key)) {
			LOG.debug(EELFLoggerDelegate.debugLogger, "No such property: {}. {} value is used", key, defaultValue);
			return defaultValue;
		}
		String configValue = getProperty(key);
		if (StringUtils.isNumeric(configValue)) {
			return Long.parseLong(configValue);
		} else {
			LOG.debug(EELFLoggerDelegate.debugLogger, "{} property value is not valid: {}. {} value is used", key, configValue, defaultValue);
			return defaultValue;
		}
	}
}
