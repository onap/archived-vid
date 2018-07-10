package org.onap.vid.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.onap.vid.job.Job.JobStatus;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "vid_job_audit_status")
public class JobAuditStatus extends VidBaseEntity {

    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(JobAuditStatus.class);

    public JobAuditStatus(){}

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source){
        this.jobId = jobId;
        this.jobStatus = jobStatus;
        this.source = source;
    }

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source, Date date){
        this(jobId, jobStatus, source);
        this.created = date;
    }

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source, UUID requestId, String additionalInfo) {
        this(jobId, jobStatus, source);
        this.requestId = requestId;
        this.additionalInfo = additionalInfo;
    }

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source, UUID requestId, String additionalInfo, Date date){
        this(jobId, jobStatus, source, requestId, additionalInfo);
        this.created = date;
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
                .toHashCode();
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    @Column(name = "ID", columnDefinition = "INT(11)")
    public Long getId() {
        return this.id;
    }

    @Column(name = "JOB_ID", columnDefinition = "CHAR(36)")
    @Type(type="org.hibernate.type.UUIDCharType")
    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }


    @Column(name = "JOB_STATUS")
    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "SOURCE")
    public SourceStatus getSource() {
        return source;
    }

    public void setSource(SourceStatus source) {
        this.source = source;
    }

    @Column(name = "REQUEST_ID", columnDefinition = "CHAR(36)")
    @Type(type="org.hibernate.type.UUIDCharType")
    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    @Column(name = "ADDITIONAL_INFO")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Transient
    public Boolean isFinal(){
        try {
            if (getSource() == SourceStatus.VID) {
                return JobStatus.valueOf(getJobStatus()).isFinal();
            }
        }
        catch (IllegalArgumentException e){
            logger.error("JobStatus: " + getJobStatus() + " from vid isn't a value of JobStatus enum" + e.getMessage());
            return false;
        }
        return false;
    }

    @Transient
    public Date getCreatedDate() {
        return getCreated();
    }

}
