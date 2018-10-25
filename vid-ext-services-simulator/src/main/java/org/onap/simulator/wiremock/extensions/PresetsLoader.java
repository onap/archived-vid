package org.onap.simulator.wiremock.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.JsonException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.onap.simulator.wiremock.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Wiremock doesn't support requests wrapped in JSON Array. This class provides alternative preset mechanism.
 *
 * If presets doesn't contain JSON Arrays consider using native preset support in Wiremock
 */
public class PresetsLoader {

    private static final String JSON_ARRAY_START = "[";

    private final Logger LOGGER = LoggerFactory.getLogger(PresetsLoader.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final Simulator simulator;

    public PresetsLoader(Simulator simulator) {
        this.simulator = simulator;
    }

    public void loadPresets(String presetDirectory) {
        List<Path> files = collectPresets(presetDirectory);

        for (Path path : files) {
            registerRequest(path);
        }
    }

    private List<Path> collectPresets(String presetDirectory) {
        try {
            Path presetDirectoryPath = new PathMatchingResourcePatternResolver(getClass().getClassLoader())
                .getResource(presetDirectory)
                .getFile()
                .toPath();

            return collectChildren(presetDirectoryPath);
        } catch (IOException e) {
            LOGGER.error("Failed to read {}", presetDirectory);
            throw new RuntimeException(e);
        }
    }

    private List<Path> collectChildren(Path presetDirectoryPath) throws IOException {
        return Files.walk(presetDirectoryPath)
            .filter(file -> !file.toFile().isDirectory())
            .collect(Collectors.toList());
    }

    private void registerRequest(Path path) {
        try {
            String content = new String(Files.readAllBytes(path));
            if (content.startsWith(JSON_ARRAY_START)) {
                registerJsonArray(content);
            } else {
                simulator.register(content);
            }
        } catch (IOException | JsonException e) {
            LOGGER.error("Failed to register {}", path);
            throw new RuntimeException(e);
        }
    }

    private void registerJsonArray(String content) throws IOException {
        mapper
            .readTree(content)
            .iterator()
            .forEachRemaining(entry -> simulator.register(entry.toString()));
    }
}