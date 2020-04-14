package org.onap.vid.category;

/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddCategoryOptionsRequest {

	public List<String> options;

	public AddCategoryOptionsRequest() {
		options = new ArrayList<>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
           return true;
		if (o == null || this.getClass() != o.getClass())
           return false;
		AddCategoryOptionsRequest that = (AddCategoryOptionsRequest) o;
		return Objects.equals(this.options, that.options);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.options);
	}
}
