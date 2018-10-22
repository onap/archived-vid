/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.job.command;


import io.joshworks.restclient.http.HttpResponse;
import org.mockito.Mock;
import org.onap.vid.job.Job;
import org.onap.vid.job.NextCommand;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.ProcessingException;
import java.util.UUID;


import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InProgressStatusCommandTest {

    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic;

    @Mock
    private MsoInterface msoInterface;

    @Mock
    private AuditService auditService;

    @Mock
    private HttpResponse<AsyncRequestStatus> msoResponse;

    @Mock
    private AsyncRequestStatus asyncRequestStatus;

    @Mock
    private AsyncRequestStatus.Request request;

    private UUID uuid = UUID.randomUUID();

    private InProgressStatusCommand inProgressStatusCommand;

    @BeforeMethod
    public void setUp() {
        initMocks(this);

        inProgressStatusCommand = new InProgressStatusCommand(asyncInstantiationBusinessLogic, msoInterface, auditService, uuid, "sampleRequestId");

        when(asyncInstantiationBusinessLogic.getOrchestrationRequestsPath()).thenReturn("http://localhost:8080/samplePath");
        when(msoInterface.get("http://localhost:8080/samplePath/sampleRequestId", AsyncRequestStatus.class)).thenReturn(msoResponse);
        when(msoResponse.getBody()).thenReturn(asyncRequestStatus);
    }


    @Test
    public void whenSOReturnsErrorShouldSetProperFailureStateAndReturnRetryCommand() {
        when(msoResponse.getStatus()).thenReturn(500);

        NextCommand call = inProgressStatusCommand.call();

        assertThat(call.getStatus()).isEqualTo(Job.JobStatus.IN_PROGRESS);
        assertThat(call.getCommand()).isEqualTo(inProgressStatusCommand);

        verify(auditService).setFailedAuditStatusFromMso(uuid, "sampleRequestId", 500, asyncRequestStatus.toString());
    }

    @Test
    public void shouldProperlyHandleFailedInstantiation() {
        when(msoResponse.getStatus()).thenReturn(200);
        when(asyncInstantiationBusinessLogic.calcStatus(asyncRequestStatus)).thenReturn(Job.JobStatus.FAILED);
        asyncRequestStatus.request = request;

        NextCommand call = inProgressStatusCommand.call();

        assertThat(call.getCommand()).isEqualTo(inProgressStatusCommand);
        assertThat(call.getStatus()).isEqualTo(Job.JobStatus.FAILED);

        verify(asyncInstantiationBusinessLogic).handleFailedInstantiation(uuid);
        verify(asyncInstantiationBusinessLogic).auditMsoStatus(uuid, request);
    }

    @Test
    public void shouldRetryCommandWithPausedState() {
        when(msoResponse.getStatus()).thenReturn(200);
        when(asyncInstantiationBusinessLogic.calcStatus(asyncRequestStatus)).thenReturn(Job.JobStatus.PAUSE);
        asyncRequestStatus.request = request;

        NextCommand call = inProgressStatusCommand.call();

        assertThat(call.getCommand()).isEqualTo(inProgressStatusCommand);
        assertThat(call.getStatus()).isEqualTo(Job.JobStatus.IN_PROGRESS);

        verify(asyncInstantiationBusinessLogic).auditMsoStatus(uuid, request);
        verify(asyncInstantiationBusinessLogic).updateServiceInfoAndAuditStatus(uuid, Job.JobStatus.PAUSE);
    }

    @Test
    public void shouldRetryCommandExitedWithProcessingException() {
        when(msoResponse.getStatus()).thenReturn(200);
        when(asyncInstantiationBusinessLogic.calcStatus(asyncRequestStatus)).thenThrow(new ProcessingException(""));

        NextCommand call = inProgressStatusCommand.call();

        assertThat(call.getCommand()).isEqualTo(inProgressStatusCommand);
        assertThat(call.getStatus()).isEqualTo(Job.JobStatus.IN_PROGRESS);
    }

    @Test
    public void shouldSetStoppedStatusWhenRuntimeExceptionOccurs() {
        when(msoResponse.getStatus()).thenReturn(200);
        when(asyncInstantiationBusinessLogic.calcStatus(asyncRequestStatus)).thenThrow(new RuntimeException());

        NextCommand call = inProgressStatusCommand.call();

        assertThat(call.getCommand()).isEqualTo(inProgressStatusCommand);
        assertThat(call.getStatus()).isEqualTo(Job.JobStatus.STOPPED);
    }
}