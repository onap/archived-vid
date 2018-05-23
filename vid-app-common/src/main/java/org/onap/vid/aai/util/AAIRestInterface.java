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
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.exceptions.InvalidPropertyException;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.OK;
import static org.onap.vid.utils.Logging.requestIdHeaderKey;

/**
 * The Class AAIRestInterface.
 */
public class AAIRestInterface {

    final private EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("aai");

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    /**
     * The client.
     */
    private static Client client = null;

    /**
     * The rest srvr base URL.
     */
    private String restSrvrBaseURL;

    private static final String START_STRING = " start";
    private static final String TRANSACTION_ID_HEADER = "X-TransactionId";
    private static final String FROM_APP_ID_HEADER = "X-FromAppId";
    private static final String SUCCESSFUL_API_MESSAGE = " REST api call was successful!";
    private static final String URL_DECLERATION = ", url=";

    private final LogHelper logHelper;
    private final ServletRequestHelper servletRequestHelper;

    /**
     * Instantiates a new AAI rest interface.
     *
     * @param certPath the cert path
     */
    public AAIRestInterface(String certPath) {
        this.logHelper = new LogHelper(EELFLoggerDelegate.getLogger(AAIRestInterface.class));
        this.servletRequestHelper = new ServletRequestHelper();
        initRestClient(certPath);
    }

    /**
     * For testing purpose
     */
    AAIRestInterface(String certPath, LogHelper logHelper, Optional<Client> client, ServletRequestHelper servletRequestHelper){
        this.logHelper = logHelper;
        this.servletRequestHelper = servletRequestHelper;
        if (client.isPresent()){
            this.client = client.get();
        }else{
            initRestClient(certPath);
        }

    }

    /**
     * Sets the rest srvr base URL.
     *
     * @param baseURL the base URL
     */
    public void SetRestSrvrBaseURL(String baseURL) {
        if (baseURL == null) {
            logHelper.multilog("<== REST Server base URL cannot be null.");
        }
        restSrvrBaseURL = baseURL;
    }

    /**
     * Gets the rest srvr base URL.
     *
     * @return the rest srvr base URL
     */
    public String getRestSrvrBaseURL() {
        return restSrvrBaseURL;
    }

    /**
     * Rest get.
     *
     * @param fromAppId  the from app id
     * @param transId    the trans id
     * @param requestUri the request uri
     * @param xml        the xml
     * @return the string
     */
    public Response RestGet(String fromAppId, String transId, String requestUri, boolean xml) {
        String methodName = "RestGet";

        logHelper.logDebug(methodName + START_STRING);
        logHelper.logDebug(SystemPropertyHelper.getFullServicePath(requestUri) + " for the get REST API");
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, SystemPropertyHelper.getFullServicePath(requestUri));
        Response response = null;
        try {
            Invocation.Builder requestBuilder = client.target(SystemPropertyHelper.getFullServicePath(requestUri))
                    .request()
                    .accept(xml ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON)
                    .header(TRANSACTION_ID_HEADER, transId)
                    .header(FROM_APP_ID_HEADER, fromAppId)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header(requestIdHeaderKey, servletRequestHelper.getServletRequest().getHeader(requestIdHeaderKey));
            response = SystemPropertyHelper.isClientCertEnabled() ? requestBuilder.get() : authenticateRequest(requestBuilder)
                    .get();
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.GET, SystemPropertyHelper.getFullServicePath(requestUri), response);
            if (response.getStatusInfo().equals(OK)) {
                logHelper.multilog(methodName + SUCCESSFUL_API_MESSAGE);
            } else {
                logHelper.logDebug(methodName + " with status=" + response.getStatus() + URL_DECLERATION + SystemPropertyHelper.getFullServicePath(requestUri));
            }
        }catch (Exception e){
            logHelper.logDebug(getFailedResponseLogMessage(requestUri, methodName, e));
        }
        return response;
    }

    /**
     * Delete.
     *
     * @param sourceID the source ID
     * @param transId  the trans id
     * @param path     the path
     * @return true, if successful
     */
    public boolean Delete(String sourceID, String transId, String path) {
        String methodName = "Delete";
        transId += ":" + UUID.randomUUID().toString();
        logHelper.logDebug(methodName + START_STRING);
        Boolean response = false;
        try {
            Logging.logRequest(outgoingRequestsLogger, HttpMethod.DELETE, SystemPropertyHelper.getFullServicePath(path));
            final Response cres = client.target(SystemPropertyHelper.getFullServicePath(path))
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(TRANSACTION_ID_HEADER, transId)
                    .header(FROM_APP_ID_HEADER, sourceID)
                    .header(requestIdHeaderKey, servletRequestHelper.getServletRequest().getHeader(requestIdHeaderKey))
                    .delete();
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.DELETE, SystemPropertyHelper.getFullServicePath(path), cres);
            if (cres.getStatusInfo().equals(Response.Status.NOT_FOUND)) {
                logHelper.logDebug("Resource does not exist...: " + cres.getStatus()
                        + ":" + cres.readEntity(String.class));
                response = false;
            } else if (cres.getStatusInfo().equals(OK) || cres.getStatusInfo().equals(Response.Status.NO_CONTENT)) {
                logHelper.logDebug("Resource " + SystemPropertyHelper.getFullServicePath(path) + " deleted");
                response = true;
            } else {
                logHelper.logDebug("Deleting Resource failed: " + cres.getStatus()
                        + ":" + cres.readEntity(String.class));
            }
        }catch(Exception e){
            logHelper.logDebug(getFailedResponseLogMessage(path, methodName, e));
        }
        return response;
    }

    /**
     * Rest put.
     *
     * @param fromAppId the from app id
     * @param transId   the trans id
     * @param path      the path
     * @param payload   the payload
     * @param xml       the xml
     * @return the string
     */
    public Response RestPut(String fromAppId, String transId, String path, String payload, boolean xml) {
        String methodName = "RestPut";
        transId = UUID.randomUUID().toString();
        logHelper.logDebug(methodName + START_STRING);
        Response response = null;
        try {
            Logging.logRequest(outgoingRequestsLogger, HttpMethod.PUT, SystemPropertyHelper.getFullServicePath(path), payload);
            response = authenticateRequest(client.target(SystemPropertyHelper.getFullServicePath(path))
                    .request()
                    .accept(xml ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON)
                    .header(TRANSACTION_ID_HEADER, transId)
                    .header(FROM_APP_ID_HEADER, fromAppId))
                    .header(requestIdHeaderKey, servletRequestHelper.getServletRequest().getHeader(requestIdHeaderKey))
                    .put(Entity.entity(payload, MediaType.APPLICATION_JSON));
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.PUT, SystemPropertyHelper.getFullServicePath(path), response);
            if (response.getStatusInfo().equals(OK)) {
                logHelper.multilog(getValidResponseLogMessage(methodName));
            } else {
                logHelper.logDebug(getInvalidResponseLogMessage(path, methodName, response));
            }
        } catch (Exception e) {
            logHelper.logDebug(getFailedResponseLogMessage(path, methodName, e));
        }
        return response;
    }

    /**
     * Rest post.
     *
     * @param fromAppId the from app id
     * @param transId   the trans id
     * @param path      the path
     * @param payload   the payload
     * @param xml       the xml
     * @return the string
     */
    public Response RestPost(String fromAppId, String transId, String path, String payload, boolean xml) {
        String methodName = "RestPost";
        transId = UUID.randomUUID().toString();
        logHelper.logDebug(methodName + START_STRING);
        Response response = null;
        try {
            Logging.logRequest(outgoingRequestsLogger, HttpMethod.POST, SystemPropertyHelper.getFullServicePath(path), payload);
            response = authenticateRequest(client.target(SystemPropertyHelper.getFullServicePath(path))
                    .request()
                    .accept(xml ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON)
                    .header(TRANSACTION_ID_HEADER, transId)
                    .header(FROM_APP_ID_HEADER, fromAppId))
                    .header(requestIdHeaderKey, servletRequestHelper.getServletRequest().getHeader(requestIdHeaderKey))
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.POST, SystemPropertyHelper.getFullServicePath(path), response);
            if (response.getStatusInfo().equals(OK)) {
                logHelper.multilog(getValidResponseLogMessage(methodName));
            } else {
                logHelper.logDebug(getInvalidResponseLogMessage(path, methodName, response));
            }
        } catch (Exception e) {
            logHelper.logDebug(getFailedResponseLogMessage(path, methodName, e));
        }
        return response;
    }

    /**
     * Encode URL.
     *
     * @param nodeKey the node key
     * @return the string
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    String encodeURL(String nodeKey) throws UnsupportedEncodingException {
        return URLEncoder.encode(nodeKey, "UTF-8").replaceAll("\\+", "%20");
    }

    /**
     * Inits the rest client.
     */
    private void initRestClient(String certificatePath) {
        String methodName = "initRestClient";
        if (client == null) {
            try {
                client = HttpsAuthClient.getClient(certificatePath);
            } catch (KeyManagementException e) {
                logHelper.multilog("<== KeyManagementException in " + methodName, e);
            } catch (Exception e) {
                logHelper.multilog("<== Exception in REST call to DB in " + methodName, e);
            }
        }
    }

    private String getFailedResponseLogMessage(String path, String methodName, Exception e) {
        return methodName + URL_DECLERATION + SystemPropertyHelper.getFullServicePath(path) + ", Exception: " + e.toString();
    }

    private String getValidResponseLogMessage(String methodName) {
        return methodName + URL_DECLERATION;
    }

    private String getInvalidResponseLogMessage(String path, String methodName, Response cres) {
        return methodName + " with status=" + cres.getStatus() + URL_DECLERATION + SystemPropertyHelper.getFullServicePath(path);
    }

    private Invocation.Builder authenticateRequest(Invocation.Builder requestBuilder) throws InvalidPropertyException, UnsupportedEncodingException {
        return requestBuilder
                .header("Authorization", "Basic " + SystemPropertyHelper.getEncodedCredentials());
    }

}
