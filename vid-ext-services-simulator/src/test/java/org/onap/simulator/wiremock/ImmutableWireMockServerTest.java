package org.onap.simulator.wiremock;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ImmutableWireMockServerTest {

    private ImmutableWireMockServer simulator;
    private WireMockServer backingServer;

    @Before
    public void setUp() {
        backingServer = mock(WireMockServer.class);
        simulator = ImmutableWireMockServer.fromServer(backingServer);
        clearInvocations(backingServer);
    }

    @Test
    public void should_start_server() {
        simulator.startServer();

        verify(backingServer).start();
        verifyNoMoreInteractions(backingServer);
    }

    @Test
    public void should_stop_server() {
        simulator.stopServer();

        verify(backingServer).stop();
        verifyNoMoreInteractions(backingServer);
    }

    @Test
    public void should_return_port() {
        when(backingServer.port()).thenReturn(1111);

        Assert.assertEquals(1111, simulator.getPort());
    }
}