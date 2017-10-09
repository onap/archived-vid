package org.openecomp.vid.scheduler.SchedulerResponseWrappers;

import org.junit.Test;


public class SchedulerResponseWrapperTest {

	private SchedulerResponseWrapper createTestSubject() {
		return new SchedulerResponseWrapper();
	}

	
	@Test
	public void testGetEntity() throws Exception {
		SchedulerResponseWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getEntity();
	}

	
	@Test
	public void testGetStatus() throws Exception {
		SchedulerResponseWrapper testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getStatus();
	}

	
	@Test
	public void testSetStatus() throws Exception {
		SchedulerResponseWrapper testSubject;
		int v = 0;

		// default test
		testSubject = createTestSubject();
		testSubject.setStatus(v);
	}

	
	@Test
	public void testSetEntity() throws Exception {
		SchedulerResponseWrapper testSubject;
		String v = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setEntity(v);
	}

	
	@Test
	public void testToString() throws Exception {
		SchedulerResponseWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}

	
	@Test
	public void testGetResponse() throws Exception {
		SchedulerResponseWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResponse();
	}
}