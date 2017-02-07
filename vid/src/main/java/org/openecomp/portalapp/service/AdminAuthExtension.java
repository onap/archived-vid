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

package org.openecomp.portalapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.domain.User;

/**
 * The Class AdminAuthExtension.
 */
@Service("adminAuthExtension")
@Transactional
public class AdminAuthExtension {
	
	/**
	 * Save user extension.
	 *
	 * @param user the user
	 */
	public void saveUserExtension(User user){
		//app's developer implement their own logic here, like updating app's related tables
	}

}
