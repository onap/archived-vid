package org.openecomp.vid.mso;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ MsoRestIntTest.class, MsoUtilTest.class, MsoRestInterfaceTest.class, MsoLocalClientNewTest.class,
		RestMsoImplementationTest.class, org.openecomp.vid.mso.rest.TestSuite.class })
public class TestSuite { // nothing
}
