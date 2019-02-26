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

package org.onap.vid.services;

import org.onap.vid.category.AddCategoryOptionResponse;
import org.onap.vid.category.AddCategoryOptionsRequest;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter.Family;
import org.onap.vid.model.CategoryParameterOption;

public interface CategoryParameterService {
	
	CategoryParametersResponse getCategoryParameters(Family familyName);
	AddCategoryOptionResponse createCategoryParameterOptions(String categoryName, AddCategoryOptionsRequest option);
	AddCategoryOptionResponse updateCategoryParameterOption(String categoryName, CategoryParameterOptionRep option);
	void deleteCategoryOption(String categoryName, CategoryParameterOption option);
}
