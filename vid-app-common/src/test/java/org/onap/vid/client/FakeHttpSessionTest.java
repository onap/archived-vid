package org.onap.vid.client;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionContext;

import org.junit.Test;


public class FakeHttpSessionTest {

	private FakeHttpSession createTestSubject() {
		return new FakeHttpSession();
	}


	@Test
	public void testGetCreationTime() throws Exception {
		FakeHttpSession testSubject;
		long result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getCreationTime();
	}


	@Test
	public void testGetId() throws Exception {
		FakeHttpSession testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getId();
	}


	@Test
	public void testGetLastAccessedTime() throws Exception {
		FakeHttpSession testSubject;
		long result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getLastAccessedTime();
	}


	@Test
	public void testGetServletContext() throws Exception {
		FakeHttpSession testSubject;
		ServletContext result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getServletContext();
	}


	@Test
	public void testSetMaxInactiveInterval() throws Exception {
		FakeHttpSession testSubject;
		int maxInactiveInterval = 0;

		// default test
		testSubject = createTestSubject();
		testSubject.setMaxInactiveInterval(maxInactiveInterval);
	}


	@Test
	public void testGetMaxInactiveInterval() throws Exception {
		FakeHttpSession testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMaxInactiveInterval();
	}


	@Test
	public void testGetSessionContext() throws Exception {
		FakeHttpSession testSubject;
		HttpSessionContext result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getSessionContext();
	}


	@Test
	public void testGetAttribute() throws Exception {
		FakeHttpSession testSubject;
		String name = "";
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getAttribute(name);
	}


	@Test
	public void testGetValue() throws Exception {
		FakeHttpSession testSubject;
		String name = "";
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getValue(name);
	}


	@Test
	public void testGetAttributeNames() throws Exception {
		FakeHttpSession testSubject;
		Enumeration<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getAttributeNames();
	}


	@Test
	public void testGetValueNames() throws Exception {
		FakeHttpSession testSubject;
		String[] result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getValueNames();
	}


	@Test
	public void testSetAttribute() throws Exception {
		FakeHttpSession testSubject;
		String name = "";
		Object value = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setAttribute(name, value);
	}


	@Test
	public void testPutValue() throws Exception {
		FakeHttpSession testSubject;
		String name = "";
		Object value = null;

		// default test
		testSubject = createTestSubject();
		testSubject.putValue(name, value);
	}


	@Test
	public void testRemoveAttribute() throws Exception {
		FakeHttpSession testSubject;
		String name = "";

		// default test
		testSubject = createTestSubject();
		testSubject.removeAttribute(name);
	}


	@Test
	public void testRemoveValue() throws Exception {
		FakeHttpSession testSubject;
		String name = "";

		// default test
		testSubject = createTestSubject();
		testSubject.removeValue(name);
	}


	@Test
	public void testInvalidate() throws Exception {
		FakeHttpSession testSubject;

		// default test
		testSubject = createTestSubject();
		testSubject.invalidate();
	}


	@Test
	public void testIsNew() throws Exception {
		FakeHttpSession testSubject;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.isNew();
	}
}