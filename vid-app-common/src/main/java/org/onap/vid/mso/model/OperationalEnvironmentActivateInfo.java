package org.onap.vid.mso.model;

import org.onap.vid.controllers.OperationalEnvironmentController;

import com.google.common.base.MoreObjects;

public class OperationalEnvironmentActivateInfo extends OperationalEnvironmentController.OperationalEnvironmentActivateBody {
    private final String userId;
    private final String operationalEnvironmentId;

    public OperationalEnvironmentActivateInfo(OperationalEnvironmentController.OperationalEnvironmentActivateBody o, String userId, String operationalEnvironmentId) {
        super(o.getRelatedInstanceId(), o.getRelatedInstanceName(), o.getWorkloadContext(), o.getManifest());

        this.userId = userId;
        this.operationalEnvironmentId = operationalEnvironmentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOperationalEnvironmentId() {
        return operationalEnvironmentId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("operationalEnvironmentId", operationalEnvironmentId)
                .add("userId", userId)
                .add("super", super.toString())
                .toString();
    }
}
