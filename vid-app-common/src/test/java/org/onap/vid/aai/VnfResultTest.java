package org.onap.vid.aai;

import org.junit.Test;
import org.onap.vid.aai.model.VnfResult;

import java.util.Map;

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
	}
}