package org.onap.vid.policy;

import org.junit.Test;
import org.onap.vid.policy.rest.RequestDetails;

public class PolicyRestIntTest {

	private PolicyRestInt createTestSubject() {
		return new PolicyRestInt();
	}

	@Test
	public void testLogRequest() throws Exception {
		PolicyRestInt testSubject;
		RequestDetails r = null;

		// test 1
		testSubject = createTestSubject();
		r = null;
		testSubject.logRequest(r);
	}
}