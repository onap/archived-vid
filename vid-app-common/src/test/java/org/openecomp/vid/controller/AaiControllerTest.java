package org.openecomp.vid.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public class AaiControllerTest {

	private AaiController createTestSubject() {
		return new AaiController();
	}

	@Test
	public void testParseCustomerObjectForTenants() throws Exception {
		JSONObject jsonObject = null;
		String result;

		// default test
		result = AaiController.parseCustomerObjectForTenants(jsonObject);
	}

	@Test
	public void testParseServiceSubscriptionObjectForTenants() throws Exception {
		JSONObject jsonObject = null;
		String result;

		// default test
		result = AaiController.parseServiceSubscriptionObjectForTenants(jsonObject);
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
	public void testGetViewName() throws Exception {
		AaiController testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getViewName();
	}

	@Test
	public void testSetViewName() throws Exception {
		AaiController testSubject;
		String viewName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setViewName(viewName);
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