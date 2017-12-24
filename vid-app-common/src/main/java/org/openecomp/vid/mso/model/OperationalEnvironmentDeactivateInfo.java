package org.openecomp.vid.mso.model;

import com.google.common.base.MoreObjects;

public class OperationalEnvironmentDeactivateInfo {
    private final String userId;
    private final String operationalEnvironmentId;

    public OperationalEnvironmentDeactivateInfo(String userId, String operationalEnvironmentId) {
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
                .toString();
    }
}
