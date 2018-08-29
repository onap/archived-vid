package org.onap.vid.aai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xebialabs.restito.semantics.Action;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.mapper.ObjectMapper;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.testUtils.StubServerUtil;

@RunWith(MockitoJUnitRunner.class)
public class AaiOverTLSClientServerTest {

    @Mock
    private AaiOverTLSPropertySupplier propertySupplier;

    private static StubServerUtil serverUtil;

    private String searchNodesQueryResponsePayload =
        "{\n"
            + "\"result-data\": [\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/6eac8e69-c98d-4ac5-ab90-69fe0cabda76\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/e3766bc5-40a7-4dbe-9d4a-1d8c8f284913\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/6aa153ee-6637-4b49-beb5-a5e756e00393\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/5a981c30-de25-4ea9-98fa-ed398f13ea41\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/b0ef2271-8ac0-4268-b9a5-09cb50c20c85\"\n"
            + "}\n"
            + "],\n"
            + "}";

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

    @Test
    public void shouldSearchNodeTypeByName() throws IOException, ParseException {
        ObjectMapper objectMapper = getFasterXmlObjectMapper();
        AaiOverTLSClient aaiOverTLSClient = new AaiOverTLSClient(new SyncRestClient(objectMapper),  propertySupplier, serverUtil.constructTargetUrl("http", ""));

        serverUtil.prepareGetCall("/search/nodes-query", new JSONParser().parse(searchNodesQueryResponsePayload), Action.status(HttpStatus.OK_200));

        HttpResponse<AaiNodeQueryResponse> aaiNodeQueryResponseHttpResponse = aaiOverTLSClient
            .searchNodeTypeByName("any", ResourceType.GENERIC_VNF);

        AaiNodeQueryResponse body = aaiNodeQueryResponseHttpResponse.getBody();
        Assertions.assertThat(body.resultData.size()).isEqualTo(5);
        Assertions.assertThat(aaiNodeQueryResponseHttpResponse.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldGetSubscribers() throws ParseException, JsonProcessingException {
        ObjectMapper objectMapper = getCodehausObjectMapper();
        AaiOverTLSClient aaiOverTLSClient = new AaiOverTLSClient(new SyncRestClient(objectMapper),  propertySupplier, serverUtil.constructTargetUrl("http", ""));

        serverUtil.prepareGetCall("/business/customers", new JSONParser().parse(subscribersResponsePayload), Action.status(HttpStatus.OK_200));

        HttpResponse<SubscriberList> allSubscribers = aaiOverTLSClient.getAllSubscribers();

        SubscriberList subscriberList = allSubscribers.getBody();
        Assertions.assertThat(subscriberList.customer.size()).isEqualTo(4);
        Assertions.assertThat(allSubscribers.getStatus()).isEqualTo(200);
    }

    private ObjectMapper getCodehausObjectMapper() {
        return new ObjectMapper() {

            org.codehaus.jackson.map.ObjectMapper om = new org.codehaus.jackson.map.ObjectMapper();

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
