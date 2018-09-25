package org.onap.vid.controllers;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.BasicConfigurator;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.category.AddCategoryOptionResponse;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.services.CategoryParameterService;
import org.onap.vid.services.CategoryParameterServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.ws.rs.ForbiddenException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.onap.vid.model.CategoryParameter.Family.PARAMETER_STANDARDIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class MaintenanceControllerTest {

    @Mock
    private CategoryParameterService service;
    private MaintenanceController maintenanceController;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        maintenanceController = new MaintenanceController(service);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(maintenanceController).build();
    }

    @Test
    public void addCategoryOptions_shouldReturnMultiStatus_whenErrors() throws Exception {
        String testErrorsJson = "{\"errors\":[\"error one\",\"error two\"]}";

        when(service.createCategoryParameterOptions(anyString(), any())).thenReturn(createResponseWithErrors());

        MvcResult result = preparePostRequest()
                .andExpect(status().isMultiStatus())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(testErrorsJson);
    }

    @Test
    public void addCategoryOptions_shouldReturnOk_whenNoErrors() throws Exception {
        when(service.createCategoryParameterOptions(anyString(), any()))
                .thenReturn(new AddCategoryOptionResponse(new ArrayList<>()));
        preparePostRequest().andExpect(status().isOk());
    }

    @Test
    public void addCategoryOptions_shouldReturnNotFound_whenUnfoundedCategoryExceptionIsThrown() throws Exception {
        String unfoundedMsg = "unfounded category exception message";
        String testErrorsJson = "{\"errors\":[\"%s\"]}";

        when(service.createCategoryParameterOptions(anyString(), any()))
                .thenThrow(new CategoryParameterServiceImpl.UnfoundedCategoryException(unfoundedMsg));

        MvcResult result = preparePostRequest()
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(String.format(testErrorsJson, unfoundedMsg));
    }

    @Test
    public void updateNameForOption_shouldReturnMultiStatus_whenErrors() throws Exception {
        String testErrorsJson = "{\"errors\":[\"error one\",\"error two\"]}";

        when(service.updateCategoryParameterOption(anyString(), any())).thenReturn(createResponseWithErrors());

        MvcResult result = preparePutRequest()
                .andExpect(status().isMultiStatus())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(testErrorsJson);
    }

    @Test
    public void updateNameForOption_shouldReturnOk_whenNoErrors() throws Exception {
        when(service.updateCategoryParameterOption(anyString(), any()))
                .thenReturn(new AddCategoryOptionResponse(new ArrayList<>()));
        preparePutRequest().andExpect(status().isOk());
    }

    @Test
    public void updateNameForOption_shouldReturnForbidden_whenForbiddenExceptionIsThrown() throws Exception {
        when(service.updateCategoryParameterOption(anyString(), any())).thenThrow(new ForbiddenException());
        preparePutRequest().andExpect(status().isForbidden());
    }

    @Test
    public void updateNameForOption_shouldReturnNotFound_whenUnfoundedIsThrown() throws Exception {
        String unfoundedOptionMsg = "unfounded category option exception message";
        String testErrorsJson = "{\"errors\":[\"%s\"]}";

        when(service.updateCategoryParameterOption(anyString(), any()))
                .thenThrow(new CategoryParameterServiceImpl.UnfoundedCategoryOptionException(unfoundedOptionMsg));

        MvcResult result = preparePutRequest()
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(String.format(testErrorsJson, unfoundedOptionMsg));
    }

    @Test
    public void updateNameForOption_shouldReturnConflict_whenAlreadyExistOptionNameIsThrown() throws Exception {
        String conflictMsg = "already exists option name exception message";
        String testErrorsJson = "{\"errors\":[\"%s\"]}";

        when(service.updateCategoryParameterOption(anyString(), any()))
                .thenThrow(new CategoryParameterServiceImpl.AlreadyExistOptionNameException(conflictMsg));

        MvcResult result = preparePutRequest().andExpect(status().isConflict()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(String.format(testErrorsJson, conflictMsg));
    }

    @Test
    public void getCategoryParameter_shouldReturnExistingMap() throws Exception {
        String testKey = "key1";
        String testId = "testId";
        String testName = "testName";
        String maintenanceCategoryPath = "/maintenance/category_parameter";

        when(service.getCategoryParameters(PARAMETER_STANDARDIZATION))
                .thenReturn(createCategoryParametersResponse(testKey, testId, testName));

        mockMvc.perform(get(maintenanceCategoryPath)
                .param("familyName", "PARAMETER_STANDARDIZATION")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryParameters").isMap())
                .andExpect(jsonPath("$.categoryParameters", Matchers.hasKey(testKey)))
                .andExpect(jsonPath("$.categoryParameters.key1").isArray())
                .andExpect(jsonPath("$.categoryParameters.key1[0]",
                        Matchers.allOf(Matchers.hasEntry("id", testId), Matchers.hasEntry("name", testName))));
    }

    @Test
    public void deleteCategoryOption_shouldReturnOk_whenNoException() throws Exception {
        prepareDeleteRequest().andExpect(status().isOk());

        verify(service, times(1))
                .deleteCategoryOption(anyString(), any());
    }

    @Test
    public void deleteCategoryOption_shouldReturnNotFound_whenUnfoundedExceptionIsThrown() throws Exception {
        String unfoundedMsg = "unfounded category exception message";
        String testErrorsJson = "{\"errors\":[\"%s\"]}";

        doThrow(new CategoryParameterServiceImpl.UnfoundedCategoryException(unfoundedMsg))
                .when(service).deleteCategoryOption(anyString(), any());

        MvcResult result = prepareDeleteRequest()
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(String.format(testErrorsJson, unfoundedMsg));
    }
    
    private ResultActions preparePostRequest() throws Exception {
        String categoryName = "categoryName";
        String maintenanceCategoryPath = "/maintenance/category_parameter";
        String categoryParameterPath = maintenanceCategoryPath + "/{" + categoryName + "}";
        String testContentJson = "{\"options\": [\"first option\", \"second option\"]}";

        return mockMvc.perform(post(categoryParameterPath, categoryName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testContentJson));
    }

    private ResultActions preparePutRequest() throws Exception {
        String categoryName = "categoryName";
        String maintenanceCategoryPath = "/maintenance/category_parameter";
        String categoryParameterPath = maintenanceCategoryPath + "/{" + categoryName + "}";
        String testContentJson = "{\"options\": [\"first option\", \"second option\"]}";

        return mockMvc.perform(put(categoryParameterPath, categoryName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testContentJson));
    }

    private ResultActions prepareDeleteRequest() throws Exception {
        String categoryName = "categoryName";
        String deleteCategoryPath = "/maintenance/delete_category_parameter/{" + categoryName + "}";
        String testContentJson = "{\"options\": [\"first option\", \"second option\"]}";

        return mockMvc.perform(delete(deleteCategoryPath, categoryName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testContentJson));
    }

    private CategoryParametersResponse createCategoryParametersResponse(String key, String id, String name) {
        return new CategoryParametersResponse(
                ImmutableMap.of(
                        key, ImmutableList.of(
                                new CategoryParameterOptionRep(id, name))));
    }

    private AddCategoryOptionResponse createResponseWithErrors() {
        List<String> errors = new ArrayList<>();
        errors.add("error one");
        errors.add("error two");
        return new AddCategoryOptionResponse(errors);
    }
}