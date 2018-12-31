package org.onap.vid.aai.model.AaiGetOperationalEnvironments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.OperationalEnvironment;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalEnvironmentList {

    public List<OperationalEnvironment> getOperationalEnvironment() {
        return operationalEnvironment;
    }

    @JsonProperty("operational-environment")
    public void setJsonOperationalEnvironment(List<OperationalEnvironment> operationalEnvironment) {
        this.operationalEnvironment = operationalEnvironment;
    }

    public OperationalEnvironmentList() {
    }

    public OperationalEnvironmentList(List<OperationalEnvironment> operationalEnvironment) {
        this.operationalEnvironment = operationalEnvironment;
    }

    private List<OperationalEnvironment> operationalEnvironment;
}
