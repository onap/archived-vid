package org.onap.vid.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class CategoryParametersResponse {

	private Map<String, List<CategoryParameterOptionRep>> categoryParameters;

	public CategoryParametersResponse() {
	}

	public CategoryParametersResponse(Map<String, List<CategoryParameterOptionRep>> categoryParameters) {
		this.categoryParameters = categoryParameters;
	}

	@JsonProperty("categoryParameters")
	public Map<String, List<CategoryParameterOptionRep>> getCategoryParameters() {
		return categoryParameters;
	}

	public void setCategoryParameters(Map<String, List<CategoryParameterOptionRep>> categoryParameters) {
		this.categoryParameters = categoryParameters;
	}
}
