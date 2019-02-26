/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia. All rights reserved.
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


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import javax.ws.rs.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;

public class LogfilePathCreatorTest {

    private static final String AUDIT = "audit";
    private static final String ERROR = "error";
    private static final String METRICS = "metrics";

    private LogfilePathCreator creator;

    @Before
    public void setUp() {
        creator = new LogfilePathCreator();
    }

    @Test
    public void shouldThrowInternalServerErrorException_whenLogfileDoesntExist() {
        String loggerName = "notExisting";
        assertThatExceptionOfType(InternalServerErrorException.class)
            .isThrownBy(() -> creator.getLogfilePath(loggerName))
            .withMessage(String.format("logfile for %s not found", loggerName));
    }

    @Test
    public void shouldReturnAuditPath_whenAuditNameIsGiven() {
        assertThat(creator.getLogfilePath(AUDIT)).isEqualTo("logs/EELF/audit.log");
    }

    @Test
    public void shouldReturnErrorPath_whenErrorNameIsGiven() {
        assertThat(creator.getLogfilePath(ERROR)).isEqualTo("logs/EELF/error.log");
    }

    @Test
    public void shouldReturnMetricsPath_whenMetricsNameIsGiven() {
        assertThat(creator.getLogfilePath(METRICS)).isEqualTo("logs/EELF/metrics.log");
    }
}