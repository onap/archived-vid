/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright 2018 Nokia
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

package org.onap.vid.changeManagement;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import java.util.List;

import org.junit.Test;
import org.onap.vid.model.serviceInstantiation.Vnf;

public class GetVnfWorkflowRelationRequestTest {

    private GetVnfWorkflowRelationRequest createTestSubject() {
        return new GetVnfWorkflowRelationRequest();
    }

    @Test
    public void testGetVnfDetails() throws Exception {
        GetVnfWorkflowRelationRequest testSubject;
        List<VnfDetails> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfDetails();
    }

    @Test
    public void testSetVnfDetails() throws Exception {
        GetVnfWorkflowRelationRequest testSubject;
        List<VnfDetails> vnfDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfDetails(vnfDetails);
    }

    @Test
    public void testEquals() {
        new EqualsTester()
            .addEqualityGroup(new GetVnfWorkflowRelationRequest(), new GetVnfWorkflowRelationRequest())
            .addEqualityGroup(createRequest("uuid1", "uuid2", "invariantUuid1", "invariantUuid2"),
                createRequest("uuid1", "uuid2", "invariantUuid1", "invariantUuid2"))
            .addEqualityGroup(createRequest("uuid3", "uuid4", "invariantUuid3", "invariantUuid4"))
            .testEquals();
    }

    private GetVnfWorkflowRelationRequest createRequest(String uuid1, String uuid2, String invariantUuid1, String invariantUuid2) {
        return new GetVnfWorkflowRelationRequest(ImmutableList.of(new VnfDetails(uuid1, invariantUuid1),
            new VnfDetails(uuid2, invariantUuid2)));
    }
}