package org.onap.vid.mso.rest;

import org.junit.Test;
import org.onap.vid.domain.mso.InstanceIds;
import org.onap.vid.domain.mso.RequestStatus;


public class AsyncRequestStatusTest {

	private AsyncRequestStatus createTestSubject() {
		return new AsyncRequestStatus();
	}

	
	@Test
	public void testToString() throws Exception {
		AsyncRequestStatus testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}

	
	@Test
	public void testHashCode() throws Exception {
		AsyncRequestStatus testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.hashCode();
	}

	
	@Test
	public void testEquals() throws Exception {
		AsyncRequestStatus testSubject;
		Object other = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.equals(other);
	}
}