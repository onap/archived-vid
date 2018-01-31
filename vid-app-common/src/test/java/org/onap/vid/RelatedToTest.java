package org.onap.vid;

import java.util.Map;

import org.junit.Test;
import org.onap.vid.RelatedTo;


public class RelatedToTest {

	private RelatedTo createTestSubject() {
		return new RelatedTo();
	}


	@Test
	public void testGetAdditionalProperties() throws Exception {
		RelatedTo testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getAdditionalProperties();
	}


	@Test
	public void testSetAdditionalProperty() throws Exception {
		RelatedTo testSubject;
		String name = "";
		Object value = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setAdditionalProperty(name, value);
	}
}