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

package org.openecomp.vid.controller.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.openecomp.vid.model.ExceptionResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.openecomp.portalsdk.core.controller.RestrictedBaseController;

/**
 * The Class TestAaiController.
 */
@RestController
@RequestMapping("testaai")
public class TestAaiController extends RestrictedBaseController {

	/**
	 * Gets the subscription service type list.
	 *
	 * @param globalCustomerId the global customer id
	 * @param request the request
	 * @return the subscription service type list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/getSubscriptionServiceTypeList/{globalCustomerId}", method = RequestMethod.GET)
	public String getSubscriptionServiceTypeList(@PathVariable("globalCustomerId") String globalCustomerId, HttpServletRequest request)
			throws Exception {

		System.err.println("GET SUBSCRIPTION SERVICE TYPE LIST: globalCustomerId: " + globalCustomerId);

		return "[\"vMOG\", \"sevice type 2\", \"sevice type 3\", \"sevice type 4\"]";
	}

	/**
	 * Exception.
	 *
	 * @param e the e
	 * @param response the response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@ExceptionHandler(Exception.class)
	private void exception(Exception e, HttpServletResponse response) throws IOException {

		/*
		 * This logging step should preferably be replaced with an appropriate
		 * logging method consistent whatever logging mechanism the rest of the
		 * application code uses.
		 */

		e.printStackTrace(System.err);

		response.setContentType("application/json; charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setException(e.getClass().toString().replaceFirst("^.*\\.", ""));
		exceptionResponse.setMessage(e.getMessage());

		response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));

		response.flushBuffer();

	}

}
