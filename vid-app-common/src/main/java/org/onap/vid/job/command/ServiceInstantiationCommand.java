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
import org.onap.vid.aai.exceptions.InvalidAAIResponseException;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.exceptions.MaxRetriesException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.NextCommand;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
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
public class ServiceInstantiationCommand implements JobCommand {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(ServiceInstantiationCommand.class);

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    private AuditService auditService;

    @Inject
    private MsoInterface restMso;

    private UUID uuid;
    private ServiceInstantiation serviceInstantiationRequest;
    private String userId;

    public ServiceInstantiationCommand() {
    }

    public ServiceInstantiationCommand(UUID uuid, ServiceInstantiation serviceInstantiationRequest, String userId) {
        init(uuid, serviceInstantiationRequest, userId);
    }

    @Override
    public NextCommand call() {
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> requestDetailsWrapper ;
        try {
            requestDetailsWrapper = asyncInstantiationBL.generateServiceInstantiationRequest(
                    uuid, serviceInstantiationRequest, userId
            );
        }

        //Aai return bad response while checking names uniqueness
        catch (InvalidAAIResponseException exception) {
            LOGGER.error("Failed to check name uniqueness in AAI. VID will try again later", exception);
            //put the job in_progress so we will keep trying to check name uniqueness in AAI
            //And then send the request to MSO
            return new NextCommand(Job.JobStatus.IN_PROGRESS, this);
        }

        //Vid reached to max retries while trying to find unique name in AAI
        catch (MaxRetriesException exception) {
            LOGGER.error("Failed to find unused name in AAI. Set the job to FAILED ", exception);
            return handleCommandFailed();
        }

        String path = asyncInstantiationBL.getServiceInstantiationPath(serviceInstantiationRequest);

        HttpResponse<RequestReferencesContainer> msoResponse = restMso.post(path,
            requestDetailsWrapper, RequestReferencesContainer.class);


        if (msoResponse.getStatus() >= 200 && msoResponse.getStatus() < 400) {
            final Job.JobStatus jobStatus = Job.JobStatus.IN_PROGRESS;
            final String requestId = msoResponse.getBody().getRequestReferences().getRequestId();
            final String instanceId = msoResponse.getBody().getRequestReferences().getInstanceId();
            asyncInstantiationBL.auditVidStatus(uuid, jobStatus);
            setInitialRequestAuditStatusFromMso(requestId);
            asyncInstantiationBL.updateServiceInfo(uuid, x-> {
                x.setJobStatus(jobStatus);
                x.setServiceInstanceId(instanceId);
            });

            return new NextCommand(jobStatus, new InProgressStatusCommand(uuid, requestId));
        } else {
            auditService.setFailedAuditStatusFromMso(uuid,null, msoResponse.getStatus(),
                msoResponse.getBody().toString());
            return handleCommandFailed();
        }

    }

    private void setInitialRequestAuditStatusFromMso(String requestId){
        final String initialMsoRequestStatus = "REQUESTED";
        asyncInstantiationBL.auditMsoStatus(uuid,initialMsoRequestStatus,requestId,null);
    }

    protected NextCommand handleCommandFailed() {
        asyncInstantiationBL.handleFailedInstantiation(uuid);
        return new NextCommand(Job.JobStatus.FAILED);
    }

    @Override
    public ServiceInstantiationCommand init(UUID jobUuid, Map<String, Object> data) {
        final Object request = data.get("request");

        return init(
                jobUuid,
                OBJECT_MAPPER.convertValue(request, ServiceInstantiation.class),
                (String) data.get("userId")
        );
    }

    private ServiceInstantiationCommand init(UUID jobUuid, ServiceInstantiation serviceInstantiationRequest, String userId) {
        this.uuid = jobUuid;
        this.serviceInstantiationRequest = serviceInstantiationRequest;
        this.userId = userId;

        return this;
    }

    @Override
    public Map<String, Object> getData() {
        return ImmutableMap.of(
                "uuid", uuid,
                "request", serviceInstantiationRequest,
                "userId", userId
        );
    }
}
