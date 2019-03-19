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

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.aai.model.ServiceProperties;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServicePropertiesTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String SERVICE_PROPERTIES_JSON = "{\n" +
            "\"service-instance-id\": \"instanceId\",\n" +
            "\"service-instance-name\": \"instanceName\",\n" +
            "\"model-invariant-id\": \"invariantId\",\n" +
            "\"model-version-id\": \"versionId\",\n" +
            "\"resource-version\": \"version\",\n" +
            "\"orchestration-status\": \"status\",\n" +
            "\"global-customer-id\": \"customerId\",\n" +
            "\"subscriber-name\": \"subscriberName\",\n" +
            "\"subscriber-type\": \"subscriberType\",\n" +
            "\"vnf-id\": \"vnfId\",\n" +
            "\"vnf-name\": \"vnfName\",\n" +
            "\"vnf-type\": \"vnfType\",\n" +
            "\"service-id\": \"serviceId\",\n" +
            "\"prov-status\": \"provStatus\",\n" +
            "\"in-maint\": false,\n" +
            "\"is-closed-loop-disabled\": false,\n" +
            "\"model-customization-id\": \"customizationId\",\n" +
            "\"nf-type\": \"nfType\",\n" +
            "\"nf-function\": \"nfFunction\",\n" +
            "\"nf-role\": \"nfRole\",\n" +
            "\"nf-naming-code\": \"namingCode\",\n" +
            "\"not-listed-property\":\"value\"}"+
            "}";


    @Test
    public void shouldProperlyConvertJsonToServiceProperties() throws IOException {
        ServiceProperties serviceProperties = OBJECT_MAPPER.readValue(SERVICE_PROPERTIES_JSON, ServiceProperties.class);


        assertThat(serviceProperties.isClosedLoopDisabled).isFalse();
        assertThat(serviceProperties.globalCustomerId).isEqualTo("customerId");
        assertThat(serviceProperties.inMaint).isFalse();
        assertThat(serviceProperties.modelCustomizationId).isEqualTo("customizationId");
        assertThat(serviceProperties.modelInvariantId).isEqualTo("invariantId");
        assertThat(serviceProperties.modelVersionId).isEqualTo("versionId");
        assertThat(serviceProperties.nfRole).isEqualTo("nfRole");
        assertThat(serviceProperties.nfFunction).isEqualTo("nfFunction");
        assertThat(serviceProperties.nfType).isEqualTo("nfType");
        assertThat(serviceProperties.nfNamingCode).isEqualTo("namingCode");
        assertThat(serviceProperties.serviceId).isEqualTo("serviceId");
        assertThat(serviceProperties.serviceInstanceName).isEqualTo("instanceName");
        assertThat(serviceProperties.serviceInstanceId).isEqualTo("instanceId");
        assertThat(serviceProperties.provStatus).isEqualTo("provStatus");
        assertThat(serviceProperties.vnfId).isEqualTo("vnfId");
        assertThat(serviceProperties.vnfName).isEqualTo("vnfName");
        assertThat(serviceProperties.vnfType).isEqualTo("vnfType");
    }

    @Test
    public void shouldProperlyAddAdditionalProperty() throws IOException {
        ServiceProperties serviceProperties = OBJECT_MAPPER.readValue(SERVICE_PROPERTIES_JSON, ServiceProperties.class);


        serviceProperties.setAdditionalProperty("additional", "property");

        assertThat(serviceProperties.getAdditionalProperties())
                .containsOnlyKeys("not-listed-property","additional")
                .containsValues("value","property");
    }
}
