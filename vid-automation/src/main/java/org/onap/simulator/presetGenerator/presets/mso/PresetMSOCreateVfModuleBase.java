package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVfModuleBase extends PresetMSOBaseCreateInstancePost {

    private final String resourceType;
    protected String serviceInstanceId;
    protected String vnfInstanceId;

    public PresetMSOCreateVfModuleBase(String requestId, String responseInstanceId, String serviceInstanceId, String vnfInstanceId, String resourceType) {
        super(requestId, responseInstanceId);
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
        this.resourceType = resourceType;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + String.format("/serviceInstantiation/v./serviceInstances/%s/vnfs/%s/%ss",serviceInstanceId, vnfInstanceId, resourceType);
    }
}
