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
        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(CATEGORY_NAME);
        categoryParameter.getOptions().add(new CategoryParameterOption("VID", OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);
        String queryString = String.format(" where name = '%s' ", CATEGORY_NAME);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        AddCategoryOptionResponse result = testSubject.createCategoryParameterOptions(CATEGORY_NAME, optionsRequest);

        Assert.assertTrue(result.getErrors().isEmpty());;
        verify(dataAccessService, times(1))
            .saveDomainObject(anyObject(), anyObject());
    }

    @Test
    public void createCategoryParameterOptions_existingOptionsForCategory()  {
        AddCategoryOptionsRequest optionsRequest = new AddCategoryOptionsRequest();
        optionsRequest.options.add(OPTION_NAME);
        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(CATEGORY_NAME);
        categoryParameter.getOptions().add(new CategoryParameterOption("VID", OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);
        String queryString = String.format(" where name = '%s' ", CATEGORY_NAME);

        String expectedError = String.format(CategoryParameterServiceImpl.OPTION_S_ALREADY_EXIST_FOR_CATEGORY_S
            , OPTION_NAME, CATEGORY_NAME);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        AddCategoryOptionResponse result = testSubject.createCategoryParameterOptions(CATEGORY_NAME, optionsRequest);

        Assert.assertFalse(result.getErrors().isEmpty());
        Assert.assertEquals(result.getErrors().size(), 1);
        Assert.assertTrue(result.getErrors().stream().allMatch(expectedError::equals));
    }

    @Test
    public void createCategoryParameterOptions_nonExistingOptionsForCategory()  {
        String categoryName = "";
        AddCategoryOptionsRequest optionsRequest = new AddCategoryOptionsRequest();

        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(new CategoryParameter());
        String queryString = String.format(" where name = '%s' ", categoryName);
        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        AddCategoryOptionResponse result = testSubject.createCategoryParameterOptions(categoryName, optionsRequest);

        Assert.assertTrue(result.getErrors().isEmpty());
    }

    @Test(expectedExceptions = { UnfoundedCategoryException.class })
    public void createCategoryParameterOptions_wrongNumberOfCategoryParameters()  {
        AddCategoryOptionsRequest optionsRequest = new AddCategoryOptionsRequest();
        String queryString = String.format(" where name = '%s' ", CATEGORY_NAME);
        List<CategoryParameter> aList = Collections.emptyList();

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        AddCategoryOptionResponse result = testSubject.createCategoryParameterOptions(CATEGORY_NAME, optionsRequest);

        Assert.fail();
    }

    @Test(expectedExceptions = { UnfoundedCategoryException.class })
    public void deleteCategoryOption_wrongNumberOfParameters() {
        CategoryParameterOption option = null;
        String queryString = String.format(" where name = '%s' ", CATEGORY_NAME);
        List<CategoryParameter> aList = Collections.emptyList();

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        testSubject.deleteCategoryOption(CATEGORY_NAME, option);

        Assert.fail();
    }

    @Test
    public void deleteCategoryOption_happyPath() {
        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(CATEGORY_NAME);
        CategoryParameterOption categoryParameterOption =
            new CategoryParameterOption("VID", OPTION_NAME, categoryParameter);
        categoryParameter.getOptions().add(categoryParameterOption);
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(anyObject(), anyString(), anyString(), anyObject());

        testSubject.deleteCategoryOption(CATEGORY_NAME, categoryParameterOption);

        verify(dataAccessService, times(1))
            .deleteDomainObject(anyObject(), anyObject());
    }

    @Test
    public void getCategoryParametersTest() {
        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(CATEGORY_NAME);
        CategoryParameterOption categoryParameterOption =
            new CategoryParameterOption("VID", OPTION_NAME, categoryParameter);
        categoryParameter.getOptions().add(categoryParameterOption);
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);

        doReturn(aList).when(dataAccessService).getList(anyObject(), anyString(), anyString(), anyObject());

        CategoryParametersResponse response = testSubject.getCategoryParameters(Family.PARAMETER_STANDARDIZATION);

        Assert.assertFalse(response.getCategoryParameters().isEmpty());
        Assert.assertTrue(response.getCategoryParameters().containsKey(CATEGORY_NAME));

        verify(dataAccessService, times(1))
            .getList(anyObject(), anyString(), anyString(), anyObject());
    }

    @Test
    public void updateCategoryParameterOption_domainObjectGetsSavedSuccessfully() {
        CategoryParameterOptionRep optionRepExisting = new CategoryParameterOptionRep("VID", OPTION_NAME);

        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(CATEGORY_NAME);
        categoryParameter.setIdSupported(true);
        categoryParameter.getOptions().add(
            new CategoryParameterOption("VID", UNIQUE_OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);
        String queryString = String.format(" where name = '%s' ", CATEGORY_NAME);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        AddCategoryOptionResponse result = testSubject.updateCategoryParameterOption(CATEGORY_NAME, optionRepExisting);

        verify(dataAccessService, times(1))
            .saveDomainObject(anyObject(), anyObject());
    }

    @Test(expectedExceptions = { ForbiddenException.class })
    public void updateCategoryParameterOption_shouldFailUpdateForbidden() {
        CategoryParameterOptionRep optionRep = new CategoryParameterOptionRep("1", CATEGORY_NAME);

        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(CATEGORY_NAME);
        categoryParameter.setIdSupported(false);
        categoryParameter.getOptions().add(new CategoryParameterOption("VID", OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);
        String queryString = String.format(" where name = '%s' ", CATEGORY_NAME);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        AddCategoryOptionResponse result = testSubject.updateCategoryParameterOption(CATEGORY_NAME, optionRep);

        Assert.fail();
    }

    @Test(expectedExceptions = { UnfoundedCategoryOptionException.class })
    public void updateCategoryParameterOption_CategoryNotFound() {
        CategoryParameterOptionRep optionRep = new CategoryParameterOptionRep("SOME_UNRELATED_ID", CATEGORY_NAME);

        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(CATEGORY_NAME);
        categoryParameter.setIdSupported(true);
        categoryParameter.getOptions().add(new CategoryParameterOption("VID", OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);
        String queryString = String.format(" where name = '%s' ", CATEGORY_NAME);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        AddCategoryOptionResponse result = testSubject.updateCategoryParameterOption(CATEGORY_NAME, optionRep);

        Assert.fail();
    }

    @Test(expectedExceptions = { AlreadyExistOptionNameException.class })
    public void updateCategoryParameterOption_OptionNameExists() {
        CategoryParameterOptionRep optionRepExisting = new CategoryParameterOptionRep("VID", OPTION_NAME);

        CategoryParameter categoryParameter = new CategoryParameter();
        categoryParameter.setName(CATEGORY_NAME);
        categoryParameter.setIdSupported(true);
        categoryParameter.getOptions().add(
            new CategoryParameterOption("VID", UNIQUE_OPTION_NAME, categoryParameter));
        categoryParameter.getOptions().add(
            new CategoryParameterOption("SDC", OPTION_NAME, categoryParameter));
        List<CategoryParameter> aList = new ArrayList<>();
        aList.add(categoryParameter);
        String queryString = String.format(" where name = '%s' ", CATEGORY_NAME);

        doReturn(aList).when(dataAccessService).getList(CategoryParameter.class, queryString, null, null);

        AddCategoryOptionResponse result = testSubject.updateCategoryParameterOption(CATEGORY_NAME, optionRepExisting);

        Assert.fail();
    }
}