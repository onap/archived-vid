package org.openecomp.vid.asdc.beans.tosca;

import java.util.List;

import org.junit.Test;

public class ConstraintTest {

	private Constraint createTestSubject() {
		return new Constraint();
	}

	
	@Test
	public void testGetvalid_values() throws Exception {
		Constraint testSubject;
		List<Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getvalid_values();
	}

	
	@Test
	public void testGetEqual() throws Exception {
		Constraint testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getEqual();
	}

	
	@Test
	public void testGetGreater_than() throws Exception {
		Constraint testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getGreater_than();
	}

	
	@Test
	public void testGetGreater_or_equal() throws Exception {
		Constraint testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getGreater_or_equal();
	}

	
	@Test
	public void testGetLess_than() throws Exception {
		Constraint testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getLess_than();
	}

	
	@Test
	public void testGetLess_or_equal() throws Exception {
		Constraint testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getLess_or_equal();
	}

	
	@Test
	public void testGetIn_range() throws Exception {
		Constraint testSubject;
		List<Object> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getIn_range();
	}

	
	@Test
	public void testGetLength() throws Exception {
		Constraint testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getLength();
	}

	
	@Test
	public void testGetMin_length() throws Exception {
		Constraint testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMin_length();
	}

	
	@Test
	public void testGetMax_length() throws Exception {
		Constraint testSubject;
		Object result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMax_length();
	}

	
	@Test
	public void testSetvalid_values() throws Exception {
		Constraint testSubject;
		List<Object> vlist = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setvalid_values(vlist);
	}

	
	@Test
	public void testSetEqual() throws Exception {
		Constraint testSubject;
		Object e = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setEqual(e);
	}

	
	@Test
	public void testSetGreater_than() throws Exception {
		Constraint testSubject;
		Object e = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setGreater_than(e);
	}

	
	@Test
	public void testSetLess_than() throws Exception {
		Constraint testSubject;
		Object e = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setLess_than(e);
	}

	
	@Test
	public void testSetIn_range() throws Exception {
		Constraint testSubject;
		List<Object> e = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setIn_range(e);
	}

	
	@Test
	public void testSetLength() throws Exception {
		Constraint testSubject;
		Object e = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setLength(e);
	}

	
	@Test
	public void testSetMin_length() throws Exception {
		Constraint testSubject;
		Object e = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMin_length(e);
	}

	
	@Test
	public void testSetMax_length() throws Exception {
		Constraint testSubject;
		Object e = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setMax_length(e);
	}

	
	@Test
	public void testToString() throws Exception {
		Constraint testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.toString();
	}
}