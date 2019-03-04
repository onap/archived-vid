/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;


public class RelatedInstanceTest {

	private RelatedInstance relatedInstance = new RelatedInstance();

	private String instanceId = "testInstanceId";
	private String InstanceName = "testInstance";
	private String propertyName = "testProperty";
	private String additionalProperty = "testAdditionalProperty";

	@BeforeMethod
	public void setUp() {
		relatedInstance = new RelatedInstance();
	}

	@Test
	public void shouldHaveProperGettersAndSetters() {
		assertThat(RelatedInstance.class, hasValidGettersAndSettersExcluding("additionalProperties"));
	}

	@Test
	public void shouldHaveProperGetterAndSetterForAdditionalProperties() {
		//	when
		relatedInstance.setAdditionalProperty(propertyName,additionalProperty);

		//	then
		assertThat( relatedInstance.getAdditionalProperties().get(propertyName) ).isEqualTo(additionalProperty);
	}

	@Test
	public void shouldProperlyConvertRelatedInstanceObjectToString() {
		//	given
		relatedInstance.setInstanceId(instanceId);
		relatedInstance.setInstanceName(InstanceName);
		relatedInstance.setAdditionalProperty(propertyName,additionalProperty);

		//	when
		String response = relatedInstance.toString();

		//	then
		assertThat(response).contains(
						"instanceName="+InstanceName+"," +
						"instanceId="+instanceId+"," +
						"modelInfo=<null>," +
						"additionalProperties={"+propertyName+"="+additionalProperty+"}]"
		);
	}

	@Test
	public void shouldProperlyCheckIfObjectsAreEqual() {
		assertThat(RelatedInstance.class, hasValidBeanEqualsExcluding("additionalProperties"));
	}


}