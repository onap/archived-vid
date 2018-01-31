package org.onap.vid.mso.rest;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.onap.vid.domain.mso.*;


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
		List<RelatedModel> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelatedInstanceList();
	}

	
	@Test
	public void testSetRelatedInstanceList() throws Exception {
		RequestDetails testSubject;
		List<RelatedModel> relatedInstanceList = null;

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