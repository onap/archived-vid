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

package org.onap.vid.mso;

import static org.onap.vid.logging.Headers.INVOCATION_ID;
import static org.onap.vid.logging.Headers.PARTNER_NAME;
import static org.onap.vid.utils.Logging.ONAP_REQUEST_ID_HEADER_KEY;
import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;
import static org.onap.vid.utils.Logging.getMethodCallerName;
import static org.onap.vid.utils.Logging.getMethodName;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpException;
import org.eclipse.jetty.util.security.Password;
import org.glassfish.jersey.client.ClientProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.client.HttpBasicClient;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.mso.rest.RestInterface;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * Created by pickjonathan on 26/06/2017.
 */
public class RestMsoImplementation implements RestInterface {

    
    /**
     * The logger.
     */
    protected EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RestMsoImplementation.class);
    private final EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("mso");

    /** The client. */
    private Client client = null;


    protected HttpsAuthClient httpsAuthClient;
    protected SystemPropertiesWrapper systemProperties;
    protected final Logging loggingService;

    private static final String START_LOG = " start";
    private static final String APPLICATION_JSON = "application/json";
    private static final String WITH_STATUS = " with status=";
    private static final String URL_LOG = ", url=";
    private static final String NO_RESPONSE_ENTITY_LOG = " No response entity, this is probably ok, e=";
    private static final String WITH_URL_LOG = " with url=";
    private static final String EXCEPTION_LOG = ", Exception: ";
    private static final String REST_API_SUCCESSFULL_LOG = " REST api was successfull!";
    private static final String REST_MSG_TEMPLATE = "start {}->{}({}, {}, {})";
    /** The common headers. */
    /**
     * Instantiates a new mso rest interface.
     */

    @Autowired
    protected RestMsoImplementation(HttpsAuthClient httpsAuthClient, SystemPropertiesWrapper systemProperties, Logging loggingService){
        this.httpsAuthClient=httpsAuthClient;
        this.systemProperties = systemProperties;
        this.loggingService = loggingService;
    }

    @SuppressWarnings("Duplicates")
    protected MultivaluedHashMap<String, Object> initMsoClient()
    {
        final String methodname = "initRestClient()";

        final String username = systemProperties.getProperty(MsoProperties.MSO_USER_NAME);
        final String password = systemProperties.getProperty(MsoProperties.MSO_PASSWORD);
        final String mso_url = systemProperties.getProperty(MsoProperties.MSO_SERVER_URL);
        final String decrypted_password = Password.deobfuscate(password);

        String authString = username + ":" + decrypted_password;

        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        MultivaluedHashMap<String, Object> commonHeaders = new MultivaluedHashMap();
        commonHeaders.put("Authorization",  Collections.singletonList(("Basic " + authStringEnc)));
        commonHeaders.put(PARTNER_NAME.getHeaderName(), Collections.singletonList(PARTNER_NAME.getHeaderValue()));

        String requestIdValue = Logging.extractOrGenerateRequestId();
        commonHeaders.put(REQUEST_ID_HEADER_KEY, Collections.singletonList(requestIdValue));
        commonHeaders.put(ONAP_REQUEST_ID_HEADER_KEY, Collections.singletonList(requestIdValue));
        commonHeaders.put(INVOCATION_ID.getHeaderName(), Collections.singletonList(INVOCATION_ID.getHeaderValue()));

        boolean useSsl = true;
        if ( (mso_url != null) && ( !(mso_url.isEmpty()) ) ) {
            useSsl = mso_url.startsWith("https");
        }
        if (client == null) {

            try {
                if ( useSsl ) {
                    client = httpsAuthClient.getClient(HttpClientMode.WITHOUT_KEYSTORE);
                }
                else {
                    client = HttpBasicClient.getClient();
                }
            } catch (Exception e) {
                logger.info(EELFLoggerDelegate.errorLogger,methodname + " Unable to get the SSL client");
            }
        }

        return commonHeaders;
    }

    public <T> RestObjectWithRequestInfo<T> Get(T t, String path, RestObject<T> restObject, boolean warpException) {
        String methodName = "Get";

        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START_LOG);

        String url = null;
        String rawData = null;
        Integer status = null;

        try {
            restObject.set(t);
            url = systemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;

            MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();
            loggingService.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
                final Response cres = client.target(url)
                    .request()
                    .accept(APPLICATION_JSON)
                    .headers(commonHeaders)
                    .get();
            loggingService.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, cres);

            cres.bufferEntity();
            status = cres.getStatus();
            rawData = cres.readEntity(String.class);

            restObject.setStatusCode(status);

            if (status == 200 || status == 202) {
                t = (T) cres.readEntity(t.getClass());
                restObject.set(t);
                logger.debug(EELFLoggerDelegate.debugLogger, methodName + REST_API_SUCCESSFULL_LOG);

            } else {
                throw new GenericUncheckedException(new HttpException(methodName + WITH_STATUS + status + " (200 or 202 expected), url= " + url));
            }

            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " received status=" + status);

            return new RestObjectWithRequestInfo<>(HttpMethod.GET, url, restObject, status, rawData);
        } catch (RuntimeException e) {
            throw warpException ? new ExceptionWithRequestInfo(HttpMethod.GET, url, rawData, status, e) : e;
        }
    }

    @Override
    public <T> RestObject<T> GetForObject(String path, Class<T> clazz) {
        final String methodName = getMethodName();
        logger.debug(EELFLoggerDelegate.debugLogger, "start {}->{}({}, {})", getMethodCallerName(), methodName, path, clazz);

        String url = systemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
        logger.debug(EELFLoggerDelegate.debugLogger, "<== " +  methodName + " sending request to url= " + url);

        MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();
        loggingService.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
        final Response cres = client.target(url)
                .request()
                .accept(APPLICATION_JSON)
                .headers(commonHeaders)
                .get();
        loggingService.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, cres);
        final RestObject<T> restObject = cresToRestObject(cres, clazz);
        int status = cres.getStatus();

        if (status == 200 || status == 202) {
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + REST_API_SUCCESSFULL_LOG);
        } else {
            logger.debug(EELFLoggerDelegate.debugLogger,"<== " + methodName + WITH_STATUS +status+ URL_LOG +url);
        }

        logger.debug(EELFLoggerDelegate.debugLogger,methodName + " received status=" + status );

        return restObject;
    }

    public <T> RestObject<T> PostForObject(Object requestDetails, String path, Class<T> clazz) {
        logger.debug(EELFLoggerDelegate.debugLogger, REST_MSG_TEMPLATE, getMethodCallerName(), getMethodName(), requestDetails, path, clazz);
        return restCall(HttpMethod.POST, clazz, requestDetails, path);
    }

    public <T> RestObject<T> DeleteForObject(Object requestDetails, String path, Class<T> clazz) {
        logger.debug(EELFLoggerDelegate.debugLogger, REST_MSG_TEMPLATE, getMethodCallerName(), getMethodName(), requestDetails, path, clazz);
        return restCall(HttpMethod.DELETE, clazz, requestDetails, path);
    }

    @Override
    public void Post(String t, Object r, String path, RestObject<String> restObject) {
        logger.debug(EELFLoggerDelegate.debugLogger, REST_MSG_TEMPLATE, getMethodCallerName(), getMethodName(), t.getClass(), r, path);
        restObject.copyFrom(restCall(HttpMethod.POST, String.class, r, path));
    }

    public Invocation.Builder prepareClient(String path, String methodName) {
        MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();

        String url = systemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
        logger.debug(EELFLoggerDelegate.debugLogger,"<== " +  methodName + " sending request to url= " + url);
        // Change the content length
        return client.target(url)
                .request()
                .accept(APPLICATION_JSON)
                .headers(commonHeaders);
    }

    public <T> RestObject<T> restCall(HttpMethod httpMethod, Class<T> tClass, Object payload, String path) {
        return restCall(httpMethod, tClass, payload, path, Optional.empty());
    }


    /*
    user id is needed to be pass as X-RequestorID in new MSO flows like Delete instanceGroup
     */
    public <T> RestObject<T> restCall(HttpMethod httpMethod, Class<T> tClass, Object payload, String path, Optional<String> userId)  {
        String methodName = httpMethod.name();
        String url="";

        try {

            MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();
            userId.ifPresent(id->commonHeaders.put("X-RequestorID", Collections.singletonList(id)));

            url = systemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
            loggingService.logRequest(outgoingRequestsLogger, httpMethod, url, payload);
            // Change the content length
            final Invocation.Builder restBuilder = client.target(url)
                    .request()
                    .accept(APPLICATION_JSON)
                    .headers(commonHeaders)
                    .property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true)
                ;

            Invocation restInvocation = payload==null ?
                    restBuilder.build(httpMethod.name()) :
                    restBuilder.build(httpMethod.name(), Entity.entity(payload, MediaType.APPLICATION_JSON));
            final Response cres = restInvocation.invoke();

            loggingService.logResponse(outgoingRequestsLogger, httpMethod, url, cres);
            return cresToRestObject(cres, tClass);
        }
        catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger,"<== " + methodName + WITH_URL_LOG +url+ EXCEPTION_LOG + e.toString());
            throw e;
        }

    }

    private <T> RestObject<T> cresToRestObject(Response cres, Class<?> tClass) {
        RestObject<T> restObject = new RestObject<>();

        String rawEntity = null;
        try {
            cres.bufferEntity();
            rawEntity = cres.readEntity(String.class);
            restObject.setRaw(rawEntity);
            T t = (T) new ObjectMapper().readValue(rawEntity, tClass);
            restObject.set(t);
        }
        catch ( Exception e ) {
            try {
                logger.debug(EELFLoggerDelegate.debugLogger, "<== " + getMethodCallerName() + " Error reading response entity as " + tClass + ": , e="
                        + e.getMessage() + ", Entity=" + rawEntity);
            } catch (Exception e2) {
                logger.debug(EELFLoggerDelegate.debugLogger, "<== " + getMethodCallerName() + NO_RESPONSE_ENTITY_LOG
                        + e.getMessage());
            }
        }

        int status = cres.getStatus();
        restObject.setStatusCode (status);

        return restObject;
    }

    @Override
    public <T> void Put(T t, org.onap.vid.changeManagement.RequestDetailsWrapper r, String path, RestObject<T> restObject) {

        String methodName = "Put";
        String url="";

        logger.debug(EELFLoggerDelegate.debugLogger,"<== " +  methodName + START_LOG);

        try {

            MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();

            url = systemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
            loggingService.logRequest(outgoingRequestsLogger, HttpMethod.PUT, url, r);
            // Change the content length
            final Response cres = client.target(url)
                    .request()
                    .accept(APPLICATION_JSON)
                    .headers(commonHeaders)
                    //.header("content-length", 201)
                    .put(Entity.entity(r, MediaType.APPLICATION_JSON));

            loggingService.logResponse(outgoingRequestsLogger, HttpMethod.PUT, url, cres);

            try {
                t = (T) cres.readEntity(t.getClass());
                restObject.set(t);
            }
            catch ( Exception e ) {
                logger.debug(EELFLoggerDelegate.debugLogger,"<== " + methodName + NO_RESPONSE_ENTITY_LOG
                        + e.getMessage());
                throw e;
            }

            int status = cres.getStatus();
            restObject.setStatusCode (status);

            if ( status >= 200 && status <= 299 ) {
                logger.info(EELFLoggerDelegate.errorLogger, "<== " + methodName + REST_API_SUCCESSFULL_LOG);
                logger.debug(EELFLoggerDelegate.debugLogger,"<== " + methodName + REST_API_SUCCESSFULL_LOG);

            } else {
                logger.debug(EELFLoggerDelegate.debugLogger,"<== " + methodName + WITH_STATUS +status+ URL_LOG +url);
            }

        } catch (Exception e)
        {
            logger.debug(EELFLoggerDelegate.debugLogger,"<== " + methodName + WITH_URL_LOG +url+ EXCEPTION_LOG + e.toString());
            throw e;

        }
    }
}
