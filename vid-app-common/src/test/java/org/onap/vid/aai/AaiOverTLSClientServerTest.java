/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia Intellectual Property. All rights reserved.
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

import static org.mockito.MockitoAnnotations.initMocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xebialabs.restito.semantics.Action;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.mapper.ObjectMapper;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mockito.Mock;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.testUtils.StubServerUtil;
import org.onap.vid.utils.Logging;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AaiOverTLSClientServerTest {

    @Mock
    private AaiOverTLSPropertySupplier propertySupplier;

    private static StubServerUtil serverUtil;

    private String searchNodesQueryResponsePayload = "" +
            "{" +
            "\"result-data\": [" +
            "    {" +
            "      \"resource-type\": \"generic-vnf\"," +
            "      \"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/6eac8e69-c98d-4ac5-ab90-69fe0cabda76\"" +
            "    }" +
            "  ]" +
            "}";

    private String subscribersResponsePayload =
        "{\n"
        + "\"customer\": [\n"
        + "  {\n"
        + "\"global-customer-id\": \"DemoCust_752df078-d8e9-4731-abf6-8ae7348075bb\",\n"
        + "\"subscriber-name\": \"DemoCust_752df078-d8e9-4731-abf6-8ae7348075bb\",\n"
        + "\"subscriber-type\": \"INFRA\",\n"
        + "\"resource-version\": \"1536158347585\"\n"
        + "},\n"
        + "  {\n"
        + "\"global-customer-id\": \"DemoCust_62bf43a3-4888-4c82-ae98-3ebc3d782761\",\n"
        + "\"subscriber-name\": \"DemoCust_62bf43a3-4888-4c82-ae98-3ebc3d782761\",\n"
        + "\"subscriber-type\": \"INFRA\",\n"
        + "\"resource-version\": \"1536240894581\"\n"
        + "},\n"
        + "  {\n"
        + "\"global-customer-id\": \"DemoCust_e84256d6-ef3e-4a28-9741-9987019c3a8f\",\n"
        + "\"subscriber-name\": \"DemoCust_e84256d6-ef3e-4a28-9741-9987019c3a8f\",\n"
        + "\"subscriber-type\": \"INFRA\",\n"
        + "\"resource-version\": \"1536330956393\"\n"
        + "},\n"
        + "  {\n"
        + "\"global-customer-id\": \"ETE_Customer_377bb124-2638-4025-a315-cdae04f52bce\",\n"
        + "\"subscriber-name\": \"ETE_Customer_377bb124-2638-4025-a315-cdae04f52bce\",\n"
        + "\"subscriber-type\": \"INFRA\",\n"
        + "\"resource-version\": \"1536088625538\"\n"
        + "}\n"
        + "],\n"
        + "}";

    @BeforeClass
    public static void setUpClass(){
        serverUtil = new StubServerUtil();
        serverUtil.runServer();
    }

    @AfterClass
    public static void tearDown(){
        serverUtil.stopServer();
    }

    @BeforeMethod
    public void setUp(){
        initMocks(this);
    }

    @Test
    public void shouldSearchNodeTypeByName() throws IOException, ParseException {
        AaiOverTLSClient aaiOverTLSClient = createAaiOverTLSClient();

        serverUtil.prepareGetCall("/nodes/generic-vnfs", new JSONParser().parse(searchNodesQueryResponsePayload), Action.status(HttpStatus.OK_200));

        boolean aaiNodeQueryResponseHttpResponse = aaiOverTLSClient
                .isNodeTypeExistsByName("any", ResourceType.GENERIC_VNF);

        Assertions.assertThat(aaiNodeQueryResponseHttpResponse).isEqualTo(true);
    }

    @NotNull
    private AaiOverTLSClient createAaiOverTLSClient() {
        return new AaiOverTLSClient(
            new SyncRestClient(getFasterXmlObjectMapper(), Logging.getRequestsLogger("aai")),
            propertySupplier,
            serverUtil.constructTargetUrl("http", "")
        );
    }

    @Test
    public void shouldGetSubscribers() throws ParseException, JsonProcessingException {
        ObjectMapper objectMapper = getFasterXmlObjectMapper();
        AaiOverTLSClient aaiOverTLSClient = createAaiOverTLSClient();

        serverUtil.prepareGetCall("/business/customers", new JSONParser().parse(subscribersResponsePayload), Action.status(HttpStatus.OK_200));

        HttpResponse<SubscriberList> allSubscribers = aaiOverTLSClient.getAllSubscribers();

        SubscriberList subscriberList = allSubscribers.getBody();
        Assertions.assertThat(subscriberList.customer.size()).isEqualTo(4);
        Assertions.assertThat(allSubscribers.getStatus()).isEqualTo(200);
    }

    private ObjectMapper getFasterXmlObjectMapper() {
        return new ObjectMapper() {

            com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();

            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                try {
                    return om.readValue(s, aClass);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object o) {
                try {
                    return om.writeValueAsString(o);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
