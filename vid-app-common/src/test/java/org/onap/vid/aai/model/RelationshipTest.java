package org.onap.vid.aai.model;

import java.util.List;

import org.junit.Test;
import org.onap.vid.aai.model.Relationship;
import org.onap.vid.aai.model.RelationshipData;


public class RelationshipTest {

	private Relationship createTestSubject() {
		return new Relationship();
	}


	@Test
	public void testGetRelatedTo() throws Exception {
		Relationship testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelatedTo();
	}


	@Test
	public void testSetRelatedTo() throws Exception {
		Relationship testSubject;
		String relatedTo = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setRelatedTo(relatedTo);
	}


	@Test
	public void testGetRelatedLink() throws Exception {
		Relationship testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelatedLink();
	}


	@Test
	public void testSetRelatedLink() throws Exception {
		Relationship testSubject;
		String relatedLink = "";

		// default test
		testSubject = createTestSubject();
		testSubject.setRelatedLink(relatedLink);
	}


	@Test
	public void testGetRelationDataList() throws Exception {
		Relationship testSubject;
		List<RelationshipData> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelationDataList();
	}


	@Test
	public void testSetRelationDataList() throws Exception {
		Relationship testSubject;
		List<RelationshipData> relationDataList = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRelationDataList(relationDataList);
	}


	@Test
	public void testGetRelatedToPropertyList() throws Exception {
		Relationship testSubject;
		List<RelatedToProperty> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getRelatedToPropertyList();
	}


	@Test
	public void testSetRelatedToPropertyList() throws Exception {
		Relationship testSubject;
		List<RelatedToProperty> relatedToPropertyList = null;

		// default test
		testSubject = createTestSubject();
		testSubject.setRelatedToPropertyList(relatedToPropertyList);
	}
}