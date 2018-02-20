package org.onap.vid.scheduler;

import java.util.Collections;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.security.Password;
import org.json.simple.JSONObject;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.client.HttpBasicClient;
import org.onap.vid.client.HttpsBasicClient;
import org.onap.vid.scheduler.SchedulerProperties;
import org.onap.vid.scheduler.RestObjects.RestObject;
import org.springframework.stereotype.Service;

import static org.onap.vid.utils.Logging.getHttpServletRequest;
import static org.onap.vid.utils.Logging.requestIdHeaderKey;

@Service
public class SchedulerRestInterface implements SchedulerRestInterfaceIfc {

	private static Client client = null;
		
	private MultivaluedHashMap<String, Object> commonHeaders;
	
	/** The logger. */
	static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerRestInterface.class);
	
	public SchedulerRestInterface() {
		super();
	}
	
	public void initRestClient()
	{	
		System.out.println( "\t <== Starting to initialize rest client ");
		
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
		commonHeaders.put("Authorization",  Collections.singletonList((Object) ("Basic " + authStringEnc)));		
					
		try {
			if ( !username.isEmpty() ) { 
				
				client = HttpsBasicClient.getClient();
			}
			else {
				
				client = HttpBasicClient.getClient();
			}
		} catch (Exception e) {
			System.out.println( " <== Unable to initialize rest client ");
		}
		
		System.out.println( "\t<== Client Initialized \n");
	}
		
	@SuppressWarnings("unchecked")
	public <T> void Get (T t, String sourceId, String path, org.onap.vid.scheduler.RestObject<T> restObject ) throws Exception {
		
		String methodName = "Get";
		String url = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
		
		
		System.out.println( "<== URL FOR GET : " + url + "\n");

        initRestClient();
		
		final Response cres = client.target(url)
			 .request()
	         .accept("application/json")
	         .headers(commonHeaders)
			 .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
			 .get();
				
		int status = cres.getStatus();
		restObject.setStatusCode (status);
		
		if (status == 200) {
			 t = (T) cres.readEntity(t.getClass());
			 restObject.set(t);
			
		 } else {
		     throw new Exception(methodName + " with status="+ status + ", url= " + url );
		 }

		return;
	}
		
	@SuppressWarnings("unchecked")
	public <T> void Post(T t, JSONObject requestDetails, String path, RestObject<T> restObject) throws Exception {
		
        String methodName = "Post";
        String url = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
		        
        System.out.println( "<== URL FOR POST : " + url + "\n");
     
        try {
            
            initRestClient();    
    
            // Change the content length
            final Response cres = client.target(url)
            	 .request()
                 .accept("application/json")
	        	 .headers(commonHeaders)
				 .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
				 .post(Entity.entity(requestDetails, MediaType.APPLICATION_JSON));
            
            try {
	   			t = (T) cres.readEntity(t.getClass());
	   			restObject.set(t);
            }
            catch ( Exception e ) {
            	
            	System.out.println("<== " + methodName + " : No response entity, this is probably ok, e=" + e.getMessage());
            }

            int status = cres.getStatus();
    		restObject.setStatusCode (status);    		
    		    		
    		if ( status >= 200 && status <= 299 ) {
    			    			
    			System.out.println( "<== " + methodName + " : REST api POST was successful!" + "\n");
    		
             } else {
            	 System.out.println( "<== " + methodName + " : FAILED with http status : "+status+", url = " + url + "\n");
             }    
   
        } catch (Exception e)
        {
        	System.out.println( "<== " + methodName + " : with url="+url+ ", Exception: " + e.toString() + "\n");
        	throw e;        
        }
    }

	@Override
	public void logRequest(JSONObject requestDetails) {}

	@SuppressWarnings("unchecked")
   	public <T> void Delete(T t, String sourceID, String path, org.onap.vid.scheduler.RestObject<T> restObject) {
	 
		String url="";
		Response cres = null;
		
		try {
			initRestClient();
			
			url = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
		
			cres = client.target(url)
					 .request()
			         .accept("application/json")
	        		 .headers(commonHeaders)
					 .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
			         //.entity(r)
			         .delete();
			       //  .method("DELETE", Entity.entity(r, MediaType.APPLICATION_JSON));
			         //.delete(Entity.entity(r, MediaType.APPLICATION_JSON));
			
			int status = cres.getStatus();
	    	restObject.setStatusCode (status);
	    			
			try {
	   			t = (T) cres.readEntity(t.getClass());
	   			restObject.set(t);
            }
            catch ( Exception e ) {
            }
   
        } 
		catch (Exception e)
        {     	
        	 throw e;        
        }
	}
	
	public <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException
	{
		return clazz.newInstance();
	}

}
