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

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.ueb.UebMsg;


/**
 * The Class WidgetNotificationHandler.
 */
@Component
public class WidgetNotificationHandler {

	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetNotificationHandler.class);


	/**
	 * Instantiates a new widget notification handler.
	 */
	public WidgetNotificationHandler() {
	}

	/**
	 * Handle widget notification.
	 *
	 * @param requestMsg the request msg
	 */
	@Async
	public void handleWidgetNotification(UebMsg requestMsg) {
		logger.debug(EELFLoggerDelegate.debugLogger, ("handleWidgetNotification received notification: " + requestMsg.toString()));
		/*
		 * Here the notification msg can be handled
		 *
		 * requestMsg.getPayload() - returns string that contains the
		 * Application defined content
		 */
	}
}
