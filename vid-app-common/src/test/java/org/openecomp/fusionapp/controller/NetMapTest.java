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

package org.openecomp.fusionapp.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.openecomp.fusion.core.MockApplicationContextTestSuite;

/**
 * The Class NetMapTest.
 */
public class NetMapTest extends MockApplicationContextTestSuite {
	
	/**
	 * Test get net map.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetNetMap() throws Exception {
		ResultActions ra =getMockMvc().perform(MockMvcRequestBuilders.get("/net_map"));
		//Assert.assertEquals(UrlAccessRestrictedException.class,ra.andReturn().getResolvedException().getClass());
		Assert.assertEquals("net_map_int",ra.andReturn().getModelAndView().getModel().get("frame_int"));
	}
	

}
