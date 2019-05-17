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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.misc.BooleanRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.Task;
import org.onap.vid.services.CloudOwnerService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class MsoControllerTest {

    private static final long STATIC_SEED = 5336L;
    private EasyRandomParameters parameters = new EasyRandomParameters()
        .randomize(Boolean.class, new BooleanRandomizer(STATIC_SEED))
        .randomize(String.class, new StringRandomizer(4, 4, STATIC_SEED))
        .collectionSizeRange(2, 3);
    private EasyRandom modelGenerator = new EasyRandom(parameters);
    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MsoBusinessLogic msoBusinessLogic;
    private CloudOwnerService cloudService;

    @Before
    public void setUp() {
        msoBusinessLogic = mock(MsoBusinessLogic.class);
        cloudService = mock(CloudOwnerService.class);
        MsoController msoController = new MsoController(msoBusinessLogic, cloudService);

        mockMvc = MockMvcBuilders.standaloneSetup(msoController).build();
    }

    @Test
    public void shouldDelegateNewInstanceCreation() throws Exception {
        // given
        RequestDetails requestDetails = modelGenerator.nextObject(RequestDetails.class);
        String payload = objectMapper.writeValueAsString(requestDetails);

        given(msoBusinessLogic.createSvcInstance(any())).willReturn(new MsoResponseWrapper(200, "test"));

        // when & then
        mockMvc.perform(post("/mso/mso_create_svc_instance")
            .content(payload)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{ \"status\": 200, \"entity\": test}"));

        ArgumentCaptor<RequestDetails> argCaptor = ArgumentCaptor.forClass(RequestDetails.class);
        verify(msoBusinessLogic).createSvcInstance(argCaptor.capture());
        verify(cloudService).enrichRequestWithCloudOwner(argCaptor.capture());

        for (RequestDetails captured : argCaptor.getAllValues()) {
            assertThat(objectMapper.writeValueAsString(captured)).isEqualTo(payload);
        }
    }

    @Test
    public void shouldReturnOrchestrationRequestsForDashboard() throws Exception {
        // given
        List<Request> orchestrationRequests = modelGenerator
            .objects(Request.class, 2)
            .collect(Collectors.toList());

        given(msoBusinessLogic.getOrchestrationRequestsForDashboard()).willReturn(orchestrationRequests);

        // when & then
        mockMvc.perform(get("/mso/mso_get_orch_reqs/dashboard"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(orchestrationRequests)));

        verify(msoBusinessLogic).getOrchestrationRequestsForDashboard();
    }

    @Test
    public void shouldReturnManualTasksById() throws Exception {
        // given
        List<Task> manualTasks = modelGenerator
            .objects(Task.class, 2)
            .collect(Collectors.toList());

        String originalRequestId = "za1234d1-5a33-55df-13ab-12abad84e335";
        given(msoBusinessLogic.getManualTasksByRequestId(originalRequestId)).willReturn(manualTasks);

        // when & then
        mockMvc.perform(get("/mso/mso_get_man_task/" + originalRequestId))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(manualTasks)));

        verify(msoBusinessLogic).getManualTasksByRequestId(originalRequestId);
    }
}
