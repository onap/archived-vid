package org.onap.simulator.presetGenerator.presets.mso.changeManagement;

import org.apache.commons.lang3.StringUtils;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;

public abstract  class PresetMsoChangeManagementBase extends PresetMSOBaseCreateInstancePost {

    protected final String serviceInstanceId;
    protected final String vnfInstanceId;
    private final String actionName;

    public PresetMsoChangeManagementBase(String serviceInstanceId, String vnfInstanceId, String actionName) {
        super(DEFAULT_REQUEST_ID, vnfInstanceId);
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
        this.actionName = actionName;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/"+vnfInstanceId+ getReqPathTrailer();
    }

    protected String getReqPathTrailer() {
        return StringUtils.isEmpty(actionName) ? "" : "/"+ actionName;
    }

}
