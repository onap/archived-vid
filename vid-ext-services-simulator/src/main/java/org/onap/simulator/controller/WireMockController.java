package org.onap.simulator.controller;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import com.github.tomakehurst.wiremock.common.JsonException;
import java.io.IOException;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.onap.simulator.wiremock.extensions.PresetsLoader;
import org.onap.simulator.wiremock.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Profile("useWiremock")
@Component
@RestController
public class WireMockController {

    private final Logger LOGGER = LoggerFactory.getLogger(WireMockController.class);

    private int port;
    private boolean enablePresets;
    private String presetsDirectory;
    private boolean verbose;

    private final Simulator simulator;
    private final PresetsLoader presetsLoader;

    public WireMockController() {
        readProperties();
        simulator = new Simulator(port, verbose);
        presetsLoader = new PresetsLoader(simulator);
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Starting VID Simulator....");

        simulator.startServer();

        if (enablePresets) {
            presetsLoader.loadPresets("/" + presetsDirectory);
        }

        LOGGER.info("VID Simulator started successfully on port {}", simulator.getPort());
        LOGGER.info("Check api: http://wiremock.org/docs/wiremock-admin-api.html");
    }

    @PreDestroy
    public void tearDown() {
        LOGGER.info("Stopping VID Simulator....");

        simulator.stopServer();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody String jsonRequest) {
        try {
            simulator.register(jsonRequest);
        } catch (JsonException e) {
            LOGGER.error("Unprocessable entity: ", e);
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok().build();
    }

    private void readProperties() {
        Resource resource = new ClassPathResource("wiremock.properties");
        Properties props;
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            LOGGER.error("Error loading simulator properties, error: ", e);
            return;
        }
        LOGGER.info("Simulator properties are {}", props);
        port = parseInt(props.getProperty("simulator.port"));
        enablePresets = parseBoolean(props.getProperty("simulator.enablePresets"));
        presetsDirectory = props.getProperty("simulator.presetsDirectory");
        verbose = parseBoolean(props.getProperty("simulator.verbose"));
    }
}
