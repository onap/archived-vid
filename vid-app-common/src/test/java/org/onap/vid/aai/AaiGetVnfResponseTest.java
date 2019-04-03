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

package org.onap.vid.aai;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.onap.vid.aai.model.VnfResult;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AaiGetVnfResponseTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final String VNF_RESULT =
			"{\n" +
					"	\"id\": \"1\",\n" +
					"   \"nodetype\": \"testNodeType\",\n" +
					"   \"url\": \"test/url\",\n" +
					"   \"serviceProperties\": {},\n" +
					"   \"relatedTo\": []\n" +
					"}\n";

	private static final String AAI_GET_VNF_RESPONSE_TEST = "{ \n" +
			"  \"results\": \n" +
			"  [\n" +
					VNF_RESULT +
			"  ]\n" +
			"}";


	private AaiGetVnfResponse aaiGetVnfResponse;
	private VnfResult expectedVnfResult;
	private List<VnfResult> expectedList;

	@Before
	public void setUp() throws IOException {
		expectedVnfResult = OBJECT_MAPPER.readValue(VNF_RESULT, VnfResult.class);
		expectedList = Collections.singletonList(expectedVnfResult);

	}

	@Test
	public void shouldHaveProperSetters() {
		aaiGetVnfResponse = new AaiGetVnfResponse();
		aaiGetVnfResponse.setAdditionalProperty("key","value");
		aaiGetVnfResponse.setResults(expectedList);

		assertEquals(aaiGetVnfResponse.getAdditionalProperties().size(), 1);
		assertTrue(aaiGetVnfResponse.getAdditionalProperties().containsKey("key"));
		assertTrue(aaiGetVnfResponse.getAdditionalProperties().containsValue("value"));
		assertEquals(aaiGetVnfResponse.getResults().size(), 1);
		assertEquals(aaiGetVnfResponse.getResults().get(0), expectedVnfResult);
	}

	@Test
	public void shouldProperlyConvertJsonToAiiGetVnfResponse() throws IOException {
		aaiGetVnfResponse = OBJECT_MAPPER.readValue(AAI_GET_VNF_RESPONSE_TEST, AaiGetVnfResponse.class);
		assertThat(aaiGetVnfResponse.getResults(), is(expectedList));
	}

	@Test
	public void shouldReturnProperToString(){
		aaiGetVnfResponse = new AaiGetVnfResponse();
		aaiGetVnfResponse.setAdditionalProperty("testKey","testValue");
		aaiGetVnfResponse.setResults(expectedList);
		Map<String,Object> expectedMap = new HashMap<>();
		expectedMap.put("testKey", "testValue");

		String expectedOutput = "AaiGetVnfResponse{" +
				"results=" + expectedList +
				", additionalProperties="+expectedMap+"}";

		assertEquals(aaiGetVnfResponse.toString(), expectedOutput);
	}

	@Test
	public void shouldAddAdditionalProperty(){
		aaiGetVnfResponse = new AaiGetVnfResponse();
		aaiGetVnfResponse.setAdditionalProperty("key", "value");
		assertTrue(aaiGetVnfResponse.getAdditionalProperties().containsKey("key"));
	}
}
