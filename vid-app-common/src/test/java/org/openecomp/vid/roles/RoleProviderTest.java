package org.openecomp.vid.roles;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;


public class RoleProviderTest {

	private RoleProvider createTestSubject() {
		return new RoleProvider();
	}

	
	@Test
	public void testExtractRoleFromSession() throws Exception {
		HttpServletRequest request = null;
		List<String> result;

		// default test
		result = RoleProvider.extractRoleFromSession(request);
	}

	
	@Test
	public void testGetUserRoles() throws Exception {
		RoleProvider testSubject;
		HttpServletRequest request = null;
		List<Role> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUserRoles(request);
	}

	
	@Test
	public void testSplitRole() throws Exception {
		RoleProvider testSubject;
		String roleAsString = "";
		String[] result;

		// default test
		testSubject = createTestSubject();
		//TODO:fix result = testSubject.splitRole(roleAsString);
	}

}