package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.InstanceGroup;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstanceGroupWrapper {

    private InstanceGroup instanceGroup;

    @JsonProperty("instance-group")
    public InstanceGroup getInstanceGroup() {
        return instanceGroup;
    }
    @JsonProperty("instance-group")
    public void setInstanceGroup(InstanceGroup instanceGroup) {
        this.instanceGroup = instanceGroup;
    }
}
