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