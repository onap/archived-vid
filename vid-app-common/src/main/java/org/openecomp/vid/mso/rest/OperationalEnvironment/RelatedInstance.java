package org.openecomp.vid.mso.rest.OperationalEnvironment;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedInstance {
    private String resourceType;
    private String instanceId;
    private String instanceName;

    public RelatedInstance(String resourceType, String instanceId, String instanceName) {
        this.resourceType = resourceType;
        this.instanceId = instanceId;
        this.instanceName = instanceName;
    }

    @JsonProperty("resourceType")
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @JsonProperty("instanceId")
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @JsonProperty("instanceName")
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
