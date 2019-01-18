/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright 2019 Nokia
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

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.services.RoleGeneratorService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class RoleGeneratorControllerTest {

    private static final String FIRST_RUN = "/generateRoleScript/{firstRun}";

    private static final String FIRST_JSON = "{key1: val1}";
    private static final String SECOND_JSON = "{key2: val2}";

    private RoleGeneratorController roleGeneratorController;
    private MockMvc mockMvc;

    @Mock
    private RoleGeneratorService service;

    @Before
    public void setUp() {
        roleGeneratorController = new RoleGeneratorController(service);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(roleGeneratorController).build();

        given(service.generateRoleScript(true)).willReturn(FIRST_JSON);
        given(service.generateRoleScript(false)).willReturn(SECOND_JSON);
    }

    @Test
    public void generateRoleScript_shouldReturnJson_whenFirstRun() throws Exception {
        mockMvc.perform(get(FIRST_RUN, "true")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(FIRST_JSON));
    }

    @Test
    public void generateRoleScript_shouldReturnJson_whenNoFirstRun() throws Exception {
        mockMvc.perform(get(FIRST_RUN, "false")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(SECOND_JSON));
    }
}