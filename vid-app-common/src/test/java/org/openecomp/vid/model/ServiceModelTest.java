package org.openecomp.vid.model;

import java.util.Map;

import org.junit.Test;
import org.openecomp.vid.asdc.beans.tosca.ToscaModel;
import org.openecomp.vid.model.Service;

public class ServiceModelTest {

	private ServiceModel createTestSubject() {
		return new ServiceModel();
	}

	@Test
	public void testGetService() throws Exception {
		ServiceModel testSubject;
		Service result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getService();
	}

	@Test
	public void testGetVnfs() throws Exception {
		ServiceModel testSubject;
		Map<String, VNF> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVnfs();
	}

	@Test
	public void testGetNetworks() throws Exception {
		ServiceModel testSubject;
		Map<String, Network> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getNetworks();
	}

	@Test
	public void testSetService() throws Exception {
		ServiceModel testSubject;
		Service service = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setService(service);
	}

	@Test
	public void testSetVnfs() throws Exception {
		ServiceModel testSubject;
		Map<String, VNF> vnfs = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setVnfs(vnfs);
	}

	@Test
	public void testSetNetworks() throws Exception {
		ServiceModel testSubject;
		Map<String, Network> networks = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setNetworks(networks);
	}

	@Test
	public void testGetVfModules() throws Exception {
		ServiceModel testSubject;
		Map<String, VfModule> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVfModules();
	}

	@Test
	public void testGetVolumeGroups() throws Exception {
		ServiceModel testSubject;
		Map<String, VolumeGroup> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVolumeGroups();
	}

	@Test
	public void testSetVfModules() throws Exception {
		ServiceModel testSubject;
		Map<String, VfModule> vfModules = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setVfModules(vfModules);
	}

	@Test
	public void testSetVolumeGroups() throws Exception {
		ServiceModel testSubject;
		Map<String, VolumeGroup> volumeGroups = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setVolumeGroups(volumeGroups);
	}

	@Test
	public void testAssociateGroups() throws Exception {
		ServiceModel testSubject;

		// default test
		testSubject = createTestSubject();
		testSubject.associateGroups();
	}
}