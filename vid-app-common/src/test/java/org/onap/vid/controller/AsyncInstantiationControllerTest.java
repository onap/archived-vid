/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.onap.vid.controller.AsyncInstantiationController.ASYNC_INSTANTIATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.A;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.misc.BooleanRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.togglz.core.manager.FeatureManager;

public class AsyncInstantiationControllerTest {

    private static final long STATIC_SEED = 5336L;
    private final EasyRandomParameters parameters = new EasyRandomParameters()
        .randomize(Boolean.class, new BooleanRandomizer(STATIC_SEED))
        .randomize(String.class, new StringRandomizer(4, 4, STATIC_SEED))
        .excludeField(FieldPredicates.ofType(Serializable.class))
        .collectionSizeRange(1, 1);
    private final EasyRandom modelGenerator = new EasyRandom(parameters);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private AsyncInstantiationBusinessLogic instantiationBusinessLogic;
    private AsyncInstantiationRepository asyncInstantiationRepository;
    private SystemPropertiesWrapper propertiesWrapper;
    private AuditService auditService;

    @Before
    public void setUp() {
        instantiationBusinessLogic = mock(AsyncInstantiationBusinessLogic.class);
        RoleProvider roleProvider = mock(RoleProvider.class);
        FeatureManager featureManager = mock(FeatureManager.class);
        propertiesWrapper = mock(SystemPropertiesWrapper.class);
        auditService = mock(AuditService.class);
        asyncInstantiationRepository = mock(AsyncInstantiationRepository.class);
        AsyncInstantiationController asyncInstantiationController = new AsyncInstantiationController(
            instantiationBusinessLogic, asyncInstantiationRepository, roleProvider, featureManager, propertiesWrapper, auditService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(asyncInstantiationController).build();
    }

    @Test
    public void shouldReturnAllServiceInfos() throws Exception {
        List<ServiceInfo> serviceInfos = modelGenerator.objects(ServiceInfo.class, 3).collect(Collectors.toList());
        when(instantiationBusinessLogic.getAllServicesInfo()).thenReturn(serviceInfos);

        mockMvc.perform(get("/" + ASYNC_INSTANTIATION))
            .andExpect(content().json(asJson(serviceInfos)));

        verify(instantiationBusinessLogic).getAllServicesInfo();
        verifyNoMoreInteractions(instantiationBusinessLogic);
    }

    @Test
    public void shouldRetryJobsWithGivenUuid() throws Exception {
        when(propertiesWrapper.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("some user");

        List<UUID> expectedUuids = new ArrayList<>();
        expectedUuids.add(UUID.fromString("c195c600-a162-4655-9d88-d1a44518c4b5"));
        expectedUuids.add(UUID.fromString("1a7ee2b5-ac2b-4dc7-a2a6-22e5d3b33d79"));
        MsoResponseWrapper2<List<UUID>> expectedResponse = new MsoResponseWrapper2<>(200, expectedUuids);

        ServiceInstantiation serviceInstantiation = modelGenerator.nextObject(ServiceInstantiation.class);

        ArgumentCaptor<ServiceInstantiation> svcInstCaptor = ArgumentCaptor.forClass(ServiceInstantiation.class);
        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);

        when(instantiationBusinessLogic.retryJob(
            svcInstCaptor.capture(),
            uuidCaptor.capture(),
            any()
        )).thenReturn(expectedUuids);

        mockMvc.perform(
            post("/" + ASYNC_INSTANTIATION + "/retryJobWithChangedData/{jobId}",
                "804d26c3-fbe9-426c-8eff-25c6ab18fdcf")
                .content(asJson(serviceInstantiation))
                .contentType(APPLICATION_JSON))
            .andExpect(content().json(asJson(expectedResponse)));

        assertThat(svcInstCaptor.getValue().getInstanceId()).isEqualTo(serviceInstantiation.getInstanceId());
        assertThat(uuidCaptor.getValue()).isEqualTo(UUID.fromString("804d26c3-fbe9-426c-8eff-25c6ab18fdcf"));
    }

    @Test
    public void shouldDeleteJob() throws Exception {
        mockMvc.perform(
            delete("/" + ASYNC_INSTANTIATION + "/job/{jobId}", "804d26c3-fbe9-426c-8eff-25c6ab18fdcf"));

        verify(instantiationBusinessLogic).deleteJob(eq(UUID.fromString("804d26c3-fbe9-426c-8eff-25c6ab18fdcf")));
    }

    @Test
    public void shouldHideServiceJob() throws Exception {
        mockMvc.perform(
            post("/" + ASYNC_INSTANTIATION + "/hide/{jobId}", "804d26c3-fbe9-426c-8eff-25c6ab18fdcf"));

        verify(instantiationBusinessLogic).hideServiceInfo(eq(UUID.fromString("804d26c3-fbe9-426c-8eff-25c6ab18fdcf")));
    }

    @Test
    public void shouldRetryJob() throws Exception {
        when(propertiesWrapper.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("some user");

        List<UUID> expectedUuids = new ArrayList<>();
        expectedUuids.add(UUID.fromString("c195c600-a162-4655-9d88-d1a44518c4b5"));
        expectedUuids.add(UUID.fromString("1a7ee2b5-ac2b-4dc7-a2a6-22e5d3b33d79"));
        MsoResponseWrapper2<List<UUID>> expectedResponse = new MsoResponseWrapper2<>(200, expectedUuids);

        when(instantiationBusinessLogic.retryJob(eq(UUID.fromString("804d26c3-fbe9-426c-8eff-25c6ab18fdcf")), any()))
            .thenReturn(expectedUuids);

        mockMvc.perform(
            post("/" + ASYNC_INSTANTIATION + "/retry/{jobId}", "804d26c3-fbe9-426c-8eff-25c6ab18fdcf"))
            .andExpect(content().json(asJson(expectedResponse)));
    }

    private <T> String asJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}