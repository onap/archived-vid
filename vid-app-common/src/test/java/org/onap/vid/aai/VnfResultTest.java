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

package org.onap.vid.aai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.aai.model.VnfResult;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class VnfResultTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String VNF_RESULT_JSON = "{\n" +
            "\"id\": \"sample\",\n" +
            "\"node-type\": \"sampleNodeType\",\n" +
            "\"url\": \"sample\",\n" +
            "\"properties\": {\n" +
            "\"service-instance-id\": \"sample\",\n" +
            "\"service-instance-name\": \"sample\",\n" +
            "\"model-invariant-id\": \"sample\",\n" +
            "\"model-version-id\": \"sample\",\n" +
            "\"resource-version\": \"sample\",\n" +
            "\"orchestration-status\": \"sample\",\n" +
            "\"global-customer-id\": \"sample\",\n" +
            "\"subscriber-name\": \"sample\",\n" +
            "\"subscriber-type\": \"sample\",\n" +
            "\"vnf-id\": \"sample\",\n" +
            "\"vnf-name\": \"sample\",\n" +
            "\"vnf-type\": \"sample\",\n" +
            "\"service-id\": \"sample\",\n" +
            "\"prov-status\": \"sample\",\n" +
            "\"in-maint\": false,\n" +
            "\"is-closed-loop-disabled\": false,\n" +
            "\"model-customization-id\": \"sample\",\n" +
            "\"nf-type\": \"sample\",\n" +
            "\"nf-function\": \"sample\",\n" +
            "\"nf-role\": \"sample\",\n" +
            "\"nf-naming-code\": \"sample\"\n" +
            "},\n" +
            "\"related-to\": [{\n" +
            "\"id\": \"sample\",\n" +
            "\"relationship-label\": \"sample\",\n" +
            "\"node-type\": \"sample\",\n" +
            "\"url\": \"sample\"\n" +
            "\t}]\n" +
            "}";


    @Test
    public void shouldProperlyConvertJsonToVnfResult() throws IOException {
        VnfResult vnfResult = OBJECT_MAPPER.readValue(VNF_RESULT_JSON, VnfResult.class);

        assertThat(vnfResult.nodeType).isEqualTo("sampleNodeType");
        assertThat(vnfResult.id).isEqualTo("sample");
        assertThat(vnfResult.url).isEqualTo("sample");
        assertThat(vnfResult.relatedTo).hasSize(1);
        assertThat(vnfResult.properties.globalCustomerId).isEqualTo("sample");
        assertThat(vnfResult.getAdditionalProperties()).isEmpty();
    }
}
