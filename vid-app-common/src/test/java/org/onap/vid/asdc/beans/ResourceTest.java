package org.onap.vid.asdc.beans;

import java.util.Collection;

import org.junit.Test;
import org.onap.vid.asdc.beans.Resource.LifecycleState;
import org.onap.vid.asdc.beans.Resource.Type;


public class ResourceTest {

	private Resource createTestSubject() {
		return new Resource();
	}


	@Test
	public void testGetUuid() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUuid();
	}


	@Test
	public void testGetInvariantUUID() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getInvariantUUID();
	}


	@Test
	public void testGetName() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getName();
	}


	@Test
	public void testGetDescription() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getDescription();
	}


	@Test
	public void testGetVersion() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVersion();
	}


	@Test
	public void testGetToscaModelURL() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getToscaModelURL();
	}


	@Test
	public void testGetCategory() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getCategory();
	}


	@Test
	public void testGetSubCategory() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getSubCategory();
	}


	@Test
	public void testGetResourceType() throws Exception {
		Resource testSubject;
		Type result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceType();
	}


	@Test
	public void testGetLifecycleState() throws Exception {
		Resource testSubject;
		LifecycleState result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getLifecycleState();
	}


	@Test
	public void testGetLastUpdaterUserId() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getLastUpdaterUserId();
	}

	
	@Test
	public void testGetLastUpdaterFullName() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getLastUpdaterFullName();
	}


	@Test
	public void testGetToscaModel() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getToscaModel();
	}


	@Test
	public void testGetToscaResourceName() throws Exception {
		Resource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getToscaResourceName();
	}


	@Test
	public void testGetArtifacts() throws Exception {
		Resource testSubject;
		Collection<Artifact> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifacts();
	}


	@Test
	public void testGetResources() throws Exception {
		Resource testSubject;
		Collection<SubResource> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResources();
	}


	@Test
	public void testSetUuid() throws Exception {
		Resource testSubject;
		String uuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setUuid(uuid);
	}


	@Test
	public void testSetInvariantUUID() throws Exception {
		Resource testSubject;
		String invariantUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setInvariantUUID(invariantUUID);
	}


	@Test
	public void testSetName() throws Exception {
		Resource testSubject;
		String name = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setName(name);
	}


	@Test
	public void testSetDescription() throws Exception {
		Resource testSubject;
		String description = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setDescription(description);
	}


	@Test
	public void testSetVersion() throws Exception {
		Resource testSubject;
		String version = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setVersion(version);
	}


	@Test
	public void testSetToscaModelURL() throws Exception {
		Resource testSubject;
		String toscaModelURL = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setToscaModelURL(toscaModelURL);
	}


	@Test
	public void testSetCategory() throws Exception {
		Resource testSubject;
		String category = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setCategory(category);
	}


	@Test
	public void testSetSubCategory() throws Exception {
		Resource testSubject;
		String subCategory = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setSubCategory(subCategory);
	}


	@Test
	public void testSetResourceType() throws Exception {
		Resource testSubject;
		Type resourceType = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceType(resourceType);
	}


	@Test
	public void testSetLifecycleState() throws Exception {
		Resource testSubject;
		LifecycleState lifecycleState = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setLifecycleState(lifecycleState);
	}


	@Test
	public void testSetLastUpdaterUserId() throws Exception {
		Resource testSubject;
		String lastUpdaterUserId = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setLastUpdaterUserId(lastUpdaterUserId);
	}


	@Test
	public void testSetLastUpdaterFullName() throws Exception {
		Resource testSubject;
		String lastUpdaterFullName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setLastUpdaterFullName(lastUpdaterFullName);
	}


	@Test
	public void testSetToscaModel() throws Exception {
		Resource testSubject;
		String toscaModel = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setToscaModel(toscaModel);
	}


	@Test
	public void testSetToscaResourceName() throws Exception {
		Resource testSubject;
		String toscaResourceName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setToscaResourceName(toscaResourceName);
	}


	@Test
	public void testSetArtifacts() throws Exception {
		Resource testSubject;
		Collection<Artifact> artifacts = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifacts(artifacts);
	}


	@Test
	public void testSetResources() throws Exception {
		Resource testSubject;
		Collection<SubResource> resources = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setResources(resources);
	}


	@Test
	public void testHashCode() throws Exception {
		Resource testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		testSubject.setUuid("cb49608f-5a24-4789-b0f7-2595473cb997");
		result = testSubject.hashCode();
	}


	@Test
	public void testEquals() throws Exception {
		Resource testSubject;
		Object o = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.equals(o);
	}
}