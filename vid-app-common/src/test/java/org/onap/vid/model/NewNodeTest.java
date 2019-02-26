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

package org.onap.vid.model;

import java.util.Map;

import org.junit.Test;
import org.onap.vid.asdc.beans.tosca.Input;


public class NewNodeTest {

	private NewNode createTestSubject() {
		return new NewNode();
	}

	
	@Test
	public void testGetUuid() throws Exception {
		NewNode testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUuid();
	}

	
	@Test
	public void testGetInvariantUuid() throws Exception {
		NewNode testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getInvariantUuid();
	}

	
	@Test
	public void testGetDescription() throws Exception {
		NewNode testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getDescription();
	}

	
	@Test
	public void testGetName() throws Exception {
		NewNode testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getName();
	}

	
	@Test
	public void testGetVersion() throws Exception {
		NewNode testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVersion();
	}

	
	@Test
	public void testGetCustomizationUuid() throws Exception {
		NewNode testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getCustomizationUuid();
	}

	
	@Test
	public void testGetInputs() throws Exception {
		NewNode testSubject;
		Map<String, Input> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getInputs();
	}

	
	@Test
	public void testGetCommands() throws Exception {
		NewNode testSubject;
		Map<String, CommandProperty> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getCommands();
	}


	@Test
	public void testGetProperties() throws Exception {
		NewNode testSubject;
		Map<String, String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getProperties();
	}

	
	@Test
	public void testSetUuid() throws Exception {
		NewNode testSubject;
		String uuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setUuid(uuid);
	}

	
	@Test
	public void testSetInvariantUuid() throws Exception {
		NewNode testSubject;
		String invariantUuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setInvariantUuid(invariantUuid);
	}

	
	@Test
	public void testSetDescription() throws Exception {
		NewNode testSubject;
		String description = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setDescription(description);
	}

	
	@Test
	public void testSetName() throws Exception {
		NewNode testSubject;
		String name = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setName(name);
	}

	
	@Test
	public void testSetVersion() throws Exception {
		NewNode testSubject;
		String version = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setVersion(version);
	}

	
	@Test
	public void testSetCustomizationUuid() throws Exception {
		NewNode testSubject;
		String u = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setCustomizationUuid(u);
	}

	
	@Test
	public void testSetInputs() throws Exception {
		NewNode testSubject;
		Map<String, Input> inputs = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setInputs(inputs);
	}

	
	@Test
	public void testSetCommands() throws Exception {
		NewNode testSubject;
		Map<String, CommandProperty> m = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setCommands(m);
	}

	
	@Test
	public void testSetProperties() throws Exception {
		NewNode testSubject;
		Map<String, String> p = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setProperties(p);
	}
}
