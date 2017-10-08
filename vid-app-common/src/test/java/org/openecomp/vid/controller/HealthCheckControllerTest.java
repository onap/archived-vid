package org.openecomp.vid.controller;

import org.junit.Test;
import org.openecomp.vid.controller.HealthCheckController.HealthStatus;

public class HealthCheckControllerTest {

	private HealthCheckController createTestSubject() {
		return new HealthCheckController();
	}

	@Test
	public void testGetProfileCount() throws Exception {
		HealthCheckController testSubject;
		String driver = "";
		String URL = "";
		String username = "";
		String password = "";
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getProfileCount(driver, URL, username, password);
	}

	@Test
	public void testGethealthCheckStatusforIDNS() throws Exception {
		HealthCheckController testSubject;
		HealthStatus result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.gethealthCheckStatusforIDNS();
	}

	@Test
	public void testGetHealthCheck() throws Exception {
		HealthCheckController testSubject;
		String UserAgent = "";
		String ECOMPRequestID = "";
		HealthStatus result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getHealthCheck(UserAgent, ECOMPRequestID);
	}
}