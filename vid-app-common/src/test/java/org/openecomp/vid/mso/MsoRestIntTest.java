package org.openecomp.vid.mso;

import org.junit.Test;
import org.openecomp.vid.mso.rest.RequestDetails;

public class MsoRestIntTest {

	private MsoRestInt createTestSubject() {
		return new MsoRestInt();
	}

	@Test
	public void testLogRequest() throws Exception {
		MsoRestInt testSubject;
		RequestDetails r = null;

		// test 1
		testSubject = createTestSubject();
		r = null;
		testSubject.logRequest(r);
	}
}