package org.onap.vid.model.mso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalEnvironmentList {

    public List<OperationalEnvironment> getOperationalEnvironment() {
        return operationalEnvironment;
    }

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
