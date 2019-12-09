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

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import org.onap.vid.mso.model.ModelInfo;
import org.testng.annotations.Test;


public class ModelUtilTest {

	private final ModelUtil testSubject = new ModelUtil();

	private final Function<Integer, ModelInfo> intToModelInfo = n -> {
		// yields a model info with a single id, or null if n==0
		ModelInfo result = new ModelInfo();
		if (n > 0) {
			result.setModelCustomizationId("model_" + n);
		} else if (n<0) {
			result.setModelVersionId("model_" + (n * -1));
		} else {
			// keep nulls
		}
		return result;
	};

	@Test
	public void getExistingCounterMap_trivialCase() {
		Map<String, Long> existingCounterMap =
			testSubject.getExistingCounterMap(
				ImmutableMap.of(
					"a", 1,
					"b", 1,
					"c", 2
				),
				intToModelInfo
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
					"a", 1,
					"b", -1, // ModelVersionId instead of customizationId
					"c", -2,
					"d", 0  // skip nulls
				),
				intToModelInfo
			);

		assertThat(existingCounterMap, jsonEquals(ImmutableMap.of(
			"model_1", 2,
			"model_2", 1
		)));
	}

	@Test
	public void getExistingCounterMap_handleEmptyCollections() {
		Map<String, Integer> emptyNodeList = Collections.emptyMap();
		assertThat(testSubject.getExistingCounterMap(emptyNodeList, intToModelInfo), is(anEmptyMap()));
	}
}
