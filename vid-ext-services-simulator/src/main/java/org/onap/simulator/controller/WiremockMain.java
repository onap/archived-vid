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
package org.onap.simulator.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.onap.simulator.wiremock.WireMockSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("useWiremock")
@Component
public class WiremockMain {

    private static final Logger logger = LoggerFactory.getLogger(WiremockMain.class);

    private final WireMockSimulator wiremockSimulator;

    @Autowired
    WiremockMain(WireMockSimulator wiremockSimulator) {
        this.wiremockSimulator = wiremockSimulator;
    }

    @PostConstruct
    void init() {
        logger.info("Starting VID Simulator....");

        wiremockSimulator.startServer();

        logger.info("VID Simulator started successfully on port {}", wiremockSimulator.getPort());
        logger.info("Check api: http://wiremock.org/docs/wiremock-admin-api.html");
    }

    @PreDestroy
    public void tearDown() {
        logger.info("Stopping VID Simulator....");

        wiremockSimulator.stopServer();
    }
}