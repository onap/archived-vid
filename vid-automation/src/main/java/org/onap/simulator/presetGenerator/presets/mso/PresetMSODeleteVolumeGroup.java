package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSODeleteVolumeGroup extends PresetMSOBaseDeleteWithCloudConfiguration {
    private final String serviceInstanceId;
    private final String vnfInstanceId;
    private final String volumeGroupInstanceId;
    public static final String DEFAULT_SERVICE_INSTANCE_ID = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    public static final String DEFAULT_VNF_INSTANCE_ID = "c015cc0f-0f37-4488-aabf-53795fd93cd3";
    public static final String DEFAULT_VG_INSTANCE_ID = "d9db0900-31f6-4880-8658-8c996c6123b4";

    public PresetMSODeleteVolumeGroup() {
        this(null, null, null, null);
    }

    public PresetMSODeleteVolumeGroup(String requestId, String serviceInstanceId, String vnfInstanceId, String volumeGroupInstanceId) {
        super(requestId, "volumeGroup");
        this.serviceInstanceId = serviceInstanceId != null ? serviceInstanceId : DEFAULT_SERVICE_INSTANCE_ID;
        this.vnfInstanceId = vnfInstanceId != null ? vnfInstanceId : DEFAULT_VNF_INSTANCE_ID;
        this.volumeGroupInstanceId = volumeGroupInstanceId != null ? volumeGroupInstanceId : DEFAULT_VG_INSTANCE_ID;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + serviceInstanceId + "/vnfs/" + vnfInstanceId + "/volumeGroups/" + volumeGroupInstanceId;
    }

}
