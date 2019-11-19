package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVfModuleBase extends PresetMSOBaseCreateInstancePost {

    protected String serviceInstanceId;
    protected String vnfInstanceId;

    public PresetMSOCreateVfModuleBase(String requestId, String serviceInstanceId, String vnfInstanceId) {
        super(requestId);
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/" + vnfInstanceId + "/vfModules";
    }
}
