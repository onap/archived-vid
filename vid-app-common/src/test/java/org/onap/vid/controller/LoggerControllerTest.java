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

package org.onap.vid.controller;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.roles.EcompRole;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class LoggerControllerTest {

    private static final String ERROR_URL = "/logger/error";
    private static final String AUDIT_URL = "/logger/audit";
    private static final String METRICS_URL = "/logger/metrics";
    private static final String VALID_LOG_PATH = "src/test/resources/loggerFiles/validLog.log";
    private static final String EMPTY_LOG_PATH = "src/test/resources/loggerFiles/emptyLog.log";

    private MockMvc mockMvc;
    private LoggerController loggerController;

    @Mock
    private RoleProvider provider;
    @Mock
    private LogfilePathCreator creator;

    @Before
    public void setUp() {
        loggerController = new LoggerController(provider, creator);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(loggerController).build();
    }

    @Test
    public void shouldThrowNotAuthorizedException_whenUserIsNotAuthorizedToGetLogs() throws Exception {
        List<Role> list = ImmutableList.of(new Role(EcompRole.READ, "subName1", "servType1", "tenant1", ""));

        given(provider.getUserRoles(argThat(req -> req.getRequestedSessionId().equals("id1")))).willReturn(list);
        given(provider.userPermissionIsReadLogs(list)).willReturn(false);

        mockMvc.perform(get(ERROR_URL)
            .with(req -> {req.setRequestedSessionId("id1");
                            return req;}))
            .andExpect(content().string("UNAUTHORIZED"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnLastAndOneBeforeLogLines_whenLimitIs2() throws Exception {
        List<Role> list = ImmutableList.of(new Role(EcompRole.READ, "subName1", "servType1", "tenant1", ""));

        given(provider.getUserRoles(argThat(req -> req.getRequestedSessionId().equals("id1")))).willReturn(list);
        given(provider.userPermissionIsReadLogs(list)).willReturn(true);
        given(creator.getLogfilePath("audit")).willReturn(VALID_LOG_PATH);

        mockMvc.perform(get(AUDIT_URL)
            .with(req -> {req.setRequestedSessionId("id1");
                return req;})
            .param("limit", "2"))
            .andExpect(content().string("and the third line\nthe second line"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnEmptyString_whenLogFileIsEmpty() throws Exception {
        List<Role> list = ImmutableList.of(new Role(EcompRole.READ, "subName1", "servType1", "tenant1", ""));

        given(provider.getUserRoles(argThat(req -> req.getRequestedSessionId().equals("id1")))).willReturn(list);
        given(provider.userPermissionIsReadLogs(list)).willReturn(true);
        given(creator.getLogfilePath("metrics")).willReturn(EMPTY_LOG_PATH);

        mockMvc.perform(get(METRICS_URL)
            .with(req -> {req.setRequestedSessionId("id1");
                return req;}))
            .andExpect(content().string(""))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnEmptyString_whenDebugLogFileIsEmpty() throws Exception {
        List<Role> list = ImmutableList.of(new Role(EcompRole.READ, "subName1", "servType1", "tenant1", ""));

        given(provider.getUserRoles(argThat(req -> req.getRequestedSessionId().equals("id1")))).willReturn(list);
        given(provider.userPermissionIsReadLogs(list)).willReturn(true);
        given(creator.getLogfilePath("debug")).willReturn(EMPTY_LOG_PATH);

        mockMvc.perform(get("/logger/debug")
                .with(req -> {req.setRequestedSessionId("id1");
                    return req;}))
                .andExpect(content().string(""))
                .andExpect(status().isOk());
    }
}
