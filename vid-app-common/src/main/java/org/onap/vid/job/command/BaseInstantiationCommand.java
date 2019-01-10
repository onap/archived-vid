package org.onap.vid.job.command;

import org.onap.vid.job.impl.JobSharedData;

import java.util.Map;


public abstract class BaseInstantiationCommand extends CommandBase{


    protected CommandParentData commandParentData = new CommandParentData();

    protected BaseInstantiationCommand init(JobSharedData sharedData, Map<String, Object> commandData) {
        super.init(sharedData);
        commandParentData.initParentData(commandData);
        return this;
    }
}
