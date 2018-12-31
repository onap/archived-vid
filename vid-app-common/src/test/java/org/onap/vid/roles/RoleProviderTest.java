package org.onap.vid.roles;

import org.junit.Test;


public class RoleProviderTest {

	private RoleProvider createTestSubject() {
		return new RoleProvider();
	}

	
//	@Test
//	public void testGetUserRoles() throws Exception {
//		RoleProvider testSubject;
//		HttpServletRequest request = null;
//		List<Role> result;
//
//		// default test
//		testSubject = createTestSubject();
//		result = testSubject.getUserRoles(request);
//	}

	
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