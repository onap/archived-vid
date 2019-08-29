/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */


package org.onap.vid.aai.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.KeyValueModel;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AAITreeNodeUtilsTest {

    private static final String STRING_VALUE = "Property value";
    @Mock
    private RelationshipList relationshipList;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetMocks() {
        Mockito.reset(relationshipList);
    }


    private static final String STRING_TO_SEARCH = "string to search";

    @Test
    public void findFirstRelationshipByRelatedToRelationshipListIsNull_returnEmptyOptional() {
        Assert.assertFalse(AAITreeNodeUtils.findFirstRelationshipByRelatedTo(null, STRING_TO_SEARCH).isPresent());
    }

    @Test
    public void findFirstRelationshipByRelatedToRelationshipIsNull_returnEmptyOptional() {
        RelationshipList relationshipListNull = mock(RelationshipList.class);
        when(relationshipListNull.getRelationship()).thenReturn(null);
        Assert.assertFalse(AAITreeNodeUtils.findFirstRelationshipByRelatedTo(relationshipListNull, STRING_TO_SEARCH).isPresent());
    }

    @Test
    public void findFirstRelationshipByRelatedTo_oneRelationshipIsPresent_returnOptionalWithRelationship() {
        List<Relationship> relationships = new ArrayList<>();
        Relationship relationship = mockRelationshipRelatedTo(STRING_TO_SEARCH);
        relationships.add(relationship);
        relationships.add(mock(Relationship.class));
        when(relationshipList.getRelationship()).thenReturn(relationships);
        Optional<Relationship> foundRelationship = AAITreeNodeUtils.findFirstRelationshipByRelatedTo(relationshipList, STRING_TO_SEARCH);
        Assert.assertTrue(foundRelationship.isPresent());
        Assert.assertEquals(relationship, foundRelationship.get());
    }

    @Test
    public void findFirstRelationshipByRelatedTo_manyRelationshipsArePresent_returnOptionalWithRelationship() {
        List<Relationship> relationships = new ArrayList<>();
        Relationship relationship = mockRelationshipRelatedTo(STRING_TO_SEARCH);
        Relationship relationship2 = mockRelationshipRelatedTo(STRING_TO_SEARCH);
        relationships.add(relationship);
        relationships.add(relationship2);
        relationships.add(mock(Relationship.class));
        when(relationshipList.getRelationship()).thenReturn(relationships);
        Optional<Relationship> foundRelationship = AAITreeNodeUtils.findFirstRelationshipByRelatedTo(relationshipList, STRING_TO_SEARCH);
        Assert.assertTrue(foundRelationship.isPresent());
        Assert.assertTrue(relationship.equals(foundRelationship.get()) || relationship2.equals(foundRelationship.get()));
    }

    @Test
    public void findFirstRelationshipByRelatedTo_RelationshipsIsNotPresent_returnEmptyOptional() {
        List<Relationship> relationships = new ArrayList<>();
        relationships.add(mock(Relationship.class));
        relationships.add(mock(Relationship.class));
        relationships.add(mock(Relationship.class));
        when(relationshipList.getRelationship()).thenReturn(relationships);
        Assert.assertFalse(AAITreeNodeUtils.findFirstRelationshipByRelatedTo(relationshipList, STRING_TO_SEARCH).isPresent());
    }

    @Test
    public void findFirstValueDataIsNull_returnEmptyOptional() {
        Assert.assertFalse(AAITreeNodeUtils.findFirstValue(null, STRING_TO_SEARCH).isPresent());
    }

    @Test
    public void findFirstValueDataIsEmpty_returnEmptyOptional() {
        Assert.assertFalse(AAITreeNodeUtils.findFirstValue(new ArrayList<>(), STRING_TO_SEARCH).isPresent());
    }

    @Test
    public void findFirstValue_OneMemberWithKeyIsPresent() {
        List<KeyValueModel> members = new ArrayList<>();
        KeyValueModel member = mockKeyValueModel(STRING_TO_SEARCH, STRING_VALUE);
        members.add(mock(KeyValueModel.class));
        members.add(member);
        Optional<String> result = AAITreeNodeUtils.findFirstValue(members, STRING_TO_SEARCH);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(STRING_VALUE, result.get());
    }

    @Test
    public void findFirstValue_ManyMembersWithKeyArePresent() {
        List<KeyValueModel> members = new ArrayList<>();
        KeyValueModel member1 = mockKeyValueModel(STRING_TO_SEARCH, STRING_VALUE);
        KeyValueModel member2 = mockKeyValueModel(STRING_TO_SEARCH, STRING_VALUE + "2");
        members.add(mock(KeyValueModel.class));
        members.add(member2);
        members.add(member1);
        Optional<String> result = AAITreeNodeUtils.findFirstValue(members, STRING_TO_SEARCH);
        Assert.assertTrue(result.isPresent());
        Assert.assertTrue(STRING_VALUE.equals(result.get()) || (STRING_VALUE + "2").equals(result.get()));
    }

    @Test
    public void findFirstValue_MemberIsNotPresent_returnEmptyString() {
        List<KeyValueModel> members = new ArrayList<>();
        members.add(mock(KeyValueModel.class));
        members.add(mock(KeyValueModel.class));
        members.add(mock(KeyValueModel.class));
        Assert.assertFalse(AAITreeNodeUtils.findFirstValue(members, STRING_TO_SEARCH).isPresent());
    }

    private static Relationship mockRelationshipRelatedTo(String relatedTo) {
        Relationship relationship = mock(Relationship.class);
        when(relationship.getRelatedTo()).thenReturn(relatedTo);
        return relationship;
    }

    private static KeyValueModel mockKeyValueModel(String key, String value) {
        KeyValueModel model = mock(KeyValueModel.class);
        when(model.getKey()).thenReturn(key);
        when(model.getValue()).thenReturn(value);
        return model;
    }



}
