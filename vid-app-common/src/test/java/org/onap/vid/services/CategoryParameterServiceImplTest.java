/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.ForbiddenException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.category.AddCategoryOptionResponse;
import org.onap.vid.category.AddCategoryOptionsRequest;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter;
import org.onap.vid.model.CategoryParameter.Family;
import org.onap.vid.model.CategoryParameterOption;
import org.onap.vid.services.CategoryParameterServiceImpl.AlreadyExistOptionNameException;
import org.onap.vid.services.CategoryParameterServiceImpl.UnfoundedCategoryException;
import org.onap.vid.services.CategoryParameterServiceImpl.UnfoundedCategoryOptionException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class CategoryParameterServiceImplTest {

    private static final String CATEGORY_NAME = "SAMPLE_CATEGORY_NAME";
    private static final String OPTION_NAME = "SAMPLE_OPTION_NAME";
    private static final String UNIQUE_OPTION_NAME = "UNIQUE_OPTION_NAME";
    private static final String APP_ID_VID = "VID";
    private static final String APP_ID_SDC = "SDC";
    private static final String QUERY_STRING_FOR_CATEGORY_NAME = String.format(" where name = '%s' ", CATEGORY_NAME);


    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private CategoryParameterServiceImpl testSubject;

    @BeforeSuite
    public void before() {
        initMocks(this);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(dataAccessService);
    }

    @Test
    public void createCategoryParameterOptions_happyPath()  {
        AddCategoryOptionsRequest optionsRequest = new AddCategoryOptionsRequest();
        optionsRequest.options.add(UNIQUE_OPTION_NAME);
        CategoryParameter categoryParameter = createCategoryParameter(CATEGORY_NAME, true);
        List<CategoryParameter> aList = createCategoryParametersList(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        AddCategoryOptionResponse result = testSubject.createCategoryParameterOptions(CATEGORY_NAME, optionsRequest);

        Assert.assertTrue(result.getErrors().isEmpty());;
        verify(dataAccessService, times(1))
            .saveDomainObject(anyObject(), anyObject());
    }

    @Test
    public void createCategoryParameterOptions_existingOptionsForCategory()  {
        AddCategoryOptionsRequest optionsRequest = new AddCategoryOptionsRequest();
        optionsRequest.options.add(OPTION_NAME);
        CategoryParameter categoryParameter = createCategoryParameter(CATEGORY_NAME, true);
        categoryParameter.getOptions().add(new CategoryParameterOption(APP_ID_VID, OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = createCategoryParametersList(categoryParameter);

        String expectedError = String.format(CategoryParameterServiceImpl.OPTION_ALREADY_EXIST_FOR_CATEGORY
            , OPTION_NAME, CATEGORY_NAME);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        AddCategoryOptionResponse result = testSubject.createCategoryParameterOptions(CATEGORY_NAME, optionsRequest);

        Assert.assertFalse(result.getErrors().isEmpty());
        Assert.assertEquals(result.getErrors().size(), 1);
        Assert.assertTrue(result.getErrors().stream().allMatch(expectedError::equals));
    }

    private List<CategoryParameter> createCategoryParametersList(CategoryParameter categoryParameter) {
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);
        return aList;
    }

    @Test
    public void createCategoryParameterOptions_nonExistingOptionsForCategory()  {
        AddCategoryOptionsRequest optionsRequest = new AddCategoryOptionsRequest();

        List<CategoryParameter> aList = createCategoryParametersList(new CategoryParameter());
        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        AddCategoryOptionResponse result = testSubject.createCategoryParameterOptions(CATEGORY_NAME, optionsRequest);

        Assert.assertTrue(result.getErrors().isEmpty());
    }

    @Test(expectedExceptions = { UnfoundedCategoryException.class })
    public void createCategoryParameterOptions_wrongNumberOfCategoryParameters()  {
        AddCategoryOptionsRequest optionsRequest = new AddCategoryOptionsRequest();
        List<CategoryParameter> aList = Collections.emptyList();

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        AddCategoryOptionResponse result = testSubject.createCategoryParameterOptions(CATEGORY_NAME, optionsRequest);

        Assert.fail();
    }

    @Test(expectedExceptions = { UnfoundedCategoryException.class })
    public void deleteCategoryOption_wrongNumberOfParameters() {
        CategoryParameterOption option = null;
        List<CategoryParameter> aList = Collections.emptyList();

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        testSubject.deleteCategoryOption(CATEGORY_NAME, option);

        Assert.fail();
    }

    @Test
    public void deleteCategoryOption_happyPath() {
        CategoryParameter categoryParameter = createCategoryParameter(CATEGORY_NAME, true);
        CategoryParameterOption categoryParameterOption =
            new CategoryParameterOption(APP_ID_VID, OPTION_NAME, categoryParameter);
        categoryParameter.getOptions().add(categoryParameterOption);
        List<CategoryParameter> aList = createCategoryParametersList(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(anyObject(), anyString(), anyString(), anyObject());

        testSubject.deleteCategoryOption(CATEGORY_NAME, categoryParameterOption);

        verify(dataAccessService, times(1))
            .deleteDomainObject(anyObject(), anyObject());
    }

    @Test
    public void getCategoryParametersTest() {
        CategoryParameter categoryParameter = createCategoryParameter(CATEGORY_NAME, true);
        CategoryParameterOption categoryParameterOption =
            new CategoryParameterOption(APP_ID_VID, OPTION_NAME, categoryParameter);
        categoryParameter.getOptions().add(categoryParameterOption);
        List<CategoryParameter> aList = createCategoryParametersList(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(anyObject(), anyString(), anyString(), anyObject());

        CategoryParametersResponse response = testSubject.getCategoryParameters(Family.PARAMETER_STANDARDIZATION);

        Assert.assertFalse(response.getCategoryParameters().isEmpty());
        Assert.assertTrue(response.getCategoryParameters().containsKey(CATEGORY_NAME));

        verify(dataAccessService, times(1))
            .getList(anyObject(), anyString(), anyString(), anyObject());
    }

    @Test
    public void updateCategoryParameterOption_domainObjectGetsSavedSuccessfully() {
        CategoryParameterOptionRep optionRepExisting = new CategoryParameterOptionRep(APP_ID_VID, OPTION_NAME);
        CategoryParameter categoryParameter = createCategoryParameter(CATEGORY_NAME, true);
        categoryParameter.getOptions().add(
            new CategoryParameterOption(APP_ID_VID, UNIQUE_OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = createCategoryParametersList(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        AddCategoryOptionResponse result = testSubject.updateCategoryParameterOption(CATEGORY_NAME, optionRepExisting);

        verify(dataAccessService, times(1))
            .saveDomainObject(anyObject(), anyObject());
    }

    @Test(expectedExceptions = { ForbiddenException.class })
    public void updateCategoryParameterOption_shouldFailUpdateForbidden() {
        CategoryParameterOptionRep optionRep = new CategoryParameterOptionRep("1", CATEGORY_NAME);
        CategoryParameter categoryParameter = createCategoryParameter(CATEGORY_NAME, false);
        categoryParameter.getOptions().add(new CategoryParameterOption(APP_ID_VID, OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = createCategoryParametersList(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        AddCategoryOptionResponse result = testSubject.updateCategoryParameterOption(CATEGORY_NAME, optionRep);

        Assert.fail();
    }

    @Test(expectedExceptions = { UnfoundedCategoryOptionException.class })
    public void updateCategoryParameterOption_CategoryNotFound() {
        CategoryParameterOptionRep optionRep = new CategoryParameterOptionRep("SOME_UNRELATED_ID", CATEGORY_NAME);

        CategoryParameter categoryParameter = createCategoryParameter(CATEGORY_NAME, true);
        categoryParameter.getOptions().add(new CategoryParameterOption(APP_ID_VID, OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = createCategoryParametersList(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        AddCategoryOptionResponse result = testSubject.updateCategoryParameterOption(CATEGORY_NAME, optionRep);

        Assert.fail();
    }

    @Test(expectedExceptions = { AlreadyExistOptionNameException.class })
    public void updateCategoryParameterOption_OptionNameExists() {
        CategoryParameterOptionRep optionRepExisting = new CategoryParameterOptionRep(APP_ID_VID, OPTION_NAME);

        CategoryParameter categoryParameter = createCategoryParameter(CATEGORY_NAME, true);
        CategoryParameter anotherCategoryParameter = createCategoryParameter(CATEGORY_NAME, true);
        categoryParameter.getOptions().add(
            new CategoryParameterOption(APP_ID_VID, UNIQUE_OPTION_NAME, anotherCategoryParameter));
        categoryParameter.getOptions().add(
            new CategoryParameterOption(APP_ID_SDC, OPTION_NAME, anotherCategoryParameter));
        List<CategoryParameter> aList = createCategoryParametersList(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, QUERY_STRING_FOR_CATEGORY_NAME, null, null);

        AddCategoryOptionResponse result = testSubject.updateCategoryParameterOption(CATEGORY_NAME, optionRepExisting);

        Assert.fail();
    }

    private CategoryParameter createCategoryParameter(String categoryName, boolean idSupported) {
        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(categoryName);
        categoryParameter.setIdSupported(idSupported);
        return categoryParameter;
    }
}