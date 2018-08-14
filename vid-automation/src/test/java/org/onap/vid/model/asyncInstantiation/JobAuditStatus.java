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



    public enum SourceStatus {
        MSO,
        VID
    }

    private UUID jobId;
    private String jobStatus;
    private SourceStatus source;
    private UUID requestId;
    private String additionalInfo;



    private Boolean isFinal;

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



    public Boolean isFinal(){
        return isFinal;
    }

    public void setFinal(Boolean aFinal) {
        isFinal = aFinal;
    }

    @Override
    public String toString() {
        return "JobAuditStatus{" +
                "jobId=" + jobId +
                ", jobStatus='" + jobStatus + '\'' +
                ", source=" + source +
                ", requestId=" + requestId +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", isFinal=" + isFinal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JobAuditStatus that = (JobAuditStatus) o;

        return new EqualsBuilder()
                .append(jobId, that.jobId)
                .append(jobStatus, that.jobStatus)
                .append(source, that.source)
                .append(requestId, that.requestId)
                .append(additionalInfo, that.additionalInfo)
                .append(isFinal, that.isFinal)
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
                .append(isFinal)
                .toHashCode();
    }





}
