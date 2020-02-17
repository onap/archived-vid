/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
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

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.services.AaiService;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class GetTenantsTest {
    @Mock private AaiService aaiService;
    @Mock private AAIRestInterface aaiRestInterface;
    @Mock private RoleProvider roleProvider;
    @Mock private SystemPropertiesWrapper systemPropertiesWrapper;
    @Mock private FeatureManager featureManager;
    @Mock private RoleValidator roleValidator;

    @InjectMocks
    AaiController aaiController;

    private MockMvc mockMvc;

    private final GetTenantsResponse oneTenant =
        TestUtils.setStringsInStringFields(new GetTenantsResponse());

    @BeforeMethod
    public void setUp() {
        TestUtils.initMockitoMocks(this);
        when(roleProvider.getUserRolesValidator(any())).thenReturn(roleValidator);
        mockMvc = MockMvcBuilders.standaloneSetup(aaiController).build();
    }

    @Test
    public void givenOneTenant_itIsReturned() throws Exception {
        AaiResponse<GetTenantsResponse[]> aaiResponse =
            new AaiResponse<>(array(oneTenant), null, 200);

        when(aaiService.getTenants("global-customer-id", "service-type", roleValidator))
            .thenReturn(aaiResponse);

        mockMvc.perform(get("/aai_get_tenants/global-customer-id/service-type"))
            .andExpect(content().string(jsonEquals(list(oneTenant))));
    }

}