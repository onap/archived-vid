/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.controllers;

import org.onap.portalsdk.core.controller.UnRestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.dao.FnAppDoaImpl;
import org.onap.vid.model.GitRepositoryState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Controller for user profile view. The view is restricted to authenticated
 * users. The view name resolves to page user_profile.jsp which uses Angular.
 */

@RestController
@RequestMapping("/")
public class HealthCheckController extends UnRestrictedBaseController {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(HealthCheckController.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    private static final String GIT_PROPERTIES_FILENAME = "git.properties";
    private FnAppDoaImpl fnAppDoaImpl;

    @Autowired
    public HealthCheckController(FnAppDoaImpl fnAppDoaImpl) {
        this.fnAppDoaImpl = fnAppDoaImpl;
    }

    private static void logData() {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "Performing health check");
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "URL::" + getUrl());
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "username::" + getUsername());
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "password::" + getPassword());
    }

    private static String getUrl() {
        return SystemProperties.getProperty("db.connectionURL");
    }

    private static String getUsername() {
        return SystemProperties.getProperty("db.userName");
    }

    private static String getPassword() {
        return SystemProperties.getProperty("db.password");
    }

    private static HealthStatus getOkStatus() {
        return new HealthStatus(HttpStatus.OK, dateFormat.format(new Date()), "health check succeeded");
    }

    private static HealthStatus getErrorStatus(String msg) {
        return new HealthStatus(HttpStatus.INTERNAL_SERVER_ERROR, dateFormat.format(
                new Date()), "health check failed: " + msg);
    }

    /**
     * Obtain the HealthCheck Status from the System.Properties file.
     * Used by IDNS for redundancy
     *
     * @return ResponseEntity The response entity
     */
    @RequestMapping(value = "/healthCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HealthStatus getHealthCheckStatusForIDNS() {
        return getStatus();
    }

    /**
     * Obtain the  HealthCheck Status from the System.Properties file.
     *
     * @return ResponseEntity The response entity
     */
    @RequestMapping(value = "rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HealthStatus getHealthCheck(
            @PathVariable("User-Agent") String UserAgent,
            @PathVariable("X-ECOMP-RequestID") String ECOMPRequestID) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "User-Agent" + UserAgent);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "X-ECOMP-RequestID" + ECOMPRequestID);
        return getStatus();
    }

    @RequestMapping(value = "/commitInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public GitRepositoryState getCommitInfo() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(GIT_PROPERTIES_FILENAME));
        return new GitRepositoryState(properties);
    }

    private HealthStatus getStatus() {
        logData();
        try {
            int count = getProfileCount(getUrl(), getUsername(), getPassword());
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "count:::" + count);
            return getOkStatus();
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            LOGGER.error(EELFLoggerDelegate.errorLogger, errorMsg);
            return getErrorStatus(errorMsg);
        }
    }

    private int getProfileCount(String URL, String username, String password) throws SQLException {
        return fnAppDoaImpl.getProfileCount(URL, username, password);
    }

    /**
     * Model for JSON response with health-check results.
     */
    public static final class HealthStatus {

        private int statusCode;
        private String detailedMsg;
        private String date;

        public HealthStatus(HttpStatus code, String date, String detailedMsg) {
            this.statusCode = code.value();
            this.detailedMsg = detailedMsg;
            this.date = date;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getDetailedMsg() {
            return detailedMsg;
        }

        public String getDate() {
            return date;
        }
    }
}

