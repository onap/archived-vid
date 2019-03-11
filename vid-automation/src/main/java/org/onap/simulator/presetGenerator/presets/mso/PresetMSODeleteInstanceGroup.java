package org.onap.simulator.presetGenerator.presets.mso;


import java.util.Map;

public class PresetMSODeleteInstanceGroup extends PresetMSOBaseDelete {

    private final String userId;

    public PresetMSODeleteInstanceGroup(String requestId, String instanceId, String userId) {
        super(requestId, instanceId);
        this.userId = userId;
    }

    @Override
    public String getReqPath() {
        return "/mso/serviceInstantiation/v./instanceGroups/"+instanceId;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> map = super.getRequestHeaders();
        map.put("X-RequestorID", userId);
        return map;

    }

}
