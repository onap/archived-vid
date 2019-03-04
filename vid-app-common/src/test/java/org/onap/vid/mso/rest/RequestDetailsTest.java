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

import org.assertj.core.api.AssertionsForClassTypes;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;


public class RequestDetailsTest {

	private RequestDetails requestDetails;

	private String propertyName = "testProperty";
	private String additionalProperty = "testAdditionalProperty";

	@BeforeMethod
	public void setUp() {
		requestDetails = new RequestDetails();
	}

	@Test
	public void shouldHaveProperSettersAndGetters() {
		assertThat(RequestDetails.class, hasValidGettersAndSettersExcluding("additionalProperties"));
	}

	@Test
	public void shouldHaveProperGetterAndSetterForAdditionalProperties() {
		//	when
		requestDetails.setAdditionalProperty(propertyName,additionalProperty);

		//	then
		AssertionsForClassTypes.assertThat( requestDetails.getAdditionalProperties().get(propertyName) ).isEqualTo(additionalProperty);
	}

	@Test
	public void shouldProperlyConvertRelatedInstanceObjectToString() {
		//	given
		requestDetails.setAdditionalProperty(propertyName,additionalProperty);

		//	when
		String response = requestDetails.toString();

		//	then
		AssertionsForClassTypes.assertThat(response).contains(
						"additionalProperties={"+propertyName+"="+additionalProperty+"}]"
		);
	}

	@Test
	public void shouldProperlyCheckIfObjectsAreEqual() {
		//	given
		RequestDetails sameRequestDetails = new RequestDetails();
		RequestDetails differentRequestDetails = new RequestDetails();

		ModelInfo modelInfo = new ModelInfo();
		modelInfo.setModelType("testModel");

		CloudConfiguration cloudConfiguration = new CloudConfiguration();
		cloudConfiguration.setTenantId("testTenantId");

		requestDetails.setModelInfo(modelInfo);
		requestDetails.setCloudConfiguration(cloudConfiguration);

		sameRequestDetails.setModelInfo(modelInfo);
		sameRequestDetails.setCloudConfiguration(cloudConfiguration);

		//	when
		boolean sameResponse = requestDetails.equals(requestDetails);
		boolean equalResponse = requestDetails.equals(sameRequestDetails);
		boolean differentResponse = requestDetails.equals(differentRequestDetails);
		boolean differentClassResponse = requestDetails.equals("RelatedInstance");

		//	then
		assertThat(sameResponse).isEqualTo(true);
		assertThat(equalResponse).isEqualTo(true);

		assertThat(differentResponse).isEqualTo(false);
		assertThat(differentClassResponse).isEqualTo(false);
	}

	@Test
	public void shouldProperlyExtractValueByPathUsingAdditionalProperties(){
		//	given
		List<String> keys = new ArrayList<>();

		requestDetails.setAdditionalProperty(propertyName,additionalProperty);
		keys.add(propertyName);

		//	when
		String response = requestDetails.extractValueByPathUsingAdditionalProperties(keys,String.class);

		//	then
		assertThat(response).contains(additionalProperty);
		assertThat(response).doesNotContain("notExistingProperty");
	}

	@Test(expectedExceptions = NotFoundException.class )
	public void shouldThrowExceptionWhenExtractValueByPathUsingAdditionalPropertiesIsCalledWithWrongKey(){
		//	given
		List<String> keys = new ArrayList<>();

		requestDetails.setAdditionalProperty(propertyName,additionalProperty);
		keys.add("notExistingKey");

		//	when
		requestDetails.extractValueByPathUsingAdditionalProperties(keys,String.class);
	}

	@Test(expectedExceptions = NotFoundException.class )
	public void shouldThrowExceptionWhenExtractValueByPathUsingAdditionalPropertiesIsCalledWithWrongClass(){
		//	given
		List<String> keys = new ArrayList<>();

		requestDetails.setAdditionalProperty(propertyName,additionalProperty);
		keys.add(propertyName);

		//	when
		requestDetails.extractValueByPathUsingAdditionalProperties(keys,Integer.class);
	}
}
