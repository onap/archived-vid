package org.onap.simulator.wiremock;

import static org.onap.simulator.util.JsonUtils.normalizeJson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class SimulatorTest {

    private Simulator simulator;
    private int port;

    @Before
    public void setUp() {
        port = 1337;
        simulator = new Simulator(port, false);
        simulator.startServer();
    }

    @After
    public void tearDown() {
        simulator.stopServer();
    }

    @Test
    public void should_mock_simple_request() {
        String stub = normalizeJson("{'request':{'method':'GET','url':'/test'},'response':{'body':'OK'}}");
        simulator.register(stub);
        String response = new RestTemplate().getForObject("http://localhost:" + port + "/test", String.class);

        Assert.assertEquals("OK", response);
    }
}