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

package org.onap.vid.dao;

import java.util.List;
import org.onap.portalsdk.core.domain.Profile;

/**
 * The Interface ProfileDao.
 */
public interface ProfileDao {
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<Profile> findAll();
	
	/**
	 * Gets the profile.
	 *
	 * @param id the id
	 * @return the profile
	 */
	Profile getProfile(int id);
}
