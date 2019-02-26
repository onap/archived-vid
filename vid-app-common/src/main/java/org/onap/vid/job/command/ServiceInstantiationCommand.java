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
import org.apache.commons.lang3.ObjectUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.exceptions.MaxRetriesException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;

import javax.inject.Inject;
import java.util.Map;


public abstract class ServiceInstantiationCommand extends BaseRootCommand implements JobCommand {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(ServiceInstantiationCommand.class);

    @Inject
    protected AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    private RestMsoImplementation restMso;

    protected String optimisticUniqueServiceInstanceName;

    public ServiceInstantiationCommand() {
    }

    @Override
    public NextCommand call() {
        RequestDetailsWrapper<ServiceInstantiationRequestDetails> requestDetailsWrapper ;
        try {
            requestDetailsWrapper = generateServiceInstantiationRequest();
        }

        //Aai return bad response while checking names uniqueness
        catch (ExceptionWithRequestInfo exception) {
            return handleAaiNameUniquenessBadResponse(exception);
        }

        //Vid reached to max retries while trying to find unique name in AAI
        catch (MaxRetriesException exception) {
            return handleMaxRetryInNameUniqueness(exception);
        }

        String path = asyncInstantiationBL.getServiceInstantiationPath(getRequest());

        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetailsWrapper,
                path, RequestReferencesContainer.class);

        return handleRootResponse(msoResponse);

    }

    @Override
    protected ServiceInstantiation getRequest() {
        return (ServiceInstantiation) getSharedData().getRequest();
    }

    protected abstract RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateServiceInstantiationRequest();

    private NextCommand handleMaxRetryInNameUniqueness(MaxRetriesException exception) {
        LOGGER.error("Failed to find unused name in AAI. Set the job to FAILED ", exception);
        return handleCommandFailed();
    }

    private NextCommand handleAaiNameUniquenessBadResponse(ExceptionWithRequestInfo exception) {
        LOGGER.error("Failed to check name uniqueness in AAI. VID will try again later", exception);
        //put the job in_progress so we will keep trying to check name uniqueness in AAI
        //And then send the request to MSO
        return new NextCommand(Job.JobStatus.IN_PROGRESS, this);
    }

    @Override
    public ServiceInstantiationCommand init(JobSharedData sharedData, Map<String, Object> commandData) {

        return init(
                sharedData,
                (String) commandData.get("optimisticUniqueServiceInstanceName")
        );
    }

    protected ServiceInstantiationCommand init(JobSharedData sharedData, String optimisticUniqueServiceInstanceName) {
        init(sharedData);
        this.optimisticUniqueServiceInstanceName = ObjectUtils.defaultIfNull(optimisticUniqueServiceInstanceName,
                (getRequest()).getInstanceName());
        return this;
    }

    @Override
    public Map<String, Object> getData() {
        return ImmutableMap.of(
                "optimisticUniqueServiceInstanceName", optimisticUniqueServiceInstanceName
        );
    }
}
