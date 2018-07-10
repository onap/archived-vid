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
