package org.openecomp.vid.mso.rest;

import org.junit.Test;
import org.openecomp.vid.domain.mso.InstanceIds;
import org.openecomp.vid.domain.mso.RequestStatus;


public class AsyncRequestStatusTest {

	private AsyncRequestStatus createTestSubject() {
		return new AsyncRequestStatus();
	}

	
	@Test
	public void testGetInstanceIds() throws Exception {
		AsyncRequestStatus testSubject;
		InstanceIds result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getInstanceIds();
	}

	
	@Test
	public void testSetInstanceIds() throws Exception {
		AsyncRequestStatus testSubject;
		InstanceIds instanceIds = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setInstanceIds(instanceIds);
	}

	
	@Test
	public void testGetRequestStatus() throws Exception {
		AsyncRequestStatus testSubject;
		RequestStatus result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRequestStatus();
	}

	
	@Test
	public void testSetRequestStatus() throws Exception {
		AsyncRequestStatus testSubject;
		RequestStatus requestStatus = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRequestStatus(requestStatus);
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