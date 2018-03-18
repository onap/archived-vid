package org.onap.portalapp.scheduler;

import java.util.List;

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
    }

    @Test
    public void testGetSchedulerBean() throws Exception {
        RegistryAdapter testSubject;
        SchedulerFactoryBean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSchedulerBean();
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
    }

    @Test
    public void testGetWorkflowScheduleService() throws Exception {
        RegistryAdapter testSubject;
        WorkflowScheduleService result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflowScheduleService();
    }

    @Test
    public void testSetWorkflowScheduleService() throws Exception {
        RegistryAdapter testSubject;
        WorkflowScheduleService workflowScheduleService = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflowScheduleService(workflowScheduleService);
    }
}