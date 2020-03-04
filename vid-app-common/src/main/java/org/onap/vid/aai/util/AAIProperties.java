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

package org.onap.vid.aai.util;


import org.onap.portalsdk.core.util.SystemProperties;

/**
 * The Class AAIProperties.
 */
public class AAIProperties extends SystemProperties {

	/** The Constant AAI_SERVER_URL_BASE. */
	//VID Specific
	public static final String AAI_SERVER_URL_BASE = "aai.server.url.base";
	
	/** The Constant AAI_SERVER_URL. */
	public static final String AAI_SERVER_URL = "aai.server.url";
	
	/** The Constant AAI_TRUSTSTORE_FILENAME. */
	public static final String AAI_TRUSTSTORE_FILENAME = "aai.truststore.filename";
	
	/** The Constant AAI_TRUSTSTORE_PASSWD_X. */
	public static final String AAI_TRUSTSTORE_PASSWD_X = "aai.truststore.passwd.x";
	
	/** The Constant AAI_KEYSTORE_FILENAME. */
	public static final String AAI_KEYSTORE_FILENAME = "aai.keystore.filename";
	
	/** The Constant AAI_KEYSTORE_PASSWD_X. */
	public static final String AAI_KEYSTORE_PASSWD_X = "aai.keystore.passwd.x";
		
	/** The Constant AAI_VID_USERNAME. */
	public static final String AAI_VID_USERNAME = "aai.vid.username";

	/** The Constant AAI_VID_PASSWD_X. */
	public static final String AAI_VID_PASSWD_X = "aai.vid.passwd.x";
	
	/** The Constant FILESEPARTOR. */
	public static final String FILESEPARTOR = (System.getProperty("file.separator") == null) ? "/" : System.getProperty("file.separator");

	/** The Constant AAI_USE_CLIENT_CERT */
	public static final String AAI_USE_CLIENT_CERT = "aai.use.client.cert";
	
}
