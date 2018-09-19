package org.onap.vid.controllers;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import org.onap.vid.controllers.HealthCheckController.HealthStatus;
import org.onap.vid.model.GitRepositoryState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HealthCheckControllerTest {

	private HealthCheckController createTestSubject() {
		return new HealthCheckController();
	}
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;

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

	@Test
	public void testGetCommitInfo() throws Exception {
		// GIVEN
		HealthCheckController testSubject = createTestSubject();

		// WHEN
		GitRepositoryState result = testSubject.getCommitInfo("testgit.properties");

		// THEN
		assertEquals("123987", result.getCommitId());
		assertEquals("Test short commit message", result.getCommitMessageShort());
		assertEquals("1999-09-12T13:48:55+0200", result.getCommitTime());
	}

	@Test
	public void testCommitInfoEndpoint() throws Exception {
		// GIVEN
		HealthCheckController testSubject = createTestSubject();
		BasicConfigurator.configure();
		mockMvc = MockMvcBuilders.standaloneSetup(testSubject).build();

		// WHEN THEN
		mockMvc.perform(get("/version")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}