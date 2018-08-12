package vid.automation.test.services;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.opencomp.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.opencomp.simulator.presetGenerator.presets.model.RegistrationRequest;
import org.springframework.http.HttpStatus;
import vid.automation.test.utils.ReadFile;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static org.testng.Assert.assertEquals;
import static vid.automation.test.services.DropTestApiField.dropTestApiFieldFromString;

public class SimulatorApi {

    public enum RegistrationStrategy {
        APPEND, CLEAR_THEN_SET
    }

    private static final URI uri; //uri for registration
    private static final URI simulationUri; //uri for getting simulated responses
    private static final Client client;

    private static final List<UnaryOperator<String>> presetStringPostProccessors =
            ImmutableList.of(dropTestApiFieldFromString());

    static {
        String host = System.getProperty("VID_HOST", "127.0.0.1" );
        Integer port = Integer.valueOf(System.getProperty("SIM_PORT", System.getProperty("VID_PORT", "8080"))); //port for registration
        uri = new JerseyUriBuilder().host(host).port(port).scheme("http").path("vidSimulator").build();
        client = ClientBuilder.newClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        //registering jacksonJsonProvider for avoiding exceptions like :
        // org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException:
        // MessageBodyWriter not found for media type=application/json
        JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider();
        client.register(jacksonJsonProvider);

        Integer simulationPort = Integer.valueOf(System.getProperty("SIMULATION_PORT", "1080")); //port getting simulated responses
        simulationUri = new JerseyUriBuilder().host(host).port(simulationPort).scheme("http").build();
    }

    public static URI getSimulationUri() {
        return simulationUri;
    }

    public static void registerExpectation(String expectationFilename, RegistrationStrategy registrationStrategy) {
        registerExpectation(expectationFilename, ImmutableMap.<String, Object>of(), registrationStrategy);
    }

    public static void registerExpectation(RegistrationStrategy strategy, String... expectationTemplateFilenames) {
        registerExpectation(expectationTemplateFilenames, ImmutableMap.of(), strategy);
    }

    public static void registerExpectation(String[] expectationTemplateFilenames, ImmutableMap<String, Object> templateParams, RegistrationStrategy strategy) {
        if (strategy.equals(RegistrationStrategy.CLEAR_THEN_SET)) {
            clearRegistrations();
        }
        for (String expectationTemplateFilename: expectationTemplateFilenames) {
            registerExpectation(expectationTemplateFilename, templateParams, RegistrationStrategy.APPEND);
        }
    }

    public static void registerExpectation(String expectationTemplateFilename, ImmutableMap<String, Object> templateParams, RegistrationStrategy registrationStrategy) {

        String content = ReadFile.loadResourceAsString("registration_to_simulator/" + expectationTemplateFilename);

        for (Map.Entry<String, Object> templateParam : templateParams.entrySet()) {
            content = content.replaceAll(templateParam.getKey(), templateParam.getValue().toString());
        }

        registerToSimulatorAndAssertSuccess(content, registrationStrategy);

    }

    public static void registerExpectationFromPreset(BasePreset preset, RegistrationStrategy registrationStrategy) {
        RegistrationRequest content = preset.generateScenario();
        registerToSimulatorAndAssertSuccess(content, registrationStrategy);
    }

    public static void registerExpectationFromPresets(Collection<BasePreset> presets, RegistrationStrategy registrationStrategy) {
        if (registrationStrategy == RegistrationStrategy.CLEAR_THEN_SET) {
            clearRegistrations();
        }
        presets.forEach(
                preset-> {
                    try {registerToSimulatorAndAssertSuccess(preset.generateScenario());}
                    catch (RuntimeException e) {
                        throw new RuntimeException("Failed to register preset "+preset.getClass().getName(), e);
                    }
                }
        );
    }

    private static void registerToSimulatorAndAssertSuccess(Object content, RegistrationStrategy registrationStrategy) {
        if (registrationStrategy == RegistrationStrategy.CLEAR_THEN_SET) {
            clearRegistrations();
        }
        registerToSimulatorAndAssertSuccess(content);
    }

    private static void registerToSimulatorAndAssertSuccess(Object content) {

        content = postProccessContent(content);

        Response response = createSimulatorRegistrationWebTarget().request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(content));
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    private static Object postProccessContent(Object content) {
        if (content instanceof String) {
            for (UnaryOperator<String> presetStringPostProccessor : presetStringPostProccessors) {
                content = presetStringPostProccessor.apply((String) content);
            }
        }

        return content;
    }

    public static void clearExpectations() {
        clearRegistrations();
    }

    private static void clearRegistrations() {
        Response response = createSimulatorRegistrationWebTarget().request().delete();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    private static WebTarget createSimulatorRegistrationWebTarget() {
        return client.target(uri).path("registerToVidSimulator");
    }

    public static void clearAll() {
        WebTarget webTarget = createSimulatorRegistrationWebTarget();
        webTarget.request().delete();
    }
}
