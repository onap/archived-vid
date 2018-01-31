package org.onap.vid.asdc.beans.tosca;

import java.util.Map;

import org.junit.Test;


public class TopologyTemplateTest {

	private TopologyTemplate createTestSubject() {
		return new TopologyTemplate();
	}

	
	@Test
	public void testGetsubstitution_mappings() throws Exception {
		TopologyTemplate testSubject;
		SubstitutionMappings result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getsubstitution_mappings();
	}

	
	@Test
	public void testSetsubstitution_mappings() throws Exception {
		TopologyTemplate testSubject;
		SubstitutionMappings substitution_mappings = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setsubstitution_mappings(substitution_mappings);
	}

	
	@Test
	public void testGetInputs() throws Exception {
		TopologyTemplate testSubject;
		Map<String, Input> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getInputs();
	}

	
	@Test
	public void testSetInputs() throws Exception {
		TopologyTemplate testSubject;
		Map<String, Input> inputs = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setInputs(inputs);
	}

	
	@Test
	public void testGetnode_templates() throws Exception {
		TopologyTemplate testSubject;
		Map<String, NodeTemplate> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getnode_templates();
	}

	
	@Test
	public void testSetnode_templates() throws Exception {
		TopologyTemplate testSubject;
		Map<String, NodeTemplate> node_templates = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setnode_templates(node_templates);
	}

	
	@Test
	public void testGetGroups() throws Exception {
		TopologyTemplate testSubject;
		Map<String, Group> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getGroups();
	}

	
	@Test
	public void testSetGroups() throws Exception {
		TopologyTemplate testSubject;
		Map<String, Group> groups = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setGroups(groups);
	}
}