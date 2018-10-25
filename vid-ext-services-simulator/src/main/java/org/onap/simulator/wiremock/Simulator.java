package org.onap.simulator.wiremock;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Simulator {

    private final Logger LOGGER = LoggerFactory.getLogger(Simulator.class);

    /**
     * Wiremock search for:
     *
     * 1) presets at ${FILES_ROOT}/mappings/
     * 2) body response files at ${FILES_ROOT}/__files/
     *
     * Suffix is not configurable
     */
    private static final String FILES_ROOT = "/wiremock";

    private final WireMockServer server;
    private final int port;

    public Simulator(int port, boolean verbose) {
        this.port = port;
        this.server = new WireMockServer(configuration(port, verbose));
        logShortRequestInfo();
    }

    public void startServer() {
        server.start();
    }

    public void stopServer() {
        server.stop();
    }

    public void register(String jsonStub) {
        server.addStubMapping(StubMapping.buildFrom(jsonStub));
    }

    public int getPort() {
        return port;
    }

    private WireMockConfiguration configuration(int port, boolean verbose) {
        return options()
            .port(port)
            .withRootDirectory(getClass().getResource(FILES_ROOT).getPath())
            .notifier(new Slf4jNotifier(verbose));
    }

    private void logShortRequestInfo() {
        server.addMockServiceRequestListener((request, response) ->
            LOGGER.info("Returned {} for request {} {}", response.getStatus(), request.getMethod(), request.getUrl())
        );
    }
}