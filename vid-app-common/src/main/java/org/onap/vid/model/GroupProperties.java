package org.onap.vid.model;

public class GroupProperties {
    private Integer minCountInstances;
    private Integer maxCountInstances;
    private Integer initialCount;
    private String vfModuleLabel;
    private boolean baseModule = false;

    public String getVfModuleLabel() {
        return vfModuleLabel;
    }

    public void setVfModuleLabel(String vfModuleLabel) {
        this.vfModuleLabel = vfModuleLabel;
    }

    public Integer getMinCountInstances() {
        return minCountInstances;
    }

    public void setMinCountInstances(Integer minCountInstances) {
        this.minCountInstances = minCountInstances;
    }

    public Integer getMaxCountInstances() {
        return maxCountInstances;
    }

    public void setMaxCountInstances(Integer maxCountInstances) {
        this.maxCountInstances = maxCountInstances;
    }

    public boolean getBaseModule() {
        return baseModule;
    }

    public void setBaseModule(boolean baseModule) {
        this.baseModule = baseModule;
    }

    public Integer getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(Integer initialCount) {
        this.initialCount = initialCount;
    }
}
