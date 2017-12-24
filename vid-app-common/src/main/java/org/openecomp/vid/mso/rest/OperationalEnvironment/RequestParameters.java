package org.openecomp.vid.mso.rest.OperationalEnvironment;

import org.codehaus.jackson.annotate.JsonProperty;

public class RequestParameters {
    private String operationalEnvironmentType;
    private String tenantContext;
    private String workloadContext;

    public RequestParameters(String operationalEnvironmentType, String tenantContext, String workloadContext) {
        this.operationalEnvironmentType = operationalEnvironmentType;
        this.tenantContext = tenantContext;
        this.workloadContext = workloadContext;
    }

    @JsonProperty("operationalEnvironmentType")
    public String getOperationalEnvironmentType() {
        return operationalEnvironmentType;
    }

    public void setOperationalEnvironmentType(String operationalEnvironmentType) {
        this.operationalEnvironmentType = operationalEnvironmentType;
    }

    @JsonProperty("tenantContext")
    public String getTenantContext() {
        return tenantContext;
    }

    public void setTenantContext(String tenantContext) {
        this.tenantContext = tenantContext;
    }

    @JsonProperty("workloadContext")
    public String getWorkloadContext() {
        return workloadContext;
    }

    public void setWorkloadContext(String workloadContext) {
        this.workloadContext = workloadContext;
    }
}
