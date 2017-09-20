package org.openecomp.vid.scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.scheduler.SchedulerResponseWrappers.GetTimeSlotsWrapper;
import org.openecomp.vid.scheduler.SchedulerResponseWrappers.PostCreateNewVnfWrapper;
import org.openecomp.vid.scheduler.SchedulerResponseWrappers.PostSubmitVnfChangeTimeSlotsWrapper;
import org.openecomp.vid.scheduler.SchedulerUtil;
import org.openecomp.vid.scheduler.RestObjects.GetTimeSlotsRestObject;
import org.openecomp.vid.scheduler.RestObjects.PostCreateNewVnfRestObject;
import org.openecomp.vid.scheduler.RestObjects.PostSubmitVnfChangeRestObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SchedulerUtil {
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerUtil.class);
	
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

	public static GetTimeSlotsWrapper getTimeSlotsWrapResponse (GetTimeSlotsRestObject<String> rs) {	
		
		String resp_str = "";
		int status = 0;
		
		if ( rs != null ) {
			resp_str = rs.get();
			status = rs.getStatusCode();
		}
				
		GetTimeSlotsWrapper w = new GetTimeSlotsWrapper();
		
		w.setEntity(resp_str);
		w.setStatus (status);
		
		return (w);
	}
	
	public static PostSubmitVnfChangeTimeSlotsWrapper postSubmitNewVnfWrapResponse (PostSubmitVnfChangeRestObject<String> rs) {	
		
		String resp_str = "";
		int status = 0;
		String uuid = "";
		
		if ( rs != null ) {
			resp_str = rs.get();
			status = rs.getStatusCode();
			uuid = rs.getUUID();
		}
				
		PostSubmitVnfChangeTimeSlotsWrapper w = new PostSubmitVnfChangeTimeSlotsWrapper();
		
		w.setEntity(resp_str);
		w.setStatus (status);
		w.setUuid(uuid);
		
		return (w);
	}
	
	public static PostCreateNewVnfWrapper postCreateNewVnfWrapResponse (PostCreateNewVnfRestObject<String> rs) {	
		
		String resp_str = "";
		int status = 0;
		String uuid = "";
		
		if ( rs != null ) {
			resp_str = rs.get();
			status = rs.getStatusCode();
			uuid = rs.getUUID();
		}
				
		PostCreateNewVnfWrapper w = new PostCreateNewVnfWrapper();
		
		w.setEntity(resp_str);
		w.setStatus (status);
		w.setUuid(uuid);
		
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
	
}
