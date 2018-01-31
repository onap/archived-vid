package org.onap.vid.services;

import static org.junit.Assert.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.RequestDetails;
import org.onap.vid.mso.rest.Request;
import org.springframework.http.ResponseEntity;

public class ChangeManagementServiceImplTest {

	/*TODO: fix private ChangeManagementServiceImpl createTestSubject() {
		return new ChangeManagementServiceImpl();
	}*/

	
	/*@Test
	public void testGetMSOChangeManagements() throws Exception {
		ChangeManagementServiceImpl testSubject;
		Collection<Request> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMSOChangeManagements();
	}

	
	@Test
	public void testFindRequestByVnfName() throws Exception {
	ChangeManagementServiceImpl testSubject;List<RequestDetails> requests = null;
	String vnfName = "";
	RequestDetails result;
	
	// test 1
	testSubject=createTestSubject();requests = null;
	result=Deencapsulation.invoke(testSubject, "findRequestByVnfName", new Object[]{List<RequestDetails>.class, vnfName});
	Assert.assertEquals(null, result);
	}*/
/*
	
	@Test
	public void testDoChangeManagement() throws Exception {
		ChangeManagementServiceImpl testSubject;
		ChangeManagementRequest request = null;
		String vnfName = "";
		ResponseEntity<String> result;

		// test 1
		testSubject = createTestSubject();
		request = null;
		result = testSubject.doChangeManagement(request, vnfName);
		Assert.assertEquals(null, result);
	}

	
	@Test
	public void testGetSchedulerChangeManagements() throws Exception {
		ChangeManagementServiceImpl testSubject;
		JSONArray result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getSchedulerChangeManagements();
	}*/
}