package org.onap.vid.aai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xebialabs.restito.semantics.Action;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.mapper.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import org.apache.commons.io.IOUtils;
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
import org.onap.vid.testUtils.StubServerUtil;

@RunWith(MockitoJUnitRunner.class)
public class AaiOverTLSClientServerTest {

    @Mock
    private AaiOverTLSPropertySupplier propertySupplier;

    private static StubServerUtil serverUtil;

    private String responsePayload =
        "{\n"
            + "\"result-data\": [\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/6eac8e69-c98d-4ac5-ab90-69fe0cabda76\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/35305ca7-ad59-4b9b-9d21-1dd2b5103968\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/1ed7a7ef-e9b8-4fad-8e10-92d0f714acc6\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/5054bf7f-7913-4307-8bcd-aecce8b7539c\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/7b484da9-8ac1-406c-9c3f-ffcf0437047d\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/8e4ac9cb-c1d3-4a4b-9b1b-3bc60dc4c22d\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/b04f78e2-2f09-4dd5-bd9d-c6045036966f\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/e1e44c20-8803-416d-a4ba-c665de36f1aa\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/823f40cc-683a-4591-b82a-d6457a18e1bb\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/76f8282a-6099-4d2c-9f8b-b636ba486c23\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/c6039a4b-54e8-40a5-817d-84d8e87387e8\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/5ba68684-1c61-48b9-872f-de483fdc5cdb\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/b0400a1f-4dad-434a-bb36-ac13ef6afbc3\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/1142a64e-dd13-425e-a218-bf562365dfc9\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/98c34cbf-da33-4659-9797-4729d2a481df\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/b043e61c-a73a-446b-83ee-4751cac700e3\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/82ae1189-c4b9-45e4-870b-95160be90dae\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/75ddc2c9-d61b-49d9-9d57-22473fd0b7fe\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/b4443ed1-cfb5-4cc0-aa78-598942354285\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/c72519e2-3b76-494f-b742-524abd82b6d0\"\n"
            + "},\n"
            + "  {\n"
            + "\"resource-type\": \"generic-vnf\",\n"
            + "\"resource-link\": \"/aai/v13/network/generic-vnfs/generic-vnf/f6561cb3-7a23-44cc-98e3-3a6275e5f340\"\n"
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
        ObjectMapper objectMapper = new ObjectMapper() {

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
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        AaiOverTLSClient aaiOverTLSClient = new AaiOverTLSClient(new SyncRestClient(objectMapper),  propertySupplier, serverUtil.constructTargetUrl("http", ""));

        serverUtil.prepareGetCall("/search/nodes-query", new JSONParser().parse(responsePayload), Action.status(HttpStatus.OK_200));

        HttpResponse<AaiNodeQueryResponse> aaiNodeQueryResponseHttpResponse = aaiOverTLSClient
            .searchNodeTypeByName("any", ResourceType.GENERIC_VNF);

        AaiNodeQueryResponse body = aaiNodeQueryResponseHttpResponse.getBody();
        Assertions.assertThat(body.resultData.size()).isEqualTo(25);
        Assertions.assertThat(aaiNodeQueryResponseHttpResponse.getStatus()).isEqualTo(200);
    }



}
