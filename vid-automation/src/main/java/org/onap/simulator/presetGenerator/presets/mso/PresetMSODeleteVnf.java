package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSODeleteVnf extends PresetMSOBaseDelete {
    private final String serviceInstanceId;
    private final String vnfInstanceId;
    public static final String DEFAULT_SERVICE_INSTANCE_ID = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    public static final String DEFAULT_VNF_INSTANCE_ID = "9908b762-136f-4b1f-8eb4-ef670ef58bb4";

    public PresetMSODeleteVnf() {
        this(null, null, null);
    }

    public PresetMSODeleteVnf(String requestId, String serviceInstanceId, String vnfInstanceId) {
        super(requestId);
        this.serviceInstanceId = serviceInstanceId != null ? serviceInstanceId : DEFAULT_SERVICE_INSTANCE_ID;
        this.vnfInstanceId = vnfInstanceId != null ? vnfInstanceId : DEFAULT_VNF_INSTANCE_ID;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + serviceInstanceId + "/vnfs/" + vnfInstanceId;
    }
}
