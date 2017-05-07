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

package org.openecomp.vid.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.openecomp.portalsdk.core.controller.UnRestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.dao.FnAppDoaImpl;

/**
 * Controller for user profile view. The view is restricted to authenticated
 * users. The view name resolves to page user_profile.jsp which uses Angular.
 */

@RestController
@RequestMapping("/")
public class HealthCheckController extends UnRestrictedBaseController {


	/** The logger. */
		EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HealthCheckController.class);
		
		/** The Constant dateFormat. */
		final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
		
	   private static final String HEALTH_CHECK_PATH = "/healthCheck";
	   
	   /**
		 * Model for JSON response with health-check results.
		 */
		public class HealthStatus {
			// Either 200 or 500
			public int statusCode;
			
			// Additional detail in case of error, empty in case of success.
			public String message;
			
			public String date;

			public HealthStatus(int code, String msg) {
				this.statusCode = code;
				this.message = msg;
			}
			
			public HealthStatus(int code,String date, String msg) {
				this.statusCode = code;
				this.message = msg;
				this.date=date;
			}

			public int getStatusCode() {
				return statusCode;
			}

			public void setStatusCode(int code) {
				this.statusCode = code;
			}

			public String getMessage() {
				return message;
			}

			public void setMessage(String msg) {
				this.message = msg;
			}
			
			public String getDate() {
				return date;
			}

			public void setDate(String date) {
				this.date = date;
			}

		}
  
	   @SuppressWarnings("unchecked")
		public int getProfileCount(String driver, String URL, String username, String password) {
		   FnAppDoaImpl doa= new FnAppDoaImpl();
		   int count= doa.getProfileCount(driver,URL,username,password);
			return count;
		}
	   
	   
	   
		/**
		 * Obtain the HealthCheck Status from the System.Properties file.
		 * Used by IDNS for redundancy
		 * @return ResponseEntity The response entity
		 * @throws IOException Signals that an I/O exception has occurred.
		 * @throws InterruptedException the interrupted exception
		 */	
		@RequestMapping(value="/healthCheck",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)  	
		public HealthStatus gethealthCheckStatusforIDNS() throws IOException, InterruptedException {

			String driver = SystemProperties.getProperty("db.driver");
			String URL = SystemProperties.getProperty("db.connectionURL");
			String username = SystemProperties.getProperty("db.userName");
			String password = SystemProperties.getProperty("db.password");
			
			logger.debug(EELFLoggerDelegate.debugLogger, "driver ::" + driver);
			logger.debug(EELFLoggerDelegate.debugLogger, "URL::" + URL);
			logger.debug(EELFLoggerDelegate.debugLogger, "username::" + username);
			logger.debug(EELFLoggerDelegate.debugLogger,"password::" + password);
			
			
			HealthStatus healthStatus = null;   
			try {
				logger.debug(EELFLoggerDelegate.debugLogger, "Performing health check");
				int count=getProfileCount(driver,URL,username,password);
				logger.debug(EELFLoggerDelegate.debugLogger,"count:::"+count);
				healthStatus = new HealthStatus(200, "health check succeeded");
			} catch (Exception ex) {
			
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to perform health check", ex);
				healthStatus = new HealthStatus(500, "health check failed: " + ex.toString());
			}
			return healthStatus;
		}
		
		/**
		 * Obtain the  HealthCheck Status from the System.Properties file.
		 *
		 * @return ResponseEntity The response entity
		 * @throws IOException Signals that an I/O exception has occurred.
		 * @throws InterruptedException the interrupted exception
		 * Project :
		 */	
		@RequestMapping(value="rest/healthCheck/{User-Agent}/{X-ECOMP-RequestID}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)  	
		public HealthStatus getHealthCheck(
				@PathVariable("User-Agent") String UserAgent,
				@PathVariable("X-ECOMP-RequestID") String ECOMPRequestID) throws IOException, InterruptedException {

			String driver = SystemProperties.getProperty("db.driver");
			String URL = SystemProperties.getProperty("db.connectionURL");
			String username = SystemProperties.getProperty("db.userName");
			String password = SystemProperties.getProperty("db.password");
			
				logger.debug(EELFLoggerDelegate.debugLogger, "driver ::" + driver);
				logger.debug(EELFLoggerDelegate.debugLogger, "URL::" + URL);
				logger.debug(EELFLoggerDelegate.debugLogger, "username::" + username);
				logger.debug(EELFLoggerDelegate.debugLogger,"password::" + password);
				
			
			HealthStatus healthStatus = null;   
			try {
				logger.debug(EELFLoggerDelegate.debugLogger, "Performing health check");
				logger.debug(EELFLoggerDelegate.debugLogger, "User-Agent" + UserAgent);
				logger.debug(EELFLoggerDelegate.debugLogger, "X-ECOMP-RequestID" + ECOMPRequestID);

				
				int count=getProfileCount(driver,URL,username,password);
				
				logger.debug(EELFLoggerDelegate.debugLogger,"count:::"+count);
				healthStatus = new HealthStatus(200,dateFormat.format(new Date()) ,"health check succeeded");
			} catch (Exception ex) {
			
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to perform health check", ex);
				healthStatus = new HealthStatus(500,dateFormat.format(new Date()),"health check failed: " + ex.toString());
			}
			return healthStatus;
		}
}

