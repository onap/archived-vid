package org.onap.vid.policy;

import org.junit.Test;

public class PolicyResponseWrapperTest {

	private PolicyResponseWrapper createTestSubject() {
		return new PolicyResponseWrapper();
	}

	@Test
	public void testGetEntity() throws Exception {
		PolicyResponseWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getEntity();
	}

	@Test
	public void testGetStatus() throws Exception {
		PolicyResponseWrapper testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getStatus();
	}

	@Test
	public void testSetStatus() throws Exception {
		PolicyResponseWrapper testSubject;
		int v = 0;

		// default test
		testSubject = createTestSubject();
		testSubject.setStatus(v);
	}

	@Test
	public void testSetEntity() throws Exception {
		PolicyResponseWrapper testSubject;
		String v = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setEntity(v);
	}

	@Test
	public void testToString() throws Exception {
		PolicyResponseWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}

	@Test
	public void testGetResponse() throws Exception {
		PolicyResponseWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResponse();
	}
}