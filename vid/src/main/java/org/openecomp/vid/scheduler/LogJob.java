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

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.conf.ExternalAppConfig;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LogJob extends QuartzJobBean {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(LogJob.class);

	
	@Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
	   // JobDataMap dataMap = ctx.getJobDetail().getJobDataMap();
 	   //int cnt = dataMap.getInt("");
	   // JobKey jobKey = ctx.getJobDetail().getKey();
	   logger.info(EELFLoggerDelegate.errorLogger, (Runtime.getRuntime().maxMemory() + " " + Runtime.getRuntime().maxMemory()));

	}

}
