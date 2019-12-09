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

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.mso.model.ModelInfo;
import org.springframework.stereotype.Component;

@Component
public class ModelUtil {
	public <T> Map<String, Long> getExistingCounterMap(Map<String, T> nodeList, Function<T, ModelInfo> modelInfoExtractor) {
		return nodeList.values().stream()
			.map(it -> {
				ModelInfo modelInfo = modelInfoExtractor.apply(it);
				return StringUtils.defaultIfEmpty(modelInfo.getModelCustomizationId(), modelInfo.getModelVersionId());
			})
			.filter(Objects::nonNull)
			.collect(groupingBy(identity(), counting()));
	}
}
