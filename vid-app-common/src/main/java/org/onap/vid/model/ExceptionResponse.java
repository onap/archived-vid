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

package org.onap.vid.model;

import org.onap.logging.ref.slf4j.ONAPLogConstants.MDCs;
import org.slf4j.MDC;

/**
 * The Class ExceptionResponse.
 */
public class ExceptionResponse {

	public ExceptionResponse() {
	}

	/** The exception. */
	private String exception;
	
	/** The message. */
	private String message;

	public ExceptionResponse(String exception, String message) {
		this.exception = exception;
		this.message = message;
	}

	public ExceptionResponse(Exception exception) {
		setException(exception);
	}

	/**
	 * Gets the exception.
	 *
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * Sets the exception.
	 *
	 * @param exception the new exception
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	public void setException(Exception exception) {
		setException(exception.getClass().toString().replaceFirst("^.*[\\.$]", ""));
		setMessage(exception.getMessage() + " (Request id: " + MDC.get(MDCs.REQUEST_ID) + ")");
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
