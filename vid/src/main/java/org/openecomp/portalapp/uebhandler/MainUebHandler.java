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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.ueb.UebMsg;
import org.openecomp.portalsdk.core.onboarding.ueb.UebMsgTypes;

//-------------------------------------------------------------------------
// Listens for received UEB messages and handles the messages
//
// Note: To implement a synchronous reply call getMsgId on the request 
//       and putMsgId on the reply (echoing the request MsgId).
//       
/**
 * The Class MainUebHandler.
 */
//-------------------------------------------------------------------------
@Component("MainUebHandler")
public class MainUebHandler {

	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MainUebHandler.class);


	/** The inbox queue. */
	ConcurrentLinkedQueue<UebMsg> inboxQueue = null;

	/** The widget notification handler. */
	@Autowired
	WidgetNotificationHandler widgetNotificationHandler;

	/**
	 * Run handler.
	 *
	 * @param queue the queue
	 */
	@Async
	public void runHandler(ConcurrentLinkedQueue<UebMsg> queue) {
		inboxQueue = queue;

		logger.info(EELFLoggerDelegate.debugLogger, ("==> MainUebHandler started"));

		while (true) {
			UebMsg msg = null;
			while ((msg = inboxQueue.poll()) != null) {
				if (msg.getMsgType() != null) {
					logger.debug(EELFLoggerDelegate.debugLogger, ("<== Received UEB message : " + msg.toString()));

					switch (msg.getMsgType()) {
					/*
					 * Add your own defined handler objects, use @Component for
					 * the class. See WidgetNotificationHandler as an example.
					 *
					 * Use @Async on methods for performance
					 *
					 * For syncronous replies use UebManager publishReply and
					 * echo back the msgId in your response ie
					 * msg.putMsgId(requestMsg.getMsgId())
					 *
					 * case UebMsgTypes.UEB_MSG_TYPE_XYZ: {
					 * XYZHandler.handleMsg(msg); break; }
					 */
					case UebMsgTypes.UEB_MSG_TYPE_WIDGET_NOTIFICATION: {
						widgetNotificationHandler.handleWidgetNotification(msg);
						break;
					}
					default: {
						
						logger.info(EELFLoggerDelegate.debugLogger, ("Unknown message type [" + msg.getMsgType() + "] from " + msg.getSourceTopicName()));

						break;
					}
					}
				}
			}

			if (Thread.interrupted()) {
				
				logger.info(EELFLoggerDelegate.debugLogger, ("==> UebMainHandler exiting"));

				break;
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.info(EELFLoggerDelegate.debugLogger, ("UebMainHandler interrupted during sleep" + e.getMessage()));

			}
		}
	}
}
