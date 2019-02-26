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
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobType;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.properties.Features;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VnfInProgressStatusCommand extends ResourceWithChildrenInProgressCommand {
    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VnfInProgressStatusCommand.class);

    public VnfInProgressStatusCommand(JobSharedData sharedData,
                                           String requestId,
                                           String instanceId,
                                           CommandParentData commandParentData) {
        super(sharedData, requestId, instanceId, commandParentData);
     }

    public VnfInProgressStatusCommand() {
    }

    @Override
    protected NextCommand processJobStatus(Job.JobStatus jobStatus) {
        if (jobStatus == Job.JobStatus.FAILED) {
            return new NextCommand(jobStatus);
        }

        Vnf request = (Vnf) getSharedData().getRequest();

        if (isNeedToCreateChildJobs(jobStatus, request)) {
            commandParentData.addInstanceId(CommandDataKey.VNF_INSTANCE_ID, instanceId);
            commandParentData.addModelInfo(CommandDataKey.VNF_MODEL_INFO, request.getModelInfo());
            //create volume group of base module job
            Map<String, Object> dataForChild = buildDataForChild();
            List<VfModule> vfModules = request.getVfModules().values().stream().flatMap(vfKey -> vfKey.values().stream()).collect(Collectors.toList());
            List<String> vgBaseJobs = new ArrayList<>();
            for( VfModule vfModule : vfModules){
                try {
                    if(commandUtils.isVfModuleBaseModule(commandParentData.getModelInfo(CommandDataKey.SERVICE_MODEL_INFO).getModelVersionId(), vfModule.getModelInfo().getModelVersionId())) {
                        vgBaseJobs.add(jobsBrokerService.add(
                                jobAdapter.createChildJob(JobType.VolumeGroupInstantiation, Job.JobStatus.CREATING, vfModule, getSharedData(), dataForChild)).toString());
                    }
                } catch (AsdcCatalogException e) {
                    LOG.error("Failed to retrieve service definitions from SDC, for VfModule is BaseModule. Error: "+e.getMessage() , e);
                    return new NextCommand(Job.JobStatus.COMPLETED_WITH_ERRORS);
                }
            }
            return new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, new WatchingCommandBaseModule(getSharedData(), vgBaseJobs, false, commandParentData));
        }

        //in case of JobStatus.PAUSE we leave the job itself as IN_PROGRESS, for keep tracking job progress
        if (jobStatus == Job.JobStatus.PAUSE) {
            return new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, this);
        }
        return new NextCommand(jobStatus, this);
    }


    protected boolean isNeedToCreateChildJobs(Job.JobStatus jobStatus, BaseResource request) {
        return  featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VFMODULE) &&
                jobStatus == Job.JobStatus.COMPLETED &&
                MapUtils.isNotEmpty(((Vnf)request).getVfModules());
    }


    @Override
    protected ExpiryChecker getExpiryChecker() {
        return x->false;
    }
}
