package org.onap.vid.model.asyncInstantiation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;


public class JobAuditStatus {

    public JobAuditStatus(){}


    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source){
        this.jobId = jobId;
        this.jobStatus = jobStatus;
        this.source = source;
        this.isFinal = isFinal();
    }

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source, UUID requestId, String additionalInfo, Boolean isFinal) {
        this(jobId, jobStatus, source);
        this.requestId = requestId;
        this.additionalInfo = additionalInfo;
        this.isFinal = isFinal;
    }

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source, UUID requestId, String additionalInfo, Boolean isFinal, String instanceName) {
        this(jobId, jobStatus, source, requestId, additionalInfo, isFinal);
        this.instanceName = instanceName;
    }

    public JobAuditStatus(String instanceName, String jobStatus, UUID requestId, String additionalInfo, Boolean isFinal, String instanceType) {
        this.instanceType = instanceType;
        this.instanceName = instanceName;
        this.jobStatus = jobStatus;
        this.requestId = requestId;
        this.additionalInfo = additionalInfo;
        this.isFinal = isFinal;
    }

    public JobAuditStatus(UUID requestId, String instanceName, String modelType, String instanceType, String startTime, String finishTime, String jobStatus, String additionalInfo) {
        this.requestId = requestId;
        this.instanceName = instanceName;
        this.modelType = modelType;
        this.instanceType = instanceType;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.jobStatus = jobStatus;
		this.instanceId = instanceId;
        this.additionalInfo = additionalInfo;
    }



    public enum SourceStatus {
        MSO,
        VID
    }

    private UUID jobId;
    private String jobStatus;
    private SourceStatus source;
    private UUID requestId;
    private String additionalInfo;
    private String instanceName;
    private String instanceType;
	private String instanceId;

    public String getInstanceType() {return instanceType;}

    public void setInstanceType(String instanceType) {this.instanceType = instanceType; }

    private Boolean isFinal;


	public String getInstanceId() {
		return instanceId;
	}
	
	public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public UUID getJobId() {
        return jobId;
    }

    public SourceStatus getSource() {
        return source;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public Boolean isFinal(){
        return isFinal;
    }

    public void setFinal(Boolean aFinal) {
        isFinal = aFinal;
    }
    private String startTime;
    private String finishTime;

    public void setStartTime(String startTime) {
            this.startTime = startTime;
    }

    public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
    }

    public void setModelType(String modelType) {
            this.modelType = modelType;
    }

    private String modelType;

    public String getStartTime() {
            return startTime;
    }

    public String getFinishTime() {
            return finishTime;
    }

    public String getModelType() {
            return modelType;
    }

    @Override
    public String toString() {
        return "JobAuditStatus{" +
                "jobId=" + jobId +
                ", jobStatus='" + jobStatus + '\'' +
                ", source=" + source +
                ", requestId=" + requestId +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", isFinal=" + isFinal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof JobAuditStatus)) return false;

        JobAuditStatus that = (JobAuditStatus) o;

        return new EqualsBuilder()
                .append(jobId, that.jobId)
                .append(jobStatus, that.jobStatus)
                .append(source, that.source)
                .append(requestId, that.requestId)
                .append(additionalInfo, that.additionalInfo)
                .append(instanceName, that.instanceName)
                .append(modelType, that.modelType)
                //.append(isFinal, that.isFinal)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(jobId)
                .append(jobStatus)
                .append(source)
                .append(requestId)
                .append(additionalInfo)
                .append(instanceName)
                .append(isFinal)
                .toHashCode();
    }
}
