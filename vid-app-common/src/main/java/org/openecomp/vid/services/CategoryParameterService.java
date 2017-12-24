package org.openecomp.vid.services;

import java.io.IOException;

import org.openecomp.vid.category.AddCategoryOptionResponse;
import org.openecomp.vid.category.CategoryParameterOptionRep;
import org.openecomp.vid.category.CategoryParametersResponse;
import org.openecomp.vid.category.AddCategoryOptionsRequest;
import org.openecomp.vid.model.CategoryParameterOption;

public interface CategoryParameterService {
	
	CategoryParametersResponse getCategoryParameters() throws IOException;
	AddCategoryOptionResponse createCategoryParameterOptions(String categoryName, AddCategoryOptionsRequest option) throws IOException;
	AddCategoryOptionResponse updateCategoryParameterOption(String categoryName, CategoryParameterOptionRep option);
	void deleteCategoryOption(String categoryName, CategoryParameterOption option) throws IOException;
}
