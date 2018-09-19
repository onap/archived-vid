package org.onap.vid.controllers;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.dao.FnAppDoaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckControllerTest {

    private HealthCheckController testSubject;
    private MockMvc mockMvc;
    private static final String ERROR_MESSAGE = "error message";

    @Mock
    private FnAppDoaImpl fnAppDoa;

    @Before
    public void setUp() {
        testSubject = new HealthCheckController(fnAppDoa);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(testSubject).build();
    }

    private void okCaseSetUp() throws SQLException {
        given(fnAppDoa.getProfileCount(anyString(), anyString(), anyString()))
                .willReturn(0);
    }

    private void nokCaseSetUp() throws SQLException {
        given(fnAppDoa.getProfileCount(anyString(), anyString(), anyString()))
                .willThrow(new SQLException(ERROR_MESSAGE));
    }

    @Test
    public void getHealthCheckStatusForIDNS_shouldReturnSuccess_whenNoExceptionIsThrown() throws Exception {
        okCaseSetUp();
        mockMvc.perform(get("/healthCheck")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.detailedMsg").value("health check succeeded"));
    }

    @Test
    public void getHealthCheckStatusForIDNS_shouldReturnErrorCode_whenExceptionIsThrown() throws Exception {
        nokCaseSetUp();
        mockMvc.perform(get("/healthCheck")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.detailedMsg").value("health check failed: " + ERROR_MESSAGE));
    }

    @Test
    public void getHealthCheck_shouldReturnSuccess_whenNoExceptionIsThrown() throws Exception {
        okCaseSetUp();
        mockMvc.perform(get("/rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}", "userAgent", "requestId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.detailedMsg").value("health check succeeded"))
                .andExpect(jsonPath("$.date").isString());
    }

    @Test
    public void getHealthCheck_shouldReturnErrorCode_whenExceptionIsThrown() throws Exception {
        nokCaseSetUp();
        mockMvc.perform(get("/rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}", "userAgent", "requestId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.detailedMsg").value("health check failed: " + ERROR_MESSAGE));
    }

    @Test
    public void getCommitInfo_shouldReturnCommitData_whenCorrectPropertiesFileExists() throws Exception {
        mockMvc.perform(get("/version")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commitId").value("123"))
                .andExpect(jsonPath("$.commitMessageShort").value("Test short commit message"))
                .andExpect(jsonPath("$.commitTime").value("1999-09-12T13:48:55+0200"));
    }
}