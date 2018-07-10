 package org.onap.vid.model;


 import org.hibernate.annotations.Type;
 import org.onap.vid.job.Job;
 import org.onap.portalsdk.core.domain.support.DomainVo;

 import javax.persistence.*;
 import java.io.Serializable;
 import java.util.Date;
 import java.util.Objects;
 import java.util.Set;
 import java.util.UUID;

@Entity
@Table(name = "vid_service_info")
public class ServiceInfo extends DomainVo {

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private UUID jobId;
    private UUID templateId;
    private String userId;
    private Job.JobStatus jobStatus;
    private Date statusModifiedDate;
    private boolean hidden;
    private boolean pause;
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
    private String serviceInstanceId;
    private String serviceInstanceName;
    private String serviceModelId;
    private String serviceModelName;
    private String serviceModelVersion;
    private Date createdBulkDate;

    public ServiceInfo(){

    }

    public ServiceInfo(String userId, Job.JobStatus jobStatus, boolean pause, UUID jobId, UUID templateId, String owningEntityId, String owningEntityName, String project, String aicZoneId, String aicZoneName, String tenantId, String tenantName, String regionId, String regionName, String serviceType, String subscriberName, String serviceInstanceId, String serviceInstanceName, String serviceModelId, String serviceModelName, String serviceModelVersion, Date createdBulkDate) {
        this.userId = userId;
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
        this.serviceInstanceId = serviceInstanceId;
        this.serviceInstanceName = serviceInstanceName;
        this.serviceModelId = serviceModelId;
        this.serviceModelName = serviceModelName;
        this.serviceModelVersion = serviceModelVersion;
        this.createdBulkDate = createdBulkDate;
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

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
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


     @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (!(o instanceof ServiceInfo)) return false;
         ServiceInfo that = (ServiceInfo) o;
         return isHidden() == that.isHidden() &&
                 isPause() == that.isPause() &&
                 Objects.equals(getDeletedAt(), that.getDeletedAt()) &&
                 Objects.equals(getJobId(), that.getJobId()) &&
                 Objects.equals(getTemplateId(), that.getTemplateId()) &&
                 Objects.equals(getUserId(), that.getUserId()) &&
                 getJobStatus() == that.getJobStatus() &&
                 Objects.equals(getStatusModifiedDate(), that.getStatusModifiedDate()) &&
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
                 Objects.equals(getServiceInstanceId(), that.getServiceInstanceId()) &&
                 Objects.equals(getServiceInstanceName(), that.getServiceInstanceName()) &&
                 Objects.equals(getServiceModelId(), that.getServiceModelId()) &&
                 Objects.equals(getServiceModelName(), that.getServiceModelName()) &&
                 Objects.equals(getServiceModelVersion(), that.getServiceModelVersion()) &&
                 Objects.equals(getCreatedBulkDate(), that.getCreatedBulkDate());
     }

     @Override
     public int hashCode() {

         return Objects.hash(getJobId(), getTemplateId(), getUserId(), getJobStatus(), getStatusModifiedDate(),
                 isHidden(), isPause(), getDeletedAt(), getOwningEntityId(), getOwningEntityName(), getProject(),
                 getAicZoneId(), getAicZoneName(), getTenantId(), getTenantName(), getRegionId(),
                 getRegionName(), getServiceType(), getSubscriberName(), getServiceInstanceId(),
                 getServiceInstanceName(), getServiceModelId(), getServiceModelName(),
                 getServiceModelVersion(), getCreatedBulkDate());
     }
 }
