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

package org.onap.vid.scheduler;

import org.onap.portalsdk.core.util.SystemProperties;


public class SchedulerProperties extends SystemProperties { 
	
	public static final String SCHEDULER_USER_NAME_VAL =  "scheduler.user.name";
		
	public static final String SCHEDULER_PASSWORD_VAL = "scheduler.password";
	
	public static final String SCHEDULER_SERVER_URL_VAL = "scheduler.server.url";

	public static final String SCHEDULER_GET_SCHEDULES = "scheduler.get.schedules";

	public static final String SCHEDULER_DELETE_SCHEDULE = "scheduler.delete.schedule";

	public static final String SCHEDULER_BASIC_AUTH =  "scheduler.basic.auth";


}
