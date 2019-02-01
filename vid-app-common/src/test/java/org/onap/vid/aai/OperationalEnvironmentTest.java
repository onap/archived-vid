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
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class OperationalEnvironmentTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String OPERATIONAL_ENVIRONMENT_TEST = "{\n" +
            "\"operational-environment-id\": \"sample\",\n" +
            "\"operational-environment-name\": \"sample\",\n" +
            "\"operational-environment-type\": \"sample\",\n" +
            "\"operational-environment-status\": \"sample\",\n" +
            "\"tenant-context\": \"sample\",\n" +
            "\"workload-context\": \"sample\",\n" +
            "\"resource-version\": \"sample\",\n" +
            "\"relationship-list\": {\n" +
            "\"relationship\": []\n" +
            "}\n" +
            "}";

    @Test
    public void shouldProperlyConvertJsonToOperationalEnvironment() throws IOException {
        OperationalEnvironment operationalEnvironment = OBJECT_MAPPER.readValue(OPERATIONAL_ENVIRONMENT_TEST, OperationalEnvironment.class);

        assertThat(operationalEnvironment.getOperationalEnvironmentId()).isEqualTo("sample");
        assertThat(operationalEnvironment.getWorkloadContext()).isEqualTo("sample");
        assertThat(operationalEnvironment.getRelationshipList().getRelationship()).hasSize(0);
        assertThat(operationalEnvironment.getResourceVersion()).isEqualTo("sample");
        assertThat(operationalEnvironment.getWorkloadContext()).isEqualTo("sample");
        assertThat(operationalEnvironment.getTenantContext()).isEqualTo("sample");
        assertThat(operationalEnvironment.getOperationalEnvironmentType()).isEqualTo("sample");
        assertThat(operationalEnvironment.getOperationalEnvironmentStatus()).isEqualTo("sample");
        assertThat(operationalEnvironment.getOperationalEnvironmentName()).isEqualTo("sample");
    }
}
