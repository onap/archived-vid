package org.onap.vid.controllers;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.CR;
import org.onap.vid.model.Network;
import org.onap.vid.model.Node;
import org.onap.vid.model.PortMirroringConfig;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.ServiceProxy;
import org.onap.vid.model.VNF;
import org.onap.vid.model.VfModule;
import org.onap.vid.model.VolumeGroup;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.PombaService;
import org.onap.vid.services.VidService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.ws.rs.core.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class VidControllerTest  {

    @Mock
    private VidService vidService;
    @Mock
    private AaiService aaiService;
    @Mock
    private RoleProvider roleProvider;
    @Mock
    private PombaService pombaService;

    private VidController vidController;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        vidController = new VidController(vidService, aaiService, roleProvider, pombaService);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(vidController).build();
    }

    @Test
    public void getServices_shouldReturnService_whenServiceExists() throws Exception {
        String testUuid = "9180d75b-3a24-42aa-a2e8-1eba1e107939";
        String testInvariantUuid = "587ac0a5-5271-4f8d-b761-fa459cd89c75";
        String testCategory = "category1";
        String testVersion = "1.0.0";
        String testName = "serviceName1";
        String testDistStatus = "distributionStatus1";
        String testToscaUrl = "model.url.1";

        Service testService = new Service(testUuid, testInvariantUuid, testCategory, testVersion,
                testName, testDistStatus, testToscaUrl, null, null, null);

        when(aaiService.getServicesByDistributionStatus()).thenReturn(ImmutableList.of(testService));

        mockMvc.perform(get("/rest/models/services")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.services").isArray())
                .andExpect(jsonPath("$.services[0].uuid").value(testUuid))
                .andExpect(jsonPath("$.services[0].invariantUUID").value(testInvariantUuid))
                .andExpect(jsonPath("$.services[0].name").value(testName))
                .andExpect(jsonPath("$.services[0].version").value(testVersion))
                .andExpect(jsonPath("$.services[0].toscaModelURL").value(testToscaUrl))
                .andExpect(jsonPath("$.services[0].category").value(testCategory))
                .andExpect(jsonPath("$.services[0].distributionStatus").value(testDistStatus));
    }

    @Test
    public void getService_shouldReturnService_whenNoException() throws Exception {
        String testUuid = "9180d75b-3a24-42aa-a2e8-1eba1e107939";

        String resKey = "resKey1";
        String confKey = "confKey1";
        String networkKey = "networkKey1";
        String pnfKey = "pnfKey1";
        String servProxyKey = "servProxyKey1";
        String vfModKey = "vfModKey1";
        String vnfKey = "vnfKey1";
        String volGroupKey = "volGroupKey1";

        ServiceModel model = createModel(resKey, confKey, networkKey, pnfKey, servProxyKey, vfModKey, vnfKey, volGroupKey);

        when(vidService.getService(testUuid)).thenReturn(model);

        mockMvc.perform(get("/rest/models/services/{uuid}", testUuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vnfs").isMap())
                .andExpect(jsonPath("$.vnfs."+ vnfKey).exists())
                .andExpect(jsonPath("$.networks").isMap())
                .andExpect(jsonPath("$.networks."+ networkKey).exists())
                .andExpect(jsonPath("$.collectionResource").isMap())
                .andExpect(jsonPath("$.collectionResource."+ resKey).exists())
                .andExpect(jsonPath("$.configurations").isMap())
                .andExpect(jsonPath("$.configurations."+ confKey).exists())
                .andExpect(jsonPath("$.serviceProxies").isMap())
                .andExpect(jsonPath("$.serviceProxies."+ servProxyKey).exists())
                .andExpect(jsonPath("$.vfModules").isMap())
                .andExpect(jsonPath("$.vfModules."+ vfModKey).exists())
                .andExpect(jsonPath("$.volumeGroups").isMap())
                .andExpect(jsonPath("$.volumeGroups."+ volGroupKey).exists())
                .andExpect(jsonPath("$.pnfs").isMap())
                .andExpect(jsonPath("$.pnfs."+ pnfKey).exists());
    }

    @Test
    public void getService_shouldThrow_whenAsdcCatalogExceptionIsThrown() throws Exception { //expect exception?
        String testUuid = "9180d75b-3a24-42aa-a2e8-1eba1e107939";

        when(vidService.getService(testUuid)).thenThrow(new AsdcCatalogException("error msg"));

        mockMvc.perform(get("/rest/models/services/{uuid}", testUuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void invalidateServiceModelCache_shouldReturnAccepted() throws Exception {
        mockMvc.perform(post("/rest/models/reset")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void verifyServiceInstance_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/rest/models/services/verifyService")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    private ServiceModel createModel(String resKey, String confKey, String networkKey, String pnfKey,
                                     String servProxyKey, String vfModKey, String vnfKey, String volGroupKey) {
        ServiceModel model = new ServiceModel();
        model.setCollectionResource(ImmutableMap.of(resKey, new CR()));
        model.setConfigurations(ImmutableMap.of(confKey, new PortMirroringConfig()));
        model.setNetworks(ImmutableMap.of(networkKey, new Network()));
        model.setPnfs(ImmutableMap.of(pnfKey, new Node()));
        model.setServiceProxies(ImmutableMap.of(servProxyKey, new ServiceProxy()));
        model.setVfModules(ImmutableMap.of(vfModKey, new VfModule()));
        model.setVnfs(ImmutableMap.of(vnfKey, new VNF()));
        model.setVolumeGroups(ImmutableMap.of(volGroupKey, new VolumeGroup()));
        model.setService(new org.onap.vid.model.Service());
        return model;
    }
}