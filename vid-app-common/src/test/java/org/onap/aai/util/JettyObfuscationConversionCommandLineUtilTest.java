package org.onap.aai.util;

import org.junit.Test;
import org.onap.vid.aai.util.JettyObfuscationConversionCommandLineUtil;

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