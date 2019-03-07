/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.aai.util;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.onap.vid.aai.util.JettyObfuscationConversionCommandLineUtil;

@RunWith(Parameterized.class)
public class JettyObfuscationConversionCommandLineUtilTest {

	final String[] args;
	final String expected;

	public JettyObfuscationConversionCommandLineUtilTest(String description, String[] args, String expected) {
		this.args = args;
		this.expected = expected;
	}

	@Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
			{"print usage on missing params", new String[]{}, "usage:"},
			{"obfuscate", new String[]{"-e", "foobar"}, "OBF:1vub1ua51uh81ugi1u9d1vuz"},
			{"deobfuscate", new String[]{"-d", "OBF:1vub1ua51uh81ugi1u9d1vuz"}, "foobar"},
			{"input parse exception", new String[]{"mm", "-mm", "-mm"}, "failed to parse input"},
			{"deobfuscate exception", new String[]{"-d", "problematic string"}, "exception:java.lang.NumberFormatException"},
		});
	}

	@Test
	public void testMain() {
		final PrintStream originalOut = System.out;
		try {
			PrintStream mockedOut = mock(PrintStream.class);
			System.setOut(mockedOut);
			JettyObfuscationConversionCommandLineUtil.main(args);
			verify(mockedOut).println(contains(expected));
		} finally {
			System.setOut(originalOut);
		}
	}

}
