package org.onap.vid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ RelatedToTest.class, org.onap.vid.aai.TestSuite.class })
public class TestSuite { // nothing
}
