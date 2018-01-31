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