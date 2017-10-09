package org.openecomp.vid.aai.model;

import org.junit.Test;


public class RelationshipDataTest {

	private RelationshipData createTestSubject() {
		return new RelationshipData();
	}


	@Test
	public void testGetRelationshipKey() throws Exception {
		RelationshipData testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelationshipKey();
	}


	@Test
	public void testSetRelationshipKey() throws Exception {
		RelationshipData testSubject;
		String relationshipKey = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setRelationshipKey(relationshipKey);
	}


	@Test
	public void testGetRelationshipValue() throws Exception {
		RelationshipData testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelationshipValue();
	}


	@Test
	public void testSetRelationshipValue() throws Exception {
		RelationshipData testSubject;
		String relationshipValue = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setRelationshipValue(relationshipValue);
	}
}