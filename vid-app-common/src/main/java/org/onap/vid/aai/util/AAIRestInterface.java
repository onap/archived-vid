/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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


import com.att.eelf.configuration.EELFLogger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jetty.util.security.Password;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.ResponseWithRequestInfo;
import org.onap.vid.utils.Logging;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;


/**
 * The Class AAIRestInterface.
 */
public class AAIRestInterface {

	public static final String WITH_STATUS = " with status=";
	/** The logger. */
	protected EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AAIRestInterface.class);

	protected final EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("aai");

	/** The Constant dateFormat. */
	protected final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

	/** The client. */
	private Client client = null;

	/** The rest srvr base URL. */
	private String restSrvrBaseURL;

	@Autowired
	protected HttpsAuthClient httpsAuthClientFactory;

	private String START_STRING = " start";
	protected String TRANSACTION_ID_HEADER = "X-TransactionId";
	protected String FROM_APP_ID_HEADER = "X-FromAppId";
	private String SUCCESSFUL_API_MESSAGE=" REST api POST was successful!";
	protected String URL_DECLARATION = ", url=";

	public AAIRestInterface(HttpsAuthClient httpsAuthClientFactory) {
		this.httpsAuthClientFactory = httpsAuthClientFactory;
	}

	/**
	 * Encode URL.
	 *
	 * @param nodeKey the node key
	 * @return the string
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public String encodeURL (String nodeKey) throws UnsupportedEncodingException {
		return URLEncoder.encode(nodeKey, "UTF-8").replaceAll("\\+", "%20");
	}

    private void initRestClient() {
        initRestClient(false);
    }


	private void initRestClient(boolean propagateExceptions)
	{
		if (client == null) {
			try {
				client = httpsAuthClientFactory.getClient(HttpClientMode.WITHOUT_KEYSTORE);
			} catch (Exception e) {
				logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== Exception in REST call to DB in initRestClient" + e.toString());
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== Exception in REST call to DB : " + e.toString());
				if (propagateExceptions) {
					ExceptionUtils.rethrow(e);
				}
			}
		}
	}



	/**
	 * Sets the rest srvr base URL.
	 *
	 * @param baseURL the base URL
	 */
	public void SetRestSrvrBaseURL(String baseURL)
	{
		if (baseURL == null)
		{
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== REST Server base URL cannot be null.");
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== REST Server base URL cannot be null.");
		}

		restSrvrBaseURL = baseURL;
	}

	/**
	 * Gets the rest srvr base URL.
	 *
	 * @return the rest srvr base URL
	 */
	public String getRestSrvrBaseURL()
	{
		return restSrvrBaseURL;
	}


	/**
	 * Rest get.
	 *
	 * @param fromAppId the from app id
	 * @param transId the trans id
	 * @param requestUri the request uri
	 * @param xml the xml
	 * @return the string
	 */

	public ResponseWithRequestInfo RestGet(String fromAppId, String transId, String requestUri, boolean xml) {
		return RestGet(fromAppId, transId, requestUri, xml, false);
	}

	public ResponseWithRequestInfo RestGet(String fromAppId, String transId, String requestUri, boolean xml, boolean propagateExceptions) {
		String url = null;
		try {
			String methodName = "RestGet";
			url = SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL) + requestUri;

			String responseType = MediaType.APPLICATION_JSON;
			if (xml)
				responseType = MediaType.APPLICATION_XML;

			initRestClient(propagateExceptions);

			String clientCert = SystemProperties.getProperty(AAIProperties.AAI_USE_CLIENT_CERT);

			boolean useClientCert = false;
			if (clientCert != null &&
					SystemProperties.getProperty(AAIProperties.AAI_USE_CLIENT_CERT).equalsIgnoreCase("true")) {
				useClientCert = true;
			}

			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + START_STRING);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + url + " for the get REST API");

			Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);

			final Response cres;
			if (useClientCert == true) {
				cres = client.target(url)
						.request()
						.accept(responseType)
						.header(TRANSACTION_ID_HEADER, transId)
						.header(FROM_APP_ID_HEADER, fromAppId)
						.header("Content-Type", MediaType.APPLICATION_JSON)
						.header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
						.get();
			} else {

				String vidUsername = SystemProperties.getProperty(AAIProperties.AAI_VID_USERNAME);
				String vidPassword = Password.deobfuscate(SystemProperties.getProperty(AAIProperties.AAI_VID_PASSWD_X));
				String encodeThis = vidUsername + ":" + vidPassword;

				cres = client.target(url)
						.request()
						.accept(responseType)
						.header(TRANSACTION_ID_HEADER, transId)
						.header(FROM_APP_ID_HEADER, fromAppId)
						.header("Content-Type", "application/json")
						.header("Authorization", "Basic " + Base64.getEncoder().encodeToString(encodeThis.getBytes("utf-8")))
						.header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
						.get();
			}
			Logging.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, cres);

			if (cres.getStatus() == 200) {
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + SUCCESSFUL_API_MESSAGE);
				logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + SUCCESSFUL_API_MESSAGE);
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + WITH_STATUS + cres.getStatus() + URL_DECLARATION + url);
			}
			return new ResponseWithRequestInfo(cres, url, HttpMethod.GET);
		} catch (Exception e) {
			// no need to ask if "propagateExceptions" because any exception
			// at this point should have already obey to the
			// "propagateExceptions" flag
			throw new ExceptionWithRequestInfo(HttpMethod.GET, defaultIfNull(url, requestUri), e);
		}
	}

	protected String extractOrGenerateRequestId() {
		return Logging.extractOrGenerateRequestId();
	}


	/**
	 * Delete.
	 *
	 * @param sourceID the source ID
	 * @param transId the trans id
	 * @param path the path
	 * @return true, if successful
	 */
	public boolean Delete(String sourceID,  String transId,  String path) {
		String methodName = "Delete";
		String url="";
		transId += ":" + UUID.randomUUID().toString();
		logger.debug(dateFormat.format(new Date()) + "<== " +  methodName + START_STRING);

		initRestClient();
		url = SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL) + path;
		Logging.logRequest(outgoingRequestsLogger, HttpMethod.DELETE, url);
		final Response cres = client.target(url)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.header(TRANSACTION_ID_HEADER, transId)
				.header(FROM_APP_ID_HEADER,  sourceID)
				.header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
				.delete();
		Logging.logResponse(outgoingRequestsLogger, HttpMethod.DELETE, url, cres);
		if (cres.getStatus() == 404) { // resource not found
			String msg = "Resource does not exist...: " + cres.getStatus()
					+ ":" + cres.readEntity(String.class);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + msg);
			return false;
		} else if (cres.getStatus() == 200  || cres.getStatus() == 204){
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "Resource " + url + " deleted");
			return true;
		} else {
			String msg = "Deleting Resource failed: " + cres.getStatus()
					+ ":" + cres.readEntity(String.class);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + msg);
		}

		return false;
	}


	/**
	 * Rest put.
	 *
	 * @param fromAppId the from app id
	 * @param path the path
	 * @param payload the payload
	 * @param xml the xml
	 * @return the string
	 */
	public Response RestPut(String fromAppId, String path, String payload, boolean xml) {
		String methodName = "RestPut";
		String url="";
		String transId = UUID.randomUUID().toString();
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " +  methodName + START_STRING);

		try {

			String responseType = MediaType.APPLICATION_JSON;
			if (xml)
				responseType = "application/xml";

			initRestClient();

			url = SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL) + path;
			String vidUsername = SystemProperties.getProperty(AAIProperties.AAI_VID_USERNAME);
            String vidPassword = Password.deobfuscate(SystemProperties.getProperty(AAIProperties.AAI_VID_PASSWD_X));
            String encodeThis = vidUsername + ":" + vidPassword;
            
			Logging.logRequest(outgoingRequestsLogger, HttpMethod.PUT, url, payload);
			final Response cres = client.target(url)
					.request()
					.accept(responseType)
					.header(TRANSACTION_ID_HEADER, transId)
					.header(FROM_APP_ID_HEADER,  fromAppId)
					.header("Authorization", "Basic " + Base64.getEncoder().encodeToString(encodeThis.getBytes("utf-8")))
					.header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
					.put(Entity.entity(payload, MediaType.APPLICATION_JSON));
			Logging.logResponse(outgoingRequestsLogger, HttpMethod.PUT, url, cres);

			if (cres.getStatus() == 200 && cres.getStatus() <= 299) {
				logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION);
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION);
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + WITH_STATUS +cres.getStatus()+ URL_DECLARATION +url);
			}
			return cres;
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION +url+ ", Exception: " + e.toString());
		}
		return null;
	}



	/**
	 * Rest post.
	 *
	 * @param fromAppId the from app id
	 * @param path the path
	 * @param payload the payload
	 * @param xml the xml
	 * @return the string
	 */
	public Response RestPost(String fromAppId, String path, String payload, boolean xml) {
		String methodName = "RestPost";
		String url="";
		String transId = UUID.randomUUID().toString();
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " +  methodName + START_STRING);

		try {

			String responseType = MediaType.APPLICATION_JSON;
			if (xml)
				responseType = "application/xml";

			initRestClient();

			url = SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL_BASE) + path;
			String vidUsername = SystemProperties.getProperty(AAIProperties.AAI_VID_USERNAME);
			String vidPassword = Password.deobfuscate(SystemProperties.getProperty(AAIProperties.AAI_VID_PASSWD_X));
			String encodeThis = vidUsername + ":" + vidPassword;
			
			Logging.logRequest(outgoingRequestsLogger, HttpMethod.POST, url, payload);
			final Response cres = client.target(url)
					.request()
					.accept(responseType)
					.header(TRANSACTION_ID_HEADER, transId)
					.header(FROM_APP_ID_HEADER,  fromAppId)
					.header("Authorization", "Basic " + Base64.getEncoder().encodeToString(encodeThis.getBytes("utf-8")))
					.header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
					.post(Entity.entity(payload, MediaType.APPLICATION_JSON));
			Logging.logResponse(outgoingRequestsLogger, HttpMethod.POST, url, cres);

			if (cres.getStatus() == 200 && cres.getStatus() <= 299) {
				logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION);
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION);
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + WITH_STATUS +cres.getStatus()+ URL_DECLARATION +url);
			}
			return cres;
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION +url+ ", Exception: " + e.toString());
		}
		return null;
	}

}
