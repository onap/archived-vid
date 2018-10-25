/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.simulator.wiremock.extensions;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.github.tomakehurst.wiremock.common.AbstractFileSource.byFileExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.JsonException;
import com.github.tomakehurst.wiremock.common.TextFile;
import com.github.tomakehurst.wiremock.standalone.MappingFileException;
import com.github.tomakehurst.wiremock.standalone.MappingsLoader;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappings;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonArrayMappingLoader implements MappingsLoader {

    private static final Logger logger = LoggerFactory.getLogger(JsonArrayMappingLoader.class);

    private static final String MAPPINGS_ROOT = "mappings";

    private final ObjectMapper arrayMapper = new ObjectMapper().configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    private final FileSource mappingsSource;

    public JsonArrayMappingLoader(FileSource filesRoot) {
        this.mappingsSource = filesRoot.child(MAPPINGS_ROOT);
    }

    @Override
    public void loadMappingsInto(StubMappings stubMappings) {

        if (mappingDirectoryNotExists()) {
            logger.warn("Mapping directory does not exist!");
            return;
        }

        mappingsSource
            .listFilesRecursively()
            .stream()
            .filter(byFileExtension("json"))
            .map(this::buildStubsFrom)
            .flatMap(Collection::stream)
            .forEach(stubMappings::addMapping);
    }

    private List<StubMapping> buildStubsFrom(TextFile mappingFile) {
        try {
            return Arrays.asList(arrayMapper.readValue(mappingFile.readContents(), StubMapping[].class));
        } catch (JsonException e) {
            throw new MappingFileException(mappingFile.getPath(), e.getErrors().first().getDetail());
        } catch (IOException e) {
            throw new MappingFileException(mappingFile.getPath(), e.getMessage());
        }
    }

    private boolean mappingDirectoryNotExists() {
        return !mappingsSource.exists();
    }
}