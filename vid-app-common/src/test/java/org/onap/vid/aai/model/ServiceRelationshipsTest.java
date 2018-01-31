package org.onap.vid.aai.model;

import org.junit.Test;


public class ServiceRelationshipsTest {

	private ServiceRelationships createTestSubject() {
		return new ServiceRelationships();
	}


	@Test
	public void testGetServiceInstanceId() throws Exception {
		ServiceRelationships testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getServiceInstanceId();
	}


	@Test
	public void testSetServiceInstanceId() throws Exception {
		ServiceRelationships testSubject;
		String serviceInstanceId = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setServiceInstanceId(serviceInstanceId);
	}


	@Test
	public void testGetServiceInstanceName() throws Exception {
		ServiceRelationships testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getServiceInstanceName();
	}


	@Test
	public void testSetServiceInstanceName() throws Exception {
		ServiceRelationships testSubject;
		String serviceInstanceName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setServiceInstanceName(serviceInstanceName);
	}


	@Test
	public void testGetModelInvariantId() throws Exception {
		ServiceRelationships testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getModelInvariantId();
	}


	@Test
	public void testSetModelInvariantId() throws Exception {
		ServiceRelationships testSubject;
		String modelInvariantId = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setModelInvariantId(modelInvariantId);
	}


	@Test
	public void testGetModelVersionId() throws Exception {
		ServiceRelationships testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getModelVersionId();
	}


	@Test
	public void testSetModelVersionId() throws Exception {
		ServiceRelationships testSubject;
		String modelVersionId = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setModelVersionId(modelVersionId);
	}


	@Test
	public void testGetResourceVersion() throws Exception {
		ServiceRelationships testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceVersion();
	}


	@Test
	public void testSetResourceVersion() throws Exception {
		ServiceRelationships testSubject;
		String resourceVersion = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceVersion(resourceVersion);
	}


	@Test
	public void testGetOrchestrationStatus() throws Exception {
		ServiceRelationships testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getOrchestrationStatus();
	}


	@Test
	public void testSetOrchestrationStatus() throws Exception {
		ServiceRelationships testSubject;
		String orchestrationStatus = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setOrchestrationStatus(orchestrationStatus);
	}


	@Test
	public void testGetRelationshipList() throws Exception {
		ServiceRelationships testSubject;
		RelationshipList result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelationshipList();
	}


	@Test
	public void testSetRelationshipList() throws Exception {
		ServiceRelationships testSubject;
		RelationshipList relationshipList = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRelationshipList(relationshipList);
	}
}