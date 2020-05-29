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

import java.util.Collection;
import java.util.Map;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class GroupTest {

	private Group createTestSubject() {
		return new Group();
	}

	
	@Test
	public void testGetMetadata() throws Exception {
		Group testSubject;
		ToscaMetadata result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMetadata();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetMetadata() throws Exception {
		Group testSubject;
		ToscaMetadata metadata = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMetadata(metadata);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetMembers() throws Exception {
		Group testSubject;
		Collection<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMembers();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetMembers() throws Exception {
		Group testSubject;
		Collection<String> members = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMembers(members);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetType() throws Exception {
		Group testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getType();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetType() throws Exception {
		Group testSubject;
		String type = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setType(type);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetvf_module_type() throws Exception {
		Group testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getvf_module_type();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetvf_module_type() throws Exception {
		Group testSubject;
		String vf_module_type = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setvf_module_type(vf_module_type);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetProperties() throws Exception {
		Group testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getProperties();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetProperties() throws Exception {
		Group testSubject;
		Map<String, Object> properties = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setProperties(properties);
		assertNotNull(testSubject);
	}
}
