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

import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Class TestPageController.
 */
@Controller
public class TestPageController extends RestrictedBaseController {

	/**
	 * Test mso page.
	 *
	 * @return the model and view
	 */
	@RequestMapping(value = { "testMso.htm" }, method = RequestMethod.GET)
	public ModelAndView testMsoPage() {
		return new ModelAndView(getViewName());
	}

	/**
	 * Test view edit page.
	 *
	 * @return the model and view
	 */
	@RequestMapping(value = { "testViewEdit" }, method = RequestMethod.GET)
	public ModelAndView testViewEditPage() {
		return new ModelAndView(getViewName());
	}

}
