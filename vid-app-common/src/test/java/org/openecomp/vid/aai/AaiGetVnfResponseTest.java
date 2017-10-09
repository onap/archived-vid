package org.openecomp.vid.aai;

import java.util.Map;

import org.junit.Test;

public class AaiGetVnfResponseTest {

	private AaiGetVnfResponse createTestSubject() {
		return new AaiGetVnfResponse();
	}

	@Test
	public void testGetAdditionalProperties() throws Exception {
		AaiGetVnfResponse testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getAdditionalProperties();
	}

	@Test
	public void testSetAdditionalProperty() throws Exception {
		AaiGetVnfResponse testSubject;
		String name = "";
		Object value = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setAdditionalProperty(name, value);
	}
}