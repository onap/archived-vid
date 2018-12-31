package org.onap.vid.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.Job.JobStatus;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/*
 The following 2 annotations let hibernate to update only fields that actually have been changed.
 DynamicUpdate tell hibernate to update only dirty fields.
 SelectBeforeUpdate is needed since during update the entity is detached (get and update are in different sessions)
 */
@DynamicUpdate()
@SelectBeforeUpdate()
@Entity
@Table(name = "vid_job_audit_status")
public class JobAuditStatus extends VidBaseEntity {

    public static final int MAX_ADDITIONAL_INFO_LENGTH = 2000;
    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(JobAuditStatus.class);
    private static final String defaultFormat = "E, dd MMM yyyy HH:mm:ss z";

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
        setAdditionalInfo(additionalInfo);
    }

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source, UUID requestId, String additionalInfo, Date date){
        this(jobId, jobStatus, source, requestId, additionalInfo);
        this.created = date;
    }

    public JobAuditStatus(String instanceName, String jobStatus, UUID requestId, String additionalInfo) {
        this.instanceName = instanceName;
        this.jobStatus = jobStatus;
        this.requestId = requestId;
        this.additionalInfo = additionalInfo;

    }

    public JobAuditStatus(String instanceName, String jobStatus, UUID requestId, String additionalInfo, String date, String instanceType) {
       this(instanceName, jobStatus, requestId, additionalInfo);
       this.created = dateStringToDate(date);
       this.instanceType = instanceType;
    }


    private Date dateStringToDate(String dateAsString){
        if (StringUtils.isEmpty(dateAsString)) {
            return null;
        }

        DateFormat format = new SimpleDateFormat(defaultFormat);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null ;
        try {
            date = format.parse(dateAsString);
        } catch (ParseException e) {
            logger.error("There was an error to parse the string "+ dateAsString +" to date ", e.getMessage());
        }
        return date;
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
    private String instanceName;
    private String instanceType;
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

    @Column(name = "ADDITIONAL_INFO", columnDefinition = "VARCHAR(2000)")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = StringUtils.substring(additionalInfo, 0, MAX_ADDITIONAL_INFO_LENGTH);
    }

    @Transient
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Transient
    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
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
