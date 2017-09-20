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

package org.openecomp.vid.scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;

public class SchedulerRestInt {
	
	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SchedulerRestInterface.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/** The request date format. */
	public DateFormat requestDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss z");
	
	public SchedulerRestInt() {
		requestDateFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
	}
}
