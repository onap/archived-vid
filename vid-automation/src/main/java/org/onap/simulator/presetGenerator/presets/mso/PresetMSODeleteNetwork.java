package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSODeleteNetwork extends PresetMSOBaseDelete {
    private final String serviceInstanceId;
    private final String networkInstanceId;
    public static final String DEFAULT_SERVICE_INSTANCE_ID = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    public static final String DEFAULT_NETWORK_INSTANCE_ID = "6adc05e4-85c6-4f91-aa5a-1eb9546c4369";

    public PresetMSODeleteNetwork() {
        this(null, null, null);
    }

    public PresetMSODeleteNetwork(String requestId, String serviceInstanceId, String networkInstanceId) {
        super(requestId);
        this.serviceInstanceId = serviceInstanceId != null ? serviceInstanceId : DEFAULT_SERVICE_INSTANCE_ID;
        this.networkInstanceId = networkInstanceId != null ? networkInstanceId : DEFAULT_NETWORK_INSTANCE_ID;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + serviceInstanceId + "/networks/" + networkInstanceId;
    }
}
