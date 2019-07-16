package org.onap.vid.model.aaiTree;

import org.onap.vid.mso.model.ModelInfo;

import java.util.HashMap;
import java.util.Map;

public class ServiceInstance extends AbstractNode {

    private String globalSubscriberId;
    private String subscriptionServiceType;

    private String owningEntityId;

    private String owningEntityName;
    private String tenantName;
    private String aicZoneId;
    private String aicZoneName;
    private String projectName;

    private String rollbackOnFailure;
    private boolean isALaCarte;

    private Map<String, Vnf> vnfs = new HashMap<>();
    private Map<String, Network> networks = new HashMap<>();
    private Map<String, Vrf> vrfs = new HashMap<>();


    private Map<String, VnfGroup> vnfGroups = new HashMap<>();
    private Map<String, CollectionResource> collectionResources = new HashMap<>();

    private int validationCounter;
    private Map<String, Long> existingVNFCounterMap;
    private Map<String, Long> existingNetworksCounterMap;
    private Map<String, Long> existingVnfGroupCounterMap;
    private Map<String, Long> existingVRFCounterMap;

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setOrchStatus(String orchStatus) {
        this.orchStatus = orchStatus;
    }

    public String getGlobalSubscriberId() {
        return globalSubscriberId;
    }

    public void setGlobalSubscriberId(String globalSubscriberId) {
        this.globalSubscriberId = globalSubscriberId;
    }

    public String getSubscriptionServiceType() {
        return subscriptionServiceType;
    }

    public void setSubscriptionServiceType(String subscriptionServiceType) {
        this.subscriptionServiceType = subscriptionServiceType;
    }

    public String getOwningEntityId() {
        return owningEntityId;
    }

    public void setOwningEntityId(String owningEntityId) {
        this.owningEntityId = owningEntityId;
    }

    public String getOwningEntityName() {
        return owningEntityName;
    }

    public void setOwningEntityName(String owningEntityName) {
        this.owningEntityName = owningEntityName;
    }

    public void setProductFamilyId(String productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    public void setLcpCloudRegionId(String lcpCloudRegionId) {
        this.lcpCloudRegionId = lcpCloudRegionId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAicZoneId() {
        return aicZoneId;
    }

    public void setAicZoneId(String aicZoneId) {
        this.aicZoneId = aicZoneId;
    }

    public String getAicZoneName() {
        return aicZoneName;
    }

    public void setAicZoneName(String aicZoneName) {
        this.aicZoneName = aicZoneName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRollbackOnFailure() {
        return rollbackOnFailure;
    }

    public void setRollbackOnFailure(String rollbackOnFailure) {
        this.rollbackOnFailure = rollbackOnFailure;
    }

    public boolean getIsALaCarte() {
        return isALaCarte;
    }

    public void setIsALaCarte(boolean isALaCarte) {
        this.isALaCarte = isALaCarte;
    }

    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public Map<String, Vnf> getVnfs() {
        return vnfs;
    }

    public void setVnfs(Map<String, Vnf> vnfs) {
        this.vnfs = vnfs;
    }

    public Map<String, Network> getNetworks() {
        return networks;
    }

    public void setNetworks(Map<String, Network> networks) {
        this.networks = networks;
    }

    public Map<String, CollectionResource> getCollectionResources() {
        return collectionResources;
    }

    public void setCollectionResources(Map<String, CollectionResource> collectionResources) {
        this.collectionResources = collectionResources;
    }

    public Map<String, VnfGroup> getVnfGroups() { return vnfGroups; }

    public void setVnfGroups(Map<String, VnfGroup> vnfGroups) { this.vnfGroups = vnfGroups; }

    public int getValidationCounter() {
        return validationCounter;
    }

    public void setValidationCounter(int validationCounter) {
        this.validationCounter = validationCounter;
    }

    public Map<String, Long> getExistingVNFCounterMap() {
        return existingVNFCounterMap;
    }

    public void setExistingVNFCounterMap(Map<String, Long> existingVNFCounterMap) {
        this.existingVNFCounterMap = existingVNFCounterMap;
    }

    public Map<String, Long> getExistingNetworksCounterMap() {
        return existingNetworksCounterMap;
    }

    public void setExistingNetworksCounterMap(Map<String, Long> existingNetworksCounterMap) {
        this.existingNetworksCounterMap = existingNetworksCounterMap;
    }

    public Map<String, Long> getExistingVnfGroupCounterMap() {
        return existingVnfGroupCounterMap;
    }

    public void setExistingVnfGroupCounterMap(Map<String, Long> existingVnfGroupCounterMap) {
        this.existingVnfGroupCounterMap = existingVnfGroupCounterMap;
    }

    public Map<String, Vrf> getVrfs() {
        return vrfs;
    }

    public void setVrfs(Map<String, Vrf> vrfs) {
        this.vrfs = vrfs;
    }

    public Map<String, Long> getExistingVRFCounterMap() {
        return existingVRFCounterMap;
    }

    public void setExistingVRFCounterMap(Map<String, Long> existingVRFCounterMap) {
        this.existingVRFCounterMap = existingVRFCounterMap;
    }
}
