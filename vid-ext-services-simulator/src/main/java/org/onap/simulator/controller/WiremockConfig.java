package org.onap.simulator.controller;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.String.format;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.standalone.MappingsLoader;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Objects;
import org.onap.simulator.wiremock.CustomMappingSource;
import org.onap.simulator.wiremock.ImmutableWireMockServer;
import org.onap.simulator.wiremock.extensions.JsonArrayMappingLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

@Lazy
@Configuration
@PropertySource("classpath:wiremock.properties")
public class WiremockConfig {

    @Bean
    public ImmutableWireMockServer immutableWireMockServer(WireMockServer server) {
        return ImmutableWireMockServer.fromServer(server);
    }

    @Bean
    public WireMockServer wireMockServer(WireMockConfiguration configuration) {
        return new WireMockServer(configuration);
    }

    /**
     * Wiremock search for:
     *
     * 1) presets at ${FILES_ROOT}/mappings/
     * 2) body response files at ${FILES_ROOT}/__files/
     *
     * Suffix is not configurable
     */
    @Bean
    public FileSource fileSource(@Value("${simulator.rootDirectory}") String rootDirectory) {
        return new SingleRootFileSource(getResource(rootDirectory));
    }

    @Bean
    public MappingsLoader mappingsLoader(@Value("${simulator.enablePresets}") boolean enablePresets, FileSource fileSource) {
        return enablePresets ? new JsonArrayMappingLoader(fileSource) : noOpMappingLoader();
    }

    @Bean
    public WireMockConfiguration configuration(
        @Value("${simulator.port}") int port,
        @Value("${simulator.verbose}") boolean verbose,
        FileSource rootFileSource,
        MappingsLoader mappingsLoader) {

        return options()
            .port(port)
            .fileSource(rootFileSource)
            .mappingSource(new CustomMappingSource(mappingsLoader))
            .notifier(new Slf4jNotifier(verbose));
    }

    private String getResource(String rootDirectory) {
        String errorMessage = format("Does not found directory `%s` on classpath", rootDirectory);
        return Objects.requireNonNull(getClass().getClassLoader().getResource(rootDirectory), errorMessage).getPath();
    }

    private MappingsLoader noOpMappingLoader() {
        return stubMappings -> {};
    }
}