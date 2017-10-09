package org.openecomp.vid.asdc.beans.tosca;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;


public class CapabilityTest {

	private Capability createTestSubject() {
		return new Capability();
	}

	
	@Test
	public void testGetType() throws Exception {
		Capability testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getType();
	}

	
	@Test
	public void testGetDescription() throws Exception {
		Capability testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getDescription();
	}

	
	@Test
	public void testGetOccurrences() throws Exception {
		Capability testSubject;
		Collection<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getOccurrences();
	}

	
	@Test
	public void testGetProperties() throws Exception {
		Capability testSubject;
		Map<String, Property> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getProperties();
	}

	
	@Test
	public void testGetValid_source_types() throws Exception {
		Capability testSubject;
		Collection<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getValid_source_types();
	}

	
	@Test
	public void testSetType() throws Exception {
		Capability testSubject;
		String type = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setType(type);
	}

	
	@Test
	public void testSetDescription() throws Exception {
		Capability testSubject;
		String description = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setDescription(description);
	}

	
	@Test
	public void testSetOccurrences() throws Exception {
		Capability testSubject;
		Collection<String> occurrences = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setOccurrences(occurrences);
	}

	
	@Test
	public void testSetProperties() throws Exception {
		Capability testSubject;
		Map<String, Property> properties = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setProperties(properties);
	}

	
	@Test
	public void testSetValid_source_types() throws Exception {
		Capability testSubject;
		Collection<String> valid_source_types = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setValid_source_types(valid_source_types);
	}
}