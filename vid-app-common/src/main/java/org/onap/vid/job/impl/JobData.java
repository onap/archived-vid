package org.onap.vid.job.impl;

import org.onap.vid.job.JobType;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class JobData {

    private TreeMap<JobType, Map<String, Object>> commandData;
    private JobSharedData sharedData;

    public JobData() {
        commandData = new TreeMap<>();
        sharedData = new JobSharedData();
    }

    public JobData(TreeMap<JobType, Map<String, Object>> commandData, JobSharedData sharedData) {
        this.commandData = commandData;
        this.sharedData = sharedData;
    }

    public TreeMap<JobType, Map<String, Object>> getCommandData() {
        return commandData;
    }

    public void setCommandData(TreeMap<JobType, Map<String, Object>> commandData) {
        this.commandData = commandData;
    }

    public JobSharedData getSharedData() {
        return sharedData;
    }

    public void setSharedData(JobSharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobData)) return false;
        JobData jobData = (JobData) o;
        return Objects.equals(getCommandData(), jobData.getCommandData()) &&
                Objects.equals(getSharedData(), jobData.getSharedData());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCommandData(), getSharedData());
    }
}
