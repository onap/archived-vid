/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.UUID;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
}
