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

package org.onap.vid.aai;

import org.junit.Test;
import org.onap.vid.aai.model.RelationshipList;

public class OperationalEnvironmentTest {

    private OperationalEnvironment createTestSubject() {
        return new OperationalEnvironment();
    }

    @Test
    public void testGetOperationalEnvironmentId() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentId();
    }

    @Test
    public void testSetOperationalEnvironmentId() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setJsonOperationalEnvironmentId(operationalEnvironmentId);
    }

    @Test
    public void testGetOperationalEnvironmentName() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentName();
    }

    @Test
    public void testSetOperationalEnvironmentName() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setJsonOperationalEnvironmentName(operationalEnvironmentName);
    }

    @Test
    public void testGetOperationalEnvironmentType() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentType();
    }

    @Test
    public void testSetOperationalEnvironmentType() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setJsonOperationalEnvironmentType(operationalEnvironmentType);
    }

    @Test
    public void testGetOperationalEnvironmentStatus() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentStatus();
    }

    @Test
    public void testSetOperationalEnvironmentStatus() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentStatus = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setJsonOperationalEnvironmentStatus(operationalEnvironmentStatus);
    }

    @Test
    public void testGetTenantContext() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getTenantContext();
    }

    @Test
    public void testSetTenantContext() throws Exception {
        OperationalEnvironment testSubject;
        String tenantContext = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setJsonTenantContext(tenantContext);
    }

    @Test
    public void testGetWorkloadContext() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkloadContext();
    }

    @Test
    public void testSetWorkloadContext() throws Exception {
        OperationalEnvironment testSubject;
        String workloadContext = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setJsonWorkloadContext(workloadContext);
    }

    @Test
    public void testGetResourceVersion() throws Exception {
        OperationalEnvironment testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getResourceVersion();
    }

    @Test
    public void testSetResourceVersion() throws Exception {
        OperationalEnvironment testSubject;
        String resourceVersion = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setJsonResourceVersion(resourceVersion);
    }

    @Test
    public void testGetRelationshipList() throws Exception {
        OperationalEnvironment testSubject;
        RelationshipList result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRelationshipList();
    }

    @Test
    public void testSetRelationshipList() throws Exception {
        OperationalEnvironment testSubject;
        RelationshipList relationshipList = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setJsonRelationshipList(relationshipList);
    }

    @Test
    public void testOperationalEnvironment() throws Exception {
        OperationalEnvironment testSubject;
        String operationalEnvironmentId = "";
        String operationalEnvironmentName = "";
        String operationalEnvironmentType = "";
        String operationalEnvironmentStatus = "";
        String tenantContext = "";
        String workloadContext = "";
        String resourceVersion = "";
        RelationshipList relationshipList = null;

        // default test
        testSubject = new OperationalEnvironment(operationalEnvironmentId, operationalEnvironmentName,
                operationalEnvironmentType, operationalEnvironmentStatus, tenantContext, workloadContext,
                resourceVersion, relationshipList);
    }
}
