package org.onap.vid.aai.model.AaiGetServicesRequestModel;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetServicesAAIRespone {

    public List<Service> service;
}
