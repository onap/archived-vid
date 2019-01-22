package org.onap.vid.mso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class MsoUtilTest {

	private MsoUtil createTestSubject() {
		return new MsoUtil();
	}

	@Test
	public void testWrapResponse() throws Exception {
		String body = "Test";
		int statusCode = 0;
		MsoResponseWrapper result;

		// default test
		result = MsoUtil.wrapResponse(body, statusCode);
		assertEquals(result.getEntity(),body);
		assertEquals(result.getStatus(), statusCode);
	}


	@Test
	public void testWrapResponse_2() throws Exception {
		String body = "Test";
		int statusCode = 0;
		RestObject<String> rs = new RestObject<>();
		rs.set(body);
		rs.setStatusCode(statusCode);

		MsoResponseWrapper result;

		result = MsoUtil.wrapResponse(rs);
		Assert.assertNotNull(result);
		assertEquals(result.getEntity(),body);
		assertEquals(result.getStatus(), statusCode);
	}

	@Test
	public void testConvertPojoToString() throws Exception {
		// test 1
		String result = MsoUtil.convertPojoToString(null);
		assertEquals("", result);

		TestModel model = new TestModel("Test");
		result = MsoUtil.convertPojoToString(model);
		ObjectMapper mapper = new ObjectMapper();
		TestModel resultModel = mapper.readValue(result, TestModel.class);
		assertEquals(model, resultModel);
	}

	@Test
	public void testMain() throws Exception {
		String[] args = new String[] { "" };

		// default test
		MsoUtil.main(args);
	}

	class TestModel{
		String test;
		public TestModel(String test) {
			this.test = test;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			TestModel testModel = (TestModel) o;
			return test.equals(testModel.test);
		}

		@Override
		public int hashCode() {
			return Objects.hash(test);
		}

	}
}