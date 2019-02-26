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

import org.apache.commons.collections.MapUtils;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.JobType;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.properties.Features;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServiceInProgressStatusCommand extends BaseInProgressStatusCommand {

    public ServiceInProgressStatusCommand() {
    }

    ServiceInProgressStatusCommand(JobSharedData sharedData, MsoResourceIds msoResourceIds) {
        init(sharedData, msoResourceIds.getRequestId(), msoResourceIds.getInstanceId());
    }

    @Override
    protected ExpiryChecker getExpiryChecker() {
        return new ServiceExpiryChecker();
    }

    protected NextCommand processJobStatus(Job.JobStatus jobStatus) {
        JobCommand jobCommand = this;
        Job.JobStatus nextJobStatus = jobStatus;
        switch (jobStatus) {
            case FAILED:
                asyncInstantiationBL.handleFailedInstantiation(getSharedData().getJobUuid());
                return new NextCommand(nextJobStatus, jobCommand);
            case PAUSE:
                nextJobStatus = Job.JobStatus.IN_PROGRESS;
                break;
            case COMPLETED:
                ServiceInstantiation request = (ServiceInstantiation) getSharedData().getRequest();
                if (isNeedToCreateChildJobs(request)) {
                    List<String> childrenJobs = getChildJobs(request);
                    nextJobStatus = Job.JobStatus.IN_PROGRESS;
                    jobCommand = new WatchingCommand(getSharedData(), childrenJobs, true);
                    return new NextCommand(nextJobStatus, jobCommand);
                }
                break;
                default: // for sonar
        }
        asyncInstantiationBL.updateServiceInfoAndAuditStatus(getSharedData().getJobUuid(), jobStatus);
        return new NextCommand(nextJobStatus, jobCommand);
    }

    private List<String> getChildJobs(ServiceInstantiation request) {
        Map<String, Object> dataForChild = buildDataForChild(request);

        Stream<String> vnfJobs = request.getVnfs().values().stream().map(
                vnf -> jobsBrokerService.add(
                        jobAdapter.createChildJob(JobType.VnfInstantiation, JobStatus.CREATING , vnf, getSharedData(), dataForChild)).toString()
        );

        Stream<String> networkJobs = request.getNetworks().values().stream().map(
                network -> jobsBrokerService.add(
                        jobAdapter.createChildJob(JobType.NetworkInstantiation, JobStatus.CREATING , network, getSharedData(), dataForChild)).toString()
        );

        Stream<String> instanceGroupJobs = request.getVnfGroups().values().stream().map(
                instanceGroup -> jobsBrokerService.add(
                        jobAdapter.createChildJob(JobType.InstanceGroupInstantiation, JobStatus.CREATING , instanceGroup, getSharedData(), dataForChild)).toString()
        );

        return Stream.of(vnfJobs, networkJobs, instanceGroupJobs)
                .reduce(Stream::concat)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }

    public boolean isNeedToCreateChildJobs(ServiceInstantiation request) {
        return featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VNF) && request.isALaCarte() &&
                    (
                            MapUtils.isNotEmpty(request.getVnfs()) || MapUtils.isNotEmpty(request.getNetworks()) ||
                            (featureManager.isActive(Features.FLAG_1902_VNF_GROUPING) && MapUtils.isNotEmpty(request.getVnfGroups()))
                    );
    }

    protected Map<String, Object> buildDataForChild(ServiceInstantiation request) {
        commandParentData.addInstanceId(CommandDataKey.SERVICE_INSTANCE_ID, this.instanceId);
        commandParentData.addModelInfo(CommandDataKey.SERVICE_MODEL_INFO, request.getModelInfo());
        return commandParentData.getParentData();
    }
}
