package org.onap.vid.model;

public class VfcInstanceGroup {

    private String uuid;
    private String invariantUuid;
    private String name;
    private String version;
    private VfcInstanceGroupProperties vfcInstanceGroupProperties;

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

    public VfcInstanceGroupProperties getVfcInstanceGroupProperties() {
        return vfcInstanceGroupProperties;
    }

    public void setVfcInstanceGroupProperties(VfcInstanceGroupProperties vfcInstanceGroupProperties) {
        this.vfcInstanceGroupProperties = vfcInstanceGroupProperties;
    }





}
