/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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


import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.onap.vid.logging.Headers.PARTNER_NAME;
import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

import com.att.eelf.configuration.EELFLogger;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.ResponseWithRequestInfo;
import org.onap.vid.aai.exceptions.InvalidPropertyException;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.Unchecked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * The Class AAIRestInterface.
 */
public class AAIRestInterface {

    /** The logger. */
    protected EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AAIRestInterface.class);

    protected final EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("aai");

    /** The client. */
    private Client client = null;

    /** The rest srvr base URL. */
    private String restSrvrBaseURL;

    @Autowired
    protected HttpsAuthClient httpsAuthClientFactory;
    private final ServletRequestHelper servletRequestHelper;
    private final SystemPropertyHelper systemPropertyHelper;
    protected final Logging loggingService;

    protected static final String START_STRING = " start";
    protected static final String TRANSACTION_ID_HEADER = "X-TransactionId";
    protected static final String FROM_APP_ID_HEADER = "X-FromAppId";
    protected static final String SUCCESSFUL_API_MESSAGE = " REST api call was successful!";
    protected static final String URL_DECLARATION = ", url=";

    public AAIRestInterface(HttpsAuthClient httpsAuthClientFactory,
        ServletRequestHelper servletRequestHelper,
        SystemPropertyHelper systemPropertyHelper,
        Logging loggingService) {
        this.httpsAuthClientFactory = httpsAuthClientFactory;
        this.servletRequestHelper = servletRequestHelper;
        this.systemPropertyHelper = systemPropertyHelper;
        this.loggingService = loggingService;
        initRestClient();
    }

    /**
     * For testing purpose
     */
    AAIRestInterface(Optional<Client> client,
        HttpsAuthClient httpsAuthClientFactory,
        ServletRequestHelper servletRequestHelper,
        SystemPropertyHelper systemPropertyHelper,
        Logging loggingService){
        this.httpsAuthClientFactory = httpsAuthClientFactory;
        this.servletRequestHelper = servletRequestHelper;
        this.systemPropertyHelper = systemPropertyHelper;
        this.loggingService = loggingService;
        if (client != null && client.isPresent()){
            this.client = client.get();
        }

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

    protected void initRestClient() {
        initRestClient(false);
    }


    private void initRestClient(boolean propagateExceptions) {
        if (client == null) {
            try {
                client = httpsAuthClientFactory.getClient(HttpClientMode.WITH_KEYSTORE);
            } catch (Exception e) {
                logger.info(EELFLoggerDelegate.errorLogger, "Exception in REST call to DB in initRestClient" + e.toString());
                logger.debug(EELFLoggerDelegate.debugLogger, "Exception in REST call to DB : " + e.toString());
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
    public void setRestSrvrBaseURL(String baseURL)
    {
        if (baseURL == null) {
            logger.info(EELFLoggerDelegate.errorLogger, "REST Server base URL cannot be null.");
            logger.debug(EELFLoggerDelegate.debugLogger, "REST Server base URL cannot be null.");
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
     * @param fromAppId the from app id
     * @param transId the trans id
     * @param requestUri the request uri
     * @param xml the xml
     * @return the string
     */
    public ResponseWithRequestInfo RestGet(String fromAppId, String transId, URI requestUri, boolean xml) {
        return RestGet(fromAppId, transId, requestUri, xml, false);
    }

    public ResponseWithRequestInfo RestGet(String fromAppId, String transId, URI requestUri, boolean xml, boolean propagateExceptions) {
        return doRest(fromAppId, transId, requestUri, null, HttpMethod.GET, xml, propagateExceptions);
    }

    public ResponseWithRequestInfo doRest(String fromAppId, String transId, URI requestUri, String payload, HttpMethod method, boolean xml, boolean propagateExceptions) {
        String url = null;
        String methodName = "Rest"+method.name();
        try {

            url = systemPropertyHelper.getFullServicePath(requestUri);

            initRestClient(propagateExceptions);

            logger.debug(EELFLoggerDelegate.debugLogger, methodName + START_STRING);
            logger.debug(EELFLoggerDelegate.debugLogger, url + " for the get REST API");

            loggingService.logRequest(outgoingRequestsLogger, method, url, payload);

            final Response response;
            Invocation.Builder requestBuilder = client.target(url)
                .request()
                .accept(xml ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON)
                .header(PARTNER_NAME.getHeaderName(), PARTNER_NAME.getHeaderValue())
                .header(TRANSACTION_ID_HEADER, transId)
                .header(FROM_APP_ID_HEADER, fromAppId)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId());

            requestBuilder = systemPropertyHelper.isClientCertEnabled() ?
                requestBuilder : authenticateRequest(requestBuilder);

            Invocation restInvocation = StringUtils.isEmpty(payload) ?
                requestBuilder.build(method.name()) :
                requestBuilder.build(method.name(), Entity.entity(payload, MediaType.APPLICATION_JSON));

            response = restInvocation.invoke();
            loggingService.logResponse(outgoingRequestsLogger, method, url, response);

            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                logger.debug(EELFLoggerDelegate.debugLogger, methodName + SUCCESSFUL_API_MESSAGE);
                logger.info(EELFLoggerDelegate.errorLogger, methodName + SUCCESSFUL_API_MESSAGE);
            } else {
                logger.debug(EELFLoggerDelegate.debugLogger, getInvalidResponseLogMessage(url, methodName, response));
            }
            return new ResponseWithRequestInfo(response, url, method);
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, getFailedResponseLogMessage(url, methodName, e));
            if (propagateExceptions) {
                throw new ExceptionWithRequestInfo(method, defaultIfNull(url, requestUri.toASCIIString()), e);
            } else {
                return new ResponseWithRequestInfo(null, url, method);
            }
        }
    }

    protected String extractOrGenerateRequestId() {
        return servletRequestHelper.extractOrGenerateRequestId();
    }


    /**
     * Delete.
     *
     * @param sourceID the source ID
     * @param transId the trans id
     * @param path the path
     * @return true, if successful
     */
    public boolean Delete(String sourceID, String transId, String path) {
        String methodName = "Delete";
        transId += ":" + UUID.randomUUID().toString();
        logger.debug(methodName + START_STRING);
        Boolean response = false;
        String url = systemPropertyHelper.getFullServicePath(path);
        try {

            initRestClient();
            loggingService.logRequest(outgoingRequestsLogger, HttpMethod.DELETE, url);
            final Response cres = client.target(url)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .header(TRANSACTION_ID_HEADER, transId)
                .header(PARTNER_NAME.getHeaderName(), PARTNER_NAME.getHeaderValue())
                .header(FROM_APP_ID_HEADER, sourceID)
                .header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
                .delete();
            loggingService.logResponse(outgoingRequestsLogger, HttpMethod.DELETE, url, cres);
            if (cres.getStatusInfo().equals(Response.Status.NOT_FOUND)) {
                logger.debug(EELFLoggerDelegate.debugLogger, "Resource does not exist...: " + cres.getStatus()
                    + ":" + cres.readEntity(String.class));
                response = false;
            } else if (cres.getStatusInfo().equals(Response.Status.OK) || cres.getStatusInfo().equals(Response.Status.NO_CONTENT)) {
                logger.debug(EELFLoggerDelegate.debugLogger, "Resource " + url + " deleted");
                logger.info(EELFLoggerDelegate.errorLogger, "Resource " + url + " deleted");
                response = true;
            } else {
                logger.debug(EELFLoggerDelegate.debugLogger, "Deleting Resource failed: " + cres.getStatus()
                    + ":" + cres.readEntity(String.class));
                response = false;
            }

        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, getFailedResponseLogMessage(url, methodName, e));
        }
        return response;
    }


    /**
     * Rest put.
     *
     * @param fromAppId the from app id
     * @param path the path
     * @param payload the payload
     * @param xml the xml
     * @param propagateExceptions
     * @return the string
     */
    public ResponseWithRequestInfo RestPut(String fromAppId, String path, String payload, boolean xml, boolean propagateExceptions) {
        return doRest(fromAppId, UUID.randomUUID().toString(), Unchecked.toURI(path), payload, HttpMethod.PUT, xml, propagateExceptions);
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
        String url=systemPropertyHelper.getServiceBasePath(path);
        String transId = UUID.randomUUID().toString();
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START_STRING);

        Response response = null;
        try {
            initRestClient();
            loggingService.logRequest(outgoingRequestsLogger, HttpMethod.POST, url, payload);
            response = authenticateRequest(client.target(url)
                .request()
                .accept(xml ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON)
                .header(TRANSACTION_ID_HEADER, transId)
                .header(PARTNER_NAME.getHeaderName(), PARTNER_NAME.getHeaderValue())
                .header(FROM_APP_ID_HEADER,  fromAppId))
                .header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
                .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
            loggingService.logResponse(outgoingRequestsLogger, HttpMethod.POST, url, response);

            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                logger.info(EELFLoggerDelegate.errorLogger, getValidResponseLogMessage(methodName));
                logger.debug(EELFLoggerDelegate.debugLogger, getValidResponseLogMessage(methodName));
            } else {
                logger.debug(EELFLoggerDelegate.debugLogger, getInvalidResponseLogMessage(url, methodName, response));
            }
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, getFailedResponseLogMessage(url, methodName, e));
        }
        return response;
    }

    protected String getFailedResponseLogMessage(String path, String methodName, Exception e) {
        return methodName + URL_DECLARATION + path + ", Exception: " + e.toString();
    }

    protected String getValidResponseLogMessage(String methodName) {
        return methodName + URL_DECLARATION;
    }

    protected String getInvalidResponseLogMessage(String path, String methodName, Response cres) {
        return methodName + " with status=" + cres.getStatus() + URL_DECLARATION + path;
    }

    private Invocation.Builder authenticateRequest(Invocation.Builder requestBuilder) throws InvalidPropertyException, UnsupportedEncodingException {
        return requestBuilder
            .header("Authorization", "Basic " + systemPropertyHelper.getEncodedCredentials());
    }

}
