/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.onap.vid.model.ServiceModel;

public class ToscaParserMockHelper {
    private static final Logger logger = LogManager.getLogger(ToscaParserMockHelper.class);

    private static final ObjectMapper om = new ObjectMapper();
    private final String uuid;
    private final String filePath;
    private final ServiceModel serviceModel;

    public ToscaParserMockHelper(String uuid, String filePath) throws IOException {
        this.uuid = uuid;
        this.filePath = filePath;

        InputStream jsonFile = this.getClass().getClassLoader().getResourceAsStream(getFilePath());
        logger.info(jsonFile);
        String expectedJsonAsString = IOUtils.toString(jsonFile, StandardCharsets.UTF_8.name());
        om.registerModule(new KotlinModule());
        this.serviceModel = om.readValue(expectedJsonAsString, ServiceModel.class);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFilePath() {
        return filePath;
    }

    public ServiceModel getServiceModel() {
        return serviceModel;
    }
}
