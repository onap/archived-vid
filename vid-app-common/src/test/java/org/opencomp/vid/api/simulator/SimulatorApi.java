package org.opencomp.vid.api.simulator;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
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
import java.net.URI;
import java.net.URL;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class SimulatorApi {

    public enum RegistrationStrategy {
        APPEND, CLEAR_THEN_SET
    }

    private static final URI uri;
    private static final Client client;
    private static boolean useSimulator;

    static {
        String host = System.getProperty("VID_HOST", "127.0.0.1" );
        Integer port = Integer.valueOf(System.getProperty("SIM_PORT", System.getProperty("VID_PORT", "8080")));
        uri = new JerseyUriBuilder().host(host).port(port).scheme("http").path("vidSimulator").build();
        client = ClientBuilder.newClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
    }

    public static void registerExpectation(String expectationFilename) {
        registerExpectation(expectationFilename, ImmutableMap.of(), RegistrationStrategy.CLEAR_THEN_SET);
    }

    public static void registerExpectation(String expectationFilename, RegistrationStrategy registrationStrategy) {
        registerExpectation(expectationFilename, ImmutableMap.of(), registrationStrategy);
    }

    public static void registerExpectation(String expectationTemplateFilename, ImmutableMap<String, Object> templateParams) {
        registerExpectation(expectationTemplateFilename, templateParams, RegistrationStrategy.CLEAR_THEN_SET);
    }

    public static void registerExpectation(String [] expectationTemplateFilenames, ImmutableMap<String, Object> templateParams) {
        clearExpectations();
        for(String expectationTemplateFilename: expectationTemplateFilenames) {
            registerExpectation(expectationTemplateFilename, templateParams, RegistrationStrategy.APPEND);
        }
    }

    public static void registerExpectation(String expectationTemplateFilename, ImmutableMap<String, Object> templateParams, RegistrationStrategy registrationStrategy) {

        String content;
        try {
            final URL resource = SimulatorApi.class.getClassLoader().getResource("registration_to_simulator/" + expectationTemplateFilename);
            if (resource == null) throw new RuntimeException("template file not found: " + "/registration_to_simulator/" + expectationTemplateFilename);
            content = IOUtils.toString(resource, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("couldn't read + expectationTemplateFilename", e);
        }

        for (Map.Entry<String, Object> templateParam : templateParams.entrySet()) {
            content = content.replaceAll(templateParam.getKey(), templateParam.getValue().toString());
        }

        registerToSimulatorAndAssertSuccess(content, registrationStrategy);
    }

    private static void registerToSimulatorAndAssertSuccess(String content, RegistrationStrategy registrationStrategy) {
        WebTarget webTarget = buildWebTarget();
        if (registrationStrategy == RegistrationStrategy.CLEAR_THEN_SET) {
            clearExpectations();
        }
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(content));
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    private static WebTarget buildWebTarget() {
        return client.target(uri).path("registerToVidSimulator");
    }

    public static void clearExpectations() {
        WebTarget webTarget = buildWebTarget();
        Response response = webTarget.request().delete();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    public static void setUseSimulator(boolean _useSimulator) {
        useSimulator = _useSimulator;
    }
}
