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

import java.util.Map;

import org.junit.Test;

public class CloudConfigurationTest {

    private CloudConfiguration createTestSubject() {
        return new CloudConfiguration();
    }

    @Test
    public void testGetLcpCloudRegionId() throws Exception {
        CloudConfiguration testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getLcpCloudRegionId();
    }

    @Test
    public void testSetLcpCloudRegionId() throws Exception {
        CloudConfiguration testSubject;
        String lcpCloudRegionId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setLcpCloudRegionId(lcpCloudRegionId);
    }

    @Test
    public void testGetTenantId() throws Exception {
        CloudConfiguration testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getTenantId();
    }

    @Test
    public void testSetTenantId() throws Exception {
        CloudConfiguration testSubject;
        String tenantId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setTenantId(tenantId);
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        CloudConfiguration testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        CloudConfiguration testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }
}
