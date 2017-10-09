package org.openecomp.vid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ RelatedToTest.class, org.openecomp.vid.aai.TestSuite.class })
public class TestSuite { // nothing
}
