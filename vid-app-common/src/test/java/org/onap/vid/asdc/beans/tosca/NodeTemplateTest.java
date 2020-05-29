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

package org.onap.vid.asdc.beans.tosca;

import java.util.Map;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class NodeTemplateTest {

	private NodeTemplate createTestSubject() {
		return new NodeTemplate();
	}

	@Test
	public void testGetType() throws Exception {
		NodeTemplate testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getType();
		assertNotNull(testSubject);
	}

	@Test
	public void testSetType() throws Exception {
		NodeTemplate testSubject;
		String type = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setType(type);
		assertNotNull(testSubject);
	}

	@Test
	public void testGetMetadata() throws Exception {
		NodeTemplate testSubject;
		ToscaMetadata result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMetadata();
		assertNotNull(testSubject);
	}

	@Test
	public void testSetMetadata() throws Exception {
		NodeTemplate testSubject;
		ToscaMetadata metadata = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMetadata(metadata);
		assertNotNull(testSubject);
	}

	@Test
	public void testGetProperties() throws Exception {
		NodeTemplate testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getProperties();
		assertNotNull(testSubject);
	}

	@Test
	public void testSetProperties() throws Exception {
		NodeTemplate testSubject;
		Map<String, Object> properties = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setProperties(properties);
		assertNotNull(testSubject);
	}

	@Test
	public void testGetRequirements() throws Exception {
		NodeTemplate testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRequirements();
		assertNotNull(testSubject);
	}

	@Test
	public void testSetRequirements() throws Exception {
		NodeTemplate testSubject;
		Object requirements = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRequirements(requirements);
		assertNotNull(testSubject);
	}
}
