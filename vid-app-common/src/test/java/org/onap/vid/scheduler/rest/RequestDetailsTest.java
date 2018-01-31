package org.onap.vid.scheduler.rest;

import org.junit.Test;


public class RequestDetailsTest {

	private RequestDetails createTestSubject() {
		return new RequestDetails();
	}


	@Test
	public void testGetDomain() throws Exception {
		RequestDetails testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getDomain();
	}


	@Test
	public void testSetDomain() throws Exception {
		RequestDetails testSubject;
		String domain = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setDomain(domain);
	}
}