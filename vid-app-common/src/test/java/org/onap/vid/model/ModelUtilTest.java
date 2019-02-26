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

import org.junit.Assert;
import org.junit.Test;


public class ModelUtilTest {

	private ModelUtil createTestSubject() {
		return new ModelUtil();
	}

	
	@Test
	public void testGetTags() throws Exception {
		String[] namespaces;
		String constantValue = "test";
		String[] result;

		// test 1
		namespaces = null;
		result = ModelUtil.getTags(namespaces, constantValue);
		Assert.assertNull(result);

		// test 2
		namespaces = new String[] { "" };
		result = ModelUtil.getTags(namespaces, constantValue);
		Assert.assertArrayEquals(new String[] { constantValue }, result);
	}

	
	@Test
	public void testIsType() throws Exception {
		String type = "a";
		String[] tags;
		boolean result;

		// test 1
		tags = null;
		result = ModelUtil.isType(type, tags);
		Assert.assertEquals(false, result);

		// test 2
		tags = new String[] { "a" };
		result = ModelUtil.isType(type, tags);
		Assert.assertEquals(true, result);
	}
}
