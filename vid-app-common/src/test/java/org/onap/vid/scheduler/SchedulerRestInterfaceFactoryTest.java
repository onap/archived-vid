package org.onap.vid.scheduler;

import org.junit.Test;

public class SchedulerRestInterfaceFactoryTest {

    private SchedulerRestInterfaceFactory createTestSubject() {
        return new SchedulerRestInterfaceFactory();
    }

    @Test
    public void testGetInstance() throws Exception {
        SchedulerRestInterfaceIfc result;

        // default test
        result = SchedulerRestInterfaceFactory.getInstance();
    }
}