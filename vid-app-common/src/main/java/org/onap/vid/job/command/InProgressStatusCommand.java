/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.joshworks.restclient.http.HttpResponse;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.NextCommand;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InProgressStatusCommand implements JobCommand {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(InProgressStatusCommand.class);

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    private MsoInterface restMso;

    @Inject
    private AuditService auditService;

    private String requestId;

    private UUID jobUuid;

    public InProgressStatusCommand() {
    }

    InProgressStatusCommand(UUID jobUuid, String requestId) {
        init(jobUuid, requestId);
    }

    @Override
    public NextCommand call() {

        try {
            String path = asyncInstantiationBL.getOrchestrationRequestsPath()+"/"+requestId;
            HttpResponse<AsyncRequestStatus> msoResponse = restMso.get(path, AsyncRequestStatus.class);


            JobStatus jobStatus;
            if (msoResponse.getStatus() >= 400 || msoResponse.getBody() == null) {
                auditService.setFailedAuditStatusFromMso(jobUuid, requestId, msoResponse.getStatus(), msoResponse.getBody().toString());
                LOGGER.error(EELFLoggerDelegate.errorLogger,
                        "Failed to get orchestration status for {}. Status code: {},  Body: {}",
                        requestId, msoResponse.getStatus(), msoResponse.getRawBody().toString());
                return new NextCommand(JobStatus.IN_PROGRESS, this);
            }
            else {
                jobStatus = asyncInstantiationBL.calcStatus(msoResponse.getBody());
            }

            asyncInstantiationBL.auditMsoStatus(jobUuid,msoResponse.getBody().request);


            if (jobStatus == JobStatus.FAILED) {
                asyncInstantiationBL.handleFailedInstantiation(jobUuid);
            }
            else {
                asyncInstantiationBL.updateServiceInfoAndAuditStatus(jobUuid, jobStatus);
            }
            //in case of JobStatus.PAUSE we leave the job itself as IN_PROGRESS, for keep tracking job progress
            if (jobStatus == JobStatus.PAUSE) {
                return new NextCommand(JobStatus.IN_PROGRESS, this);
            }
            return new NextCommand(jobStatus, this);
        } catch (javax.ws.rs.ProcessingException e) {
            // Retry when we can't connect MSO during getStatus
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Cannot get orchestration status for {}, will retry: {}", requestId, e, e);
            return new NextCommand(JobStatus.IN_PROGRESS, this);
        } catch (RuntimeException e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Cannot get orchestration status for {}, stopping: {}", requestId, e, e);
            return new NextCommand(JobStatus.STOPPED, this);
        }
    }

    @Override
    public InProgressStatusCommand init(UUID jobUuid, Map<String, Object> data) {
        return init(jobUuid, (String) data.get("requestId"));
    }

    private InProgressStatusCommand init(UUID jobUuid, String requestId) {
        this.requestId = requestId;
        this.jobUuid = jobUuid;
        return this;
    }

    @Override
    public Map<String, Object> getData() {
        return ImmutableMap.of("requestId", requestId);
    }


}
