package org.onap.vid.scheduler;

import org.onap.portalsdk.core.util.SystemProperties;


public class SchedulerProperties extends SystemProperties { 
	
	public static final String SCHEDULER_USER_NAME_VAL =  "scheduler.user.name";
		
	public static final String SCHEDULER_PASSWORD_VAL = "scheduler.password";
	
	public static final String SCHEDULER_SERVER_URL_VAL = "scheduler.server.url";

	public static final String SCHEDULER_GET_SCHEDULES = "scheduler.get.schedules";

	public static final String SCHEDULER_DELETE_SCHEDULE = "scheduler.delete.schedule";


}
