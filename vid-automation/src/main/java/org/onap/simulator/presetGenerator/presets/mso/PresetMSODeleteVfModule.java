package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSODeleteVfModule extends PresetMSOBaseDelete {
    private final String serviceInstanceId;
    private final String vnfInstanceId;
    private final String vfModuleInstanceId;
    public static final String DEFAULT_SERVICE_INSTANCE_ID = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    public static final String DEFAULT_VNF_INSTANCE_ID = "c015cc0f-0f37-4488-aabf-53795fd93cd3";
    public static final String DEFAULT_VFMODULE_INSTANCE_ID = "f2805715-e24f-4c8a-9eb3-6c618da7691d";

    public PresetMSODeleteVfModule() {
        this(null, null, null, null);
    }

    public PresetMSODeleteVfModule(String requestId, String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId) {
        super(requestId);
        this.serviceInstanceId = serviceInstanceId != null ? serviceInstanceId : DEFAULT_SERVICE_INSTANCE_ID;
        this.vnfInstanceId = vnfInstanceId != null ? vnfInstanceId : DEFAULT_VNF_INSTANCE_ID;
        this.vfModuleInstanceId = vfModuleInstanceId != null ? vfModuleInstanceId : DEFAULT_VFMODULE_INSTANCE_ID;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + serviceInstanceId + "/vnfs/" + vnfInstanceId + "/vfModules/" + vfModuleInstanceId;
    }
}
