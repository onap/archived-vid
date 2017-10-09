package org.openecomp.vid.aai;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ SubscriberAaiResponseTest.class, AaiGetVnfResponseTest.class, ServicePropertiesTest.class,
		SubscriberWithFilterTest.class, VnfResultTest.class, org.openecomp.vid.aai.model.TestSuite.class })
public class TestSuite { // nothing
}
