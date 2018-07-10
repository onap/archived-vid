package org.onap.vid.model.serviceInstantiation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.domain.mso.ModelInfo;
import org.onap.vid.job.JobAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServiceInstantiation implements JobAdapter.AsyncJobRequest {

    private final ModelInfo modelInfo;

    private final String owningEntityId;

    private final String owningEntityName;

    private final String projectName;

    private final String subscriberName;

    private final String globalSubscriberId;

    private final String productFamilyId;

    private final String instanceName;

    private final Boolean isUserProvidedNaming;

    private final String subscriptionServiceType;

    private final String lcpCloudRegionId;

    private final String tenantId;

    private final String tenantName;

    private final String aicZoneId;

    private final String aicZoneName;

    private final Map<String, Vnf> vnfs;

    private final List<Map<String,String>> instanceParams;

    private final boolean isPause;

    private final int bulkSize;

    private final boolean rollbackOnFailure;

    public ServiceInstantiation(@JsonProperty("modelInfo") ModelInfo modelInfo,
                                @JsonProperty("owningEntityId") String owningEntityId,
                                @JsonProperty("owningEntityName") String owningEntityName,
                                @JsonProperty("projectName") String projectName,
                                @JsonProperty("globalSubscriberId") String globalSubscriberId,
                                @JsonProperty("subscriberName") String subscriberName,
                                @JsonProperty("productFamilyId") String productFamilyId,
                                @JsonProperty("instanceName") String instanceName,
                                @JsonProperty("isUserProvidedNaming") Boolean isUserProvidedNaming,
                                @JsonProperty("subscriptionServiceType") String subscriptionServiceType,
                                @JsonProperty("lcpCloudRegionId") String lcpCloudRegionId,
                                @JsonProperty("tenantId") String tenantId,
                                @JsonProperty("tenantName") String tenantName,
                                @JsonProperty("aicZoneId") String aicZoneId,
                                @JsonProperty("aicZoneName") String aicZoneName,
                                @JsonProperty("vnfs") Map<String, Vnf> vnfs,
                                @JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
                                @JsonProperty("pause") boolean isPause,
                                @JsonProperty("bulkSize") int bulkSize,
                                @JsonProperty("rollbackOnFailure") boolean rollbackOnFailure
                               ) {

        this.modelInfo = modelInfo;
        this.modelInfo.setModelType("service");
        this.owningEntityId = owningEntityId;
        this.owningEntityName = owningEntityName;
        this.projectName = projectName;
        this.globalSubscriberId = globalSubscriberId;
        this.subscriberName = subscriberName;
        this.productFamilyId = productFamilyId;
        this.instanceName = instanceName;
        this.isUserProvidedNaming = isUserProvidedNaming;
        this.subscriptionServiceType = subscriptionServiceType;
        this.lcpCloudRegionId = lcpCloudRegionId;
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.aicZoneId = aicZoneId;
        this.aicZoneName = aicZoneName;
        this.vnfs = vnfs;
        this.instanceParams = instanceParams;
        this.isPause = isPause;
        this.bulkSize = bulkSize;
        this.rollbackOnFailure = rollbackOnFailure;
    }

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public String getOwningEntityId() {
        return owningEntityId;
    }

    public String getOwningEntityName() {
        return owningEntityName;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getGlobalSubscriberId() {
        return globalSubscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public String getProductFamilyId() {
        return productFamilyId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    @JsonProperty("isUserProvidedNaming")
    public Boolean isUserProvidedNaming() { return isUserProvidedNaming; }

    public String getSubscriptionServiceType() {
        return subscriptionServiceType;
    }

    public String getLcpCloudRegionId() {
        return lcpCloudRegionId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getAicZoneId() {
        return aicZoneId;
    }

    public String getAicZoneName() {
        return aicZoneName;
    }

    public Map<String, Vnf> getVnfs() {
        return vnfs;
    }

    public List<Map<String, String>> getInstanceParams() {
        return instanceParams == null ? Collections.emptyList() : instanceParams;
    }

    public boolean isPause() {
        return isPause;
    }

    public int getBulkSize() { return bulkSize; }

    public boolean isRollbackOnFailure() {
        return rollbackOnFailure;
    }
}