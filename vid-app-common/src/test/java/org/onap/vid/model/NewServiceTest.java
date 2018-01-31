package org.onap.vid.model;

import java.util.Map;

import org.junit.Test;
import org.onap.vid.asdc.beans.tosca.Input;

public class NewServiceTest {

	private NewService createTestSubject() {
		return new NewService();
	}

	
	@Test
	public void testGetUuid() throws Exception {
		NewService testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUuid();
	}

	
	@Test
	public void testGetInvariantUuid() throws Exception {
		NewService testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getInvariantUuid();
	}

	
	@Test
	public void testGetName() throws Exception {
		NewService testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getName();
	}

	
	@Test
	public void testGetVersion() throws Exception {
		NewService testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVersion();
	}

	
	@Test
	public void testGetToscaModelURL() throws Exception {
		NewService testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getToscaModelURL();
	}

	
	@Test
	public void testGetCategory() throws Exception {
		NewService testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getCategory();
	}

	
	@Test
	public void testGetDescription() throws Exception {
		NewService testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getDescription();
	}

	
	@Test
	public void testGetInputs() throws Exception {
		NewService testSubject;
		Map<String, Input> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getInputs();
	}

	
	@Test
	public void testGetServiceEcompNaming() throws Exception {
		NewService testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getServiceEcompNaming();
	}

	
	@Test
	public void testSetUuid() throws Exception {
		NewService testSubject;
		String uuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setUuid(uuid);
	}

	
	@Test
	public void testSetInvariantUuid() throws Exception {
		NewService testSubject;
		String invariantUuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setInvariantUuid(invariantUuid);
	}

	
	@Test
	public void testSetName() throws Exception {
		NewService testSubject;
		String name = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setName(name);
	}

	
	@Test
	public void testSetVersion() throws Exception {
		NewService testSubject;
		String version = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setVersion(version);
	}

	
	@Test
	public void testSetToscaModelURL() throws Exception {
		NewService testSubject;
		String toscaModelURL = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setToscaModelURL(toscaModelURL);
	}

	
	@Test
	public void testSetCategory() throws Exception {
		NewService testSubject;
		String category = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setCategory(category);
	}

	
	@Test
	public void testSetDescription() throws Exception {
		NewService testSubject;
		String description = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setDescription(description);
	}

	
	@Test
	public void testSetInputs() throws Exception {
		NewService testSubject;
		Map<String, Input> inputs = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setInputs(inputs);
	}

	
	@Test
	public void testSetServiceEcompNaming() throws Exception {
		NewService testSubject;
		String serviceEcompNaming = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setServiceEcompNaming(serviceEcompNaming);
	}

	
	@Test
	public void testHashCode() throws Exception {
		NewService testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		testSubject.setUuid("cb49608f-5a24-4789-b0f7-2595473cb997");
		result = testSubject.hashCode();
	}

	
	@Test
	public void testEquals() throws Exception {
		NewService testSubject;
		Object o = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.equals(o);
	}
}