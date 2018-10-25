package org.onap.simulator.wiremock;

import com.github.tomakehurst.wiremock.standalone.MappingsLoader;
import com.github.tomakehurst.wiremock.standalone.MappingsSource;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappings;
import java.util.List;
import org.onap.simulator.wiremock.extensions.JsonArrayMappingLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wiremock doesn't expose API to set MappingsLoader alone without MappingSource
 *
 * @see com.github.tomakehurst.wiremock.core.WireMockConfiguration#mappingSource(MappingsSource)
 */
public class CustomMappingSource implements MappingsSource {

    private static final Logger logger = LoggerFactory.getLogger(JsonArrayMappingLoader.class);
    private final MappingsLoader mappingsLoader;

    public CustomMappingSource(MappingsLoader mappingsLoader) {
        this.mappingsLoader = mappingsLoader;
    }

    @Override
    public void loadMappingsInto(StubMappings stubMappings) {
        mappingsLoader.loadMappingsInto(stubMappings);
    }

    @Override
    public void save(List<StubMapping> list) {
        logger.warn("Method `saveAll` not supported");
    }

    @Override
    public void save(StubMapping stubMapping) {
        logger.warn("Method `save` not supported");
    }

    @Override
    public void remove(StubMapping stubMapping) {
        logger.warn("Method `remove` not supported");
    }

    @Override
    public void removeAll() {
        logger.warn("Method `removeAll` not supported");
    }
}