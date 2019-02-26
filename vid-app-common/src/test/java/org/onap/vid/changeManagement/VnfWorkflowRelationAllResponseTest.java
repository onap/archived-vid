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

package org.onap.vid.changeManagement;

import java.util.List;

import org.junit.Test;

public class VnfWorkflowRelationAllResponseTest {

    private VnfWorkflowRelationAllResponse createTestSubject() {
        return new VnfWorkflowRelationAllResponse();
    }

    @Test
    public void testGetVnfs() throws Exception {
        VnfWorkflowRelationAllResponse testSubject;
        List<VnfDetailsWithWorkflows> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfs();
    }

    @Test
    public void testSetVnfs() throws Exception {
        VnfWorkflowRelationAllResponse testSubject;
        List<VnfDetailsWithWorkflows> vnfs = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfs(vnfs);
    }
}
