package org.onap.vid.services;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigData;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigDataError;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigDataOk;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class AaiResponseTranslatorTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void extractPortMirroringConfigData_givenValidAaiResponse_yieldCloudRegionId() throws IOException {

        final JsonNode aaiPayload = objectMapper.readTree("" +
                "{" +
                "  \"results\": [{" +
                "      \"id\": \"2979590232\"," +
                "      \"node-type\": \"cloud-region\"," +
                "      \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/SDNO-S-BcloudReg-E1802\"," +
                "      \"properties\": {" +
                "        \"cloud-owner\": \"att-aic\"," +
                "        \"cloud-region-id\": \"THE-EXPECTED-REGION-ID\"," +
                "        \"sriov-automation\": false," +
                "        \"resource-version\": \"1513631040564\"" +
                "      }" +
                "    }," +
                "    {" +
                "      \"id\": \"2979598424\"," +
                "      \"node-type\": \"generic-vnf\"," +
                "      \"url\": \"/aai/v12/network/generic-vnfs/generic-vnf/SOURCE-gVnf-E1802\"," +
                "      \"properties\": {" +
                "        \"vnf-id\": \"SOURCE-gVnf-E1802\"," +
                "        \"vnf-name\": \"SOURCE-vnf-SDNO\"," +
                "        \"vnf-type\": \"S-1-SDNO\"," +
                "        \"service-id\": \"a9a77d5a-123e-4-SDNO\"," +
                "        \"orchestration-status\": \"active\"," +
                "        \"in-maint\": true," +
                "        \"is-closed-loop-disabled\": false," +
                "        \"resource-version\": \"1513631043149\"" +
                "      }" +
                "    }" +
                "  ]" +
                "}");

        PortMirroringConfigData portMirroringConfigData =
                new AaiResponseTranslator().extractPortMirroringConfigData(aaiPayload);

        assertThat(portMirroringConfigData, is(instanceOf(PortMirroringConfigDataOk.class)));
        assertThat(((PortMirroringConfigDataOk) portMirroringConfigData).getCloudRegionId(), is("THE-EXPECTED-REGION-ID"));

    }

    @Test
    public void extractPortMirroringConfigData_givenKindOfValidAaiResponse_yieldCloudRegionId() throws IOException {
        // some completley different response, but with
        // the results[cloud-region]->properties->cloud-region-id

        final JsonNode aaiPayload = objectMapper.readTree("" +
                "{  " +
                "  \"results\": [{  " +
                "      \"node-type\": \"generic-vnf\",  " +
                "      \"url\": \"configuration entries) so that git\"  " +
                "    },  " +
                "    {},  " +
                "    {  " +
                "      \"node-type\": \"cloud-region\",  " +
                "      \"but it will not switch\": \"tip commits are reachable\",  " +
                "      \"named\": [{  " +
                "        \"resource-version\": \"1513631040564\"  " +
                "      }],  " +
                "      \"properties\": {  " +
                "        \"cloud-region-id\": \"THE-EXPECTED-REGION-ID\",  " +
                "        \"oldbranch> will be renamed\": false  " +
                "      }  " +
                "    },  " +
                "    {  " +
                "      \"node-type\": [\"generic-vnf\", \"can be overridden by using\"]  " +
                "    }  " +
                "  ]  " +
                "}");

        PortMirroringConfigData portMirroringConfigData =
                new AaiResponseTranslator().extractPortMirroringConfigData(aaiPayload);

        assertThat(portMirroringConfigData, is(instanceOf(PortMirroringConfigDataOk.class)));
        assertThat(((PortMirroringConfigDataOk) portMirroringConfigData).getCloudRegionId(), is("THE-EXPECTED-REGION-ID"));

    }

    public void extractPortMirroringConfigData_givenAaiResponseWithoutRegionIdName_yieldException() throws IOException {

        final JsonNode aaiPayload = objectMapper.readTree("" +
                "{" +
                "  \"results\": [{" +
                "      \"node-type\": \"cloud-region\"," +
                "      \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/att-aic/SDNO-S-BcloudReg-E1802\"," +
                "      \"properties\": {" +
                "        \"resource-version\": \"1513631040564\"" +
                "      }" +
                "    }" +
                "  ]" +
                "}");

        PortMirroringConfigData portMirroringConfigData =
                new AaiResponseTranslator().extractPortMirroringConfigData(aaiPayload);

        assertThat(portMirroringConfigData, is(instanceOf(PortMirroringConfigDataError.class)));
        assertThat(((PortMirroringConfigDataError) portMirroringConfigData).getErrorDescription(),
                containsString("The node-type 'cloud-region' does not contain the property 'cloud-region-id'"));
        assertThat(((PortMirroringConfigDataError) portMirroringConfigData).getRawAaiResponse(),
                containsString(aaiPayload.toString())
        );

    }

    /*
    More tests:
    [x]  cloud-region-id field is missing -- descriptive exception is thrown, including the problematic payload itself
    [ ]  cloud-region-id field is empty -- descriptive exception etc.
    [ ]  node-type=="cloud-region" entry is empty -- descriptive exception etc.
     */
}
