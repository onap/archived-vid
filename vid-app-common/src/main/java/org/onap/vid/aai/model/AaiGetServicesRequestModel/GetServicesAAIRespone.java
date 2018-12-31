package org.onap.vid.aai.model.AaiGetServicesRequestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetServicesAAIRespone {

    public List<Service> service;
}
