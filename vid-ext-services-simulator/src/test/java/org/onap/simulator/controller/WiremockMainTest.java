package org.onap.simulator.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.onap.simulator.wiremock.WireMockSimulator;

public class WiremockMainTest {

    private WireMockSimulator simulator;

    @Before
    public void setUp() {
        simulator = mock(WireMockSimulator.class);
    }

    @Test
    public void should_start_server_on_init() {
        WiremockMain wiremockMain = new WiremockMain(simulator);

        wiremockMain.init();

        verify(simulator).startServer();
    }

    @Test
    public void should_stop_server_on_tear_down() {
        WiremockMain wiremockMain = new WiremockMain(simulator);

        wiremockMain.tearDown();

        verify(simulator).stopServer();
    }
}