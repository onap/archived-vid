package org.onap.vid.scheduler;

import com.att.eelf.configuration.EELFLogger;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.security.Password;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.client.HttpBasicClient;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.utils.Logging;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

@Service
public class SchedulerRestInterface implements SchedulerRestInterfaceIfc {

    private Client client = null;

    @Autowired
    HttpsAuthClient httpsAuthClient;

    private MultivaluedHashMap<String, Object> commonHeaders;

    /** The logger. */
    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerRestInterface.class);
    final private static EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("scheduler");

    public void initRestClient() {
        logger.info("Starting to initialize rest client ");

        final String username;
        final String password;

		/*Setting user name based on properties*/
        String retrievedUsername = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_USER_NAME_VAL);
        if(retrievedUsername.isEmpty()) {
            username = "";
        } else {
            username = retrievedUsername;
        }

		/*Setting password based on properties*/
        String retrievedPassword = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_PASSWORD_VAL);
        if(retrievedPassword.isEmpty()) {
            password = "";
        } else {
            if (retrievedPassword.contains("OBF:")) {
                password = Password.deobfuscate(retrievedPassword);
            } else {
                password = retrievedPassword;
            }
        }

        String authString = username + ":" + password;

        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        commonHeaders = new MultivaluedHashMap<String, Object> ();
        commonHeaders.put("Authorization", Collections.singletonList("Basic " + authStringEnc));

        try {
            if ( !username.isEmpty() ) {
                client = httpsAuthClient.getClient(HttpClientMode.WITHOUT_KEYSTORE);
            }
            else {

                client = HttpBasicClient.getClient();
            }
        } catch (Exception e) {
            logger.error(" <== Unable to initialize rest client ", e);
        }

        logger.info("\t<== Client Initialized \n");
    }

    public <T> void Get (T t, String sourceId, String path, org.onap.vid.scheduler.RestObject<T> restObject ) {

        String methodName = "Get";
        String url = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
        initRestClient();
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
        final Response cres = client.target(url)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .headers(commonHeaders)
                .header(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId())
                .get();
        Logging.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, cres);
        int status = cres.getStatus();
        restObject.setStatusCode (status);

        if (status == 200) {
            t = (T) cres.readEntity(t.getClass());
            restObject.set(t);

        } else {
            throw new GenericUncheckedException(methodName + " with status="+ status + ", url= " + url );
        }

    }

    public <T> void Delete(T t, String sourceID, String path, org.onap.vid.scheduler.RestObject<T> restObject) {

        initRestClient();

        String url = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.DELETE, url);
        Response cres = client.target(url)
                .request()
                .accept("application/json")
                .headers(commonHeaders)
                .header(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId())
                .delete();
        Logging.logResponse(outgoingRequestsLogger, HttpMethod.DELETE, url, cres);

        int status = cres.getStatus();
        restObject.setStatusCode(status);

        t = (T) cres.readEntity(t.getClass());
        restObject.set(t);

    }

    public <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException
    {
        return clazz.newInstance();
    }

}
