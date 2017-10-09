package org.openecomp.vid.aai;

import java.util.Map;

import org.junit.Test;

public class VnfResultTest {

	private VnfResult createTestSubject() {
		return new VnfResult();
	}

	@Test
	public void testGetAdditionalProperties() throws Exception {
		VnfResult testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getAdditionalProperties();
	}

	@Test
	public void testSetAdditionalProperty() throws Exception {
		VnfResult testSubject;
		String name = "";
		Object value = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setAdditionalProperty(name, value);
	}
}