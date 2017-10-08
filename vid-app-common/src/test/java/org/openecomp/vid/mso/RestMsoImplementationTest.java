package org.openecomp.vid.mso;

import org.apache.poi.hssf.record.formula.functions.T;
import org.junit.Test;
import org.openecomp.vid.changeManagement.RequestDetails;
import org.openecomp.vid.changeManagement.RequestDetailsWrapper;

public class RestMsoImplementationTest {

	private RestMsoImplementation createTestSubject() {
		return new RestMsoImplementation();
	}	

	@Test
	public void testInitMsoClient() throws Exception {
		RestMsoImplementation testSubject;

		// default test
		testSubject = createTestSubject();
		testSubject.initMsoClient();
	}

	@Test
	public void testLogRequest() throws Exception {
		RestMsoImplementation testSubject;
		RequestDetails r = null;

		// test 1
		testSubject = createTestSubject();
		r = null;
		testSubject.logRequest(r);
	}

	@Test
	public void testLogRequest_1() throws Exception {
		RestMsoImplementation testSubject;
		RequestDetails r = null;

		// test 1
		testSubject = createTestSubject();
		r = null;
		testSubject.logRequest(r);
	}
}