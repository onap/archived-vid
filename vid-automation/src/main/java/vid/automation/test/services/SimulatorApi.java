package vid.automation.test.services;

import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertEquals;
import static vid.automation.test.services.DropTestApiField.dropFieldCloudOwnerFromString;
import static vid.automation.test.services.DropTestApiField.dropTestApiFieldFromString;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.model.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import vid.automation.test.utils.ReadFile;

public class SimulatorApi {

    public enum RegistrationStrategy {
        APPEND, CLEAR_THEN_SET
    }

    private static final Logger logger = LoggerFactory.getLogger(SimulatorApi.class);

    /*
    these classes are partial representation of org.mockserver.model.HttpRequest.
    We can not use HttpRequest since it contains Map with NottableString and jackson throw the following error :
    com.fasterxml.jackson.databind.JsonMappingException: Can not find a (Map) Key deserializer for type
     [simple type, class org.mockserver.model.NottableString]
    */
    public static class StringWrapper {
        public String value;
    }

    public static class RecordedHeaders {
        public StringWrapper name;
        public List<StringWrapper> values;
    }

    public static class BodyWrapper {
        public String value;
    }

    public static class HttpRequest {
        public StringWrapper path;
        public BodyWrapper body;
        public List<RecordedHeaders> headers;
    }

    public static class RecordedRequests {
        public String path;
        public String body;
        public Map<String, List<String>> headers;

        public RecordedRequests(String path,  String body, Map<String, List<String>> headers) {
            this.path = path;
            this.body = body;
            this.headers = headers;
        }

        public RecordedRequests() {
        }
    }

    private static final URI uri; //uri for registration
    private static final URI simulationUri; //uri for getting simulated responses
    private static final Client client;

    private static final List<UnaryOperator<String>> presetStringPostProccessors =
            ImmutableList.of(dropTestApiFieldFromString(), dropFieldCloudOwnerFromString());

    static {
        String host = getSimulatorHost();
        Integer port = Integer.valueOf(System.getProperty("SIM_PORT", System.getProperty("VID_PORT", "8080"))); //port for registration
        uri = new JerseyUriBuilder().host(host).port(port).scheme("http").path("vidSimulator").build();
        client = ClientBuilder.newClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        //registering jacksonJsonProvider for avoiding exceptions like :
        // org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException:
        // MessageBodyWriter not found for media type=application/json
        JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        client.register(jacksonJsonProvider);

        Integer simulationPort = getSimulatedResponsesPort();
        simulationUri = new JerseyUriBuilder().host(host).port(simulationPort).scheme("http").build();
    }

    public static String getSimulatorHost() {
        return System.getProperty("SIM_HOST", System.getProperty("VID_HOST", "127.0.0.1"));
    }

    public static Integer getSimulatedResponsesPort() {
        return Integer.valueOf(System.getProperty("SIMULATION_PORT", "1080"));
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

        registerToSimulatorAndAssertSuccess(expectationTemplateFilename, content, registrationStrategy);

    }

    public static void registerExpectationFromPreset(BasePreset preset, RegistrationStrategy registrationStrategy) {
        RegistrationRequest content = preset.generateScenario();
        registerToSimulatorAndAssertSuccess(preset.getClass().getCanonicalName(), content, registrationStrategy);
    }

    public static void registerExpectationFromPresetsCollections(Collection<Collection<BasePreset>> presets, RegistrationStrategy registrationStrategy) {
        registerExpectationFromPresets(presets.stream()
                .flatMap(Collection::stream)
                .collect(toList()), registrationStrategy);
    }

    public static void registerExpectationFromPresets(Collection<BasePreset> presets, RegistrationStrategy registrationStrategy) {
        if (registrationStrategy == RegistrationStrategy.CLEAR_THEN_SET) {
            clearRegistrations();
        }
        presets.forEach(
                preset-> {
                    try {registerToSimulatorAndAssertSuccess(preset.getClass().getCanonicalName(), preset.generateScenario());}
                    catch (Throwable e) {
                        throw new RuntimeException("Failed to register preset "+preset.getClass().getName(), e);
                    }
                }
        );
    }

//    public static List<HttpRequest> retrieveRecordedRequests() {
//        Response response = client.target(uri).path("retrieveRecordedRequests").request().get();
//        return response.readEntity(new GenericType<List<HttpRequest>>(){});
//    }

    /*
        This method return counter of requests that has been sent to simulator.
        The key of the map is a path, and the value is counter
     */
    public static Map<String, Long> retrieveRecordedRequestsPathCounter() {
        List<HttpRequest> httpRequests =  retrieveRecordedHttpRequests();
        return httpRequests.stream().map(x->x.path.value).collect(
                Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static List<HttpRequest> retrieveRecordedHttpRequests() {
        Response response = client.target(uri).path("retrieveRecordedRequests").request().get();
        return response.readEntity(new GenericType<List<HttpRequest>>(){});
    }

    public static List<RecordedRequests> retrieveRecordedRequests() {
        List<HttpRequest> rawRequests =  retrieveRecordedHttpRequests();
        return rawRequests.stream().map(request->new RecordedRequests(
            request.path.value,
            request.body == null ? null : request.body.value,
            request.headers.stream().collect(
                Collectors.toMap(
                    x->x.name.value,
                    x->x.values.stream().map(y->y.value).collect(toList())))
        )).collect(toList());
    }

    private static void registerToSimulatorAndAssertSuccess(String name, Object content, RegistrationStrategy registrationStrategy) {
        if (registrationStrategy == RegistrationStrategy.CLEAR_THEN_SET) {
            clearRegistrations();
        }
        registerToSimulatorAndAssertSuccess(name, content);
    }

    private static void registerToSimulatorAndAssertSuccess(String name, Object content) {
        logger.info("Setting {}", name);

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
        logger.info("Clearing Registrations");
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
