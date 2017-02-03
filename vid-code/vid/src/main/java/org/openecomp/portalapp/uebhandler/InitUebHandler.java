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

package org.openecomp.portalapp.uebhandler;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.openecomp.portalsdk.core.logging.format.AlarmSeverityEnum;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalApiConstants;
import org.openecomp.portalsdk.core.onboarding.crossapi.PortalApiProperties;
import org.openecomp.portalsdk.core.onboarding.ueb.UebManager;
import org.openecomp.portalsdk.core.onboarding.ueb.UebMsg;
import org.openecomp.vid.conf.ExternalAppConfig;

//
// Adding this class for the sole purpose of insuring that the MainUebHandler really 
// honors @Async and kicks off a thread.  For more info google @Async and read about
// @Async only working if called from different class.
/**
 * The Class InitUebHandler.
 */
//
@Configuration
public class InitUebHandler {

	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(InitUebHandler.class);

	
	/** The main ueb handler. */
	@Autowired
	MainUebHandler mainUebHandler;
	
	/**
	 * Instantiates a new inits the ueb handler.
	 */
	public InitUebHandler()	{
	
	}
	
	/**
	 * Inits the ueb.
	 */
	@PostConstruct
	public void initUeb()	{
		
		try {
			String enableListenerThread = PortalApiProperties.getProperty(PortalApiConstants.UEB_LISTENERS_ENABLE);
			if (enableListenerThread.equalsIgnoreCase("true")) {
                ConcurrentLinkedQueue<UebMsg> inboxQueue = new ConcurrentLinkedQueue<UebMsg>();
	    	    UebManager.getInstance().initListener(inboxQueue);
  		        mainUebHandler.runHandler(inboxQueue);
				logger.info(EELFLoggerDelegate.debugLogger, ("Returned from initiating mainUebHandler..."));
  	        }
		    else {
				logger.info(EELFLoggerDelegate.debugLogger, ("Not starting UEB listening thread because ueb_listeners_enable is not set to true in the properties file."));
		    }
		}
		catch (Exception e) {
			logger.error(EELFLoggerDelegate.debugLogger, ("Not starting UEB listening thread because property could not be read " + PortalApiConstants.UEB_LISTENERS_ENABLE),AlarmSeverityEnum.MAJOR);
		}
	
	}
}

