package org.onap.vid.aai;

import java.util.Map;

import org.junit.Test;
import org.onap.vid.aai.model.ServiceProperties;

public class ServicePropertiesTest {

	private ServiceProperties createTestSubject() {
		return new ServiceProperties();
	}

	@Test
	public void testGetAdditionalProperties() throws Exception {
		ServiceProperties testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getAdditionalProperties();
	}

	@Test
	public void testSetAdditionalProperty() throws Exception {
		ServiceProperties testSubject;
		String name = "";
		Object value = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setAdditionalProperty(name, value);
	}
}