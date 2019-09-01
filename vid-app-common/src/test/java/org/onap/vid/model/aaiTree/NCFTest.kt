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

package org.onap.vid.model.aaiTree

import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class NCFTest {

    private fun createRelationshipListWithRelatedTo(relatedTo: List<String>): RelationshipList {
        val relationshipList = RelationshipList()
        relationshipList.relationship = relatedTo.map {
            val relationShip = Relationship()
            relationShip.relatedTo = it
            relationShip
        }
        return relationshipList
    }

    @DataProvider
    fun testGetNumberOfNetworksDataProvider(): Array<Array<out Any?>> {
        return arrayOf(
                arrayOf(RelationshipList(), 0),
                arrayOf(null, 0),
                arrayOf(createRelationshipListWithRelatedTo(listOf("l3-network", "l3-network")), 2),
                arrayOf(createRelationshipListWithRelatedTo(listOf("l3-network", "something-else")), 1),
                arrayOf(createRelationshipListWithRelatedTo(listOf("something-else")), 0),
                arrayOf(createRelationshipListWithRelatedTo(listOf()), 0)
        )
    }


    @Test(dataProvider = "testGetNumberOfNetworksDataProvider")
    fun testGetNumberOfNetworks(relationShipList: RelationshipList?, expectedNumber: Int) {
        val aaiTreeNode: AAITreeNode = mock(AAITreeNode::class.java)
        Mockito.`when`(aaiTreeNode.relationshipList).thenReturn(relationShipList)
        val ncf = NCF(aaiTreeNode)
        assertEquals(ncf.numberOfNetworks, expectedNumber)
    }
}
