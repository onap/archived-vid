package org.onap.vid.model.mso;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalEnvironmentList {

    @JsonProperty("operational-environment")
    public List<OperationalEnvironment> getOperationalEnvironment() {
        return operationalEnvironment;
    }

    @JsonProperty("operational-environment")
    public void setOperationalEnvironment(List<OperationalEnvironment> operationalEnvironment) {
        this.operationalEnvironment = operationalEnvironment;
    }

    public OperationalEnvironmentList() {
    }

    public OperationalEnvironmentList(List<OperationalEnvironment> operationalEnvironment) {
        this.operationalEnvironment = operationalEnvironment;
    }

    private List<OperationalEnvironment> operationalEnvironment;
}
