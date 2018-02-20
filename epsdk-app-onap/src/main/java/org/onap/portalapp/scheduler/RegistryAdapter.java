/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.onap.portalsdk.core.scheduler.Registerable;
import org.onap.portalsdk.workflow.services.WorkflowScheduleService;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class RegistryAdapter {

	@Autowired
	private Registerable registry;

	@Autowired
	private WorkflowScheduleService workflowScheduleService;

	private SchedulerFactoryBean schedulerBean;

	Trigger [] trigger = new Trigger[1];

	public Trigger[] getTriggers() {
		registry.registerTriggers();
		List<Trigger> allTriggers = new ArrayList<>();
		List<Trigger> coreTriggers = addCoreTriggers();
		final Trigger[] extTriggerArray = registry.getTriggers();
		allTriggers.addAll(Arrays.asList(extTriggerArray));
		allTriggers.addAll(coreTriggers);
		return allTriggers.toArray(trigger);
	}

	public List<Trigger> addCoreTriggers() {
		// On startup of the application after crash recovery, invoke workflow
		// schedule trigger
		List<Trigger> triggers = getWorkflowScheduleService().triggerWorkflowScheduling();
		return triggers;
	}

	public void setSchedulerBean(final SchedulerFactoryBean schedulerBean) {
		this.schedulerBean = schedulerBean;
	}

	public SchedulerFactoryBean getSchedulerBean() {
		return schedulerBean;
	}

	public Registerable getRegistry() {
		return registry;
	}

	public void setRegistry(Registerable registry) {
		this.registry = registry;
	}

	public WorkflowScheduleService getWorkflowScheduleService() {
		return workflowScheduleService;
	}

	public void setWorkflowScheduleService(WorkflowScheduleService workflowScheduleService) {
		this.workflowScheduleService = workflowScheduleService;
	}

}
