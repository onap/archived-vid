package org.onap.vid.changeManagement;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "instanceId",
        "modelInfo"
})
public class RelatedInstance {

    @JsonProperty("instanceId")
    public String instanceId;


    @JsonProperty("modelInfo")
    public ModelInfo modelInfo;

    @JsonGetter
    public String getInstanceId() {
        return instanceId;
    }

    @JsonSetter
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

}
