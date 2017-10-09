package org.openecomp.vid.mso;

import org.junit.Test;
import org.openecomp.vid.changeManagement.RequestDetails;

public class MsoLocalClientNewTest {

	private MsoLocalClientNew createTestSubject() {
		return new MsoLocalClientNew();
	}

	@Test
	public void testCreateSvcInstance() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.createSvcInstance(requestDetails, endpoint);
	}

	@Test
	public void testCreateInstance() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails request = null;
		String path = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.createInstance(request, path);
	}

	@Test
	public void testCreateVnf() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.createVnf(requestDetails, endpoint);
	}

	@Test
	public void testCreateNwInstance() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.createNwInstance(requestDetails, endpoint);
	}

	@Test
	public void testCreateVolumeGroupInstance() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String path = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.createVolumeGroupInstance(requestDetails, path);
	}

	@Test
	public void testCreateVfModuleInstance() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.createVfModuleInstance(requestDetails, endpoint);
	}

	@Test
	public void testDeleteSvcInstance() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.deleteSvcInstance(requestDetails, endpoint);
	}

	@Test
	public void testDeleteVnf() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.deleteVnf(requestDetails, endpoint);
	}

	@Test
	public void testDeleteVfModule() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.deleteVfModule(requestDetails, endpoint);
	}

	@Test
	public void testDeleteVolumeGroupInstance() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.deleteVolumeGroupInstance(requestDetails, endpoint);
	}

	@Test
	public void testDeleteNwInstance() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.deleteNwInstance(requestDetails, endpoint);
	}

	@Test
	public void testGetOrchestrationRequest() throws Exception {
		MsoLocalClientNew testSubject;
		String t = "";
		String sourceId = "";
		String endpoint = "";
		RestObject restObject = null;

		// default test
		testSubject = createTestSubject();
		testSubject.getOrchestrationRequest(t, sourceId, endpoint, restObject);
	}


	@Test
	public void testUpdateVnf() throws Exception {
		MsoLocalClientNew testSubject;
		RequestDetails requestDetails = null;
		String vnf_endpoint = "";
		MsoResponseWrapper result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.updateVnf(requestDetails, vnf_endpoint);
	}
}