package org.onap.vid.controllers;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HealthCheckControllerTest {

    private HealthCheckController testSubject;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        testSubject = new HealthCheckController();
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(testSubject).build();
    }

    @Test
    public void testGethealthCheckStatusforIDNS() throws Exception {
        mockMvc.perform(get("/healthCheck")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("health check succeeded"));
    }

    @Test
    public void testGetHealthCheck() throws Exception {
        mockMvc.perform(get("/rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}", "userAgent", "requestId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("health check succeeded"))
                .andExpect(jsonPath("$.date").isString());
    }

    @Test
    public void testCommitInfoEndpoint() throws Exception {
        mockMvc.perform(get("/version")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commitId").value("123"))
                .andExpect(jsonPath("$.commitMessageShort").value("Test short commit message"))
                .andExpect(jsonPath("$.commitTime").value("1999-09-12T13:48:55+0200"));
    }
}