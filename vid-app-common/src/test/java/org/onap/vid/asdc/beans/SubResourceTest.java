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

package org.onap.vid.asdc.beans;

import java.util.Collection;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;


public class SubResourceTest {

	private SubResource createTestSubject() {
		return new SubResource();
	}


	@Test
	public void testGetResourceInstanceName() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceInstanceName();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetResourceName() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceName();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetResourceInvariantUUID() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceInvariantUUID();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetResourceVersion() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceVersion();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetResoucreType() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResoucreType();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetResourceUUID() throws Exception {
		SubResource testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getResourceUUID();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetArtifacts() throws Exception {
		SubResource testSubject;
		Collection<Artifact> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifacts();
		assertNotNull(testSubject);
	}


	@Test
	public void testSetResourceInstanceName() throws Exception {
		SubResource testSubject;
		String resourceInstanceName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceInstanceName(resourceInstanceName);
		assertNotNull(testSubject);
	}


	@Test
	public void testSetResourceName() throws Exception {
		SubResource testSubject;
		String resourceName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceName(resourceName);
		assertNotNull(testSubject);
	}


	@Test
	public void testSetResourceInvariantUUID() throws Exception {
		SubResource testSubject;
		String resourceInvariantUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceInvariantUUID(resourceInvariantUUID);
		assertNotNull(testSubject);
	}


	@Test
	public void testSetResourceVersion() throws Exception {
		SubResource testSubject;
		String resourceVersion = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceVersion(resourceVersion);
		assertNotNull(testSubject);
	}


	@Test
	public void testSetResoucreType() throws Exception {
		SubResource testSubject;
		String resourceType = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResoucreType(resourceType);
		assertNotNull(testSubject);
	}


	@Test
	public void testSetResourceUUID() throws Exception {
		SubResource testSubject;
		String resourceUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setResourceUUID(resourceUUID);
		assertNotNull(testSubject);
	}


	@Test
	public void testSetArtifacts() throws Exception {
		SubResource testSubject;
		Collection<Artifact> artifacts = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifacts(artifacts);
		assertNotNull(testSubject);
	}
}
