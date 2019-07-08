package org.onap.simulator.presetGenerator.presets.aai;

public class Placement {
    public final String cloudOwner;
    public final String lcpRegionId;
    public final String tenantId;

    public Placement(String cloudOwner, String lcpRegionId, String tenantId) {
        this.cloudOwner = cloudOwner;
        this.lcpRegionId = lcpRegionId;
        this.tenantId = tenantId;
    }
}

