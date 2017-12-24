package org.openecomp.vid.mso.rest.OperationalEnvironment;


import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationalEnvironmnetPostParameters {
    private String instanceName;
    private String ecompInstanceId;
    private String ecompInstanceName;
    private String operationalEnvironmentType;
    private String tenantContext;
    private String workloadContext;

    public String getInstanceName() {
        return instanceName;
    }

    public String getEcompInstanceId() {
        return ecompInstanceId;
    }

    public String getEcompInstanceName() {
        return ecompInstanceName;
    }

    public String getOperationalEnvironmentType() {
        return operationalEnvironmentType;
    }

    public String getTenantContext() {
        return tenantContext;
    }

    public String getWorkloadContext() {
        return workloadContext;
    }

    public OperationalEnvironmnetPostParameters(@JsonProperty(value = "instanceName", required = true) String instanceName,
                                                @JsonProperty(value = "ecompInstanceId", required = true) String ecompInstanceId,
                                                @JsonProperty(value = "ecompInstanceName", required = true) String ecompInstanceName,
                                                @JsonProperty(value = "operationalEnvironmentType", required = true) String operationalEnvironmentType,
                                                @JsonProperty(value = "tenantContext", required = true) String tenantContext,
                                                @JsonProperty(value = "workloadContext", required = true) String workloadContext) {
        this.instanceName = instanceName;
        this.ecompInstanceId = ecompInstanceId;
        this.ecompInstanceName = ecompInstanceName;
        this.operationalEnvironmentType = operationalEnvironmentType;
        this.tenantContext = tenantContext;
        this.workloadContext = workloadContext;
    }
}
