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

import java.util.Collection;

import org.junit.Test;

public class WorkflowTest {

	private Workflow createTestSubject() {
		return new Workflow();
	}

	@Test
	public void testGetId() throws Exception {
		Workflow testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getId();
	}

	@Test
	public void testGetWorkflowName() throws Exception {
		Workflow testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getWorkflowName();
	}

	@Test
	public void testGetVnfNames() throws Exception {
		Workflow testSubject;
		Collection<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVnfNames();
	}

	@Test
	public void testSetId() throws Exception {
		Workflow testSubject;
		int id = 0;

		// default test
		testSubject = createTestSubject();
		testSubject.setId(id);
	}

	@Test
	public void testSetWorkflowName() throws Exception {
		Workflow testSubject;
		String workflowName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setWorkflowName(workflowName);
	}

	@Test
	public void testSetVnfName() throws Exception {
		Workflow testSubject;
		Collection<String> vnfNames = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setVnfName(vnfNames);
	}
}
