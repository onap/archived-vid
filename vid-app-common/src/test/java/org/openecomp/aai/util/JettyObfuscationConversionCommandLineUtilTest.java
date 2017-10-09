package org.openecomp.aai.util;

import org.junit.Test;

public class JettyObfuscationConversionCommandLineUtilTest {

	private JettyObfuscationConversionCommandLineUtil createTestSubject() {
		return new JettyObfuscationConversionCommandLineUtil();
	}

	
	@Test
	public void testMain() throws Exception {
		String[] args = new String[] { "" };

		// default test
		JettyObfuscationConversionCommandLineUtil.main(args);
	}

	
}