package org.onap.simulator.wiremock.extensions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.standalone.MappingsLoader;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappings;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.onap.simulator.controller.WiremockConfig;
import org.onap.simulator.wiremock.ResourceNotFoundException;

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
        MappingsLoader enabledMappingLoader = new WiremockConfig().mappingsLoader(true, filesRoot);

        enabledMappingLoader.loadMappingsInto(stubMappings);

        ArgumentCaptor<StubMapping> stubCaptor = ArgumentCaptor.forClass(StubMapping.class);
        verify(stubMappings, times(3)).addMapping(stubCaptor.capture());
        verifyNoMoreInteractions(stubMappings);

        List<String> urls = getUrlsFromStubs(stubCaptor);
        assertThat(urls).containsExactlyInAnyOrder("/single/request", "/array/1/request", "/array/2/request");
    }

    @Test
    public void should_not_register_requests_when_simulator_disabled() {
        MappingsLoader disabledMappingLoader = new WiremockConfig().mappingsLoader(false, filesRoot);

        disabledMappingLoader.loadMappingsInto(stubMappings);

        verifyZeroInteractions(stubMappings);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_not_register_requests_when_files_root_not_exist() {
        FileSource nonExistentFilesRoot = new WiremockConfig().fileSource("non-existent");
        new WiremockConfig().mappingsLoader(true, nonExistentFilesRoot);
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