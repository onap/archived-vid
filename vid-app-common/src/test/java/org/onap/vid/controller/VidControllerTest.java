/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Modifications Copyright 2018 - 2019 Nokia
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


import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.SecureServices;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.CR;
import org.onap.vid.model.Network;
import org.onap.vid.model.Node;
import org.onap.vid.model.PombaInstance.PombaRequest;
import org.onap.vid.model.PombaInstance.ServiceInstance;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.ServiceProxy;
import org.onap.vid.model.VNF;
import org.onap.vid.model.VfModule;
import org.onap.vid.model.VolumeGroup;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.InstantiationTemplatesService;
import org.onap.vid.services.PombaService;
import org.onap.vid.services.VidService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class VidControllerTest {

    public static final String REST_MODELS_SERVICES = "/rest/models/services";
    public static final String REST_MODELS_SERVICES_UUID = "/rest/models/services/{uuid}";
    public static final String REST_MODELS_RESET = "/rest/models/reset";
    public static final String REST_MODELS_SERVICES_VERIFY_SERVICE = "/rest/models/services/verifyService";
    @Mock
    private VidService vidService;
    @Mock
    private AaiService aaiService;
    @Mock
    private RoleProvider roleProvider;
    @Mock
    private PombaService pombaService;
    @Mock
    private InstantiationTemplatesService instantiationTemplatesService;

    private VidController vidController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private String uuid1;
    private String uuid2;
    private String uuid3;

    @Before
    public void setUp() {
        vidController = new VidController(vidService, aaiService, roleProvider, pombaService, instantiationTemplatesService);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(vidController).build();
        objectMapper = new ObjectMapper();

        uuid1 = UUID.randomUUID().toString();
        uuid2 = UUID.randomUUID().toString();
        uuid3 = UUID.randomUUID().toString();
    }

    @Test
    public void getServices_shouldReturnService_whenServiceExists() throws Exception {
        List<Service> services1 = ImmutableList.of(createService(uuid1, 1), createService(uuid2, 2), createService(uuid3, 3));
        List<Service> services2 = ImmutableList.of(createService(uuid1, 4), createService(uuid2, 5), createService(uuid3, 6));

        given(aaiService.getServicesByDistributionStatus())
            .willReturn(services1);

        given(instantiationTemplatesService.setOnEachServiceIsTemplateExists(services1))
            .willReturn(services2);

        SecureServices secureServices = new SecureServices();
        secureServices.setServices(services2);
        secureServices.setReadOnly(false);

        mockMvc.perform(get(REST_MODELS_SERVICES)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(secureServices)));
    }

    @Test
    public void getService_shouldReturnService_whenNoExceptionIsThrown() throws Exception {
        ServiceModel model = expectedServiceModel(uuid1);

        given(vidService.getService(uuid1)).willReturn(model);

        mockMvc.perform(get(REST_MODELS_SERVICES_UUID, uuid1)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(model)));
    }

    @Test
    public void getService_shouldThrow_whenAsdcCatalogExceptionIsThrown() throws Exception {
        String testUuid = UUID.randomUUID().toString();

        given(vidService.getService(testUuid)).willThrow(new AsdcCatalogException("error msg"));

        mockMvc.perform(get(REST_MODELS_SERVICES_UUID, testUuid)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void invalidateServiceModelCache_shouldReturnAccepted() throws Exception {
        mockMvc.perform(post(REST_MODELS_RESET)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isAccepted());

        then(vidService).should(times(1)).invalidateServiceCache();
    }

    @Test
    public void verifyServiceInstance_shouldReturnOk() throws Exception {
        PombaRequest pombaRequest = new PombaRequest(ImmutableList.of(new ServiceInstance()));

        mockMvc.perform(post(REST_MODELS_SERVICES_VERIFY_SERVICE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pombaRequest)))
            .andExpect(status().isOk());

        ArgumentCaptor<PombaRequest> argumentCaptor = ArgumentCaptor.forClass(PombaRequest.class);
        then(pombaService).should(times(1)).verify(argumentCaptor.capture());

        assertThat(pombaRequest).isEqualToComparingFieldByFieldRecursively(argumentCaptor.getValue());
    }

    private ServiceModel expectedServiceModel(String uuid) {
        final ServiceModel serviceModel = getModelsByUuid().get(uuid);
        Assert.assertThat(serviceModel, is(not(nullValue())));
        return serviceModel;
    }

    private Service createService(String uuid, int i) {
        return new Service.ServiceBuilder().setUuid(uuid).setInvariantUUID("invariantUUID" + i)
            .setCategory("category" + i).setVersion("version" + i).setName("name" + i)
            .setDistributionStatus("distStatus" + i).setToscaModelURL("toscaModelUrl" + i).build();
    }

    private ServiceModel createServiceModel(int i) {
        ServiceModel model = new ServiceModel();

        model.setCollectionResources(ImmutableMap.of("resKey" + i, new CR()));
        model.setNetworks(ImmutableMap.of("network" + i, new Network()));
        model.setPnfs(ImmutableMap.of("pnf" + i, new Node()));
        model.setServiceProxies(ImmutableMap.of("servProxy" + i, new ServiceProxy()));
        model.setVfModules(ImmutableMap.of("vfmod" + i, new VfModule()));
        model.setVnfs(ImmutableMap.of("vnf" + i, new VNF()));
        model.setVolumeGroups(ImmutableMap.of("volgroup" + i, new VolumeGroup()));
        model.setService(new org.onap.vid.model.Service());
        return model;
    }

    private Map<String, ServiceModel> getModelsByUuid() {
        ServiceModel serviceModel1 = createServiceModel(1);
        ServiceModel serviceModel2 = createServiceModel(2);
        ServiceModel serviceModel3 = createServiceModel(3);

        List<ServiceModel> pseudoServiceModels = ImmutableList.of(serviceModel1, serviceModel2, serviceModel3);
        List<String> uuids = ImmutableList.of(uuid1, uuid2, uuid3);
        return IntStream.range(0, pseudoServiceModels.size()).boxed()
            .collect(toMap(i -> uuids.get(i), i -> pseudoServiceModels.get(i)));
    }
}
