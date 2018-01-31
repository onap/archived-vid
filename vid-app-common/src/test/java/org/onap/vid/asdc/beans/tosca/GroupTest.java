package org.onap.vid.asdc.beans.tosca;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;


public class GroupTest {

	private Group createTestSubject() {
		return new Group();
	}

	
	@Test
	public void testGetMetadata() throws Exception {
		Group testSubject;
		ToscaMetadata result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMetadata();
	}

	
	@Test
	public void testSetMetadata() throws Exception {
		Group testSubject;
		ToscaMetadata metadata = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMetadata(metadata);
	}

	
	@Test
	public void testGetMembers() throws Exception {
		Group testSubject;
		Collection<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMembers();
	}

	
	@Test
	public void testSetMembers() throws Exception {
		Group testSubject;
		Collection<String> members = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMembers(members);
	}

	
	@Test
	public void testGetType() throws Exception {
		Group testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getType();
	}

	
	@Test
	public void testSetType() throws Exception {
		Group testSubject;
		String type = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setType(type);
	}

	
	@Test
	public void testGetvf_module_type() throws Exception {
		Group testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getvf_module_type();
	}

	
	@Test
	public void testSetvf_module_type() throws Exception {
		Group testSubject;
		String vf_module_type = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setvf_module_type(vf_module_type);
	}

	
	@Test
	public void testGetProperties() throws Exception {
		Group testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getProperties();
	}

	
	@Test
	public void testSetProperties() throws Exception {
		Group testSubject;
		Map<String, Object> properties = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setProperties(properties);
	}
}