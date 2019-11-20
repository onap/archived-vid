/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

package org.onap.vid.aai;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.aai.model.RelationshipList;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


public class OperationalEnvironmentTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String OPERATIONAL_ENVIRONMENT_TEST = "{\n" +
            "\"operational-environment-id\": \"environmentId\",\n" +
            "\"operational-environment-name\": \"environmentName\",\n" +
            "\"operational-environment-type\": \"environmentType\",\n" +
            "\"operational-environment-status\": \"environmentStatus\",\n" +
            "\"tenant-context\": \"tenantContext\",\n" +
            "\"workload-context\": \"workloadContext\",\n" +
            "\"resource-version\": \"resourceVersion\",\n" +
            "\"relationship-list\": {\n" +
            "\"relationship\": []\n" +
            "}\n" +
            "}";

    @Test
    public void shouldCreateProperOperationalEnvironmentWithConstructor(){
        RelationshipList relationshipList = new RelationshipList();
        relationshipList.relationship = new ArrayList<>();

        OperationalEnvironment operationalEnvironment =
                new OperationalEnvironment.OperationalEnvironmentBuilder()
                        .withOperationalEnvironmentId("testId")
                        .withOperationalEnvironmentName("testEnvName")
                        .withOperationalEnvironmentType("testEnvType")
                        .withOperationalEnvironmentStatus("testEnvStatus")
                        .withTenantContext("testTenant").withWorkloadContext("testWorkload")
                        .withResourceVersion("testResource").withRelationshipList(relationshipList)
                        .build();

        assertThat(operationalEnvironment.getOperationalEnvironmentId()).isEqualTo("testId");
        assertThat(operationalEnvironment.getWorkloadContext()).isEqualTo("testWorkload");
        assertThat(operationalEnvironment.getRelationshipList().getRelationship()).hasSize(0);
        assertThat(operationalEnvironment.getResourceVersion()).isEqualTo("testResource");
        assertThat(operationalEnvironment.getTenantContext()).isEqualTo("testTenant");
        assertThat(operationalEnvironment.getOperationalEnvironmentType()).isEqualTo("testEnvType");
        assertThat(operationalEnvironment.getOperationalEnvironmentStatus()).isEqualTo("testEnvStatus");
        assertThat(operationalEnvironment.getOperationalEnvironmentName()).isEqualTo("testEnvName");
    }

    @Test
    public void shouldProperlyConvertJsonToOperationalEnvironment() throws IOException {
        OperationalEnvironment operationalEnvironment =
                OBJECT_MAPPER.readValue(OPERATIONAL_ENVIRONMENT_TEST, OperationalEnvironment.class);

        assertThat(operationalEnvironment.getOperationalEnvironmentId()).isEqualTo("environmentId");
        assertThat(operationalEnvironment.getWorkloadContext()).isEqualTo("workloadContext");
        assertThat(operationalEnvironment.getRelationshipList().getRelationship()).hasSize(0);
        assertThat(operationalEnvironment.getResourceVersion()).isEqualTo("resourceVersion");
        assertThat(operationalEnvironment.getTenantContext()).isEqualTo("tenantContext");
        assertThat(operationalEnvironment.getOperationalEnvironmentType()).isEqualTo("environmentType");
        assertThat(operationalEnvironment.getOperationalEnvironmentStatus()).isEqualTo("environmentStatus");
        assertThat(operationalEnvironment.getOperationalEnvironmentName()).isEqualTo("environmentName");
    }

}
