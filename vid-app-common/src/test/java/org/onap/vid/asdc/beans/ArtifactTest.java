package org.onap.vid.asdc.beans;

import org.junit.Test;


public class ArtifactTest {

	private Artifact createTestSubject() {
		return new Artifact();
	}


	@Test
	public void testGetArtifactName() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactName();
	}


	@Test
	public void testGetArtifactType() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactType();
	}


	@Test
	public void testGetArtifactGroupType() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactGroupType();
	}


	@Test
	public void testGetArtifactLabel() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactLabel();
	}

	
	@Test
	public void testGetArtifactURL() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactURL();
	}

	
	@Test
	public void testGetArtifactDescription() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactDescription();
	}

	
	@Test
	public void testGetArtifactTimeout() throws Exception {
		Artifact testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactTimeout();
	}

	
	@Test
	public void testGetArtifactChecksum() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactChecksum();
	}

	
	@Test
	public void testGetArtifactUUID() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactUUID();
	}

	
	@Test
	public void testGetArtifactVersion() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getArtifactVersion();
	}

	
	@Test
	public void testGetGeneratedFromUUID() throws Exception {
		Artifact testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getGeneratedFromUUID();
	}

	
	@Test
	public void testSetArtifactName() throws Exception {
		Artifact testSubject;
		String artifactName = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactName(artifactName);
	}

	
	@Test
	public void testSetArtifactType() throws Exception {
		Artifact testSubject;
		String artifactType = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactType(artifactType);
	}

	
	@Test
	public void testSetArtifactGroupType() throws Exception {
		Artifact testSubject;
		String artifactGroupType = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactGroupType(artifactGroupType);
	}

	
	@Test
	public void testSetArtifactLabel() throws Exception {
		Artifact testSubject;
		String artifactLabel = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactLabel(artifactLabel);
	}

	
	@Test
	public void testSetArtifactURL() throws Exception {
		Artifact testSubject;
		String artifactURL = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactURL(artifactURL);
	}

	
	@Test
	public void testSetArtifactDescription() throws Exception {
		Artifact testSubject;
		String artifactDescription = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactDescription(artifactDescription);
	}

	
	@Test
	public void testSetArtifactTimeout() throws Exception {
		Artifact testSubject;
		int artifactTimeout = 0;

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactTimeout(artifactTimeout);
	}

	
	@Test
	public void testSetArtifactChecksum() throws Exception {
		Artifact testSubject;
		String artifactChecksum = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactChecksum(artifactChecksum);
	}

	
	@Test
	public void testSetArtifactUUID() throws Exception {
		Artifact testSubject;
		String artifactUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactUUID(artifactUUID);
	}

	
	@Test
	public void testSetArtifactVersion() throws Exception {
		Artifact testSubject;
		String artifactVersion = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactVersion(artifactVersion);
	}

	
	@Test
	public void testSetGeneratedFromUUID() throws Exception {
		Artifact testSubject;
		String generatedFromUUID = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setGeneratedFromUUID(generatedFromUUID);
	}

	
	@Test
	public void testHashCode() throws Exception {
		Artifact testSubject;
		int result;

		// default test
		testSubject = createTestSubject();
		testSubject.setArtifactUUID("48a52540-8772-4368-9cdb-1f124ea5c931");
		result = testSubject.hashCode();
	}

	
	@Test
	public void testEquals() throws Exception {
		Artifact testSubject;
		Object o = null;
		boolean result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.equals(o);
	}
}