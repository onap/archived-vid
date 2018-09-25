package org.onap.vid.controllers;

import org.apache.log4j.BasicConfigurator;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.category.AddCategoryOptionResponse;
import org.onap.vid.category.AddCategoryOptionsRequest;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter;
import org.onap.vid.model.CategoryParameterOption;
import org.onap.vid.services.CategoryParameterService;
import org.onap.vid.services.CategoryParameterServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.ws.rs.ForbiddenException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class MaintenanceControllerTest {

    private static final String OPTION_1 = "first option";
    private static final String OPTION_2 = "second option";
    private static final String TEST_CONTENT_JSON = "{\"options\": [\"" + OPTION_1 + "\", \"" + OPTION_2 + "\"]}";
    private static final String ERROR_1 = "error one";
    private static final String ERROR_2 = "error two";
    private static final String ERRORS = ERROR_1 + "\",\"" + ERROR_2;
    private static final String TEST_ERRORS_JSON = "{\"errors\":[\"%s\"]}";
    private static final String UNFOUNDED_MSG = "unfounded category exception message";
    private static final String UNFOUNDED_OPTION_MSG = "unfounded category option exception message";
    private static final String CONFLICT_MSG = "already exists option name exception message";
    private static final String MAINTENANCE_CATEGORY_PATH = "/maintenance/category_parameter";
    private static final String CATEGORY_NAME = "categoryName";
    private static final String CATEGORY_PARAMETER_PATH = MAINTENANCE_CATEGORY_PATH + "/{" + CATEGORY_NAME + "}";
    private static final String DELETE_CATEGORY_PATH = "/maintenance/delete_category_parameter/{" + CATEGORY_NAME + "}";
    private static final String TEST_KEY = "key1";
    private static final String TEST_NAME = "testName";
    private static final String TEST_ID = "testId";

    @Mock
    private CategoryParameterService categoryParameterService;
    private MaintenanceController maintenanceController;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        maintenanceController = new MaintenanceController(categoryParameterService);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(maintenanceController).build();
    }

    //--------------------------- ADD CATEGORY OPTIONS TESTS ---------------------------------

    @Test
    public void addCategoryOptions_shouldReturnMultiStatus_whenErrors() throws Exception {
        when(categoryParameterService.createCategoryParameterOptions(
                anyString(), isA(AddCategoryOptionsRequest.class))).thenReturn(createResponseWithErrors());

        MvcResult result = basicMockMvcPostSetUp(CATEGORY_PARAMETER_PATH)
                .andExpect(status().isMultiStatus())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(String.format(TEST_ERRORS_JSON, ERRORS));

        ArgumentCaptor<AddCategoryOptionsRequest> req = ArgumentCaptor.forClass(AddCategoryOptionsRequest.class);
        verify(categoryParameterService).createCategoryParameterOptions(anyString(), req.capture());
        assertThat(req.getValue().options.size()).isEqualTo(2);
        assertThat(req.getValue().options.get(0)).isEqualTo(OPTION_1);
        assertThat(req.getValue().options.get(1)).isEqualTo(OPTION_2);
    }

    @Test
    public void addCategoryOptions_shouldReturnOk_whenNoErrors() throws Exception {
        when(categoryParameterService.createCategoryParameterOptions(
                anyString(), isA(AddCategoryOptionsRequest.class)))
                .thenReturn(new AddCategoryOptionResponse(new ArrayList<>()));

        basicMockMvcPostSetUp(CATEGORY_PARAMETER_PATH)
                .andExpect(status().isOk());
    }

    @Test
    public void addCategoryOptions_shouldReturnNotFound_whenUnfoundedCategoryExceptionIsThrown() throws Exception {
        when(categoryParameterService.createCategoryParameterOptions(anyString(), isA(AddCategoryOptionsRequest.class)))
                .thenThrow(new CategoryParameterServiceImpl.UnfoundedCategoryException(UNFOUNDED_MSG));

        MvcResult result = basicMockMvcPostSetUp(CATEGORY_PARAMETER_PATH)
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(String.format(TEST_ERRORS_JSON, UNFOUNDED_MSG));
    }

    //--------------------------- UPDATE NAME FOR OPTION TESTS ---------------------------------

    @Test
    public void updateNameForOption_shouldReturnMultiStatus_whenErrors() throws Exception {
        when(categoryParameterService.updateCategoryParameterOption(
                anyString(), isA(CategoryParameterOptionRep.class))).thenReturn(createResponseWithErrors());

        MvcResult result = basicMockMvcPutSetUp()
                .andExpect(status().isMultiStatus())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(String.format(TEST_ERRORS_JSON, ERRORS));
    }

    @Test
    public void updateNameForOption_shouldReturnOk_whenNoErrors() throws Exception {
        when(categoryParameterService.updateCategoryParameterOption(
                anyString(), isA(CategoryParameterOptionRep.class)))
                .thenReturn(new AddCategoryOptionResponse(new ArrayList<>()));

        basicMockMvcPutSetUp()
                .andExpect(status().isOk());
    }

    @Test
    public void updateNameForOption_shouldReturnForbidden_whenForbiddenExceptionIsThrown() throws Exception {
        when(categoryParameterService.updateCategoryParameterOption(
                anyString(), isA(CategoryParameterOptionRep.class))).thenThrow(new ForbiddenException());

        basicMockMvcPutSetUp()
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateNameForOption_shouldReturnNotFound_whenUnfoundedIsThrown() throws Exception {
        when(categoryParameterService.updateCategoryParameterOption(
                anyString(), isA(CategoryParameterOptionRep.class)))
                .thenThrow(new CategoryParameterServiceImpl.UnfoundedCategoryOptionException(UNFOUNDED_OPTION_MSG));

        MvcResult result = basicMockMvcPutSetUp()
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(String.format(TEST_ERRORS_JSON, UNFOUNDED_OPTION_MSG));
    }

    @Test
    public void updateNameForOption_shouldReturnConflict_whenAlreadyExistOptionNameIsThrown() throws Exception {
        when(categoryParameterService.updateCategoryParameterOption(
                anyString(), isA(CategoryParameterOptionRep.class)))
                .thenThrow(new CategoryParameterServiceImpl.AlreadyExistOptionNameException(CONFLICT_MSG));

        MvcResult result = basicMockMvcPutSetUp()
                .andExpect(status().isConflict())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(String.format(TEST_ERRORS_JSON, CONFLICT_MSG));
    }

    //--------------------------- GET CATEGORY PARAMETER TESTS ---------------------------------

    @Test
    public void getCategoryParameter_shouldReturnExistingMap() throws Exception {
        when(categoryParameterService.getCategoryParameters(CategoryParameter.Family.PARAMETER_STANDARDIZATION))
                .thenReturn(new CategoryParametersResponse(createCategoryParametersMap()));

        mockMvc.perform(get(MAINTENANCE_CATEGORY_PATH)
                .param("familyName", "PARAMETER_STANDARDIZATION")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryParameters").isMap())
                .andExpect(jsonPath("$.categoryParameters", Matchers.hasKey(TEST_KEY)))
                .andExpect(jsonPath("$.categoryParameters.key1").isArray())
                .andExpect(jsonPath("$.categoryParameters.key1[0]",
                        Matchers.allOf(Matchers.hasEntry("id", TEST_ID), Matchers.hasEntry("name", TEST_NAME))));
    }

    //--------------------------- DELETE CATEGORY OPTIONS TESTS ---------------------------------

    @Test
    public void deleteCategoryOption_shouldReturnOk_whenNoException() throws Exception {
        doNothing().when(categoryParameterService).deleteCategoryOption(
                anyString(), isA(CategoryParameterOption.class));

        basicMockMvcPostSetUp(DELETE_CATEGORY_PATH)
                .andExpect(status().isOk());

        verify(categoryParameterService, times(1))
                .deleteCategoryOption(anyString(), isA(CategoryParameterOption.class));
    }

    @Test
    public void deleteCategoryOption_shouldReturnNotFound_whenUnfoundedExceptionIsThrown() throws Exception {
        doThrow(new CategoryParameterServiceImpl.UnfoundedCategoryException(UNFOUNDED_MSG))
                .when(categoryParameterService).deleteCategoryOption(anyString(), isA(CategoryParameterOption.class));

        MvcResult result = basicMockMvcPostSetUp(DELETE_CATEGORY_PATH)
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(String.format(TEST_ERRORS_JSON, UNFOUNDED_MSG));
    }

    private ResultActions basicMockMvcPostSetUp(String path) throws Exception {
        return mockMvc.perform(post(path, CATEGORY_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_CONTENT_JSON));
    }

    private ResultActions basicMockMvcPutSetUp() throws Exception {
        return mockMvc.perform(put(CATEGORY_PARAMETER_PATH, CATEGORY_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_CONTENT_JSON));
    }

    private Map<String, List<CategoryParameterOptionRep>> createCategoryParametersMap() {
        CategoryParameterOptionRep categoryParameterOptionRep = new CategoryParameterOptionRep(TEST_ID, TEST_NAME);
        List<CategoryParameterOptionRep> li = new ArrayList<>();
        li.add(categoryParameterOptionRep);
        Map<String, List<CategoryParameterOptionRep>> map = new LinkedHashMap<>();
        map.put(TEST_KEY, li);
        return map;
    }

    private AddCategoryOptionResponse createResponseWithErrors() {
        List<String> errors = new ArrayList<>();
        errors.add(ERROR_1);
        errors.add(ERROR_2);
        return new AddCategoryOptionResponse(errors);
    }
}