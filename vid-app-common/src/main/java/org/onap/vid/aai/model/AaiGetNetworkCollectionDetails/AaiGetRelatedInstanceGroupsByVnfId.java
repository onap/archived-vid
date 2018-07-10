package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AaiGetRelatedInstanceGroupsByVnfId {

    @JsonProperty("vnf-id")
    private String vnfId;
    @JsonProperty("vnf-name")
    private String vnfName;
    @JsonProperty("vnf-type")
    private String vnfType;
    @JsonProperty("prov-status")
    private String provStatus;
    @JsonProperty("operational-status")
    private String operationalStatus;
    @JsonProperty("equipment-role")
    private String equipmentRole;
    @JsonProperty("in-maint")
    private Boolean inMaint;
    @JsonProperty("is-closed-loop-disabled")
    private Boolean isClosedLoopDisabled;
    @JsonProperty("resource-version")
    private String resourceVersion;
    @JsonProperty("model-invariant-id")
    private String modelInvariantId;
    @JsonProperty("model-version-id")
    private String modelVersionId;
    @JsonProperty("model-customization-id")
    private String modelCustomizationId;
    @JsonProperty("selflink")
    private String selflink;
    @JsonProperty("relationship-list")
    private RelationshipList relationshipList;


    @JsonProperty("vnf-id")
    public String getVnfId() {
        return vnfId;
    }

    @JsonProperty("vnf-id")
    public void setVnfId(String vnfId) {
        this.vnfId = vnfId;
    }

    @JsonProperty("vnf-name")
    public String getVnfName() {
        return vnfName;
    }

    @JsonProperty("vnf-name")
    public void setVnfName(String vnfName) {
        this.vnfName = vnfName;
    }

    @JsonProperty("vnf-type")
    public String getVnfType() {
        return vnfType;
    }

    @JsonProperty("vnf-type")
    public void setVnfType(String vnfType) {
        this.vnfType = vnfType;
    }

    @JsonProperty("prov-status")
    public String getProvStatus() {
        return provStatus;
    }

    @JsonProperty("prov-status")
    public void setProvStatus(String provStatus) {
        this.provStatus = provStatus;
    }

    @JsonProperty("operational-status")
    public String getOperationalStatus() {
        return operationalStatus;
    }

    @JsonProperty("operational-status")
    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    @JsonProperty("equipment-role")
    public String getEquipmentRole() {
        return equipmentRole;
    }

    @JsonProperty("equipment-role")
    public void setEquipmentRole(String equipmentRole) {
        this.equipmentRole = equipmentRole;
    }

    @JsonProperty("in-maint")
    public Boolean getInMaint() {
        return inMaint;
    }

    @JsonProperty("in-maint")
    public void setInMaint(Boolean inMaint) {
        this.inMaint = inMaint;
    }

    @JsonProperty("is-closed-loop-disabled")
    public Boolean getIsClosedLoopDisabled() {
        return isClosedLoopDisabled;
    }

    @JsonProperty("is-closed-loop-disabled")
    public void setIsClosedLoopDisabled(Boolean isClosedLoopDisabled) {
        this.isClosedLoopDisabled = isClosedLoopDisabled;
    }

    @JsonProperty("resource-version")
    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @JsonProperty("model-invariant-id")
    public String getModelInvariantId() {
        return modelInvariantId;
    }

    @JsonProperty("model-invariant-id")
    public void setModelInvariantId(String modelInvariantId) {
        this.modelInvariantId = modelInvariantId;
    }

    @JsonProperty("model-version-id")
    public String getModelVersionId() {
        return modelVersionId;
    }

    @JsonProperty("model-version-id")
    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    @JsonProperty("model-customization-id")
    public String getModelCustomizationId() {
        return modelCustomizationId;
    }

    @JsonProperty("model-customization-id")
    public void setModelCustomizationId(String modelCustomizationId) {
        this.modelCustomizationId = modelCustomizationId;
    }

    @JsonProperty("selflink")
    public String getSelflink() {
        return selflink;
    }

    @JsonProperty("selflink")
    public void setSelflink(String selflink) {
        this.selflink = selflink;
    }

    @JsonProperty("relationship-list")
    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonProperty("relationship-list")
    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }


}
