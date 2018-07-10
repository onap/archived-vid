package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Network {
    @JsonProperty("network-id")
    private String networkId;
    @JsonProperty("network-name")
    private String networkName;
    @JsonProperty("network-type")
    private String networkType;
    @JsonProperty("network-role")
    private String networkRole;
    @JsonProperty("network-technology")
    private String networkTechnology;
    @JsonProperty("is-bound-to-vpn")
    private Boolean isBoundToVpn;
    @JsonProperty("resource-version")
    private String resourceVersion;
    @JsonProperty("is-provider-network")
    private Boolean isProviderNetwork;
    @JsonProperty("is-shared-network")
    private Boolean isSharedNetwork;
    @JsonProperty("is-external-network")
    private Boolean isExternalNetwork;
    @JsonProperty("relationship-list")
    private RelationshipList relationshipList;


    @JsonProperty("network-id")
    public String getNetworkId() {
        return networkId;
    }

    @JsonProperty("network-id")
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    @JsonProperty("network-name")
    public String getNetworkName() {
        return networkName;
    }

    @JsonProperty("network-name")
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    @JsonProperty("network-type")
    public String getNetworkType() {
        return networkType;
    }

    @JsonProperty("network-type")
    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    @JsonProperty("network-role")
    public String getNetworkRole() {
        return networkRole;
    }

    @JsonProperty("network-role")
    public void setNetworkRole(String networkRole) {
        this.networkRole = networkRole;
    }

    @JsonProperty("network-technology")
    public String getNetworkTechnology() {
        return networkTechnology;
    }

    @JsonProperty("network-technology")
    public void setNetworkTechnology(String networkTechnology) {
        this.networkTechnology = networkTechnology;
    }

    @JsonProperty("is-bound-to-vpn")
    public Boolean getIsBoundToVpn() {
        return isBoundToVpn;
    }

    @JsonProperty("is-bound-to-vpn")
    public void setIsBoundToVpn(Boolean isBoundToVpn) {
        this.isBoundToVpn = isBoundToVpn;
    }

    @JsonProperty("resource-version")
    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @JsonProperty("is-provider-network")
    public Boolean getIsProviderNetwork() {
        return isProviderNetwork;
    }

    @JsonProperty("is-provider-network")
    public void setIsProviderNetwork(Boolean isProviderNetwork) {
        this.isProviderNetwork = isProviderNetwork;
    }

    @JsonProperty("is-shared-network")
    public Boolean getIsSharedNetwork() {
        return isSharedNetwork;
    }

    @JsonProperty("is-shared-network")
    public void setIsSharedNetwork(Boolean isSharedNetwork) {
        this.isSharedNetwork = isSharedNetwork;
    }

    @JsonProperty("is-external-network")
    public Boolean getIsExternalNetwork() {
        return isExternalNetwork;
    }

    @JsonProperty("is-external-network")
    public void setIsExternalNetwork(Boolean isExternalNetwork) {
        this.isExternalNetwork = isExternalNetwork;
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
