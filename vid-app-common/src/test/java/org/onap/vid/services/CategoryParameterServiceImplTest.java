package org.onap.vid.services;

import org.junit.Test;
import org.onap.vid.category.AddCategoryOptionResponse;
import org.onap.vid.category.AddCategoryOptionsRequest;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameterOption;

public class CategoryParameterServiceImplTest {

    private CategoryParameterServiceImpl createTestSubject() {
        return new CategoryParameterServiceImpl();
    }

    @Test
    public void testCreateCategoryParameterOptions() throws Exception {
        CategoryParameterServiceImpl testSubject;
        String categoryName = "";
        AddCategoryOptionsRequest optionsRequest = null;
        AddCategoryOptionResponse result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createCategoryParameterOptions(categoryName, optionsRequest);
        } catch (

        Exception e) {
        }
    }

    @Test
    public void testDeleteCategoryOption() throws Exception {
        CategoryParameterServiceImpl testSubject;
        String categoryName = "";
        CategoryParameterOption option = null;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.deleteCategoryOption(categoryName, option);
        } catch (

        Exception e) {
        }
    }

    @Test
    public void testGetCategoryParameters() throws Exception {
        CategoryParameterServiceImpl testSubject;
        CategoryParametersResponse result;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.getCategoryParameters(null);
        } catch (

        Exception e) {
        }
    }

    @Test
    public void testUpdateCategoryParameterOption() throws Exception {
        CategoryParameterServiceImpl testSubject;
        String categoryName = "";
        CategoryParameterOptionRep option = null;
        AddCategoryOptionResponse result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.updateCategoryParameterOption(categoryName, option);
        } catch (

        Exception e) {
        }
    }
}