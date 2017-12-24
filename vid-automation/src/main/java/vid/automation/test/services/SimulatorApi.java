package vid.automation.test.services;

import com.google.common.collect.ImmutableMap;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class SimulatorApi {


    private static final URI uri;
    private static final Client client;

    static {
        String host = System.getProperty("VID_HOST", "127.0.0.1" );
        Integer port = Integer.valueOf(System.getProperty("VID_PORT", "8080"));
        uri = new JerseyUriBuilder().host(host).port(port).scheme("http").path("vidSimulator").build();
        client = ClientBuilder.newClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
    }

    public static void registerExpectation(String expectationFilename) throws Exception {
        registerExpectation(expectationFilename, ImmutableMap.of());
    }

    public static void registerExpectation(String expectationTemplateFilename, ImmutableMap<String, Object> templateParams) throws Exception {
        ClassLoader cl = SimulatorApi.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources;
        try {
            resources = resolver.getResources("/registration_to_simulator/" + expectationTemplateFilename);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        String content;
        try {
            File file = resources[0].getFile();
            content = new Scanner(file).useDelimiter("\\Z").next();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        for (Map.Entry<String, Object> templateParam : templateParams.entrySet()) {
            content = content.replaceAll(templateParam.getKey(), templateParam.getValue().toString());
        }

        registerToSimulatorAndAssertSuccess(content);
    }

    private static void registerToSimulatorAndAssertSuccess(String content) throws IOException {
        WebTarget webTarget = client.target(uri).path("registerToVidSimulator");
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(content));
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
