package org.onap.vid.controllers;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.onap.vid.category.AddCategoryOptionsRequest;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.model.CategoryParameterOption;
import org.springframework.http.ResponseEntity;

public class MaintenanceControllerTest {

    private MaintenanceController createTestSubject() {
        return new MaintenanceController();
    }

    @Test
    public void testAddCategoryOptions() throws Exception {
        MaintenanceController testSubject;
        HttpServletRequest request = null;
        String categoryName = "";
        AddCategoryOptionsRequest option = null;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.addCategoryOptions(request, categoryName, option);
    }

    @Test
    public void testUpdateNameForOption() throws Exception {
        MaintenanceController testSubject;
        HttpServletRequest request = null;
        String categoryName = "";
        CategoryParameterOptionRep option = null;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.updateNameForOption(request, categoryName, option);
    }

    @Test
    public void testGetCategoryParameter() throws Exception {
        MaintenanceController testSubject;
        HttpServletRequest request = null;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCategoryParameter(request, null);
    }

    @Test
    public void testDeleteCategoryOption() throws Exception {
        MaintenanceController testSubject;
        HttpServletRequest request = null;
        String categoryName = "";
        CategoryParameterOption option = null;
        ResponseEntity result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.deleteCategoryOption(request, categoryName, option);
    }
}