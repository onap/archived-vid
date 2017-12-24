package org.openecomp.simulator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.openecomp.simulator.errorHandling.VidSimulatorException;
import org.openecomp.simulator.model.SimulatorRequestResponseExpectation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.View;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RestController
@Component
public class SimulatorController {

    private ClientAndServer mockServer;
    private String mockServerProtocol;
    private String mockServerHost;
    private Integer mockServerPort;
    private Boolean enablePresetRegistration;


    Logger logger = LoggerFactory.getLogger(SimulatorController.class);

    @PostConstruct
    public void init(){
        logger.info("Starting VID Simulator....");
        setProperties();
        mockServer = startClientAndServer(mockServerPort);
        presetRegister();
    }

    @PreDestroy
    public void tearDown(){
        logger.info("Stopping VID Simulator....");
        mockServer.stop();
    }


    private void presetRegister() {
        //Checking if set
        if (enablePresetRegistration == null || !enablePresetRegistration){
            logger.info("Preset registration property is false or not set - skipping preset registration...");
            return;
        }
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources;
        try {
            resources = resolver.getResources("/preset_registration/*.json");
        } catch (IOException e) {
            logger.error("Error performing preset registration, error: ", e);
            return;
        }
        logger.info("Starting preset registrations, number of requests: {}", resources.length);
        for (Resource resource: resources){
            File file;
            String content;
            try {
                file = resource.getFile();
                content = new Scanner(file).useDelimiter("\\Z").next();
            } catch (IOException e){
                logger.error("Error reading preset registration file {},skipping to next one. Error: ", resource.getFilename(), e);
                continue;
            }
            //registrating the preset request
            try {
                register(content);
            } catch (VidSimulatorException e) {
                logger.error("Error proceeding preset registration file {},skipping to next one. Check if the JSON is in correct format. Error: ", resource.getFilename(), e);
                continue;
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
    }

    @RequestMapping(value = {"/registerToVidSimulator"}, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity registerRequest(HttpServletRequest request, @RequestBody String expectation) {
        try {
            register(expectation);
        } catch (VidSimulatorException e) {
            return new ResponseEntity("Registration failure! Error: "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Registration successful!",HttpStatus.OK);
    }

    private void register(String expectation) throws VidSimulatorException{
        ObjectMapper mapper = new ObjectMapper();
        SimulatorRequestResponseExpectation expectationModel = null;
        try {
            expectationModel = mapper.readValue(expectation, SimulatorRequestResponseExpectation.class);
        } catch (IOException e) {
            logger.error("Couldn't deserialize register expectation {}, error:", expectation, e);
            throw new VidSimulatorException(e.getMessage());
        }
        logger.info("Proceeding registration request: {}", expectationModel);
        register(expectationModel);
    }


    @RequestMapping(value = {"/**"})
    public String redirectToMockServer(HttpServletRequest request, HttpServletResponse response) {
        //Currently, the easiest logic is redirecting

        //This is needed to allow POST redirect - see http://www.baeldung.com/spring-redirect-and-forward
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        //Building the redirect URL
        String restOfTheUrl = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        StringBuilder sb = new StringBuilder();
        sb.append(mockServerProtocol+"://"+mockServerHost+":"+mockServerPort+"/"+restOfTheUrl);
        String queryString = request.getQueryString();
        if (queryString != null){
            sb.append("?").append(queryString);
        }
        String redirectUrl = sb.toString();
        logger.info("Redirecting the request to : {}", redirectUrl);
        return ("redirect:"+redirectUrl);

        //This was a try to setup a proxy instead of redirect
        //Abandoned this direction when trying to return the original HTTP error code which was registered to mock server,  instead of wrapped up HTTP 500.

       /* String restOfTheUrl = "/"+(String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        URI uri = null;
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

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(uri, HttpMethod.resolve(request.getMethod()), new HttpEntity<String>(body, headers), String.class);
        
        return responseEntity;*/
    }

    private void register(SimulatorRequestResponseExpectation expectationModel) throws VidSimulatorException{
        //Setting request according to what is passed
        HttpRequest request = HttpRequest.request();
        String id = expectationModel.getSimulatorRequest().getId();
        if (id != null) {
            request.withHeader("x-simulator-id", id);
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
            request.withBody(body);
        }

        //Queryparams
        final Map<String, List<String>> queryParams = expectationModel.getSimulatorRequest().getQueryParams();
        if (queryParams != null){
            String[] arr = new String[0];
            queryParams.entrySet().stream().forEach(x -> {
                request.withQueryStringParameter(x.getKey(), x.getValue().toArray(arr));
            });
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

        Map<String, String> responseHeaders = expectationModel.getSimulatorResponse().getResponseHeaders();
        if (responseHeaders != null) {
            responseHeaders.forEach(response::withHeader);
        }

        logger.info("Unregistering request expectation, if previously set, request: {}", expectationModel.getSimulatorRequest());
        mockServer.clear(request);
        mockServer
                .when(request).respond(response);
    }
}
