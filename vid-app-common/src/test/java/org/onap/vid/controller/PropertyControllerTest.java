/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter.Family;
import org.onap.vid.services.CategoryParameterService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class PropertyControllerTest {

	private static final String GET_PROPERTY = "/get_property/{name}/{defaultvalue}";
	private static final String CATEGORY_PARAMETER = "/category_parameter";

	private PropertyController propertyController;
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@Mock
	private CategoryParameterService service;
	@Mock
	private SystemPropertiesService systemPropertiesService;

	@Before
	public void setUp() {
		propertyController = new PropertyController(service, systemPropertiesService);
		BasicConfigurator.configure();
		mockMvc = MockMvcBuilders.standaloneSetup(propertyController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void getProperty_shouldReturnInputJson_whenPropertyIsNotFound() throws Exception {
		given(systemPropertiesService.getProperty("name.1")).willReturn(null);

		String inputJson = "{key1: val1}";
		mockMvc.perform(get(GET_PROPERTY, "name_1", inputJson)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(inputJson));
	}

	@Test
	public void getProperty_shouldReturnGivenJson_whenPropertyIsFound() throws Exception {
		String propertyJson = "{key1: val1}";
		given(systemPropertiesService.getProperty("name.1")).willReturn(propertyJson);

		String inputJson = "{key1: val1}";
		mockMvc.perform(get(GET_PROPERTY, "name_1", inputJson)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(propertyJson));
	}

	@Test
	public void getProperty_shouldReturnInternalServerError_whenExceptionIsThrown() throws Exception {
		String message = "Test exception message";
		given(systemPropertiesService.getProperty("name.1")).willThrow(new RuntimeException(message));

		mockMvc.perform(get(GET_PROPERTY, "name_1", "{key1: val1}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isInternalServerError())
			.andExpect(content().string("Internal error occurred: " + message));
	}

	@Test
	public void getCategoryParameter_should_when() throws Exception {

		CategoryParametersResponse categoryParametersResponse =
			new CategoryParametersResponse(
				ImmutableMap.of(
					"key1", ImmutableList.of(
						new CategoryParameterOptionRep("testId", "testName"))));

		given(service.getCategoryParameters(Family.PARAMETER_STANDARDIZATION)).willReturn(categoryParametersResponse);

		mockMvc.perform(get(CATEGORY_PARAMETER)
			.param("familyName", "PARAMETER_STANDARDIZATION")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(categoryParametersResponse)));
	}

	@Test
	public void getCategoryParameter_shouldReturnInternalServerError_whenExceptionIsThrown() throws Exception {
		String message = "Test exception message";
		given(service.getCategoryParameters(Family.PARAMETER_STANDARDIZATION)).willThrow(new RuntimeException(message));

		mockMvc.perform(get(CATEGORY_PARAMETER)
			.param("familyName", "PARAMETER_STANDARDIZATION")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isInternalServerError())
			.andExpect(content().string("Internal error occurred: " + message));
	}
}