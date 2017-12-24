package org.openecomp.vid.scheduler;

import org.openecomp.portalsdk.core.util.SystemProperties;


public class SchedulerProperties extends SystemProperties { 
	
	public static final String SCHEDULER_USER_NAME_VAL =  "scheduler.user.name";
		
	public static final String SCHEDULER_PASSWORD_VAL = "scheduler.password";
	
	public static final String SCHEDULER_SERVER_URL_VAL = "scheduler.server.url";
	
	public static final String SCHEDULER_CREATE_NEW_VNF_CHANGE_INSTANCE_VAL = "scheduler.create.new.vnf.change.instance";

	public static final String SCHEDULER_GET_TIME_SLOTS = "scheduler.get.time.slots";

	public static final String SCHEDULER_SUBMIT_NEW_VNF_CHANGE = "scheduler.submit.new.vnf.change";

	public static final String SCHEDULER_GET_SCHEDULES = "scheduler.get.schedules";

	public static final String SCHEDULER_DELETE_SCHEDULE = "scheduler.delete.schedule";

	public static final String GET_VERSION_BY_INVARIANT_ID = "aai_get_version_by_invariant_id";

}
