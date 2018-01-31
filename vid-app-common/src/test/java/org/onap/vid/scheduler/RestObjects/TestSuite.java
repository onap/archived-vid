package org.onap.vid.scheduler.RestObjects;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ RestObjectTest.class, GetTimeSlotsRestObjectTest.class, PostSubmitVnfChangeRestObjectTest.class,
		PostCreateNewVnfRestObjectTest.class })
public class TestSuite { // nothing
}
