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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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

    /**
     * Model for JSON response with health-check results.
     */
    public final class HealthStatus {
        /**
         * Either 200 or 500
         * */
        private int statusCode;

        /**
         * Additional detail in case of error, empty in case of success.
         * */
        private String message;
        private String date;

        public HealthStatus(int code, String msg) {
            this.statusCode = code;
            this.message = msg;
        }

        public HealthStatus(int code, String date, String msg) {
            this.statusCode = code;
            this.message = msg;
            this.date = date;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getMessage() {
            return message;
        }

        public String getDate() {
            return date;
        }
    }

    @SuppressWarnings("unchecked")
    public int getProfileCount(String driver, String URL, String username, String password) {
        return new FnAppDoaImpl().getProfileCount(driver, URL, username, password);
    }


    /**
     * Obtain the HealthCheck Status from the System.Properties file.
     * Used by IDNS for redundancy
     *
     * @return ResponseEntity The response entity
     */
    @RequestMapping(value = "/healthCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HealthStatus gethealthCheckStatusforIDNS() {

        String driver = SystemProperties.getProperty("db.driver");
        String URL = SystemProperties.getProperty("db.connectionURL");
        String username = SystemProperties.getProperty("db.userName");
        String password = SystemProperties.getProperty("db.password");

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "driver ::" + driver);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "URL::" + URL);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "username::" + username);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "password::" + password);

        try {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "Performing health check");
            int count = getProfileCount(driver, URL, username, password);
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "count:::" + count);
            return new HealthStatus(200, "health check succeeded");
        } catch (Exception ex) {

            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to perform health check", ex);
            return new HealthStatus(500, "health check failed: " + ex.toString());
        }
    }

    /**
     * Obtain the  HealthCheck Status from the System.Properties file.
     *
     * @return ResponseEntity The response entity
     * @throws IOException Signals that an I/O exception has occurred.
     *                     Project :
     */
    @RequestMapping(value = "rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HealthStatus getHealthCheck(
            @PathVariable("User-Agent") String UserAgent,
            @PathVariable("X-ECOMP-RequestID") String ECOMPRequestID) {

        String driver = SystemProperties.getProperty("db.driver");
        String URL = SystemProperties.getProperty("db.connectionURL");
        String username = SystemProperties.getProperty("db.userName");
        String password = SystemProperties.getProperty("db.password");

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "driver ::" + driver);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "URL::" + URL);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "username::" + username);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "password::" + password);

        try {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "Performing health check");
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "User-Agent" + UserAgent);
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "X-ECOMP-RequestID" + ECOMPRequestID);


            int count = getProfileCount(driver, URL, username, password);

            LOGGER.debug(EELFLoggerDelegate.debugLogger, "count:::" + count);
            return new HealthStatus(200, dateFormat.format(new Date()), "health check succeeded");
        } catch (Exception ex) {

            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to perform health check", ex);
            return new HealthStatus(500, dateFormat.format(new Date()), "health check failed: " + ex.toString());
        }
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public GitRepositoryState getCommitInfo() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(GIT_PROPERTIES_FILENAME));
        return new GitRepositoryState(properties);
    }
}

