package org.onap.simulator.controller;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.JsonBody.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.onap.simulator.db.entities.Function;
import org.onap.simulator.db.entities.User;
import org.onap.simulator.errorHandling.VidSimulatorException;
import org.onap.simulator.model.SimulatorRequestResponseExpectation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;

@RestController
@Component
public class SimulatorController {

    private static final Times DEFAULT_NUMBER_OF_TIMES = Times.unlimited();
    private ClientAndServer mockServer;
    private String mockServerProtocol;
    private String mockServerHost;
    private Integer mockServerPort;
    private Boolean enablePresetRegistration;
    private Boolean enableJPA;
    private volatile boolean isInitialized = false;

    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;


    private static final Logger logger = LoggerFactory.getLogger(SimulatorController.class);

    @PostConstruct
    public void init(){
        logger.info("Starting VID Simulator....");
        setProperties();
        mockServer = startClientAndServer(mockServerPort);
        presetRegister();
        try {
            initJPA();
        } catch (RuntimeException e) {
            isInitialized = false;
            logger.error("Error during the JPA initialization:", e);
            return;
        }
        isInitialized = true;
        logger.info("VID Simulator started successfully");
    }

    private void initJPA() {
        if (enableJPA) {
            entityManagerFactory = Persistence.createEntityManagerFactory("vid", overrideConnectionUrl());
            entityManager = entityManagerFactory.createEntityManager();
        }
    }

    private Map<Object, Object> overrideConnectionUrl() {
        final String connectionUrlEnvProperty = "hibernate.connection.url";
        if (isEmpty(System.getProperty(connectionUrlEnvProperty))) {
            return Collections.emptyMap();
        } else {
            return ImmutableMap.of(connectionUrlEnvProperty, System.getProperty(connectionUrlEnvProperty));
        }
    }

    @PreDestroy
    public void tearDown(){
        logger.info("Stopping VID Simulator....");
        entityManager.close();
        entityManagerFactory.close();
        isInitialized = false;
        mockServer.stop(false);
    }


    private void presetRegister() {
        //Checking if set
        if (enablePresetRegistration == null || !enablePresetRegistration){
            logger.info("Preset registration property is false or not set - skipping preset registration...");
            return;
        }
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        List<Path> resources = new ArrayList<>();
        try {
            File presetDir = resolver.getResource("/preset_registration/").getFile();
            if (presetDir.exists() && presetDir.isDirectory()) {
                try (Stream<Path> files = Files.walk(Paths.get(presetDir.getPath()))) {
                    resources = files
                            .filter(p -> p.toString().endsWith(".json"))
                            .collect(Collectors.toList());
                }
            } else {
                logger.error("preset_registration directory is not exists");
            }
        } catch (IOException e) {
            logger.error("Error performing preset registration, error: ", e);
            return;
        }
        logger.info("Starting preset registrations, number of requests: {}", resources.size());
        for (Path resource: resources){
            String content;
            try (Scanner scanner = new Scanner(resource).useDelimiter("\\Z")){
                content = scanner.next();
            } catch (IOException e){
                logger.error("Error reading preset registration file {}, skipping to next one. Error: ", resource.getFileName(), e);
                continue;
            }
            //register the preset request
            try {
                register(content);
            } catch (VidSimulatorException e) {
                logger.error("Error proceeding preset registration file {},skipping to next one. Check if the JSON is in correct format. Error: ", resource.getFileName(), e);
            }
        }
    }



    private void setProperties() {
        Resource resource = new ClassPathResource("simulator.properties");
        Properties props = new Properties();
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            logger.error("Error loading simulator properties, error: ", e);
            return;
        }
        logger.info("Simulator properties are {}", props);
        mockServerProtocol = (String)props.get("simulator.mockserver.protocol");
        mockServerHost = (String)props.get("simulator.mockserver.host");
        mockServerPort = Integer.parseInt((String)props.get("simulator.mockserver.port"));
        enablePresetRegistration = Boolean.parseBoolean((String)props.get("simulator.enablePresetRegistration"));
        enableJPA = Boolean.parseBoolean((String)props.get("simulator.enableCentralizedRoleAccess"));
    }

    @RequestMapping(value = {"/registerToVidSimulator"}, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity registerRequest(HttpServletRequest request, @RequestBody String expectation) {
        try {
            register(expectation);
        } catch (VidSimulatorException e) {
            return new ResponseEntity<>("Registration failure! Error: "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Registration successful!",HttpStatus.OK);
    }

    @RequestMapping(value = {"/echo"}, method = RequestMethod.GET)
    ResponseEntity echo(HttpServletRequest request) {
        return isInitialized ? new ResponseEntity<>("",HttpStatus.OK) : new ResponseEntity<>("",HttpStatus.SERVICE_UNAVAILABLE);
    }

    @RequestMapping(value = {"/retrieveRecordedRequests"}, method = RequestMethod.GET)
    public List<HttpRequest> retrieveRecordedRequests() {
        return Arrays.asList(mockServer.retrieveRecordedRequests(null));
    }

    @RequestMapping(value = {"/registerToVidSimulator"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void wipeOutAllExpectations() {
        mockServer.reset();
    }

    private void register(String expectation) throws VidSimulatorException{
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        SimulatorRequestResponseExpectation[] expectationModels;
        try {
            expectationModels = mapper.readValue(expectation, SimulatorRequestResponseExpectation[].class);
        } catch (IOException e) {
            logger.error("Couldn't deserialize register expectation {}, error:", expectation, e);
            throw new VidSimulatorException(e.getMessage());
        }

        for (SimulatorRequestResponseExpectation expectationModel : expectationModels) {
            logger.info("Proceeding registration request: {}", expectationModel);
            register(expectationModel);
        }
    }

    //*******portal role access simulator (added by ag137v)

    @RequestMapping(value = {"/ecompportal_att/auxapi//{ver}/user/*", "/ONAPPORTAL/auxapi//{ver}/user/*"}, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity auxapiGetUser(HttpServletRequest request) {
        if (!enableJPA) {
            return new ResponseEntity<>("Centralized Role Access is disabled", HttpStatus.SERVICE_UNAVAILABLE);
        }
        entityManager.clear();
        String reqUri = request.getRequestURI();
        String userName = reqUri.substring(reqUri.lastIndexOf('/') + 1);
        TypedQuery<User> userQuery = entityManager.createQuery("select u from fn_user u where u.loginId = :userName", User.class);
        userQuery.setParameter("userName", userName);
        User user = doWithSingleRetry(userQuery::getSingleResult);

        Gson g = new Gson();
        String jsonString = g.toJson(user);

        return new ResponseEntity<>(jsonString, HttpStatus.OK);

    }

    @RequestMapping(value = {"/ecompportal_att/auxapi//{ver}/functions", "/ONAPPORTAL/auxapi//{ver}/functions"}, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity auxapiGetFunctions(HttpServletRequest request) {
        if (!enableJPA) {
            return new ResponseEntity<>("Centralized Role Access is disabled", HttpStatus.SERVICE_UNAVAILABLE);
        }
        TypedQuery<Function> userQuery = entityManager.createQuery("select f from fn_function f", Function.class);
        List<Function> functions = doWithSingleRetry(userQuery::getResultList);
        Gson g = new Gson();
        String jsonString = g.toJson(functions);

        return new ResponseEntity<>(jsonString, HttpStatus.OK);

    }

    //*******portal role access simulator end

    @RequestMapping(value = {"/ecompportal_att/auxapi//{ver}/getSessionSlotCheckInterval", "/ONAPPORTAL/auxapi//{ver}/getSessionSlotCheckInterval"}, method = RequestMethod.GET)
    @ResponseBody
    public String getSessionSlotCheckInterval() {
        return "300000";
    }

    @RequestMapping(value = {"/**"})
    public ResponseEntity redirectToMockServer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //This is needed to allow POST redirect - see http://www.baeldung.com/spring-redirect-and-forward
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        //Building the redirect URL
//        String restOfTheUrl = (String) request.getAttribute(
//                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String requestUri = URLEncoder.encode(request.getRequestURI(), "UTF-8");
        requestUri = requestUri.replaceAll("%2F", "/");
        String restOfTheUrl = requestUri.replaceFirst(request.getContextPath(), "");

        StringBuilder sb = new StringBuilder();
        sb.append(mockServerProtocol).append("://").append(mockServerHost).append(":").append(mockServerPort).append(restOfTheUrl);
        String queryString = request.getQueryString();
        if (queryString != null){
            sb.append("?").append(queryString);
        }
        String redirectUrl = sb.toString();
        logger.info("Redirecting the request to : {}", redirectUrl);

        URI uri;
        try {
            uri = new URI("http", null, "localhost", 1080, restOfTheUrl, request.getQueryString(), null);
        } catch (URISyntaxException e) {
            logger.error("Error during proxying request {}, error: ", request.getRequestURI(), e.getMessage());
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        RestTemplate restTemplate = new RestTemplate();
        //Preparing the headers
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames =  request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String headerToSet = headerNames.nextElement();
            headers.set(headerToSet, request.getHeader(headerToSet));
        }
        HttpEntity<String> proxyRequest;
        if ("POST".equalsIgnoreCase(request.getMethod()))
        {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            proxyRequest = new HttpEntity<>(body, headers);
        } else {
            proxyRequest = new HttpEntity<>(headers);
        }

        ResponseEntity<String> responseEntity;
        try {
            responseEntity =
                    restTemplate.exchange(uri, HttpMethod.resolve(request.getMethod()), proxyRequest, String.class);
        } catch (HttpClientErrorException exception) {
            responseEntity = new ResponseEntity<>(exception.getResponseBodyAsString(), exception.getStatusCode());
        }

        return responseEntity;
    }

    private <T> T doWithSingleRetry(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            logger.error("exception was thrown; will retry the same action one more time", e);
            // here exceptions will be thrown
            return supplier.get();
        }
    }

    private void register(SimulatorRequestResponseExpectation expectationModel) throws VidSimulatorException{
        //Setting request according to what is passed
        HttpRequest request = HttpRequest.request();
        String id = expectationModel.getSimulatorRequest().getId();
        if (id != null) {
            request.withHeader("x-simulator-id", id);
        }

        if (expectationModel.getSimulatorRequest().getHeaders()!=null) {
            expectationModel.getSimulatorRequest().getHeaders().forEach(
                    request::withHeader);
        }

        String method = expectationModel.getSimulatorRequest().getMethod();
        if (method != null) {
            request.withMethod(method);
        }
        String path = expectationModel.getSimulatorRequest().getPath();
        if (path != null) {
            request.withPath(path);
        }
        String body = expectationModel.getSimulatorRequest().getBody();
        if (body != null) {
            if (expectationModel.getSimulatorRequest().getStrict()) {
                request.withBody(json(body, MatchType.STRICT));
            } else {
                request.withBody(new JsonBody(body));
            }
        }

        //Queryparams
        final Map<String, List<String>> queryParams = expectationModel.getSimulatorRequest().getQueryParams();
        if (queryParams != null){
            String[] arr = new String[0];
            queryParams.forEach((key, value) -> request.withQueryStringParameter(key, value.toArray(arr)));
        }

        //Setting response according to what is passed
        HttpResponse response = HttpResponse.response();
        Integer responseCode = expectationModel.getSimulatorResponse().getResponseCode();
        if (responseCode != null) {
            response.withStatusCode(responseCode);
        } else {
            logger.error("Invalid registration - response code cannot be empty");
            throw new VidSimulatorException("Invalid registration - response code cannot be empty");
        }

        String respBody = expectationModel.getSimulatorResponse().getBody();
        if (respBody != null) {
            response.withBody(respBody);
        }

        String file = expectationModel.getSimulatorResponse().getFile();
        if (file != null) {
            response.withBody(loadFileString(file));
        }

        Map<String, String> responseHeaders = expectationModel.getSimulatorResponse().getResponseHeaders();
        if (responseHeaders != null) {
            responseHeaders.forEach(response::withHeader);
        }

        Times numberOfTimes = getExpectationNumberOfTimes(expectationModel);

        if (expectationModel.getMisc().getReplace()) {
            logger.info("Unregistering request expectation, if previously set, request: {}", expectationModel.getSimulatorRequest());
            mockServer.clear(request);
        }

        mockServer
                .when(request, numberOfTimes).respond(response);
    }


    private byte[] loadFileString(String filePath) {
        byte[] bytes = null;
        File file = null;
        try {
            file = new ClassPathResource("download_files/" + filePath).getFile();
            try(DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file.getPath())))) {
                bytes = new byte[(int)file.length()];
                dataInputStream.readFully(bytes);
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found for file:" + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Error reading file:" + filePath);
            e.printStackTrace();
        }

        return bytes;
    }
    private Times getExpectationNumberOfTimes(SimulatorRequestResponseExpectation expectationModel) {
        Integer expectationModelNumberOfTimes = expectationModel.getMisc().getNumberOfTimes();
        Times effectiveNumberOfTimes;
        if (expectationModelNumberOfTimes == null || expectationModelNumberOfTimes < 0) {
            effectiveNumberOfTimes = DEFAULT_NUMBER_OF_TIMES;
        } else {
            effectiveNumberOfTimes = exactly(expectationModelNumberOfTimes);
        }
        return effectiveNumberOfTimes;
    }
}
