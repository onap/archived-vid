package org.onap.vid.controllers;

/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright 2018 Nokia
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.onap.vid.model.CategoryParameter.Family.PARAMETER_STANDARDIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.function.BiFunction;
import javax.ws.rs.ForbiddenException;
import org.apache.log4j.BasicConfigurator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class MaintenanceControllerTest {

    final private String MAINTENANCE_CATEGORY_PATH = "/maintenance/category_parameter";
    final private String CATEGORY_PARAMETER_PATH = MAINTENANCE_CATEGORY_PATH + "/{name}";
    final private String DELETE_CATEGORY_PATH = "/maintenance/delete_category_parameter/{name}";

    @Mock
    private CategoryParameterService service;
    private MaintenanceController maintenanceController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        maintenanceController = new MaintenanceController(service);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(maintenanceController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void addCategoryOptions_shouldReturnMultiStatus_whenErrorsExist() throws Exception {
        String categoryName = "catName1";
        AddCategoryOptionsRequest req = new AddCategoryOptionsRequest();
        req.options = ImmutableList.of("first option", "second option");
        AddCategoryOptionResponse addCategoryOptionResponse = new AddCategoryOptionResponse(
            ImmutableList.of("error one", "error two"));

        given(service.createCategoryParameterOptions(eq(categoryName), argThat(requestMatcher(req))))
            .willReturn(addCategoryOptionResponse);

        prepareRequestExpectations(MockMvcRequestBuilders::post, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(req))
            .andExpect(status().isMultiStatus())
            .andExpect(
                content().json(objectMapper
                    .writeValueAsString(addCategoryOptionResponse)));
    }

    @Test
    public void addCategoryOptions_shouldReturnOk_whenNoErrorsExist() throws Exception {
        String categoryName = "catName2";
        AddCategoryOptionsRequest req = new AddCategoryOptionsRequest();
        req.options = ImmutableList.of("first option", "second option", "third option");
        AddCategoryOptionResponse addCategoryOptionResponse = new AddCategoryOptionResponse(Collections.emptyList());

        given(service.createCategoryParameterOptions(eq(categoryName), argThat(requestMatcher(req))))
            .willReturn(addCategoryOptionResponse);
        prepareRequestExpectations(MockMvcRequestBuilders::post, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(req))
            .andExpect(status().isOk())
            .andExpect(content().json(
                objectMapper.writeValueAsString(addCategoryOptionResponse)));
    }

    @Test
    public void addCategoryOptions_shouldReturnNotFound_whenUnfoundedCategoryExceptionIsThrown() throws Exception {
        String unfoundedMsg = "unfounded category exception message";
        String categoryName = "catName3";
        AddCategoryOptionsRequest req = new AddCategoryOptionsRequest();
        req.options = ImmutableList.of("first option");

        given(service.createCategoryParameterOptions(eq(categoryName), argThat(requestMatcher(req))))
            .willThrow(new CategoryParameterServiceImpl.UnfoundedCategoryException(unfoundedMsg));

        prepareRequestExpectations(MockMvcRequestBuilders::post, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(req))
            .andExpect(status().isNotFound())
            .andExpect(content().json(
                objectMapper.writeValueAsString(new AddCategoryOptionResponse(ImmutableList.of(unfoundedMsg)))));
    }

    @Test
    public void addCategoryOptions_shouldReturnInternalServerError_whenExceptionIsThrown() throws Exception {
        String categoryName = "catName13";
        AddCategoryOptionsRequest req = new AddCategoryOptionsRequest();
        req.options = ImmutableList.of("option second", "first option");

        given(service.createCategoryParameterOptions(eq(categoryName), argThat(requestMatcher(req))))
            .willThrow(new RuntimeException());

        prepareRequestExpectations(MockMvcRequestBuilders::post, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(req))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateNameForOption_shouldReturnMultiStatus_whenErrorsExist() throws Exception {
        String categoryName = "catName4";
        CategoryParameterOptionRep categoryParameterOptionRep = new CategoryParameterOptionRep("id1", "name1");
        AddCategoryOptionResponse addCategoryOptionResponse = new AddCategoryOptionResponse(
            ImmutableList.of("error one", "error two"));

        given(service
            .updateCategoryParameterOption(eq(categoryName), argThat(requestMatcher(categoryParameterOptionRep))))
            .willReturn(addCategoryOptionResponse);

        prepareRequestExpectations(MockMvcRequestBuilders::put, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOptionRep))
            .andExpect(status().isMultiStatus())
            .andExpect(
                content().json(objectMapper
                    .writeValueAsString(addCategoryOptionResponse)));
    }

    @Test
    public void updateNameForOption_shouldReturnOk_whenNoErrorsExist() throws Exception {
        String categoryName = "catName5";
        CategoryParameterOptionRep categoryParameterOptionRep = new CategoryParameterOptionRep("id2", "name2");
        AddCategoryOptionResponse addCategoryOptionResponse = new AddCategoryOptionResponse(Collections.emptyList());

        given(service
            .updateCategoryParameterOption(eq(categoryName), argThat(requestMatcher(categoryParameterOptionRep))))
            .willReturn(addCategoryOptionResponse);
        prepareRequestExpectations(MockMvcRequestBuilders::put, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOptionRep))
            .andExpect(status().isOk())
            .andExpect(content().json(
                objectMapper.writeValueAsString(addCategoryOptionResponse)));
    }

    @Test
    public void updateNameForOption_shouldReturnForbidden_whenForbiddenExceptionIsThrown() throws Exception {
        String categoryName = "catName6";
        CategoryParameterOptionRep categoryParameterOptionRep = new CategoryParameterOptionRep("id3", "name3");

        given(service
            .updateCategoryParameterOption(eq(categoryName), argThat(requestMatcher(categoryParameterOptionRep))))
            .willThrow(new ForbiddenException());
        prepareRequestExpectations(MockMvcRequestBuilders::put, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOptionRep))
            .andExpect(status().isForbidden())
            .andExpect(content().json(
                objectMapper
                    .writeValueAsString(new AddCategoryOptionResponse(ImmutableList.of("HTTP 403 Forbidden")))));
    }

    @Test
    public void updateNameForOption_shouldReturnNotFound_whenUnfoundedIsThrown() throws Exception {
        String unfoundedOptionMsg = "unfounded category option exception message";
        String categoryName = "catName7";
        CategoryParameterOptionRep categoryParameterOptionRep = new CategoryParameterOptionRep("id4", "name4");

        given(service
            .updateCategoryParameterOption(eq(categoryName), argThat(requestMatcher(categoryParameterOptionRep))))
            .willThrow(new CategoryParameterServiceImpl.UnfoundedCategoryOptionException(unfoundedOptionMsg));

        prepareRequestExpectations(MockMvcRequestBuilders::put, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOptionRep))
            .andExpect(status().isNotFound())
            .andExpect(content().json(
                objectMapper.writeValueAsString(new AddCategoryOptionResponse(ImmutableList.of(unfoundedOptionMsg)))));
    }

    @Test
    public void updateNameForOption_shouldReturnConflict_whenAlreadyExistOptionNameIsThrown() throws Exception {
        String conflictMsg = "already exists option name exception message";
        String categoryName = "catName8";
        CategoryParameterOptionRep categoryParameterOptionRep = new CategoryParameterOptionRep("id5", "name5");

        given(service
            .updateCategoryParameterOption(eq(categoryName), argThat(requestMatcher(categoryParameterOptionRep))))
            .willThrow(new CategoryParameterServiceImpl.AlreadyExistOptionNameException(conflictMsg));

        prepareRequestExpectations(MockMvcRequestBuilders::put, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOptionRep))
            .andExpect(status().isConflict())
            .andExpect(content().json(
                objectMapper.writeValueAsString(new AddCategoryOptionResponse(ImmutableList.of(conflictMsg)))));
    }

    @Test
    public void updateNameForOption_shouldReturnInternalServerError_whenExceptionIsThrown() throws Exception {
        String categoryName = "catName18";
        CategoryParameterOptionRep categoryParameterOptionRep = new CategoryParameterOptionRep("id6", "name6");

        given(service
            .updateCategoryParameterOption(eq(categoryName), argThat(requestMatcher(categoryParameterOptionRep))))
            .willThrow(new RuntimeException());

        prepareRequestExpectations(MockMvcRequestBuilders::put, CATEGORY_PARAMETER_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOptionRep))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void getCategoryParameter_shouldReturnExistingMap() throws Exception {
        CategoryParametersResponse categoryParametersResponse =
            new CategoryParametersResponse(
                ImmutableMap.of(
                    "key1", ImmutableList.of(
                        new CategoryParameterOptionRep("testId", "testName"))));

        given(service.getCategoryParameters(PARAMETER_STANDARDIZATION))
            .willReturn(categoryParametersResponse);

        mockMvc.perform(get(MAINTENANCE_CATEGORY_PATH)
            .param("familyName", "PARAMETER_STANDARDIZATION")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(categoryParametersResponse)));
    }

    @Test
    public void getCategoryParameter_shouldReturnInternalServerError_whenExceptionIsThrown() throws Exception {
        given(service.getCategoryParameters(PARAMETER_STANDARDIZATION))
            .willThrow(new RuntimeException());

        mockMvc.perform(get(MAINTENANCE_CATEGORY_PATH)
            .param("familyName", "PARAMETER_STANDARDIZATION")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteCategoryOption_shouldReturnOk_whenNoExceptionIsThrown() throws Exception {
        String categoryName = "catName9";
        CategoryParameterOption categoryParameterOption = new CategoryParameterOption("id1", "name1",
            new CategoryParameter());

        prepareRequestExpectations(MockMvcRequestBuilders::delete, DELETE_CATEGORY_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOption))
            .andExpect(status().isOk());

        then(service).should(times(1))
            .deleteCategoryOption(eq(categoryName), argThat(requestMatcher(categoryParameterOption)));
    }

    @Test
    public void deleteCategoryOption_shouldReturnNotFound_whenUnfoundedExceptionIsThrown() throws Exception {
        String unfoundedMsg = "unfounded category exception message";
        String categoryName = "catName10";
        CategoryParameterOption categoryParameterOption = new CategoryParameterOption("id2", "name2",
            new CategoryParameter());

        willThrow(new CategoryParameterServiceImpl.UnfoundedCategoryException(unfoundedMsg))
            .given(service).deleteCategoryOption(eq(categoryName), argThat(requestMatcher(categoryParameterOption)));

        prepareRequestExpectations(MockMvcRequestBuilders::delete, DELETE_CATEGORY_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOption))
            .andExpect(status().isNotFound())
            .andExpect(content().json(
                objectMapper.writeValueAsString(new AddCategoryOptionResponse(ImmutableList.of(unfoundedMsg)))));
    }

    @Test
    public void deleteCategoryOption_shouldReturnInternalServerError_whenExceptionIsThrown() throws Exception {
        String categoryName = "catName19";
        CategoryParameterOption categoryParameterOption = new CategoryParameterOption("id3", "name3",
            new CategoryParameter());

        willThrow(new RuntimeException()).given(service)
            .deleteCategoryOption(eq(categoryName), argThat(requestMatcher(categoryParameterOption)));

        prepareRequestExpectations(MockMvcRequestBuilders::delete, DELETE_CATEGORY_PATH, categoryName,
            objectMapper.writeValueAsString(categoryParameterOption))
            .andExpect(status().isInternalServerError());
    }

    private <T> ArgumentMatcher<T> requestMatcher(T t) {
        return new ArgumentMatcher<T>() {
            @Override
            public boolean matches(Object o) {
                return t.equals(o);
            }
        };
    }

    private ResultActions prepareRequestExpectations(
        BiFunction<String, String, MockHttpServletRequestBuilder> httpMethod,
        String path, String name, String jsonContent) throws Exception {
        return mockMvc.perform(httpMethod.apply(path, name)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent));
    }
}