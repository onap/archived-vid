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


import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import org.onap.vid.aai.model.RelationshipList;
import org.testng.annotations.Test;


public class OperationalEnvironmentTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String OPERATIONAL_ENVIRONMENT_JSON_DASHES = "{\n" +
        "\"operational-environment-id\": \"testEnvironmentId\",\n" +
        "\"operational-environment-name\": \"testEnvironmentName\",\n" +
        "\"operational-environment-type\": \"testEnvironmentType\",\n" +
        "\"operational-environment-status\": \"testEnvironmentStatus\",\n" +
        "\"tenant-context\": \"testTenantContext\",\n" +
        "\"workload-context\": \"testWorkloadContext\",\n" +
        "\"resource-version\": \"testResourceVersion\",\n" +
        "\"relationship-list\": {\n" +
        "\"relationship\": []\n" +
        "}\n" +
        "}";

    private static final String OPERATIONAL_ENVIRONMENT_JSON_CAMELCASE = "{\n" +
        "\"operationalEnvironmentId\": \"testEnvironmentId\",\n" +
        "\"operationalEnvironmentName\": \"testEnvironmentName\",\n" +
        "\"operationalEnvironmentType\": \"testEnvironmentType\",\n" +
        "\"operationalEnvironmentStatus\": \"testEnvironmentStatus\",\n" +
        "\"tenantContext\": \"testTenantContext\",\n" +
        "\"workloadContext\": \"testWorkloadContext\",\n" +
        "\"resourceVersion\": \"testResourceVersion\",\n" +
        "\"relationshipList\": {\n" +
        "\"relationship\": []\n" +
        "}\n" +
        "}";

    @Test
    public void shouldProperlyConvertJsonToOperationalEnvironment_whenJsonPropertyNamesContainDashSeparators()
        throws IOException {
        assertOperationalEnvironmentDeserialization(OPERATIONAL_ENVIRONMENT_JSON_DASHES);
    }

    @Test
    public void shouldProperlyConvertJsonToOperationalEnvironment_whenJsonPropertyNamesAreCamelCase()
        throws IOException {
        assertOperationalEnvironmentDeserialization(OPERATIONAL_ENVIRONMENT_JSON_CAMELCASE);
    }

    private void assertOperationalEnvironmentDeserialization(String operationalEnvironmentTestDashes)
        throws IOException {
        OperationalEnvironment operationalEnvironment =
            OBJECT_MAPPER.readValue(operationalEnvironmentTestDashes, OperationalEnvironment.class);

        assertThat(operationalEnvironment.getOperationalEnvironmentId()).isEqualTo("testEnvironmentId");
        assertThat(operationalEnvironment.getOperationalEnvironmentName()).isEqualTo("testEnvironmentName");
        assertThat(operationalEnvironment.getOperationalEnvironmentType()).isEqualTo("testEnvironmentType");
        assertThat(operationalEnvironment.getOperationalEnvironmentStatus()).isEqualTo("testEnvironmentStatus");
        assertThat(operationalEnvironment.getTenantContext()).isEqualTo("testTenantContext");
        assertThat(operationalEnvironment.getWorkloadContext()).isEqualTo("testWorkloadContext");
        assertThat(operationalEnvironment.getResourceVersion()).isEqualTo("testResourceVersion");
        assertThat(operationalEnvironment.getRelationshipList().getRelationship()).hasSize(0);
    }

    @Test
    public void shouldSerializeToJson_usingActualPropertyNameAsJsonLogicalProperty() throws JsonProcessingException {
        RelationshipList relationshipList = new RelationshipList();
        relationshipList.relationship = new ArrayList<>();

        OperationalEnvironment operationalEnvironment = createOperationalEnvironment(relationshipList);

        assertThat(OPERATIONAL_ENVIRONMENT_JSON_CAMELCASE)
            .isEqualToIgnoringWhitespace(OBJECT_MAPPER.writeValueAsString(operationalEnvironment));
    }

    private OperationalEnvironment createOperationalEnvironment(RelationshipList relationshipList) {
        return new OperationalEnvironment.OperationalEnvironmentBuilder()
            .withOperationalEnvironmentId("testEnvironmentId")
            .withOperationalEnvironmentName("testEnvironmentName")
            .withOperationalEnvironmentType("testEnvironmentType")
            .withOperationalEnvironmentStatus("testEnvironmentStatus")
            .withTenantContext("testTenantContext")
            .withWorkloadContext("testWorkloadContext")
            .withResourceVersion("testResourceVersion")
            .withRelationshipList(relationshipList)
            .build();
    }
}
