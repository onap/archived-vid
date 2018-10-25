package org.onap.simulator.wiremock.extensions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.onap.simulator.util.JsonUtils.normalizeJson;

import org.junit.Test;
import org.onap.simulator.wiremock.Simulator;

public class PresetsLoaderTest {

    private static final String TEST_DIRECTORY = "PresetLoader_test";

    private final Simulator simulator = mock(Simulator.class);

    @Test
    public void should_register_all_requests_to_simulator() {
        PresetsLoader presetsLoader = new PresetsLoader(simulator);
        presetsLoader.loadPresets(TEST_DIRECTORY);

        verify(simulator).register(normalizeJson("{'request1':{},'response1':{}}"));
        verify(simulator).register(normalizeJson("{'request2':{},'response2':{}}"));
        verify(simulator).register(normalizeJson("{'request3':{},'response3':{}}"));
        verifyNoMoreInteractions(simulator);
    }
}