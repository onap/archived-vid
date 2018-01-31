package org.onap.vid.services;

import java.io.IOException;

import org.onap.vid.category.AddCategoryOptionResponse;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.category.AddCategoryOptionsRequest;
import org.onap.vid.model.CategoryParameterOption;
import org.onap.vid.model.CategoryParameter.Family;

public interface CategoryParameterService {
	
	CategoryParametersResponse getCategoryParameters(Family familyName) throws IOException;
	AddCategoryOptionResponse createCategoryParameterOptions(String categoryName, AddCategoryOptionsRequest option) throws IOException;
	AddCategoryOptionResponse updateCategoryParameterOption(String categoryName, CategoryParameterOptionRep option);
	void deleteCategoryOption(String categoryName, CategoryParameterOption option) throws IOException;
}
