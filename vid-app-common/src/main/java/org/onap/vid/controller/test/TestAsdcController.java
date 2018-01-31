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

package  org.onap.vid.controller.test;

import org.codehaus.jackson.map.ObjectMapper;
import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.onap.vid.model.ExceptionResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The Class TestAsdcController.
 */
@RestController
@RequestMapping("testasdc")
public class TestAsdcController extends RestrictedBaseController {

	/**
	 * Gets the model.
	 *
	 * @param modelId the model id
	 * @param request the request
	 * @return the model
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/getModel/{modelId}", method = RequestMethod.GET)
	public String getModel(@PathVariable("modelId") String modelId, HttpServletRequest request) throws Exception {

		System.err.println("SDC: GET MODEL: modelId: " + modelId);

		// @formatter:off
		return
				"{" +
						"\"uuid\": \"5be686dc-fdca-4d54-8548-5d0ed23e962b\"," +
						"\"invariantUUID\": \"e5962da9-fe4f-433a-bc99-b43e0d88a9a1\"," +
						"\"name\": \"DE220127\"," +
						"\"version\": \"0.1\"," +
						"\"inputs\": {" +
						"\"defaultGateway\": {" +
						"\"type\": \"String\"," +
						"\"default\": \"192.168.1.1\"," +
						"\"description\": \"Router default gateway - use any valid IPv4 address\"" +
						"}," +
						"\"subnetMask\": {" +
						"\"type\": \"String\"," +
						"\"default\": \"255.255.255.0\"," +
						"\"description\": \"Router subnet mask - example (255.255.255.0)\"" +
						"}" +
						"}" +
						"}";
		// @formatter:on
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
