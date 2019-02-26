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

import org.junit.Test;


public class ToscaModelTest {

	private ToscaModel createTestSubject() {
		return new ToscaModel();
	}

	
	@Test
	public void testGetMetadata() throws Exception {
		ToscaModel testSubject;
		ToscaMetadata result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMetadata();
	}

	
	@Test
	public void testSetMetadata() throws Exception {
		ToscaModel testSubject;
		ToscaMetadata metadata = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMetadata(metadata);
	}

	
	@Test
	public void testGettosca_definitions_version() throws Exception {
		ToscaModel testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.gettosca_definitions_version();
	}

	
	@Test
	public void testSettosca_definitions_version() throws Exception {
		ToscaModel testSubject;
		String tosca_definitions_version = "";

		// default test
		testSubject = createTestSubject();
		testSubject.settosca_definitions_version(tosca_definitions_version);
	}

	
	@Test
	public void testGettopology_template() throws Exception {
		ToscaModel testSubject;
		TopologyTemplate result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.gettopology_template();
	}

	
	@Test
	public void testSettopology_template() throws Exception {
		ToscaModel testSubject;
		TopologyTemplate topology_template = null;

		// default test
		testSubject = createTestSubject();
		testSubject.settopology_template(topology_template);
	}

	
	@Test
	public void testGetDescription() throws Exception {
		ToscaModel testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getDescription();
	}

	
	@Test
	public void testSetDescription() throws Exception {
		ToscaModel testSubject;
		String description = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setDescription(description);
	}

	
	@Test
	public void testGetImports() throws Exception {
		ToscaModel testSubject;
		Collection<Map<String, Map<String, String>>> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getImports();
	}

	
	@Test
	public void testSetImports() throws Exception {
		ToscaModel testSubject;
		Collection<Map<String, Map<String, String>>> imports = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setImports(imports);
	}

	
	@Test
	public void testGetnode_types() throws Exception {
		ToscaModel testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getnode_types();
	}

	
	@Test
	public void testSetnode_types() throws Exception {
		ToscaModel testSubject;
		Map<String, Object> node_types = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setnode_types(node_types);
	}
}
