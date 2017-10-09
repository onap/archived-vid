package org.openecomp.vid.aai;

import org.junit.Test;

public class SubscriberWithFilterTest {

	private SubscriberWithFilter createTestSubject() {
		return new SubscriberWithFilter();
	}

	@Test
	public void testGetIsPermitted() throws Exception {
		SubscriberWithFilter testSubject;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getIsPermitted();
	}

	@Test
	public void testSetIsPermitted() throws Exception {
		SubscriberWithFilter testSubject;
		boolean isPermitted = false;

		// default test
		testSubject = createTestSubject();
		testSubject.setIsPermitted(isPermitted);
	}
}