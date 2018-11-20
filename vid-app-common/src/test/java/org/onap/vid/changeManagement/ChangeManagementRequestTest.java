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
import java.util.Map;

import org.junit.Test;

public class ChangeManagementRequestTest {

    private ChangeManagementRequest createTestSubject() {
        return new ChangeManagementRequest();
    }

    @Test
    public void testGetRequestDetails() throws Exception {
        ChangeManagementRequest testSubject;
        List<RequestDetails> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestDetails();
    }

    @Test
    public void testSetRequestDetails() throws Exception {
        ChangeManagementRequest testSubject;
        List<RequestDetails> requestDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestDetails(requestDetails);
    }

    @Test
    public void testGetRequestType() throws Exception {
        ChangeManagementRequest testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestType();
    }

    @Test
    public void testSetRequestType() throws Exception {
        ChangeManagementRequest testSubject;
        String requestType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestType(requestType);
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        ChangeManagementRequest testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        ChangeManagementRequest testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }

    @Test
    public void testEquals() {
        new EqualsTester()
            .addEqualityGroup(new ChangeManagementRequest(), new ChangeManagementRequest())
            .addEqualityGroup(createRequest("type-1", "name-1", new String("txt")),
                createRequest("type-1", "name-1", new String("txt")))
            .addEqualityGroup(createRequest("type-2", "name-2", new String("txt")))
            .testEquals();
    }

    private ChangeManagementRequest createRequest(String type, String name, Object value) {

        RelatedInstanceList relatedInstanceList1 = createRelatedInstanceList(createRelatedInstance("instance-1"));
        RelatedInstanceList relatedInstanceList2 = createRelatedInstanceList(createRelatedInstance("instance-2"));
        List<RelatedInstanceList> relatedInstanceListList = ImmutableList.of(relatedInstanceList1, relatedInstanceList2);
        RequestDetails requestDetails = createRequestDetails("vnf-instance-id-1", "vnf-name-1", relatedInstanceListList);
        List<RequestDetails> details = ImmutableList.of(requestDetails);

        ChangeManagementRequest request = new ChangeManagementRequest();
        request.setRequestDetails(details);
        request.setRequestType(type);
        request.setAdditionalProperty(name, value);
        return request;
    }

    private RequestDetails createRequestDetails(String vnfInstanceId, String vnfName, List<RelatedInstanceList> list) {
        RequestDetails details = new RequestDetails();
        details.setVnfInstanceId(vnfInstanceId);
        details.setVnfName(vnfName);
        details.setRelatedInstList(list);
        return details;
    }

    private RelatedInstanceList createRelatedInstanceList(RelatedInstance instance) {
        RelatedInstanceList relatedInstanceList = new RelatedInstanceList();
        relatedInstanceList.setRelatedInstance(instance);
        return relatedInstanceList;
    }

    private RelatedInstance createRelatedInstance(String instanceId) {
        RelatedInstance relatedInstance = new RelatedInstance();
        relatedInstance.setInstanceId(instanceId);
        return relatedInstance;
    }


}