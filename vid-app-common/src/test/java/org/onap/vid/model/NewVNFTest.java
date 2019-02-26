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

public class NewVNFTest {

	private NewVNF createTestSubject() {
		return new NewVNF();
	}

	
	@Test
	public void testGetModelCustomizationName() throws Exception {
		NewVNF testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getModelCustomizationName();
	}

	
	@Test
	public void testGetVfModules() throws Exception {
		NewVNF testSubject;
		Map<String, VfModule> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVfModules();
	}

	
	@Test
	public void testSetVfModules() throws Exception {
		NewVNF testSubject;
		Map<String, VfModule> vfModules = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setVfModules(vfModules);
	}

	
	@Test
	public void testGetVolumeGroups() throws Exception {
		NewVNF testSubject;
		Map<String, VolumeGroup> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVolumeGroups();
	}

	
	@Test
	public void testSetVolumeGroups() throws Exception {
		NewVNF testSubject;
		Map<String, VolumeGroup> volumeGroups = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setVolumeGroups(volumeGroups);
	}

	
	@Test
	public void testSetModelCustomizationName() throws Exception {
		NewVNF testSubject;
		String modelCustomizationName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setModelCustomizationName(modelCustomizationName);
	}

	
	@Test
	public void testNormalizeName() throws Exception {
		String originalName = "";
		String result;

		// default test
		result = NewVNF.normalizeName(originalName);
	}
}
