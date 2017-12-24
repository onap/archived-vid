package org.openecomp.vid.mso.rest.OperationalEnvironment;

import org.codehaus.jackson.annotate.JsonProperty;

public class RequestInfo {
    private String resourceType;
    private String instanceName;
    private String source;
    private String requestorId;

    public RequestInfo(String resourceType, String instanceName, String source, String requestorId) {
        this.resourceType = resourceType;
        this.instanceName = instanceName;
        this.source = source;
        this.requestorId = requestorId;
    }

    @JsonProperty("resourceType")
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @JsonProperty("instanceName")
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("requestorId")
    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }
}
