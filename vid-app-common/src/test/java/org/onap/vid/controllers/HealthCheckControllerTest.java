package org.onap.vid.controllers;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.onap.vid.controllers.HealthCheckController.HealthStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HealthCheckControllerTest {

	private HealthCheckController testSubject;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		testSubject = new HealthCheckController();
		BasicConfigurator.configure();
		mockMvc = MockMvcBuilders.standaloneSetup(testSubject).build();
	}

	@Test
	public void testGetProfileCount() throws Exception {
		String driver = "";
		String URL = "";
		String username = "";
		String password = "";
		int result;

		// default test
		result = testSubject.getProfileCount(driver, URL, username, password);
	}

	@Test
	public void testGethealthCheckStatusforIDNS() throws Exception {
		HealthStatus result;

		// default test
		result = testSubject.gethealthCheckStatusforIDNS();
	}

	@Test
	public void testGetHealthCheck() throws Exception {
		String UserAgent = "";
		String ECOMPRequestID = "";
		HealthStatus result;

		// default test
		result = testSubject.getHealthCheck(UserAgent, ECOMPRequestID);
	}

	@Test
	public void testCommitInfoEndpoint() throws Exception {
		mockMvc.perform(get("/version")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.commitId").value("123987"))
				.andExpect(jsonPath("$.commitMessageShort").value("Test short commit message"))
				.andExpect(jsonPath("$.commitTime").value("1999-09-12T13:48:55+0200"));
	}
}