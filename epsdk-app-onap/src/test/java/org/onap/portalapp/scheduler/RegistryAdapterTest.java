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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onap.portalsdk.core.scheduler.Registerable;
import org.onap.portalsdk.workflow.services.WorkflowScheduleService;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class RegistryAdapterTest {

    private RegistryAdapter createTestSubject() {
        return new RegistryAdapter();
    }

    @Test
    public void testSetSchedulerBean() throws Exception {
        RegistryAdapter testSubject;
        SchedulerFactoryBean schedulerBean = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setSchedulerBean(schedulerBean);
        Assert.assertNotNull(testSubject);
    }

    @Test
    public void testGetSchedulerBean() throws Exception {
        RegistryAdapter testSubject;
        SchedulerFactoryBean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSchedulerBean();
        Assert.assertNotNull(testSubject);
    }

    @Test
    public void testGetRegistry() throws Exception {
        RegistryAdapter testSubject;
        Registerable result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRegistry();
    }

    @Test
    public void testSetRegistry() throws Exception {
        RegistryAdapter testSubject;
        Registerable registry = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRegistry(registry);
        Assert.assertNotNull(testSubject);
    }

    @Test
    public void testGetWorkflowScheduleService() throws Exception {
        RegistryAdapter testSubject;
        WorkflowScheduleService result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflowScheduleService();
        Assert.assertNotNull(testSubject);
    }

    @Test
    public void testSetWorkflowScheduleService() throws Exception {
        RegistryAdapter testSubject;
        WorkflowScheduleService workflowScheduleService = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflowScheduleService(workflowScheduleService);
        Assert.assertNotNull(testSubject);
    }
}