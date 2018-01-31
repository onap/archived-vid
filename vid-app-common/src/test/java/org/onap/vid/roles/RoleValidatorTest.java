package org.onap.vid.roles;

import org.junit.Test;
import org.onap.vid.mso.rest.RequestDetails;

public class RoleValidatorTest {

	private RoleValidator createTestSubject() {
		return new RoleValidator(null);
	}

	@Test
	public void testIsSubscriberPermitted() throws Exception {
		RoleValidator testSubject;
		String subscriberName = "";
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.isSubscriberPermitted(subscriberName);
	}

	@Test
	public void testIsServicePermitted() throws Exception {
		RoleValidator testSubject;
		String subscriberName = "";
		String serviceType = "";
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.isServicePermitted(subscriberName, serviceType);
	}

	@Test
	public void testIsMsoRequestValid() throws Exception {
		RoleValidator testSubject;
		RequestDetails mso_request = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.isMsoRequestValid(mso_request);
	}

	@Test
	public void testIsTenantPermitted() throws Exception {
		RoleValidator testSubject;
		String globalCustomerId = "";
		String serviceType = "";
		String tenant = "";
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.isTenantPermitted(globalCustomerId, serviceType, tenant);
	}
}