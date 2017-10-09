package org.openecomp.vid.scheduler.SchedulerResponseWrappers;

import org.junit.Test;


public class PostSubmitVnfChangeTimeSlotsWrapperTest {

	private PostSubmitVnfChangeTimeSlotsWrapper createTestSubject() {
		return new PostSubmitVnfChangeTimeSlotsWrapper();
	}


	@Test
	public void testGetUuid() throws Exception {
		PostSubmitVnfChangeTimeSlotsWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUuid();
	}


	@Test
	public void testSetUuid() throws Exception {
		PostSubmitVnfChangeTimeSlotsWrapper testSubject;
		String v = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setUuid(v);
	}


	@Test
	public void testToString() throws Exception {
		PostSubmitVnfChangeTimeSlotsWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}


	@Test
	public void testGetResponse() throws Exception {
		PostSubmitVnfChangeTimeSlotsWrapper testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResponse();
	}
}