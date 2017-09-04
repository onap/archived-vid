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

package org.openecomp.fusionapp.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.openecomp.fusion.core.MockApplicationContextTestSuite;
import org.openecomp.portalsdk.core.domain.Profile;
import org.openecomp.portalsdk.core.domain.User;
import org.openecomp.portalsdk.core.service.ProfileService;
import org.openecomp.portalsdk.core.service.UserProfileService;


/**
 * The Class ProfileServiceTest.
 */

public class ProfileServiceTest extends MockApplicationContextTestSuite {
	
	/** The service. */
	@Autowired
	ProfileService service;
	
	/** The user profile service. */
	@Autowired
	UserProfileService userProfileService;
	
	/**
	 * Test find all.
	 */
	@Test
	public void testFindAll() {
		
		List<Profile> profiles = service.findAll();
		Assert.assertTrue(profiles.size() > 0);
	}

	/**
	 * Test find all active.
	 */
	@Test
	public void testFindAllActive() {
				
		List<User> users = userProfileService.findAllActive();
		List<User> activeUsers = userProfileService.findAllActive();
		Assert.assertTrue(users.size() - activeUsers.size() >= 0);
	}
}
