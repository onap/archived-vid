package org.openecomp.vid.aai;

import org.junit.Test;
import org.openecomp.vid.model.SubscriberList;

public class SubscriberAaiResponseTest {

	private SubscriberAaiResponse createTestSubject() {
		return new SubscriberAaiResponse(new SubscriberList(), "", 0);
	}

	
	@Test
	public void testGetSubscriberList() throws Exception {
		SubscriberAaiResponse testSubject;
		SubscriberList result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getSubscriberList();
	}
}