package org.onap.vid.mso;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Assert;
import org.junit.Test;

public class MsoUtilTest {

	private MsoUtil createTestSubject() {
		return new MsoUtil();
	}

	@Test
	public void testWrapResponse() throws Exception {
		String body = "";
		int statusCode = 0;
		MsoResponseWrapper result;

		// default test
		result = MsoUtil.wrapResponse(body, statusCode);
	}


	@Test
	public void testWrapResponse_2() throws Exception {
		RestObject<String> rs = null;
		MsoResponseWrapper result;

		// test 1
		result = MsoUtil.wrapResponse(rs);
		Assert.assertNotNull(result);
	}

	@Test
	public void testConvertPojoToString() throws Exception {
		String result;

		// test 1
		result = MsoUtil.convertPojoToString(null);
		Assert.assertEquals("", result);
	}

	@Test
	public void testMain() throws Exception {
		String[] args = new String[] { "" };

		// default test
		MsoUtil.main(args);
	}
}