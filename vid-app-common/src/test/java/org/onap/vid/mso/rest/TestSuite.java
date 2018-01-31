package org.onap.vid.mso.rest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(

{ RelatedModelTest.class, AsyncRequestStatusTest.class, RequestDetailsTest.class, 
		RelatedInstanceTest.class })
public class TestSuite { // nothing
}
