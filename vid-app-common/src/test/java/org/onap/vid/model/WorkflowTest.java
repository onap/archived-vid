package org.onap.vid.model;

import java.util.Collection;

import org.junit.Test;

public class WorkflowTest {

	private Workflow createTestSubject() {
		return new Workflow();
	}

	@Test
	public void testGetId() throws Exception {
		Workflow testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getId();
	}

	@Test
	public void testGetWorkflowName() throws Exception {
		Workflow testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getWorkflowName();
	}

	@Test
	public void testGetVnfNames() throws Exception {
		Workflow testSubject;
		Collection<String> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVnfNames();
	}

	@Test
	public void testSetId() throws Exception {
		Workflow testSubject;
		int id = 0;

		// default test
		testSubject = createTestSubject();
		testSubject.setId(id);
	}

	@Test
	public void testSetWorkflowName() throws Exception {
		Workflow testSubject;
		String workflowName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setWorkflowName(workflowName);
	}

	@Test
	public void testSetVnfName() throws Exception {
		Workflow testSubject;
		Collection<String> vnfNames = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setVnfName(vnfNames);
	}
}