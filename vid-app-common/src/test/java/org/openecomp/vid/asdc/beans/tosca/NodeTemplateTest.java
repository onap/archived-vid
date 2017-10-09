package org.openecomp.vid.asdc.beans.tosca;

import java.util.Map;

import org.junit.Test;

public class NodeTemplateTest {

	private NodeTemplate createTestSubject() {
		return new NodeTemplate();
	}

	@Test
	public void testGetType() throws Exception {
		NodeTemplate testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getType();
	}

	@Test
	public void testSetType() throws Exception {
		NodeTemplate testSubject;
		String type = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setType(type);
	}

	@Test
	public void testGetMetadata() throws Exception {
		NodeTemplate testSubject;
		ToscaMetadata result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMetadata();
	}

	@Test
	public void testSetMetadata() throws Exception {
		NodeTemplate testSubject;
		ToscaMetadata metadata = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMetadata(metadata);
	}

	@Test
	public void testGetProperties() throws Exception {
		NodeTemplate testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getProperties();
	}

	@Test
	public void testSetProperties() throws Exception {
		NodeTemplate testSubject;
		Map<String, Object> properties = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setProperties(properties);
	}

	@Test
	public void testGetRequirements() throws Exception {
		NodeTemplate testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRequirements();
	}

	@Test
	public void testSetRequirements() throws Exception {
		NodeTemplate testSubject;
		Object requirements = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRequirements(requirements);
	}
}