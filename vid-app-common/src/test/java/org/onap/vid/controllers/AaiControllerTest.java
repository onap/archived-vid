package org.onap.vid.controllers;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.onap.vid.controllers.AaiController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public class AaiControllerTest {

	private AaiController createTestSubject() {
		return new AaiController();
	}

	@Test
	public void testWelcome() throws Exception {
		AaiController testSubject;
		HttpServletRequest request = null;
		ModelAndView result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.welcome(request);
	}


	@Test
	public void testGetTargetProvStatus() throws Exception {
		AaiController testSubject;
		ResponseEntity<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getTargetProvStatus();
	}

	@Test
	public void testViewEditGetTenantsFromServiceType() throws Exception {
		AaiController testSubject;
		HttpServletRequest request = null;
		String globalCustomerId = "";
		String serviceType = "";
		ResponseEntity<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.viewEditGetTenantsFromServiceType(request, globalCustomerId, serviceType);
	}

}