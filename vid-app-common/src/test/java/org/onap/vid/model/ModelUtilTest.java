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

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.onap.vid.mso.model.ModelInfo;
import org.testng.annotations.Test;


public class ModelUtilTest {

	private final ModelUtil testSubject = new ModelUtil();

	private ModelInfo modelWithCustomizationId(String id) {
		ModelInfo result = new ModelInfo();
		result.setModelCustomizationId(id);
		return result;
	}

	private ModelInfo modelWithModelVersionId(String id) {
		ModelInfo result = new ModelInfo();
		result.setModelVersionId(id);
		return result;
	}

	private ModelInfo modelWithNullValues() {
		return new ModelInfo();
	}

	@Test
	public void getExistingCounterMap_trivialCase() {
		Map<String, Long> existingCounterMap =
			testSubject.getExistingCounterMap(
				ImmutableMap.of(
					"a", modelWithCustomizationId("model_1"),
					"b", modelWithCustomizationId("model_1"),
					"c", modelWithCustomizationId("model_2")
				),
				identity()
			);

		assertThat(existingCounterMap, jsonEquals(ImmutableMap.of(
			"model_1", 2,
			"model_2", 1
		)));
	}

	@Test
	public void getExistingCounterMap_givenMixOfIdsAndNulls_resultContainsIdsAndOmitsNulls() {
		Map<String, Long> existingCounterMap =
			testSubject.getExistingCounterMap(
				ImmutableMap.of(
					"a", modelWithCustomizationId("model_1"),
					"b", modelWithModelVersionId("model_1"),
					"c", modelWithModelVersionId("model_2"),
					"d", modelWithNullValues()
				),
				identity()
			);

		assertThat(existingCounterMap, jsonEquals(ImmutableMap.of(
			"model_1", 2,
			"model_2", 1
		)));
	}

	@Test
	public void getExistingCounterMap_handleEmptyCollections() {
		assertThat(testSubject.getExistingCounterMap(
			emptyMap(),
			any -> modelWithCustomizationId("foo")
		), is(anEmptyMap()));
	}
}
