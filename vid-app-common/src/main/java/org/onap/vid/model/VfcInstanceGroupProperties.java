package org.onap.vid.model;

public class VfcInstanceGroupProperties {

    private String vfcParentPortRole;
    private String networkCollectionFunction;
    private String vfcInstanceGroupFunction;
    private String subinterfaceRole;

    public String getVfcParentPortRole() {
        return vfcParentPortRole;
    }

    public void setVfcParentPortRole(String vfcParentPortRole) {
        this.vfcParentPortRole = vfcParentPortRole;
    }

    public String getNetworkCollectionFunction() {
        return networkCollectionFunction;
    }

    public void setNetworkCollectionFunction(String networkCollectionFunction) {
        this.networkCollectionFunction = networkCollectionFunction;
    }

    public String getVfcInstanceGroupFunction() {
        return vfcInstanceGroupFunction;
    }

    public void setVfcInstanceGroupFunction(String vfcInstanceGroupFunction) {
        this.vfcInstanceGroupFunction = vfcInstanceGroupFunction;
    }

    public String getSubinterfaceRole() {
        return subinterfaceRole;
    }

    public void setSubinterfaceRole(String subinterfaceRole) {
        this.subinterfaceRole = subinterfaceRole;
    }



}
