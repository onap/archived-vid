package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVnfBase extends PresetMSOBaseCreateInstancePost {
    protected String serviceInstanceId;

    public PresetMSOCreateVnfBase(String requestId, String serviceInstanceId) {
        super(requestId);
        this.serviceInstanceId = serviceInstanceId;
    }

    public PresetMSOCreateVnfBase(String requestId, String serviceInstanceId, String responseInstanceId) {
        super(requestId, responseInstanceId);
        this.serviceInstanceId = serviceInstanceId;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs";
    }
}
