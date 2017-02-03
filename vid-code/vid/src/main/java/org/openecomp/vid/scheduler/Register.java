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

import java.util.ArrayList;
import java.util.List;

import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.scheduler.Registerable;
import org.openecomp.portalsdk.core.util.SystemProperties;


@Component
@DependsOn({"logRegistry", "myLoginsFeedRegistry", "systemProperties"})
public class Register implements Registerable {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(Register.class);


	private List<Trigger> scheduleTriggers = new ArrayList<Trigger>();
	Trigger trigger[] = new Trigger[1];

	
	@Autowired
	private LogRegistry logRegistry;
	
	@Autowired
	private MyLoginsFeedRegistry myLoginsFeedRegistry;
	
	@Override
	public Trigger[] getTriggers() {
		return getScheduleTriggers().toArray(trigger);
	}

	@Override
	public void registerTriggers() {
		// if the property value is not available; the cron will not be added and can be ignored. its safe to ignore the exceptions
		try {
			if(SystemProperties.getProperty(SystemProperties.LOG_CRON) != null)
				getScheduleTriggers().add(logRegistry.getTrigger());
			
		} catch(IllegalStateException ies) {
			logger.info(EELFLoggerDelegate.errorLogger, ("Log Cron not available"));
		}
			
		try {
			if(SystemProperties.getProperty(SystemProperties.MYLOGINS_FEED_CRON) != null)
				getScheduleTriggers().add(myLoginsFeedRegistry.getTrigger());
			
		} catch(IllegalStateException ies) {
			logger.info(EELFLoggerDelegate.errorLogger, ("MyLogins Cron not available"));
		}
		
	}
	
	
	public List<Trigger> getScheduleTriggers() {
		return scheduleTriggers;
	}

	public void setScheduleTriggers(List<Trigger> scheduleTriggers) {
		this.scheduleTriggers = scheduleTriggers;
	}
	
	

	
	

}
