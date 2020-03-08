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

package org.onap.vid.job.command;

import static org.onap.vid.utils.TimeUtils.parseZonedDateTime;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.Job;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.properties.Features;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.togglz.core.manager.FeatureManager;

@Service
public class InProgressStatusService {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(InProgressStatusService.class);

    private final AsyncInstantiationBusinessLogic asyncInstantiationBL;

    private final RestMsoImplementation restMso;

    private final AuditService auditService;

    private final FeatureManager featureManager;

    @Autowired
    public InProgressStatusService(AsyncInstantiationBusinessLogic asyncInstantiationBL, RestMsoImplementation restMso, AuditService auditService, FeatureManager featureManager) {
        this.asyncInstantiationBL = asyncInstantiationBL;
        this.restMso = restMso;
        this.auditService = auditService;
        this.featureManager = featureManager;
    }


    public Job.JobStatus call(ExpiryChecker expiryChecker, JobSharedData sharedData, String requestId) {

        RestObject<AsyncRequestStatus> asyncRequestStatus = getAsyncRequestStatus(requestId);
        auditService.auditMsoStatus(sharedData.getRootJobId(), asyncRequestStatus.get().request);
        Job.JobStatus jobStatus = asyncInstantiationBL.calcStatus(asyncRequestStatus.get());
        ZonedDateTime jobStartTime = getZonedDateTime(asyncRequestStatus, requestId);
        jobStatus = expiryChecker.isExpired(jobStartTime) ? Job.JobStatus.FAILED : jobStatus;
        asyncInstantiationBL.updateResourceInfo(sharedData, jobStatus, asyncRequestStatus.get());
        return jobStatus;
    }

    RestObject<AsyncRequestStatus> getAsyncRequestStatus(String requestId) {
        String path = asyncInstantiationBL.getOrchestrationRequestsPath() + "/" + requestId +
                (featureManager.isActive(Features.FLAG_1908_RESUME_MACRO_SERVICE) ? "?format=detail" : "");
        RestObject<AsyncRequestStatus> msoResponse = restMso.GetForObject(path, AsyncRequestStatus.class);

        if (msoResponse.getStatusCode() >= 400 || msoResponse.get() == null) {
            throw new BadResponseFromMso(msoResponse);
        }

        return msoResponse;
    }

    public void handleFailedMsoResponse(UUID jobUUID, String requestId, RestObject<AsyncRequestStatus> msoResponse) {
        auditService.setFailedAuditStatusFromMso(jobUUID, requestId, msoResponse.getStatusCode(), msoResponse.getRaw());
        LOGGER.error("Failed to get orchestration status for {}. Status code: {},  Body: {}",
                requestId, msoResponse.getStatusCode(), msoResponse.getRaw());
    }

    public static class BadResponseFromMso extends RuntimeException {
        private final RestObject<AsyncRequestStatus> msoResponse;

        public BadResponseFromMso(RestObject<AsyncRequestStatus> msoResponse) {
            this.msoResponse = msoResponse;
        }

        public RestObject<AsyncRequestStatus> getMsoResponse() {
            return msoResponse;
        }
    }

    private ZonedDateTime getZonedDateTime(RestObject<AsyncRequestStatus> asyncRequestStatusResponse, String requestId) {
        ZonedDateTime jobStartTime;
        try {
            jobStartTime = parseZonedDateTime(asyncRequestStatusResponse.get().request.startTime);
        } catch (DateTimeParseException | NullPointerException e) {
            LOGGER.error("Failed to parse start time for {}, body: {}. Current time will be used", requestId, asyncRequestStatusResponse.getRaw(), e);
            jobStartTime = ZonedDateTime.now();
        }
        return jobStartTime;
    }
}
