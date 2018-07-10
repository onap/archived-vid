package org.onap.vid.mso;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.security.Password;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.client.HttpBasicClient;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.mso.rest.RestInterface;
import org.onap.vid.utils.Logging;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.onap.vid.utils.Logging.*;

/**
 * Created by pickjonathan on 26/06/2017.
 */
public class RestMsoImplementation implements RestInterface {

    public static final String START_LOG = " start";
    public static final String APPLICATION_JSON = "application/json";
    public static final String WITH_STATUS = " with status=";
    public static final String URL_LOG = ", url=";
    public static final String NO_RESPONSE_ENTITY_LOG = " No response entity, this is probably ok, e=";
    public static final String WITH_URL_LOG = " with url=";
    public static final String EXCEPTION_LOG = ", Exception: ";
    public static final String REST_API_SUCCESSFULL_LOG = " REST api was successfull!";
    public static final String REST_API_POST_WAS_SUCCESSFUL_LOG = " REST api POST was successful!";
    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RestMsoImplementation.class);
    private final EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("mso");

    /**
     * The Constant dateFormat.
     */
    static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    /** The client. */
    private Client client = null;

    @Autowired
    HttpsAuthClient httpsAuthClient;

    /** The common headers. */
    /**
     * Instantiates a new mso rest interface.
     */

    @SuppressWarnings("Duplicates")
    @Override
    public MultivaluedHashMap<String, Object> initMsoClient()
    {
        final String methodname = "initRestClient()";

        final String username = SystemProperties.getProperty(MsoProperties.MSO_USER_NAME);
        final String password = SystemProperties.getProperty(MsoProperties.MSO_PASSWORD);
        final String mso_url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL);
        final String decrypted_password = Password.deobfuscate(password);

        String authString = username + ":" + decrypted_password;

        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        MultivaluedHashMap<String, Object> commonHeaders = new MultivaluedHashMap();
        commonHeaders.put("Authorization",  Collections.singletonList(("Basic " + authStringEnc)));
		//Pass calling application identifier to SO
  		commonHeaders.put("X-FromAppId", Collections.singletonList(SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME)));
        try {
            commonHeaders.put(REQUEST_ID_HEADER_KEY, Collections.singletonList(Logging.extractOrGenerateRequestId()));
        }
        catch (IllegalStateException e){
            //in async jobs we don't have any HttpServletRequest
            commonHeaders.put(REQUEST_ID_HEADER_KEY, Collections.singletonList(UUID.randomUUID().toString()));
        }


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
                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  methodname + " Unable to get the SSL client");
            }
        }

        return commonHeaders;
    }

    public <T> void  Get (T t, String sourceId, String path, RestObject<T> restObject ) {
        String methodName = "Get";

        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START_LOG);

        String url="";
        restObject.set(t);

        url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;

        MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
        final Response cres = client.target(url)
                .request()
                .accept(APPLICATION_JSON)
                .headers(commonHeaders)
                .get();
        Logging.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, cres);
        int status = cres.getStatus();
        restObject.setStatusCode (status);

        if (status == 200 || status == 202) {
            t = (T) cres.readEntity(t.getClass());
            restObject.set(t);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + REST_API_SUCCESSFULL_LOG);

        } else {
            throw new GenericUncheckedException(methodName + WITH_STATUS + status + ", url= " + url );
        }

        logger.debug(EELFLoggerDelegate.debugLogger,methodName + " received status=" + status );

        return;
    }

    public <T> RestObject<T> GetForObject(String sourceID, String path, Class<T> clazz) {
        final String methodName = getMethodName();
        logger.debug(EELFLoggerDelegate.debugLogger, "start {}->{}({}, {}, {})", getMethodCallerName(), methodName, sourceID, path, clazz);

        String url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " +  methodName + " sending request to url= " + url);

        MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
        final Response cres = client.target(url)
                .request()
                .accept(APPLICATION_JSON)
                .headers(commonHeaders)
                .get();
        Logging.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, cres);
        final RestObject<T> restObject = cresToRestObject(cres, clazz);
        int status = cres.getStatus();

        if (status == 200 || status == 202) {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + REST_API_SUCCESSFULL_LOG);
        } else {
            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + WITH_STATUS +status+ URL_LOG +url);
        }

        logger.debug(EELFLoggerDelegate.debugLogger,methodName + " received status=" + status );

        return restObject;
    }

    @Override
    public <T> void Delete(T t, Object r, String sourceID, String path, RestObject<T> restObject) {

        String methodName = "Delete";
        String url="";
        Response cres = null;

        logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + START_LOG);

        try {
            MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();

            url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
            Logging.logRequest(outgoingRequestsLogger, HttpMethod.DELETE, url, r);
            cres = client.target(url)
                    .request()

                    .accept(APPLICATION_JSON)
                    .headers(commonHeaders)
                    //.entity(r)
                    .build("DELETE", Entity.entity(r, MediaType.APPLICATION_JSON)).invoke();
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.DELETE, url, cres);
            int status = cres.getStatus();
            restObject.setStatusCode (status);

            if (status == 404) { // resource not found
                String msg = "Resource does not exist...: " + cres.getStatus();
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + msg);
            } else if (status == 200  || status == 204){
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + "Resource " + url + " deleted");
            } else if (status == 202) {
                String msg = "Delete in progress: " + status;
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + msg);
            }
            else {
                String msg = "Deleting Resource failed: " + status;
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + msg);
            }

            try {
                t = (T) cres.readEntity(t.getClass());
                restObject.set(t);
            }
            catch ( Exception e ) {
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + NO_RESPONSE_ENTITY_LOG
                        + e.getMessage());
            }

        }
        catch (Exception e)
        {
            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + WITH_URL_LOG +url+ EXCEPTION_LOG + e.toString());
            throw e;

        }
    }

    public <T> RestObject<T> PostForObject(Object requestDetails, String sourceID, String path, Class<T> clazz) {
        logger.debug(EELFLoggerDelegate.debugLogger, "start {}->{}({}, {}, {}, {})", getMethodCallerName(), getMethodName(), requestDetails, sourceID, path, clazz);
        RestObject<T> restObject = new RestObject<>();
        Post(clazz, requestDetails, path, restObject);
        return restObject;
    }

    @Override
    public <T> void Post(T t, Object r, String sourceID, String path, RestObject<T> restObject) {
        logger.debug(EELFLoggerDelegate.debugLogger, "start {}->{}({}, {}, {}, {})", getMethodCallerName(), getMethodName(), t.getClass(), r, sourceID, path);
        Post(t.getClass(), r, path, restObject);
    }

    public Invocation.Builder prepareClient(String path, String methodName) {
        MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();

        String url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
        logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " sending request to url= " + url);
        // Change the content length
        return client.target(url)
                .request()
                .accept(APPLICATION_JSON)
                .headers(commonHeaders);
    }



    public <T> void Post(Class<?> tClass, Object requestDetails, String path, RestObject<T> restObject)  {
        String methodName = "Post";
        String url="";

        try {

            MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();

            url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
            Logging.logRequest(outgoingRequestsLogger, HttpMethod.POST, url, requestDetails);
            // Change the content length
            final Response cres = client.target(url)
                    .request()
                    .accept(APPLICATION_JSON)
                    .headers(commonHeaders)
                    .post(Entity.entity(requestDetails, MediaType.APPLICATION_JSON));
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.POST, url, cres);
            final RestObject<T> cresToRestObject = cresToRestObject(cres, tClass);
            restObject.set(cresToRestObject.get());
            restObject.setStatusCode(cresToRestObject.getStatusCode());
            restObject.setRaw(cresToRestObject.getRaw());

            int status = cres.getStatus();
            restObject.setStatusCode (status);

            if ( status >= 200 && status <= 299 ) {
                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + REST_API_POST_WAS_SUCCESSFUL_LOG);
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + REST_API_POST_WAS_SUCCESSFUL_LOG);

            } else {
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + WITH_STATUS +status+ URL_LOG +url);
            }

        } catch (Exception e)
        {
            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + WITH_URL_LOG +url+ EXCEPTION_LOG + e.toString());
            throw e;

        }

        logger.debug(EELFLoggerDelegate.debugLogger, "end {}() => ({}){}", getMethodName(), tClass, restObject);
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
                logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + getMethodCallerName() + " Error reading response entity as " + tClass + ": , e="
                        + e.getMessage() + ", Entity=" + rawEntity);
            } catch (Exception e2) {
                logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + getMethodCallerName() + NO_RESPONSE_ENTITY_LOG
                        + e.getMessage());
            }
        }

        int status = cres.getStatus();
        restObject.setStatusCode (status);

        return restObject;

    }

    @Override
    public <T> void Put(T t, org.onap.vid.changeManagement.RequestDetailsWrapper r, String sourceID, String path, RestObject<T> restObject) {

        String methodName = "Put";
        String url="";

        logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + START_LOG);

        try {

            MultivaluedHashMap<String, Object> commonHeaders = initMsoClient();

            url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
            Logging.logRequest(outgoingRequestsLogger, HttpMethod.PUT, url, r);
            // Change the content length
            final Response cres = client.target(url)
                    .request()
                    .accept(APPLICATION_JSON)
                    .headers(commonHeaders)
                    //.header("content-length", 201)
                    //.header("X-FromAppId",  sourceID)
                    .put(Entity.entity(r, MediaType.APPLICATION_JSON));

            Logging.logResponse(outgoingRequestsLogger, HttpMethod.PUT, url, cres);

            try {
                t = (T) cres.readEntity(t.getClass());
                restObject.set(t);
            }
            catch ( Exception e ) {
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + NO_RESPONSE_ENTITY_LOG
                        + e.getMessage());
            }

            int status = cres.getStatus();
            restObject.setStatusCode (status);

            if ( status >= 200 && status <= 299 ) {
                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + REST_API_POST_WAS_SUCCESSFUL_LOG);
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + REST_API_POST_WAS_SUCCESSFUL_LOG);

            } else {
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + WITH_STATUS +status+ URL_LOG +url);
            }

        } catch (Exception e)
        {
            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + WITH_URL_LOG +url+ EXCEPTION_LOG + e.toString());
            throw e;

        }
    }
}
