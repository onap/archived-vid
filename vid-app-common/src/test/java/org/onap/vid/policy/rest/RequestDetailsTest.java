package org.onap.vid.policy.rest;

import org.junit.Test;


public class RequestDetailsTest {

	private RequestDetails createTestSubject() {
		return new RequestDetails();
	}


	@Test
	public void testGetPolicyName() throws Exception {
		RequestDetails testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getPolicyName();
	}


	@Test
	public void testSetPolicyName() throws Exception {
		RequestDetails testSubject;
		String policyName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setPolicyName(policyName);
	}
}