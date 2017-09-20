package org.openecomp.vid.mso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.security.Password;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.changeManagement.MsoRequestDetails;
import org.openecomp.vid.client.HttpBasicClient;
import org.openecomp.vid.client.HttpsBasicClient;
import org.openecomp.vid.mso.rest.RequestDetails;
import org.openecomp.vid.mso.rest.RestInterface;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * Created by pickjonathan on 26/06/2017.
 */
public class RestMsoImplementation implements RestInterface {

    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RestMsoImplementation.class);

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    /** The client. */
    private static Client client = null;

    /** The common headers. */
    private MultivaluedHashMap<String, Object> commonHeaders;
    /**
     * Instantiates a new mso rest interface.
     */

    @Override
    public void initMsoClient()
    {
        final String methodname = "initRestClient()";

        final String username = SystemProperties.getProperty(MsoProperties.MSO_USER_NAME);
        final String password = SystemProperties.getProperty(MsoProperties.MSO_PASSWORD);
        final String mso_url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL);
        final String decrypted_password = Password.deobfuscate(password);

        String authString = username + ":" + decrypted_password;

        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        commonHeaders = new MultivaluedHashMap<String, Object>();
        commonHeaders.put("Authorization",  Collections.singletonList((Object) ("Basic " + authStringEnc)));

        boolean use_ssl = true;
        if ( (mso_url != null) && ( !(mso_url.isEmpty()) ) ) {
            if ( mso_url.startsWith("https")) {
                use_ssl = true;
            }
            else {
                use_ssl = false;
            }
        }
        if (client == null) {

            try {
                if ( use_ssl ) {
                    //logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  methodname + " getting HttpsBasicClient with username=" + username
                    //		+ " password=" + password);
                    client = HttpsBasicClient.getClient();
                }
                else {
                    //logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  methodname + " getting HttpsBasicClient with username=" + username
                    //		+ " password=" + password);
                    client = HttpBasicClient.getClient();
                }
            } catch (Exception e) {
                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  methodname + " Unable to get the SSL client");
            }
        }
    }

    public <T> void  Get (T t, String sourceId, String path, RestObject<T> restObject ) throws Exception {
        String methodName = "Get";

        logger.debug(EELFLoggerDelegate.debugLogger, methodName + " start");

        String url="";
        restObject.set(t);

        url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " +  methodName + " sending request to url= " + url);

        initMsoClient();

        final Response cres = client.target(url)
                .request()
                .accept("application/json")
                .headers(commonHeaders)
                .get();

        int status = cres.getStatus();
        restObject.setStatusCode (status);

        if (status == 200 || status == 202) {
            t = (T) cres.readEntity(t.getClass());
            restObject.set(t);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " REST api was successfull!");

        } else {
            throw new Exception(methodName + " with status="+ status + ", url= " + url );
        }

        logger.debug(EELFLoggerDelegate.debugLogger,methodName + " received status=" + status );

        return;
    }

    @Override
    public <T> void Delete(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) {

        String methodName = "Delete";
        String url="";
        Response cres = null;

        logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " start");
        logRequest (r);

        try {
            initMsoClient();

            url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + " methodName sending request to: " + url);

            cres = client.target(url)
                    .request()
                    .accept("application/json")
                    .headers(commonHeaders)
                    //.entity(r)
                    .build("DELETE", Entity.entity(r, MediaType.APPLICATION_JSON)).invoke();
            //  .method("DELETE", Entity.entity(r, MediaType.APPLICATION_JSON));
            //.delete(Entity.entity(r, MediaType.APPLICATION_JSON));

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
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " No response entity, this is probably ok, e="
                        + e.getMessage());
            }

        }
        catch (Exception e)
        {
            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " with url="+url+ ", Exception: " + e.toString());
            throw e;

        }
    }
    
    @Override
    public <T> void Post(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) throws Exception {
        String methodName = "Post";
        String url="";

        logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " start");

        logRequest (r);
        try {

            initMsoClient();

            url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " sending request to url= " + url);
            // Change the content length
            final Response cres = client.target(url)
                    .request()
                    .accept("application/json")
                    .headers(commonHeaders)
                    //.header("content-length", 201)
                    //.header("X-FromAppId",  sourceID)
                    .post(Entity.entity(r, MediaType.APPLICATION_JSON));

            try {
                t = (T) cres.readEntity(t.getClass());
                restObject.set(t);
            }
            catch ( Exception e ) {
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " No response entity, this is probably ok, e="
                        + e.getMessage());
            }

            int status = cres.getStatus();
            restObject.setStatusCode (status);

            if ( status >= 200 && status <= 299 ) {
                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + " REST api POST was successful!");
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " REST api POST was successful!");

            } else {
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " with status="+status+", url="+url);
            }

        } catch (Exception e)
        {
            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " with url="+url+ ", Exception: " + e.toString());
            throw e;

        }
    }

    @Override
    public void logRequest(RequestDetails r) {
        String methodName = "logRequest";
        ObjectMapper mapper = new ObjectMapper();
        String r_json_str = "";
        if ( r != null ) {
            r_json_str = r.toString();
            try {
                r_json_str = mapper.writeValueAsString(r);
            }
            catch ( com.fasterxml.jackson.core.JsonProcessingException j ) {
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Unable to parse request as json");
            }
        }
        logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Request=(" + r_json_str + ")");
    }
    
    @Override
    public void logRequest(org.openecomp.vid.changeManagement.RequestDetails r) {
        String methodName = "logRequest";
        ObjectMapper mapper = new ObjectMapper();
        String r_json_str = "";
        if ( r != null ) {
            r_json_str = r.toString();
            try {
                r_json_str = mapper.writeValueAsString(r);
            }
            catch ( com.fasterxml.jackson.core.JsonProcessingException j ) {
                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Unable to parse request as json");
            }
        }
        logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Request=(" + r_json_str + ")");
    }

	@Override
	public <T> void Put(T t, org.openecomp.vid.changeManagement.RequestDetailsWrapper r, String sourceID, String path, RestObject<T> restObject) throws Exception {
		
	     String methodName = "Put";
	        String url="";

	        logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " start");

//	        logRequest (r);
	        try {

	            initMsoClient();

	            url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) + path;
	            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " sending request to url= " + url);
	            // Change the content length
	            final Response cres = client.target(url)
	                    .request()
	                    .accept("application/json")
	                    .headers(commonHeaders)
	                    //.header("content-length", 201)
	                    //.header("X-FromAppId",  sourceID)
	                    .put(Entity.entity(r, MediaType.APPLICATION_JSON));

	            try {
	                t = (T) cres.readEntity(t.getClass());
	                restObject.set(t);
	            }
	            catch ( Exception e ) {
	                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " No response entity, this is probably ok, e="
	                        + e.getMessage());
	            }

	            int status = cres.getStatus();
	            restObject.setStatusCode (status);

	            if ( status >= 200 && status <= 299 ) {
	                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + " REST api POST was successful!");
	                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " REST api POST was successful!");

	            } else {
	                logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " with status="+status+", url="+url);
	            }

	        } catch (Exception e)
	        {
	            logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " + methodName + " with url="+url+ ", Exception: " + e.toString());
	            throw e;

	        }		
	}
}
