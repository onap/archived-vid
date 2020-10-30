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


import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;
import org.onap.portalsdk.core.domain.support.DomainVo;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.utils.DaoUtils.StringToLongMapAttributeConverter;

/*
 The following 2 annotations let hibernate to update only fields that actually have been changed.
 DynamicUpdate tell hibernate to update only dirty fields.
 SelectBeforeUpdate is needed since during update the entity is detached (get and update are in different sessions)
*/
@DynamicUpdate()
@SelectBeforeUpdate()
@Entity
@Table(name = "vid_service_info")
public class ServiceInfo extends DomainVo {

    public enum ServiceAction {
        INSTANTIATE,
        DELETE,
        UPDATE,
        RESUME,
        UPGRADE
    }

    private UUID jobId;
    private UUID templateId;
    private String userId;
    private UUID msoRequestId;
    private boolean aLaCarte;
    private Job.JobStatus jobStatus;
    private Date statusModifiedDate;
    private boolean hidden;
    private boolean pause;
    private boolean retryEnabled;
    private Date deletedAt;
    private String owningEntityId;
    private String owningEntityName;
    private String project;
    private String aicZoneId;
    private String aicZoneName;
    private String tenantId;
    private String tenantName;
    private String regionId;
    private String regionName;
    private String serviceType;
    private String subscriberName;
    private String subscriberId;
    private String serviceInstanceId;
    private String serviceInstanceName;
    private String serviceModelId;
    private String serviceModelName;
    private String serviceModelVersion;
    private Date createdBulkDate;
    private ServiceAction action;
    private Map<String, Long> requestSummary;

    public ServiceInfo(){

    }

    public ServiceInfo(String userId, Boolean aLaCarte, JobStatus jobStatus, boolean pause, UUID jobId,
        UUID templateId,
        String owningEntityId, String owningEntityName, String project, String aicZoneId, String aicZoneName,
        String tenantId, String tenantName, String regionId, String regionName, String serviceType,
        String subscriberName, String subscriberId, String serviceInstanceId, String serviceInstanceName,
        String serviceModelId, String serviceModelName, String serviceModelVersion, Date createdBulkDate,
        ServiceAction action, boolean retryEnabled, Map<String, Long> requestSummary) {
        this.userId = userId;
        this.aLaCarte = aLaCarte;
        this.jobStatus = jobStatus;
        this.jobId = jobId;
        this.templateId = templateId;
        this.pause = pause;
        this.owningEntityId = owningEntityId;
        this.owningEntityName = owningEntityName;
        this.project = project;
        this.aicZoneId = aicZoneId;
        this.aicZoneName = aicZoneName;
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.regionId = regionId;
        this.regionName = regionName;
        this.serviceType = serviceType;
        this.subscriberName = subscriberName;
        this.subscriberId = subscriberId;
        this.serviceInstanceId = serviceInstanceId;
        this.serviceInstanceName = serviceInstanceName;
        this.serviceModelId = serviceModelId;
        this.serviceModelName = serviceModelName;
        this.serviceModelVersion = serviceModelVersion;
        this.createdBulkDate = createdBulkDate;
        this.action = action;
        this.retryEnabled = retryEnabled;
        this.requestSummary = requestSummary;
    }

    @Column(name = "JOB_ID", columnDefinition = "CHAR(36)")
    @Type(type="org.hibernate.type.UUIDCharType")
    public UUID getJobId() {
        return jobId;
    }

    @Column(name = "TEMPLATE_ID", columnDefinition = "CHAR(36)")
    @Type(type="org.hibernate.type.UUIDCharType")
    public UUID getTemplateId() {
        return templateId;
    }

    @Column(name="USER_ID")
    public String getUserId() {
        return userId;
    }

    @Column(name = "MSO_REQUEST_ID", columnDefinition = "CHAR(36)")
    @Type(type="org.hibernate.type.UUIDCharType")
    public UUID getMsoRequestId() {
        return msoRequestId;
    }

    @Column(name="IS_A_LA_CARTE")
    @JsonProperty("aLaCarte")
    public boolean isALaCarte() {
        return aLaCarte;
    }

    @Column(name="JOB_STATUS")
    @Enumerated(EnumType.STRING)
    public Job.JobStatus getJobStatus() {
        return jobStatus;
    }

    @Column(name="STATUS_MODIFIED_DATE")
    public Date getStatusModifiedDate() {
        return statusModifiedDate;
    }

    @Column(name="IS_HIDDEN")
    public boolean isHidden() {
        return hidden;
    }

    @Column(name="IS_PAUSE")
    public boolean isPause() {
        return pause;
    }

    @Column(name="IS_RETRY_ENABLED")
    @JsonProperty("isRetryEnabled")
    public boolean isRetryEnabled() {
        return retryEnabled;
    }

    @Column(name="OWNING_ENTITY_ID")
    public String getOwningEntityId() {
        return owningEntityId;
    }

    @Column(name="OWNING_ENTITY_NAME")
    public String getOwningEntityName() {
        return owningEntityName;
    }

    @Column(name="PROJECT")
    public String getProject() {
        return project;
    }

    @Column(name="AIC_ZONE_ID")
    public String getAicZoneId() {
        return aicZoneId;
    }

    @Column(name="AIC_ZONE_NAME")
    public String getAicZoneName() {
        return aicZoneName;
    }

    @Column(name="TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    @Column(name="TENANT_NAME")
    public String getTenantName() {
        return tenantName;
    }

    @Column(name="REGION_ID")
    public String getRegionId() {
        return regionId;
    }

    @Column(name="REGION_NAME")
    public String getRegionName() {
        return regionName;
    }

    @Column(name="SERVICE_TYPE")
    public String getServiceType() {
        return serviceType;
    }

    @Column(name="SUBSCRIBER_NAME")
    public String getSubscriberName() {
        return subscriberName;
    }

    @Column(name="SUBSCRIBER_ID")
    public String getSubscriberId() {
        return subscriberId;
    }

    @Column(name="SERVICE_INSTANCE_ID")
    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    @Column(name="SERVICE_INSTANCE_NAME")
    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    @Column(name="SERVICE_MODEL_ID")
    public String getServiceModelId() {
        return serviceModelId;
    }

    @Column(name="SERVICE_MODEL_NAME")
    public String getServiceModelName() {
        return serviceModelName;
    }

    @Column(name="SERVICE_MODEL_VERSION")
    public String getServiceModelVersion() {
        return serviceModelVersion;
    }

    @Column(name="CREATED_BULK_DATE")
    public Date getCreatedBulkDate() {
        return createdBulkDate;
    }

    @Column(name="DELETED_AT")
    public Date getDeletedAt() {
        return deletedAt;
    }

    @Column(name="ACTION")
    @Enumerated(EnumType.STRING)
    public ServiceAction getAction() {
        return action;
    }

    @Column(name="REQUEST_SUMMARY")
    @Convert(converter = StringToLongMapAttributeConverter.class)
    public Map<String, Long> getRequestSummary() {
        return requestSummary;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    @Column(name = "ID", columnDefinition = "INT(11)")
    public Long getId() {
        return this.id;
    }

    @Override
    @Column(name = "CREATED_DATE")
    public Date getCreated() {
        return super.getCreated();
    }

    @Override
    @Column(name = "MODIFIED_DATE")
    public Date getModified() {
        return super.getModified();
    }

    @Override
    @Transient
    public Long getCreatedId() {
        return super.getCreatedId();
    }

    @Override
    @Transient
    public Long getModifiedId() {
        return super.getModifiedId();
    }

    @Override
    @Transient
    public Serializable getAuditUserId() {
        return super.getAuditUserId();
    }

    @Override
    @Transient
    public Long getRowNum() {
        return super.getRowNum();
    }

    @Override
    @Transient
    public Set getAuditTrail() {
        return super.getAuditTrail();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public void setMsoRequestId(UUID requestId) {
        this.msoRequestId = requestId;
    }

    public void setALaCarte(boolean aLaCarte) {
        this.aLaCarte = aLaCarte;
    }

    public void setJobStatus(Job.JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setStatusModifiedDate(Date statusModifiedDate) {
        this.statusModifiedDate = statusModifiedDate;
    }

    public void setHidden(boolean isHidden) {
        hidden = isHidden;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setRetryEnabled(boolean retryEnabled) {
        this.retryEnabled = retryEnabled;
    }

    public void setOwningEntityId(String owningEntityId) {
        this.owningEntityId = owningEntityId;
    }

    public void setOwningEntityName(String owningEntityName) {
        this.owningEntityName = owningEntityName;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setAicZoneId(String aicZoneId) {
        this.aicZoneId = aicZoneId;
    }

    public void setAicZoneName(String aicZoneName) {
        this.aicZoneName = aicZoneName;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public void setServiceInstanceName(String serviceInstanceName) {
        this.serviceInstanceName = serviceInstanceName;
    }

    public void setServiceModelId(String serviceModelId) {
        this.serviceModelId = serviceModelId;
    }

    public void setServiceModelName(String serviceModelName) {
        this.serviceModelName = serviceModelName;
    }

    public void setServiceModelVersion(String serviceModelVersion) {
        this.serviceModelVersion = serviceModelVersion;
    }

    public void setCreatedBulkDate(Date createdBulkDate) {
        this.createdBulkDate = createdBulkDate;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setAction(ServiceAction action) { this.action = action; }

    public void setRequestSummary(Map<String, Long> requestSummary) {
        this.requestSummary = requestSummary;
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "jobId=" + jobId +
                ", templateId=" + templateId +
                ", userId='" + userId + '\'' +
                ", msoRequestId=" + msoRequestId +
                ", aLaCarte=" + aLaCarte +
                ", jobStatus=" + jobStatus +
                ", statusModifiedDate=" + statusModifiedDate +
                ", hidden=" + hidden +
                ", pause=" + pause +
                ", retryEnabled=" + retryEnabled +
                ", deletedAt=" + deletedAt +
                ", owningEntityId='" + owningEntityId + '\'' +
                ", owningEntityName='" + owningEntityName + '\'' +
                ", project='" + project + '\'' +
                ", aicZoneId='" + aicZoneId + '\'' +
                ", aicZoneName='" + aicZoneName + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", regionId='" + regionId + '\'' +
                ", regionName='" + regionName + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", subscriberName='" + subscriberName + '\'' +
                ", subscriberId='" + subscriberId + '\'' +
                ", serviceInstanceId='" + serviceInstanceId + '\'' +
                ", serviceInstanceName='" + serviceInstanceName + '\'' +
                ", serviceModelId='" + serviceModelId + '\'' +
                ", serviceModelName='" + serviceModelName + '\'' +
                ", serviceModelVersion='" + serviceModelVersion + '\'' +
                ", createdBulkDate=" + createdBulkDate +
                ", action=" + action +
                ", requestSummary=" + requestSummary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceInfo)) return false;
        ServiceInfo that = (ServiceInfo) o;
        return aLaCarte == that.aLaCarte &&
                isHidden() == that.isHidden() &&
                isPause() == that.isPause() &&
                isRetryEnabled() == that.isRetryEnabled() &&
                Objects.equals(getJobId(), that.getJobId()) &&
                Objects.equals(getTemplateId(), that.getTemplateId()) &&
                Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getMsoRequestId(), that.getMsoRequestId()) &&
                getJobStatus() == that.getJobStatus() &&
                Objects.equals(getStatusModifiedDate(), that.getStatusModifiedDate()) &&
                Objects.equals(getDeletedAt(), that.getDeletedAt()) &&
                Objects.equals(getOwningEntityId(), that.getOwningEntityId()) &&
                Objects.equals(getOwningEntityName(), that.getOwningEntityName()) &&
                Objects.equals(getProject(), that.getProject()) &&
                Objects.equals(getAicZoneId(), that.getAicZoneId()) &&
                Objects.equals(getAicZoneName(), that.getAicZoneName()) &&
                Objects.equals(getTenantId(), that.getTenantId()) &&
                Objects.equals(getTenantName(), that.getTenantName()) &&
                Objects.equals(getRegionId(), that.getRegionId()) &&
                Objects.equals(getRegionName(), that.getRegionName()) &&
                Objects.equals(getServiceType(), that.getServiceType()) &&
                Objects.equals(getSubscriberName(), that.getSubscriberName()) &&
                Objects.equals(getSubscriberId(), that.getSubscriberId()) &&
                Objects.equals(getServiceInstanceId(), that.getServiceInstanceId()) &&
                Objects.equals(getServiceInstanceName(), that.getServiceInstanceName()) &&
                Objects.equals(getServiceModelId(), that.getServiceModelId()) &&
                Objects.equals(getServiceModelName(), that.getServiceModelName()) &&
                Objects.equals(getServiceModelVersion(), that.getServiceModelVersion()) &&
                Objects.equals(getCreatedBulkDate(), that.getCreatedBulkDate()) &&
                Objects.equals(getRequestSummary(), that.getRequestSummary()) &&
                getAction() == that.getAction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobId(), getTemplateId(), getUserId(), getMsoRequestId(), aLaCarte, getJobStatus(),
                getStatusModifiedDate(), isHidden(), isPause(), isRetryEnabled(), getDeletedAt(), getOwningEntityId(), getOwningEntityName(),
                getProject(), getAicZoneId(), getAicZoneName(), getTenantId(), getTenantName(), getRegionId(), getRegionName(), getServiceType(),
                getSubscriberName(), getSubscriberId(), getServiceInstanceId(), getServiceInstanceName(), getServiceModelId(), getServiceModelName(),
                getServiceModelVersion(), getCreatedBulkDate(), getAction(), getRequestSummary());
    }
}
