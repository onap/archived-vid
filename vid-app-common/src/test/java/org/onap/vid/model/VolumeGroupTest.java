package org.onap.vid.model;

import org.junit.Test;
import org.onap.vid.asdc.beans.tosca.Group;


public class VolumeGroupTest {

	private VolumeGroup createTestSubject() {
		return new VolumeGroup();
	}

	
	@Test
	public void testGetUuid() throws Exception {
		VolumeGroup testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getUuid();
	}

	
	@Test
	public void testGetCustomizationUuid() throws Exception {
		VolumeGroup testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getCustomizationUuid();
	}

	
	@Test
	public void testGetModelCustomizationName() throws Exception {
		VolumeGroup testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getModelCustomizationName();
	}

	
	@Test
	public void testGetInvariantUuid() throws Exception {
		VolumeGroup testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getInvariantUuid();
	}

	
	@Test
	public void testGetDescription() throws Exception {
		VolumeGroup testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getDescription();
	}

	
	@Test
	public void testGetName() throws Exception {
		VolumeGroup testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getName();
	}

	
	@Test
	public void testGetVersion() throws Exception {
		VolumeGroup testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getVersion();
	}

	
	@Test
	public void testSetUuid() throws Exception {
		VolumeGroup testSubject;
		String uuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setUuid(uuid);
	}

	
	@Test
	public void testSetInvariantUuid() throws Exception {
		VolumeGroup testSubject;
		String invariantUuid = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setInvariantUuid(invariantUuid);
	}

	
	@Test
	public void testSetDescription() throws Exception {
		VolumeGroup testSubject;
		String description = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setDescription(description);
	}

	
	@Test
	public void testSetName() throws Exception {
		VolumeGroup testSubject;
		String name = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setName(name);
	}

	
	@Test
	public void testSetVersion() throws Exception {
		VolumeGroup testSubject;
		String version = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setVersion(version);
	}

	
	@Test
	public void testSetCustomizationUuid() throws Exception {
		VolumeGroup testSubject;
		String u = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setCustomizationUuid(u);
	}

	
	@Test
	public void testSetModelCustomizationName() throws Exception {
		VolumeGroup testSubject;
		String u = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setModelCustomizationName(u);
	}

	

}