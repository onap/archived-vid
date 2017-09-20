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
package org.openecomp.aai.util;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.eclipse.jetty.util.security.Password;
import org.openecomp.aai.util.AAIProperties;
import org.openecomp.aai.util.HttpsAuthClient;
/**
 * The Class AAIRestInterface.
 */
public class AAIRestInterface {

	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AAIRestInterface.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/** The client. */
	private static Client client = null;

	/** The rest srvr base URL. */
	private String restSrvrBaseURL;
	
	/** The certificate path. */
	public String certificatePath = "";
	
	/**
	 * Instantiates a new AAI rest interface.
	 *
	 * @param certPath the cert path
	 */
	public AAIRestInterface(String certPath)
	{
		certificatePath = certPath;
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
	
	/**
	 * Inits the rest client.
	 */
	private void initRestClient()
	{
		String methodName = "initRestClient";
		
		if (client == null) {
			try {
				client = HttpsAuthClient.getClient(certificatePath);
			}
			catch (KeyManagementException e){
				logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== KeyManagementException in " + methodName + e.toString());
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== KeyManagementException in " + methodName + e.toString());
			} catch (Exception e) {
				logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== Exception in REST call to DB in initRestClient" + e.toString());
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== Exception in REST call to DB : " + e.toString());
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
	 * @throws UnsupportedEncodingException 
	 */
	public Response RestGet(String fromAppId, String transId, String requestUri, boolean xml) throws UnsupportedEncodingException  {
		String methodName = "RestGet";
		
		String responseType = "application/json";
		if (xml)
          	responseType = "application/xml";
		  
		initRestClient();

		String clientCert = SystemProperties.getProperty(AAIProperties.AAI_USE_CLIENT_CERT);
		
		boolean useClientCert = false;
		if (clientCert != null && 
				SystemProperties.getProperty(AAIProperties.AAI_USE_CLIENT_CERT).equalsIgnoreCase("true")) {
			useClientCert  = true;
		}
		String url = "";
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
	
		url = SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL) + requestUri;
		
	    try {
	    	// what is the point of this, just to check syntax??
		    URL urlObj= new URL(url);
			URI uri = new URI(urlObj.getProtocol(), urlObj.getUserInfo(), urlObj.getHost(), urlObj.getPort(), urlObj.getPath(), urlObj.getQuery(), urlObj.getRef());
			url = uri.toASCIIString();
	    } catch (URISyntaxException | MalformedURLException e) {
	    	 logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " +  methodName + " bad URL");
	    	 return null;
		}
	    logger.debug(dateFormat.format(new Date()) + "<== " + url + " for the get REST API");

	    final Response cres;
	    if (useClientCert == true) { 
	    	 cres = client.target(url)
	    			 .request()
	    	         .accept(responseType)
	    	         .header("X-TransactionId", transId)
	    	         .header("X-FromAppId",  fromAppId)
	    	         .header("Content-Type", "application/json")
	    	         .get();
	    } else { 
			
			String vidUsername = SystemProperties.getProperty(AAIProperties.AAI_VID_USERNAME);
			String vidPassword = Password.deobfuscate(SystemProperties.getProperty(AAIProperties.AAI_VID_PASSWD_X));
			String encodeThis = vidUsername + ":" + vidPassword;
			
			cres = client.target(url)
					.request()
					.accept(responseType)
					.header("X-TransactionId", transId)
					.header("X-FromAppId",  fromAppId)
					.header("Content-Type", "application/json")
					.header("Authorization", "Basic " + Base64.getEncoder().encodeToString(encodeThis.getBytes("utf-8")))
					.get();
	    }
//		String r = cres.readEntity(String.class);
		if (cres.getStatus() == 200) {
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " REST api GET was successful!");
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + " REST api GET was successful!");
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName +" with status="+cres.getStatus()+", url="+url);
		}
		return cres;
//		 logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName +" resp=" + r );
//		 return r;
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
		logger.debug(dateFormat.format(new Date()) + "<== " +  methodName + " start");
		
		initRestClient();
		String request = "{}";
		url = SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL) + path;			
		final Response cres = client.target(url)
				 .request()
		         .accept("application/json")
		         .header("X-TransactionId", transId)
		         .header("X-FromAppId",  sourceID)
		         //.entity(request)
		         .delete();
		
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
     * @param transId the trans id
     * @param path the path
     * @param payload the payload
     * @param xml the xml
     * @return the string
     */
    public Response RestPut(String fromAppId,  String transId,  String path, String payload, boolean xml) {
        String methodName = "RestPost";
        String url="";
        transId = UUID.randomUUID().toString();
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " +  methodName + " start");       
        
        try {
            
        	String responseType = "application/json";
        	if (xml)
               	responseType = "application/xml";
        	   
            initRestClient();    
    
            url = SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL) + path;

            final Response cres = client.target(url)
                 .request()
                 .accept(responseType)
                 .header("X-TransactionId", transId)
                 .header("X-FromAppId",  fromAppId)
                 .put(Entity.entity(payload, MediaType.APPLICATION_JSON));
            
        	if (cres.getStatus() == 200 && cres.getStatus() <= 299) {
        		logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + " REST api POST was successful!");
    			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " REST api POST was successful!");
             } else {
    			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName +" with status="+cres.getStatus()+", url="+url);
    		}
    		return cres;
        } catch (Exception e) {
        	logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " with url="+url+ ", Exception: " + e.toString());
        }
        return null;
    }
	
	
    
    /**
     * Rest post.
     *
     * @param fromAppId the from app id
     * @param transId the trans id
     * @param path the path
     * @param payload the payload
     * @param xml the xml
     * @return the string
     */
    public Response RestPost(String fromAppId,  String transId,  String path, String payload, boolean xml) {
        String methodName = "RestPost";
        String url="";
        transId = UUID.randomUUID().toString();
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " +  methodName + " start");       
        
        try {
            
        	String responseType = "application/json";
        	if (xml)
               	responseType = "application/xml";
        	   
            initRestClient();    
    
            url = SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL_BASE) + path;

            String vidUsername = SystemProperties.getProperty(AAIProperties.AAI_VID_USERNAME);
			String vidPassword = Password.deobfuscate(SystemProperties.getProperty(AAIProperties.AAI_VID_PASSWD_X));
			String encodeThis = vidUsername + ":" + vidPassword;
			
            final Response cres = client.target(url)
                 .request()
                 .accept(responseType)
                 .header("X-TransactionId", transId)
                 .header("X-FromAppId",  fromAppId)
                 .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(encodeThis.getBytes("utf-8")))
                 .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
            
        	if (cres.getStatus() == 200 && cres.getStatus() <= 299) {
        		logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + " REST api POST was successful!");
    			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " REST api POST was successful!");
             } else {
    			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName +" with status="+cres.getStatus()+", url="+url);
    		}
    		return cres;
        } catch (Exception e) {
        	logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " with url="+url+ ", Exception: " + e.toString());
        }
        return null;
    }
    
}
