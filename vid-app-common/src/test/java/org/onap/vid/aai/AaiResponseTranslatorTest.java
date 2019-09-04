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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigData;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigDataError;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigDataOk;
import org.testng.annotations.Test;

public class AaiResponseTranslatorTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void extractPortMirroringConfigData_givenValidAaiResponse_yieldCloudRegionId() throws IOException {

        final JsonNode aaiPayload = objectMapper.readTree("" +
                "{" +
                "  \"results\": [{" +
                "      \"id\": \"2979590232\"," +
                "      \"node-type\": \"cloud-region\"," +
                "      \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/SDNO-S-BcloudReg-E1802\"," +
                "      \"properties\": {" +
                "        \"cloud-owner\": \"irma-aic\"," +
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

    @Test
    public void extractPortMirroringConfigData_givenAaiResponseWithoutRegionIdName_yieldException() throws IOException {

        final JsonNode aaiPayload = objectMapper.readTree("" +
                "{" +
                "  \"results\": [{" +
                "      \"node-type\": \"cloud-region\"," +
                "      \"url\": \"/aai/v12/cloud-infrastructure/cloud-regions/cloud-region/irma-aic/SDNO-S-BcloudReg-E1802\"," +
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
