package org.onap.vid.scheduler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ SchedulerRestInterfaceTest.class, org.onap.vid.scheduler.RestObjects.TestSuite.class })
public class TestSuite { // nothing
}
