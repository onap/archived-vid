package org.onap.vid.controller;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class PropertyControllerTest {

	private PropertyController createTestSubject() {
		return new PropertyController();
	}

	@Test
	public void testWelcome() throws Exception {
		PropertyController testSubject;
		HttpServletRequest request = null;
		ModelAndView result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.welcome(request);
	}


	@Test
	public void testGetProperty() throws Exception {
		PropertyController testSubject;
		String name = "";
		String defaultvalue = "";
		HttpServletRequest request = null;
		ResponseEntity<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getProperty(name, defaultvalue, request);
	}
}