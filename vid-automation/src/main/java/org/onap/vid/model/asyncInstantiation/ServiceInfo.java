package org.onap.vid.model.asyncInstantiation;


import java.util.Date;
import vid.automation.test.model.JobStatus;
import vid.automation.test.model.ServiceAction;

public class ServiceInfo {

    public String jobId;
    public String templateId;
    public String userId;
    public JobStatus jobStatus;
    public Date statusModifiedDate;
    public boolean hidden;
    public boolean pause;
    public String owningEntityId;
    public String owningEntityName;
    public String project;
    public String aicZoneId;
    public String aicZoneName;
    public String tenantId;
    public String tenantName;
    public String regionId;
    public String regionName;
    public String serviceType;
    public String subscriberName;
    public String serviceInstanceId;
    public String serviceInstanceName;
    public String serviceModelId;
    public String serviceModelName;
    public String serviceModelVersion;
    public Date createdBulkDate;
    public ServiceAction action;
    public boolean isRetryEnabled;

    public ServiceInfo(){

    }

    public ServiceInfo(String userId, JobStatus jobStatus, boolean pause, String owningEntityId, String owningEntityName, String project, String aicZoneId, String aicZoneName, String tenantId, String tenantName, String regionId, String regionName, String serviceType, String subscriberName, String serviceInstanceId, String serviceInstanceName, String serviceModelId, String serviceModelName, String serviceModelVersion, String jobId, String templateId, ServiceAction action, boolean isRetryEnabled) {
        this.userId = userId;
        this.jobStatus = jobStatus;
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
        this.jobId = jobId;
        this.templateId = templateId;
        this.action = action;
        this.isRetryEnabled = isRetryEnabled;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    public String getJobId() {
        return jobId;
    }

}
