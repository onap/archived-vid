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

import org.glassfish.grizzly.http.util.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.mso.rest.AsyncRequestStatusList;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuditServiceImplTest {
  @Mock
  private AsyncInstantiationBusinessLogic asyncInstantiationBL;

  @InjectMocks
  private AuditServiceImpl auditService;

  @BeforeClass
  public void init() {
    initMocks(this);
  }

  @Test
  public void setFailedAuditStatusFromMsoTest() {

    UUID jobUuid = UUID.randomUUID();
    String requestId = "1";
    int statusCode = HttpStatus.OK_200.getStatusCode();
    String msoResponse = "{}";

    auditService.setFailedAuditStatusFromMso(jobUuid, requestId, statusCode, msoResponse);

    verify(asyncInstantiationBL, times(1))
        .auditMsoStatus(
            Mockito.any(UUID.class),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString());
  }

  @Test(enabled = false)
  public void testConvertMsoResponseStatusToJobAuditStatus_missingDateFromMso_shouldNoError() throws IOException {
    final AsyncRequestStatusList asyncRequestStatusList = TestUtils.readJsonResourceFileAsObject("/orchestrationRequestsByServiceInstanceId.json", AsyncRequestStatusList.class);

    AuditServiceImpl auditService = new AuditServiceImpl(null, null);

    final List<JobAuditStatus> jobAuditStatuses = auditService.convertMsoResponseStatusToJobAuditStatus(asyncRequestStatusList.getRequestList(), "foo");

    final List<Date> dates = jobAuditStatuses.stream().map(JobAuditStatus::getCreatedDate).collect(toList());
    final List<String> statuses = jobAuditStatuses.stream().map(JobAuditStatus::getJobStatus).collect(toList());

    assertThat(dates, containsInAnyOrder(notNullValue(), notNullValue(), nullValue()));
    assertThat(statuses, containsInAnyOrder("COMPLETE", "COMPLETE", "IN_PROGRESS"));
  }

}
