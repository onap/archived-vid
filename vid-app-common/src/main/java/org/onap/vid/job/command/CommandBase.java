package org.onap.vid.job.command;

import org.onap.vid.job.impl.JobSharedData;

import javax.inject.Inject;


public abstract class CommandBase  {

    @Inject
    protected CommandUtils commandUtils;

    private JobSharedData sharedData;

    protected CommandBase init(JobSharedData sharedData) {
        this.setSharedData(sharedData);
        return this;
    }

    public JobSharedData getSharedData() {
        return sharedData;
    }

    private void setSharedData(JobSharedData sharedData) {
        this.sharedData = sharedData;
    }
}
