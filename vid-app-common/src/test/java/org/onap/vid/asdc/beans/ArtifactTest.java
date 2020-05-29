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

import org.junit.Test;
import static org.junit.Assert.assertNotNull;


public class ArtifactTest {

	private Artifact createTestSubject() {
		return new Artifact();
	}


	@Test
	public void testGetArtifactName() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactName();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetArtifactType() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactType();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetArtifactGroupType() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactGroupType();
		assertNotNull(testSubject);
	}


	@Test
	public void testGetArtifactLabel() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactLabel();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetArtifactURL() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactURL();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetArtifactDescription() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactDescription();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetArtifactTimeout() throws Exception {
		Artifact testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactTimeout();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetArtifactChecksum() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactChecksum();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetArtifactUUID() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactUUID();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetArtifactVersion() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactVersion();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testGetGeneratedFromUUID() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getGeneratedFromUUID();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactName() throws Exception {
		Artifact testSubject;
		String artifactName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactName(artifactName);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactType() throws Exception {
		Artifact testSubject;
		String artifactType = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactType(artifactType);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactGroupType() throws Exception {
		Artifact testSubject;
		String artifactGroupType = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactGroupType(artifactGroupType);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactLabel() throws Exception {
		Artifact testSubject;
		String artifactLabel = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactLabel(artifactLabel);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactURL() throws Exception {
		Artifact testSubject;
		String artifactURL = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactURL(artifactURL);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactDescription() throws Exception {
		Artifact testSubject;
		String artifactDescription = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactDescription(artifactDescription);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactTimeout() throws Exception {
		Artifact testSubject;
		int artifactTimeout = 0;

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactTimeout(artifactTimeout);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactChecksum() throws Exception {
		Artifact testSubject;
		String artifactChecksum = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactChecksum(artifactChecksum);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactUUID() throws Exception {
		Artifact testSubject;
		String artifactUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactUUID(artifactUUID);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetArtifactVersion() throws Exception {
		Artifact testSubject;
		String artifactVersion = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactVersion(artifactVersion);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testSetGeneratedFromUUID() throws Exception {
		Artifact testSubject;
		String generatedFromUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setGeneratedFromUUID(generatedFromUUID);
		assertNotNull(testSubject);
	}

	
	@Test
	public void testHashCode() throws Exception {
		Artifact testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactUUID("48a52540-8772-4368-9cdb-1f124ea5c931");
		result = testSubject.hashCode();
		assertNotNull(testSubject);
	}

	
	@Test
	public void testEquals() throws Exception {
		Artifact testSubject;
		Object o = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.equals(o);
		assertNotNull(testSubject);
	}
}
