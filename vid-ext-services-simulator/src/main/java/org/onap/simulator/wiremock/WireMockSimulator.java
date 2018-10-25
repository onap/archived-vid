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
package org.onap.simulator.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WireMockSimulator {

    private static final Logger logger = LoggerFactory.getLogger(WireMockSimulator.class);

    private final WireMockServer server;

    public WireMockSimulator(WireMockServer server) {
        this.server = server;

        logShortRequestInfo();
    }

    public void startServer() {
        server.start();
    }

    public void stopServer() {
        server.stop();
    }

    public int getPort() {
        return server.port();
    }

    private void logShortRequestInfo() {
        server.addMockServiceRequestListener(this::requestListener);
    }

    private void requestListener(Request request, Response response) {
        logger.info("Returned {} for request {} {}", response.getStatus(), request.getMethod(), request.getUrl());
    }
}