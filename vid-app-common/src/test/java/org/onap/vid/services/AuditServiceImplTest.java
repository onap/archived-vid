/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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
package org.onap.vid.services;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.AsyncRequestStatusList;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AuditServiceImplTest {

  @Mock
  private RestMsoImplementation restMso;
  @Mock
  private AsyncInstantiationRepository asyncInstantiationRepository;

  @InjectMocks
  private AuditServiceImpl auditService;

  @BeforeMethod
  public void setUp() {
    restMso = null;
    asyncInstantiationRepository = null;
    auditService = null;
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetRequestsIdsByServiceIdAndRequestTypeAndScope() throws Exception {

    String instanceId = "d40c8a82-cc04-45e5-a0f6-0c9394c8f8d2";
    //the request id in multipleOrchestrationRequestsServiceInstance.json
    String expectedRequestId = "fab854bf-e53c-415e-b3cc-b6fcce8414b2";
    String msoBasePath = "/someMsoPath/v2019?";

    AsyncRequestStatusList asyncRequestStatusList = TestUtils.readJsonResourceFileAsObject(
        "/responses/mso/multipleOrchestrationRequestsServiceInstance.json",
        AsyncRequestStatusList.class);
    RestObject<AsyncRequestStatusList> msoResponse = new RestObject<>();
    msoResponse.set(asyncRequestStatusList);
    msoResponse.setStatusCode(200);
    when(restMso.GetForObject(eq(msoBasePath + "filter=serviceInstanceId:EQUALS:" + instanceId),
        eq(AsyncRequestStatusList.class)))
        .thenReturn(msoResponse);
    TestUtils.testWithSystemProperty("mso.restapi.get.orc.reqs", msoBasePath, () -> {
      List<AsyncRequestStatus.Request> result = auditService
          .retrieveRequestsFromMsoByServiceIdAndRequestTypeAndScope(instanceId, "createInstance", "service");
      assertThat(result.size(), equalTo(1));
      assertThat(result.get(0).requestId, equalTo(expectedRequestId));
      assertThat(result.get(0).startTime, equalTo("Mon, 04 Mar 2019 20:47:15 GMT"));
    });
  }

  @Test
  public void nextOrdinalAfter_givenNull_returnZero() {
    assertThat(
        auditService.nextOrdinalAfter(null),
        equalTo(0)
    );
  }

  @Test
  public void nextOrdinalAfter_givenX_returnXplus1() {
    final int x = 6;
    final JobAuditStatus jobAuditStatus = new JobAuditStatus();
    jobAuditStatus.setOrdinal(x);

    assertThat(
        auditService.nextOrdinalAfter(jobAuditStatus),
        equalTo(x + 1)
    );
  }

}
