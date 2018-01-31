package org.onap.vid.policy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.glassfish.jersey.client.ClientResponse;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.policy.PolicyResponseWrapper;
import org.onap.vid.policy.PolicyUtil;
import org.onap.vid.policy.RestObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PolicyUtil {
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PolicyUtil.class);
	
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	public static PolicyResponseWrapper wrapResponse ( String body, int statusCode ) {
		
		PolicyResponseWrapper w = new PolicyResponseWrapper();
		w.setStatus (statusCode);
		w.setEntity(body);
		
		return w;
	}
	
	public static PolicyResponseWrapper wrapResponse (ClientResponse cres) {	
		String resp_str = "";
		if ( cres != null ) {
			resp_str = cres.readEntity(String.class);
		}
		int statuscode = cres.getStatus();
		PolicyResponseWrapper w = PolicyUtil.wrapResponse ( resp_str, statuscode );
		return (w);
	}
	
	public static PolicyResponseWrapper wrapResponse (RestObject<String> rs) {	
		String resp_str = "";
		int status = 0;
		if ( rs != null ) {
			resp_str = rs.get();
			status = rs.getStatusCode();
		}
		PolicyResponseWrapper w = PolicyUtil.wrapResponse ( resp_str, status );
		return (w);
	}
	
	public static <T> String convertPojoToString ( T t ) throws com.fasterxml.jackson.core.JsonProcessingException {
		
		String methodName = "convertPojoToString";
		ObjectMapper mapper = new ObjectMapper();
		String r_json_str = "";
	    if ( t != null ) {
		    try {
		    	r_json_str = mapper.writeValueAsString(t);
		    }
		    catch ( com.fasterxml.jackson.core.JsonProcessingException j ) {
		    	logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Unable to parse object as json");
		    }
	    }
	    return (r_json_str);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
	}
}
