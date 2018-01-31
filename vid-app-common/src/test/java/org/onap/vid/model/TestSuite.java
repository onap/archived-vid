package org.onap.vid.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ ModelUtilTest.class, NewServiceTest.class, VolumeGroupTest.class, NewNodeTest.class, ServiceModelTest.class,
		WorkflowTest.class, NewVNFTest.class })
public class TestSuite { // nothing
}
