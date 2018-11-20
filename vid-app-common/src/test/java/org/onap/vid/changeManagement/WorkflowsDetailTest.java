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

import com.google.common.testing.EqualsTester;
import org.junit.Test;

public class WorkflowsDetailTest {

    private WorkflowsDetail createTestSubject() {
        return new WorkflowsDetail();
    }

    @Test
    public void testGetVnfDetails() throws Exception {
        WorkflowsDetail testSubject;
        VnfDetails result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfDetails();
    }

    @Test
    public void testSetVnfDetails() throws Exception {
        WorkflowsDetail testSubject;
        VnfDetails vnfDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfDetails(vnfDetails);
    }

    @Test
    public void testGetWorkflowName() throws Exception {
        WorkflowsDetail testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflowName();
    }

    @Test
    public void testSetWorkflowName() throws Exception {
        WorkflowsDetail testSubject;
        String workflowName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflowName(workflowName);
    }

    @Test
    public void testToString() throws Exception {
        WorkflowsDetail testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }

    @Test
    public void testEquals() {
        new EqualsTester()
            .addEqualityGroup(new WorkflowsDetail(), new WorkflowsDetail())
            .addEqualityGroup(createDetail("uuid1", "invUuid1", "workflowName1"),
                createDetail("uuid1", "invUuid1", "workflowName1"))
            .addEqualityGroup(createDetail("uuid2", "invUuid2", "workflowName2"))
            .testEquals();
    }

    private WorkflowsDetail createDetail(String uuid, String invUuid, String workflowName) {
        return new WorkflowsDetail(new VnfDetails(uuid, invUuid), workflowName);
    }
}