package org.openecomp.vid.controller;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

public class ViewEditSubControllerTest {

	private ViewEditSubController createTestSubject() {
		return new ViewEditSubController();
	}

	@Test
	public void testWelcome() throws Exception {
		ViewEditSubController testSubject;
		HttpServletRequest request = null;
		ModelAndView result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.welcome(request);
	}


	@Test
	public void testGetViewName() throws Exception {
		ViewEditSubController testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getViewName();
	}

	@Test
	public void testSetViewName() throws Exception {
		ViewEditSubController testSubject;
		String viewName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setViewName(viewName);
	}
}