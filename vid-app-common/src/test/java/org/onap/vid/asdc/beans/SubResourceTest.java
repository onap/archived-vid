package org.onap.vid.asdc.beans;

import java.util.Collection;

import org.junit.Test;


public class SubResourceTest {

	private SubResource createTestSubject() {
		return new SubResource();
	}


	@Test
	public void testGetResourceInstanceName() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceInstanceName();
	}


	@Test
	public void testGetResourceName() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceName();
	}


	@Test
	public void testGetResourceInvariantUUID() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceInvariantUUID();
	}


	@Test
	public void testGetResourceVersion() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceVersion();
	}


	@Test
	public void testGetResoucreType() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResoucreType();
	}


	@Test
	public void testGetResourceUUID() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceUUID();
	}


	@Test
	public void testGetArtifacts() throws Exception {
		SubResource testSubject;
		Collection<Artifact> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifacts();
	}


	@Test
	public void testSetResourceInstanceName() throws Exception {
		SubResource testSubject;
		String resourceInstanceName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceInstanceName(resourceInstanceName);
	}


	@Test
	public void testSetResourceName() throws Exception {
		SubResource testSubject;
		String resourceName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceName(resourceName);
	}


	@Test
	public void testSetResourceInvariantUUID() throws Exception {
		SubResource testSubject;
		String resourceInvariantUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceInvariantUUID(resourceInvariantUUID);
	}


	@Test
	public void testSetResourceVersion() throws Exception {
		SubResource testSubject;
		String resourceVersion = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceVersion(resourceVersion);
	}


	@Test
	public void testSetResoucreType() throws Exception {
		SubResource testSubject;
		String resourceType = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResoucreType(resourceType);
	}


	@Test
	public void testSetResourceUUID() throws Exception {
		SubResource testSubject;
		String resourceUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceUUID(resourceUUID);
	}


	@Test
	public void testSetArtifacts() throws Exception {
		SubResource testSubject;
		Collection<Artifact> artifacts = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifacts(artifacts);
	}
}