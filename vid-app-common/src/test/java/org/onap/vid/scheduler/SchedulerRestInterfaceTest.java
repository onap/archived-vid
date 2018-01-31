package org.onap.vid.scheduler;

import org.apache.poi.hssf.record.formula.functions.T;
import org.json.simple.JSONObject;
import org.junit.Test;

public class SchedulerRestInterfaceTest {

	private SchedulerRestInterface createTestSubject() {
		return new SchedulerRestInterface();
	}


	
	@Test
	public void testLogRequest() throws Exception {
		SchedulerRestInterface testSubject;
		JSONObject requestDetails = null;

		// default test
		testSubject = createTestSubject();
		testSubject.logRequest(requestDetails);
	}


}