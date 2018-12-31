package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class VolumeGroupRequestDetails extends BaseResourceInstantiationRequestDetails {

    public VolumeGroupRequestDetails(
            @JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
            @JsonProperty(value = "cloudConfiguration", required = true) CloudConfiguration cloudConfiguration,
            @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
            @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
            @JsonProperty(value = "requestParameters", required = true) VfModuleInstantiationRequestDetails.RequestParametersVfModule requestParameters)
    {
        super(modelInfo, cloudConfiguration, requestInfo, relatedInstanceList, requestParameters);
    }
}
