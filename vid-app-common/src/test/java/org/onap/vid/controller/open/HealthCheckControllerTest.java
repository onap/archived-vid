/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.controller.open;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.controller.open.HealthCheckController;
import org.onap.vid.dao.FnAppDoaImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckControllerTest {

    private static final String ERROR_MESSAGE = "error message";
    private HealthCheckController testSubject;
    private MockMvc mockMvc;

    @Mock
    private FnAppDoaImpl fnAppDoa;

    @Before
    public void setUp() {
        testSubject = new HealthCheckController(fnAppDoa);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(testSubject).build();
    }

    @Test
    public void getHealthCheckStatusForIDNS_shouldReturnSuccess_whenNoExceptionIsThrown() throws Exception {
        databaseConnectionEstablished();
        mockMvc.perform(get("/healthCheck")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OK.value()))
                .andExpect(jsonPath("$.detailedMsg").value("health check succeeded"));
    }

    @Test
    public void getHealthCheckStatusForIDNS_shouldReturnErrorCode_whenExceptionIsThrown() throws Exception {
        databaseNotAccessible();
        mockMvc.perform(get("/healthCheck")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.detailedMsg").value("health check failed: " + ERROR_MESSAGE));
    }

    @Test
    public void getHealthCheck_shouldReturnSuccess_whenNoExceptionIsThrown() throws Exception {
        databaseConnectionEstablished();
        mockMvc.perform(get("/rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}", "userAgent", "requestId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OK.value()))
                .andExpect(jsonPath("$.detailedMsg").value("health check succeeded"))
                .andExpect(jsonPath("$.date").isString());
    }

    @Test
    public void getHealthCheck_shouldReturnErrorCode_whenExceptionIsThrown() throws Exception {
        databaseNotAccessible();
        mockMvc.perform(get("/rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}", "userAgent", "requestId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.detailedMsg").value("health check failed: " + ERROR_MESSAGE));
    }

    @Test
    public void getCommitInfo_shouldReturnCommitData_whenCorrectPropertiesFileExists() throws Exception {
        mockMvc.perform(get("/commitInfo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commitId").value("123"))
                .andExpect(jsonPath("$.commitMessageShort").value("Test short commit message"))
                .andExpect(jsonPath("$.commitTime").value("1999-09-12T13:48:55+0200"));
    }

    private void databaseConnectionEstablished() throws SQLException {
        given(fnAppDoa.getProfileCount(anyString(), anyString(), anyString()))
                .willReturn(0);
    }

    private void databaseNotAccessible() throws SQLException {
        given(fnAppDoa.getProfileCount(anyString(), anyString(), anyString()))
                .willThrow(new SQLException(ERROR_MESSAGE));
    }
}
