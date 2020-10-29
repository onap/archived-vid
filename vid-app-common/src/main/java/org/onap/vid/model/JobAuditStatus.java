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

package org.onap.vid.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.Job.JobStatus;

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

    private JobAuditStatus(UUID jobId, String instanceName, String instanceType, String jobStatus,
                           SourceStatus source, UUID requestId, String additionalInfo, Date date, int ordinal) {
        this.jobId = jobId;
        this.instanceName = instanceName;
        this.instanceType = instanceType;
        this.jobStatus = jobStatus;
        this.source = source;
        this.requestId = requestId;
        setAdditionalInfo(additionalInfo);
        this.ordinal = ordinal;
        this.created = date;
    }

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source){
        this(jobId, null, null, jobStatus, source, null, null, null, 0);
    }

    public JobAuditStatus(UUID jobId, String jobStatus, SourceStatus source, UUID requestId, String additionalInfo) {
        this(jobId, null, null, jobStatus, source, requestId, additionalInfo, null, 0);
    }

    public JobAuditStatus(String instanceName, String jobStatus, UUID requestId, String additionalInfo, String date, String instanceType) {
        this(null, instanceName, instanceType, jobStatus, null, requestId, additionalInfo, null, 0);
        this.created = dateStringToDate(date);
    }

    public static JobAuditStatus createForTest(UUID jobId, String jobStatus, SourceStatus source, Date date, int ordinal) {
        return new JobAuditStatus(jobId, null, null, jobStatus, source, null, null, date, ordinal);
    }

    public static JobAuditStatus createForTest(UUID jobId, String jobStatus, SourceStatus source, UUID requestId, String additionalInfo, Date date) {
        return new JobAuditStatus(jobId, null, null, jobStatus, source, requestId, additionalInfo, date, 0);
    }

    private Date dateStringToDate(String dateAsString){
        if (StringUtils.isEmpty(dateAsString)) {
            return null;
        }

        DateFormat format = new SimpleDateFormat(defaultFormat, Locale.US);
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
                .append(modelType, that.modelType)
                // ordinal is not part of equality (similarly to "created" field)
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
                
                // ordinal is not part of equality (similarly to "created" field)
                .toHashCode();
    }

    public enum SourceStatus {
        MSO,
        VID
    }

    public enum ResourceTypeFilter {
        SERVICE("serviceInstanceId"),
        VNF("vnfInstanceId"),
        VFMODULE("vfModuleInstanceId"),
        NETWORK("networkInstanceId"),
        VNFGROUP("instanceGroupId");

        private final String filterBy;

        ResourceTypeFilter(String filterBy) {
            this.filterBy = filterBy;
        }

        public String getFilterBy() {
            return filterBy;
        }
    }

    public JobAuditStatus(UUID requestId, String instanceName,
                String modelType, String instanceType, String startTime,
                String finishTime, String jobStatus, String instanceId, String additionalInfo) {
         this.requestId = requestId;
         this.instanceName = instanceName;
         this.modelType = modelType;
         this.instanceType = instanceType;

         this.startTime = startTime;
         this.finishTime = finishTime;
		 
		 this.instanceId = instanceId;
         this.jobStatus = jobStatus;
         this.additionalInfo = additionalInfo;
         this.created = dateStringToDate(finishTime);
    }
    private String modelType;
    private String startTime;
    private String finishTime;
	
	 @Transient
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    private String instanceId;

    @Transient
    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    @Transient
    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    @Transient
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    private UUID jobId;
    private String instanceName;
    private String instanceType;
    private String jobStatus;
    private SourceStatus source;
    private UUID requestId;
    private String additionalInfo;
    private int ordinal;

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

    @Column(name = "ORDINAL", columnDefinition = "INT")
    public int getOrdinal() {
        // Ordinal allows sorting audit statuses by
        // insertion order, regardless of "created"
        // field
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
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
