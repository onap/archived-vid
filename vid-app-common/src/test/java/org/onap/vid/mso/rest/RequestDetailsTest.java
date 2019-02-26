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

package org.onap.vid.mso.rest;

import org.junit.Test;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.RequestInfo;

import java.util.List;
import java.util.Map;


public class RequestDetailsTest {

	private RequestDetails createTestSubject() {
		return new RequestDetails();
	}

	
	@Test
	public void testGetCloudConfiguration() throws Exception {
		RequestDetails testSubject;
		CloudConfiguration result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getCloudConfiguration();
	}

	
	@Test
	public void testSetCloudConfiguration() throws Exception {
		RequestDetails testSubject;
		CloudConfiguration cloudConfiguration = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setCloudConfiguration(cloudConfiguration);
	}

	
	@Test
	public void testGetModelInfo() throws Exception {
		RequestDetails testSubject;
		ModelInfo result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getModelInfo();
	}

	
	@Test
	public void testSetModelInfo() throws Exception {
		RequestDetails testSubject;
		ModelInfo modelInfo = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setModelInfo(modelInfo);
	}

	
	@Test
	public void testGetRelatedInstanceList() throws Exception {
		RequestDetails testSubject;
		List<RelatedInstanceWrapper> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelatedInstanceList();
	}

	
	@Test
	public void testSetRelatedInstanceList() throws Exception {
		RequestDetails testSubject;
		List<RelatedInstanceWrapper> relatedInstanceList = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRelatedInstanceList(relatedInstanceList);
	}

	
	@Test
	public void testGetRequestInfo() throws Exception {
		RequestDetails testSubject;
		RequestInfo result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRequestInfo();
	}

	
	@Test
	public void testSetRequestInfo() throws Exception {
		RequestDetails testSubject;
		RequestInfo requestInfo = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRequestInfo(requestInfo);
	}

	
	@Test
	public void testGetSubscriberInfo() throws Exception {
		RequestDetails testSubject;
		SubscriberInfo result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getSubscriberInfo();
	}

	
	@Test
	public void testSetSubscriberInfo() throws Exception {
		RequestDetails testSubject;
		SubscriberInfo subscriberInfo = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setSubscriberInfo(subscriberInfo);
	}

	
	@Test
	public void testToString() throws Exception {
		RequestDetails testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}

	
	@Test
	public void testGetAdditionalProperties() throws Exception {
		RequestDetails testSubject;
		Map<String, Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getAdditionalProperties();
	}

	
	@Test
	public void testSetAdditionalProperty() throws Exception {
		RequestDetails testSubject;
		String name = "";
		Object value = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setAdditionalProperty(name, value);
	}

	
	@Test
	public void testHashCode() throws Exception {
		RequestDetails testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.hashCode();
	}

	
	@Test
	public void testEquals() throws Exception {
		RequestDetails testSubject;
		Object other = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.equals(other);
	}
}
