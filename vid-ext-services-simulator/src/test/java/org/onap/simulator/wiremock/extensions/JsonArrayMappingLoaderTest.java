package org.onap.simulator.wiremock.extensions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappings;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.onap.simulator.controller.WiremockConfig;

public class JsonArrayMappingLoaderTest {

    private static final String TEST_DIRECTORY = "wiremock";

    private StubMappings stubMappings;
    private FileSource filesRoot;

    @Before
    public void setUp() {
        stubMappings = mock(StubMappings.class);
        filesRoot = new WiremockConfig().fileSource(TEST_DIRECTORY);
    }

    @Test
    public void should_register_all_requests_to_simulator() {
        JsonArrayMappingLoader enabledMappingLoader = new JsonArrayMappingLoader(filesRoot, true);

        enabledMappingLoader.loadMappingsInto(stubMappings);

        ArgumentCaptor<StubMapping> argument = ArgumentCaptor.forClass(StubMapping.class);
        verify(stubMappings, times(3)).addMapping(argument.capture());
        verifyNoMoreInteractions(stubMappings);

        List<String> urls = getUrlsFromStubs(argument);
        assertEquals(3, urls.size());
        assertThat(urls).containsExactlyInAnyOrder("/single/request", "/array/1/request", "/array/2/request");
    }

    @Test
    public void should_not_register_requests_when_simulator_disabled() {
        JsonArrayMappingLoader disabledMappingLoader = new JsonArrayMappingLoader(filesRoot, false);

        disabledMappingLoader.loadMappingsInto(stubMappings);

        verifyZeroInteractions(stubMappings);
    }

    @Test(expected = NullPointerException.class)
    public void should_not_register_requests_when_files_root_not_exist() {
        FileSource nonExistentFilesRoot = new WiremockConfig().fileSource("non-existent");
        JsonArrayMappingLoader mappingLoader = new JsonArrayMappingLoader(nonExistentFilesRoot, true);

        mappingLoader.loadMappingsInto(stubMappings);
    }

    private List<String> getUrlsFromStubs(ArgumentCaptor<StubMapping> argument) {
        return argument
            .getAllValues()
            .stream()
            .map(StubMapping::getRequest)
            .map(RequestPattern::getUrl)
            .collect(Collectors.toList());
    }
}