package org.onap.vid.mso.rest;

import java.util.Map;

import org.junit.Test;
import org.onap.vid.domain.mso.ModelInfo;


public class RelatedModelTest {

	private RelatedModel createTestSubject() {
		return new RelatedModel();
	}

	
	@Test
	public void testGetModelInfo() throws Exception {
		RelatedModel testSubject;
		ModelInfo result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getModelInfo();
	}

	
	@Test
	public void testSetModelInfo() throws Exception {
		RelatedModel testSubject;
		ModelInfo modelInfo = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setModelInfo(modelInfo);
	}

	
	@Test
	public void testToString() throws Exception {
		RelatedModel testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}

	
	@Test
	public void testGetAdditionalProperties() throws Exception {
		RelatedModel testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getAdditionalProperties();
	}

	
	@Test
	public void testSetAdditionalProperty() throws Exception {
		RelatedModel testSubject;
		String name = "";
		Object value = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setAdditionalProperty(name, value);
	}

	
	@Test
	public void testHashCode() throws Exception {
		RelatedModel testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.hashCode();
	}

	
	@Test
	public void testEquals() throws Exception {
		RelatedModel testSubject;
		Object other = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.equals(other);
	}
}