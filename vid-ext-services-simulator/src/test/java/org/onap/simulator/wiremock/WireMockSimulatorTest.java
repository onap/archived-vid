package org.onap.simulator.wiremock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.Before;
import org.junit.Test;

public class WireMockSimulatorTest {

    private WireMockSimulator simulator;
    private WireMockServer backingServer;

    @Before
    public void setUp() {
        backingServer = mock(WireMockServer.class);
        simulator = new WireMockSimulator(backingServer);
    }

    @Test
    public void should_start_server() {
        simulator.startServer();

        verify(backingServer).start();
    }

    @Test
    public void should_stop_server() {
        simulator.stopServer();

        verify(backingServer).stop();
    }

    @Test
    public void should_return_port() {
        simulator.getPort();

        verify(backingServer).port();
    }
}