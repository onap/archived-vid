package vid.automation.test.services;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.springframework.http.HttpStatus;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class SimulatorApi {

    private static Logger logger = Logger.getLogger(SimulatorApi.class.getName());

    public enum RegistrationStrategy {
        APPEND, CLEAR_THEN_SET
    }

    private static final URI uri;
    private static final Client client;

    static {
        String host = System.getProperty("VID_HOST", "127.0.0.1" );
        Integer port = Integer.valueOf(System.getProperty("VID_PORT", "8080"));
        uri = new JerseyUriBuilder().host(host).port(port).scheme("http").path("vidSimulator").build();
        client = ClientBuilder.newClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
    }

    public static void registerExpectation(String expectationFilename) {
        registerExpectation(expectationFilename, ImmutableMap.<String, Object>of(), RegistrationStrategy.APPEND);
    }

    public static void registerExpectation(String expectationFilename, RegistrationStrategy registrationStrategy) {
        registerExpectation(expectationFilename, ImmutableMap.<String, Object>of(), registrationStrategy);
    }

    public static void registerExpectation(String expectationTemplateFilename, ImmutableMap<String, Object> templateParams) {
        registerExpectation(expectationTemplateFilename, templateParams, RegistrationStrategy.APPEND);
    }

    public static void registerExpectation(String... expectationTemplateFilenames) {
        registerExpectation(expectationTemplateFilenames, ImmutableMap.of());
    }

    public static void registerExpectation(String[] expectationTemplateFilenames, ImmutableMap<String, Object> templateParams) {
        for (String expectationTemplateFilename: expectationTemplateFilenames) {
            registerExpectation(expectationTemplateFilename, templateParams);
        }
    }

    public static void registerExpectation(String expectationTemplateFilename, ImmutableMap<String, Object> templateParams, RegistrationStrategy registrationStrategy) {

        try {
            final InputStream resource = SimulatorApi.class.getClassLoader().getResourceAsStream("registration_to_simulator/" + expectationTemplateFilename);
            if (resource == null) throw new RuntimeException("template file not found: " + "/registration_to_simulator/" + expectationTemplateFilename);
            String content = IOUtils.toString(resource, "UTF-8");

            for (Map.Entry<String, Object> templateParam : templateParams.entrySet()) {
                content = content.replaceAll(templateParam.getKey(), templateParam.getValue().toString());
            }

            registerToSimulatorAndAssertSuccess(content, registrationStrategy);

        } catch (IOException e) {
            logger.error("couldn't read " + expectationTemplateFilename, e);
//            throw new RuntimeException("couldn't read " + expectationTemplateFilename, e);
        }
    }

    private static void registerToSimulatorAndAssertSuccess(String content, RegistrationStrategy registrationStrategy) {
        WebTarget webTarget = client.target(uri).path("registerToVidSimulator");
        Response response;
        if (registrationStrategy == RegistrationStrategy.CLEAR_THEN_SET) {
            response = webTarget.request().delete();
            assertEquals(response.getStatus(), HttpStatus.OK.value());
        }
        response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(content));
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    public static void clearAll() {
        WebTarget webTarget = client.target(uri).path("registerToVidSimulator");
        webTarget.request().delete();
    }
}
