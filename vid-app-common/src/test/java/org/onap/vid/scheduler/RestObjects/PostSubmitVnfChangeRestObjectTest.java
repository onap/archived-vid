package org.onap.vid.scheduler.RestObjects;

import org.junit.Test;


public class PostSubmitVnfChangeRestObjectTest {

	private PostSubmitVnfChangeRestObject createTestSubject() {
		return new PostSubmitVnfChangeRestObject();
	}


	@Test
	public void testSetUUID() throws Exception {
		PostSubmitVnfChangeRestObject testSubject;
		String uuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setUUID(uuid);
	}


	@Test
	public void testGetUUID() throws Exception {
		PostSubmitVnfChangeRestObject testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUUID();
	}
}