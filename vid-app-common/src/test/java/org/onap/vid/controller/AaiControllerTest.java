/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia.
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
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Map;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigData;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigDataError;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigDataOk;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.AaiGetAicZone.Zone;
import org.onap.vid.aai.model.PortDetailsTranslator.PortDetails;
import org.onap.vid.aai.model.PortDetailsTranslator.PortDetailsError;
import org.onap.vid.aai.model.PortDetailsTranslator.PortDetailsOk;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class AaiControllerTest {

    private static final String ID_1 = "id1";
    private static final String ID_2 = "id2";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AaiService aaiService;
    @Mock
    private AAIRestInterface aaiRestInterface;
    @Mock
    private RoleProvider roleProvider;
    @Mock
    private SystemPropertiesWrapper systemPropertiesWrapper;
    private MockMvc mockMvc;
    private AaiController aaiController;

    @Before
    public void setUp() {
        aaiController = new AaiController(aaiService, aaiRestInterface, roleProvider, systemPropertiesWrapper);
        mockMvc = MockMvcBuilders.standaloneSetup(aaiController).build();
    }

    @Test
    public void getPortMirroringConfigData_givenIds_shouldReturnConfigDataMappedById() throws Exception {
        PortMirroringConfigDataOk okConfigData = new PortMirroringConfigDataOk("foo");
        PortMirroringConfigDataError errorConfigData = new PortMirroringConfigDataError("bar", "{ baz: qux }");
        Map<String, PortMirroringConfigData> expectedJson = ImmutableMap.of(
            ID_1, okConfigData,
            ID_2, errorConfigData);
        given(aaiService.getPortMirroringConfigData(ID_1)).willReturn(okConfigData);
        given(aaiService.getPortMirroringConfigData(ID_2)).willReturn(errorConfigData);

        mockMvc
            .perform(get("/aai_getPortMirroringConfigsData")
                .param("configurationIds", ID_1, ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJson)));
    }

    @Test
    public void getPortMirroringSourcePorts_givenIds_shouldReturnPortDetailsMappedById() throws Exception {
        PortDetailsOk portDetailsOk = new PortDetailsOk("foo", "testInterface", true);
        PortDetailsError portDetailsError = new PortDetailsError("bar", "{ baz: qux }");
        Multimap<String, PortDetails> expectedJson = ImmutableMultimap.of(
            ID_1, portDetailsOk,
            ID_2, portDetailsError);
        given(aaiService.getPortMirroringSourcePorts(ID_1)).willReturn(Lists.newArrayList(portDetailsOk));
        given(aaiService.getPortMirroringSourcePorts(ID_2)).willReturn(Lists.newArrayList(portDetailsError));

        mockMvc
            .perform(get("/aai_getPortMirroringSourcePorts")
                .param("configurationIds", ID_1, ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(expectedJson.asMap())));
    }

    @Test
    public void getNodeTemplateInstances_givenParams_shouldReturnCorrectResponse() throws Exception {
        String globalCustomerId = "testCustomerId";
        String serviceType = "testServiceType";
        String modelVersionId = UUID.nameUUIDFromBytes("modelVersionId".getBytes()).toString();
        String modelInvariantId = UUID.nameUUIDFromBytes("modelInvariantId".getBytes()).toString();
        String cloudRegion = "testRegion";
        String urlTemplate = "/aai_get_vnf_instances/{globalCustomerId}/{serviceType}/{modelVersionId}/{modelInvariantId}/{cloudRegion}";
        String expectedResponseBody = "myResponse";
        AaiResponse<String> aaiResponse = new AaiResponse<>(expectedResponseBody, "", HttpStatus.OK.value());
        given(aaiService
            .getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion))
            .willReturn(aaiResponse);

        mockMvc
            .perform(get(urlTemplate, globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
    }

    @Test
    public void getAicZones_shouldReturnAaiZones_whenAaiHttpStatusIsOK() throws Exception {
        AicZones aicZones = new AicZones(ImmutableList.of(new Zone("TEST_ZONE_ID", "TEST_ZONE_NAME")));
        given(aaiService.getAaiZones()).willReturn(new AaiResponse(aicZones, "", HttpStatus.OK.value()));

        mockMvc.perform(get("/aai_get_aic_zones")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(aicZones)));
    }

    @Test
    public void getAicZones_shouldReturnErrorResponse_whenAaiHttpStatusOtherThanOK() throws Exception {
        String expectedErrorMessage = "Calling AAI Failed";
        given(aaiService.getAaiZones())
            .willReturn(new AaiResponse(null, expectedErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));

        mockMvc.perform(get("/aai_get_aic_zones")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string(expectedErrorMessage));
    }
}

