package org.onap.vid.model;

public class NetworkCollection {

    private String uuid;

    private String invariantUuid;

    private String name;

    private String version;

    private NetworkCollectionProperties networkCollectionProperties;

    public NetworkCollection(){
        this.networkCollectionProperties = new NetworkCollectionProperties();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getInvariantUuid() {
        return invariantUuid;
    }

    public void setInvariantUuid(String invariantUuid) {
        this.invariantUuid = invariantUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public NetworkCollectionProperties getNetworkCollectionProperties() {
        return networkCollectionProperties;
    }

    public void setNetworkCollectionProperties(NetworkCollectionProperties networkCollectionProperties) {
        this.networkCollectionProperties = networkCollectionProperties;
    }

}
