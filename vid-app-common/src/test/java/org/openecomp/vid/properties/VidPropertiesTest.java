package org.openecomp.vid.properties;

import org.junit.Test;


public class VidPropertiesTest {

	private VidProperties createTestSubject() {
		return new VidProperties();
	}


	@Test
	public void testGetAsdcModelNamespace() throws Exception {
		String result;

		// default test
		result = VidProperties.getAsdcModelNamespace();
	}


	@Test
	public void testGetPropertyWithDefault() throws Exception {
		String propName = "";
		String defaultValue = "";
		String result;

		// default test
		result = VidProperties.getPropertyWithDefault(propName, defaultValue);
	}
}