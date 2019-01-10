package org.onap.vid.model.aaiTree;

import org.onap.vid.model.Action;
import org.onap.vid.mso.model.ModelInfo;

public abstract class AbstractNode {

    protected final Action action;
    protected String instanceName;
    protected String instanceId;
    protected String orchStatus;
    protected String productFamilyId;
    protected String lcpCloudRegionId;
    protected String tenantId;
    protected ModelInfo modelInfo;

    public AbstractNode() {
        this.action = Action.None;
    }

    public final Action getAction() {
        return action;
    }

    public final String getInstanceName() {
        return instanceName;
    }

    public final String getInstanceId() {
        return instanceId;
    }

    public final String getOrchStatus() {
        return orchStatus;
    }

    public final String getProductFamilyId() {
        return productFamilyId;
    }

    public final String getLcpCloudRegionId() {
        return lcpCloudRegionId;
    }

    public final String getTenantId() {
        return tenantId;
    }

    public final ModelInfo getModelInfo() {
        return modelInfo;
    }

}
