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

import com.google.common.collect.ImmutableMap;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.*;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.togglz.core.manager.FeatureManager;

import javax.inject.Inject;
import java.util.Map;

public abstract class BaseInProgressStatusCommand extends BaseInstantiationCommand implements JobCommand {
    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(BaseInProgressStatusCommand.class);

    @Inject
    protected AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    protected JobsBrokerService jobsBrokerService;

    @Inject
    protected JobAdapter jobAdapter;

    @Inject
    protected RestMsoImplementation restMso;

    @Inject
    protected FeatureManager featureManager;

    @Inject
    protected InProgressStatusService inProgressStatusService;


    protected String requestId;

    protected String instanceId;


    @Override
    public NextCommand call() {

        try {
            Job.JobStatus jobStatus =  inProgressStatusService.call(getExpiryChecker(), getSharedData(), requestId);
            return processJobStatus(jobStatus);
        } catch (javax.ws.rs.ProcessingException e) {
            // Retry when we can't connect MSO during getStatus
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Cannot get orchestration status for {}, will retry: {}", requestId, e, e);
            return new NextCommand(Job.JobStatus.IN_PROGRESS, this);
        } catch (InProgressStatusService.BadResponseFromMso e) {
            return handleFailedMsoResponse(e.getMsoResponse());
        }
        catch (RuntimeException e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Cannot get orchestration status for {}, stopping: {}", requestId, e, e);
            return new NextCommand(Job.JobStatus.STOPPED, this);
        }
    }

    protected abstract ExpiryChecker getExpiryChecker();

    abstract NextCommand processJobStatus(Job.JobStatus jobStatus);

    private NextCommand handleFailedMsoResponse(RestObject<AsyncRequestStatus> msoResponse) {
        inProgressStatusService.handleFailedMsoResponse(getSharedData().getJobUuid(), requestId, msoResponse);
        return new NextCommand(Job.JobStatus.IN_PROGRESS, this);
    }

    @Override
    public BaseInProgressStatusCommand init(JobSharedData sharedData, Map<String, Object> commandData) {
        return init(sharedData, (String) commandData.get("requestId"), (String) commandData.get("instanceId"));
    }


    protected BaseInProgressStatusCommand init(JobSharedData sharedData,
                                               String requestId,
                                               String instanceId) {
        init(sharedData);
        this.requestId = requestId;
        this.instanceId = instanceId;
        return this;
    }

    @Override
    public Map<String, Object> getData() {
        return ImmutableMap.of(
            "requestId", requestId,
            "instanceId", instanceId
        );
    }


}
