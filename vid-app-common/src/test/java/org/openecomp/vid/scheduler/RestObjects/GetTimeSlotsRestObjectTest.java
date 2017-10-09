package org.openecomp.vid.scheduler.RestObjects;

import org.junit.Test;


public class GetTimeSlotsRestObjectTest {

	private GetTimeSlotsRestObject createTestSubject() {
		return new GetTimeSlotsRestObject();
	}


	@Test
	public void testSetUUID() throws Exception {
		GetTimeSlotsRestObject testSubject;
		String uuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setUUID(uuid);
	}


	@Test
	public void testGetUUID() throws Exception {
		GetTimeSlotsRestObject testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUUID();
	}
}