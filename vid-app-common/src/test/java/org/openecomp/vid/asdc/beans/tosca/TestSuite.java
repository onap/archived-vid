package org.openecomp.vid.asdc.beans.tosca;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ GroupTest.class, TopologyTemplateTest.class, ToscaModelTest.class, CapabilityTest.class, ToscaMetadataTest.class,
		ConstraintTest.class, NodeTemplateTest.class })
public class TestSuite { // nothing
}
