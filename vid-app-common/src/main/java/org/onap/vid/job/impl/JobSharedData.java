package org.onap.vid.job.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.onap.vid.job.JobAdapter;

import java.util.Objects;
import java.util.UUID;

public class JobSharedData {

    protected UUID jobUuid;
    protected String userId;
    protected Class requestType;
    protected UUID rootJobId;

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, property="class")
    protected JobAdapter.AsyncJobRequest request;

    public JobSharedData() {
    }

    public JobSharedData(UUID jobUuid, String userId, JobAdapter.AsyncJobRequest request) {
        this.jobUuid = jobUuid;
        this.userId = userId;
        this.requestType = request.getClass();
        this.request = request;
        this.rootJobId = jobUuid;
    }

    public JobSharedData(UUID jobUuid, JobAdapter.AsyncJobRequest request, JobSharedData parentData) {
        this(jobUuid, parentData.getUserId(), request);
        rootJobId = parentData.getRootJobId() != null ? parentData.getRootJobId() : parentData.getJobUuid();
    }


    public UUID getJobUuid() {
        return jobUuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Class getRequestType() {
        return requestType;
    }

    public void setRequestType(Class requestType) {
        this.requestType = requestType;
    }

    public JobAdapter.AsyncJobRequest getRequest() {
        return request;
    }

    public void setRequest(JobAdapter.AsyncJobRequest request) {
        this.request = request;
    }

    public UUID getRootJobId() {
        return rootJobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobSharedData)) return false;
        JobSharedData that = (JobSharedData) o;
        return Objects.equals(getJobUuid(), that.getJobUuid()) &&
                Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getRequestType(), that.getRequestType()) &&
                Objects.equals(getRootJobId(), that.getRootJobId()) &&
                Objects.equals(getRequest(), that.getRequest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobUuid(), getUserId(), getRequestType(), getRootJobId(), getRequest());
    }
}
