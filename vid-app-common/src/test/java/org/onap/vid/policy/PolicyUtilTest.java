package org.onap.vid.policy;

import org.apache.poi.hssf.record.formula.functions.T;
import org.glassfish.jersey.client.ClientResponse;
import org.junit.Assert;
import org.junit.Test;

public class PolicyUtilTest {

	private PolicyUtil createTestSubject() {
		return new PolicyUtil();
	}

	@Test
	public void testWrapResponse() throws Exception {
		String body = "";
		int statusCode = 0;
		PolicyResponseWrapper result;

		// default test
		result = PolicyUtil.wrapResponse(body, statusCode);
	}


	@Test
	public void testWrapResponse_2() throws Exception {
		RestObject<String> rs = null;
		PolicyResponseWrapper result;

		// test 1
		rs = null;
		result = PolicyUtil.wrapResponse(rs);
		Assert.assertNotNull(result);
	}

	@Test
	public void testConvertPojoToString() throws Exception {
		T t = null;
		String result;

		// test 1
		t = null;
		result = PolicyUtil.convertPojoToString(t);
		Assert.assertEquals("", result);
	}

	@Test
	public void testMain() throws Exception {
		String[] args = new String[] { "" };

		// default test
		PolicyUtil.main(args);
	}
}