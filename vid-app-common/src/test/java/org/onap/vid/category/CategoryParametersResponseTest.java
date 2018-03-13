package org.onap.vid.category;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CategoryParametersResponseTest {

    private CategoryParametersResponse createTestSubject() {
        return new CategoryParametersResponse();
    }

    @Test
    public void testGetCategoryParameters() throws Exception {
        CategoryParametersResponse testSubject;
        Map<String, List<CategoryParameterOptionRep>> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCategoryParameters();
    }

    @Test
    public void testSetCategoryParameters() throws Exception {
        CategoryParametersResponse testSubject;
        Map<String, List<CategoryParameterOptionRep>> categoryParameters = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setCategoryParameters(categoryParameters);
    }
}