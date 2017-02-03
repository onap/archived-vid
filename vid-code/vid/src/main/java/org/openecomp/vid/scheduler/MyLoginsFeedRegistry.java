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

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import org.openecomp.portalsdk.core.scheduler.CronRegistry;
import org.openecomp.portalsdk.core.util.SystemProperties;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Component
@DependsOn({ "dataSource", "systemProperties" })
public class MyLoginsFeedRegistry extends CronRegistry {

	// @Autowired
	// private SystemProperties systemProperties;

	@Autowired
	public MyLoginsFeedRegistry(ComboPooledDataSource dataSource) {
		super(dataSource);
	}

	private static final String groupName = "AppGroup";
	private static final String jobName = "MyLoginsFeedJob";
	private static final String triggerName = "MyLoginsFeedTrigger";

	// @Bean
	public JobDetailFactoryBean jobDetailFactoryBean() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataSource", getDataSource());
		return jobDetailFactoryBean(groupName, jobName, MyLoginsFeedJob.class, map);
	}

	// @Bean
	public CronTriggerFactoryBean cronTriggerFactoryBean() throws ParseException {
		// "0 * * * * ? *"
		return cronTriggerFactoryBean(groupName, triggerName,
				SystemProperties.getProperty(SystemProperties.MYLOGINS_FEED_CRON));
	}

}
