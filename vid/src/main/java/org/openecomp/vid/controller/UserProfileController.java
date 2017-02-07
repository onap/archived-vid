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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.domain.Profile;
import org.openecomp.portalsdk.core.service.ProfileService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller for user profile view. The view is restricted to authenticated
 * users. The view name resolves to page user_profile.jsp which uses Angular.
 */

@Controller
@RequestMapping("/")
public class UserProfileController extends RestrictedBaseController {

   /** The service. */
   @Autowired
   ProfileService service;

   /**
    * Profile search.
    *
    * @param request the request
    * @return the model and view
    */
   @RequestMapping(value = {"/user_profile" }, method = RequestMethod.GET)
   public ModelAndView ProfileSearch(HttpServletRequest request) {
	   Map<String, Object> model = new HashMap<String, Object>();
	   ObjectMapper mapper = new ObjectMapper();
	   List<Profile> profileList = service.findAll();
       try {
		model.put("customerInfo", mapper.writeValueAsString(profileList));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
     
       return new ModelAndView("user_profile","model", model);
   }

}
