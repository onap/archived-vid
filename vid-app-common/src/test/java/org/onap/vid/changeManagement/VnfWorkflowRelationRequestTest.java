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

public class VnfWorkflowRelationRequestTest {

    private VnfWorkflowRelationRequest createTestSubject() {
        return new VnfWorkflowRelationRequest();
    }

    @Test
    public void testGetWorkflowsDetails() throws Exception {
        VnfWorkflowRelationRequest testSubject;
        List<WorkflowsDetail> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflowsDetails();
    }

    @Test
    public void testSetWorkflowsDetails() throws Exception {
        VnfWorkflowRelationRequest testSubject;
        List<WorkflowsDetail> workflowsDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflowsDetails(workflowsDetails);
    }

    @Test
    public void testEquals() {
        new EqualsTester()
            .addEqualityGroup(new VnfWorkflowRelationRequest(), new VnfWorkflowRelationRequest())
            .addEqualityGroup(createRequest("uuid1", "invUuid1", "workflow-name-1"),
                createRequest("uuid1", "invUuid1", "workflow-name-1"))
            .addEqualityGroup(createRequest("uuid2", "invUuid2", "workflow-name-2"))
            .testEquals();
    }

    private  VnfWorkflowRelationRequest createRequest(String uuid, String invariantUuid, String workflowName) {
        VnfDetails vnfDetails = new VnfDetails(uuid, invariantUuid);
        return new VnfWorkflowRelationRequest(ImmutableList.of(new WorkflowsDetail(vnfDetails, workflowName)));
    }
}