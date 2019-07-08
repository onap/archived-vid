package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSODeleteService extends PresetMSOBaseDelete {
    private final String serviceInstanceId;
    public static final String DEFAULT_SERVICE_INSTANCE_ID = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";

    public PresetMSODeleteService() {
        this(null, null);
    }

    public PresetMSODeleteService(String requestId, String serviceInstanceId) {
        super(requestId);
        this.serviceInstanceId = serviceInstanceId != null ? serviceInstanceId : DEFAULT_SERVICE_INSTANCE_ID;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + serviceInstanceId;
    }

    @Override
    protected String getRootPath() {
        return "/mso/serviceInstantiation/v./serviceInstances/" ;

    }
}
