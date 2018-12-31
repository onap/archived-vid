package org.onap.vid.job.command;

import com.google.common.collect.ImmutableMap;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.job.*;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WatchingCommandBaseModule extends BaseWatchingCommand {
    @Inject
    protected JobsBrokerService jobsBrokerService;

    @Inject
    protected JobAdapter jobAdapter;
    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(WatchingCommandBaseModule.class);

    public WatchingCommandBaseModule(
            JobSharedData sharedData,
            List<String> childrenJobsIds,
            boolean isService,
            CommandParentData commandParentData) {
       super(sharedData, childrenJobsIds, isService);
        this.commandParentData = commandParentData;
    }

    public WatchingCommandBaseModule() {

    }

    @Override
    protected NextCommand getNextCommand(Job.JobStatus cumulativeJobsStatus) {

        if (cumulativeJobsStatus== Job.JobStatus.IN_PROGRESS) {
            return new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, this);
        }

        if(cumulativeJobsStatus==Job.JobStatus.FAILED || cumulativeJobsStatus==Job.JobStatus.COMPLETED_WITH_ERRORS){
            return new NextCommand(Job.JobStatus.COMPLETED_WITH_ERRORS);
        }
        Vnf request = (Vnf) getSharedData().getRequest();
        Map<String, Object> dataForChild = buildDataForChild();
        //Create non-base Volume groups job
        List<VfModule> vfModules = request.getVfModules().values().stream().flatMap(vfKey -> vfKey.values().stream()).collect(Collectors.toList());
        List<String> vgNonBaseJobs = new ArrayList<>();
        for( VfModule vfModule : vfModules){
            try {
                if(!commandUtils.isVfModuleBaseModule(commandParentData.getModelInfo(CommandDataKey.SERVICE_MODEL_INFO).getModelVersionId(), vfModule.getModelInfo().getModelVersionId())) {
                    vgNonBaseJobs.add(jobsBrokerService.add(
                            jobAdapter.createChildJob(JobType.VolumeGroupInstantiation, Job.JobStatus.CREATING, vfModule, getSharedData(), dataForChild)).toString());
                }
            } catch (AsdcCatalogException e) {
                LOG.error("Failed to retrieve service definitions from SDC, for VfModule is BaseModule. Error: "+e.getMessage() , e);
                return new NextCommand(Job.JobStatus.COMPLETED_WITH_ERRORS);
            }
        }
        return new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, new WatchingCommand(getSharedData(), vgNonBaseJobs, false));
    }

    @Override
    public WatchingCommandBaseModule init(JobSharedData sharedData, Map<String, Object> commandData) {
        super.init(sharedData, commandData);
        commandParentData.initParentData(commandData);
        return this;
    }

    protected Map<String, Object> buildDataForChild() {
        return commandParentData.getParentData();
    }

    @Override
    public Map<String, Object> getData() {
        return ImmutableMap.<String, Object>builder()
                .putAll(super.getData())
                .putAll(commandParentData.getParentData())
                .build();
    }


}
